package com.emre.netsleep;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by emre on 30.08.2017.
 */

public class WIFIAPI {
    private ConnectivityManager connectivityManager;
    private NetworkInfo mWifi;
    private WifiManager wifiManager;
    private Context context1;

    public WIFIAPI(Context context){
        context1 = context;

        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    public boolean isWifiEnabled(){
        return wifiManager.isWifiEnabled();
    }

    public void toggleWIFI(boolean toggle){
        wifiManager.setWifiEnabled(toggle);
    }
}
