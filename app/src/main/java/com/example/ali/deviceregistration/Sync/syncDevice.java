package com.example.ali.deviceregistration.Sync;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.ali.deviceregistration.Core.MainApp;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ali.azaz on 5/4/2017.
 */

public class syncDevice extends AsyncTask<Void, Void, String> {

    private static final String TAG = "SyncDevice";
    private Context mContext;
    private ProgressDialog pd;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public syncDevice(Context context) {
        mContext = context;
    }

    public static void longInfo(String str) {
        if (str.length() > 4000) {
            Log.i("TAG: ", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("TAG: ", str);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(mContext);
        pd.setTitle("Please wait... Processing Device");
        pd.show();

    }


    @Override
    protected String doInBackground(Void... params) {

        String line = "No Response";
        try {
            String url = MainApp._HOST_URL + "devReg.php?condition=insertData";
            Log.d(TAG, "doInBackground: URL " + url);
            return downloadUrl(url);
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

            Toast.makeText(mContext," Device Registered.", Toast.LENGTH_SHORT).show();

            pd.setMessage("Device Registered.");
            pd.setTitle("Done uploading Device data");
            pd.show();

            sharedPref = mContext.getSharedPreferences("register",MODE_PRIVATE);
            editor = sharedPref.edit();

            editor.putBoolean("flag", true);
            editor.commit();


        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Failed Sync " + result, Toast.LENGTH_SHORT).show();

            pd.setMessage(result);
            pd.setTitle("Device's Sync Failed");
            pd.show();
        }
    }

    private String downloadUrl(String myurl) throws IOException {
        String line = "No Response";

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000 /* milliseconds */);
                conn.setConnectTimeout(30000 /* milliseconds */);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("charset", "utf-8");
                conn.setUseCaches(false);
                // Starts the query
                conn.connect();
                JSONArray jsonSync = new JSONArray();
                try {
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

//                    for (deviceContract dc : MainApp.dc) {

                    if (MainApp.regFlag) {
                        jsonSync.put(MainApp.dc.toJSONObject());
                        Log.d("Data:", ""+MainApp.dc.toJSONObject());
                    }else {
                        jsonSync.put(MainApp.dc.toAlreadyJSONObject());
                        Log.d("Data:", ""+MainApp.dc.toAlreadyJSONObject());
                    }


//                    }
                    wr.writeBytes(jsonSync.toString().replace("\uFEFF", "") + "\n");
                    longInfo(jsonSync.toString().replace("\uFEFF", "") + "\n");
                    wr.flush();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

/*===================================================================*/
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
            /*===================================================================*/
    }

}
