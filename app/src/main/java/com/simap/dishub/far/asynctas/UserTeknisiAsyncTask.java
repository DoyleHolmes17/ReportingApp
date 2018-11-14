package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Teknisi;
import com.simap.dishub.far.util.HttpOpenConnection;
import com.simap.dishub.far.util.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Aviroez on 23/04/2015.
 */
public class UserTeknisiAsyncTask extends AsyncTask<Object, Integer, List<Teknisi>> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public UserTeknisiAsyncTask(Context context, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected List<Teknisi> doInBackground(Object... objects) {
        List<Teknisi> listTeknisi = new ArrayList<Teknisi>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.userteknisi;
        String result = HttpOpenConnection.get(url, header);

        if (result != null) {
            try {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
                if (!data.equals("[]")) {
                    JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_user_teknis = jsonObject.has(FieldName.ID_USER_TEKNIS) ? jsonObject.getString(FieldName.ID_USER_TEKNIS) : "";
                        String nama_teknisi = jsonObject.has(FieldName.NAMA_TEKNISI) ? jsonObject.getString(FieldName.NAMA_TEKNISI) : "";

                        Teknisi teknisi = new Teknisi();
                        teknisi.setStatus(status);
                        teknisi.setMessage(message);
                        teknisi.setId_user_teknis(id_user_teknis);
                        teknisi.setNama_teknisi(nama_teknisi);
                        listTeknisi.add(teknisi);
                    }
                } else {
                    Teknisi teknisi = new Teknisi();
                    teknisi.setStatus("0");
                    teknisi.setMessage("Data kosong.");
                    listTeknisi.add(teknisi);
                }

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            Teknisi teknisi = new Teknisi();
            teknisi.setStatus("0");
            teknisi.setMessage("Could not connect to Internet");
            listTeknisi.add(teknisi);
        }
        return listTeknisi;
    }
}