package com.github.somi92.hsmsdroid.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.ACTION_SMS_SENT;

/**
 * Created by milos on 9/9/15.
 */
public class SMSSendBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if(action.equals(ACTION_SMS_SENT)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS je poslat.", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    Toast.makeText(context, "Greška. SMS nije poslat: generic failure", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    Toast.makeText(context, "Greška. SMS nije poslat: no service", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    Toast.makeText(context, "Greška. SMS nije poslat: null pdu", Toast.LENGTH_LONG).show();
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    Toast.makeText(context, "Greška. SMS nije poslat: radio off", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    }
}
