package com.simap.dishub.far;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.simap.dishub.far.asynctas.ConfirmLaporanTeknisiAsyncTask;
import com.simap.dishub.far.asynctas.NotifTopikAsyncTask;
import com.simap.dishub.far.entity.Area;
import com.simap.dishub.far.entity.FieldName;
import com.simap.dishub.far.entity.ListLaporan;
import com.simap.dishub.far.entity.Notification;
import com.simap.dishub.far.util.CustomProgressDialog;
import com.simap.dishub.far.util.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
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

public class ReportConfDetail extends AppCompatActivity {
    private Spinner spinner;
    private ImageView gambarupload;
    Bitmap FixBitmap;
    private CustomProgressDialog progressDialog;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private HashMap<String, String> hashMap;
    private String header, idcat, idarea, area, noteinf,
            alamatinf, notetek, sys_file_id_k,filePath, idfoto, error ;
    private static final int CAMERA_REQUEST = 1888;
    String updateStatus;
    private Bitmap bitmap;
    private int serverResponseCode = 0;
    private Uri fileUri;
    OutputStream outputStream;
    private Area areab;
    private Intent intent;
    private File compressedImageFile;
    private List<Area> listArea;
    ByteArrayOutputStream byteArrayOutputStream;
    private File mediaStorageDir;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private String p1 = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private ImageView imgPreview;
    private Button fotob;
//    private String[] stat = {
//            "On Scheduling",
//            "Progress",
//            "On Confirmation"
//            "Close"
//    };

    private EditText noteteknis, areaText, noteText, alamatText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportconf_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent in = getIntent();
        idcat = in.getStringExtra(FieldName.ID_CATEGORY);
        idarea = in.getStringExtra(FieldName.ID_AREA);
        area = in.getStringExtra(FieldName.AREA_INFRA);
        noteinf = in.getStringExtra(FieldName.NOTE_INFRA);
        alamatinf = in.getStringExtra(FieldName.ALAMAT_INFRA);
        Log.e("intent", idcat + "," + idarea + "," + area + "," + noteinf + "," + alamatinf);

        progressDialog = new CustomProgressDialog(this);

        areaText = (EditText) findViewById(R.id.detailAreaInfra);
        noteteknis = (EditText) findViewById(R.id.textNoteteknis);
        areaText.setText(area);
        noteText = (EditText) findViewById(R.id.detailNoteInfra);
        noteText.setText(noteinf);
        alamatText = (EditText) findViewById(R.id.detailAlamatInfra);
        alamatText.setText(alamatinf);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        fotob = (Button) findViewById(R.id.buttonUpload);
//        gambarupload = (ImageView) findViewById(R.id.imageView);

//        spinner = (Spinner) findViewById(R.id.detailStatus);

        fotob.setEnabled(true);
        header = Utils.readSharedSetting(ReportConfDetail.this, "api_key", null);
        Log.e("header", header);
        byteArrayOutputStream = new ByteArrayOutputStream();

        if (Build.VERSION.SDK_INT >= 23) {
            permissionAccess();
        }
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            fotob.setEnabled(false);
        }


        // inisialiasi Array Adapter dengan memasukkan string array di atas
//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_spinner_dropdown_item, stat);

        // mengeset Array Adapter tersebut ke Spinner
//        spinner.setAdapter(adapter);
//        if (!status.equals(null)) {
//            int spinnerPosition = adapter.getPosition(status);
//        spinner.setSelection(0);
//        }
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//                //Object item = parent.getItemAtPosition(pos);
//                updateStatus = spinner.getItemAtPosition(pos).toString();
//                Log.e("spinner", updateStatus);
////                if (updateStatus.equals("Progress")) {
////                    gambarupload.setVisibility(View.VISIBLE);
////                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
////                } else {
////                    gambarupload.setVisibility(View.GONE);
////                }
//            }
//
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });

        Button update = (Button) findViewById(R.id.btnUpdateReport);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ReportDetail.this, updateStatus,
                //Toast.LENGTH_LONG).show();
//                if (updateStatus.equals("On Confirmation")) {
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
                            sys_file_id_k = "0";
                        }

                        async();
                    }
                }).start();
//                } else {
//                    Toast.makeText(ReportConfDetail.this, "Status harus diubah terlebih dahulu", Toast.LENGTH_LONG).show();
//                }
            }
        });

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

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(ReportConfDetail.this, permission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission(String permission) {

        if (ContextCompat.checkSelfPermission(ReportConfDetail.this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ReportConfDetail.this, new String[]{permission}, PERMISSION_REQUEST_CODE);
        } else {
            //Do the stuff that requires permission...
            Log.e("TAG", "Not say request");
        }
    }

    private void permissionAccess() {

        if (!checkPermission(p1)) {
            Log.e("TAG", p1);
            requestPermission(p1);
        } else {
            Toast.makeText(ReportConfDetail.this, "All permission granted", Toast.LENGTH_LONG).show();

            // External sdcard location
            mediaStorageDir = new File(
                    Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    "Android File Upload");
            Log.e("dir", "create");

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("TAG", "Oops! Failed create "
                            + "Android File Upload" + " directory");
                }
            }
        }

    }

    public void ambil_foto(View view) {
//        foto();

        intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    public Uri getOutputMediaFileUri(int type) {
        return FileProvider.getUriForFile(ReportConfDetail.this,
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
        BitmapFactory.Options options = new BitmapFactory.Options();

        // down sizing image as it throws OutOfMemory Exception for larger
        // images
        options.inSampleSize = 8;

        bitmap = BitmapFactory.decodeFile(filePath, options);
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


    @SuppressLint("StaticFieldLeak")
    protected void async() {
        notetek = noteteknis.getText().toString().trim();
        if (notetek.equals("")){
            notetek = "Tidak ada catatan";
        }

        Log.e("sys_k", sys_file_id_k);
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.ID_CATEGORY, idcat);
        hashMap.put(FieldName.ID_AREA, idarea);
        hashMap.put(FieldName.NOTE_TEKNIS, notetek);
        hashMap.put(FieldName.SYS_FILE_ID_K, sys_file_id_k );
        Log.e("hashmap", hashMap + "");

        new ConfirmLaporanTeknisiAsyncTask(this, hashMap, header) {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                // Showing progress dialog
//                pDialog = new ProgressDialog(ReportConfDetail.this);
//                pDialog.setMessage("Please wait...");
//                pDialog.setCancelable(false);
//                pDialog.show();
            }

            @Override
            protected void onPostExecute(ListLaporan listLaporan) {
                super.onPostExecute(listLaporan);
                // Dismiss the progress dialog
//                if (pDialog.isShowing())
//                    pDialog.dismiss();
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                if (listLaporan != null) {
                    notif();

                    Toast.makeText(ReportConfDetail.this, listLaporan.getMessage(), Toast.LENGTH_LONG).show();
                    finish();
                    Intent intent = new Intent(ReportConfDetail.this, Teknisi1Activity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(ReportConfDetail.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
                }

                /**
                 * Updating parsed JSON data into ListView
                 * */
            }

        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    protected void notif() {
        hashMap = new HashMap<String, String>();
        hashMap.put(FieldName.TOPIC, "admin");
        hashMap.put(FieldName.MESSAGE, "Konfirmasi Pengerjaan Laporan");
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
                    Toast.makeText(ReportConfDetail.this, notification.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReportConfDetail.this, "Tidak dapat terhubung ke server.", Toast.LENGTH_LONG).show();
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

                            areab = new Area();
                            areab.setId(idfoto);
                            areab.setError(error);
                            listArea.add(areab);


                            if (idfoto.equals("")){
                                Toast.makeText(ReportConfDetail.this, listArea.get(0).getError(), Toast.LENGTH_LONG).show();
                            } else {
                                sys_file_id_k = listArea.get(0).getId();
                                Log.e("sys id", sys_file_id_k);
                            }
                        }
//                        LaporkanMasalah();
                    }
                } else {
                    Toast.makeText(ReportConfDetail.this, "Tidak dapat terhubung ke internet.", Toast.LENGTH_LONG).show();
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

                        Toast.makeText(ReportConfDetail.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                Intent intent = new Intent(ReportConfDetail.this, Teknisi1Activity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
