package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.View.Uid;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class yuanshenlist extends AppCompatActivity {
    private String cookie;
    private TextView textView;
    private Uid uidclass;
    private String uid;
    private Button uidxiugai;
    private Button zdqdbutton;
    private Button ssbfbutton;
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
        uidxiugai = (Button) findViewById(R.id.uidxiugai);
        zdqdbutton = (Button)findViewById(R.id.yuanshenzdqdbutton);
        ssbfbutton = (Button)findViewById(R.id.yuanshenssbfbutton) ;
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
//                    autoqdbyuidandcookieclass.synchronous.setVisibility(View.GONE);
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