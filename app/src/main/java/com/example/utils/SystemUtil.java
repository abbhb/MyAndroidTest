package com.example.utils;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class SystemUtil {

    public static DisplayMetrics getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int Dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density; //当前屏幕密度因子
        return (int) (dp * scale + 0.5f);
    }

    public static int Px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static Bitmap getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e(StringUtil.TAG, "Exception", e);
        }
        return versionName;
    }
    /**
     * 返回某个服务是否存在
     */
    public static boolean isServiceExisted(Context context, String className) {
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList =activityManager.getRunningServices(Integer.MAX_VALUE);
        if(!(serviceList.size() > 0)) {
            return false;
        }
        for(int i = 0; i < serviceList.size(); i++) {
            ActivityManager.RunningServiceInfo serviceInfo = serviceList.get(i);
            ComponentName serviceName = serviceInfo.service;
            if(serviceName.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

}
