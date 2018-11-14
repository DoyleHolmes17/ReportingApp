package com.simap.dishub.far;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.UpdateLaporanTeknisiAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;

import java.util.HashMap;

public class ReportDetail extends AppCompatActivity {
    private Spinner spinner;
    private ImageView gambarupload;
    Bitmap FixBitmap;
    private ProgressDialog pDialog;
    private HashMap<String, String> hashMap;
    private String header, idcat, idarea, area, noteinf, alamatinf;
    private static final int CAMERA_REQUEST = 1888;
    String updateStatus;
//    private String[] stat = {
//            "On Scheduling",
//            "Progress"
//            "On Confirmation",
//            "Close"
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
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
        alamatText.setText(alamatinf);
//        gambarupload = (ImageView) findViewById(R.id.imageView);

//        spinner = (Spinner) findViewById(R.id.detailStatus);

        header = Utils.readSharedSetting(ReportDetail.this, "api_key", null);
        Log.e("header", header);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, stat);

        // mengeset Array Adapter tersebut ke Spinner
//        spinner.setAdapter(adapter);
//        if (!status.equals(null)) {
//            int spinnerPosition = adapter.getPosition(status);
//        spinner.setSelection(0);
//        }
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                //Object item = parent.getItemAtPosition(pos);
//                updateStatus = spinner.getItemAtPosition(pos).toString();
//                Log.e("spinner", updateStatus);
////                if (updateStatus.equals("Progress")) {
////                    gambarupload.setVisibility(View.VISIBLE);
////                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
////                } else {
////                    gambarupload.setVisibility(View.GONE);
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
                //Toast.makeText(ReportDetail.this, updateStatus,
                //Toast.LENGTH_LONG).show();
//                if (updateStatus.equals("Progress")) {
                    async();
//                } else {
//                    Toast.makeText(ReportDetail.this, "Status harus diubah terlebih dahulu", Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

    @SuppressLint("StaticFieldLeak")
    protected void async() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        Log.e("hashmap", hashMap + "");

        new UpdateLaporanTeknisiAsyncTask(this, hashMap, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ReportDetail.this);
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

                if (listLaporan != null){
                    Toast.makeText(ReportDetail.this, listLaporan.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ReportDetail.this, Teknisi1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportDetail.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

//    @Override
//    protected void onActivityResult(int RC, int RQC, Intent I) {
//
//        super.onActivityResult(RC, RQC, I);
//        Uri uri = I.getData(); // the uri of the image taken
//        if (String.valueOf((Bitmap) I.getExtras().get("data")).equals("null")) {
//            try {
//                FixBitmap = MediaStore.Images.Media.getBitmap(ReportDetail.this.getContentResolver(), uri);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            FixBitmap = (Bitmap) I.getExtras().get("data");
//        }
//        gambarupload.setImageBitmap(FixBitmap);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(ReportDetail.this, Teknisi1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
