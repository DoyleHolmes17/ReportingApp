package com.simap.dishub.far.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.simap.dishub.far.R;
import com.simap.dishub.far.entity.Teknisi;

import java.util.List;

/**
 * Created by programmer on 5/18/2017.
 */

public class RecyclerListTeknisiAdapter extends RecyclerView.Adapter<RecyclerListTeknisiAdapter.ViewHolder> {
    private List<Teknisi> rvData;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String statlistlap;
//    private TopUp cart;

    public RecyclerListTeknisiAdapter(Context mContext, List<Teknisi> inputData) {
        this.mContext = mContext;
        rvData = inputData;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // di tutorial ini kita hanya menggunakan data String untuk tiap item
        public TextView txNama, txId;

        public ViewHolder(View v) {
            super(v);
            txNama = (TextView) v.findViewById(R.id.idTeknisi);
            txId = (TextView) v.findViewById(R.id.namaTeknisi);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        // membuat view baru
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_teknisi, parent, false);

        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        final String idtek = rvData.get(position).getId_user_teknis();
        final String nmtek = rvData.get(position).getNama_teknisi();

        Log.e("VH >> ", idtek + " , " + nmtek);
        holder.txId.setText(idtek);
        holder.txNama.setText(nmtek);

    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }
}
