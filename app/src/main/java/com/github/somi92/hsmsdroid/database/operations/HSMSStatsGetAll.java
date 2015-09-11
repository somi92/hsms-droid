package com.github.somi92.hsmsdroid.database.operations;

import android.content.Entity;
import android.database.Cursor;

import com.github.somi92.hsmsdroid.database.HSMSDBResult;
import com.github.somi92.hsmsdroid.database.HSMSStatsDBContract;
import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;

import java.util.ArrayList;
import java.util.List;

import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.TABLE_NAME;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSStatsGetAll extends HSMSStatsOperation {

    public HSMSStatsGetAll(HSMSStatsDBHelper dbHelper) {
        super(dbHelper);
    }

    @Override
    public HSMSDBResult executeHSMSStatsOperations() {
        mDatabase = mDBHelper.getReadableDatabase();
        List<HSMSStatsEntity> statsList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = mDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()) {
            do {
                HSMSStatsEntity e = new HSMSStatsEntity();
                e.setActionId(cursor.getString(1));
                e.setActionDesc(cursor.getString(2));
                e.setActionPrice(cursor.getString(3));
                e.setNumberOfDonations(cursor.getInt(4));
                statsList.add(e);
            } while(cursor.moveToNext());
        }
        mDatabase.close();
        return new HSMSDBResult<>(statsList);
    }
}
