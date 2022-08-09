package com.example.utils;
public interface HttpCallBack<T>{
    void onSuccess(T t);
    void onFailure(String message);
}