package com.example.myapplicationtest;

import static android.content.Context.NOTIFICATION_SERVICE;
import static androidx.constraintlayout.motion.utils.Oscillator.TAG;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.format.Time;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


import androidx.core.app.NotificationCompat;

import com.example.myapplicationtest.R;


import com.example.myapplicationtest.server.MyService;
import com.google.gson.Gson;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
/*
* long timeGetTime =new Date().getTime();
                editor.putString("runorstoplasttime", String.valueOf(timeGetTime));
                editor.commit();
* */

public class NewAppWidget extends AppWidgetProvider {
    private static final int UPADTA_WIDGET = 109567;
    private static final int UPADTA_TIP =176449;
    private static JSONObject jsonObject;
    private static SharedPreferences useraaa;
    private static SharedPreferences.Editor editoraaa;
    private static int finishedNumber=0;
    private static final int UPDATA_L=2373874;
    private static final String MyOnClick = "myOnClickTag";
    private static NotificationManager manager;
    private static Notification notification;
    private static Intent intent;
    private static PendingIntent pendingIntent;
    static String ongoingtozh(String string){
        if(string.equals("Ongoing")){
            return "正在探索";
        }
        else if(string.equals("Finished")){
            return "已完成";
        }
        else{
            return "出错了";
        }
    }
    static boolean ongoinstate(String string){
        if(string.equals("Ongoing")){
            return true;
        }
        else if(string.equals("Finished")){
            return false;
        }
        else{
            return false;
        }
    }
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) throws InterruptedException {


        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case UPADTA_WIDGET:
                        // Construct the RemoteViews object
                        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                        views.setTextViewText(R.id.bqtip,"");

                        //        views.setTextViewText(R.id.appwidget_text, widgetText);
                        try {
                            views.setTextViewText(R.id.yuanshenycsz,jsonObject.getString("current_resin")+"/160");
                            views.setTextViewText(R.id.yuanshenmrwt,jsonObject.getString("finished_task_num")+"/"+jsonObject.getString("total_task_num"));

                            views.setTextViewText(R.id.yuanshendtbq,jsonObject.getString("current_home_coin")+"/"+jsonObject.getString("max_home_coin"));
                            views.setTextViewText(R.id.yuanshenzbjb,jsonObject.getString("remain_resin_discount_num")+"/"+jsonObject.getString("resin_discount_num_limit"));

                            com.alibaba.fastjson.JSONArray tableData = com.alibaba.fastjson.JSONArray.parseArray(jsonObject.getString("expeditions"));
                            int i;
                            JSONObject jsonObjectt;
                            finishedNumber=0;
                            for(i=0;i <tableData.toArray().length;i++){
                                jsonObjectt = new JSONObject(tableData.getString(i));
                                if(jsonObjectt.getString("status").equals("Finished")){
                                    finishedNumber+=1;
                                }
                            }

                            views.setTextViewText(R.id.yuanshenmrpq,finishedNumber+"/"+jsonObject.getString("current_expedition_num")+"/"+jsonObject.getString("max_expedition_num"));
                            if(tableData.toArray().length>=1){
                                final Bitmap[] bgmap = new Bitmap[1];
                                jsonObjectt = new JSONObject(tableData.getString(0));
                                // 网络图片请求子线程
                                JSONObject finalJsonObjectt = jsonObjectt;

                                final Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("子线程执行中");
                                            bgmap[0] = getImageBitmap(finalJsonObjectt.getString("avatar_side_icon"));
                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                thread1.join();
                                views.setImageViewBitmap(R.id.yuanshenmrpqlist1image,bgmap[0]);
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    views.setTextColor(R.id.yuanshenmrpqlist1text, Color.parseColor("#48B853"));
                                }else{
                                    views.setTextColor(R.id.yuanshenmrpqlist1text, Color.parseColor("#e72f28"));
                                }
                                views.setTextViewText(R.id.yuanshenmrpqlist1text,ongoingtozh(jsonObjectt.getString("status")));

                            }
                            if(tableData.toArray().length>=2){

                                jsonObjectt = new JSONObject(tableData.getString(1));
                                final Bitmap[] bgmap = new Bitmap[1];
                                // 网络图片请求子线程
                                JSONObject finalJsonObjectt = jsonObjectt;

                                final Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("子线程执行中");
                                            bgmap[0] = getImageBitmap(finalJsonObjectt.getString("avatar_side_icon"));
                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                thread1.join();
                                views.setImageViewBitmap(R.id.yuanshenmrpqlist2image,bgmap[0]);
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    views.setTextColor(R.id.yuanshenmrpqlist2text,Color.parseColor("#48B853"));
                                }else{
                                    views.setTextColor(R.id.yuanshenmrpqlist2text,Color.parseColor("#e72f28"));
                                }
                                views.setTextViewText(R.id.yuanshenmrpqlist2text,ongoingtozh(jsonObjectt.getString("status")));
                            }
                            if(tableData.toArray().length>=3){
                                jsonObjectt = new JSONObject(tableData.getString(2));
                                final Bitmap[] bgmap = new Bitmap[1];
                                // 网络图片请求子线程
                                JSONObject finalJsonObjectt = jsonObjectt;

                                final Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("子线程执行中");
                                            bgmap[0] = getImageBitmap(finalJsonObjectt.getString("avatar_side_icon"));
                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                thread1.join();
                                views.setImageViewBitmap(R.id.yuanshenmrpqlist3image,bgmap[0]);
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    views.setTextColor(R.id.yuanshenmrpqlist3text,Color.parseColor("#48B853"));
                                }else{
                                    views.setTextColor(R.id.yuanshenmrpqlist3text,Color.parseColor("#e72f28"));
                                }

                                views.setTextViewText(R.id.yuanshenmrpqlist3text,ongoingtozh(jsonObjectt.getString("status")));
                            }
                            if(tableData.toArray().length>=4){
                                jsonObjectt = new JSONObject(tableData.getString(3));
                                final Bitmap[] bgmap = new Bitmap[1];
                                // 网络图片请求子线程
                                JSONObject finalJsonObjectt = jsonObjectt;

                                final Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("子线程执行中");
                                            bgmap[0] = getImageBitmap(finalJsonObjectt.getString("avatar_side_icon"));
                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                thread1.join();
                                views.setImageViewBitmap(R.id.yuanshenmrpqlist4image,bgmap[0]);
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    views.setTextColor(R.id.yuanshenmrpqlist4text,Color.parseColor("#48B853"));
                                }else{
                                    views.setTextColor(R.id.yuanshenmrpqlist4text,Color.parseColor("#e72f28"));
                                }
                                views.setTextViewText(R.id.yuanshenmrpqlist4text,ongoingtozh(jsonObjectt.getString("status")));
                            }
                            if(tableData.toArray().length>=5){
                                jsonObjectt = new JSONObject(tableData.getString(4));
                                final Bitmap[] bgmap = new Bitmap[1];
                                // 网络图片请求子线程
                                JSONObject finalJsonObjectt = jsonObjectt;

                                final Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            System.out.println("子线程执行中");
                                            bgmap[0] = getImageBitmap(finalJsonObjectt.getString("avatar_side_icon"));
                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                thread1.join();
                                views.setImageViewBitmap(R.id.yuanshenmrpqlist5image,bgmap[0]);
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    views.setTextColor(R.id.yuanshenmrpqlist5text,Color.parseColor("#48B853"));
                                }else{
                                    views.setTextColor(R.id.yuanshenmrpqlist5text,Color.parseColor("#e72f28"));
                                }
                                views.setTextViewText(R.id.yuanshenmrpqlist5text,ongoingtozh(jsonObjectt.getString("status")));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        // Instruct the widget manager to update the widget
                        appWidgetManager.updateAppWidget(appWidgetId, views);
                        break;
                    case UPADTA_TIP:
                        RemoteViews viewss = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
                        viewss.setTextViewText(R.id.bqtip,"请检查ys-cookie");
                        appWidgetManager.updateAppWidget(appWidgetId, viewss);
                        break;
                    default:
                        break;
                }
            }
        };

        useraaa = context.getSharedPreferences("user",0);
        editoraaa = useraaa.edit();

        String pathaaa = "http://121.5.71.186:9537/a?cookie="+useraaa.getString("cookie","");
        if(!useraaa.getString("cookie","").equals("")){
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
                        connection.setConnectTimeout(3000);
                        connection.setReadTimeout(3000);
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
                            Log.d("Fail", "失败了");
                        }else{
                            Log.d("succuss", "成功了 "+result);
                            editoraaa.putString("cardresultdata",result);
                            editoraaa.commit();
                            String cardresultdata = result;
                            if(result.indexOf("current_expedition_num")!=-1){
                                try {
                                    long timeGetTime =new Date().getTime();
                                    editoraaa.putLong("lastupdatatimewithysbq", timeGetTime);
                                    editoraaa.commit();
                                    jsonObject = new JSONObject(cardresultdata);
                                    Message msg = new Message();
                                    msg.what = UPADTA_WIDGET;
                                    handle.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Message msg = new Message();
                                msg.what = UPADTA_TIP;
                                handle.sendMessage(msg);
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





    }

    static private Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e(TAG, "Error getting bitmap", e);
        }
        return bm;
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        context.startForegroundService(new Intent(context, MyService.class));
//
//        Button button = views.
//        button.setOnClickListener(new OnClickListener(){
//
//            public void onClick(View v) {
//
//                Toast toast = Toast.makeText(getApplicationContext(), “Hello world!”, Toast.LENGTH_LONG);//提示被点击了
//
//                toast.show();
//
//            }
//
//        });

        for (int appWidgetId : appWidgetIds) {

            try {

                updateAppWidget(context, appWidgetManager, appWidgetId);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created


    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}