package com.emre.netsleep;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;

/**
 * Created by emre on 12.05.2017.
 */

//Network Control API

public class NetworkManagement {
    private ConnectivityManager connectivityManager;
    private NetworkInfo mWifi;
    private BluetoothAdapter mBluetoothAdapter;
    private WifiManager wifiManager;
    private Context context1;

    public NetworkManagement(Context context){
        context1 = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        mWifi = (NetworkInfo) connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    }

    public int booleanToInt(boolean var){
        return var ? 1 : 0;
    }

    public void toggleBluetooth(boolean toggle){
        if (toggle){
            mBluetoothAdapter.enable();
        }else {
            mBluetoothAdapter.disable();
        }
    }

    public boolean isBluetoothEnabled(){
        return mBluetoothAdapter.isEnabled();
    }

    public boolean isMobileDataEnabled(){
        boolean a = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                a = ControlNetworkLollipop.isMobileDataEnabledFromLollipop(context1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            a = ControlNetworkUnder21.isEnabled(context1);
        }
        return a;
    }

    public void toggleMobileData(boolean toggle){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                ControlNetworkLollipop.toggleMobileData(toggle);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            ControlNetworkUnder21.toggleIt(context1,toggle);
        }
    }

    public boolean isWifiEnabled(){
        return wifiManager.isWifiEnabled();
    }

    public void toggleWIFI(boolean toggle){
        wifiManager.setWifiEnabled(toggle);
    }
}
