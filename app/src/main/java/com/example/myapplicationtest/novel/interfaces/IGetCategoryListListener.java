package com.example.myapplicationtest.novel.interfaces;

import com.example.myapplicationtest.novel.model.standard.CategoriesListItem;

import java.util.List;

/**
 * 获取目录下的图书列表
 */
public interface IGetCategoryListListener {

    void onGetCategoryListSuccess(List<CategoriesListItem> list);

    void onGetCategoryListLoadFail();
}
