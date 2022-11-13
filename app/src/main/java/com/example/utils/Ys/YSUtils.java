package com.example.utils.Ys;

import static com.example.utils.Const.UPADTA_TIP;
import static com.example.utils.Const.UPDATA_MSG;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class YSUtils {
    public static void getSSBF(String cookie, Handler handler, SharedPreferences useraaa){
        SharedPreferences.Editor editoraaa = useraaa.edit();
        String pathaaa = "http://121.5.71.186:9537/a?cookie="+cookie;
        if(isCookieValidity(cookie)){
            Log.d("test2022","通过cookie校验");
            if(isTimeValidity(useraaa)&&isDataValidity(useraaa)){
                Log.d("test2022","时间");
                final Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println("子线程执行中");
                            networdRequest(pathaaa);
                            //子线程等待, 后唤醒lockObject锁
                            System.out.println("子线程执行完毕");
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("子线程:"+ e.toString());
                        }
                    }
                    private void networdRequest(String urla){
                        HttpURLConnection connection=null;
                        try {
                            URL url = new URL(urla);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setConnectTimeout(4000);
                            connection.setReadTimeout(4000);
                            //设置请求方式 GET / POST 一定要大小
                            connection.setRequestMethod("GET");
                            connection.setDoInput(true);
                            connection.setDoOutput(false);
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            if (responseCode != HttpURLConnection.HTTP_OK) {
                                throw new IOException("HTTP error code" + responseCode);
                            }
                            String result = getStringByStream(connection.getInputStream());
                            if (result == null) {
                                android.util.Log.d("Fail", "失败了");
                            }else{
                                Log.d("succuss", "成功了 "+result);
                                editoraaa.putString("cardresultdata",result);
                                editoraaa.commit();
                                String cardresultdata = result;
                                if(result.indexOf("current_expedition_num")!=-1){
                                    try {
                                        long dqtime = new Date().getTime();
                                        editoraaa.putLong("lastupdatatimewithysbq",dqtime);
                                        editoraaa.commit();
                                        Message msg = new Message();
                                        msg.obj = new JSONObject(cardresultdata);
                                        msg.what = UPDATA_MSG;
                                        handler.sendMessage(msg);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }else {
                                    Message msg = new Message();
                                    msg.what = UPADTA_TIP;
                                    handler.sendMessage(msg);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    private String getStringByStream(InputStream inputStream){
                        Reader reader;
                        try {
                            reader=new InputStreamReader(inputStream,"UTF-8");
                            char[] rawBuffer=new char[512];
                            StringBuffer buffer=new StringBuffer();
                            int length;
                            while ((length=reader.read(rawBuffer))!=-1){
                                buffer.append(rawBuffer,0,length);
                            }
                            return buffer.toString();
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
                thread1.start();    // 线程启动
            }
            else{
                String carddata = useraaa.getString("cardresultdata","");
                try {

                    Message msg = new Message();
                    msg.what = UPDATA_MSG;
                    msg.obj = new JSONObject(carddata);
                    handler.sendMessage(msg);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Message msg = new Message();
                    msg.what = UPADTA_TIP;
                    handler.sendMessage(msg);
                }

            }
        }
        else{
            Message msg = new Message();
            msg.what = UPADTA_TIP;
            handler.sendMessage(msg);
        }
    }
    public static boolean isCookieValidity(String cookie){
        if (cookie.equals("")){
            return false;
        }
        else{
            return true;
        }
    }
    public static boolean isTimeValidity(SharedPreferences useraaa){
        long time = new Date().getTime();
        long lasttime = useraaa.getLong("lastupdatatimewithysbq",0);
        long shentgyu = time-lasttime;
        if (shentgyu>300000){
            return true;
        }
        else{
            return false;
        }
    }
    public static boolean isDataValidity(SharedPreferences useraaa){
        if (useraaa.getString("cardresultdata","").equals("")){
            return true;
        }
        else{
            return false;
        }
    }

}
