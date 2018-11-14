package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Notification;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class NotifTopikAsyncTask extends AsyncTask<Object, Integer, Notification> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public NotifTopikAsyncTask(Context context, HashMap<String, String> hashMap) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected Notification doInBackground(Object... objects) {
        JSONObject reader = null;
        Notification notif = new Notification();
        String url = Urls.urllaporan + Urls.notiftopic;
        String result = HttpOpenConnection.post(url, hashMap);

        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String mess_id = reader.has(FieldName.MESSAGE_ID) ? reader.getString(FieldName.MESSAGE_ID) : "0";

                notif = new Notification();
                notif.setMessage_id(mess_id);
                notif.setMessage("Sukses");

            } else {
                notif = new Notification();
                notif.setMessage_id("0");
                notif.setMessage("Could not connect to Internet");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return notif;
    }
}