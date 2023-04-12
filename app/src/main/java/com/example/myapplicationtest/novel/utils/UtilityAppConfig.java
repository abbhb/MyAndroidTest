package com.example.myapplicationtest.novel.utils;

import com.renrui.libraries.interfaces.IHttpRequestInterFace;
import com.renrui.libraries.util.UtilitySecurity;
import com.renrui.libraries.util.mHttpClient;
import com.example.myapplicationtest.novel.application.MyApplication;
import com.example.myapplicationtest.novel.constant.ConstantInterFace;
import com.example.myapplicationtest.novel.model.httpModel.GetAppConfigHttpModel;
import com.example.myapplicationtest.novel.model.standard.AppConfigInfo;

/**
 * app配置信息
 * <p>
 * 进入app时需要调用update方法
 */
public class UtilityAppConfig {

    private static AppConfigInfo appConfigInfo = null;

    /**
     * 如果没有数据，优先获取数据库中的缓存  其次获取配置文件中的数据
     */
    public static AppConfigInfo getInstant() {
        if (appConfigInfo == null) {
            appConfigInfo = EditSharedPreferences.getConfigInfo();
            if (appConfigInfo == null || UtilitySecurity.isEmpty(appConfigInfo.categories)) {
                try {
                    final String categoriesMenuJson = UtilityData.readFromAssets(MyApplication.getAppContext(), "data/categories.json");
                    appConfigInfo = mHttpClient.GetGsonInstance().fromJson(categoriesMenuJson, AppConfigInfo.class);
                } catch (Exception ex) {
                    UtilityException.catchException(ex);
                }
            }
        }

        return appConfigInfo;
    }

    /**
     * 更新配置信息
     */
    public static void updateConfigInfo() {
        GetAppConfigHttpModel httpModel = new GetAppConfigHttpModel();
        mHttpClient.Request(MyApplication.getAppContext(), httpModel, new IHttpRequestInterFace() {
            @Override
            public void onStart() {

            }

            @Override
            public void onResponse(String s) {
                if (!UtilityData.CheckResponseString(s, false)) {
                    return;
                }

                try {
                    AppConfigInfo res = mHttpClient.fromDataJson(s, AppConfigInfo.class);

                    if (res != null && !UtilitySecurity.isEmpty(res.categories)) {
                        appConfigInfo = res;
                        EditSharedPreferences.saveConfigInfo(res);

                        // 如果没设置过端口，自动设置第一个端口
                        if (ConstantInterFace.getDomainPort() == ConstantInterFace.getDomainDefaultPort()
                                && !UtilitySecurity.isEmpty(res.ports))
                            ConstantInterFace.setDomainPort(res.ports.get(0));
                    }
                } catch (Exception ex) {
                    UtilityException.catchException(ex);
                }
            }

            @Override
            public void onErrorResponse(String s) {

            }

            @Override
            public void onFinish() {

            }
        });
    }
}
