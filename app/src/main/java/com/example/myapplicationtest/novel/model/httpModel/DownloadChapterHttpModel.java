package com.example.myapplicationtest.novel.model.httpModel;

/**
 * 下载
 */
public class DownloadChapterHttpModel extends InterFaceBaseHttpModel {

    @Override
    public String getUrl() {
        return getInterFaceStart() + "chapter/get";
    }
}