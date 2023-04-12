package com.example.myapplicationtest.novel.widget.page;

import com.example.myapplicationtest.novel.database.tb.TbBookChapter;

public interface IPagerLoader {

    void onPreChapter(TbBookChapter newBookChapter);

    void onNextChapter(TbBookChapter newBookChapter);

    /**
     * 翻页
     */
    void onTurnPage();

    void showAd();
}
