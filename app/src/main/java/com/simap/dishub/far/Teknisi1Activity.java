package com.simap.dishub.far;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.simap.dishub.far.adapter.ImageAdapter;
import com.simap.dishub.far.adapter.ImageAdapter1;
import com.simap.dishub.far.entity.FieldName;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;

/**
 * Created by imac on 10/31/17.
 */

public class Teknisi1Activity extends AppCompatActivity {

    private Intent intent;
    private String npwz, imei, sessionKey;
    private HashMap<String, String> hashMap;
    private int clientIdentityStatus = 0;
    private int securityCharStatus = 0;
    private int bankAccountStatus = 0;
    private int securityQuestionStatus = 0;
    private int clientNameStatus = 0;
    private int referralStatus;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private double balance, subbalance;
    private TextView etBalance;
    private GridView gridView, gridView1;
    private DecimalFormat kursIndonesia;
    private DecimalFormatSymbols formatRp;
    private SessionManager session;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai1);

        gridView = (GridView) findViewById(R.id.grid_view);

        // Instance of ImageAdapter Class
        gridView.setAdapter(new ImageAdapter1(this));
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(android.content.Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {

                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
                }
            }
        };

        // gcm successfully registered
        // now subscribe to `admin` topic to receive app wide notifications
        FirebaseMessaging.getInstance().unsubscribeFromTopic("teknisi");
        FirebaseMessaging.getInstance().subscribeToTopic("admin");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("user");

        displayFirebaseRegId();

        Utils.saveSharedSetting(this, "statlistlap", "kosong");

        session = new SessionManager(getApplicationContext());
        String Nama = session.isNama();
        String Email = session.isEmail();
        if (!session.isLoggedIn()) {
            Logout();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    finish();
                    intent = new Intent(Teknisi1Activity.this, UploadActivity.class);
                    startActivity(intent);
                } else if (position == 1) {
                    finish();
                    Utils.saveSharedSetting(Teknisi1Activity.this, "statlistlap", "user");
                    intent = new Intent(Teknisi1Activity.this, ListLaporanActivity.class);
                    startActivity(intent);
                } else if (position == 2) {
                    finish();
                    Utils.saveSharedSetting(Teknisi1Activity.this, "statlistlap", "teknisiprogress");
                    intent = new Intent(Teknisi1Activity.this, ListLaporanOnProgressTeknisiActivity.class);
                    startActivity(intent);
                } else if (position == 3) {
                    finish();
                    Utils.saveSharedSetting(Teknisi1Activity.this, "statlistlap", "teknisiconf");
                    intent = new Intent(Teknisi1Activity.this, ListLaporanOnConfActivity.class);
                    startActivity(intent);
                } else if (position == 4) {
                    finish();
                    intent = new Intent(Teknisi1Activity.this, ChatActivity.class);
                    intent.putExtra(FieldName.ID_USER, "teknisi");
                    startActivity(intent);
                } else if (position == 5) {
                    finish();
                    session.setLogin(false);
                    Utils.saveSharedSetting(Teknisi1Activity.this, "jenisakun", "kosong");
                    intent = new Intent(Teknisi1Activity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("TAG", "Firebase reg id: " + regId);

    }

    public void Logout() {
        session.setLogin(false);
        Utils.saveSharedSetting(Teknisi1Activity.this, "jenisakun", "kosong");
        intent = new Intent(Teknisi1Activity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
    }


    @Override
    protected void onResume() {
        super.onResume();


        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finish();
            Teknisi1Activity.this.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}
