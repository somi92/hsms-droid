package com.github.somi92.hsmsdroid.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;
import com.github.somi92.hsmsdroid.tasks.HSMSRegisterTask;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.PREF_FILE;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.SERVICE_IP_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_DATA_ENABLED_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_EMAIL_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_NAME_PREF;

/**
 * Created by milos on 9/6/15.
 */
public class SettingsActivity extends PreferenceActivity {

    private SharedPreferences mPrefs;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }

        mPrefs = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        mPrefsListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                if(s.equals(USER_DATA_ENABLED_PREF)) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(SettingsActivity.this);
                    dialog.setTitle("Obaveštenje").setMessage(getResources().getString(R.string.user_email_notification))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // ok
                        }
                    }).show();
                }
                if(s.equals(USER_EMAIL_PREF)) {
//                    Toast.makeText(SettingsActivity.this, "Promjenjena email adresa", Toast.LENGTH_SHORT).show();
                    HSMSRegisterTask hrt = new HSMSRegisterTask(getApplicationContext());
                    String url = mPrefs.getString(SERVICE_IP_PREF, "192.168.1.2");
                    String email = mPrefs.getString(USER_EMAIL_PREF, "");
                    String name = mPrefs.getString(USER_NAME_PREF, "");
                    String[] data = {url, email, name};
                    hrt.execute(data);
                }
                if(s.equals(USER_NAME_PREF)) {
                    Toast.makeText(SettingsActivity.this, "Promjenjeno ime i prezime", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mPrefs.registerOnSharedPreferenceChangeListener(mPrefsListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return SettingsFragment.class.getName().equals(fragmentName);
    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(PREF_FILE);
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.preferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPrefs.unregisterOnSharedPreferenceChangeListener(mPrefsListener);
    }
}
