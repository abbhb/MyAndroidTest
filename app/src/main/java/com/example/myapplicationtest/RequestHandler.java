package com.example.myapplicationtest;

import com.example.myapplicationtest.entity.WishVo;

import java.util.List;

public interface RequestHandler {
    void onComplete(List<WishVo> list);
}
