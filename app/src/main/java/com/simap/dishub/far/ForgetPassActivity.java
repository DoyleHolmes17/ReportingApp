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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.ForgetAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Login;
import com.simap.dishub.far.util.CustomProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForgetPassActivity extends AppCompatActivity {

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
    Button uKirim;
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
        setContentView(R.layout.activity_forget_pass);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new CustomProgressDialog(this);

        uEmail = (EditText) findViewById(R.id.input_email);

        intent = getIntent();
        setatus = intent.getStringExtra(FieldName.STATUS);

        uKirim = (Button) findViewById(R.id.btn_kirim);
        uKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Kirim();
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

    private void Kirim() {

        uEmail.setError(null);

        email = uEmail.getText().toString();

        boolean cancel = false;
        View focusView = null;

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

        if (cancel) {
            focusView.requestFocus();
        } else {

            hashMap = new HashMap<String, String>();
            hashMap.put(FieldName.EMAIL, email);

            new ForgetAsyncTask(this, hashMap) {
                protected void onPreExecute() {
                    progressDialog.show();
                    progressDialog.setMessage("Please Wait, connecting to dishub server.");
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
                            Toast.makeText(ForgetPassActivity.this, message, Toast.LENGTH_LONG).show();

                            finish();
                            intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else if (status.equals("false")) {
                            Toast.makeText(ForgetPassActivity.this, message, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ForgetPassActivity.this, "Gagal mengirim email.", Toast.LENGTH_LONG).show();
                    }
                }
            }.execute();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    @Override
    public void onBackPressed() {
        finish();
        intent = new Intent(ForgetPassActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
