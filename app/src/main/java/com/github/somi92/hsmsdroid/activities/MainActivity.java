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

import static com.github.somi92.hsmsdroid.util.HSMSConstants.PREF_FILE;

public class MainActivity extends Activity implements HSMSListTask.HSMSListEventListener {

    private ProgressDialog mProgressDialog;
    private ListView mHSMSListView;
    private SwipeRefreshLayout mSwipeLayout;
    private SharedPreferences mPrefs;

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
            onSearchRequested();
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
            Toast.makeText(this, "Nije pronađena nije humanitarna akcija.", Toast.LENGTH_SHORT).show();
            return;
        }
        mHSMSListView = (ListView) findViewById(R.id.hsmsListView);
        ArrayList<HSMSEntity> entitiesList = new ArrayList<>(Arrays.asList(entities));
        HSMSListAdapter adapter = new HSMSListAdapter(this, entitiesList);
        mHSMSListView.setAdapter(adapter);
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(mSwipeLayout != null && mSwipeLayout.isRefreshing()) {
            mSwipeLayout.setRefreshing(false);
        }
        Toast.makeText(this, "Broj preuzetih humanitarnih akcija: "+entitiesList.size(), Toast.LENGTH_SHORT).show();
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
}
