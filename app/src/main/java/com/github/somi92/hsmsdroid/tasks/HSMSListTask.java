package com.github.somi92.hsmsdroid.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.domain.HSMSEntity;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by milos on 9/1/15.
 */
public class HSMSListTask extends AsyncTask<URL, Integer, Boolean> {

    private ProgressDialog mProgressDialog;
    private Activity mParentActivity;
    private JSONObject mJsonObj;
    private HSMSEntity mHsmsEntity;

    public HSMSListTask(Activity parentActivity) {
        mParentActivity = parentActivity;
        mProgressDialog = new ProgressDialog(mParentActivity);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog.setMessage("Molimo sačekajte...");
        mProgressDialog.show();
    }

    @Override
    protected Boolean doInBackground(URL... url) {
        String string = "http://192.168.1.181/HSMS-MS/public/service/listhsms";
        HttpURLConnection connection = null;

        try {
            connection = (HttpURLConnection) (new URL(string).openConnection());
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK) {
                // Toast.makeText(mParentActivity, "HTTP greška, status kod: "+statusCode, Toast.LENGTH_SHORT).show();
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            mJsonObj = new JSONObject(getResponseText(inStream));


            // Toast.makeText(mParentActivity, jsonObj.toString(), Toast.LENGTH_LONG).show();

        }
//        catch (SocketTimeoutException e) {
//            // Toast.makeText(mParentActivity, "Greška! Konekcija je istekla: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//            return false;
//        } catch (IOException e) {
//            // Toast.makeText(mParentActivity, "Greška! I/O sistem ne može preuzeti podatke: "+e.getMessage(), Toast.LENGTH_SHORT).show();
//            return false;
//        }
        catch (Exception e) {
            // Toast.makeText(mParentActivity, "Greška! Sistem ne može preuzeti podatke: "+e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(this.getClass().getSimpleName(), "Error: "+e.getMessage());
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
        if(mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        if(isSuccessful) {
            Toast.makeText(mParentActivity, mJsonObj.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mParentActivity, "Greška! Sistem ne može preuzeti podatke!", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }


}
