package com.github.somi92.hsmsdroid.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSListTask;

public class MainActivity extends AppCompatActivity implements HSMSListTask.HSMSListEventListener {

    private ProgressDialog mProgressDialog;

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
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        Toast.makeText(this, "Entities received!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onHSMSEventNotification(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
