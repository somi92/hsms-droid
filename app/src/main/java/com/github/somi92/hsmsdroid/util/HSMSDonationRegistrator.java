package com.github.somi92.hsmsdroid.util;

import android.content.Context;

import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsUpdate;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSDonateTask;
import com.github.somi92.hsmsdroid.tasks.HSMSStatsDatabaseTask;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSDonationRegistrator {

    private Context mContext;
    private String[] mData;
    private HSMSEntity mEntity;

    private static HSMSDonationRegistrator INSTANCE;

    private HSMSDonationRegistrator() {

    }

    public static HSMSDonationRegistrator getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new HSMSDonationRegistrator();
        }
        return INSTANCE;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    public void setData(String[] data) {
        mData = data;
    }

    public void setEntity(HSMSEntity entity) {
        mEntity = entity;
    }

    public void registerDonation() {
        HSMSDonateTask hdt = new HSMSDonateTask(mContext);
        hdt.execute(mData);
    }

    public void saveInternalStatistics() {
        HSMSStatsDatabaseTask hsdt = new HSMSStatsDatabaseTask(mContext,
                new HSMSStatsUpdate(new HSMSStatsDBHelper(mContext), mEntity), null);
        hsdt.execute();
    }

}
