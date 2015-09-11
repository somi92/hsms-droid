package com.github.somi92.hsmsdroid.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.github.somi92.hsmsdroid.database.HSMSDBResult;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsOperation;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSStatsDatabaseTask extends AsyncTask<Void, Integer, HSMSDBResult> {

    private Context mContext;
    private HSMSStatsOperation mDatabaseOperation;
    private HSMSDatabaseListener mDatabaseListener;

    public HSMSStatsDatabaseTask(Context context, HSMSStatsOperation databaseOperation, HSMSDatabaseListener databaseListener) {
        mContext = context;
        mDatabaseOperation = databaseOperation;
        mDatabaseListener = databaseListener;
    }

    @Override
    protected HSMSDBResult doInBackground(Void... voids) {
        return mDatabaseOperation.executeHSMSStatsOperations();
    }

    @Override
    protected void onPostExecute(HSMSDBResult hsmsdbResult) {
        if(mDatabaseListener != null) {
            mDatabaseListener.onHSMSStatsDataRead(hsmsdbResult.get());
        }
    }

    public interface HSMSDatabaseListener<T> {
        void onHSMSStatsDataRead(T result);
    }
}
