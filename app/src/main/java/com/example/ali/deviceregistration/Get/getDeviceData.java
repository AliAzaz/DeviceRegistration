package com.example.ali.deviceregistration.Get;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ali.deviceregistration.Contract.DeviceContract;
import com.example.ali.deviceregistration.Core.MainApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ali.azaz on 5/6/2017.
 */

public class getDeviceData extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GetMembers";
    private Context mContext;
    private ProgressDialog pd;
    private String imei;

    public getDeviceData(Context context, String imei) {
        mContext = context;
        this.imei = imei;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Please wait... Processing Data");
        pd.show();

    }

    @Override
    protected String doInBackground(Void... params) {

        String line = "No Response";
        try {
            return downloadUrl(MainApp._HOST_URL + DeviceContract.DeviceTable._URI_GETDATA);
        } catch (IOException e) {
            return "Unable to upload data. Server may be down.";
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONArray json = null;
        try {

            SharedPreferences sharedPref = mContext.getSharedPreferences("register", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();

            editor.putBoolean("flag", true);
            editor.commit();
            pd.dismiss();

            json = new JSONArray(result);

            MainApp.getdc = new DeviceContract(json.getJSONObject(0));

            Log.d("getdata", String.valueOf(MainApp.getdc));

            Toast.makeText(mContext, "Successfully Get Data ", Toast.LENGTH_SHORT).show();

            pd.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
//            Toast.makeText(mContext, "New Registration ", Toast.LENGTH_SHORT).show();
            pd.dismiss();
        }

    }

    private String downloadUrl(String myurl) {
        String line = "No Response";

        HttpURLConnection conn = null;
        StringBuilder result = null;
        try {
            URL url = new URL(myurl);
            Log.d(TAG, "downloadUrl: " + myurl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);

            // Starts the query
            conn.connect();
            JSONArray jsonSync = new JSONArray();
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
            JSONObject json = new JSONObject();
            try {
                json.put("imei", imei);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Log.d(TAG, "downloadUrl: " + json.toString());
            wr.writeBytes(json.toString());
            wr.flush();
            wr.close();

            int HttpResult = conn.getResponseCode();
            if (HttpResult == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(
                        conn.getInputStream(), "utf-8"));
                StringBuffer sb = new StringBuffer();

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();

                System.out.println("" + sb.toString());
                return sb.toString();
            } else {
                System.out.println(conn.getResponseMessage());
                return conn.getResponseMessage();
            }
        } catch (MalformedURLException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return line;
    }

}
