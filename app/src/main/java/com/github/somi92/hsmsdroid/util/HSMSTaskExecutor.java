package com.github.somi92.hsmsdroid.util;

import android.content.Context;

import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsUpdate;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.tasks.HSMSDonateTask;
import com.github.somi92.hsmsdroid.tasks.HSMSRegisterTask;
import com.github.somi92.hsmsdroid.tasks.HSMSStatsDatabaseTask;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSTaskExecutor {

    private Context mContext;
    private String[] mData;
    private HSMSEntity mEntity;

    private static HSMSTaskExecutor INSTANCE;

    private HSMSTaskExecutor() {

    }

    public static HSMSTaskExecutor getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new HSMSTaskExecutor();
        }
        return INSTANCE;
    }

    public void setupDonationRegistration(Context context, String[] data) {
        mContext = context;
        mData = data;
    }

    public void registerDonation(boolean resetState) {
        HSMSDonateTask hdt = new HSMSDonateTask(mContext);
        hdt.execute(mData);
        if(resetState) {
            resetState();
        }
    }

    public void setupStatistics(Context context, HSMSEntity entity) {
        mContext = context;
        mEntity = entity;
    }

    public void saveInternalStatistics(boolean resetState) {
        HSMSStatsDatabaseTask hsdt = new HSMSStatsDatabaseTask(mContext,
                new HSMSStatsUpdate(new HSMSStatsDBHelper(mContext), mEntity), null);
        hsdt.execute();
        if(resetState) {
            resetState();
        }
    }

    public void setupUserRegistration(Context context, String[] data) {
        mContext = context;
        mData = data;
    }

    public void registerUser(boolean resetState) {
        HSMSRegisterTask hrt = new HSMSRegisterTask(mContext);
        hrt.execute(mData);
        if(resetState) {
            resetState();
        }
    }

    public void resetState() {
        mContext = null;
        mEntity = null;
        mData = null;
    }

}
