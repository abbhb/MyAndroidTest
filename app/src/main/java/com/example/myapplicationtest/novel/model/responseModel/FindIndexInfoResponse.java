package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.FindItemBookItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现首页
 */
public class FindIndexInfoResponse extends BaseResponseModel {

    public List<FindItemBookItemModel> list = new ArrayList<>();

    public int total;
    public int pageNum;
    public int pageSize;
}
