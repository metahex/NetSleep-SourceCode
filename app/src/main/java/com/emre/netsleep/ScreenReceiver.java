package com.emre.netsleep;

/**
 * Created by emre on 01.05.2017.
 */

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

public class ScreenReceiver extends BroadcastReceiver {

    private PreferencesManager preferencesManager;
    private MobileDataAPI mobileDataAPI;
    private WIFIAPI wifiapi;
    private BluetoothAPI bluetoothAPI;
    private boolean need_open_wifi = false;
    private boolean need_open_bt = false;
    private boolean need_open_mobile_data = false;
    private KeyguardManager myKM;
    private A a;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferencesManager = new PreferencesManager(context);
        myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        a = new A();
        if (preferencesManager.getPref(StaticVariables.WIFI)) {
            wifiapi = new WIFIAPI(context);
        }

        if (preferencesManager.getPref(StaticVariables.MOBILE_DATA)) {
            mobileDataAPI = new MobileDataAPI(context);
        }

        if (preferencesManager.getPref(StaticVariables.BLUETOOTH)) {
            bluetoothAPI = new BluetoothAPI();
        }

        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            a.execute();
        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

            a.cancel(true);

            if (preferencesManager.getPref(StaticVariables.WIFI)) {
                need_open_wifi = wifiapi.isWifiEnabled();
                if (wifiapi.isWifiEnabled()) {
                    wifiapi.toggleWIFI(false);
                }
            }

            if (preferencesManager.getPref(StaticVariables.BLUETOOTH)) {
                need_open_bt = bluetoothAPI.isBluetoothEnabled();
                if (bluetoothAPI.isBluetoothEnabled()) {
                    bluetoothAPI.toggleBluetooth(false);
                }
            }

            if (preferencesManager.getPref(StaticVariables.BATTERY_SAVER)) {
                BatteryControlAPI.toggleBatterySaver(true);
            }

            if (preferencesManager.getPref(StaticVariables.MOBILE_DATA)) {
                need_open_mobile_data = mobileDataAPI.isMobileDataEnabled();
                if (mobileDataAPI.isMobileDataEnabled()) {
                    mobileDataAPI.toggleMobileData(false);
                }
            }

        }
    }
    private class A extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            while(true){
                if(!myKM.inKeyguardRestrictedInputMode()) {
                    //Log.d("aaaaaaaaaaaaa","aaaaaaaaaaaaa");
                    if (preferencesManager.getPref(StaticVariables.WIFI)) {
                        if (need_open_wifi) {
                            wifiapi.toggleWIFI(true);
                        }
                    }

                    if (preferencesManager.getPref(StaticVariables.BLUETOOTH)) {
                        if (need_open_bt) {
                            bluetoothAPI.toggleBluetooth(true);
                        }
                    }

                    if (preferencesManager.getPref(StaticVariables.MOBILE_DATA)) {
                        if (need_open_mobile_data) {
                            mobileDataAPI.toggleMobileData(true);
                        }
                    }

                    if (preferencesManager.getPref(StaticVariables.BATTERY_SAVER)) {
                        BatteryControlAPI.toggleBatterySaver(false);
                    }
                    break;
                }
            }
            return null;
        }
    }
}
