package com.github.somi92.hsmsdroid.util;

import android.content.Context;

import com.github.somi92.hsmsdroid.tasks.HSMSDonateTask;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSDonationRegistrator {

    private Context mContext;
    private String[] mData;

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

    public void registerDonation() {
        HSMSDonateTask hdt = new HSMSDonateTask(mContext);
        hdt.execute(mData);
    }

}
