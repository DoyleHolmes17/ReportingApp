package com.simap.dishub.far.adapter;

/**
 * Created by imac on 10/31/17.
 */

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.simap.dishub.far.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public Integer[] mThumbIds = {
            R.drawable.l, R.drawable.j, R.drawable.k,
            R.drawable.f, R.drawable.n,
            R.drawable.laporan_close, R.drawable.r,
            R.drawable.add_user, R.drawable.cchat, R.drawable.llogout,
    };

    public String[] mLabel = {
            "Pelaporan", "Cek Laporan", "Cek Laporan Masuk", "Lap Status Follow Up",
            "Konfirmasi Lap Teknisi", "Lap Status Close",
            "Penugasan Teknisi", "Add User",
            "Chat", "Logout",
    };

    // Constructor
    public ImageAdapter(Context c){
        mContext = c;
    }

    @Override
    public int getCount() {
        return mThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        return mThumbIds[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        ImageView imageView = new ImageView(mContext);
//        TextView textView = new TextView(mContext);
//
//        imageView.setImageResource(mThumbIds[position]);
//        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        imageView.setLayoutParams(new GridView.LayoutParams(100, 100));
//        return imageView;

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;

//        if (convertView == null) {

            gridView = new View(mContext);

            // get layout from mobile.xml
            gridView = inflater.inflate(R.layout.mobile, null);

            // set value into textview
            TextView textView = (TextView) gridView
                    .findViewById(R.id.grid_item_label);
            textView.setText(mLabel[position]);

            // set image based on selected text
            ImageView imageView = (ImageView) gridView
                    .findViewById(R.id.grid_item_image);
            imageView.setImageResource(mThumbIds[position]);

            Log.e("posisi", (position+"") + " ; " + mLabel[position]);
//        } else {
//            gridView = (View) convertView;
//        }

        return gridView;
    }

}
