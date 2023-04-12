package com.example.myapplicationtest.novel.model.httpModel;

public class GetCategoryChannelHttpModel extends InterFaceBaseHttpModel {

    public String getUrl() {
        return getInterFaceStart() + "category/getCategoryChannel";
    }
}