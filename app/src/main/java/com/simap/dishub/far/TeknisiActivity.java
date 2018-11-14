package com.simap.dishub.far;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.simap.dishub.far.entity.FieldName;

public class TeknisiActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar = null;
    DrawerLayout drawer;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;
    private SessionManager session;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teknisi);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        FirebaseMessaging.getInstance().subscribeToTopic("teknisi");
        FirebaseMessaging.getInstance().unsubscribeFromTopic("admin");
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

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View hView = navigationView.getHeaderView(0);
        TextView textNamaTeknisi = (TextView) hView.findViewById(R.id.textNamaTeknisi);
        TextView textEmailTeknisi = (TextView) hView.findViewById(R.id.textEmailTeknisi);
        textNamaTeknisi.setText(Nama);
        textEmailTeknisi.setText(Email);

        navigationView.setNavigationItemSelectedListener(this);
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);

        Log.e("TAG", "Firebase reg id: " + regId);

//        if (!TextUtils.isEmpty(regId))
//            txtRegId.setText("Firebase Reg Id: " + regId);
//        else
//            txtRegId.setText("Firebase Reg Id is not received yet!");
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
        if (id == R.id.nav_progress_teknisi) {
            Utils.saveSharedSetting(this, "statlistlap", "teknisiprogress");
            intent = new Intent(TeknisiActivity.this, ListLaporanOnProgressTeknisiActivity.class); // ReportActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat_teknisi) {
            intent = new Intent(TeknisiActivity.this, ChatActivity.class);
            intent.putExtra(FieldName.ID_USER, "teknisi");
            startActivity(intent);
        } else if (id == R.id.nav_track) {
            intent = new Intent(TeknisiActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            Logout();
        } else if (id == R.id.nav_report_update_teknisi) {
            Utils.saveSharedSetting(this, "statlistlap", "teknisiconf");
            intent = new Intent(TeknisiActivity.this, ListLaporanOnConfActivity.class); //ReportFollowActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_daftarlaporan) {
            Utils.saveSharedSetting(this, "statlistlap", "user");
            intent = new Intent(TeknisiActivity.this, ListLaporanActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_camera) {
            intent = new Intent(TeknisiActivity.this, UploadActivity.class);
            startActivity(intent);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // untuk mengganti isi kontainer menu yang dipiih
    private void callFragment(Fragment fragment) {
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.remove(fragment);
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();
    }

    public void Logout() {
        session.setLogin(false);
        Utils.saveSharedSetting(TeknisiActivity.this, "jenisakun", "kosong");
        intent = new Intent(TeknisiActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}