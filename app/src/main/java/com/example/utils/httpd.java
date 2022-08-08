package com.example.utils;

import android.util.Log;

import com.example.myapplicationtest.MainActivityforcharroom;
import com.example.myapplicationtest.login.LoginReturn;
import com.example.myapplicationtest.login.LoginUser;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class httpd {
    public static  String  URLVisit(LoginUser user) {
        Gson gson = new Gson();
        String jsonInString = gson.toJson(user);
        MediaType type = MediaType.parse("application/json;charset=utf-8");
        RequestBody RequestBody2 = RequestBody.create(type,jsonInString);
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    // 指定访问的服务器地址
                    .url("http://121.5.71.186:4000/users/authenticate").post(RequestBody2)
                    .build();
            Response response = client.newCall(request).execute();
            String responseData = response.body().string();
            MainActivityforcharroom.lrt = gson.fromJson(responseData, LoginReturn.class);
            Log.d("tere",response.message());
            return "yes";
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
