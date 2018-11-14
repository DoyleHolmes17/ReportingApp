package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.Area;
import com.simap.dishub.far.entity.FieldName;
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
public class AreaListAsyncTask extends AsyncTask<Object, Integer, List<Area>> {

    private Context context;
    private String header;

    public AreaListAsyncTask(Context context, String header) {
        this.context = context;
        this.header = header;
    }

    @Override
    protected void onPostExecute(List<Area> o) {
    }

    @Override
    protected List<Area> doInBackground(Object... objects) {
        List<Area> listArea = new ArrayList<Area>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.area;
        String result = HttpOpenConnection.get(url, header);

        try {
            if (result != null) {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : "0";
                if (data != "0") {
                    JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_area = jsonObject.has(FieldName.ID_AREA) ? jsonObject.getString(FieldName.ID_AREA) : "";
                        String area_infra = jsonObject.has(FieldName.AREA_INFRA) ? jsonObject.getString(FieldName.AREA_INFRA) : "";
                        String alamat_infra = jsonObject.has(FieldName.ALAMAT_INFRA) ? jsonObject.getString(FieldName.ALAMAT_INFRA) : "";
                        String id_wilayah = jsonObject.has(FieldName.ID_WILAYAH) ? jsonObject.getString(FieldName.ID_WILAYAH) : "";

                        Area area = new Area();
                        area.setStatus(status);
                        area.setMessage(message);
                        area.setId_area(id_area);
                        area.setArea_infra(area_infra);
                        area.setAlamat_infra(alamat_infra);
                        area.setId_wilayah(id_wilayah);
                        listArea.add(area);
                    }
                } else {
                    Area area = new Area();
                    area.setStatus(status);
                    area.setMessage(message);
                    listArea.add(area);
                }
            } else {
                Area area = new Area();
                area.setStatus("0");
                area.setMessage("Could not connect to Internet");
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return listArea;
    }
}