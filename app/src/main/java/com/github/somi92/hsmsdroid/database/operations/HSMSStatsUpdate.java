package com.github.somi92.hsmsdroid.database.operations;

import android.database.Cursor;

import com.github.somi92.hsmsdroid.database.HSMSDBResult;
import com.github.somi92.hsmsdroid.database.HSMSStatsDBHelper;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;

import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.COLUMN_NAME_ACTION_DESC;
import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.COLUMN_NAME_ACTION_ID;
import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.COLUMN_NAME_ACTION_PRICE;
import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.COLUMN_NAME_NUM_DONATIONS;
import static com.github.somi92.hsmsdroid.database.HSMSStatsDBContract.HSMSStatsTable.TABLE_NAME;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSStatsUpdate extends HSMSStatsOperation {

    private HSMSEntity mEntity;

    public HSMSStatsUpdate(HSMSStatsDBHelper dbHelper, HSMSEntity entity) {
        super(dbHelper);
        mEntity = entity;
    }

    @Override
    public HSMSDBResult executeHSMSStatsOperations() {
        mDatabase = mDBHelper.getWritableDatabase();

        HSMSStatsEntity statsEntity = loadEntity();
        long result;
        if(statsEntity != null) {
            result = updateEntity(statsEntity);
        } else {
            result = insertEntity();
        }

        mDatabase.close();
        return new HSMSDBResult(result);
    }

    private long insertEntity() {
        mContentValues.put(COLUMN_NAME_ACTION_ID, mEntity.getId());
        mContentValues.put(COLUMN_NAME_ACTION_DESC, mEntity.getDesc());
        mContentValues.put(COLUMN_NAME_ACTION_PRICE, mEntity.getPrice());
        mContentValues.put(COLUMN_NAME_NUM_DONATIONS, 1);
        return mDatabase.insert(TABLE_NAME, null, mContentValues);
    }

    private long updateEntity(HSMSStatsEntity statsEntity) {
        mContentValues.put(COLUMN_NAME_ACTION_DESC, mEntity.getDesc());
        mContentValues.put(COLUMN_NAME_ACTION_PRICE, mEntity.getPrice());
        mContentValues.put(COLUMN_NAME_NUM_DONATIONS, statsEntity.getNumberOfDonations() + 1);
        return mDatabase.update(TABLE_NAME, mContentValues, COLUMN_NAME_ACTION_ID + "=?",
                new String[] {statsEntity.getActionId()});
    }

    private HSMSStatsEntity loadEntity() {
        Cursor cursor = mDatabase.query(TABLE_NAME,
                new String[]{COLUMN_NAME_ACTION_ID, COLUMN_NAME_ACTION_DESC, COLUMN_NAME_ACTION_PRICE, COLUMN_NAME_NUM_DONATIONS},
                COLUMN_NAME_ACTION_ID + "=?", new String[]{mEntity.getId()}, null, null, null, null);

        HSMSStatsEntity e = null;
        if(cursor.moveToFirst()) {
            e = new HSMSStatsEntity();
            e.setActionId(cursor.getString(0));
            e.setActionDesc(cursor.getString(1));
            e.setActionPrice(cursor.getString(2));
            e.setNumberOfDonations(cursor.getInt(3));
        }

        cursor.close();
        return e;
    }
}
