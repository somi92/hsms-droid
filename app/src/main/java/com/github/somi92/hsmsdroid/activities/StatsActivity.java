package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsGetAll;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSStatsDatabaseTask;
import com.github.somi92.hsmsdroid.util.HSMSStatsListAdapter;

import java.util.List;

public class StatsActivity extends Activity implements HSMSStatsDatabaseTask.HSMSDatabaseListener<List<HSMSStatsEntity>> {

    private TextView mMessages;
    private TextView mAmount;
    private ListView mStatsListView;

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        mMessages = (TextView) findViewById(R.id.stats_total_user_messages);
        mAmount = (TextView) findViewById(R.id.stats_total_user_amount);
        mStatsListView = (ListView) findViewById(R.id.statsListView);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setMessage("Molimo sačekajte...");
        mProgressDialog.show();

        HSMSStatsDatabaseTask dbt = new HSMSStatsDatabaseTask(getApplicationContext(),
                new HSMSStatsGetAll(new HSMSStatsDBHelper(getApplicationContext())), this);
        dbt.execute();
    }

    @Override
    public void onHSMSStatsDataRead(List<HSMSStatsEntity> result) {
        int messages = 0;
        int amount = 0;
        if(result != null && result.size() > 0) {
            for(HSMSStatsEntity e : result) {
                messages += e.getNumberOfDonations();
                amount += Integer.parseInt(e.getActionPrice().split(" ")[0])
                        * e.getNumberOfDonations();
            }
            mStatsListView = (ListView) findViewById(R.id.statsListView);
            HSMSStatsListAdapter adapter = new HSMSStatsListAdapter(this, result);
            mStatsListView.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Statistika je prazna.", Toast.LENGTH_LONG).show();
        }
        mMessages.setText("Broj vaših SMS donacija: " + messages + "");
        mAmount.setText("Iznos novca koji ste donirali: " + amount + " din");
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
