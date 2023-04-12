package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.CategoryChannelItemInfo;

import java.util.ArrayList;

/**
 * 分类
 */
public class CategoryChannelListResponse extends BaseResponseModel {

    public ArrayList<CategoryChannelItemInfo> channels = new ArrayList<>();
}
