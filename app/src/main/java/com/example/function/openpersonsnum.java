package com.example.function;

import android.content.AsyncQueryHandler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import com.example.values.*;

import org.json.JSONException;
import org.json.JSONObject;

public class openpersonsnum {
    /**
     *get的方式请求
     *@return 返回null 登录异常
     */
    public static String loginByGet(){
        //get的方式提交就是url拼接的方式
        strings str = new strings();
        String path = str.getPath();
        path = path + "/test";
        try {
            URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            //获得结果码
            int responseCode = connection.getResponseCode();
            if(responseCode ==200){
                //请求成功 获得返回的流
                InputStream is = connection.getInputStream();
                //转换成一个加强型的buffered流
                BufferedReader reader=new BufferedReader(new InputStreamReader(is));
                //把读到的内容赋值给result
                String result = reader.readLine();
                JSONObject json_test = new JSONObject(result);
                //打印json 数据
                Log.e("json", json_test.get("msg").toString());

                return "is.msg";
            }else {
                //请求失败
                return null;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
