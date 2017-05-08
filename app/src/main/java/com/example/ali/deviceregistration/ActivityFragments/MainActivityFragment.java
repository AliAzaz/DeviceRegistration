package com.example.ali.deviceregistration.ActivityFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.ali.deviceregistration.Get.getDeviceData;
import com.example.ali.deviceregistration.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    ToggleButton btnRegister;
    TextView txtRegister;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnRegister = (ToggleButton) view.findViewById(R.id.btnRegister);
        txtRegister = (TextView) view.findViewById(R.id.txtRegister);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public Context context;

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Register for new Project", Snackbar.LENGTH_LONG)
                        .setAction("Click here!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getContext(), "Register Button Enable", Toast.LENGTH_SHORT).show();
                                editor.putBoolean("flag", false);
                                editor.commit();

                                btnRegister.setChecked(false);

                                Fragment frg = null;
                                frg = getFragmentManager().findFragmentById(R.id.sectionFragment);
                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(frg);
                                ft.attach(frg);
                                ft.commit();

                            }
                        }).show();
            }
        });

        sharedPref = getContext().getSharedPreferences("register", MODE_PRIVATE);
        editor = sharedPref.edit();

        Boolean flag = sharedPref.getBoolean("flag", false);

        if (flag) {
            btnRegister.setEnabled(false);
            btnRegister.setChecked(true);
            txtRegister.setText("Device Registered");
            txtRegister.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        } else {
            btnRegister.setEnabled(true);
            btnRegister.setChecked(false);
            txtRegister.setText("Device Not Registered");
            txtRegister.setTextColor(Color.RED);
        }


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager)
                        getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new syncData(getContext()).execute();
                }
            }
        });

        return view;
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
                    new getDeviceData(getContext(),((TelephonyManager)getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId(),
                            Settings.Secure.getString(getContext().getContentResolver(),Settings.Secure.ANDROID_ID)).execute();
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    getFragmentManager().beginTransaction().replace(R.id.sectionFragment, new RegistrationActivity())
                            .commit();
                }
            }, 1200);
        }
    }

}
