package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;

public class Autoqdbyuidandcookie extends AppCompatActivity {
    public Button stoporrun;
    public Button synchronous;
    public TextView headtip;
    public Button save;
    private SharedPreferences user;
    private SharedPreferences.Editor editor;
    private static final int UPDATE_TEXT = 10122;
    private static final int UPDATE_FOU = 1004435;
    private static final int UPDATE_NIO = 5674544;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autoqd);
        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case UPDATE_TEXT:
                        headtip.setText(R.string.run);
                        synchronous.setVisibility(View.VISIBLE);
                        stoporrun.setText(R.string.stopbutton);
                        headtip.setTextColor(getColor(R.color.gre));
                        break;
                    case UPDATE_FOU:

                        headtip.setText(R.string.stop);
                        headtip.setTextColor(getColor(R.color.red));
                        stoporrun.setText(R.string.runbutton);
                        synchronous.setVisibility(View.GONE);
                        break;
                    case UPDATE_NIO:
                        Toast.makeText(Autoqdbyuidandcookie.this, "错误", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(Autoqdbyuidandcookie.this, "未知", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        user = getSharedPreferences("user",0);
        editor = user.edit();
        // 根据id在布局中找到控件对象
        headtip = (TextView) findViewById(R.id.text_state_zdqd);
        stoporrun = (Button) findViewById(R.id.btn_stop_run_zdqd);
        synchronous = (Button) findViewById(R.id.btn_synchronous_zdqd);
        save = (Button)findViewById(R.id.btn_save);





        if(user.getString("config","").equals("true")){
            Log.d("succuss",user.getString("config","")+666);
            headtip.setText(R.string.run);
            headtip.setTextColor(getColor(R.color.gre));
            stoporrun.setText(R.string.stopbutton);
            synchronous.setVisibility(View.VISIBLE);
        }
        else{
            headtip.setText(R.string.stop);
            synchronous.setVisibility(View.GONE);
            stoporrun.setText(R.string.runbutton);
            headtip.setTextColor(getColor(R.color.red));

        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Autoqdbyuidandcookie.this, "已保存", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        synchronous.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(user.getString("uid","").equals("")){
                    Toast.makeText(Autoqdbyuidandcookie.this, "请检查uid设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.d("clicklist","synchronous");
                long dq =new Date().getTime();
                Log.d("time", String.valueOf(dq));
                if(dq-Long.parseLong(user.getString("runorstoplasttime","0"))<30000){
                    Toast.makeText(Autoqdbyuidandcookie.this, "请半分钟后尝试！", Toast.LENGTH_SHORT).show();
                    return;
                }
                long timeGetTime =new Date().getTime();
                editor.putString("runorstoplasttime", String.valueOf(timeGetTime));
                editor.commit();

                String path = "http://121.5.71.186:9527/a?uid="+user.getString("uid","")+"&way=uidupdate"+"&cookie="+user.getString("cookie","p");

                new Thread(){
                    @Override
                    public void run() {
                        networdRequest(path);
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
                                Log.d("succuss", "成功了 ");
                                editor.putString("config","true");
                                editor.commit();
                                Toast.makeText(Autoqdbyuidandcookie.this, result, Toast.LENGTH_SHORT).show();
                                Log.d("result",result);

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
            };
        });
        stoporrun.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("clicklist","stoporrun");
                if(user.getString("config","false").equals("false")){
                    long dq =new Date().getTime();
                    Log.d("time", String.valueOf(dq));
                    if(user.getString("uid","").equals("")){
                        Toast.makeText(Autoqdbyuidandcookie.this, "请检查uid设置", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(dq-Long.parseLong(user.getString("runorstoplasttime","0"))<30000){
                        Toast.makeText(Autoqdbyuidandcookie.this, "请半分钟后尝试！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long timeGetTime =new Date().getTime();
                    editor.putString("runorstoplasttime", String.valueOf(timeGetTime));
                    editor.commit();
                    String path = "http://121.5.71.186:9527/a?uid="+user.getString("uid","")+"&cookie="+user.getString("cookie","");

                    new Thread(){
                        @Override
                        public void run() {
                            networdRequest(path);
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
                                    JSONObject toJsonObj= new JSONObject(result);

                                    Message msg = new Message();
                                    msg.what = UPDATE_TEXT;
                                    //还可以通过message.obj = "";传值
                                    handle.sendMessage(msg);
                                    editor.putString("config","true");
                                    editor.commit();
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
                    long dq =new Date().getTime();
                    if(dq-Long.parseLong(user.getString("runorstoplasttime","0"))<30000){
                        Toast.makeText(Autoqdbyuidandcookie.this, "请半分钟后尝试！", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String path = "http://121.5.71.186:9527/a?uid="+user.getString("uid","")+"&way=uiddelete"+"&cookie="+user.getString("cookie","");
                    long timeGetTime =new Date().getTime();
                    editor.putString("runorstoplasttime", String.valueOf(timeGetTime));
                    editor.commit();
                    new Thread(){
                        @Override
                        public void run() {
                            networdRequest(path);
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
//                                    JSONObject toJsonObj= new JSONObject(result);

                                    Message msg = new Message();
                                    msg.what = UPDATE_FOU;
                                    //还可以通过message.obj = "";传值
                                    handle.sendMessage(msg);
                                    editor.putString("config","false");
                                    editor.commit();
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
            };
        });
        String patha = "http://121.5.71.186:9527/a?uid="+user.getString("uid","123")+"&way=uidget"+"&cookie="+user.getString("cookie","p");

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
                            if(user.getString("config","")=="false"){
                                Message msg = new Message();
                                msg.what = UPDATE_TEXT;
                                //还可以通过message.obj = "";传值
                                handle.sendMessage(msg);
                                editor.putString("config","true");
                                editor.commit();

                            }
                        }
                        else{
                            if(user.getString("config","")=="true"){
                                Message msg = new Message();
                                msg.what = UPDATE_FOU;
                                //还可以通过message.obj = "";传值
                                handle.sendMessage(msg);
                                editor.putString("config","false");
                                editor.commit();

                            }
//                            else{
//                                Log.d("succuss",user.getString("config",""));
//                                Message msg = new Message();
//                                msg.what = UPDATE_NIO;
//                                //还可以通过message.obj = "";传值
//                                handle.sendMessage(msg);
//                            }
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

        // 为按钮绑定点击事件监听器
//        save.setOnClickListener(mClickListener);
//        stoporrun.setOnClickListener(mClickListener);
//        synchronous.setOnClickListener(mClickListener);
    }



}


//case R.id.btn_stop_run_zdqd:
//        SharedPreferences userv = getSharedPreferences("user", 0);
//        if(userv.getString("config","false").equals("false")){
//        SharedPreferences.Editor editorr = userv.edit();
//        editorr.putString("config","true");
//        editorr.commit();
//        autoqdbyuidandcookieclass.headtip.setText(R.string.run);
//        autoqdbyuidandcookieclass.headtip.setTextColor(R.color.gre);
//        autoqdbyuidandcookieclass.stoporrun.setText(R.string.stopbutton);
//        autoqdbyuidandcookieclass.synchronous.setVisibility(View.VISIBLE);
//
//

//
//
//
//        }
//        else{
//        SharedPreferences.Editor editorr = userv.edit();
//        editorr.putString("config","false");
//        editorr.commit();
//        autoqdbyuidandcookieclass.headtip.setText(R.string.stop);
//        autoqdbyuidandcookieclass.headtip.setTextColor(R.color.red);
//        autoqdbyuidandcookieclass.stoporrun.setText(R.string.runbutton);
//        autoqdbyuidandcookieclass.synchronous.setVisibility(View.GONE);
//
//
//
//
//
//        }
//        break;