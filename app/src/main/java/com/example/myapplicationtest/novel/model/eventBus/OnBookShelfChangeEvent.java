package com.example.myapplicationtest.novel.model.eventBus;

import com.example.myapplicationtest.novel.database.tb.TbBookShelf;

/**
 * 添加或移除书架
 */
public class OnBookShelfChangeEvent {

    public TbBookShelf addTbBookShelf;

    public int removeBookId;
}
