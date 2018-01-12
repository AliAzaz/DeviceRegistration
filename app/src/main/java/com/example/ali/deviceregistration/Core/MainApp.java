package com.example.ali.deviceregistration.Core;

import android.app.Application;

import com.example.ali.deviceregistration.Contract.deviceContract;

/**
 * Created by ali.azaz on 5/4/2017.
 */

public class MainApp extends Application {

//    public static final String _IP = "10.1.42.92"; // Test PHP server
    public static final String _IP = "43.245.131.159"; // Main server
    public static final Integer _PORT = 8080; // Port - with colon (:)
    public static final String _HOST_URL = "http://" + MainApp._IP + ":" + MainApp._PORT + "/devicereg/api/";

    public static deviceContract dc;

    public static deviceContract getdc;

    public static Boolean regFlag = true;

}
