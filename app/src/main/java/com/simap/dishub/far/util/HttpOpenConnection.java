package com.simap.dishub.far.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Aviroez on 26/06/2015.
 */
public class HttpOpenConnection {

    public static String post(String uri, HashMap<String, String> params) {
        String response = "";
        HttpURLConnection conn = null;
        HttpOpenConnection open = new HttpOpenConnection();
        try {
            URL url = new URL(uri);
            Log.e("url", uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(open.getPostDataString(params));
//            if (Configs.ENCRYPTED_MESSAGE) {
//                writer.write(open.encryptJsonPost(params));
//            } else {
//                writer.write(open.getPostToJson(params));
////                Log.e("getPostToJson", getPostToJson(params));
//            }
            StringBuilder result = new StringBuilder();
            String[] split = null;
            for(int i = 0; i < params.toString().substring(1, params.toString().length()-1).split(",").length; i++){
                split = params.toString().substring(1, params.toString().length()-1).split(",")[i].split("=");
                /*if(split.length==0){
                    split[0] = params.toString().substring(1, params.toString().length());
                }*/
                if(result.length() != 0) {
                    result.append("&");
                }
                if(split[0].substring(0,1).equals(" ")){
                    split[0] = split[0].substring(1,split[0].length());
                }
                result.append(split[0]+"="+split[1]);
            }
            Log.e("postdatas", result.toString());
            writer.write(result.toString());
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            Log.e("response", ":" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String postheader(String uri, HashMap<String, String> params, String header) {
        String response = "";
        HttpURLConnection conn = null;
        HttpOpenConnection open = new HttpOpenConnection();
        try {
            URL url = new URL(uri);
            Log.e("url", uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", header);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
//            writer.write(open.getPostDataString(params));
//            if (Configs.ENCRYPTED_MESSAGE) {
//                writer.write(open.encryptJsonPost(params));
//            } else {
//                writer.write(open.getPostToJson(params));
////                Log.e("getPostToJson", getPostToJson(params));
//            }
            StringBuilder result = new StringBuilder();
            String[] split = null;
            for(int i = 0; i < params.toString().substring(1, params.toString().length()-1).split(",").length; i++){
                split = params.toString().substring(1, params.toString().length()-1).split(",")[i].split("=");
                /*if(split.length==0){
                    split[0] = params.toString().substring(1, params.toString().length());
                }*/
                if(result.length() != 0) {
                    result.append("&");
                }
                if(split[0].substring(0,1).equals(" ")){
                    split[0] = split[0].substring(1,split[0].length());
                }
                result.append(split[0]+"="+split[1]);
            }
            Log.e("postdatas", result.toString());
            writer.write(result.toString());
            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            Log.e("response", ":" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String get(String uri, String header) {
        String response = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(uri);
            Log.e("url", uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", header);
//            conn.setRequestProperty("page", page);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            Log.e("response", ":" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    public static String get1(String uri, String header, String page) {
        String response = "";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(uri);
            Log.e("url", uri);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", header);
            conn.setRequestProperty("page", page);

            int responseCode = conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";
            }
            Log.e("response", ":" + response);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }
}