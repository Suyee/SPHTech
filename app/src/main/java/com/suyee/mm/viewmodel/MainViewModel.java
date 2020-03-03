package com.suyee.mm.viewmodel;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.util.MutableBoolean;

import com.suyee.mm.MainActivity;
import com.suyee.mm.model.DataUsage;
import com.suyee.mm.service.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MutableLiveData<ArrayList<DataUsage>> liveData;
    private ArrayList<DataUsage> dataList;
    private MutableLiveData<Boolean> errorFlag;

    private static final String KEY_QUARTER = "quarter";
    private static final String KEY_VOLUME = "volume_of_mobile_data";
    private static final String MSG_FORMAT = "%s-%s: volume of data usage drop from %f to %f";
    private static final String TAG = "SPHTech";
    private static final String CACHE_FILE = MainActivity.activity.getCacheDir() + File.separator + "sphtech.cac";

    public MainViewModel(){
        liveData = new MutableLiveData<>();
        errorFlag = new MutableLiveData<>();
        loadData();
    }

    private void loadData() {
        File cacheFile = new File(CACHE_FILE);
        if(cacheFile.exists()){
            loadCacheData();
        }else{
            if(ApiService.isNetworkAvailable()){
                loadApiData();
            }else{
                // Ni Cache File & No Connection
                errorFlag.setValue(true);
            }
        }
        liveData.setValue(dataList);
    }

    private void loadCacheData(){
        // Load Data from cache file

        try{
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(new File(CACHE_FILE)));
            String result = (String)input.readObject();
            formatData(result);
        }catch (IOException ex){
            ex.printStackTrace();
        }catch (ClassNotFoundException ex){
            ex.printStackTrace();
        }
    }

    private void saveCachFile(String jsonString){
        Log.e(TAG, "<<<<< Save REST Api response to cache file >>>>>");
        try{
            ObjectOutput output = new ObjectOutputStream(new FileOutputStream(new File(CACHE_FILE)));
            output.writeObject(jsonString);
            output.close();
        }catch(FileNotFoundException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void loadApiData(){
        Log.e(TAG, "<<<<< Load Data from REST Api >>>>>");
        ApiService apiService = new ApiService();
        String result = apiService.callApi();
        try{
            JSONObject jObject = new JSONObject(result);
            if(jObject.getString("code") == null)
                formatData(result);
            else
                errorFlag.setValue(true);
        }catch(JSONException ex){
            ex.printStackTrace();
        }
        saveCachFile(result);
    }

    private void formatData(String result){
        try{
            if(result != null && result.length() > 0){
                JSONArray jArray = ((new JSONObject(result)).getJSONObject("result")).getJSONArray("records");
                if(jArray != null && jArray.length() > 0) {
                    Map<String, DataUsage> dataMap = new LinkedHashMap<>();
                    if (dataList == null)
                        dataList = new ArrayList<>();

                    double maxVolume = 0.0;
                    for (int i = 0; i < jArray.length(); i++) {
                        JSONObject jObj = jArray.getJSONObject(i);
                        String quarterValue = jObj.getString(KEY_QUARTER);
                        String year = quarterValue.substring(0, quarterValue.indexOf("-"));
                        String quarter = quarterValue.substring(quarterValue.indexOf("-") + 1, quarterValue.length());

                        DataUsage data = dataMap.get(year);
                        double dataVolume = jObj.getDouble(KEY_VOLUME);

                        if (data != null) {
                            data.setVolume(data.getVolume() + dataVolume);
                        } else {
                            maxVolume = 0.0;
                            data = new DataUsage(year, dataVolume);
                        }

                        if (maxVolume > dataVolume)
                            data.setDownMessage(String.format(MSG_FORMAT, year, quarter, maxVolume, dataVolume));
                        else
                            maxVolume = dataVolume;
                        dataMap.put(year, data);
                    }

                    for (String key : dataMap.keySet())
                        dataList.add(dataMap.get(key));
                }else {
                    throw new Exception("No Record Found!");
                }
            }else{
                throw new Exception("Data Format Error!");
            }
        }catch(JSONException ex){
            ex.printStackTrace();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public MutableLiveData<ArrayList<DataUsage>> getLiveData() { return liveData; }
    public MutableLiveData<Boolean> getErrorFlag() { return errorFlag; }
}
