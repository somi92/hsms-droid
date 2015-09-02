package com.github.somi92.hsmsdroid.tasks;

import android.os.AsyncTask;

import com.github.somi92.hsmsdroid.domain.HSMSEntity;
import com.google.gson.Gson;

import org.json.JSONArray;
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


    private HSMSListEventListener mHSMSListener;
    private String mResult;

    public HSMSListTask(HSMSListEventListener hsmsListener) {
        mHSMSListener = hsmsListener;
    }

    @Override
    protected void onPreExecute() {
        mHSMSListener.onHSMSListTaskStarted();
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
        try {
            if(isSuccessful) {
                HSMSEntity[] entities = getHsmsEntities();
                mHSMSListener.onHSMSListReceived(entities);
            } else {
                mHSMSListener.onHSMSEventNotification(mResult);
            }
        }
        catch (JSONException e) {
            mHSMSListener.onHSMSEventNotification("Greška! JSON parsiranje neuspešno.");
        } catch (Exception e) {
            mHSMSListener.onHSMSEventNotification("Greška! Prikaz podataka neuspešan.");
        }
    }

    private HSMSEntity[] getHsmsEntities() throws JSONException {
        JSONObject obj = new JSONObject(mResult);
        JSONArray array = obj.getJSONArray("actions");
        Gson gson = new Gson();
        HSMSEntity[] entities = gson.fromJson(array.toString(), HSMSEntity[].class);
        return entities;
    }

    private static String getResponseText(InputStream in) {
        Scanner scan = new Scanner(in);
        String s = scan.useDelimiter("\\A").next();
        scan.close();
        return s;
    }

    public interface HSMSListEventListener {
        void onHSMSListTaskStarted();
        void onHSMSListReceived(HSMSEntity[] entities);
        void onHSMSEventNotification(String message);
    }

}
