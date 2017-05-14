package com.emre.netsleep;

import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

/**
 * Created by emre on 01.05.2017.
 */

public class PreferencesManager {

    Context context;

    public PreferencesManager(Context context1){
        context = context1;
    }

    public boolean getPref(String prefName) {
        File file = new File("/data/data/com.emre.netsleep/files/"+prefName);

        boolean abc = false;

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }

            br.close();
        } catch (Exception e) {

        }

        if (text.toString().equals("1")){
            abc = true;
        }

        return abc;
    }

    public boolean isPrefNull(String prefName){
        File file = new File("/data/data/com.emre.netsleep/files/"+prefName);
        return !file.exists();
    }

    public void setPref(String prefName, boolean var){
        try {
            FileOutputStream fOut = context.openFileOutput(prefName, Context.MODE_PRIVATE);

            String prefBool = var ? "1" : "0";

            fOut.write(prefBool.getBytes());
            fOut.flush();
            fOut.close();
        } catch (Exception e) {

        }
    }
}
