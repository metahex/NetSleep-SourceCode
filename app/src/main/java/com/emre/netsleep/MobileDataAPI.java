package com.emre.netsleep;

import android.content.Context;
import android.os.Build;

/**
 * Created by emre on 30.08.2017.
 */

public class MobileDataAPI {
    Context context1;

    public MobileDataAPI(Context context){
        context1 = context;
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
}
