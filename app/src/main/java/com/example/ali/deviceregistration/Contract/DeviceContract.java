package com.example.ali.deviceregistration.Contract;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ali.azaz on 5/4/2017.
 */

public class DeviceContract {

    String imei = "";
    String tag = "";
    String deviceserial = "";
    String deviceid = "";
    String projectName = "";
    String flag = "";
    String _id = "";

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDeviceserial() {
        return deviceserial;
    }

    public void setDeviceserial(String deviceserial) {
        this.deviceserial = deviceserial;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(DeviceTable.COLUMN_IMEI, this.imei == null ? JSONObject.NULL : this.imei);
        json.put(DeviceTable.COLUMN_TAG, this.tag == null ? JSONObject.NULL : this.tag);
        json.put(DeviceTable.COLUMN_DEVICE_ID, this.deviceid == null ? JSONObject.NULL : this.deviceid);
        json.put(DeviceTable.COLUMN_DEVICE_SERIAL, this.deviceserial == null ? JSONObject.NULL : this.deviceserial);
        json.put(DeviceTable.COLUMN_PROJECT_NAME, this.projectName == null ? JSONObject.NULL : this.projectName);
        json.put(DeviceTable.COLUMN_FLAG, this.flag == null ? JSONObject.NULL : this.flag);

        return json;
    }

    public JSONObject toAlreadyJSONObject() throws JSONException {
        JSONObject json = new JSONObject();

        json.put(DeviceTable.COLUMN_IMEI, this.imei == null ? JSONObject.NULL : this.imei);
        json.put(DeviceTable.COLUMN_DEVICE_ID, this.deviceid == null ? JSONObject.NULL : this.deviceid);
        json.put(DeviceTable.COLUMN_PROJECT_NAME, this.projectName == null ? JSONObject.NULL : this.projectName);
        json.put(DeviceTable.COLUMN_FLAG, this.flag == null ? JSONObject.NULL : this.flag);

        return json;
    }

    public DeviceContract(JSONObject jsonObject) throws JSONException {
        this.imei = jsonObject.getString(DeviceTable.COLUMN_IMEI);
        this.tag = jsonObject.getString(DeviceTable.COLUMN_TAG);
        this.deviceserial = jsonObject.getString(DeviceTable.COLUMN_DEVICE_SERIAL);
        this._id = jsonObject.getString(DeviceTable._ID);
    }

    public DeviceContract() {
    }

    public static abstract class DeviceTable {

        public static final String _ID = "_id";
        public static final String COLUMN_IMEI = "imei";
        public static final String COLUMN_TAG = "tag";
        public static final String COLUMN_DEVICE_SERIAL = "deviceserial";
        public static final String COLUMN_DEVICE_ID = "deviceid";
        public static final String COLUMN_PROJECT_NAME = "projectname";
        public static final String COLUMN_FLAG = "flag";

        public static final String _URI_SYNCDATA = "devReg.php?condition=insertData";
        public static final String _URI_GETDATA = "devReg.php?condition=getData";
    }
}
