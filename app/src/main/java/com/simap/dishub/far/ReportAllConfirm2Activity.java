package com.simap.dishub.far;

import android.annotation.SuppressLint;
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

import com.simap.dishub.far.asynctas.DataReportConfirmAsyncTask;
import com.simap.dishub.far.asynctas.UpdateLaporanConfirmAsyncTask;
import com.simap.dishub.far.asynctas.UserTeknisiAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;
import com.simap.dishub.far.entity.Report;
import com.simap.dishub.far.entity.Teknisi;

import java.util.HashMap;
import java.util.List;

public class ReportAllConfirm2Activity extends AppCompatActivity {
    private Spinner spinner, teknisidaf;
    private ImageView gambarupload;
    private TextView daftartek, daftarstat;
    private EditText noteAdm;
    private ArrayAdapter areaArrayAdapter;
    private ArrayAdapter catArrayAdapter;
    Bitmap FixBitmap;
    private ProgressDialog pDialog;
    private HashMap<String, String> hashMap;
    private String header, idcat, idarea, area, noteinf, alamatinf, noteadmin, iduserteknis, namateknisi, id_report_status, nama_report_status;
    private static final int CAMERA_REQUEST = 1888;
    private String updateStatus;
//    private String[] stat = {
////            "On Scheduling",
//            "Progress",
//            "On Confirmation"
////            "Close"
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
        daftarstat = (TextView) findViewById(R.id.detailStatusInfra);
        alamatText.setText(alamatinf);
        noteAdm = (EditText) findViewById(R.id.noteAdmin);

        spinner = (Spinner) findViewById(R.id.detailStatus);
        teknisidaf = (Spinner) findViewById(R.id.detailTeknisi);

//        teknisidaf.setVisibility(View.VISIBLE);
//        daftartek.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        daftarstat.setVisibility(View.VISIBLE);

        header = Utils.readSharedSetting(ReportAllConfirm2Activity.this, "api_key", null);
        Log.e("header", header);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, stat);

//        requestSpinnerTek();
        requestSpinnerStat();
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
//                if (updateStatus.equals("Progress")) {
//                    daftartek.setVisibility(View.VISIBLE);
//                    teknisidaf.setVisibility(View.VISIBLE);
//                } else {
//                    daftartek.setVisibility(View.GONE);
//                    teknisidaf.setVisibility(View.GONE);
//                }
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
                async();
//                } else {
//                    Toast.makeText(ReportAllConfirmActivity.this, "Status harus diubah terlebih dahulu", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    protected void requestSpinnerTek() {
        new UserTeknisiAsyncTask(this, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final List<Teknisi> listTeknisi) {
                super.onPostExecute(listTeknisi);
                if (catArrayAdapter == null) {
                    catArrayAdapter = new ArrayAdapter<Teknisi>(ReportAllConfirm2Activity.this, R.layout.custom_dropdown_menu, listTeknisi);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    teknisidaf.setAdapter(catArrayAdapter);
                }
                teknisidaf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        iduserteknis = listTeknisi.get(teknisidaf.getSelectedItemPosition()).getId_user_teknis();
                        namateknisi = listTeknisi.get(teknisidaf.getSelectedItemPosition()).getNama_teknisi();
                        Log.e("idteknis", iduserteknis);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void requestSpinnerStat() {
        new DataReportConfirmAsyncTask(this, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final List<Report> listReport) {
                super.onPostExecute(listReport);
                if (areaArrayAdapter == null) {
                    areaArrayAdapter = new ArrayAdapter<Report>(ReportAllConfirm2Activity.this, R.layout.custom_dropdown_menu, listReport);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    spinner.setAdapter(areaArrayAdapter);
                }

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        id_report_status = listReport.get(spinner.getSelectedItemPosition()).getId_report_status();
                        nama_report_status = listReport.get(spinner.getSelectedItemPosition()).getNama_report_status();
                        Log.e("idstatus", id_report_status);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void async() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        hashMap.put(FieldName.NOTE_ADMIN, noteadmin);
        hashMap.put(FieldName.ID_REPORT_STATUS, id_report_status);
        hashMap.put(FieldName.ID_USER_TEKNIS, iduserteknis);
        Log.e("hashmap", hashMap + "");

        new UpdateLaporanConfirmAsyncTask(this, hashMap, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ReportAllConfirm2Activity.this);
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

                if (listLaporan != null) {

                    Toast.makeText(ReportAllConfirm2Activity.this, listLaporan.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ReportAllConfirm2Activity.this, Pegawai1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportAllConfirm2Activity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(ReportAllConfirm2Activity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
