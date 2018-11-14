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
public class ForgetAsyncTask extends AsyncTask<Object, Integer, Login> {
    private HashMap<String, String> hashMap;
    private Context context;

    public ForgetAsyncTask(Context context, HashMap<String, String> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
    }

    @Override
    protected Login doInBackground(Object... objects) {
        Login login = new Login();
        String url = Urls.urlauth + Urls.forgotpassword;
        String result = HttpOpenConnection.post(url, hashMap);
        Log.e("result", result);

        try {
            if (result != null) {
                Log.e("result", result);
                JSONObject reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "";

                login = new Login();
                login.setStatus(status);
                login.setMessage(message);

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