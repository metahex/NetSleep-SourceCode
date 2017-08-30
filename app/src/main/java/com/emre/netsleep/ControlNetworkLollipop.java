package com.emre.netsleep;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by emre on 08.04.2016.
 * Rewrote 30.08.2017
 */
public class ControlNetworkLollipop {
    public static boolean isMobileDataEnabledFromLollipop(Context context) {
        boolean state = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            state = Settings.Global.getInt(context.getContentResolver(), "mobile_data", 0) == 1;
        }
        if (state) {
           // Log.d("mobile data", "enabled");
        }
        return state;
    }
    public static void toggleMobileData(boolean a){
        TerminalCommand.command("svc data " + (a ? "enable" : "disable"));
    }
}
