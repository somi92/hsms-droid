package com.github.somi92.hsmsdroid.tasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.domain.HSMSEntity;

import org.json.JSONException;
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
    private String result;

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
                result = "HTTP greška, status kod: " + statusCode;
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            result = getResponseText(inStream);

        } catch (SocketTimeoutException e) {
            result = "Greška! Konekcija je istekla: "+e.getMessage();
            return false;
        } catch (IOException e) {
            result = "Greška! I/O sistem ne može preuzeti podatke: "+e.getMessage();
            return false;
        }
        catch (Exception e) {
            result = "Greška! Sistem ne može preuzeti podatke: "+e.getMessage();
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
        try {
            if(mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            if(isSuccessful) {
                Toast.makeText(mParentActivity, result, Toast.LENGTH_SHORT).show();
                JSONObject obj = new JSONObject(result);
            } else {
                Toast.makeText(mParentActivity, result, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            Toast.makeText(mParentActivity, "Greška! JSON parsiranje neuspešno.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(mParentActivity, "Greška! Prikaz podataka neuspešan.", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }


}
