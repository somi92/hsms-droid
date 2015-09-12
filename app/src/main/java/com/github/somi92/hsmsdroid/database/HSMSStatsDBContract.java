package com.github.somi92.hsmsdroid.database;

import android.provider.BaseColumns;

/**
 * Created by milos on 9/11/15.
 */
public final class HSMSStatsDBContract {

    public HSMSStatsDBContract() {}

    public static final String TEXT_TYPE = " TEXT";
    public static final String BIGINT_TYPE = " BIGINT";
    public static final String COMMA_SEP = ",";

    public static abstract class HSMSStatsTable implements BaseColumns {

        public static final String TABLE_NAME = "hsms_stats";
        public static final String COLUMN_NAME_ACTION_ID = "action_id";
        public static final String COLUMN_NAME_ACTION_DESC = "action_desc";
        public static final String COLUMN_NAME_ACTION_PRICE = "action_price";
        public static final String COLUMN_NAME_ACTION_NUMBER = "action_number";
        public static final String COLUMN_NAME_NUM_DONATIONS = "num_of_donations";

        public static final String CREATE_HSMSSTATS_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + BIGINT_TYPE + " PRIMARY KEY" + COMMA_SEP +
                COLUMN_NAME_ACTION_ID + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ACTION_DESC + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ACTION_PRICE + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_ACTION_NUMBER + TEXT_TYPE + COMMA_SEP +
                COLUMN_NAME_NUM_DONATIONS + BIGINT_TYPE + " )";

        public static final String DROP_HSMSSTATS_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
