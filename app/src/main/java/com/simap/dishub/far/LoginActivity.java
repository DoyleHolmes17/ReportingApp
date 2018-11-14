package com.simap.dishub.far;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.LoginAsyncTask;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Login;
import com.simap.dishub.far.util.Devices;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    private SessionManager session;
    private Login login;
    private HashMap<String, String> hashMap;
    private Intent intent;
    private String emei, jenisakun;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String p1 = Manifest.permission.READ_CONTACTS, p2 = Manifest.permission.READ_PHONE_STATE, p3 = Manifest.permission
            .INTERNET;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
//    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView txRegister, txLupapass;
    Toolbar toolbar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        toolbar.setTitle("Login SiPekaAPILL");

        session = new SessionManager(getApplicationContext());
        int akses = session.isUser();
        if (session.isLoggedIn()) {
            Log.e("akses >> ", akses + "");
            if (akses == 4) {
                intent = new Intent(LoginActivity.this, Pegawai1Activity.class);
                intent.putExtra("nama", session.isNama());
                intent.putExtra("email", session.isEmail());
                intent.putExtra("hakakses", session.isUser());
                startActivity(intent);
                LoginActivity.this.finish();
            } else if (akses == 2) {
                intent = new Intent(LoginActivity.this, Teknisi1Activity.class);
                intent.putExtra("nama", session.isNama());
                intent.putExtra("email", session.isEmail());
                intent.putExtra("hakakses", session.isUser());
                startActivity(intent);
                LoginActivity.this.finish();
            } else if (akses == 1) {
                intent = new Intent(LoginActivity.this, Menu1Activity.class);
                intent.putExtra("nama", session.isNama());
                intent.putExtra("email", session.isEmail());
                intent.putExtra("hakakses", session.isUser());
                startActivity(intent);
                LoginActivity.this.finish();
            }
        }
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        if (Build.VERSION.SDK_INT >= 23) {
            permissionAccess();
        } else {
            getLoaderManager().initLoader(0, null, this);

            emei = Devices.getImei(LoginActivity.this);
            Log.e("emei", emei);
        }

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 1 || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        txRegister = (TextView) findViewById(R.id.txRegister);
        txLupapass = (TextView) findViewById(R.id.txLupapass);

        jenisakun = Utils.readSharedSetting(this, "jenisakun", "kosong");
        Log.e("session", jenisakun);
        if (jenisakun.equals("user")) {
            finish();
            intent = new Intent(LoginActivity.this, Menu1Activity.class);
            startActivity(intent);
        } else if (jenisakun.equals("teknisi")) {
            finish();
            intent = new Intent(LoginActivity.this, Teknisi1Activity.class);
            startActivity(intent);
        } else if (jenisakun.equals("admin")) {
            finish();
            intent = new Intent(LoginActivity.this, Pegawai1Activity.class);
            startActivity(intent);
        }

        txRegister.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                intent = new Intent(LoginActivity.this, RegisterUserActivity.class);
                intent.putExtra(FieldName.STATUS, "login");
                startActivity(intent);
            }
        });
        txLupapass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                intent = new Intent(LoginActivity.this, ForgetPassActivity.class);
                intent.putExtra(FieldName.STATUS, "login");
                startActivity(intent);
            }
        });
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void permissionAccess() {


        if (!checkPermission(p1)) {
            Log.e("TAG", p1);
            requestPermission(p1);
        } else if (!checkPermission(p2)) {
            Log.e("TAG", p2);
            requestPermission(p2);
        } else if (!checkPermission(p3)) {
            Log.e("TAG", p3);
            requestPermission(p3);
        } else {
            Toast.makeText(LoginActivity.this, "All permission granted", Toast.LENGTH_LONG).show();
            getLoaderManager().initLoader(0, null, this);

            emei = Devices.getImei(LoginActivity.this);
            Log.e("emei", emei);
        }

    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(LoginActivity.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    private void requestPermission(String permission) {

        if (ContextCompat.checkSelfPermission(LoginActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            //Do the stuff that requires permission...
            Log.e("TAG", "Not say request");

            getLoaderManager().initLoader(0, null, this);

            emei = Devices.getImei(LoginActivity.this);
            Log.e("emei", emei);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
//                Log.e("TAG", "val " + grantResults[0]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccess();
                } else {
                    Toast.makeText(LoginActivity.this, "Bye bye", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // Show the Up button in the action bar.
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store custom_btn at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.EMEI, emei);
        hashMap.put(FieldName.EMAIL, email);
        hashMap.put(FieldName.PASSWORD, password);
        Log.e("hashmap", hashMap + "");

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }


        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            try {
                login = new LoginAsyncTask(this, hashMap).execute().get();
                showProgress(false);
                String status = login.getStatus();
                String message = login.getMessage();
                Log.e("stat mess", status + "," + message);
                if (status != null) {
//                    Utils.saveSharedSetting(LoginActivity.this, "session", "ok");
                    if (status.equals("true")) {
                        String group = login.getGrp_group_id();
                        String api_key = login.getApi_key();
                        Utils.saveSharedSetting(LoginActivity.this, "api_key", api_key);
                        Log.e("api grup", api_key + " , " + group);
                        String nama = login.getFullname();

                        session.setLogin(true);
                        session.setNama(nama);
                        session.setEmail(email);
                        Log.e("grup", group);
                        if (group.equals("2") || group.equals("1")) {
                            Utils.saveSharedSetting(LoginActivity.this, "jenisakun", "admin");
                            finish();
                            intent = new Intent(LoginActivity.this, Pegawai1Activity.class);
                            intent.putExtra("nama", nama);
                            intent.putExtra("email", email);
                            intent.putExtra("hakakses", group);
                            startActivity(intent);
                        } else if (group.equals("3")) {
                            Utils.saveSharedSetting(LoginActivity.this, "jenisakun", "teknisi");
                            finish();
                            intent = new Intent(LoginActivity.this, Teknisi1Activity.class);
                            intent.putExtra("nama", nama);
                            intent.putExtra("email", email);
                            intent.putExtra("hakakses", group);
//                            Utils.saveSharedSetting(LoginActivity.this, "iduser", group);
                            startActivity(intent);
                        } else if (group.equals("4")) {
                            Utils.saveSharedSetting(LoginActivity.this, "jenisakun", "user");
                            finish();
                            intent = new Intent(LoginActivity.this, Menu1Activity.class);
                            intent.putExtra("nama", nama);
                            intent.putExtra("email", email);
                            intent.putExtra("hakakses", group);
                            startActivity(intent);
                        }
                    } else if (status.equals("false")) {
                        Toast.makeText(LoginActivity.this, "Email atau password salah.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 3;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
////
////    /**
////     * Represents an asynchronous login/registration task used to authenticate
////     * the user.
////     */
////    public class UserLoginTask1 extends AsyncTask<String, String, JSONObject> {
////
////        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
////        HttpURLConnection conn;
////        URL url = null;
////        JSONParser jsonParser = new JSONParser();
////        private static final String TAG_MESSAGE = "message";
////        private static final String TAG_NAMA = "nama_user";
////        private static final String TAG_EMAIL = "username";
////        private static final String TAG_HAKAKSES = "hakakses_userakses";
////        private static final String TAG_IDUSER = "id";
////        private static final String TAG_ERROR = "error";
////        private static final String LOGIN_URL = "http://182.253.200.185/dishub2/index.php/c_login_hp";
////
////        /*@Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////
////            //this method will be running on UI thread
////            pdLoading.setMessage("\tLoading...");
////            pdLoading.setCancelable(false);
////            pdLoading.show();
////
////        }*/
////        @Override
////        protected JSONObject doInBackground(String... args) {
////
////            try {
////
////                HashMap<String, String> params = new HashMap<>();
////                params.put("username", args[0]);
////                params.put("password", args[1]);
////                params.put("device", args[2]);
////
////                Log.d("request", "starting");
////
////                JSONObject json = jsonParser.makeHttpRequest(
////                        LOGIN_URL, "POST", params);
////
////                if (json != null) {
////                    Log.d("JSON result", json.toString());
////
////                    return json;
////                }
////
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////
////            return null;
////        }
////
////        protected void onPostExecute(JSONObject json) {
////            mAuthTask = null;
////            showProgress(false);
////            String nama = "";
////            int iduser = 0;
////            String email = "";
////            int hakakses = 0;
////            int error_message = 0;
////
////
////            if (json != null) {
////                //Toast.makeText(LoginActivity.this, json.toString(),
////                        //Toast.LENGTH_LONG).show();
////
////                try {
////                    nama = json.getString(TAG_NAMA);
////                    email = json.getString(TAG_EMAIL);
////                    hakakses = json.getInt(TAG_HAKAKSES);
////                    iduser = json.getInt(TAG_IDUSER);
////                    error_message = json.getInt(TAG_ERROR);
////                } catch (JSONException e) {
////                    e.printStackTrace();
////                }
////
////            }
////            if(error_message == 1) {
////                mAuthTask = null;
////                showProgress(false);
////                session.setLogin(true);
////                session.setStatus(hakakses);
////                session.setNama(nama);
////                session.setEmail(email);
////                session.setId(iduser);
////                //Toast.makeText(LoginActivity.this, email,
////                        //Toast.LENGTH_LONG).show();
////                if(hakakses == 4) {
////                    Intent intent = new Intent(LoginActivity.this, PegawaiActivity.class);
////                    intent.putExtra("nama", nama);
////                    intent.putExtra("email", email);
////                    intent.putExtra("hakakses", hakakses);
////                    startActivity(intent);
////                    LoginActivity.this.finish();
////                }else if(hakakses == 2){
////                    Intent intent = new Intent(LoginActivity.this, TeknisiActivity.class);
////                    intent.putExtra("nama", nama);
////                    intent.putExtra("email", email);
////                    intent.putExtra("hakakses", hakakses);
////                    Utils.saveSharedSetting(LoginActivity.this, "iduser", iduser+"");
////                    startActivity(intent);
////                    LoginActivity.this.finish();
////                }else if(hakakses == 1){
////                    Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
////                    intent.putExtra("nama", nama);
////                    intent.putExtra("email", email);
////                    intent.putExtra("hakakses", hakakses);
////                    startActivity(intent);
////                    LoginActivity.this.finish();
////                }
////            }else{
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
////                mPasswordView.requestFocus();
////            }
////
////        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

