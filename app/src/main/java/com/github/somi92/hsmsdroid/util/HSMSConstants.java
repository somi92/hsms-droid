package com.github.somi92.hsmsdroid.util;

/**
 * Created by milos on 9/7/15.
 */
public class HSMSConstants {

    public static final String PREF_FILE = "hsms";

    /**
     * Preferences keys
     */
    public static final String SERVICE_IP_PREF = "service_ip_pref";
    public static final String USER_DATA_ENABLED_PREF = "user_data_enabled_pref";
    public static final String USER_EMAIL_PREF = "user_email_pref";
    public static final String USER_NAME_PREF = "user_name_pref";
    public static final String APP_FIRST_RUN = "app_first_run";

    /**
     * Custom actions
     */
    public static final String ACTION_SMS_DELIVERED = "HSMS_DELIVERED";
    public static final String ACTION_SMS_SENT = "HSMS_SENT";

    /**
     * Misc
     */
    public static final String VALID_EMAIL_REGEX = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    public static final String DEFAULT_IP = "192.168.0.103";
}
