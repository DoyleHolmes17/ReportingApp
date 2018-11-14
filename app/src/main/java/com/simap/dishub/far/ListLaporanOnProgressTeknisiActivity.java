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

import com.simap.dishub.far.adapter.RecyclerListLaporanAdapter;
import com.simap.dishub.far.asynctas.ListLaporanTeknisiAsyncTask;
import com.simap.dishub.far.entity.ListLaporan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by MACBOOK on 17/09/2017.
 */

public class ListLaporanOnProgressTeknisiActivity extends AppCompatActivity {

    private String header, statlistlap, pages;
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

        i = 0;
        rvView = (RecyclerView) findViewById(R.id.rV_main);

        header = Utils.readSharedSetting(ListLaporanOnProgressTeknisiActivity.this, "api_key", null);
        Log.e("header", header);

        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        reportList = new ArrayList<>();

//        lv = (ListView) findViewById(R.id.listLaporan);
        statlistlap = Utils.readSharedSetting(this, "statlistlap", "kosong");
        Log.e("stat", statlistlap);

        if (statlistlap.equals("teknisiprogress")){
            pages = "0";
            asyncpro(pages);
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
                                asyncpro(String.valueOf(pagei));
                                layoutManager.setStackFromEnd(true);
                            } else {
                                Log.e("i", i + "");
                                if (i < 4) {
                                    i = i + 1;
                                } else {
                                    i = 0;
                                    Log.e("pagei", pagei + "");
                                    asyncpro(String.valueOf(pagei));
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
                Intent intent = new Intent(ListLaporanOnProgressTeknisiActivity.this, Teknisi1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void asyncpro(final String page) {

        new ListLaporanTeknisiAsyncTask(this, header, page) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
                pDialog = new ProgressDialog(ListLaporanOnProgressTeknisiActivity.this);
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

                if (listLaporan != null) {
                    if (!listLaporan.isEmpty()) {
                        templist.addAll(listLaporan);
//                        Log.e("list >>>", templist + "");
                        pagei = temp + 1;
                        adapter = new RecyclerListLaporanAdapter(ListLaporanOnProgressTeknisiActivity.this, templist);
                        rvView.setAdapter(adapter);
                        layoutManager.scrollToPosition(
                                (templist.size() - (listLaporan.size() * 2)));
                    } else {
                        if (!templist.isEmpty()) {
                            adapter = new RecyclerListLaporanAdapter(ListLaporanOnProgressTeknisiActivity.this, templist);
                            rvView.setAdapter(adapter);
                            layoutManager.setStackFromEnd(true);
                        } else {
                            Toast.makeText(ListLaporanOnProgressTeknisiActivity.this, "Data kosong.", Toast.LENGTH_LONG).show();
                        }
                        pagei = temp;
                    }
                } else {if (!templist.isEmpty()) {
                    adapter = new RecyclerListLaporanAdapter(ListLaporanOnProgressTeknisiActivity.this, templist);
                    rvView.setAdapter(adapter);
                    layoutManager.setStackFromEnd(true);
                }
                    pagei = temp;
                    Toast.makeText(ListLaporanOnProgressTeknisiActivity.this, "Laporan Kosong.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

}
