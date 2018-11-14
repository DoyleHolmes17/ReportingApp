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
public class ListLaporanConfirmAsyncTask extends AsyncTask<Object, Integer, List<ListLaporan>> {

    private Context context;
    private String header, page;
    private HashMap<String, String> hashMap;

    public ListLaporanConfirmAsyncTask(Context context, String header, String page) {
        this.context = context;
        this.header = header;
        this.page = page;
    }

    @Override
    protected List<ListLaporan> doInBackground(Object... objects) {
        List<ListLaporan> listLaporan = new ArrayList<ListLaporan>();
        JSONObject reader = null;
        String url = Urls.urllaporan + Urls.laporanteknisiconfirmation;

        String result = HttpOpenConnection.get1(url, header, page);

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
                        String jumlah_pelapor = jsonObject.has(FieldName.JUMLAH_PELAPOR) ? jsonObject.getString(FieldName.JUMLAH_PELAPOR) : "";
                        String wilayah_infra = jsonObject.has(FieldName.WILAYAH_INFRA) ? jsonObject.getString(FieldName.WILAYAH_INFRA) : "";
                        String tanggal_pelaporan = jsonObject.has(FieldName.TANGGAL_PELAPORAN) ? jsonObject.getString(FieldName.TANGGAL_PELAPORAN) : "";
                        String note_infra = jsonObject.has(FieldName.NOTE_INFRA) ? jsonObject.getString(FieldName.NOTE_INFRA) : "";
                        String status_report = jsonObject.has(FieldName.STATUS_REPORT) ? jsonObject.getString(FieldName.STATUS_REPORT) : "";
                        String id_reportstatus = jsonObject.has(FieldName.ID_REPORTSTAT) ? jsonObject.getString(FieldName.ID_REPORTSTAT) : "";
                        String lokasi_infra = jsonObject.has(FieldName.LOKASI_INFRA) ? jsonObject.getString(FieldName.LOKASI_INFRA) : "";
                        String alamat_infra = jsonObject.has(FieldName.ALAMAT_INFRA) ? jsonObject.getString(FieldName.ALAMAT_INFRA) : "";
                        String categoryinfra = jsonObject.has(FieldName.CATEGORYINFRA) ? jsonObject.getString(FieldName.CATEGORYINFRA) : "";
//                        String sub_category_infra = jsonObject.has(FieldName.SUB_CATEGORY_INFRA) ? jsonObject.getString(FieldName.SUB_CATEGORY_INFRA) : "";
                        String id_category_infra = jsonObject.has(FieldName.ID_CATEGORY_INF) ? jsonObject.getString(FieldName.ID_CATEGORY_INF) : "";
                        String id_area_infra = jsonObject.has(FieldName.ID_AREA_INF) ? jsonObject.getString(FieldName.ID_AREA_INF) : "";
                        String id_userteknis = jsonObject.has(FieldName.ID_USERTEKNIS) ? jsonObject.getString(FieldName.ID_USERTEKNIS) : "";
                        String mulai_pengerjaan = jsonObject.has(FieldName.MULAI_PENGERJAAN) ? jsonObject.getString(FieldName.MULAI_PENGERJAAN) : "";
                        String selesai_pengerjaan = jsonObject.has(FieldName.SELESAI_PENGERJAAN) ? jsonObject.getString(FieldName.SELESAI_PENGERJAAN) : "";
                        String lama_pengerjaan = jsonObject.has(FieldName.LAMA_PENGERJAAN) ? jsonObject.getString(FieldName.LAMA_PENGERJAAN) : "";
                        String nama_teknisi = jsonObject.has(FieldName.NAMA_TEKNISI) ? jsonObject.getString(FieldName.NAMA_TEKNISI) : "";
                        String img_pelapor = jsonObject.has(FieldName.IMG_PELAPOR) ? jsonObject.getString(FieldName.IMG_PELAPOR) : "";
                        String img_konfirmasi = jsonObject.has(FieldName.IMG_KONFIRMASI) ? jsonObject.getString(FieldName.IMG_KONFIRMASI) : "";

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
                        laporan.setCategory_infra(categoryinfra);
                        laporan.setId_userteknis(id_userteknis);
                        laporan.setId_area_infra(id_area_infra);
                        laporan.setMulai_pengerjaan(mulai_pengerjaan);
                        laporan.setSelesai_pengerjaan(selesai_pengerjaan);
                        laporan.setLama_pengerjaan(lama_pengerjaan);
                        laporan.setNama_teknisi(nama_teknisi);
                        laporan.setImg_pelapor(img_pelapor);
                        laporan.setImg_konfirmasi(img_konfirmasi);
                        listLaporan.add(laporan);
                    }
                } else {
                    ListLaporan laporan = new ListLaporan();
                    laporan.setStatus("0");
                    laporan.setMessage("Data kosong.");
                    listLaporan.add(laporan);
                }

            } catch (JSONException e) {
                Log.e("JSONException", e.getMessage());
            } catch (Exception e) {
                Log.e("Exception", e.getMessage());
            }
        } else {
            ListLaporan laporan = new ListLaporan();
            laporan.setStatus("0");
            laporan.setMessage("Could not connect to Internet");
            listLaporan.add(laporan);
        }
        return listLaporan;
    }
}