package com.github.somi92.hsmsdroid.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSListTask;
import com.github.somi92.hsmsdroid.util.HSMSListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements HSMSListTask.HSMSListEventListener {

    private ProgressDialog mProgressDialog;
    private ListView mHSMSListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        HSMSListTask hlt = new HSMSListTask(this);
        hlt.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHSMSListTaskStarted() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Molimo sačekajte...");
        mProgressDialog.show();
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
        Toast.makeText(this, "Broj preuzetih humanitarnih akcija: "+entitiesList.size(), Toast.LENGTH_SHORT).show();
    }

    private List<Map<String, HSMSEntity>> getEntitiesList(HSMSEntity[] entities) {
        if(entities.length < 1) {
            return null;
        }
        List<Map<String, HSMSEntity>> entitiesList = new ArrayList<>();
        for(HSMSEntity entity : entities) {
            HashMap<String, String> map = new HashMap<>();

        }
        return entitiesList;
    }

    @Override
    public void onHSMSEventNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
