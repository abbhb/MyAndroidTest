package com.example.myapplicationtest.novel.model.httpModel;

import com.example.myapplicationtest.novel.constant.ConstantInterFace;

/**
 * 完本
 */
public class GetCategoryEndHttpModel extends InterFaceBaseHttpModel {

    public int pageNum;
    public int pageSize = ConstantInterFace.pageSize;

    @Override
    public String getUrl() {
        return getInterFaceStart() + "category/getCategoryEnd";
    }
}