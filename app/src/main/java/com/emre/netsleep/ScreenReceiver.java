package com.emre.netsleep;

/**
 * Created by emre on 01.05.2017.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ScreenReceiver extends BroadcastReceiver {

    private PreferencesManager preferencesManager;
    private NetworkManagement networkManagement;
    private boolean need_open_wifi = false;
    private boolean need_open_bt = false;
    private boolean need_open_mobile_data = false;

    @Override
    public void onReceive(final Context context, Intent intent) {
        preferencesManager = new PreferencesManager(context);
        networkManagement = new NetworkManagement(context);
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {


            if (preferencesManager.getPref(StaticVariables.WIFI)) {
                if (need_open_wifi) {
                    networkManagement.toggleWIFI(true);
                }
            }

            if (preferencesManager.getPref(StaticVariables.BLUETOOTH)) {
                if (need_open_bt) {
                    networkManagement.toggleBluetooth(true);
                }
            }

            if (preferencesManager.getPref(StaticVariables.MOBILE_DATA)) {
                if (need_open_mobile_data) {
                    networkManagement.toggleMobileData(true);
                }
            }

        }
        else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                if (preferencesManager.getPref(StaticVariables.WIFI)) {
                    need_open_wifi = networkManagement.isWifiEnabled();
                    if (networkManagement.isWifiEnabled()) {
                        networkManagement.toggleWIFI(false);
                    }
                }

                if (preferencesManager.getPref(StaticVariables.BLUETOOTH)) {
                    need_open_bt = networkManagement.isBluetoothEnabled();
                    if (networkManagement.isBluetoothEnabled()) {
                        networkManagement.toggleBluetooth(false);
                    }
                }

                if (preferencesManager.getPref(StaticVariables.MOBILE_DATA)) {
                    need_open_mobile_data = networkManagement.isMobileDataEnabled();
                    if (networkManagement.isMobileDataEnabled()) {
                        networkManagement.toggleMobileData(false);
                    }
                }

            }
        }
}
