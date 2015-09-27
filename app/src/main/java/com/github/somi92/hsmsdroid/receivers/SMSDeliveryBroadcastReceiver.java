package com.github.somi92.hsmsdroid.receivers;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.util.HSMSTaskExecutor;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.ACTION_SMS_DELIVERED;

/**
 * Created by milos on 9/9/15.
 */
public class SMSDeliveryBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if(action.equals(ACTION_SMS_DELIVERED)) {

            if(getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(context, "SMS je uspešno dostavljen. Hvala Vam na humanosti.", Toast.LENGTH_LONG).show();
                HSMSTaskExecutor.getInstance().registerDonation(false);
                HSMSTaskExecutor.getInstance().saveInternalStatistics(true);
            } else {
                Toast.makeText(context, "Greška. SMS nije dostavljen.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
