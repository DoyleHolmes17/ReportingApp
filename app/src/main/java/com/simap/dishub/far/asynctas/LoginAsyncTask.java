package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.Area;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Login;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class LoginAsyncTask extends AsyncTask<Object, Integer, Login> {
    private HashMap<String, String> hashMap;
    private Context context;

    public LoginAsyncTask(Context context, HashMap<String, String> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
    }

    @Override
    protected Login doInBackground(Object... objects) {
        Login login = new Login();
        String url = Urls.urlauth + Urls.login;
        String result = HttpOpenConnection.post(url, hashMap);
        Log.e("result", result);

        try {
            if (result != null) {
                Log.e("result", result);
                JSONObject reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "";
                String api_key1 = reader.has(FieldName.API_KEY) ? reader.getString(FieldName.API_KEY) : "0";
                if (status.equals("true")) {
                    JSONObject json = new JSONObject(api_key1);
                    String api_key = json.has(FieldName.API_KEY) ? json.getString(FieldName.API_KEY) : "";
                    String fullname = json.has(FieldName.FULLNAME) ? json.getString(FieldName.FULLNAME) : "";
                    String grp_group_id = json.has(FieldName.GRP_GROUP_ID) ? json.getString(FieldName.GRP_GROUP_ID) : "";
                    String name = json.has(FieldName.NAME) ? json.getString(FieldName.NAME) : "";

                    login = new Login();
                    login.setStatus(status);
                    login.setMessage(message);
                    login.setApi_key(api_key);
                    login.setFullname(fullname);
                    login.setGrp_group_id(grp_group_id);
                    login.setName(name);
                } else {
                    login = new Login();
                    login.setStatus(status);
                    login.setMessage(message);
                }
            } else {
                Area area = new Area();
                area.setStatus("false");
                area.setMessage("Could not connect to Internet");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return login;
    }
}