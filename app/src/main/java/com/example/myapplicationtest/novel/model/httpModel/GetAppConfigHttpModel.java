package com.example.myapplicationtest.novel.model.httpModel;

/**
 * 获取配置信息
 */
public class GetAppConfigHttpModel extends InterFaceBaseHttpModel {

    @Override
    public String getUrl() {
        return getInterFaceStart() + "system/getAppConfig";
    }
}