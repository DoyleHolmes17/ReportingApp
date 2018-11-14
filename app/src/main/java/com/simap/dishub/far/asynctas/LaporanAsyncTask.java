package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.Utils;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Laporan;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class LaporanAsyncTask extends AsyncTask<Object, Integer, Laporan> {

    private Context context;
    private String header, jenisakun, url;
    private HashMap<String, String> hashMap;

    public LaporanAsyncTask(Context context, HashMap<String, String> hashMap, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected Laporan doInBackground(Object... objects) {
        Log.e("background", "ok");
        jenisakun = Utils.readSharedSetting(context, "jenisakun", "kosong");
        JSONObject reader = null;
        Laporan laporan = new Laporan();
        if (jenisakun.equals("admin")) {
            url = Urls.urllaporan + Urls.inputlaporanadmin;
        } else {
            url = Urls.urllaporan + Urls.inputlaporan;
        }
        String result = HttpOpenConnection.postheader(url, hashMap, header);
        Log.e("result", result);
        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;

                if (data != null) {
                    JSONObject jsonObject = new JSONObject(data);
                    String id = jsonObject.has(FieldName.ID) ? jsonObject.getString(FieldName.ID) : "";
                    String id_user = jsonObject.has(FieldName.ID_USER) ? jsonObject.getString(FieldName.ID_USER) : "";
                    String rep_number = jsonObject.has(FieldName.REP_NUMBER) ? jsonObject.getString(FieldName.REP_NUMBER) : "";

                    laporan = new Laporan();
                    laporan.setStatus(status);
                    laporan.setMessage(message);
                    laporan.setId(id);
                    laporan.setId_user(id_user);
                    laporan.setRep_number(rep_number);
                } else {
                    laporan = new Laporan();
                    laporan.setStatus(status);
                    laporan.setMessage(message);
                }
            } else {
                laporan = new Laporan();
                laporan.setStatus("0");
                laporan.setMessage("Could not connect to Internet");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return laporan;
    }
}