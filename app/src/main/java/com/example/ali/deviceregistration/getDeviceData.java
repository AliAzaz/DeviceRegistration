package com.example.ali.deviceregistration;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ali.azaz on 5/6/2017.
 */

public class getDeviceData extends AsyncTask<Void, Void, String> {

    private static final String TAG = "GetMembers";
    private Context mContext;
    private ProgressDialog pd;

    private String imei, deviceId;

    public String getImei() {
        return imei;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public getDeviceData(Context context, String imei, String deviceId) {
        mContext = context;

        this.imei = imei;
        this.deviceId = deviceId;
    }

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i(TAG + "LongInfo", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i(TAG + "LongInfo", str);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Please wait... Processing Members");
        pd.show();

    }


    @Override
    protected String doInBackground(Void... params) {

        String line = "No Response";
        try {
            return downloadUrl(MainApp._HOST_URL + "devReg.php?condition=getData");
        } catch (IOException e) {
            return "Unable to upload data. Server may be down.";
        }

    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        JSONArray json = null;
        try {
            json = new JSONArray(result);

//            DatabaseHelper db = new DatabaseHelper(mContext);
//            db.syncMembers(json);

            MainApp.getdc = new deviceContract(json.getJSONObject(0));

            Log.d("getdata", String.valueOf(MainApp.getdc));

            Toast.makeText(mContext, "Successfully Get Data ", Toast.LENGTH_SHORT).show();

            pd.dismiss();

//            pd.setMessage(json.length() + " Members synced.");
//            pd.setTitle("Members: Done");
//            pd.show();
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "New Registration ", Toast.LENGTH_SHORT).show();
            pd.dismiss();

//            pd.setMessage(result);
//            pd.setTitle("Members Sync Failed");
//            pd.show();
        }

    }

    private String downloadUrl(String myurl) throws IOException {
        String line = "No Response";

        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 5000;

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
                json.put("imei", getImei());
                json.put("deviceid", getDeviceId());

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            Log.d(TAG, "downloadUrl: " + json.toString());
            wr.writeBytes(json.toString());
            longInfo(jsonSync.toString().replace("\uFEFF", "") + "\n");
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
