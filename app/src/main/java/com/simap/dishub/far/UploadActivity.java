package com.simap.dishub.far;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.simap.dishub.far.asynctas.AreaListAsyncTask;
import com.simap.dishub.far.asynctas.CategoryListAsyncTask;
import com.simap.dishub.far.asynctas.LaporanAsyncTask;
import com.simap.dishub.far.asynctas.NotifTopikAsyncTask;
import com.simap.dishub.far.asynctas.ViaListAsyncTask;
import com.simap.dishub.far.entity.Area;
import com.simap.dishub.far.entity.Category;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.Laporan;
import com.simap.dishub.far.entity.Notification;
import com.simap.dishub.far.entity.Via;
import com.simap.dishub.far.util.CustomProgressDialog;
import com.simap.dishub.far.util.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import id.zelory.compressor.Compressor;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class UploadActivity extends AppCompatActivity implements TextWatcher, AdapterView.OnItemClickListener {

    Button GetImageFromGalleryButton, UploadImageOnServerButton, fotob;
    ImageView ShowSelectedImage;
    EditText noteMasalah;
    private Bitmap bitmap;
    String ImageTag = "image_tag";

    String ImageName = "image_data";
    String ImageInfo = "image_info";
    String ImageArea = "image_area";
    String ImageUser = "image_user";
    String ImageLati = "image_lati";
    String ImageLongi = "image_longi";
    private Spinner spinnerInfo;
    private Spinner spinnerArea, spinnerVia;
    private String timeStamp, kolom1;
    String ServerUploadPath = "http://182.253.200.185/dishub2/upload/upload.php";
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] byteArray;
    String ConvertImage;
    String NoteMas;
    private SessionManager session;
    String Info;
    String Area, filePath, sys_file_id;
    String iduser;
    Context mContext;
    //Button btnShowLocation;
    GPSTracker gps;
    HttpURLConnection httpURLConnection;
    URL url;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    int RC;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    private RequestQueue queue;
    private RequestQueue queue2;
    protected List<DataObject> spinnerData;
    protected List<DataObject> spinnerData2;
    private static final String PATH_TO_SERVER = "http://182.253.200.185/dishub2/index.php/c_spinner_hp/info";
    private static final String PATH_TO_SERVER2 = "http://182.253.200.185/dishub2/index.php/c_spinner_hp/area";
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    boolean check = true;
    private Uri fileUri;
    private String lati, longi;
    private CustomProgressDialog progressDialog;
    private double latitude, longitude;
    private static final int CAMERA_REQUEST = 1888;
    private ArrayAdapter<Area> areaArrayAdapter;
    private ArrayAdapter catArrayAdapter, viaArrayAdapter;
    private String idcat, idarea, idwilayah, header, idfoto, error, idvia, jenisakun;
    private HashMap<String, String> hashMap;
    private Intent intent;
    private ImageView imgPreview;
    private int serverResponseCode = 0;
    private Area area;
    private List<Area> listArea;
    private File mediaStorageDir;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String p1 = Manifest.permission.ACCESS_FINE_LOCATION, p2 = Manifest.permission.WRITE_EXTERNAL_STORAGE,
            p3 = Manifest.permission.READ_EXTERNAL_STORAGE,
            kolom, namawil, strarea;

    private AutoCompleteTextView mEmailView;
    private Location location;
    private LocationManager lm;
    private EditText textKode;
    private Boolean isValid = false;
    private File compressedImageFile;
    private ListView lv;
    private Toast toast;
    private LinearLayout linVia;
    private BitmapFactory.Options options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        GetImageFromGalleryButton = (Button) findViewById(R.id.button);
        UploadImageOnServerButton = (Button) findViewById(R.id.button2);
        fotob = (Button) findViewById(R.id.buttonUpload);
        spinnerInfo = (Spinner) findViewById(R.id.spInfo);
        spinnerArea = (Spinner) findViewById(R.id.spArea);
        spinnerVia = (Spinner) findViewById(R.id.spVia);
        ShowSelectedImage = (ImageView) findViewById(R.id.imageView);
        noteMasalah = (EditText) findViewById(R.id.editText);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        textKode = (EditText) findViewById(R.id.textKode);
        lv = (ListView) findViewById(R.id.lv_data);
        linVia = (LinearLayout) findViewById(R.id.layVia);

        progressDialog = new CustomProgressDialog(this);

        header = Utils.readSharedSetting(UploadActivity.this, "api_key", null);
        Log.e("header", header);

        fotob.setEnabled(true);

        jenisakun = Utils.readSharedSetting(this, "jenisakun", "kosong");
        Log.e("jenisakun", jenisakun);
        idvia = "1";

        requestJsonObject();

        if (Build.VERSION.SDK_INT >= 23) {
            permissionAccess();
        } else {
            gps();
        }

//        gps = new GPSTracker(mContext, UploadActivity.this);
//
//        // Check if GPS enabled
//        if (gps.canGetLocation()) {
//
//            latitude = gps.getLatitude();
//            longitude = gps.getLongitude();
//            Log.e("gps", "ok");
//        } else {
//            // Can't get location.
//            // GPS or network is not enabled.
//            // Ask user to enable GPS/network in settings.
//            Toast.makeText(UploadActivity.this, "Tidak dapat memuat lokasi.", Toast.LENGTH_LONG).show();
////                finish();
//        }

        byteArrayOutputStream = new ByteArrayOutputStream();

        mContext = this;

        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            fotob.setEnabled(false);
        }


        textKode.addTextChangedListener(this);
        lv.setOnItemClickListener(this);

        UploadImageOnServerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isValid = validation();
                if (isValid) {
                    lati = String.valueOf(latitude);
                    longi = String.valueOf(longitude);
                    Log.e("longilati", lati + " , " + longi);
                    NoteMas = noteMasalah.getText().toString();
                    if (NoteMas.equals("")) {
                        NoteMas = "Tidak ada catatan";
                    }

                    progressDialog.show();
                    progressDialog.setMessage("Please Wait, Sending Report to server.");
                    progressDialog.setTitle("SiPekaAPILL");

                    new Thread(new Runnable() {
                        public void run() {
                            if (filePath != null) {
                                Log.e("filepath", filePath);
                                uploadFile(filePath);

                            } else {
//                            Log.e("else", filePath);
                                sys_file_id = "0";
                            }

                            LaporkanMasalah();
                        }
                    }).start();

//                    if (filePath != null) {
//                        Log.e("filepath", filePath);
//                        progressDialog.show();
//                        progressDialog.setMessage("Please Wait, Sending Report to server.");
//                        progressDialog.setTitle("SiPekaAPILL");
//                        uploadFile(filePath);
//
//                    } else {
////                            Log.e("else", filePath);
//                        sys_file_id = "0";
//                    }
//
//                    LaporkanMasalah();

                }

            }
        });

    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0,
                                  int arg1, int arg2,
                                  int arg3) {
        lv.setVisibility(View.GONE);
    }

    @Override
    public void onTextChanged(CharSequence s, int arg1,
                              int arg2, int arg3) {
        kolom = textKode.getText().toString().trim();
        if (kolom.equals("")) {
            lv.setVisibility(View.GONE);
//            camerapreview.setVisibility(View.VISIBLE);
        } else {
            lv.setVisibility(View.VISIBLE);
            areaArrayAdapter.getFilter().filter(s.toString());
//            Log.e("adapterwathcer ", areaArrayAdapter+"");
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0,
                            View arg1, int position,
                            long id) {
        //TODO Auto-generated method stub
        textKode.setText(areaArrayAdapter.getItem(position).toString());
        lv.setVisibility(View.GONE);

        namawil = areaArrayAdapter.getItem(position).toString();
        idarea = areaArrayAdapter.getItem(position).getId_area();
        idwilayah = areaArrayAdapter.getItem(position).getId_wilayah();
        Log.e("id area id wil", idarea + " ;;; " + idwilayah);
    }

    protected void gps() {
        lm = (LocationManager) UploadActivity.this.getSystemService(Context.LOCATION_SERVICE);
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                longitude = location.getLongitude();
                latitude = location.getLatitude();
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
    }

    private void permissionAccess() {

        if (!checkPermission(p2)) {
            Log.e("TAG", p2);
            requestPermission(p2);
        } else if (!checkPermission(p3)) {
            Log.e("TAG", p3);
            requestPermission(p3);
        } else if (!checkPermission(p1)) {
            Log.e("TAG", p1);
            requestPermission(p1);
        } else {
            Toast.makeText(UploadActivity.this, "All permission granted", Toast.LENGTH_LONG).show();

            gps();

            // External sdcard location
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Android File Upload");
            Log.e("dir", String.valueOf(mediaStorageDir));

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("TAG", "Oops! Failed create "
                            + "Android File Upload" + " directory");
                }
            }

            bitmap = BitmapFactory.decodeFile(filePath, options);
        }

    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(UploadActivity.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {

        if (ContextCompat.checkSelfPermission(UploadActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(UploadActivity.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            //Do the stuff that requires permission...
            Log.e("TAG", "Not say request");
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onActivityResult(int requestCoded, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCoded == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                filePath = fileUri.getPath();
                Log.e("path", String.valueOf(filePath));
                // successfully captured the image
                // launching upload activity
                if (filePath != null) {
                    // Displaying the image or video on the screen

                    previewMedia();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
                }

            } else if (resultCode == RESULT_CANCELED) {

                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();

            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }

        }
    }

    private void previewMedia() {
        // Checking whether captured media is image or video
        imgPreview.setVisibility(View.VISIBLE);
        try {
            outputStream = new FileOutputStream("/sdcard/test.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }

//            vidPreview.setVisibility(View.GONE);
        // bimatp factory
        options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        Log.e("bitmap", String.valueOf(outputStream) + " ;;; " + String.valueOf(options));
        bitmap = BitmapFactory.decodeFile(filePath, options);
        Log.e("bitmap asd", String.valueOf(bitmap));
//        bitmap = ((BitmapDrawable) imgPreview.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);
//        Bitmap bitmap = Bitmap.createScaledBitmap(capturedImage, width, height, true);

        imgPreview.setImageBitmap(bitmap);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(UploadActivity.this,
                BuildConfig.APPLICATION_ID + ".provider",
                getOutputMediaFile(type));
    }

    private static File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "Android File Upload");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TAG", "Oops! Failed create "
                        + "Android File Upload" + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }

    public void ambil_foto(View view) {
//        foto();

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        Log.e("uri", String.valueOf(fileUri));

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
//                Log.e("TAG", "val " + grantResults[0]);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAccess();
                } else {
                    Toast.makeText(UploadActivity.this, "Anda harus mengizinkan aplikasi untuk beroperasi, silahkan restart smartphone anda" +
                            " untuk mengizinkan aplikasi beroperasi kembali.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void requestJsonObject() {
        new AreaListAsyncTask(this, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.show();
                progressDialog.setMessage("Please Wait");
                progressDialog.setTitle("SiPekaAPILL");
            }

            @Override
            protected void onPostExecute(final List<Area> listArea) {
                super.onPostExecute(listArea);


                if (listArea != null) {
                    if (areaArrayAdapter == null) {
                        areaArrayAdapter = new ArrayAdapter<com.simap.dishub.far.entity.Area>(UploadActivity.this,
                                R.layout.custom_dropdown_menu, listArea);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                        lv.setAdapter(areaArrayAdapter);
                    }
                }

//                spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                        idarea = listArea.get(spinnerArea.getSelectedItemPosition()).getId_area();
//                        idwilayah = listArea.get(spinnerArea.getSelectedItemPosition()).getId_wilayah();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> adapterView) {
//
//                    }
//                });
            }
        }.execute();

        new CategoryListAsyncTask(this, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final List<Category> listCat) {
                super.onPostExecute(listCat);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (catArrayAdapter == null) {
                    catArrayAdapter = new ArrayAdapter<com.simap.dishub.far.entity.Category>(UploadActivity.this, R.layout.custom_dropdown_menu, listCat);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                    spinnerInfo.setAdapter(catArrayAdapter);
                }

                spinnerInfo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        idcat = listCat.get(spinnerInfo.getSelectedItemPosition()).getId_category();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }.execute();

        if (jenisakun.equals("admin")){
            linVia.setVisibility(View.VISIBLE);
            new ViaListAsyncTask(this, header) {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }

                @Override
                protected void onPostExecute(final List<Via> listVia) {
                    super.onPostExecute(listVia);

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                    if (viaArrayAdapter == null) {
                        viaArrayAdapter = new ArrayAdapter<com.simap.dishub.far.entity.Via>(UploadActivity.this, R.layout.custom_dropdown_menu, listVia);
//                    operatorArrayAdapter.setDropDownViewResource(android.R.layout.simple_expandable_list_item_1);
                        spinnerVia.setAdapter(viaArrayAdapter);
                    }

                    spinnerVia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            idvia = listVia.get(spinnerVia.getSelectedItemPosition()).getId_pelaporan_via();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }.execute();
        } else {
            linVia.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                NavUtils.navigateUpFromSameTask(this);
                finish();
                Intent intent = new Intent(UploadActivity.this, Pegawai1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @SuppressLint("StaticFieldLeak")
    public void LaporkanMasalah() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        hashMap.put(FieldName.ID_WILAYAH, idwilayah);
        hashMap.put(FieldName.NOTE_INFRA, NoteMas);
        hashMap.put(FieldName.LATITUDE, lati);
        hashMap.put(FieldName.LONGITUDE, longi);
        hashMap.put(FieldName.SYS_FILE_ID, sys_file_id);
        if (jenisakun.equals("admin")){
            hashMap.put(FieldName.ID_PELAPORAN_VIA, idvia);
        }
        Log.e("hashmap", hashMap + "");

        new LaporanAsyncTask(UploadActivity.this, hashMap, header) {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(final Laporan laporan) {
                super.onPostExecute(laporan);

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                String status = laporan.getStatus();
                String message = laporan.getMessage();
                Log.e("stat mess", status + " , " + message);

                if (status != null) {
                    if (status.equals("true")) {
                        Toast.makeText(UploadActivity.this, "Laporan berhasil terkirim", Toast.LENGTH_LONG).show();
                        finish();

                        Log.e("jenisakun", jenisakun);

                        notif();
                        notif1();

                        if (jenisakun.equals("user")) {
                            Intent intent = new Intent(UploadActivity.this, Menu1Activity.class);
                            startActivity(intent);
                        } else if (jenisakun.equals("teknisi")) {
                            Intent intent = new Intent(UploadActivity.this, Teknisi1Activity.class);
                            startActivity(intent);
                        } else if (jenisakun.equals("admin")) {
                            Intent intent = new Intent(UploadActivity.this, Pegawai1Activity.class);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(UploadActivity.this, "Maaf! " + message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();

    }

    private boolean validation() {
        strarea = textKode.getText().toString().trim();

        if (strarea.isEmpty()) {
            Toast.makeText(this, "Area Infra tidak boleh kosong.", toast.LENGTH_LONG).show();
            textKode.requestFocus();
            return false;
        } else if (!strarea.equals(namawil)) {
            Toast.makeText(this, "Area Infra tidak terdaftar.", toast.LENGTH_LONG).show();
            textKode.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("StaticFieldLeak")
    protected void notif() {
        kolom1 = kolom.replace(",","-");
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "teknisi");
        hashMap.put(FieldName.MESSAGE, "Ada Pelaporan Masuk (" + kolom1 + ")");
        Log.e("hashmap", hashMap + "");

        new NotifTopikAsyncTask(this, hashMap) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(Notification notification) {
                super.onPostExecute(notification);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                if (notification != null) {
                    Toast.makeText(UploadActivity.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UploadActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void notif1() {
        kolom1 = kolom.replace(",","-");
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "admin");
        hashMap.put(FieldName.MESSAGE, "Ada Pelaporan Masuk (" + kolom1 + ")");
        Log.e("hashmap", hashMap + "");

        new NotifTopikAsyncTask(this, hashMap) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportAllProgresActivity.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(Notification notification) {
                super.onPostExecute(notification);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();

                if (notification != null) {
                    Toast.makeText(UploadActivity.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(UploadActivity.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

    public int uploadFile(String sourceFileUri) {

        String response = "";
        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
//        String boundary = "----WebKitFormBoundary7MA4YWxkTrZu0gW";
        String boundary = "123456";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile", "Source File not exist :" + sourceFile);
            this.runOnUiThread(new Runnable() {
                public void run() {

                }
            });
            return 0;
        } else {
//            try {
//                compressedImageFile = new Compressor(this).compressToFile(sourceFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            try {
                compressedImageFile = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(sourceFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("ëlse file", compressedImageFile + "");

            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(compressedImageFile);
                URL url = new URL(Urls.urllaporan + Urls.upload_file);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Authorization", header);
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
//                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary + lineEnd + lineEnd);
                //conn.setRequestProperty("img", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"data\"" + lineEnd + lineEnd);
//                dos.writeBytes(datas + lineEnd);
//                Log.e("DATANYAAA", String.valueOf(datas));
//               /* dos.writeBytes(twoHyphens + boundary + lineEnd);
//                dos.writeBytes("Content-Disposition: form-data; name=\"ImageShot\"" + lineEnd + lineEnd);
//                dos.writeBytes(tag + lineEnd);*/
//
//                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"files\"; filename=\""
                        + compressedImageFile + "\"" + lineEnd);
                dos.writeBytes("Content-Type: image/jpg" + lineEnd + lineEnd);
//                dos.writeBytes("Content-Transfer-Encoding: binary" + lineEnd + lineEnd);
//                dos.writeBytes(twoHyphens + boundary);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

//                InputStream inputStream = conn.getInputStream();
                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.e("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);
                /////
                if (serverResponseCode == HttpsURLConnection.HTTP_OK) {
//                    progressDialog.dismiss();
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;
                    }

                } else {
//                    progressDialog.dismiss();
                    response = "";
                }
                Log.e("responses", ":" + response);

                listArea = new ArrayList<Area>();

                if (!response.equals("")) {
                    JSONObject json = new JSONObject(response);
                    String files = json.has(FieldName.FILES) ? json.getString(FieldName.FILES) : "0";
                    if (!files.equals("0")) {

                        JSONArray jsonArray = json.getJSONArray(FieldName.FILES);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            idfoto = jsonObject.has(FieldName.ID) ? jsonObject.getString(FieldName.ID) : "";
                            error = jsonObject.has(FieldName.ERROR) ? jsonObject.getString(FieldName.ERROR) : "";

                            area = new Area();
                            area.setId(idfoto);
                            area.setError(error);
                            listArea.add(area);


                            if (idfoto.equals("")) {
                                Toast.makeText(UploadActivity.this, listArea.get(0).getError(), Toast.LENGTH_LONG).show();
                            } else {
                                sys_file_id = listArea.get(0).getId();
                                Log.e("sys id", sys_file_id);
                            }
                        }
                    }
                } else {
                    Toast.makeText(UploadActivity.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
                }

                conn.disconnect();
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {


                ex.printStackTrace();
                progressDialog.dismiss();

                this.runOnUiThread(new Runnable() {
                    public void run() {

                        Toast.makeText(UploadActivity.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
                    }
                });

                //Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                this.runOnUiThread(new Runnable() {
                    public void run() {

                        // Toast.makeText(getActivity(), "Can not upload", Toast.LENGTH_SHORT).show();
                    }
                });
                //Log.e("Upload file to server Exception", "Exception : " + e.getMessage(), e);
            }
            //progressDialog.dismiss();

            return serverResponseCode;

        }
    }

    @Override
    public void onBackPressed() {
        if (jenisakun.equals("user")) {
            finish();
            Intent intent = new Intent(UploadActivity.this, Menu1Activity.class);
            startActivity(intent);
        } else if (jenisakun.equals("teknisi")) {
            finish();
            Intent intent = new Intent(UploadActivity.this, Teknisi1Activity.class);
            startActivity(intent);
        } else if (jenisakun.equals("admin")) {
            finish();
            Intent intent = new Intent(UploadActivity.this, Pegawai1Activity.class);
            startActivity(intent);
        }
    }

}
