package com.example.myapplicationtest.novel.model.standard;

import java.util.ArrayList;
import java.util.List;

public class DownloadChapterRequestInfo {
    public int bookId;
    public List<Long> chapterIdList = new ArrayList<>();
}