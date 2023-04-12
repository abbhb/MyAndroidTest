package com.example.myapplicationtest.novel.model.httpModel;

import com.example.myapplicationtest.novel.constant.ConstantInterFace;

/**
 * 搜索
 */
public class SearchBookHttpModel extends InterFaceBaseHttpModel {
    public String keyWord;
    public int pageNum;
    public int pageSize = ConstantInterFace.pageSize;

    @Override
    public String getUrl() {
        return getInterFaceStart() + "book/search";
    }
}