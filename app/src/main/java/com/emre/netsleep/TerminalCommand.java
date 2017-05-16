package com.emre.netsleep;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Created by emre on 22.04.2016.
 */
public class TerminalCommand {
    private static void RunAsRoot(String[] cmds) {
		try {
			
        DataOutputStream os = new DataOutputStream(Runtime.getRuntime().exec("su").getOutputStream());
        for (String tmpCmd : cmds) {
            os.writeBytes(tmpCmd + "\n");
        }
        os.writeBytes("exit\n");
        os.flush();
		
		}
		catch (Exception e){
			
		}
    }
    public static void command(String command) {
        try {
			
            RunAsRoot(new String[]{command});

        } catch (Exception e) {
           
        }
    }
}
