package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.BookMenuItemInfo;

import java.util.List;

/**
 * 目录
 */
public class MenuListResponse extends BaseResponseModel {

    public int id;
    public String title;
    public String author;
    public String desc;
    public String word;
    public String coverImg;

    public List<BookMenuItemInfo> chapters;
}