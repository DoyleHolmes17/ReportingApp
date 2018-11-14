package com.simap.dishub.far;

//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.firebase.messaging.FirebaseMessaging;
import com.simap.dishub.far.entity.FieldName;

public class PegawaiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar = null;
    DrawerLayout drawer;
    NavigationView navigationView;
    //    FragmentManager fragmentManager;
//    FragmentTransaction fragmentTransaction;
//    Fragment fragment = null;
    private SessionManager session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Intent intent;
    private String ping, lastWord;
    private String[] delay, timeping;
    private Handler handler;
    private int lastIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pegawai);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        fragmentManager = getFragmentManager();

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

        intent = getIntent();
        if (savedInstanceState == null) {
            getSupportFragmentManager().
                    beginTransaction().
                    add(R.id.container, HomeFragment.newInstance(0)).
                    commit();
        }

        startService(new Intent(getBaseContext(), NotificationListener.class));
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView textNamaPegawai = (TextView) hView.findViewById(R.id.textNamaPegawai);
        TextView textEmailPegawai = (TextView) hView.findViewById(R.id.textEmailPegawai);
        textNamaPegawai.setText(Nama);
        textEmailPegawai.setText(Email);

        navigationView.setNavigationItemSelectedListener(this);

        //membuat obyek dari widget textview
        handler = new Handler();

//        final Runnable r = new Runnable() {
//            public void run() {
//                ping = Devices.pingg("103.255.15.34");
//                lastIndex = ping.lastIndexOf(" ");
//                Log.e("ping", ping);
//                Log.e("last indx", lastIndex + "");
//                if (ping != null || !ping.isEmpty()) {
//                    if (lastIndex > 40) {
//                        lastWord = ping.substring(ping.lastIndexOf(" ") - 9);
//                        delay = lastWord.split("=");
//
//                        //delay = ping.split("e=");
//                        Log.e("delay if", lastWord);
//                        //Log.e("delay if1", delay[1]);
//                    } else {
//                        delay[1] = "0 ms";
//                    }
//                } else {
//                    delay[1] = "0 ms";
//                }
//                timeping = delay[1].split("\\s+");
//                Log.e("time", timeping[0]);
//                Utils.saveSharedSetting(PegawaiActivity.this, "ping", timeping[0]);
//                handler.postDelayed(this, 5000);
//            }
//        };
//        handler.postDelayed(r, 10000);

//        // tampilan default awal ketika aplikasii dijalankan
//        if (savedInstanceState == null) {
//            fragment = new Root();
//            callFragment(fragment);
//        }

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

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("TAG", "Firebase reg id: " + regId);

    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        // Untuk memanggil layout dari menu yang dipilih
        if (id == R.id.nav_add_user) {
            Intent intent = new Intent(PegawaiActivity.this, RegisterUserActivity.class);
            intent.putExtra(FieldName.STATUS, "home");
            startActivity(intent);
        } else if (id == R.id.nav_report_admin) {
            Utils.saveSharedSetting(this, "statlistlap", "user");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_report_all) {
            Utils.saveSharedSetting(this, "statlistlap", "all");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminAllActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_report_progres) {
            Utils.saveSharedSetting(this, "statlistlap", "adminconfirm");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminFollowActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_report_confirmation) {
            Utils.saveSharedSetting(this, "statlistlap", "adminconfirm2");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminOnConfActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_report_close) {
            Utils.saveSharedSetting(this, "statlistlap", "close");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminCloseActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_cek_teknisi) {
            Intent intent = new Intent(PegawaiActivity.this, StatusActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat_admin) {
            Intent intent = new Intent(PegawaiActivity.this, ChatActivity.class);
            intent.putExtra(FieldName.ID_USER, "pegawai");
            startActivity(intent);
        } else if (id == R.id.nav_track) {
            Intent intent = new Intent(PegawaiActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Logout();
        } else if (id == R.id.nav_camera) {
            Intent intent = new Intent(PegawaiActivity.this, UploadActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_penugasan_teknisi) {
            Utils.saveSharedSetting(this, "statlistlap", "penugasanprogress");
            Intent intent = new Intent(PegawaiActivity.this, ListLaporanAdminPenugasanActivity.class);
            startActivity(intent);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // untuk mengganti isi kontainer menu yang dipiih
//    private void callFragment(Fragment fragment) {
//        fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.remove(fragment);
//        fragmentTransaction.replace(R.id.frame_container, fragment);
//        fragmentTransaction.commit();
//    }

    public void Logout() {
        session.setLogin(false);
        Utils.saveSharedSetting(PegawaiActivity.this, "jenisakun", "kosong");
        Intent intent = new Intent(PegawaiActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
