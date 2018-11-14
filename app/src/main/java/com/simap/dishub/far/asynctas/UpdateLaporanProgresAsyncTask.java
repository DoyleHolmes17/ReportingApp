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
public class UpdateLaporanProgresAsyncTask extends AsyncTask<Object, Integer, ListLaporan> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public UpdateLaporanProgresAsyncTask(Context context, HashMap<String, String> hashMap, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected ListLaporan doInBackground(Object... objects) {
        JSONObject reader = null;
        ListLaporan laporan = new ListLaporan();
        String url = Urls.urllaporan + Urls.updatelaporanprogress;
        String result = HttpOpenConnection.postheader(url, hashMap, header);

        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
                if (data != null) {
                    JSONObject jsonObject = new JSONObject(data);
                    String id_reportstatus = jsonObject.has(FieldName.ID_REPORTSTAT) ? jsonObject.getString(FieldName.ID_REPORTSTAT) : "";
                    String note_admin = jsonObject.has(FieldName.NOTE_ADMIN) ? jsonObject.getString(FieldName.NOTE_ADMIN) : "";
                    String end_teknisi_confir = jsonObject.has(FieldName.END_TEKNISI_CONF) ? jsonObject.getString(FieldName.END_TEKNISI_CONF) : "";
                    String end_teknis = jsonObject.has(FieldName.END_TEKNIS) ? jsonObject.getString(FieldName.END_TEKNIS) : "";
                    String updated_id = jsonObject.has(FieldName.UPDATE_ID) ? jsonObject.getString(FieldName.UPDATE_ID) : "";
                    String updated_at = jsonObject.has(FieldName.UPDATE_AT) ? jsonObject.getString(FieldName.UPDATE_AT) : "";

                    laporan = new ListLaporan();
                    laporan.setStatus(status);
                    laporan.setMessage(message);
                    laporan.setId_reportstatus(id_reportstatus);
                    laporan.setNote_admin(note_admin);
                    laporan.setEnd_teknisi_confir(end_teknisi_confir);
                    laporan.setEnd_teknis(end_teknis);
                    laporan.setUpdated_id(updated_id);
                    laporan.setUpdated_at(updated_at);
                } else {
                    laporan = new ListLaporan();
                    laporan.setStatus(status);
                    laporan.setMessage(message);
                }
            } else {
                laporan = new ListLaporan();
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