package com.example.utils;

import static android.provider.Settings.System.getString;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationtest.R;
import com.king.app.dialog.AppDialog;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.UpdateConfig;
import com.king.app.updater.callback.UpdateCallback;
import com.king.app.updater.http.OkHttpManager;

import java.io.File;

public class Updater {
    public static String mUrl = "http://121.5.71.186/app-release.apk";
    public static AppUpdater mAppUpdater;
    public static TextView tvProgress;
    public static ProgressBar progressBar;
//    public static void UpdateJ(Context context){
//        AppUpdater appUpdater = new AppUpdater(context)
//                .setUpdateFrom(UpdateFrom.JSON)
//                .setUpdateJSON("http://121.5.71.186/updata.json")
//                .setButtonDoNotShowAgain(null);
//        appUpdater.start();
//    }
    /**
     * 一键下载并监听
     */
    public static void clickBtn2(Context context){

        UpdateConfig config = new UpdateConfig();
        config.setUrl(mUrl);
        mAppUpdater = new AppUpdater(context,config)
                .setHttpManager(OkHttpManager.getInstance())
                .setUpdateCallback(new UpdateCallback() {

                    @Override
                    public void onDownloading(boolean isDownloading) {
                        if(isDownloading){
                            Toast.makeText(context, "已经在下载中,请勿重复下载。", Toast.LENGTH_SHORT).show();
                        }else{
//                            showToast("开始下载…");
                            View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress,null);
                            tvProgress = view.findViewById(R.id.tvProgress);
                            progressBar = view.findViewById(R.id.progressBar);
                            AppDialog.INSTANCE.showDialog(context,view,false);
                        }
                    }

                    @Override
                    public void onStart(String url) {
                        updateProgress(0,100);
                    }

                    @Override
                    public void onProgress(long progress, long total, boolean isChange) {
                        if(isChange){
                            updateProgress(progress,total);
                        }
                    }

                    @Override
                    public void onFinish(File file) {
                        AppDialog.INSTANCE.dismissDialog();
                        Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Exception e) {
                        AppDialog.INSTANCE.dismissDialog();
                        Toast.makeText(context, "下载失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        AppDialog.INSTANCE.dismissDialog();
                        Toast.makeText(context, "取消下载", Toast.LENGTH_SHORT).show();

                    }
                });
        mAppUpdater.start();
    }
    public static void updateProgress(long progress, long total){
        if(tvProgress == null || progressBar == null){
            return;
        }
        if(progress > 0){
            int currProgress = (int)(progress * 1.0f / total * 100.0f);
            tvProgress.setText(currProgress + "%");
            progressBar.setProgress(currProgress);
        }else{
            tvProgress.setText("start");
        }

    }
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("TAG", "当前版本名称：" + localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

}
