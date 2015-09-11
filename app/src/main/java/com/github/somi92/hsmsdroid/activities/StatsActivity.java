package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsGetAll;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSStatsDatabaseTask;

import java.util.List;

public class StatsActivity extends Activity implements HSMSStatsDatabaseTask.HSMSDatabaseListener<List<HSMSStatsEntity>> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        HSMSStatsDatabaseTask dbt = new HSMSStatsDatabaseTask(getApplicationContext(),
                new HSMSStatsGetAll(new HSMSStatsDBHelper(getApplicationContext())), this);
        dbt.execute();
    }

    @Override
    public void onHSMSStatsDataRead(List<HSMSStatsEntity> result) {
        if(result != null && result.size() > 0) {
            for(HSMSStatsEntity e : result) {
                Toast.makeText(getApplicationContext(), e.getActionDesc()+" - "+e.getNumberOfDonations(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Statistika je prazna.", Toast.LENGTH_LONG).show();
        }
    }
}
