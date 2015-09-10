package com.github.somi92.hsmsdroid.tasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.PREF_FILE;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_DATA_ENABLED_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_EMAIL_PREF;
import static com.github.somi92.hsmsdroid.util.HSMSConstants.USER_NAME_PREF;

/**
 * Created by milos on 9/10/15.
 */
public class HSMSDonateTask extends AsyncTask<String, Integer, Boolean> {

    private String mResult;
    private Context mContext;

    public HSMSDonateTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... data) {
        String email = data[1];
        String id = data[2];
        String params = "email:"+email+"/id:"+id;
        String targetUrl = "http://"+data[0]+"/HSMS-MS/public/service/donate/"+params;

        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) (new URL(targetUrl).openConnection());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK) {
                mResult = "HTTP greška, status kod: " + statusCode;
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            mResult = getResponseText(inStream);

        } catch (SocketTimeoutException e) {
            mResult = "Greška! Konekcija je istekla: "+e.getMessage();
            return false;
        } catch (IOException e) {
            mResult = "Greška! I/O sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        }
        catch (Exception e) {
            mResult = "Greška! Sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        if(isSuccessful) {
            if(mResult.contains("Error")) {
                SharedPreferences prefs = mContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = prefs.edit();
                prefsEditor.putBoolean(USER_DATA_ENABLED_PREF, false);
                prefsEditor.putString(USER_EMAIL_PREF, "");
                prefsEditor.putString(USER_NAME_PREF, "");
                prefsEditor.commit();
                Toast.makeText(mContext, "Greška u slanju statističkih podataka. Korisnička podešavanja resetovana.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(mContext, "Greška u slanju statističkih podataka. Proverite internet konekciju.", Toast.LENGTH_LONG).show();
        }
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }
}
