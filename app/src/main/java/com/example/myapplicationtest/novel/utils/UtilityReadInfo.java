package com.example.myapplicationtest.novel.utils;

import com.example.myapplicationtest.novel.model.standard.ReadSettingInfo;

public class UtilityReadInfo {

    private static ReadSettingInfo readSettingInfo;

    public static ReadSettingInfo getReadSettingInfo() {
        if (readSettingInfo == null)
            readSettingInfo = EditSharedPreferences.getReadSettingInfo();

        return readSettingInfo;
    }
}
