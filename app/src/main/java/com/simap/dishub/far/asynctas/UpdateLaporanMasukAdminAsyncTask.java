package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class UpdateLaporanMasukAdminAsyncTask extends AsyncTask<Object, Integer, ListLaporan> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public UpdateLaporanMasukAdminAsyncTask(Context context, HashMap<String, String> hashMap, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected ListLaporan doInBackground(Object... objects) {
        JSONObject reader = null;
        ListLaporan laporan = new ListLaporan();
        String url = Urls.urllaporan + Urls.updatelaporanmasukadmin;
        String result = HttpOpenConnection.postheader(url, hashMap, header);
        Log.e("results", result);
        if (result != null) {
            try {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
//                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
//                if (data != null) {
//                    JSONObject jsonObject = new JSONObject(data);
//                    String id_reportstatus = jsonObject.has(FieldName.ID_REPORTSTAT) ? jsonObject.getString(FieldName.ID_REPORTSTAT) : "";
//
                laporan = new ListLaporan();
                laporan.setStatus(status);
                laporan.setMessage(message);
//                    laporan.setId_reportstatus(id_reportstatus);
//                }

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }

        } else {
            laporan = new ListLaporan();
            laporan.setStatus("0");
            laporan.setMessage("Could not connect to Internet");
        }

        return laporan;
    }
}