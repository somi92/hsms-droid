package com.github.somi92.hsmsdroid.database;

import android.content.Context;

import com.github.somi92.hsmsdroid.database.operations.HSMSStatsGetAll;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsOperation;
import com.github.somi92.hsmsdroid.database.operations.HSMSStatsUpdate;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.github.somi92.hsmsdroid.domain.HSMSStatsEntity;

import java.util.List;

/**
 * Created by milos on 9/11/15.
 */
public class HSMSDBResult<T> {

    private T mResult;

    public HSMSDBResult(T initial) {
        mResult = initial;
    }

    public void set(T value) {
        mResult = value;
    }

    public T get(Context c, HSMSEntity e) {
        HSMSStatsOperation<List<HSMSStatsEntity>> op = new HSMSStatsGetAll(new HSMSStatsDBHelper(c));
        List<HSMSStatsEntity> list = op.executeHSMSStatsOperations();

        HSMSStatsOperation<Boolean> op1 = new HSMSStatsUpdate(new HSMSStatsDBHelper(c), e);
        return mResult;
    }
}
