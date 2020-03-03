package com.suyee.mm.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;

import com.suyee.mm.MainActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class ApiService {
    private static final String API_URL = "https://data.gov.sg/api/action/datastore_search?resource_id=a807b7ab-6cad-4aa6-87d0-e283a7353a0f";
    private static final String ERROR_FORMAT = "{\"code\": \"%s\", \"message\": \"%s\"}";

    public String callApi(){
        String result = null;
        try{
            result = new ApiTask().execute(API_URL).get();
        }catch(ExecutionException ex){
            ex.printStackTrace();
        }catch (InterruptedException ex){
            ex.printStackTrace();
        }
        return result;
    }

    private class ApiTask extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... params) {
            publishProgress(0);
            return requestData();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }

    public static boolean isNetworkAvailable(){
        ConnectivityManager connection = (ConnectivityManager) MainActivity.activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connection != null){
            if(Build.VERSION.SDK_INT < 23){
                final NetworkInfo netInfo = connection.getActiveNetworkInfo();
                if(netInfo != null)
                    return (netInfo.isConnected() && (netInfo.getType() == ConnectivityManager.TYPE_WIFI || netInfo.getType() == ConnectivityManager.TYPE_MOBILE));
            }else{
                final Network network = connection.getActiveNetwork();
                if(network != null){
                    final NetworkCapabilities netCabilities = connection.getNetworkCapabilities(network);
                    return (netCabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || netCabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }
        return false;
    }

    /* For response code 200 ==>> response json will return without error code */
    public String requestData(){
        StringBuffer buffer = new StringBuffer();
        try{
            URL url = new URL(API_URL);
            HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
            https.setDoOutput(true);
            int respCode = https.getResponseCode();
            if(respCode == 200){
                BufferedReader input = new BufferedReader(new InputStreamReader(https.getInputStream()));
                String line;
                while((line = input.readLine()) != null){
                    buffer.append(line);
                }
                input.close();
            } else {
                buffer.append(String.format(ERROR_FORMAT, Integer.toString(https.getResponseCode()), https.getResponseMessage()));
            }
        }catch (MalformedURLException ex){
            buffer.append(String.format(ERROR_FORMAT, 9001, ex.getMessage()));
        }catch (IOException ex){
            buffer.append(String.format(ERROR_FORMAT, 9001, ex.getMessage()));
        }
        return buffer.toString();
    }
}
