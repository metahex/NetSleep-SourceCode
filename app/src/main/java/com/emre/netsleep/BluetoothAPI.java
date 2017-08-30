package com.emre.netsleep;

import android.bluetooth.BluetoothAdapter;

/**
 * Created by emre on 30.08.2017.
 */

public class BluetoothAPI {
    private BluetoothAdapter mBluetoothAdapter;

    public BluetoothAPI(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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
}
