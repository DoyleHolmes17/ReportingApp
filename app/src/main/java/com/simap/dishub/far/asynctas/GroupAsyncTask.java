package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Group;
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
public class GroupAsyncTask extends AsyncTask<Object, Integer, List<Group>> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public GroupAsyncTask(Context context, String header) {
        this.context = context;
        this.header = header;
    }

    @Override
    protected List<Group> doInBackground(Object... objects) {
        List<Group> lstGrp = new ArrayList<Group>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.groupuser;
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
                        String idgrp = jsonObject.has(FieldName.ID_GROUP) ? jsonObject.getString(FieldName.ID_GROUP) : "";
                        String namagrp = jsonObject.has(FieldName.NAMA_GROUP) ? jsonObject.getString(FieldName.NAMA_GROUP) : "";

                        Group listgroup = new Group();
                        listgroup.setStatus(status);
                        listgroup.setMessage(message);
                        listgroup.setId_group(idgrp);
                        listgroup.setNama_group(namagrp);
                        lstGrp.add(listgroup);
                    }
                }
            } else {
                Group listgroup = new Group();
                listgroup.setStatus("0");
                listgroup.setMessage("Could not connect to Internet");
                lstGrp.add(listgroup);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return lstGrp;
    }
}