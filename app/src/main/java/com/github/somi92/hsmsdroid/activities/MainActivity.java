package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSListTask;
import com.github.somi92.hsmsdroid.util.HSMSConstants;
import com.github.somi92.hsmsdroid.util.HSMSListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.PREF_FILE;

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

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList();
            }
        });
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.black);

        loadList();
        handleSearchIntent(getIntent());
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
                Toast.makeText(this, "Nema preuzetih humanitarnih akcija.", Toast.LENGTH_SHORT).show();
            } else {
                onSearchRequested();
            }
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
            Toast.makeText(this, "Nije pronađena nijedna humanitarna akcija.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Broj preuzetih humanitarnih akcija: "+entitiesList.size(), Toast.LENGTH_SHORT).show();

        mHSMSListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Broj: "+mCurrentEntities.get(i).getNumber(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setEntitiesList(ArrayList<HSMSEntity> entitiesList) {
        HSMSListAdapter adapter = new HSMSListAdapter(this, entitiesList);
        mHSMSListView.setAdapter(adapter);
    }

    @Override
    public void onHSMSEventNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        if(mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    private void loadList() {
        String serviceAddress = mPrefs.getString(HSMSConstants.SERVICE_IP_PREF, "192.168.1.2");
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
        Toast.makeText(this, "Broj pronađenih humanitarnih akcija: "+ mCurrentEntities.size(), Toast.LENGTH_SHORT).show();
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
