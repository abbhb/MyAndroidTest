package com.example.myapplicationtest;

import static com.example.utils.SystemUtil.isServiceExisted;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import com.example.myapplicationtest.server.MyService;
import com.example.utils.Log;

public class MyAccessibility extends AccessibilityService {
    protected void onServiceConnected() {
        //服务启动时
        startForegroundService(new Intent(getApplicationContext(), MyService.class));
    }
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
//        boolean isserviceExisted = isServiceExisted(getApplicationContext(),"MyService");
//        Log.d("isserviceExisted", String.valueOf(isserviceExisted));
    }

    @Override
    public void onInterrupt() {

    }
}
