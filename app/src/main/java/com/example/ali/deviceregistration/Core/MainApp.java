package com.example.ali.deviceregistration.Core;

import android.app.Application;

import com.example.ali.deviceregistration.Contract.DeviceContract;

/**
 * Created by ali.azaz on 5/4/2017.
 */

public class MainApp extends Application {

    //    public static final String _IP = "10.1.42.94"; // Test PHP server
    public static final String _IP = "43.245.131.159"; // Main server
    public static final Integer _PORT = 8080; // Port - with colon (:)
    public static final String _HOST_URL = "http://" + MainApp._IP + ":" + MainApp._PORT + "/devicereg/api/";

    public static DeviceContract dc;

    public static DeviceContract getdc;

    public static Boolean regFlag = true;

    public static String projectName = "Leaps-Sup";

}
