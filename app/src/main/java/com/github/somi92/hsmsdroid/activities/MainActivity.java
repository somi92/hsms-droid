package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSListTask;
import com.github.somi92.hsmsdroid.util.HSMSConstants;
import com.github.somi92.hsmsdroid.util.HSMSListAdapter;
import com.github.somi92.hsmsdroid.util.HSMSTaskExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.APP_FIRST_RUN;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.DEFAULT_IP;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.PREF_FILE;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.SERVICE_IP_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_DATA_ENABLED_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_EMAIL_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_NAME_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.VALID_EMAIL_REGEX;

public class MainActivity extends Activity implements HSMSListTask.HSMSListEventListener {

    private ProgressDialog mProgressDialog;
    private ListView mHSMSListView;
    private SwipeRefreshLayout mSwipeLayout;
    private SharedPreferences mPrefs;
    private HSMSEntity[] mSourceEntities;
    private ArrayList<HSMSEntity> mCurrentEntities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, PREF_FILE, MODE_PRIVATE, R.xml.preferences, false);
        mPrefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE);

        boolean isFirstRun = mPrefs.getBoolean(APP_FIRST_RUN, true);
        if(isFirstRun) {
            SharedPreferences.Editor editor = mPrefs.edit();
            editor.putBoolean(APP_FIRST_RUN, false);
            editor.apply();
            showFirstRunDialog();
        }

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_dark);

        loadList();
        handleSearchIntent(getIntent());
    }

    private void showFirstRunDialog() {

        final AlertDialog.Builder firstRunDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View view = getLayoutInflater().inflate(R.layout.first_run_dialog, null);
        firstRunDialogBuilder.setView(view);

        firstRunDialogBuilder.setCancelable(false);

        final AlertDialog firstRunDialog = firstRunDialogBuilder.create();

        final EditText userEmail = (EditText) view.findViewById(R.id.user_email_fr);
        final EditText userName = (EditText) view.findViewById(R.id.user_name_fr);
        final Button saveButton = (Button) view.findViewById(R.id.button_save_user_data);
        final Button skipButton = (Button) view.findViewById(R.id.button_skip_user_data);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPrefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
                String url = mPrefs.getString(SERVICE_IP_PREF, DEFAULT_IP);
                String email = userEmail.getText().toString();
                String name = userName.getText().toString();
                String method = "registerDonator";
                if(email.matches(VALID_EMAIL_REGEX)) {
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putBoolean(USER_DATA_ENABLED_PREF, true);
                    editor.putString(USER_EMAIL_PREF, email);
                    editor.putString(USER_NAME_PREF, name);
                    editor.apply();
                    String[] data = {url, email, name, method};
                    HSMSTaskExecutor.getInstance().setupUserRegistration(MainActivity.this, data);
                    HSMSTaskExecutor.getInstance().registerUser(true);
                    firstRunDialog.cancel();
                } else {
                    Toast.makeText(MainActivity.this, "Greška! E-mail nije validan. Pokušajte ponovo.", Toast.LENGTH_LONG).show();
                }
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstRunDialog.cancel();
            }
        });

        firstRunDialog.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearchIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_dialog_button).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        if (id == R.id.search_dialog_button) {
            if(mSourceEntities == null || mSourceEntities.length == 0){
                Toast.makeText(this, "Nema preuzetih humanitarnih akcija.", Toast.LENGTH_LONG).show();
            } else {
                onSearchRequested();
            }
        }
        if(id == R.id.action_refresh) {
            loadList();
        }
        if(id == R.id.stats_button) {
            startActivity(new Intent(this, StatsActivity.class));
        }
        if(id == R.id.about_button) {
            startActivity(new Intent(this, AboutActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHSMSListTaskStarted() {
        if(mSwipeLayout != null && !mSwipeLayout.isRefreshing()) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(true);
            mProgressDialog.setMessage("Molimo sačekajte...");
            mProgressDialog.show();
        }
    }

    @Override
    public void onHSMSListReceived(HSMSEntity[] entities) {
        if(entities == null || entities.length<1) {
            Toast.makeText(this, "Nije pronađena nijedna humanitarna akcija.", Toast.LENGTH_LONG).show();
            return;
        }
        mSourceEntities = entities;
        mCurrentEntities = new ArrayList<>(Arrays.asList(mSourceEntities));
        mHSMSListView = (ListView) findViewById(R.id.hsmsListView);
        ArrayList<HSMSEntity> entitiesList = new ArrayList<>(Arrays.asList(entities));
        setEntitiesList(entitiesList);
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        Toast.makeText(this, "Broj preuzetih humanitarnih akcija: "+entitiesList.size(), Toast.LENGTH_LONG).show();

        mHSMSListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(MainActivity.this, "Broj: "+mCurrentEntities.get(i).getNumber(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, DonateActivity.class);
                intent.putExtra("entity", mCurrentEntities.get(i));
                startActivity(intent);
            }
        });
    }

    private void setEntitiesList(ArrayList<HSMSEntity> entitiesList) {
        HSMSListAdapter adapter = new HSMSListAdapter(this, entitiesList);
        mHSMSListView.setAdapter(adapter);
    }

    @Override
    public void onHSMSEventNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        if(mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void loadList() {
        String serviceAddress = mPrefs.getString(HSMSConstants.SERVICE_IP_PREF, DEFAULT_IP);
        HSMSListTask hlt = new HSMSListTask(this);
        hlt.execute(serviceAddress);
    }

    private void handleSearchIntent(Intent intent) {
        if(!Intent.ACTION_SEARCH.equals(intent.getAction()) ||
                mSourceEntities == null) {
            return;
        }
        String searchQuery = intent.getStringExtra(SearchManager.QUERY);

        mCurrentEntities = getMatchingEntitiesCopy(searchQuery);

        for(HSMSEntity entity : mCurrentEntities) {
            String regex = "(?i)" + searchQuery;
            entity.setDesc(highlight(regex, entity.getDesc()));
            entity.setOrganisation(highlight(regex, entity.getOrganisation()));
            entity.setWeb(highlight(regex, entity.getWeb()));
            entity.setNumber(highlight(regex, entity.getNumber()));
        }
        Toast.makeText(this, "Broj pronađenih humanitarnih akcija: "+ mCurrentEntities.size(), Toast.LENGTH_LONG).show();
        setEntitiesList(mCurrentEntities);
    }

    private ArrayList<HSMSEntity> getMatchingEntitiesCopy(String searchQuery) {
        ArrayList<HSMSEntity> searchEntitiesList = new ArrayList<>();
        for(int i=0; i< mSourceEntities.length; i++) {
            if(mSourceEntities[i].getDesc().toUpperCase().contains(searchQuery.toUpperCase()) ||
                    mSourceEntities[i].getOrganisation().toUpperCase().contains(searchQuery.toUpperCase()) ||
                    mSourceEntities[i].getNumber().toUpperCase().contains(searchQuery.toUpperCase()) ||
                    mSourceEntities[i].getWeb().toUpperCase().contains(searchQuery.toUpperCase())) {

                searchEntitiesList.add(mSourceEntities[i].cloneEntity());
            }
        }
        return searchEntitiesList;
    }

    private String highlight(String regex, String text) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        String result = "";
        while(matcher.find()) {
            result = matcher.group();
            text = text.replace(result, "<font color='#E6E600'>"+result+"</font>");
        }
        return text;
    }
}
