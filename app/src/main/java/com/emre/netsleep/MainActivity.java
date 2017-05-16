package com.emre.netsleep;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private InterstitialAd mInterstitialAd;
    private PreferencesManager preferencesManager;
    private View view;
    private Button disable_all;
    private SwitchCompat wifiSwitch, btSwitch, mobileDataSwitch, showNotificationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        preferencesManager = new PreferencesManager(this);

        view = findViewById(R.id.layout_main);
        wifiSwitch = (SwitchCompat) findViewById(R.id.wifiSwitch);
        showNotificationSwitch = (SwitchCompat) findViewById(R.id.showNotificationSwitch);
        disable_all = (Button) findViewById(R.id.disable_all);
        btSwitch = (SwitchCompat) findViewById(R.id.btSwitch);
        mobileDataSwitch = (SwitchCompat) findViewById(R.id.mobileDataSwitch);

        if (!preferencesManager.isPrefNull(StaticVariables.WIFI)){
            wifiSwitch.setChecked(preferencesManager.getPref(StaticVariables.WIFI));
        }
        if (!preferencesManager.isPrefNull(StaticVariables.BLUETOOTH)) {
            btSwitch.setChecked(preferencesManager.getPref(StaticVariables.BLUETOOTH));
        }
        if (!preferencesManager.isPrefNull(StaticVariables.MOBILE_DATA)) {
            mobileDataSwitch.setChecked(preferencesManager.getPref(StaticVariables.MOBILE_DATA));
        }
        if (!preferencesManager.isPrefNull(StaticVariables.NOTIFY)) {
            showNotificationSwitch.setChecked(preferencesManager.getPref(StaticVariables.NOTIFY));
        }

        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesManager.setPref(StaticVariables.WIFI,b);
                if (b){
                    startNetSleepService();
                }
                if (!mobileDataSwitch.isChecked() && !btSwitch.isChecked() && !b){
                    stopNetSleepService();
                }
            }
        });

        btSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesManager.setPref(StaticVariables.BLUETOOTH,b);
                if (b){
                    startNetSleepService();
                }
                if (!wifiSwitch.isChecked() && !mobileDataSwitch.isChecked() && !b){
                    stopNetSleepService();
                }
            }
        });

        mobileDataSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (deviceHasMobileDataFeature()) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            if (isRootGiven()) {

                                preferencesManager.setPref(StaticVariables.MOBILE_DATA, b);
                                if (b) {
                                    startNetSleepService();
                                }
                                if (!wifiSwitch.isChecked() && !btSwitch.isChecked() && !b) {
                                    stopNetSleepService();
                                }

                            }else {

                                makeSnack(getString(R.string.mobileDataFeatureForLollipop),"OK");
                                mobileDataSwitch.setChecked(false);

                            }

                        }else {
                            preferencesManager.setPref(StaticVariables.MOBILE_DATA, b);
                            if (b) {
                                startNetSleepService();
                            }
                            if (!wifiSwitch.isChecked() && !btSwitch.isChecked() && !b) {
                                stopNetSleepService();
                            }
                        }
                    }else {
                        makeSnack(getString(R.string.thereIsNoMobileDataFeature),"OK");
                        mobileDataSwitch.setChecked(false);
                    }
                }
            });

        showNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                preferencesManager.setPref(StaticVariables.NOTIFY,b);
                if (!preferencesManager.getPref(StaticVariables.WIFI) && !preferencesManager.getPref(StaticVariables.BLUETOOTH) && !preferencesManager.getPref(StaticVariables.MOBILE_DATA)){

                }else {
                    startNetSleepService();
                }
            }
        });

        disable_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileDataSwitch.setChecked(false);
                wifiSwitch.setChecked(false);
                btSwitch.setChecked(false);
                stopNetSleepService();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                rootRequest();
            }
        }).start();

        this.mInterstitialAd = new InterstitialAd(this);
        this.mInterstitialAd.setAdUnitId(StaticVariables.BANNER);
        this.mInterstitialAd.setAdListener(new AdListener() {
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("4b5d467c88b7bd63").addTestDevice("04157df47a383a0c").build());
            }
        });
        showAdWhenLoaded(0);
        mInterstitialAd.loadAd(new AdRequest.Builder().addTestDevice("4b5d467c88b7bd63").addTestDevice("04157df47a383a0c").build());

    }
    private boolean deviceHasMobileDataFeature(){
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
    }

    private void showAdWhenLoaded(int extraDelay) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                if (MainActivity.this.mInterstitialAd.isLoaded()) {
                    MainActivity.this.mInterstitialAd.show();
                } else {
                    MainActivity.this.showAdWhenLoaded(0);
                }
            }
        }, (long) (extraDelay + 400));
    }

    private boolean isRootGiven(){
            Process process = null;
            try {
                process = Runtime.getRuntime().exec(new String[]{"su", "-c", "id"});
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output = in.readLine();
                if (output != null && output.toLowerCase().contains("uid=0"))
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (process != null)
                    process.destroy();
            }
        return false;
    }

    private void startNetSleepService(){
        try {
            Intent i = new Intent(this, NetSleepService.class);
            startService(i);
        }catch (Exception e) {

        }
    }

    private void stopNetSleepService(){
        try {
            Intent i = new Intent(this, NetSleepService.class);
            stopService(i);
        }catch (Exception e) {

        }
    }

    private void rootRequest(){
        TerminalCommand.command("su");
    }

    private void makeSnack(String a,String ok){
        Snackbar.make(view, a, Snackbar.LENGTH_LONG)
                .setAction(ok, null).show();
    }
}
