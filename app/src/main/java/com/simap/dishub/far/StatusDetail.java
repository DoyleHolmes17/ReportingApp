package com.simap.dishub.far;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class StatusDetail extends AppCompatActivity {
    private Spinner spinner;
    String updateStatus;
    private String[] stat = {
            "Aktif",
            "Tidak Aktif"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        final String iduser = in.getStringExtra(("iduser"));
        final String nama = in.getStringExtra(("nama"));
        final String email = in.getStringExtra(("email"));
        final String status = in.getStringExtra(("status"));

        EditText namaText = (EditText) findViewById(R.id.detailNamaUser);
        namaText.setText(nama);
        EditText emailText = (EditText) findViewById(R.id.detailEmailUser);
        emailText.setText(email);

        spinner = (Spinner) findViewById(R.id.StatusTeknisi);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, stat);

        // mengeset Array Adapter tersebut ke Spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                //Object item = parent.getItemAtPosition(pos);
                updateStatus = spinner.getItemAtPosition(pos).toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Button update = (Button) findViewById(R.id.btnUpdateTeknisi);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(StatusDetail.this, updateStatus,
                        Toast.LENGTH_LONG).show();
                //new StatusDetail.UpdateStatus().execute(iduser, nama, email, updateStatus);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class UpdateStatus extends AsyncTask<String, String, JSONObject> {

        ProgressDialog pdLoading = new ProgressDialog(StatusDetail.this);
        HttpURLConnection conn;
        URL url = null;
        JSONParser jsonParser = new JSONParser();
        private static final String TAG_INFO = "info";
        private static final String STATUS_URL = "http://182.253.200.185/dishub2/index.php/c_status_user";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("iduser", args[0]);
                params.put("nama", args[1]);
                params.put("email", args[2]);
                params.put("updateStatus", args[3]);


                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        STATUS_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String info = "";


            if (json != null) {
                //Toast.makeText(LoginActivity.this, json.toString(),
                //Toast.LENGTH_LONG).show();

                try {
                    info = json.getString(TAG_INFO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if(info.equals("Success")) {
                pdLoading.dismiss();
                Toast.makeText(StatusDetail.this, "Status Teknisi Success Updated",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StatusDetail.this, StatusActivity.class);
                startActivity(intent);
                finish();
            }else{
                pdLoading.dismiss();
                Toast.makeText(StatusDetail.this, "Failed Try Again !!",
                        Toast.LENGTH_LONG).show();
            }

        }

    }
}
