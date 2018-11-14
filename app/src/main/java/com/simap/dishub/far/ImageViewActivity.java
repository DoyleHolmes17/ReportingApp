package com.simap.dishub.far;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.util.Urls;

/**
 * Created by MACBOOK on 23/10/2017.
 */

public class ImageViewActivity extends AppCompatActivity {

    private ImageView imagedok;
    private Intent intent;
    private String pelapor, konfirmasi, statback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        imagedok = (ImageView) findViewById(R.id.imagedokumen);

        intent = getIntent();
        pelapor = intent.getStringExtra(FieldName.IMG_PELAPOR);
        konfirmasi = intent.getStringExtra(FieldName.IMG_KONFIRMASI);

        Log.e("pelapor konf", pelapor + " ;;; " + konfirmasi);

        if (pelapor != null || konfirmasi != null){
            if (pelapor != null) {
                Glide.with(this)
                        .load(Urls.urlgambar + pelapor)
                        .into(imagedok);
            } else if (konfirmasi != null){
                Glide.with(this)
                        .load(Urls.urlgambar + konfirmasi)
                        .into(imagedok);
            }
        } else if (pelapor == null && konfirmasi == null){
            Toast.makeText(this, "Gambar tidak tersedia.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onBackPressed() {
        statback = Utils.readSharedSetting(ImageViewActivity.this, "tombolimage", "kosong");

        if (statback.equals("adminclose")) {
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminCloseActivity.class);
            startActivity(intent);
        } else if (statback.equals("adminconf")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminOnConfActivity.class);
            startActivity(intent);
        } else if (statback.equals("user")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanActivity.class);
            startActivity(intent);
        } else if (statback.equals("useradmin")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminActivity.class);
            startActivity(intent);
        } else if (statback.equals("penugasanprogress")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminPenugasanActivity.class);
            startActivity(intent);
        } else if (statback.equals("all")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminAllActivity.class);
            startActivity(intent);
        } else if (statback.equals("adminconfirm")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanAdminFollowActivity.class);
            startActivity(intent);
        } else if (statback.equals("teknisiprogress")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanOnProgressTeknisiActivity.class);
            startActivity(intent);
        } else if (statback.equals("teknisiconf")){
            finish();
            intent = new Intent(ImageViewActivity.this, ListLaporanOnConfActivity.class);
            startActivity(intent);
        }
    }
}
