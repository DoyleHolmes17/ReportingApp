package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;
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
public class ListLaporanTeknisiAsyncTask extends AsyncTask<Object, Integer, List<ListLaporan>> {

    private Context context;
    private String header, page;
    private HashMap<String, String> hashMap;

    public ListLaporanTeknisiAsyncTask(Context context, String header, String page) {
        this.context = context;
        this.header = header;
        this.page = page;
    }

    @Override
    protected List<ListLaporan> doInBackground(Object... objects) {
        List<ListLaporan> listLaporan = new ArrayList<ListLaporan>();
        JSONObject reader = null;
        String url = Urls.urllaporan + Urls.laporanteknisifollowup;
        String result = HttpOpenConnection.get1(url, header, page);

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
                        String jumlah_pelapor = jsonObject.has(FieldName.JUMLAH_PELAPOR) ? jsonObject.getString(FieldName.JUMLAH_PELAPOR) : "";
                        String wilayah_infra = jsonObject.has(FieldName.WILAYAH_INFRA) ? jsonObject.getString(FieldName.WILAYAH_INFRA) : "";
                        String tanggal_pelaporan = jsonObject.has(FieldName.TANGGAL_PELAPORAN) ? jsonObject.getString(FieldName.TANGGAL_PELAPORAN) : "";
                        String note_infra = jsonObject.has(FieldName.NOTE_INFRA) ? jsonObject.getString(FieldName.NOTE_INFRA) : "";
                        String status_report = jsonObject.has(FieldName.STATUS_REPORT) ? jsonObject.getString(FieldName.STATUS_REPORT) : "";
                        String id_reportstatus = jsonObject.has(FieldName.ID_REPORTSTAT) ? jsonObject.getString(FieldName.ID_REPORTSTAT) : "";
                        String lokasi_infra = jsonObject.has(FieldName.LOKASI_INFRA) ? jsonObject.getString(FieldName.LOKASI_INFRA) : "";
                        String alamat_infra = jsonObject.has(FieldName.ALAMAT_INFRA) ? jsonObject.getString(FieldName.ALAMAT_INFRA) : "";
                        String category_infra = jsonObject.has(FieldName.CATEGORY_INFRA) ? jsonObject.getString(FieldName.CATEGORY_INFRA) : "";
                        String sub_category_infra = jsonObject.has(FieldName.SUB_CATEGORY_INFRA) ? jsonObject.getString(FieldName.SUB_CATEGORY_INFRA) : "";
                        String id_category_infra = jsonObject.has(FieldName.ID_CATEGORY_INF) ? jsonObject.getString(FieldName.ID_CATEGORY_INF) : "";
                        String id_area_infra = jsonObject.has(FieldName.ID_AREA_INF) ? jsonObject.getString(FieldName.ID_AREA_INF) : "";
                        String img_pelapor = jsonObject.has(FieldName.IMG_PELAPOR) ? jsonObject.getString(FieldName.IMG_PELAPOR) : "";

                        ListLaporan laporan = new ListLaporan();
                        laporan.setStatus(status);
                        laporan.setMessage(message);
                        laporan.setJumlah_pelapor(jumlah_pelapor);
                        laporan.setWilayah_infra(wilayah_infra);
                        laporan.setStatus_report(status_report);
                        laporan.setTanggal_pelaporan(tanggal_pelaporan);
                        laporan.setNote_infra(note_infra);
                        laporan.setId_reportstatus(id_reportstatus);
                        laporan.setLokasi_infra(lokasi_infra);
                        laporan.setAlamat_infra(alamat_infra);
                        laporan.setId_category_infra(id_category_infra);
                        laporan.setCategory_infra(category_infra);
                        laporan.setSub_category_infra(sub_category_infra);
                        laporan.setId_area_infra(id_area_infra);
                        laporan.setImg_pelapor(img_pelapor);
                        listLaporan.add(laporan);
                    }
                }
            } else {
                ListLaporan laporan = new ListLaporan();
                laporan.setStatus("0");
                laporan.setMessage("Could not connect to Internet");
                listLaporan.add(laporan);
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return listLaporan;
    }
}