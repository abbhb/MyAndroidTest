package com.example.myapplicationtest.novel.model.httpModel;

/**
 * 榜单
 */
public class GetRankListHttpModel extends InterFaceBaseHttpModel {

    public String getUrl() {
        return getInterFaceStart() + "rank/getList";
    }
}