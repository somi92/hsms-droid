package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.ACTION_SMS_DELIVERED;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.ACTION_SMS_SENT;

public class DonateActivity extends Activity {

    private static final String TAG = DonateActivity.class.getSimpleName().toString();

    private TextView mTitle;
    private TextView mOrg;
    private TextView mWebsite;
    private TextView mRemark;
    private TextView mNumber;
    private TextView mPrice;

    private Button mDonate;
    private Button mShare;

    private HSMSEntity mEntity;

    private String mShareMessageTemplate;

    private DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i) {
                case DialogInterface.BUTTON_POSITIVE:
                    sendSmsDonation();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        mTitle = (TextView) findViewById(R.id.hsms_title);
        mOrg = (TextView) findViewById(R.id.hsms_org);
        mRemark = (TextView) findViewById(R.id.hsms_remark);
        mNumber = (TextView) findViewById(R.id.hsms_num);
        mPrice = (TextView) findViewById(R.id.hsms_price);

        mWebsite = (TextView) findViewById(R.id.hsms_website);
        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = mWebsite.getText().toString();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        mDonate = (Button) findViewById(R.id.donate_button);
        mDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(DonateActivity.this);
                dialog.setTitle("Jesti li sigurni?").setMessage("Da li ste sigurni da želite " +
                        "da pošaljete SMS poruku na broj "+mEntity.getNumber()+"? Ova akcija će Vam biti naplaćena u iznosu od " +
                        mEntity.getPrice()+".").setPositiveButton("Da", dialogListener)
                .setNegativeButton("Ne", dialogListener).show();
            }
        });

        mShare = (Button) findViewById(R.id.share_button);
        mShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mShareMessageTemplate = getResources().getString(R.string.share_message_template);
                String message = mShareMessageTemplate.replaceAll("!number!", mEntity.getNumber())
                        .replaceAll("!price!", mEntity.getPrice())
                        .replace("!desc!", mEntity.getDesc())
                        .replaceAll("!web!", mEntity.getWeb());
//                Toast.makeText(DonateActivity.this, message, Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.share_chooser_title)));
            }
        });

        Bundle bundle = getIntent().getExtras();
        mEntity = bundle.getParcelable("entity");
        setData();
    }

    private void sendSmsDonation() {

        SmsManager smsManager = SmsManager.getDefault();
        // fix intents !!!
        try {
            PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT), 0);
            PendingIntent deliverIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_DELIVERED), 0);
            smsManager.sendTextMessage(mEntity.getNumber(), null, " ", sentIntent, deliverIntent);

        } catch (Exception e) {
            Toast.makeText(this, "Greška. SMS ne može biti poslat.", Toast.LENGTH_LONG).show();
            Log.d(TAG, "sendSmsDonation error: "+e.getMessage());
        }

    }

    private void setData() {
        mTitle.setText(mEntity.getDesc());
        mOrg.setText(mEntity.getOrganisation());
        mWebsite.setText(mEntity.getWeb());
        mRemark.setText(mEntity.getRemark());
        mNumber.setText(mEntity.getNumber());
        mPrice.setText(mEntity.getPrice());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable("entity", mEntity);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        mEntity = savedInstanceState.getParcelable("entity");
        setData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_donate, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
