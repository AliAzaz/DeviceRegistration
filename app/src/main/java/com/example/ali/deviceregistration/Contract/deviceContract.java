package com.example.ali.deviceregistration.Contract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ali.azaz on 5/4/2017.
 */

public class deviceContract {

    String _iddeviceinfo = "";
    String imei = "";
    String tag = "";
    String deviceserial	 = "";
    String deviceid	 = "";
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

    public void set_iddeviceinfo(String _iddeviceinfo) {
        this._iddeviceinfo = _iddeviceinfo;
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

        json.put("imei", this.imei== null?JSONObject.NULL:this.imei);
        json.put("tag", this.tag== null?JSONObject.NULL:this.tag);
        json.put("deviceserial", this.deviceserial== null?JSONObject.NULL:this.deviceserial);
        json.put("deviceid", this.deviceid== null?JSONObject.NULL:this.deviceid);
        json.put("projectname", this.projectName== null?JSONObject.NULL:this.projectName);
        json.put("flag", this.flag== null?JSONObject.NULL:this.flag);

        return json;
    }

    public JSONObject toAlreadyJSONObject() throws JSONException {
        JSONObject json = new JSONObject();

        json.put("_iddeviceinfo", this._iddeviceinfo== null?JSONObject.NULL:this._iddeviceinfo);
        json.put("projectname", this.projectName== null?JSONObject.NULL:this.projectName);
        json.put("flag", this.flag== null?JSONObject.NULL:this.flag);

        return json;
    }

    public deviceContract(JSONObject jsonObject) throws JSONException {
        this.imei = jsonObject.getString("imei");
        this.tag = jsonObject.getString("tag");
        this.deviceserial = jsonObject.getString("deviceserial");
        this.deviceid = jsonObject.getString("deviceid");
        this._id = jsonObject.getString("_id");
    }

    public deviceContract() {
    }
}
