package com.simap.dishub.far;

        import android.app.Fragment;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.net.HttpURLConnection;
        import java.net.URL;
        import java.util.HashMap;

/**
 * Created by Denny on 10/24/2016.
 */
public class ReportLama extends Fragment{
    EditText judul_masalah, desc_masalah;
    Button submit_laporan;
    //public ReportLama(){}
    //RelativeLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View V = inflater.inflate(R.layout.laporan, container, false);
        getActivity().setTitle("Pelaporan");

        /*submit_laporan.setOnClickListener(new View.OnClickListener(){
            String newJudul = judul_masalah.getText().toString();
            String newDeskripsi = desc_masalah.getText().toString();
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(), newJudul,
                        Toast.LENGTH_LONG).show();
                new sendData().execute(newJudul,newDeskripsi);
            }
        });*/
        return V;
    }

    public void onViewCreated(View V, Bundle savedInstanceState){
        judul_masalah = (EditText) V.findViewById(R.id.judul_masalah);
        desc_masalah = (EditText) V.findViewById(R.id.desc_masalah);
        submit_laporan = (Button) V.findViewById(R.id.submit_laporan);

        submit_laporan.setOnClickListener(new View.OnClickListener(){
            String newJudul = judul_masalah.getText().toString();
            String newDeskripsi = desc_masalah.getText().toString();
            @Override
            public void onClick(View v){

                Toast.makeText(getActivity(), newJudul,
                        Toast.LENGTH_LONG).show();
                //new sendData().execute(newJudul,newDeskripsi);
            }
        });
    }

    public class sendData extends AsyncTask<String, String, JSONObject>{
        HttpURLConnection conn;
        URL url = null;
        JSONParser jsonParser = new JSONParser();
        private static final String TAG_INFO = "info";
        //private static final String LAPORAN_URL = "https://182.253.200.188/dishub2";
     private static final String LAPORAN_URL = "http://182.253.200.185/c_laporan_hp";


        @Override
        protected JSONObject doInBackground(String... args) {
            try {

                HashMap<String, String> params = new HashMap<>();
                params.put("judul", args[0]);
                params.put("deskripsi", args[1]);

                Log.e("request", "starting");

                JSONObject json = jsonParser.makeHttpRequest(
                        LAPORAN_URL, "POST", params);

                if (json != null) {
                    Log.e("JSON result", json.toString());

                    return json;
                } else {
                    Log.e("JSON else result", json.toString());
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
                Toast.makeText(getActivity(), "Data Berhasil Disimpan",
                        Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Data Gagal Disimpan",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}