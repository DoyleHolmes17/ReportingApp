package com.simap.dishub.far.asynctas;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Laporan;
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
public class GetLaporanAsyncTask extends AsyncTask<Object, Integer, List<Laporan>> {

    private Context context;
    private String header, page;
    private HashMap<String, String> hashMap;

    public GetLaporanAsyncTask(Context context, String header, String page) {
        this.context = context;
        this.hashMap = hashMap;
        this.header = header;
        this.page = page;
    }

    @Override
    protected List<Laporan> doInBackground(Object... objects) {
        List<Laporan> listLaporan = new ArrayList<Laporan>();
        JSONObject reader = null;
        String url = Urls.urllaporan + Urls.laporanuser;
        String result = HttpOpenConnection.get1(url, header, page);
        Log.e("result", result);

        if (result != null) {
            try {
                Log.e("result", result);
                reader = new JSONObject(result);
                String status = reader.has(FieldName.STATUS) ? reader.getString(FieldName.STATUS) : "0";
                String message = reader.has(FieldName.MESSAGE) ? reader.getString(FieldName.MESSAGE) : "0";
                String data = reader.has(FieldName.DATA) ? reader.getString(FieldName.DATA) : null;
//                if (data != null) {
                    if (!data.equals("[]")) {
                        JSONArray jsonArray = reader.getJSONArray(FieldName.DATA);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String id_laporan = jsonObject.has(FieldName.ID_LAPORAN) ? jsonObject.getString(FieldName.ID_LAPORAN) : "";
                            String rep_number = jsonObject.has(FieldName.REP_NUMBER) ? jsonObject.getString(FieldName.REP_NUMBER) : "";
                            String id_category_infra = jsonObject.has(FieldName.ID_CATEGORY_INF) ? jsonObject.getString(FieldName.ID_CATEGORY_INF) : "";
                            String id_area_infra = jsonObject.has(FieldName.ID_AREA_INF) ? jsonObject.getString(FieldName.ID_AREA_INF) : "";
                            String status_report = jsonObject.has(FieldName.STATUS_REPORT) ? jsonObject.getString(FieldName.STATUS_REPORT) : "";
                            String rep_date = jsonObject.has(FieldName.REP_DATE) ? jsonObject.getString(FieldName.REP_DATE) : "";
                            String note_infra = jsonObject.has(FieldName.NOTE_INFRA) ? jsonObject.getString(FieldName.NOTE_INFRA) : "";
                            String lokasi_infra = jsonObject.has(FieldName.LOKASI_INFRA) ? jsonObject.getString(FieldName.LOKASI_INFRA) : "";
                            String alamat_infra = jsonObject.has(FieldName.ALAMAT_INFRA) ? jsonObject.getString(FieldName.ALAMAT_INFRA) : "";
                            String category_infra = jsonObject.has(FieldName.CATEGORY_INFRA) ? jsonObject.getString(FieldName.CATEGORY_INFRA) : "";
                            String sub_category_infra = jsonObject.has(FieldName.SUB_CATEGORY_INFRA) ? jsonObject.getString(FieldName.SUB_CATEGORY_INFRA) : "";
                            String tanggal_pelaporan = jsonObject.has(FieldName.TANGGAL_PELAPORAN) ? jsonObject.getString(FieldName.TANGGAL_PELAPORAN) : "";
                            String img_pelapor = jsonObject.has(FieldName.IMG_PELAPOR) ? jsonObject.getString(FieldName.IMG_PELAPOR) : "";
                            String pelaporan_via = jsonObject.has(FieldName.PELAPORAN_VIA) ? jsonObject.getString(FieldName.PELAPORAN_VIA) : "";

                            Laporan laporan = new Laporan();
                            laporan.setId_laporan(id_laporan);
                            laporan.setStatus(status);
                            laporan.setMessage(message);
                            laporan.setRep_number(rep_number);
                            laporan.setId_category_infra(id_category_infra);
                            laporan.setId_area_infra(id_area_infra);
                            laporan.setStatus_report(status_report);
                            laporan.setRep_date(rep_date);
                            laporan.setNote_infra(note_infra);
                            laporan.setLokasi_infra(lokasi_infra);
                            laporan.setAlamat_infra(alamat_infra);
                            laporan.setCategory_infra(category_infra);
                            laporan.setSub_category_infra(sub_category_infra);
                            laporan.setTanggal_pelaporan(tanggal_pelaporan);
                            laporan.setImg_pelapor(img_pelapor);
                            laporan.setPelaporan_via(pelaporan_via);
                            listLaporan.add(laporan);
                        }
                    } else {
                        Laporan laporan = new Laporan();
                        laporan.setStatus("0");
                        laporan.setMessage("Data Kosong.");
                        listLaporan.add(laporan);
                    }
//                }

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            Laporan laporan = new Laporan();
            laporan.setStatus("0");
            laporan.setMessage("Could not connect to Internet");
            listLaporan.add(laporan);
        }
        return listLaporan;
    }
}