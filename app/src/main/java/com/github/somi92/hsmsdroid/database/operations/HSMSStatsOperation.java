package com.github.somi92.hsmsdroid.database.operations;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;

/**
 * Created by milos on 9/11/15.
 */
public abstract class HSMSStatsOperation<T> {

    protected HSMSStatsDBHelper mDBHelper;
    protected ContentValues mContentValues;
    protected SQLiteDatabase mDatabase;

    public HSMSStatsOperation(HSMSStatsDBHelper dbHelper) {
        mDBHelper = dbHelper;
        mContentValues = new ContentValues();
    }

    public abstract T executeHSMSStatsOperations();
}
