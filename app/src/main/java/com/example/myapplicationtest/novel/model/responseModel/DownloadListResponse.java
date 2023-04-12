package com.example.myapplicationtest.novel.model.responseModel;

import com.renrui.libraries.model.baseObject.BaseResponseModel;
import com.example.myapplicationtest.novel.model.standard.DownloadBookContentItemInfo;

import java.util.List;

/**
 * 下载
 */
public class DownloadListResponse extends BaseResponseModel {

    public List<DownloadBookContentItemInfo> list;
}
