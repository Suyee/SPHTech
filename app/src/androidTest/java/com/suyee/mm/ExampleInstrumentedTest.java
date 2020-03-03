package com.suyee.mm;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4ClassRunner.class)
public class ExampleInstrumentedTest {

    private Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo, mobileInfo;

    @Before
    public void init(){
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        mobileInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    }

    @Test
    public void useAppContext() {
        Assert.assertEquals("com.suyee.mm", context.getPackageName());
    }

    @Test
    public void networkTest_connectedMobile(){
        Assert.assertEquals(ConnectivityManager.TYPE_MOBILE, mobileInfo.getType());
        Assert.assertEquals(NetworkInfo.DetailedState.CONNECTED, mobileInfo.getDetailedState());
    }

    @Test
    public void networkTest_connectedWifi(){
        Assert.assertEquals(ConnectivityManager.TYPE_WIFI, wifiInfo.getType());
        Assert.assertEquals(NetworkInfo.DetailedState.CONNECTED, wifiInfo.getDetailedState());
    }
}
