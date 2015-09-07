package com.github.somi92.hsmsdroid.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import com.github.somi92.hsmsdroid.R;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.*;

/**
 * Created by milos on 9/6/15.
 */
public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SettingsFragment())
                    .commit();
        }
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

//    @Override
//    protected boolean isValidFragment(String fragmentName) {
//        return PreferencesFragment.class.getName().equals(fragmentName);
//    }

    public static class SettingsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            getPreferenceManager().setSharedPreferencesName(PREF_FILE);
            getPreferenceManager().setSharedPreferencesMode(MODE_PRIVATE);

            addPreferencesFromResource(R.xml.preferences);
        }
    }
}
