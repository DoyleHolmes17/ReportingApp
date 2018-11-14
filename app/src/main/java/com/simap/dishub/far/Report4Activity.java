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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Report4Activity extends AppCompatActivity {

    private String TAG = Report4Activity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;

    // URL to get contacts JSON
    private static String url = "http://182.253.200.185/dishub2/index.php/c_report_close";

    ArrayList<HashMap<String, String>> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report4);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reportList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.listLaporan);
        new Report4Activity.GetLaporan().execute();
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

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetLaporan extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(Report4Activity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray contacts = jsonObj.getJSONArray("listreport");

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        String id = c.getString("id");
                        String note = c.getString("note_infra");
                        String status = c.getString("report_status");
                        String area = c.getString("area_infra");
                        String alamat = c.getString("alamat_infra");
                        String start = c.getString("start_teknis");
                        String proses = c.getString("end_teknisi_confir");
                        String end = c.getString("end_teknis");
                        // tmp hash map for single contact
                        HashMap<String, String> contact = new HashMap<>();

                        // adding each child node to HashMap key => value
                        contact.put("id", id);
                        contact.put("note","Note Pelapor: "+ note);
                        contact.put("status", status);
                        contact.put("area", area);
                        contact.put("alamat", alamat);
                        contact.put("start","Start Teknis: "+ start);
                        contact.put("proses","Confirmation: "+ proses);
                        contact.put("end","Close: "+ end);

                        // adding contact to contact list
                        reportList.add(contact);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Data Kosong",
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            final ListAdapter adapter = new SimpleAdapter(
                    Report4Activity.this, reportList,
                    R.layout.list_laporan, new String[]{"area", "note", "alamat", "start", "proses", "end",
                    "status"}, new int[]{R.id.textareaInfra,
                    R.id.textnoteInfra, R.id.textalamatInfra,R.id.textstartInfra,R.id.textprosesInfra,R.id.textendInfra, R.id.textstatusReport});

            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) adapter.getItem(position);
                    String area = (String) obj.get("area");
                    String note = (String) obj.get("note");
                    String alamat = (String) obj.get("alamat");
                    String status = (String) obj.get("status");
                    String start = (String) obj.get("start");
                    String proses = (String) obj.get("proses");
                    String end = (String) obj.get("end");
                    String iduser = (String) obj.get("id");
                    Intent intent = new Intent(getApplicationContext(), ReportDetail.class);
                    intent.putExtra("iduser", iduser);
                    intent.putExtra("area", area);
                    intent.putExtra("note", note);
                    intent.putExtra("alamat", alamat);
                    intent.putExtra("status", status);
                    intent.putExtra("start", start);
                    intent.putExtra("proses", proses);
                    intent.putExtra("end", end);
                    startActivity(intent);
                }

            });
        }

    }
}
