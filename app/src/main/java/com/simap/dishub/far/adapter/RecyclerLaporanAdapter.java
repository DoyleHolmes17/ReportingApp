package com.simap.dishub.far.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.ImageViewActivity;
import com.simap.dishub.far.ListLaporanAdminActivity;
import com.simap.dishub.far.R;
import com.simap.dishub.far.Utils;
import com.simap.dishub.far.asynctas.DeleteLaporanAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Laporan;

import java.util.HashMap;
import java.util.List;

/**
 * Created by programmer on 5/18/2017.
 */

public class RecyclerLaporanAdapter extends RecyclerView.Adapter<RecyclerLaporanAdapter.ViewHolder> {
    private List<Laporan> rvData;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String statlistlap, tombolimage, header, status, message;
    private ProgressDialog pDialog;
    private Intent intent;
    private HashMap<String, String> hashMap;
//    private TopUp cart;

    public RecyclerLaporanAdapter(Context mContext, List<Laporan> inputData) {
        this.mContext = mContext;
        rvData = inputData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView txAreainf, txAlamatinf, tcCatsubinf, txNoteinf, txStatusrep, txtanggal, txrepnum, txvia, txsepasi;
        public TableRow txwilayah, txteknisi, txmulai, txselesai, txlama, txclose, txtotal, txjumlahpel;
        public LinearLayout laybut, pelaporanvia;
        public Button btnpelapor, btnkonfir, btnhapus;

        public ViewHolder(View v) {
            super(v);
            txAreainf = (TextView) v.findViewById(R.id.textareaInfra);
            txsepasi = (TextView) v.findViewById(R.id.sepasi);
            txAlamatinf = (TextView) v.findViewById(R.id.textalamatInfra);
            tcCatsubinf = (TextView) v.findViewById(R.id.textcategorysubInfra);
            txNoteinf = (TextView) v.findViewById(R.id.textnoteInfra);
            txStatusrep = (TextView) v.findViewById(R.id.textstatusReport);
            txtanggal = (TextView) v.findViewById(R.id.texttanggalPelaporan);
            txrepnum = (TextView) v.findViewById(R.id.textrepnumber);
            txwilayah = (TableRow) v.findViewById(R.id.txwilayah);
            txteknisi = (TableRow) v.findViewById(R.id.txteknisi);
            txmulai = (TableRow) v.findViewById(R.id.txmulai);
            txselesai = (TableRow) v.findViewById(R.id.txselesai);
            txlama = (TableRow) v.findViewById(R.id.txlama);
            txclose = (TableRow) v.findViewById(R.id.txclose);
            txtotal = (TableRow) v.findViewById(R.id.txtotal);
            txjumlahpel = (TableRow) v.findViewById(R.id.txjumlah);
            txvia = (TextView) v.findViewById(R.id.textpelaporanvia);
            pelaporanvia = (LinearLayout) v.findViewById(R.id.pelaporanvia);

            laybut = (LinearLayout) v.findViewById(R.id.laybut);
            btnpelapor = (Button) v.findViewById(R.id.btnpelapor);
            btnkonfir = (Button) v.findViewById(R.id.btnkonfir);
            btnhapus = (Button) v.findViewById(R.id.btnhapus);

            header = Utils.readSharedSetting(mContext, "api_key", "kosong");
            tombolimage = Utils.readSharedSetting(mContext, "tombolimage", "kosong");
            Log.e("tombolimage", tombolimage);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_close, parent, false);

        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        final String area = rvData.get(position).getLokasi_infra();
        final String alamat = rvData.get(position).getAlamat_infra();
        final String catsubinf = rvData.get(position).getSub_category_infra();
        final String noteinf = rvData.get(position).getNote_infra();
        final String statusrep = rvData.get(position).getStatus_report();
        final String idcat = rvData.get(position).getId_category_infra();
        final String idarea = rvData.get(position).getId_area_infra();
        final String tgl = rvData.get(position).getTanggal_pelaporan();
        final String rep = rvData.get(position).getRep_number();
        final String img_pelapor = rvData.get(position).getImg_pelapor();
        final String pelaporan_via = rvData.get(position).getPelaporan_via();
        final String idlap = rvData.get(position).getId_laporan();

        if (img_pelapor == null || img_pelapor.equals("null") || img_pelapor.isEmpty()) {
            holder.btnpelapor.setVisibility(View.GONE);
            holder.txsepasi.setVisibility(View.GONE);
        }

        Log.e("image pelapor >> ", img_pelapor);
        holder.txAreainf.setText(area);
        holder.txAlamatinf.setText(alamat);
        holder.tcCatsubinf.setText(catsubinf);
        holder.txNoteinf.setText(noteinf);
        holder.txStatusrep.setText(statusrep);
        holder.txtanggal.setText(tgl);
        holder.txrepnum.setText(rep);

        holder.txwilayah.setVisibility(View.GONE);
        holder.txteknisi.setVisibility(View.GONE);
        holder.txmulai.setVisibility(View.GONE);
        holder.txselesai.setVisibility(View.GONE);
        holder.txlama.setVisibility(View.GONE);
        holder.txclose.setVisibility(View.GONE);
        holder.txtotal.setVisibility(View.GONE);
        holder.txjumlahpel.setVisibility(View.GONE);

        holder.laybut.setVisibility(View.VISIBLE);
        holder.btnkonfir.setVisibility(View.GONE);

        if (statusrep.equals("On Scheduling")) {
            holder.btnhapus.setVisibility(View.VISIBLE);
        }

        if (holder.btnpelapor.getVisibility() == View.VISIBLE && holder.btnhapus.getVisibility() == View.VISIBLE) {
            holder.txsepasi.setVisibility(View.VISIBLE);
        } else {
            holder.txsepasi.setVisibility(View.GONE);
        }

        if (tombolimage.equals("useradmin")) {
            holder.pelaporanvia.setVisibility(View.VISIBLE);
            holder.txvia.setText(pelaporan_via);
        } else {
            holder.pelaporanvia.setVisibility(View.GONE);
        }

        holder.btnpelapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) mContext).finish();
                intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra(FieldName.IMG_PELAPOR, img_pelapor);
                mContext.startActivity(intent);
            }
        });

        holder.btnhapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("SiPekaAPILL");
                alertDialog.setMessage("Apakah anda yakin ingin membatalkan pelaporan?");
                alertDialog.setPositiveButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.setNegativeButton("Iya", new DialogInterface.OnClickListener() {
                    @SuppressLint("StaticFieldLeak")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hashMap = new HashMap<String, String>();
                        hashMap.put(FieldName.ID_LAPORAN, idlap);
                        new DeleteLaporanAsyncTask(mContext, hashMap, header) {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                // Showing progress dialog
                                pDialog = new ProgressDialog(mContext);
                                pDialog.setMessage("Membatalkan pelaporan...");
                                pDialog.setCancelable(false);
                                pDialog.show();
                            }

                            @Override
                            protected void onPostExecute(Laporan listLaporan) {
                                super.onPostExecute(listLaporan);
                                if (pDialog.isShowing())
                                    pDialog.dismiss();

                                status = listLaporan.getStatus();
                                message = listLaporan.getMessage();
                                if (status.equals("true")) {
                                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                    ((Activity) mContext).finish();
                                    intent = new Intent(mContext, ListLaporanAdminActivity.class);
                                    mContext.startActivity(intent);
                                } else {
                                    Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
                                }
                            }
                        }.execute();
                    }

                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();

//                ((Activity) mContext).finish();
//                intent = new Intent(mContext, ImageViewActivity.class);
//                intent.putExtra(FieldName.IMG_PELAPOR, img_pelapor);
//                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }
}
