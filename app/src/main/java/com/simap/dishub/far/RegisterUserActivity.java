package com.simap.dishub.far;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.RegisterAsyncTask;
import com.simap.dishub.far.asynctas.RegisterUserAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Login;
import com.simap.dishub.far.util.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RegisterUserActivity extends AppCompatActivity {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    SessionManager session;
    EditText uNama, uEmail, uPassword, ucPassword, uUname, uPhone;
    Spinner uLevel;
    private CustomProgressDialog progressDialog;
    private Intent intent;
    Button uDaftar;
    private List<String> listgroup = new ArrayList<>();
    //private List<Group> listgroup;
    //private ArrayAdapter<Group> adapterList;
    private ArrayAdapter<String> adapterList;
    private HashMap<String, String> hashMap;
    private TextView txinput_level;
    private Login login;
    private String email, password, cpassword, nama, level, uname, setatus, header, phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new CustomProgressDialog(this);

        uNama = (EditText) findViewById(R.id.input_name);
        uEmail = (EditText) findViewById(R.id.input_email);
        uPassword = (EditText) findViewById(R.id.input_password);
        ucPassword = (EditText) findViewById(R.id.input_c_password);
        uUname = (EditText) findViewById(R.id.input_u_name);
        uPhone = (EditText) findViewById(R.id.input_phone);

        uLevel = (Spinner) findViewById(R.id.input_level);
        txinput_level = (TextView) findViewById(R.id.txinput_level);

        //header = Utils.readSharedSetting(RegisterUserActivity.this, "api_key", null);
        // Log.e("header", header);
        intent = getIntent();
        setatus = intent.getStringExtra(FieldName.STATUS);

//        Log.e("setatus", setatus);
        if (setatus.equals("home")) {
            //spinasync();
            listgroup.add("Admin Dishub");
            listgroup.add("Pegawai Teknisi");
            listgroup.add("User Umum");

            //      adapterList = new ArrayAdapter<Group>(this,
            adapterList = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_dropdown_item, listgroup);
            uLevel.setAdapter(adapterList);
        } else {
            uLevel.setVisibility(View.GONE);
            txinput_level.setVisibility(View.GONE);
        }

        uLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                level = (i + 2) + "";
//                Log.e("level", level);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
                finish();
                Intent intent = new Intent(RegisterUserActivity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void spinasync() {
      //  try {
      //      listgroup = new GroupAsyncTask(this, header).execute().get();
      //      Log.e("group", listgroup + "");
      //  } catch (InterruptedException e) {
      //      e.printStackTrace();
      //  } catch (ExecutionException e) {
      //      e.printStackTrace();
      //  }
    }

    private void attemptDaftar() {

        // Reset errors.
        uNama.setError(null);
        uEmail.setError(null);
        uPassword.setError(null);
        ucPassword.setError(null);


        // Store values at the time of the login attempt.
        email = uEmail.getText().toString();
        password = uPassword.getText().toString();
        cpassword = ucPassword.getText().toString();
        nama = uNama.getText().toString();
        phone = uPhone.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            uPassword.setError(getString(R.string.error_invalid_password));
            focusView = uPassword;
            cancel = true;
        }

        if (!password.equals(cpassword)) {
            ucPassword.setError(getString(R.string.password_didnot_match));
            focusView = ucPassword;
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

        if (TextUtils.isEmpty(nama)) {
            uNama.setError(getString(R.string.err_msg_name));
            focusView = uNama;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (phone.equals("")) {
                phone = "0";
            }
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.

            if (setatus.equals("login")) {
                hashMap = new HashMap<String, String>();
                hashMap.put(FieldName.FULLNAME, nama);
                hashMap.put(FieldName.EMAIL, email);
                hashMap.put(FieldName.PASSWORD, password);
                hashMap.put(FieldName.PHONE_NUMBER, phone);
//                Log.e("hashmap", hashMap + "");

//            try {
//                login = new RegisterAsyncTask(this, hashMap).execute().get();
                new RegisterAsyncTask(this, hashMap) {
                    protected void onPreExecute() {
                        progressDialog.show();
                        progressDialog.setMessage("Please Wait, registering to dishub server.");
                        progressDialog.setTitle("SiPekaAPILL");

                    }

                    protected void onPostExecute(final Login login) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String status = login.getStatus();
                        String message = login.getMessage();
//                        Log.e("stat mess", status + "," + message);
                        if (status != null) {
                            if (status.equals("true")) {
                                Toast.makeText(RegisterUserActivity.this, message, Toast.LENGTH_LONG).show();

                                finish();
                                intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else if (status.equals("false")) {
                                Toast.makeText(RegisterUserActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Registrasi gagal.", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();

//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            }
            } else {
                hashMap = new HashMap<String, String>();
                hashMap.put(FieldName.FULLNAME, nama);
                hashMap.put(FieldName.EMAIL, email);
                hashMap.put(FieldName.PASSWORD, password);
                hashMap.put(FieldName.PHONE_NUMBER, phone);
                hashMap.put(FieldName.ID_GROUP, level);
//                Log.e("hashmap", hashMap + "");

                new RegisterUserAsyncTask(this, hashMap) {
                    protected void onPreExecute() {
                        progressDialog.show();
                        progressDialog.setMessage("Please Wait, registering to dishub server.");
                        progressDialog.setTitle("SiPekaAPILL");

                    }

                    protected void onPostExecute(final Login login) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        String status = login.getStatus();
                        String message = login.getMessage();
//                        Log.e("stat mess", status + "," + message);
                        if (status != null) {
                            if (status.equals("true")) {
                                Toast.makeText(RegisterUserActivity.this, message, Toast.LENGTH_LONG).show();

                                finish();
                                intent = new Intent(RegisterUserActivity.this, Pegawai1Activity.class);
                                startActivity(intent);
                            } else if (status.equals("false")) {
                                Toast.makeText(RegisterUserActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(RegisterUserActivity.this, "Registrasi gagal.", Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
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

    @Override
    public void onBackPressed() {

        if (setatus.equals("login")) {
            finish();
            intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
            startActivity(intent);
        } else {
            finish();
            intent = new Intent(RegisterUserActivity.this, Pegawai1Activity.class);
            startActivity(intent);
        }
    }
}
