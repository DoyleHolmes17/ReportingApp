package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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
public class DeleteLaporanAsyncTask extends AsyncTask<Object, Integer, Laporan> {

    private Context context;
    private String header, page;
    private HashMap<String, String> hashMap;

    public DeleteLaporanAsyncTask(Context context, HashMap<String, String> hashMap, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
        this.page = page;
    }

    @Override
    protected Laporan doInBackground(Object... objects) {
        Laporan listLaporan = new Laporan();
        JSONObject reader = null;
        String url = Urls.urllaporan + Urls.cancellaporan;
        String result = HttpOpenConnection.postheader(url, hashMap, header);
        Log.e("result", result);

//        {"status":true,"message":"Cancel Laporan Berhasil","data":{"is_deleted":"1"}}

        if (result != null) {
            try {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";

                listLaporan = new Laporan();
                listLaporan.setStatus(status);
                listLaporan.setMessage(message);
            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            listLaporan = new Laporan();
            listLaporan.setStatus("0");
            listLaporan.setMessage("Could not connect to Internet");
        }
        return listLaporan;
    }
}