package com.github.somi92.hsmsdroid.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.somi92.hsmsdroid.util.HSMSConstants;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;

import static com.github.somi92.hsmsdroid.util.HSMSConstants.*;

/**
 * Created by milos on 9/9/15.
 */
public class HSMSRegisterTask extends AsyncTask<String, Integer, Boolean> {

    private Context mContext;
    private String mResult;
    private String mMethod;

    public HSMSRegisterTask(Context context) {
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(String... data) {
        String mMethod = data[3];
        String targetUrl = "http://"+data[0]+"/HSMS-MS/public/service/"+mMethod;
        String email = data[1];
        String name = data[2];

        if(!email.matches(VALID_EMAIL_REGEX)) {
            mResult = "Greška! E-mail nije validan.";
            return false;
        }

        String params = "email="+email+"&"+"name="+name;
        HttpURLConnection connection = null;

        try {
            byte[] postData = params.getBytes("UTF-8");
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");

            DataOutputStream dao = new DataOutputStream(connection.getOutputStream());
            dao.write(postData);
            dao.flush();
            dao.close();

            int statusCode = connection.getResponseCode();
            if(statusCode != HttpURLConnection.HTTP_OK) {
                mResult = "HTTP greška, status kod: " + statusCode;
                return false;
            }
            InputStream inStream = new BufferedInputStream(connection.getInputStream());
            mResult = getResponseText(inStream);


        } catch (MalformedURLException e) {
            mResult = "Greška! URL je loše formiran: "+e.getMessage();
            return false;
        } catch (SocketTimeoutException e) {
            mResult = "Greška! Konekcija je istekla: "+e.getMessage();
            return false;
        } catch (IOException e) {
            mResult = "Greška! I/O sistem: "+e.getMessage();
            return false;
        } catch (Exception e) {
            mResult = "Greška! Sistem: "+e.getMessage();
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

            if(mResult.contains("Error") && mMethod.equals("registerDonator")) {
                Toast.makeText(mContext, "Greška! E-mail već postoji u bazi ili je neispravan. Pokušajte ponovo.", Toast.LENGTH_LONG).show();
            } else if(mResult.contains("Error") && mMethod.equals("updatedonator")) {
                Toast.makeText(mContext, "Greška! E-mail ne postoji u bazi. Ponovo unesite e-mail.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Uspešno ste registrovani.", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(mContext, mResult+" Pokušajte ponovo.", Toast.LENGTH_LONG).show();
        }
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }
}
