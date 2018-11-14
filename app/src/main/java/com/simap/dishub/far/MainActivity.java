package com.simap.dishub.far;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.simap.dishub.far.asynctas.AreaListAsyncTask;
import com.simap.dishub.far.entity.Area;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {

    //Set waktu lama splashscreen
    private static int splashInterval = 3000;
    private List<Area> listArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

//        coba();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(i);


                //jeda selesai Splashscreen
                MainActivity.this.finish();
            }

        }, splashInterval);

    }

    protected void coba (){
        String header = "077bf7348fdcc80c9c9462b304012c79";
        try {
            listArea = new AreaListAsyncTask(MainActivity.this, header).execute().get();
            Log.e("list", listArea+"");

//            ArrayAdapter<Bank> edittextrrayAdapter = new ArrayAdapter<Bank>(this, android.R.layout.simple_spinner_dropdown_item, listBank);
//            edittextrrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            textBank.setAdapter(edittextrrayAdapter);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
