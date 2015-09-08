package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.domain.HSMSEntity;

public class DonateActivity extends Activity {

    private TextView mTitle;
    private TextView mOrg;
    private TextView mWebsite;
    private TextView mRemark;
    private TextView mNumber;
    private TextView mPrice;

    private Button mDonate;
    private Button mShare;

    private HSMSEntity mEntity;

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

            }
        });

        Bundle bundle = getIntent().getExtras();
        mEntity = bundle.getParcelable("entity");
        setData();
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
