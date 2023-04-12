package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.CategoriesListItem;

import java.util.List;

/**
 * 目录
 */
public class CategoriesListResponse extends BaseResponseModel {

    public int total;
    public int page;
    public int page_size;

    public List<CategoriesListItem> list;
}
