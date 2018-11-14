package com.simap.dishub.far;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static final int CONNECTION_TIMEOUT=10000;
    public static final int READ_TIMEOUT=15000;
    SessionManager session;
    EditText uNama,uEmail,uPassword;
    Spinner uLevel;
    Button uDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uNama = (EditText) findViewById(R.id.input_name);
        uEmail = (EditText) findViewById(R.id.input_email);
        uPassword = (EditText) findViewById(R.id.input_password);

        String list[]={"Admin","Teknisi","Pegawai"};
        uLevel = (Spinner) findViewById(R.id.input_level);
        ArrayAdapter<String> AdapterList = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item,list);
        uLevel.setAdapter(AdapterList);
        /*uLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3)
            {
                String level = uLevel.getSelectedItem().toString();
            }

            public void onNothingSelected(AdapterView<?> arg0) {}
        });*/

        uDaftar = (Button) findViewById(R.id.btn_signup);
        uDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptDaftar();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void attemptDaftar() {

        // Reset errors.
        uNama.setError(null);
        uEmail.setError(null);
        uPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = uEmail.getText().toString();
        String password = uPassword.getText().toString();
        String nama = uNama.getText().toString();
        String level = uLevel.getSelectedItem().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            uPassword.setError(getString(R.string.error_invalid_password));
            focusView = uPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            uEmail.setError(getString(R.string.error_field_required));
            focusView = uEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            uEmail.setError(getString(R.string.error_invalid_email));
            focusView = uEmail;
            cancel = true;
        }

        if(TextUtils.isEmpty(nama)) {
            uNama.setError(getString(R.string.err_msg_name));
            focusView = uNama;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            new RegisterActivity.UserAddTask().execute(nama, email, password, level);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public class UserAddTask extends AsyncTask<String, String, JSONObject> {

        ProgressDialog pdLoading = new ProgressDialog(RegisterActivity.this);
        HttpURLConnection conn;
        URL url = null;
        JSONParser jsonParser = new JSONParser();
        private static final String TAG_INFO = "info";
        private static final String DAFTAR_URL = "http://182.253.200.185/dishub2/index.php/c_daftar_hp";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected JSONObject doInBackground(String... args) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("nama", args[0]);
                params.put("email", args[1]);
                params.put("password", args[2]);
                params.put("level", args[3]);


                Log.d("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        DAFTAR_URL, "POST", params);

                if (json != null) {
                    Log.d("JSON result", json.toString());

                    return json;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(JSONObject json) {
            String info = "";


            if (json != null) {
                //Toast.makeText(LoginActivity.this, json.toString(),
                //Toast.LENGTH_LONG).show();

                try {
                    info = json.getString(TAG_INFO);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if(info.equals("Success")) {
                pdLoading.dismiss();
                Toast.makeText(RegisterActivity.this, "User Berhasil Dibuat",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(RegisterActivity.this, PegawaiActivity.class);
                startActivity(intent);
                finish();
            }else{
                pdLoading.dismiss();
                Toast.makeText(RegisterActivity.this, "User Gagal Dibuat",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}
