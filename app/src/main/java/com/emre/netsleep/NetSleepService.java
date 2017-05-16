package com.emre.netsleep;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by emre on 01.05.2017.
 */

public class NetSleepService extends Service {

    ScreenReceiver screenReceiver = new ScreenReceiver();
    IntentFilter screenStateFilter = new IntentFilter();
    private PreferencesManager preferencesManager;

    private void createNotification(Context context, String text) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification noti = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            noti = new Notification.Builder(context)
                    .setContentTitle(context.getString(R.string.app_name))
                    .setContentText(text).setSmallIcon(R.drawable.ic_notify)
                    .setContentIntent(pIntent)
                    .setOngoing(true).build();
        }
        noti.flags |= Notification.FLAG_FOREGROUND_SERVICE;
        notificationManager.notify(12, noti);
    }
    private void closeNotif() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        preferencesManager = new PreferencesManager(this);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenStateFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerCPUResting();
        if (preferencesManager.getPref(StaticVariables.NOTIFY)){
            createNotification(this, getString(R.string.serviceEnabledNotif));
        }
    }

    private void registerCPUResting(){
        registerReceiver(screenReceiver, screenStateFilter);
    }

    private void unregisterCPUResting(){
        unregisterReceiver(screenReceiver);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (preferencesManager.getPref(StaticVariables.NOTIFY)){
            closeNotif();
        }
        unregisterCPUResting();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
