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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.NotifTopikAsyncTask;
import com.simap.dishub.far.asynctas.UpdatePenugasanTeknisiAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;
import com.simap.dishub.far.entity.Notification;

import java.util.HashMap;

public class ReportAllProgresActivity extends AppCompatActivity {
    private Spinner spinner, teknisidaf;
    private ImageView gambarupload;
    private TextView daftartek, daftarstat;
    private EditText noteAdm;
    private ArrayAdapter areaArrayAdapter;
    Bitmap FixBitmap;
    private ProgressDialog pDialog;
    private HashMap<String, String> hashMap;
    private String header, idcat, idarea, area, noteinf, alamatinf, noteadmin, iduserteknis, namateknisi;
    private static final int CAMERA_REQUEST = 1888;
    private String updateStatus;
//    private String[] stat = {
//            "On Scheduling",
//            "Progress"
////            "On Confirmation",
////            "Close"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penugasan_teknisi);
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

        teknisidaf.setVisibility(View.GONE);
        daftartek.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        daftarstat.setVisibility(View.GONE);

        header = Utils.readSharedSetting(ReportAllProgresActivity.this, "api_key", null);
        Log.e("header", header);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, stat);

//        requestSpinnerTek();
        // mengeset Array Adapter tersebut ke Spinner
//        spinner.setAdapter(adapter);
////        if (!status.equals(null)) {
////            int spinnerPosition = adapter.getPosition(status);
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
//                    Toast.makeText(ReportAllProgresActivity.this, "Status harus diubah terlebih dahulu", Toast.LENGTH_LONG).show();
//                }
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    protected void async() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        hashMap.put(FieldName.NOTE_ADMIN, noteadmin);
        hashMap.put(FieldName.ID_USER_TEKNIS, iduserteknis);
        Log.e("hashmap", hashMap + "");

        new UpdatePenugasanTeknisiAsyncTask(this, hashMap, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
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
                    Toast.makeText(ReportAllProgresActivity.this, listLaporan.getMessage(), Toast.LENGTH_LONG).show();

                    notif();

                    finish();
                    Intent intent = new Intent(ReportAllProgresActivity.this, Pegawai1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportAllProgresActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void notif() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "teknisi");
        hashMap.put(FieldName.MESSAGE, "Penugasan Laporan (" + area + " - " + alamatinf + ")");
        Log.e("hashmap", hashMap + "");

        new NotifTopikAsyncTask(this, hashMap) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(Notification notification) {
                super.onPostExecute(notification);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                if (notification != null) {
                    Toast.makeText(ReportAllProgresActivity.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReportAllProgresActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
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
                Intent intent = new Intent(ReportAllProgresActivity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
