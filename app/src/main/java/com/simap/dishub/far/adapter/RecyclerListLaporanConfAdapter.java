package com.simap.dishub.far.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.simap.dishub.far.ImageViewActivity;
import com.simap.dishub.far.R;
import com.simap.dishub.far.ReportAllConfirm2Activity;
import com.simap.dishub.far.Utils;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;

import java.util.List;

/**
 * Created by programmer on 5/18/2017.
 */

public class RecyclerListLaporanConfAdapter extends RecyclerView.Adapter<RecyclerListLaporanConfAdapter.ViewHolder> {
    private List<ListLaporan> rvData;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String statlistlap, jenislaporan;
    private View v;
    private Intent intent;
//    private TopUp cart;

    public RecyclerListLaporanConfAdapter(Context mContext, List<ListLaporan> inputData) {
        this.mContext = mContext;
        rvData = inputData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        private TextView txAreainf, txAlamatinf, tcCatsubinf, txNoteinf, txStatusrep, txTanggal, txJumlahpel,
        txWilayah, txTeknisi, txMulai, txSelesai, txLama;
        private LinearLayout laybut;
        private Button pelapor, konfirmasi;
        private TableRow txwilayah, txteknisi, txmulai, txselesai, txlama, txclose, txtotal, txjumlahpel, txrepnum, txpelaporan;

        public ViewHolder(View v) {
            super(v);
            txAreainf = (TextView) v.findViewById(R.id.textareaInfra);
            txAlamatinf = (TextView) v.findViewById(R.id.textalamatInfra);
            tcCatsubinf = (TextView) v.findViewById(R.id.textcategorysubInfra);
            txTanggal = (TextView) v.findViewById(R.id.texttanggalPelaporan);
            txJumlahpel = (TextView) v.findViewById(R.id.textpelaporInfra);
            txNoteinf = (TextView) v.findViewById(R.id.textnoteInfra);
            txStatusrep = (TextView) v.findViewById(R.id.textstatusReport);
            txWilayah = (TextView) v.findViewById(R.id.textwilayah);
            txTeknisi = (TextView) v.findViewById(R.id.textteknisiInfra);
            txMulai = (TextView) v.findViewById(R.id.textstartInfra);
            txSelesai = (TextView) v.findViewById(R.id.textstopInfra);
            txLama = (TextView) v.findViewById(R.id.textlamaInfra);

            txrepnum = (TableRow) v.findViewById(R.id.txrepnum);
            txwilayah = (TableRow) v.findViewById(R.id.txwilayah);
            txteknisi = (TableRow) v.findViewById(R.id.txteknisi);
            txpelaporan = (TableRow) v.findViewById(R.id.txpelaporan);
            txselesai = (TableRow) v.findViewById(R.id.txselesai);
            txlama = (TableRow) v.findViewById(R.id.txlama);
            txclose = (TableRow) v.findViewById(R.id.txclose);
            txtotal = (TableRow) v.findViewById(R.id.txtotal);

            laybut = (LinearLayout) v.findViewById(R.id.laybut);
            pelapor = (Button) v.findViewById(R.id.btnpelapor);
            konfirmasi = (Button) v.findViewById(R.id.btnkonfir);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        // membuat view baru
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_laporan_close, parent, false);


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
        final String catsubinf = rvData.get(position).getCategory_infra();
        final String noteinf = rvData.get(position).getNote_infra();
        final String statusrep = rvData.get(position).getStatus_report();
        final String idcat = rvData.get(position).getId_category_infra();
        final String idarea = rvData.get(position).getId_area_infra();
        final String tanggal = rvData.get(position).getTanggal_pelaporan();
        final String jumlah = rvData.get(position).getJumlah_pelapor();
        final String wilayah = rvData.get(position).getWilayah_infra();
        final String teknisi = rvData.get(position).getNama_teknisi();
        final String mulai = rvData.get(position).getMulai_pengerjaan();
        final String selesai = rvData.get(position).getSelesai_pengerjaan();
        final String lama = rvData.get(position).getLama_pengerjaan();
        final String img_pelapor = rvData.get(position).getImg_pelapor();
        final String img_konfir = rvData.get(position).getImg_konfirmasi();

        if (img_pelapor == null || img_pelapor.equals("null") || img_pelapor.isEmpty()){
            holder.pelapor.setVisibility(View.GONE);
        }
        if (img_konfir == null || img_konfir.equals("null") || img_konfir.isEmpty()){
            holder.konfirmasi.setVisibility(View.GONE);
        }

        Log.e("VH >> ", area + " , " + alamat + " , " + statusrep);
        holder.txAreainf.setText(area);
        holder.txAlamatinf.setText(alamat);
        holder.tcCatsubinf.setText(catsubinf);
        holder.txNoteinf.setText(noteinf);
        holder.txStatusrep.setText(statusrep);
        holder.txTanggal.setText(tanggal);
        holder.txJumlahpel.setText(jumlah);
        holder.txWilayah.setText(wilayah);
        holder.txTeknisi.setText(teknisi);
        holder.txMulai.setText(mulai);
        holder.txSelesai.setText(selesai);
        holder.txLama.setText(lama);

        holder.laybut.setVisibility(View.VISIBLE);

        statlistlap = Utils.readSharedSetting(mContext, "statlistlap", "kosong");
//        if (statlistlap.equals("user")){
//            //do nothing
//        } else

        holder.txrepnum.setVisibility(View.GONE);
        holder.txclose.setVisibility(View.GONE);
        holder.txtotal.setVisibility(View.GONE);


        holder.pelapor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
                intent = new Intent(mContext, ImageViewActivity.class);
                intent.putExtra(FieldName.IMG_PELAPOR, img_pelapor);
                Utils.saveSharedSetting(mContext, "tombolimage", "adminconf");
                mContext.startActivity(intent);
            }
        });

        holder.konfirmasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)mContext).finish();
                intent = new Intent(mContext, ImageViewActivity.class);
                Utils.saveSharedSetting(mContext, "tombolimage", "adminconf");
                intent.putExtra(FieldName.IMG_KONFIRMASI, img_konfir);
                mContext.startActivity(intent);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ReportAllConfirm2Activity.class);
                intent.putExtra(FieldName.ID_CATEGORY, idcat);
                intent.putExtra(FieldName.ID_AREA, idarea);
                intent.putExtra(FieldName.AREA_INFRA, area);
                intent.putExtra(FieldName.NOTE_INFRA, noteinf);
                intent.putExtra(FieldName.ALAMAT_INFRA, alamat);
                Log.e("intent", idcat + "," + idarea + "," + area + "," + noteinf + "," + alamat);

                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }
}
