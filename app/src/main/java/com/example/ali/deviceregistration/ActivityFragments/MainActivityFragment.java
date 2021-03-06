package com.example.ali.deviceregistration.ActivityFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

    AlertDialog.Builder builder;
    String m_Text = "";

    ToggleButton btnRegister;
    TextView txtRegister;
    static boolean flag = true;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        btnRegister = (ToggleButton) view.findViewById(R.id.btnRegister);
        txtRegister = (TextView) view.findViewById(R.id.txtRegister);

        // SharedPreferences
        sharedPref = getContext().getSharedPreferences("register", MODE_PRIVATE);
        editor = sharedPref.edit();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public Context context;

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Register for new Project", Snackbar.LENGTH_LONG)
                        .setAction("Click here!", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                if (btnRegister.isChecked()) {

                                    builder = new AlertDialog.Builder(getActivity());

                                    /*Creating Tag name*/
                                    SpannableStringBuilder str = new SpannableStringBuilder();
                                    str.append(" ");
                                    str.setSpan(new ImageSpan(getActivity(), R.drawable.ic_lock_black_24dp), str.length() - 1, str.length(), 0);
                                    str.append(" Enter Password");

                                    builder.setTitle(str);

                                    final EditText input = new EditText(getActivity());
                                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                                    input.setTransformationMethod(PasswordTransformationMethod.getInstance());
                                    builder.setView(input);

                                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            m_Text = input.getText().toString();
                                            if (m_Text.equals("admin1234")) {

                                                Toast.makeText(getContext(), "Register Button Enable", Toast.LENGTH_SHORT).show();
                                                /*editor.putBoolean("flag", false);
                                                editor.commit();*/

                                                flag = false;

                                                btnRegister.setEnabled(true);
                                                btnRegister.setChecked(false);
                                                txtRegister.setText("Register to Another Project");
                                                txtRegister.setTextColor(Color.RED);

                                                /*Fragment frg = getFragmentManager().findFragmentById(R.id.sectionFragment);
                                                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                                ft.detach(frg);
                                                ft.attach(frg);
                                                ft.commit();*/

                                            } else {
                                                Toast.makeText(getContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();

                                } else {
                                    Toast.makeText(getContext(), "Device not registered..", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).show();
            }
        });

        ButtonImplementor();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new syncData(getContext(), true).execute();
                } else {
                    btnRegister.setChecked(false);
                    Toast.makeText(getActivity(), "No network connection available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void ButtonImplementor() {

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

    }

    @Override
    public void onResume() {
        super.onResume();

        boolean checkFlag = sharedPref.getBoolean("flag", false);

        if (!checkFlag) {
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                new syncData(getContext(), false).execute();
            }
        }

    }

    @Override
    public void onPause() {
        flag = true;
        super.onPause();
    }

    public class syncData extends AsyncTask<String, String, String> {

        private Context mContext;
        private boolean flag;

        public syncData(Context mContext, boolean flag) {
            this.mContext = mContext;
            this.flag = flag;
        }

        @Override
        protected String doInBackground(String... strings) {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    new getDeviceData(getContext(), ((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId()).execute();
                }
            });


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    try {
                        if (flag)
                            getFragmentManager().beginTransaction().replace(R.id.sectionFragment, new RegistrationActivity()).commit();
                        else
                            ButtonImplementor();
                    } catch (Exception e) {
                    }
                }
            }, 1200);
        }
    }
}
