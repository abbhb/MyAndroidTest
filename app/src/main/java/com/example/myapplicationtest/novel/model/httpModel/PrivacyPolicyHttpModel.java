package com.example.myapplicationtest.novel.model.httpModel;

// 隐私政策
public class PrivacyPolicyHttpModel extends InterFaceBaseHttpModel {

    @Override
    public String getUrl() {
        return getInterFaceStart() + "privacy/privacyPolicy";
    }
}