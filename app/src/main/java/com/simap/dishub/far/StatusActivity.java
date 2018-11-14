package com.simap.dishub.far;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.simap.dishub.far.adapter.RecyclerListTeknisiAdapter;
import com.simap.dishub.far.asynctas.UserTeknisiAsyncTask;
import com.simap.dishub.far.entity.Teknisi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MACBOOK on 17/09/2017.
 */

public class StatusActivity extends AppCompatActivity {

    private String header, statlistlap;
    private ProgressDialog pDialog;
    private ListView lv;
    private HashMap<String, String> hashMap;
    private RecyclerView.Adapter adapter;
    private RecyclerView rvView;
    private RecyclerView.LayoutManager layoutManager;


    private ArrayList<HashMap<String, String>> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvView = (RecyclerView) findViewById(R.id.rV_main);

        header = Utils.readSharedSetting(StatusActivity.this, "api_key", null);
        Log.e("header", header);

        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        reportList = new ArrayList<>();

        async();
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

    @SuppressLint("StaticFieldLeak")
    protected void async() {

        new UserTeknisiAsyncTask(this, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(StatusActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(List<Teknisi> listTeknisi) {
                super.onPostExecute(listTeknisi);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();

                if (!listTeknisi.isEmpty()) {
                    if (listTeknisi.get(0).getStatus().equals("true")) {
                        adapter = new RecyclerListTeknisiAdapter(StatusActivity.this, listTeknisi);
                        rvView.setAdapter(adapter);
                    } else {
                        Toast.makeText(StatusActivity.this, "Data Kosong.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(StatusActivity.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
                }
                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

}
