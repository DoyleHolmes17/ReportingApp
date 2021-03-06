package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Via;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class ViaListAsyncTask extends AsyncTask<Object, Integer, List<Via>> {

    private Context context;
    private String header;

    public ViaListAsyncTask(Context context, String header) {
        this.context = context;
        this.header = header;
    }

    @Override
    protected List<Via> doInBackground(Object... objects) {
        List<Via> listVia = new ArrayList<Via>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.pelaporanvia;
        String result = HttpOpenConnection.get(url, header);

        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
                if (data != null) {
                    JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_pelaporan_via = jsonObject.has(FieldName.ID_PELAPORAN_VIA)
                                ? jsonObject.getString(FieldName.ID_PELAPORAN_VIA) : "";
                        String nama_pelaporan_via = jsonObject.has(FieldName.NAMA_PELAPORAN_VIA)
                                ? jsonObject.getString(FieldName.NAMA_PELAPORAN_VIA) : "";

                        Via category = new Via();
                        category.setStatus(status);
                        category.setMessage(message);
                        category.setId_pelaporan_via(id_pelaporan_via);
                        category.setNama_pelaporan_via(nama_pelaporan_via);
                        listVia.add(category);
                    }
                }
            } else {
                Via category = new Via();
                category.setStatus("0");
                category.setMessage("Could not connect to Internet");
                listVia.add(category);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return listVia;
    }
}