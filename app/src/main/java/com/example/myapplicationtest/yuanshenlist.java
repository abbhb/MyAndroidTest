package com.example.myapplicationtest;

import static com.example.values.strings.ysLoginUrl;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.View.Uid;
import com.example.utils.Gsonforcookieis;
import com.example.utils.HttpUtils;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class yuanshenlist extends AppCompatActivity {
    private String cookie;
    private static final int PANDUANISGUOQI = 563362;
    private TextView textView;
    private Uid uidclass;
    private String uid;
    private Button uidxiugai;
    private Button cookieupdate;
    private Button zdqdbutton;
    private Button ssbfbutton;
    private Button ckfxbutton;
    private Button yuanshenuserinfobutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuanshenlist);
        CookieManager cookieManage = CookieManager.getInstance();
        Intent intent = getIntent();
        String cookies = intent.getStringExtra("cookie");
        cookie = cookieManage.getCookie(ysLoginUrl);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        textView = (TextView) findViewById(R.id.uid);
        uidclass = new Uid(this,R.style.Theme_MyApplicationTest,onClickListener);
        SharedPreferences user = getSharedPreferences("user", 0);
        String uidget = user.getString("uid","");
        if(uidget.equals("")||uidget==null||uidget.equals("")){
            //弹出uid输入框
            uidclass.show();
        }
        else{
            textView.setText("uid:"+uidget);
        }
        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case PANDUANISGUOQI:
                        Toast.makeText(yuanshenlist.this, "ys-cookie已过期", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(yuanshenlist.this,MainActivityplusysfzindex.class);
                        startActivity(intent1);
                        finish();
                        break;
                    default:
                        Toast.makeText(yuanshenlist.this, "未知", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        uidxiugai = (Button) findViewById(R.id.uidxiugai);
        cookieupdate = (Button)findViewById(R.id.cookiehandupdate);
        zdqdbutton = (Button)findViewById(R.id.yuanshenzdqdbutton);
        ssbfbutton = (Button)findViewById(R.id.yuanshenssbfbutton);
        ckfxbutton = (Button)findViewById(R.id.yuanshenckfxbutton);
        yuanshenuserinfobutton = (Button)findViewById(R.id.yuanshenuserinfobutton);
        yuanshenuserinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(yuanshenlist.this,YuanShenUserInfo.class);
                startActivity(intent2);
            }
        });
        ckfxbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(yuanshenlist.this,MainActivityForCKFX.class);
                startActivity(intent1);
            }
        });
        ssbfbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(yuanshenlist.this,Yuanshenssbf.class);
                startActivity(intent2);
            }
        });
        zdqdbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("clicklist","点击了");
//                if(user.getString("config","false").equals("false")){
//                    autoqdbyuidandcookieclass.headtip.setText(R.string.stop);
//                    autoqdbyuidandcookieclass.synchronous.setVisibility(View.GORE);
//                    autoqdbyuidandcookieclass.headtip.setTextColor(R.color.red);
//                }
//                else{
//                    autoqdbyuidandcookieclass.headtip.setText(R.string.run);
//                    autoqdbyuidandcookieclass.headtip.setTextColor(R.color.gre);
//                    autoqdbyuidandcookieclass.synchronous.setVisibility(View.VISIBLE);
//                }


                Intent intent1 = new Intent(yuanshenlist.this,MainActivityYuanShenHuoQuChouKaLink.class);
                startActivity(intent1);
                /**
                 * 自动签到失效了，停止
                 * 改成获取抽卡地址的了
                 * */
//                Toast.makeText(yuanshenlist.this, "此功能暂停使用，失效", Toast.LENGTH_SHORT).show();

            };
        });
        uidxiugai.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("clicklist","点击了");
                uidclass.show();
            };
        });
        cookieupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("clicklist","点击了");
                Intent intent2 = new Intent(yuanshenlist.this,MainActivityplusysfzindex.class);
                startActivity(intent2);
                finish();
            }
        });

        if(cookie.equals("")){
            Intent intent1 = new Intent(this,MainActivityplusysfzindex.class);
            startActivity(intent1);
            finish();
        }
//        else{
//            //准备网络请求
//            String urlStr = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";
//            new Thread(){
//                @Override
//                public void run() {
//                    networdRequest(urlStr);
//                }
//                private void networdRequest(String urla){
//                    HttpURLConnection connection=null;
//                    try {
//                        URL url = new URL(urla);
//                        connection = (HttpURLConnection) url.openConnection();
//                        connection.setConnectTimeout(3000);
//                        connection.setReadTimeout(3000);
//                        //设置请求方式 GET / POST 一定要大小
//                        connection.setRequestMethod("GET");
//                        connection.setRequestProperty("Cookie",cookie);
//                        connection.setDoInput(true);
//                        connection.setDoOutput(false);
//                        connection.connect();
//                        int responseCode = connection.getResponseCode();
//                        if (responseCode != HttpURLConnection.HTTP_OK) {
//                            throw new IOException("HTTP error code" + responseCode);
//                        }
//                        String result = getStringByStream(connection.getInputStream());
//                        if (result == null) {
//                            Log.d("Fail", "失败了");
//                        }else{
//
//                            JSONObject jsonObject = new JSONObject(result);
//                            Log.d("succuss", "成功了 "+jsonObject.getInt("retcode"));
//                            if (jsonObject.getInt("retcode")==0){
//                            }else{
//                                Message msg = new Message();
//                                msg.what = PANDUANISGUOQI;
//                                handle.sendMessage(msg);
//
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                private String getStringByStream(InputStream inputStream){
//                    Reader reader;
//                    try {
//                        reader=new InputStreamReader(inputStream,"UTF-8");
//                        char[] rawBuffer=new char[512];
//                        StringBuffer buffer=new StringBuffer();
//                        int length;
//                        while ((length=reader.read(rawBuffer))!=-1){
//                            buffer.append(rawBuffer,0,length);
//                        }
//                        return buffer.toString();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            }.start();
//
//
//
//        }
        /**
         * 弃用，自动签到相关
         */
//        if(!user.getString("uid","").equals("")){
//            String patha = "http://121.5.71.186:9527/a?uid="+user.getString("uid","123")+"&way=uidget"+"&cookie="+user.getString("cookie","");
//            SharedPreferences useradda = getSharedPreferences("user", 0);
//            SharedPreferences.Editor editor666 = useradda.edit();
//            new Thread(){
//                @Override
//                public void run() {
//                    networdRequest(patha);
//                }
//                private void networdRequest(String urla){
//                    HttpURLConnection connection=null;
//                    try {
//                        URL url = new URL(urla);
//                        connection = (HttpURLConnection) url.openConnection();
//                        connection.setConnectTimeout(3000);
//                        connection.setReadTimeout(3000);
//                        //设置请求方式 GET / POST 一定要大小
//                        connection.setRequestMethod("GET");
//
//                        connection.setDoInput(true);
//                        connection.setDoOutput(false);
//                        connection.connect();
//                        int responseCode = connection.getResponseCode();
//                        if (responseCode != HttpURLConnection.HTTP_OK) {
//                            throw new IOException("HTTP error code" + responseCode);
//                        }
//                        String result = getStringByStream(connection.getInputStream());
//                        if (result == null) {
//                            Log.d("Fail", "失败了");
//                        }else{
//                            Log.d("succuss", "成功了 "+result);
//                            if(result.equals("cunzai")){
//                                editor666.putString("config","true");
//                                editor666.commit();
//                            }
//                            else{
//                                editor666.putString("config","false");
//                                editor666.commit();
//                            }
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//                private String getStringByStream(InputStream inputStream){
//                    Reader reader;
//                    try {
//                        reader=new InputStreamReader(inputStream,"UTF-8");
//                        char[] rawBuffer=new char[512];
//                        StringBuffer buffer=new StringBuffer();
//                        int length;
//                        while ((length=reader.read(rawBuffer))!=-1){
//                            buffer.append(rawBuffer,0,length);
//                        }
//                        return buffer.toString();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            }.start();
//        }
//        else{
//            SharedPreferences useradda = getSharedPreferences("user", 0);
//            SharedPreferences.Editor editor666 = useradda.edit();
//            editor666.putString("config","false");
//            editor666.commit();
//        }


    }

    private void initState() {
        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            //
            LinearLayout linear_bar = (LinearLayout) findViewById(R.id.ll_bar);
            linear_bar.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = getStatusBarHeight();
            //动态的设置隐藏布局的高度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) linear_bar.getLayoutParams();
            params.height = statusHeight;
            linear_bar.setLayoutParams(params);
        }
    }

    private int getStatusBarHeight() {
        try {
            Class c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @SuppressLint("ResourceAsColor")
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.btn_save_pop:

                    String uid1q = uidclass.text_uid.getText().toString().trim();
                    System.out.println(uid1q);
                    uid = uid1q;
                    SharedPreferences user = getSharedPreferences("user", 0);
                    SharedPreferences.Editor editor = user.edit();
                    editor.putString("uid",uid1q);
                    editor.commit();
                    uidclass.dismiss();
                    textView.setText("uid:"+uid1q);
                    /**
                     * 弃用，自动签到相关
                     */
//                    if(!user.getString("uid","").equals("")){
//                        String patha = "http://121.5.71.186:9527/a?uid="+user.getString("uid","")+"&way=uidget"+"&cookie="+user.getString("cookie","");
//
//                        new Thread(){
//                            @Override
//                            public void run() {
//                                networdRequest(patha);
//                            }
//                            private void networdRequest(String urla){
//                                HttpURLConnection connection=null;
//                                try {
//                                    URL url = new URL(urla);
//                                    connection = (HttpURLConnection) url.openConnection();
//                                    connection.setConnectTimeout(3000);
//                                    connection.setReadTimeout(3000);
//                                    //设置请求方式 GET / POST 一定要大小
//                                    connection.setRequestMethod("GET");
//                                    connection.setDoInput(true);
//                                    connection.setDoOutput(false);
//                                    connection.connect();
//                                    int responseCode = connection.getResponseCode();
//                                    if (responseCode != HttpURLConnection.HTTP_OK) {
//                                        throw new IOException("HTTP error code" + responseCode);
//                                    }
//                                    String result = getStringByStream(connection.getInputStream());
//                                    if (result == null) {
//                                        Log.d("Fail", "失败了");
//                                    }else{
//                                        Log.d("succuss", "成功了 "+result);
//                                        if(result.equals("cunzai")){
//                                            editor.putString("config","true");
//                                            editor.commit();
//                                        }
//                                        else{
//                                            editor.putString("config","false");
//                                            editor.commit();
//                                        }
//                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            private String getStringByStream(InputStream inputStream){
//                                Reader reader;
//                                try {
//                                    reader=new InputStreamReader(inputStream,"UTF-8");
//                                    char[] rawBuffer=new char[512];
//                                    StringBuffer buffer=new StringBuffer();
//                                    int length;
//                                    while ((length=reader.read(rawBuffer))!=-1){
//                                        buffer.append(rawBuffer,0,length);
//                                    }
//                                    return buffer.toString();
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                return null;
//                            }
//                        }.start();
//                    }
//                    else{
//                        editor.putString("config","false");
//                        editor.commit();
//                    }


                    break;
            }
        }
    };

}