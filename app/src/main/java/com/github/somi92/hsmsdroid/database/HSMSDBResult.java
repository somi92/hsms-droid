package com.github.somi92.hsmsdroid.database;

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

    public T get() {
        return mResult;
    }
}
