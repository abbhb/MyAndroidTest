package com.example.utils;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class GoToApi {
    public static void getWechatApi(Context context){
        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setComponent(cmp);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(context, "检查到您手机没有安装微信，请安装后使用该功能", Toast.LENGTH_SHORT).show();
        }
    }
}
