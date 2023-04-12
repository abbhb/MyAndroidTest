package com.example.myapplicationtest.novel.model.httpModel;

import com.example.myapplicationtest.novel.constant.ConstantInterFace;

/**
 * 预览页换一批 && 预览页热门推荐-查看更多
 */
public class BookGetRecommendHttpModel extends InterFaceBaseHttpModel {

    public int bookId;
    public int pageNum;
    public int pageSize = ConstantInterFace.pageSize;

    @Override
    public String getUrl() {
        return getInterFaceStart() + "book/getRecommend";
    }
}