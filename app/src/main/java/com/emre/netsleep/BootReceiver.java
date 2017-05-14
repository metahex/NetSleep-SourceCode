package com.emre.netsleep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by emre on 13.05.2017.
 */

public class BootReceiver extends BroadcastReceiver {

    private PreferencesManager preferencesManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferencesManager = new PreferencesManager(context);
        if (!preferencesManager.getPref(StaticVariables.WIFI) && !preferencesManager.getPref(StaticVariables.BLUETOOTH) && !preferencesManager.getPref(StaticVariables.MOBILE_DATA)){

        }else {
            startNetSleepService(context);
        }
    }
    private void startNetSleepService(Context context){
        try {
            Intent i = new Intent(context, NetSleepService.class);
            context.startService(i);
        }catch (Exception e) {

        }
    }
}