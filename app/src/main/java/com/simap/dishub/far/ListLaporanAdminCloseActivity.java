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

import com.simap.dishub.far.adapter.RecyclerListLaporanCloseAdapter;
import com.simap.dishub.far.asynctas.ListLaporanCloseAsyncTask;
import com.simap.dishub.far.entity.ListLaporan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MACBOOK on 17/09/2017.
 */

public class ListLaporanAdminCloseActivity extends AppCompatActivity {

    private String header, statlistlap, idreportstat, pages;

    private ProgressDialog pDialog;
    private ListView lv;
    private HashMap<String, String> hashMap;
    private int pagei, visibleItemCount,
            totalItemCount, pastVisiblesItems, i, temp;
    private RecyclerView.Adapter adapter;
    private RecyclerView rvView;
    private List<ListLaporan> templist = new ArrayList<ListLaporan>();
    private LinearLayoutManager layoutManager;

    private ArrayList<HashMap<String, String>> reportList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvView = (RecyclerView) findViewById(R.id.rV_main);

        i = 0;
        header = Utils.readSharedSetting(ListLaporanAdminCloseActivity.this, "api_key", null);
        Log.e("header", header);

        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        reportList = new ArrayList<>();

        statlistlap = Utils.readSharedSetting(this, "statlistlap", "kosong");
        Log.e("stat", statlistlap);

        if (statlistlap.equals("close")) {
            pages = "0";
            asynclose(pages);
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
                            if (pagei != temp) {
                                Log.e("pagei", pagei + "");
                                asynclose(String.valueOf(pagei));
                                layoutManager.setStackFromEnd(true);
                            } else {
                                Log.e("i", i + "");
                                if (i < 4) {
                                    i = i + 1;
                                } else {
                                    i = 0;
                                    Log.e("pagei", pagei + "");
                                    asynclose(String.valueOf(pagei));
                                    layoutManager.setStackFromEnd(true);
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
                finish();
                Intent intent = new Intent(ListLaporanAdminCloseActivity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    @SuppressLint("StaticFieldLeak")
    protected void asynclose(final String page) {

        new ListLaporanCloseAsyncTask(this, header, page) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ListLaporanAdminCloseActivity.this);
                pDialog.setMessage("Please wait...");
                pDialog.setCancelable(false);
                pDialog.show();
            }

            @Override
            protected void onPostExecute(List<ListLaporan> listLaporan) {
                super.onPostExecute(listLaporan);
                temp = Integer.parseInt(page);
                // Dismiss the progress dialog
                if (pDialog.isShowing())
                    pDialog.dismiss();
                if (!listLaporan.isEmpty()) {
                    if (listLaporan.get(0).getStatus().equals("true")) {
                        templist.addAll(listLaporan);
//                        Log.e("list >>>", templist + "");
                        pagei = temp + 1;
                        adapter = new RecyclerListLaporanCloseAdapter(ListLaporanAdminCloseActivity.this, templist);
                        rvView.setAdapter(adapter);
                        layoutManager.scrollToPosition(
                                (templist.size() - (listLaporan.size() * 2)));
                    } else {
                        if (!templist.isEmpty()) {
                            adapter = new RecyclerListLaporanCloseAdapter(ListLaporanAdminCloseActivity.this, templist);
                            rvView.setAdapter(adapter);
                            layoutManager.setStackFromEnd(true);
                        } else {
                            Toast.makeText(ListLaporanAdminCloseActivity.this, "Data kosong.", Toast.LENGTH_LONG).show();
                        }
                        pagei = temp;
                    }
                } else {
                    if (!templist.isEmpty()) {
                        adapter = new RecyclerListLaporanCloseAdapter(ListLaporanAdminCloseActivity.this, templist);
                        rvView.setAdapter(adapter);
                        layoutManager.setStackFromEnd(true);
                    }
                    pagei = temp;
                    Toast.makeText(ListLaporanAdminCloseActivity.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
                }
                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }
}
