package com.example.myapplicationtest.novel.interfaces;

import com.example.myapplicationtest.novel.model.standard.BookMenuItemInfo;

import java.util.List;

/**
 * 下载目录
 */
public interface IDownloadMenuListListener {

    void onDownloadSuccess(List<BookMenuItemInfo> chapters);

    void onDownloadLoadFail(String s);
}
