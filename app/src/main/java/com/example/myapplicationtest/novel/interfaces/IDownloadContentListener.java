package com.example.myapplicationtest.novel.interfaces;

import com.example.myapplicationtest.novel.model.standard.DownloadBookContentItemInfo;

import java.util.List;

/**
 * 下载文章
 */
public interface IDownloadContentListener {

    void onDownloadSuccess(List<DownloadBookContentItemInfo> list);

    void onDownloadLoadFail();
}
