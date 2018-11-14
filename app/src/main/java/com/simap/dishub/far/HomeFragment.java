package com.simap.dishub.far;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * Created by programmer on 13-Jun-17.
 */

public class HomeFragment extends Fragment {

    BroadcastReceiver mBroadcastReceiver;

    private TextView tvKoneksi;
    private String ping;
    private double iping;
    private Handler mHandler;
    private Runnable mTicker;

    public static HomeFragment newInstance(int navposisi) {
        Bundle args = new Bundle();
        args.putInt("id", navposisi);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.root, container, false);

        tvKoneksi = (TextView) view.findViewById(R.id.tvKoneksi);

        ping = "0";

//        mHandler = new Handler();
//        mTicker = new Runnable() {
//            public void run() {
//                //user interface interactions and updates on screen
//                ping = Utils.readSharedSetting(getActivity(), "ping", "0");
//                iping = Double.parseDouble(ping);
//                Log.e("iping", iping + "");
//
//                if (iping > 0.0 && 100.0 > iping){
//                    tvKoneksi.setBackgroundColor(Color.parseColor("#FF00FF00"));
//                    tvKoneksi.setTextColor(Color.parseColor("#000000"));
//                    tvKoneksi.setText("Koneksi Bagus");
//                } else if (iping > 99.0 && 600.0 > iping){
//                    tvKoneksi.setBackgroundColor(Color.parseColor("#FFFFFF00"));
//                    tvKoneksi.setTextColor(Color.parseColor("#000000"));
//                    tvKoneksi.setText("Koneksi Kurang Bagus");
//                } else if (iping > 599.0){
//                    tvKoneksi.setBackgroundColor(Color.parseColor("#FFFF0000"));
//                    tvKoneksi.setTextColor(Color.parseColor("#000000"));
//                    tvKoneksi.setText("Koneksi Buruk");
//                } else {
//                    tvKoneksi.setBackgroundColor(Color.parseColor("#FF000080"));
//                    tvKoneksi.setTextColor(Color.parseColor("#FFFFFF"));
//                    tvKoneksi.setText("Tidak terhubung ke server");
//                }
//                mHandler.postDelayed(mTicker, 5000);
//            }
//        };
//        mTicker.run();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }
}