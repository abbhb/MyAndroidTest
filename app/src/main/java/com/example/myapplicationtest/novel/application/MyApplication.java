package com.example.myapplicationtest.novel.application;

import android.app.Application;
import android.content.Context;

import com.renrui.libraries.util.LibrariesCons;
import com.renrui.libraries.util.UtilitySecurity;
import com.renrui.libraries.util.mHttpClient;
import com.example.myapplicationtest.novel.constant.ConstantInterFace;
import com.example.myapplicationtest.novel.utils.EditSharedPreferences;
import com.example.myapplicationtest.novel.utils.UtilityException;

import pl.com.salsoft.sqlitestudioremote.SQLiteStudioService;

public class MyApplication extends Application {

    private static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;

        SQLiteStudioService.instance().start(this);

        LibrariesCons.setContext(mContext);

        // temp
//        Logger.isDebug = true;

        initHttpInfo();
    }

    public static void initHttpInfo() {
        try {
            // 域名端口
            int port = EditSharedPreferences.readIntFromConfig(EditSharedPreferences.INT_INTERFACEPORT, ConstantInterFace.getDomainPort());
            ConstantInterFace.setDomainPort(port);

            mHttpClient.reInitAsyn();

            String proxyIp = EditSharedPreferences.readStringFromConfig(EditSharedPreferences.STRING_STRING_PROXYIP);

            // temp
//            proxyIp = "172.18.5.166";
            if (!UtilitySecurity.isEmpty(proxyIp))
                mHttpClient.getAsyncHttpClient().setProxy(proxyIp, 12345);
        } catch (Exception ex) {
            UtilityException.catchException(ex);
        }
    }
}