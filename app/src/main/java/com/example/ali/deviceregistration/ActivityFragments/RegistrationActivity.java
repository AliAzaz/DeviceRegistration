package com.example.ali.deviceregistration.ActivityFragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ali.deviceregistration.Contract.DeviceContract;
import com.example.ali.deviceregistration.Core.MainApp;
import com.example.ali.deviceregistration.R;
import com.example.ali.deviceregistration.Sync.syncDevice;

import org.json.JSONException;

import java.lang.reflect.Method;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationActivity extends Fragment {

    private static final String TAG = RegistrationActivity.class.getName();
    EditText txtProjectName;
    EditText txtTagName;
    Button btnContinue;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    public RegistrationActivity() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_registration, container, false);

        txtProjectName = (EditText)view.findViewById(R.id.txtProjectName);
        txtTagName = (EditText)view.findViewById(R.id.txtTagName);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                    if (validation()) {

                        try {
                            SaveDraft();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        new syncData(getContext()).execute();

                    }
                }else {
                    Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_SHORT).show();
                    getFragmentManager().beginTransaction().replace(R.id.sectionFragment, new MainActivityFragment()).commit();
                }
            }
        });




        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (MainApp.getdc != null){

            MainApp.regFlag = false;

            txtTagName.setText(MainApp.getdc.getTag());
            txtTagName.setEnabled(false);

        }else {
            MainApp.regFlag = true;
            txtTagName.setEnabled(true);
        }

    }

    public boolean validation(){

        if (txtProjectName.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "ERROR(empty): Project Name Required", Toast.LENGTH_SHORT).show();
            txtProjectName.setError("This data is Required!");    // Set Error on last radio button

            Log.i(TAG, "projectName: This data is Required!");
            return false;
        } else {
            txtProjectName.setError(null);
        }

        if (txtTagName.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "ERROR(empty): Tag Name Required", Toast.LENGTH_SHORT).show();
            txtTagName.setError("This data is Required!");    // Set Error on last radio button

            Log.i(TAG, "tagName: This data is Required!");
            return false;
        } else {
            txtTagName.setError(null);
        }

        return true;
    }

    public void SaveDraft() throws JSONException {
        Toast.makeText(getContext(), "Saving Data", Toast.LENGTH_SHORT).show();

        MainApp.dc = new DeviceContract();

        try {

            if (MainApp.regFlag) {

                TelephonyManager telephonyManager = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);

                String serialNumber;

                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class, String.class);
                serialNumber = (String) get.invoke(c, "sys.serialnumber", "Error");
                if (serialNumber.equals("Error")) {
                    serialNumber = (String) get.invoke(c, "ril.serialnumber", "Error");
                }

                Log.d("Serial No:", serialNumber);
                Log.d("IMEI No:", telephonyManager.getDeviceId());
                Log.d("Device Id:", Settings.Secure.getString(getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID));

                MainApp.dc.setDeviceserial(serialNumber);
                MainApp.dc.setImei(telephonyManager.getDeviceId());
                MainApp.dc.setDeviceid(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                MainApp.dc.setTag(txtTagName.getText().toString());
                MainApp.dc.setFlag("1");
            }else {
                MainApp.dc.setImei(MainApp.getdc.getImei());
                MainApp.dc.setDeviceid(Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID));
                MainApp.dc.setFlag("0");
            }

            MainApp.dc.setProjectName(txtProjectName.getText().toString());
        }
        catch (Exception e){
            Log.d("Error:",e.getMessage());
        }


    }

    public class syncData extends AsyncTask<String, String, String> {

        private Context mContext;

        public syncData(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(getContext(), "Device Registered", Toast.LENGTH_LONG).show();
                    new syncDevice(mContext).execute();
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

//                    sharedPref = getContext().getSharedPreferences("register",MODE_PRIVATE);
//                    editor = sharedPref.edit();
//
//                    editor.putBoolean("flag", true);
//                    editor.commit();

                    getFragmentManager().beginTransaction().replace(R.id.sectionFragment, new MainActivityFragment()).commit();

                }
            }, 1200);
        }
    }

}
