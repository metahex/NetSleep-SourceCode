package com.emre.netsleep;

/**
 * Created by emre on 30.08.2017.
 */

public class BatteryControlAPI {
    public static void toggleBatterySaver(boolean a){
        TerminalCommand.command("settings put global low_power " + (a ? "1" : "0"));
    }
}