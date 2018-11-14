package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Report;
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
public class DataReportProgresAsyncTask extends AsyncTask<Object, Integer, List<Report>> {

    private Context context;
    private String header;
    private HashMap<String, String> hashMap;

    public DataReportProgresAsyncTask(Context context, String header) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
    }

    @Override
    protected List<Report> doInBackground(Object... objects) {
        List<Report> listReport = new ArrayList<Report>();
        JSONObject reader = null;
        String url = Urls.urlmas + Urls.reportstatusprogress;
        String result = HttpOpenConnection.get(url, header);
        if (result != null) {
            try {

                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
                if (data != null) {
                    JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String id_report_status = jsonObject.has(FieldName.ID_REPORT_STATUS) ? jsonObject.getString(FieldName.ID_REPORT_STATUS) : "";
                        String nama_report_status = jsonObject.has(FieldName.NAMA_REPORT_STATUS) ? jsonObject.getString(FieldName.NAMA_REPORT_STATUS) : "";

                        Report report = new Report();
                        report.setStatus(status);
                        report.setMessage(message);
                        report.setId_report_status(id_report_status);
                        report.setNama_report_status(nama_report_status);
                        listReport.add(report);
                    }
                }

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            Report report = new Report();
            report.setStatus("0");
            report.setMessage("Could not connect to Internet");
            listReport.add(report);
        }
        return listReport;
    }
}