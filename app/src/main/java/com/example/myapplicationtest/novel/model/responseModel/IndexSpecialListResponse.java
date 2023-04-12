package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.SpecialItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 专题首页
 */
public class IndexSpecialListResponse extends BaseResponseModel {

    public List<SpecialItemModel> specialList = new ArrayList<>();
}
