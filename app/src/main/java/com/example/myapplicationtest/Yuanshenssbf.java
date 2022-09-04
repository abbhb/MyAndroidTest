package com.example.myapplicationtest;

import static androidx.constraintlayout.motion.utils.Oscillator.TAG;
import static com.example.utils.Const.UPADTA_TIP;
import static com.example.utils.Const.UPDATA_MSG;
import static com.example.utils.YSUtils.getSSBF;
import static com.example.values.strings.ysLoginUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationtest.widget.NewAppWidget;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

public class Yuanshenssbf extends AppCompatActivity {

    private static final int UPADTA_1 = 18111;
    private static final int UPADTA_2 = 18112;
    private static final int UPADTA_3 = 18113;
    private static final int UPADTA_4 = 18114;
    private static final int UPADTA_5 = 18115;
    private SharedPreferences useraaa;
    private String cookie;
    private SharedPreferences.Editor editoraaa;
    private JSONObject jsonObject;
    private TextView textycszmsg;
    private TextView textmrwtmsg;
    private TextView textdtbqmsg;
    private TextView textzbjbmsg;
    private TextView textclzbmsg;
    private TextView texttspqmsg;
    private TextView textycsznum;
    private TextView textmrwtnum;
    private TextView textdtbqnum;
    private TextView textzbjbnum;
    private TextView textclzbnum;
    private TextView texttspqnum;
    private ImageView image1;
    private ImageView image2;
    private ImageView image3;
    private ImageView image4;
    private ImageView image5;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private Handler handle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuanshenssbf);
        CookieManager cookieManager = CookieManager.getInstance();
        cookie = cookieManager.getCookie(ysLoginUrl);
        useraaa = getSharedPreferences("user",0);
        editoraaa = useraaa.edit();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getResources().getColor(R.color.wltsztlcolor));
//        WebView webView = (WebView) findViewById(R.id.yuanshenssbf);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);//启用js
//        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决视频无法播放问题
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://121.5.71.186/1.html");//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(request.getUrl().toString());
//                return true;
//            }
//        });
        textycszmsg = findViewById(R.id.yuanshenycszmsg);
        textmrwtmsg = findViewById(R.id.yuanshenmrwtmsg);
        textdtbqmsg = findViewById(R.id.yuanshendtbqmsg);
        textzbjbmsg = findViewById(R.id.yuanshenzbjbmsg);
        textclzbmsg = findViewById(R.id.yuanshenclzbmsg);
        texttspqmsg = findViewById(R.id.yuanshentspqmsg);
        textycsznum = findViewById(R.id.yuanshenycsznum);
        textmrwtnum = findViewById(R.id.yuanshenmrwtnum);
        textdtbqnum = findViewById(R.id.yuanshendtbqnum);
        textzbjbnum = findViewById(R.id.yuanshenzbjbnum);
        textclzbnum = findViewById(R.id.yuanshenclzbnum);
        texttspqnum = findViewById(R.id.yuanshentspqnum);
        image1 = findViewById(R.id.yuanshenmrpqlist1imagebq);
        image2 = findViewById(R.id.yuanshenmrpqlist2imagebq);
        image3 = findViewById(R.id.yuanshenmrpqlist3imagebq);
        image4 = findViewById(R.id.yuanshenmrpqlist4imagebq);
        image5 = findViewById(R.id.yuanshenmrpqlist5imagebq);
        text1 = findViewById(R.id.yuanshenmrpqlist1textbq);
        text2 = findViewById(R.id.yuanshenmrpqlist2textbq);
        text3 = findViewById(R.id.yuanshenmrpqlist3textbq);
        text4 = findViewById(R.id.yuanshenmrpqlist4textbq);
        text5 = findViewById(R.id.yuanshenmrpqlist5textbq);
        handle = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case UPDATA_MSG:

                        if(msg.obj==null){
                            Toast.makeText(Yuanshenssbf.this, "空指针,请重试", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        jsonObject = (JSONObject) msg.obj;
                        try {
                            textycsznum.setText(jsonObject.getString("current_resin")+"/160");
                            textmrwtnum.setText(jsonObject.getString("finished_task_num")+"/"+jsonObject.getString("total_task_num"));
                            textdtbqnum.setText(jsonObject.getString("current_home_coin")+"/"+jsonObject.getString("max_home_coin"));
                            textzbjbnum.setText(jsonObject.getString("remain_resin_discount_num")+"/"+jsonObject.getString("resin_discount_num_limit"));
//                            textclzbnum.setText(jsonObject.getString(transformerStatus(jsonObject)));

                            textclzbnum.setText(transformerStatus(jsonObject));
                            textycszmsg.setText(resinTime(jsonObject));//原粹树脂消息
                            textmrwtmsg.setText(taskStatus(jsonObject));//每日委托消息
                            textdtbqmsg.setText(coinTime(jsonObject));//洞天宝钱消息
                            textzbjbmsg.setText(bossStatus(jsonObject));//周本减半消息
                            textclzbmsg.setText(transformerTime(jsonObject));//参量质变消息
                            texttspqmsg.setText(expeditionTime(jsonObject));//探索派遣
                            texttspqmsg.setText("已经完成了"+expeditionFinishedNumber(jsonObject)+"个");
                            com.alibaba.fastjson.JSONArray tableData = com.alibaba.fastjson.JSONArray.parseArray(jsonObject.getString("expeditions"));
                            int i;
                            JSONObject jsonObjectt;
                            int finishedNumber=0;
                            for(i=0;i <tableData.toArray().length;i++){
                                jsonObjectt = new JSONObject(tableData.getString(i));
                                if(jsonObjectt.getString("status").equals("Finished")){
                                    finishedNumber+=1;
                                }
                            }
                            texttspqnum.setText(finishedNumber+"/"+jsonObject.getString("current_expedition_num")+"/"+jsonObject.getString("max_expedition_num"));
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
                                            Message msg = new Message();
                                            msg.what = UPADTA_1;
                                            msg.obj = bgmap[0];
                                            handle.sendMessage(msg);

                                            //子线程等待, 后唤醒lockObject锁
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动


                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    text1.setTextColor(Color.parseColor("#48B853"));
                                }else{
                                    text1.setTextColor(Color.parseColor("#e72f28"));
                                }
                                text1.setText(ongoingtozh(jsonObjectt.getString("status")));

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
                                            Message msg = new Message();
                                            msg.what = UPADTA_2;
                                            msg.obj = bgmap[0];
                                            handle.sendMessage(msg);
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动
                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    text2.setTextColor(Color.parseColor("#48B853"));
                                }else{
                                    text2.setTextColor(Color.parseColor("#e72f28"));
                                }
                                text2.setText(ongoingtozh(jsonObjectt.getString("status")));
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
                                            Message msg = new Message();
                                            msg.what = UPADTA_3;
                                            msg.obj = bgmap[0];
                                            handle.sendMessage(msg);
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动

                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    text3.setTextColor(Color.parseColor("#48B853"));
                                }else{
                                    text3.setTextColor(Color.parseColor("#e72f28"));
                                }
                                text3.setText(ongoingtozh(jsonObjectt.getString("status")));
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
                                            Message msg = new Message();
                                            msg.what = UPADTA_4;
                                            msg.obj = bgmap[0];
                                            handle.sendMessage(msg);
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动

                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    text4.setTextColor(Color.parseColor("#48B853"));
                                }else{
                                    text4.setTextColor(Color.parseColor("#e72f28"));
                                }
                                text4.setText(ongoingtozh(jsonObjectt.getString("status")));
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
                                            Message msg = new Message();
                                            msg.what = UPADTA_5;
                                            msg.obj = bgmap[0];
                                            handle.sendMessage(msg);
                                            System.out.println("子线程执行完毕");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            System.out.println("子线程:"+ e.toString());
                                        }
                                    }
                                });

                                thread1.start();    // 线程启动

                                if (ongoinstate(jsonObjectt.getString("status"))){
                                    text5.setTextColor(Color.parseColor("#48B853"));
                                }else{
                                    text5.setTextColor(Color.parseColor("#e72f28"));
                                }
                                text5.setText(ongoingtozh(jsonObjectt.getString("status")));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case UPADTA_TIP:
                        Toast.makeText(Yuanshenssbf.this, "请检查ys-cookie", Toast.LENGTH_SHORT).show();
                        break;
                    case UPADTA_1:
                        image1.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case UPADTA_2:
                        image2.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case UPADTA_3:
                        image3.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case UPADTA_4:
                        image4.setImageBitmap((Bitmap) msg.obj);
                        break;
                    case UPADTA_5:
                        image5.setImageBitmap((Bitmap) msg.obj);
                        break;
                    default:
                        break;
                }
            }
        };

        getSSBF(cookie,handle,useraaa);
        updateWidget();


    }

    private String transformerTime(JSONObject genshinData) {
        try {
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("transformer"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("recovery_time"));
            int day = jsonObject2.getInt("Day");
            if(day>0){
                return "还需"+day+"天可使用";
            }else if(jsonObject2.getInt("Hour")>0){
                return "还需"+jsonObject2.getInt("Hour")+"小时可再次使用";
            }else if(jsonObject2.getInt("Minute")>0){
                return "还需"+jsonObject2.getInt("Minute")+"分钟可再次使用";
            }else{
                return "还需"+jsonObject2.getInt("Second")+"秒可再次使用";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "出错咯";
        }


    }
    private long expeditionMinTime(JSONObject genshinData) {
        int finishedNumber = 0;
        long maxTime = 0;
        long minTime = 0;
        try {
            for (int i = 0; i < genshinData.getJSONArray("expeditions").length(); i++) {
                JSONObject expedition = genshinData.getJSONArray("expeditions").getJSONObject(i);
                if (i == 0) {
                    minTime = expedition.getLong("remained_time");
                }
                if (expedition.getString("status") == "Finished") {
                    finishedNumber += 1;
                } else {
                    maxTime = Math.max(maxTime, (expedition.getLong("remained_time")));
                    minTime = Math.min(minTime, (expedition.getLong("remained_time")));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return minTime;
    }
    private long expeditionMaxTime(JSONObject genshinData) {
        int finishedNumber = 0;
        long maxTime = 0;
        long minTime = 0;
        try {
            for (int i = 0; i < genshinData.getJSONArray("expeditions").length(); i++) {
                JSONObject expedition = genshinData.getJSONArray("expeditions").getJSONObject(i);
                if (expedition.getString("status") == "Finished") {
                    finishedNumber += 1;
                } else {
                    maxTime = Math.max(maxTime, (expedition.getLong("remained_time")));
                    minTime = Math.min(minTime, (expedition.getLong("remained_time")));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return minTime;
    }
    private String expeditionTime(JSONObject genshinData) {
        long minSecond = expeditionMinTime(genshinData);
        long maxSecond = expeditionMaxTime(genshinData);
        ArrayList minTime = formatExpRemainTime(minSecond);
        ArrayList maxTime = formatExpRemainTime(maxSecond);

        if (maxSecond == 0) {
            return "全部完成";
        } else if (minSecond == 0) {
            return maxTime.get(0)+":"+maxTime.get(1);
        } else {
            return minTime.get(0)+":"+minTime.get(1);
        }
    }
    private String bossStatus(JSONObject jsonObject) {
        try {
            return jsonObject.getInt("remain_resin_discount_num")!= 0 ?"待讨伐" : "已讨伐";
        } catch (JSONException e) {
            e.printStackTrace();
            return "出错了";
        }
    }
    private String coinTime(JSONObject jsonObject) {
        ArrayList remainTime = null;
        try {
            remainTime = formatExpRemainTime(jsonObject.getLong("home_coin_recovery_time"));
            return jsonObject.getInt("current_home_coin")!= jsonObject.getInt("max_home_coin") ? "还需"+remainTime.get(0)+":"+remainTime.get(1)+"满" : "MAX";
        } catch (JSONException e) {
            e.printStackTrace();
            return "出错了";
        }

    }

    private String taskStatus(JSONObject genshinData) {
        try {
            return genshinData.getInt("finished_task_num")!= genshinData.getInt("total_task_num") ? "未完成" : "已完成";
        } catch (JSONException e) {
            e.printStackTrace();
            return "出错了";
        }
    }

    private String resinTime(JSONObject jsonObject) {
        boolean nearFull = false;
        try {
            nearFull = (jsonObject.getInt("current_resin")>=jsonObject.getInt("max_resin")* 0.9);
            ArrayList list = formatExpRemainTime(jsonObject.getLong("resin_recovery_time"));

            return !nearFull ? "还需"+list.get(0).toString()+":"+list.get(1).toString()+"分钟恢复" : "MAX";
        } catch (JSONException e) {
            e.printStackTrace();
            return "错误";
        }
    }

    private boolean expeditionStatusAlert(JSONObject genshinData) {
        if (expeditionFinishedNumber(genshinData) != 0) {
            return true;
        } else {
            try {
                if (genshinData.getInt("current_expedition_num")!= genshinData.getInt("max_expedition_num")) {
                    return true;
                } else {
                    return false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    private int expeditionFinishedNumber(JSONObject jsonObject){
        com.alibaba.fastjson.JSONArray tableData = null;
        try {
            tableData = com.alibaba.fastjson.JSONArray.parseArray(jsonObject.getString("expeditions"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int i;
        JSONObject jsonObjectt;
        int finishedNumber=0;
        for(i=0;i <tableData.toArray().length;i++){
            try {
                jsonObjectt = new JSONObject(tableData.getString(i));
                if(jsonObjectt.getString("status").equals("Finished")){
                    finishedNumber+=1;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return finishedNumber;
    }
    private ArrayList formatExpRemainTime(long timeRemain) {
        long processTimeTmp = timeRemain / 60;
        long hour = processTimeTmp / 60;
        long minute = processTimeTmp % 60;
        long second = timeRemain % 60;
        String hours = String.valueOf(hour);
        String minutes = String.valueOf(minute);
        String seconds = String.valueOf(second);
        ArrayList list = new ArrayList();
        list.add(StringUtils.leftPad(hours, 2, "0"));
        list.add(StringUtils.leftPad(minutes, 2, "0"));
        list.add(StringUtils.leftPad(seconds, 2, "0"));
        return list;
    }

    private String transformerStatus(JSONObject jsonObject){
        try {
            JSONObject jsonObject1temp = new JSONObject(jsonObject.getString("transformer"));
            JSONObject jsonObject2temp = new JSONObject(jsonObject1temp.getString("recovery_time"));
            int day = jsonObject2temp.getInt("Day");
            if(day>0){
                return "冷却中";
            }else if(jsonObject2temp.getBoolean("reached")){
                return "冷却完成";
            }else{
                return "即将完成";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "出错了";
        }

    }
    private boolean ongoinstate(String string){
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
    public String ongoingtozh(String string){
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
    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, NewAppWidget.class));
        if (appWidgetIds.length > 0) {
            new NewAppWidget().onUpdate(this, appWidgetManager, appWidgetIds);
        }
    }
    private Bitmap getImageBitmap(String url) {
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

}