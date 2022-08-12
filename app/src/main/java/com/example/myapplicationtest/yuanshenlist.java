package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private static final int PANDUANISGUOQI = 563362;
    private String cookie;
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
        Intent intent = getIntent();
        textView = (TextView) findViewById(R.id.uid);
        cookie = intent.getStringExtra("cookie");
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
                Intent intent1 = new Intent(yuanshenlist.this,Autoqdbyuidandcookie.class);
                startActivity(intent1);
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
        if(user.getString("cookie","").equals("")){
            Intent intent1 = new Intent(this,MainActivityplusysfzindex.class);
            startActivity(intent1);
            finish();
        }
        else{
            //准备网络请求
            String urlStr = "https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";
            new Thread(){
                @Override
                public void run() {
                    networdRequest(urlStr);
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
                        connection.setRequestProperty("Cookie",user.getString("cookie",""));
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

                            JSONObject jsonObject = new JSONObject(result);
                            Log.d("succuss", "成功了 "+jsonObject.getInt("retcode"));
                            if (jsonObject.getInt("retcode")==0){
                            }else{
                                Message msg = new Message();
                                msg.what = PANDUANISGUOQI;
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
            }.start();



        }
        if(!user.getString("uid","").equals("")){
            String patha = "http://121.5.71.186:9527/a?uid="+user.getString("uid","123")+"&way=uidget"+"&cookie="+user.getString("cookie","");
            SharedPreferences useradda = getSharedPreferences("user", 0);
            SharedPreferences.Editor editor666 = useradda.edit();
            new Thread(){
                @Override
                public void run() {
                    networdRequest(patha);
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
                            if(result.equals("cunzai")){
                                editor666.putString("config","true");
                                editor666.commit();
                            }
                            else{
                                editor666.putString("config","false");
                                editor666.commit();
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
            }.start();
        }
        else{
            SharedPreferences useradda = getSharedPreferences("user", 0);
            SharedPreferences.Editor editor666 = useradda.edit();
            editor666.putString("config","false");
            editor666.commit();
        }


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
                    if(!user.getString("uid","").equals("")){
                        String patha = "http://121.5.71.186:9527/a?uid="+user.getString("uid","")+"&way=uidget"+"&cookie="+user.getString("cookie","");

                        new Thread(){
                            @Override
                            public void run() {
                                networdRequest(patha);
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
                                        if(result.equals("cunzai")){
                                            editor.putString("config","true");
                                            editor.commit();
                                        }
                                        else{
                                            editor.putString("config","false");
                                            editor.commit();
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
                        }.start();
                    }
                    else{
                        editor.putString("config","false");
                        editor.commit();
                    }


                    break;
            }
        }
    };

}