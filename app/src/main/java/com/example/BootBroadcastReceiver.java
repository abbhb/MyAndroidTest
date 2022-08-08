package com.example;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.myapplicationtest.server.MyService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG ="WATCHDOG BROAD";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive !!");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED"))
        {
            Intent bootServiceIntent = new Intent(context, MyService.class);
            Log.d(TAG, "onReceive BOOT_COMPLETED!! ");
            context.startService(bootServiceIntent);//startForegroundService
            //context.startForegroundService(bootServiceIntent);//startForegroundService
        }

    }
}
