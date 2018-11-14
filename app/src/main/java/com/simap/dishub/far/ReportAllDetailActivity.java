package com.simap.dishub.far;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.DataReportStatusAsyncTask;
import com.simap.dishub.far.asynctas.UpdateLaporanMasukAdminAsyncTask;
import com.simap.dishub.far.entity.*;

import java.util.HashMap;
import java.util.List;

public class ReportAllDetailActivity extends AppCompatActivity {
    private Spinner spinner, teknisidaf;
    private ImageView gambarupload;
    private TextView daftartek;
    private EditText noteAdm;
    private ArrayAdapter areaArrayAdapter;
    Bitmap FixBitmap;
    private ProgressDialog pDialog;
    private HashMap<String, String> hashMap;
    private String header, idcat, idarea, area, noteinf, alamatinf, noteadmin, id_report_status, nama_report_status;
    private static final int CAMERA_REQUEST = 1888;
    private String updateStatus;
//    private String[] stat = {
//            "On Scheduling",
////            "Progress",
////            "On Confirmation",
//            "Close"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_close_progres);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        idcat = in.getStringExtra(FieldName.ID_CATEGORY);
        idarea = in.getStringExtra(FieldName.ID_AREA);
        area = in.getStringExtra(FieldName.AREA_INFRA);
        noteinf = in.getStringExtra(FieldName.NOTE_INFRA);
        alamatinf = in.getStringExtra(FieldName.ALAMAT_INFRA);
        Log.e("intent", idcat + "," + idarea + "," + area + "," + noteinf + "," + alamatinf);

        EditText areaText = (EditText) findViewById(R.id.detailAreaInfra);
        areaText.setText(area);
        EditText noteText = (EditText) findViewById(R.id.detailNoteInfra);
        noteText.setText(noteinf);
        EditText alamatText = (EditText) findViewById(R.id.detailAlamatInfra);
        daftartek = (TextView) findViewById(R.id.daftarTeknisio);
        alamatText.setText(alamatinf);
        noteAdm = (EditText) findViewById(R.id.noteAdmin);

        spinner = (Spinner) findViewById(R.id.detailStatus);
//        teknisidaf = (Spinner) findViewById(R.id.detailTeknisi);

        header = Utils.readSharedSetting(ReportAllDetailActivity.this, "api_key", null);
        Log.e("header", header);

//        // inisialiasi Array Adapter dengan memasukkan string array di atas
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, stat);

        requestSpinner();
        // mengeset Array Adapter tersebut ke Spinner
//        spinner.setAdapter(adapter);
//        if (!status.equals(null)) {
//            int spinnerPosition = adapter.getPosition(status);
//        spinner.setSelection(0);
//        }
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                updateStatus = spinner.getItemAtPosition(pos).toString();
//                Log.e("spinner", updateStatus);
////                if (updateStatus.equals("Progress")) {
////                    daftartek.setVisibility(View.VISIBLE);
////                    teknisidaf.setVisibility(View.VISIBLE);
////                } else {
////                    daftartek.setVisibility(View.GONE);
////                    teknisidaf.setVisibility(View.GONE);
////                }
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        Button update = (Button) findViewById(R.id.btnUpdateReport);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteadmin = noteAdm.getText().toString().trim();
                //Toast.makeText(ReportDetail.this, updateStatus,
                //Toast.LENGTH_LONG).show();
//                if (updateStatus.equals("Progress")) {
                asyncclose();
//                } else if (updateStatus.equals("Close")) {
//                    asyncclose();
//                } else {
//                    Toast.makeText(ReportAllDetailActivity.this, "Status harus diubah terlebih dahulu", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    protected void requestSpinner() {
        new DataReportStatusAsyncTask(this, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final List<com.simap.dishub.far.entity.Report> listReport) {
                super.onPostExecute(listReport);
                if (areaArrayAdapter == null) {
                    areaArrayAdapter = new ArrayAdapter<com.simap.dishub.far.entity.Report>(ReportAllDetailActivity.this, R.layout.custom_dropdown_menu, listReport);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    spinner.setAdapter(areaArrayAdapter);
                }
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        id_report_status = listReport.get(spinner.getSelectedItemPosition()).getId_report_status();
                        nama_report_status = listReport.get(spinner.getSelectedItemPosition()).getNama_report_status();
                        Log.e("id_report_status", id_report_status + "," + nama_report_status);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }.execute();
    }

    protected void asyncclose() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        hashMap.put(FieldName.NOTE_ADMIN, noteadmin);
        hashMap.put(FieldName.ID_REPORT_STATUS, id_report_status);
        Log.e("hashmap", hashMap + "");

        new UpdateLaporanMasukAdminAsyncTask(this, hashMap, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ReportAllDetailActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(ListLaporan listLaporan) {
                super.onPostExecute(listLaporan);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                Log.e("listlaporan", listLaporan + "");
                if (listLaporan != null) {
                    Toast.makeText(ReportAllDetailActivity.this, "Berhasil update status laporan.", Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ReportAllDetailActivity.this, Pegawai1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportAllDetailActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(ReportAllDetailActivity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
