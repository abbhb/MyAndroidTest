package com.example.myapplicationtest.novel.model.httpModel;

import com.example.myapplicationtest.novel.constant.ConstantInterFace;

/**
 * 换一批
 */
public class CategoryDiscoveryAllListHttpModel extends InterFaceBaseHttpModel {

    public int categoryId;
    public int pageNum;
    public int pageSize = ConstantInterFace.pageSize;

    public String type;

    @Override
    public String getUrl() {
        return getInterFaceStart() + "category/discoveryAll";
    }
}