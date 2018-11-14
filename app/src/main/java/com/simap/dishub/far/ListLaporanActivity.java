package com.simap.dishub.far;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.simap.dishub.far.adapter.RecyclerLaporanAdapter;
import com.simap.dishub.far.asynctas.GetLaporanAsyncTask;
import com.simap.dishub.far.entity.Laporan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MACBOOK on 17/09/2017.
 */

public class ListLaporanActivity extends AppCompatActivity {

    private String header, statlistlap, pages, jenisakun;
    private ProgressDialog pDialog;
    private ListView lv;
    private List<Laporan> templist = new ArrayList<Laporan>();
    private int pagei, visibleItemCount, totalItemCount, pastVisiblesItems, temp, i;
    private HashMap<String, String> hashMap;
    private RecyclerView.Adapter adapter;
    private RecyclerView rvView;
    private LinearLayoutManager layoutManager;

    private ArrayList<HashMap<String, String>> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        i = 0;
        rvView = (RecyclerView) findViewById(R.id.rV_main);
        header = Utils.readSharedSetting(ListLaporanActivity.this, "api_key", null);
        Log.e("header", header);

        jenisakun = Utils.readSharedSetting(this, "jenisakun", "kosong");
        Log.e("jenisakun", jenisakun);

        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        reportList = new ArrayList<>();

//        lv = (ListView) findViewById(R.id.listLaporan);
        statlistlap = Utils.readSharedSetting(this, "statlistlap", "kosong");
        Log.e("stat", statlistlap);

        if (statlistlap.equals("user")) {
            pages = "0";
            async(pages);
        }

        rvView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = layoutManager.getChildCount();
                    totalItemCount = layoutManager.getItemCount();
                    pastVisiblesItems = ((LinearLayoutManager) recyclerView.
                            getLayoutManager()).findFirstVisibleItemPosition();

                    if (!pDialog.isShowing()) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                            Log.e("okokok", String.valueOf(visibleItemCount) + " ;;; " + String.valueOf(totalItemCount) +
//                                    " ;;; " + String.valueOf(pastVisiblesItems));
//                            pagei = (Integer.parseInt(pages)) + 1;
                            if (pagei != temp) {
                                Log.e("pagei", pagei + "");
                                async(String.valueOf(pagei));
                            } else {
                                if (i < 4) {
                                    i = i + 1;
                                } else {
                                    i = 0;
                                    Log.e("pagei", pagei + "");
                                    async(String.valueOf(pagei));
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (jenisakun.equals("teknisi")) {
                    finish();
                    Intent intent = new Intent(ListLaporanActivity.this, Teknisi1Activity.class);
                    startActivity(intent);
                } else {
                    finish();
                    Intent intent = new Intent(ListLaporanActivity.this, Menu1Activity.class);
                    startActivity(intent);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    @SuppressLint("StaticFieldLeak")
    protected void async(final String page) {

        new GetLaporanAsyncTask(this, header, page) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ListLaporanActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(List<Laporan> listLaporan) {
                super.onPostExecute(listLaporan);
                temp = Integer.parseInt(page);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (!listLaporan.isEmpty()) {
                    if (listLaporan.get(0).getStatus().equals("true")) {
                        templist.addAll(listLaporan);
                        Log.e("list >>>", templist + "");
                        pagei = temp + 1;
                        Utils.saveSharedSetting(ListLaporanActivity.this, "tombolimage", "user");
                        adapter = new RecyclerLaporanAdapter(ListLaporanActivity.this, templist);
                        rvView.setAdapter(adapter);
                        layoutManager.scrollToPosition(
                                (templist.size() - (listLaporan.size() * 2)));
                    } else {
                        if (!templist.isEmpty()) {
                            adapter = new RecyclerLaporanAdapter(ListLaporanActivity.this, templist);
                            rvView.setAdapter(adapter);
                            layoutManager.setStackFromEnd(true);
                        } else {
                            Toast.makeText(ListLaporanActivity.this, "Data kosong.", Toast.LENGTH_LONG).show();
                        }
                        pagei = temp;
                    }
                } else {
                    if (!templist.isEmpty()) {
                        adapter = new RecyclerLaporanAdapter(ListLaporanActivity.this, templist);
                        rvView.setAdapter(adapter);
                        layoutManager.setStackFromEnd(true);
                    }
                    pagei = temp;
                    Toast.makeText(ListLaporanActivity.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
                }
            }

        }.execute();
    }

}