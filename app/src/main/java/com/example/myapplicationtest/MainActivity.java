package com.example.myapplicationtest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.function.openpersonsnum;
import com.example.myapplicationtest.server.MyService;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int COOKIE_NOT = 345322;
    private static final int UPADTA_WIDGET = 143322;
    private Button buttontijiao;
    private TextView renshu1;
    private static final int UPDATA_PEOPLE=2313114;
    private SharedPreferences userav;
    private SharedPreferences.Editor editorav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        renshu1 = (TextView)findViewById(R.id.cishutexty);
        userav = getSharedPreferences("user",0);
        editorav = userav.edit();
        AppUpdater appUpdater = new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("http://121.5.71.186/updata.json")
                .setButtonDoNotShowAgain(null);
        appUpdater.start();
        Handler handle = new Handler() {
            public void handleMessage(Message msg) {
                switch(msg.what) {
                    case UPDATA_PEOPLE:
                        renshu1.setText("系列app总使用次数为:"+msg.obj.toString()+"次");
                        break;
                    case COOKIE_NOT:
                        Toast.makeText(MainActivity.this, "ys-cookie过期", Toast.LENGTH_SHORT).show();
                        break;
                    case UPADTA_WIDGET:

                        break;
                    default:
                        Toast.makeText(MainActivity.this, "未知", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        startForegroundService(new Intent(this, MyService.class));

        new Thread(() -> {
            try {
                Log.d("tag","开始socket");
                Socket socket = new Socket("121.5.71.186", 9999);
                Log.d("tag","cheng共");

                OutputStream dout = socket.getOutputStream();
                String str = "请求访问人数";
                dout.write(str.getBytes());
                // 通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
                socket.shutdownOutput();
                // 接收服务端发送的消息
                InputStream din = socket.getInputStream();
                byte[] outPut = new byte[4096];
                while (din.read(outPut) > 0) {
                    String result = new String(outPut);
                    System.out.println("服务端反回的的消息是：" + result);
                    editorav.putString("returnpeople",result);
                    editorav.commit();
                    if(!result.equals("")){
                        Message msg = new Message();
                        msg.what = UPDATA_PEOPLE;
                        //还可以通过message.obj = "";传值
                        msg.obj = result;
                        handle.sendMessage(msg);
                    }

                }
                din.close();
                dout.close();
                socket.close();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();

        String timelast = userav.getString("laststartapptime","0");
        long timeGetTime =new Date().getTime();
        if(timeGetTime-Long.parseLong(timelast)>300000||userav.getString("cardresultdata","").equals("")||userav.getString("cardresultdata","").indexOf("expeditions")==-1){
            editorav.putString("laststartapptime", String.valueOf(timeGetTime));
            if(userav.getString("cookie","").equals("")){
                Toast.makeText(this, "不包含ys-cookie", Toast.LENGTH_SHORT).show();
                editorav.putString("config","false");
                editorav.commit();

            }
            else{
                String path = "http://121.5.71.186:9527/a?"+"cookie="+userav.getString("cookie","");

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
                                Message msg = new Message();

                                if (result.equals("")){
                                    msg.what = COOKIE_NOT;
                                    //还可以通过message.obj = "";传值
                                    handle.sendMessage(msg);
                                    editorav.putString("config","false");
                                    editorav.commit();
                                }
                                else{
                                    msg.what = UPADTA_WIDGET;
                                    //还可以通过message.obj = "";传值
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





        }
//        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), NewAppWidget.class));
//        NewAppWidget myWidget = new NewAppWidget();
//        myWidget.onUpdate(this, AppWidgetManager.getInstance(this),ids);
    }
    public void clicklist(View v){
        switch(v.getId()){
            case R.id.card_viewtzjsq :
                Log.e("clicklist","通过xml文件绑定监听");
                Intent tzjsq = new Intent(MainActivity.this, MainActivityfortizhong.class);
                startActivity(tzjsq);
                break;
            case R.id.charroomid :
                Log.e("clicklist","通过xml文件绑定监听");
                Intent charroom = new Intent(MainActivity.this, MainActivityforcharroom.class);
                charroom.putExtra("way","no");
                startActivity(charroom);
                break;
            case R.id.wlts:
                Log.e("clicklist","通过xml文件绑定监听");
                Intent wlts = new Intent(MainActivity.this, MainActivityforwlts.class);
                startActivity(wlts);
                break;
            case R.id.yuanshenfuzhu:
                Log.e("clicklist","通过xml文件绑定监听");

                String cookie = userav.getString("cookie","");
                if (cookie.equals("")||cookie.indexOf("token")==-1){
                    Intent ysfz = new Intent(MainActivity.this, MainActivityplusysfzindex.class);
                    startActivity(ysfz);
                    break;
                }else{
                    Intent ysfz = new Intent(MainActivity.this, yuanshenlist.class);
                    startActivity(ysfz);
                    break;
                }

        }
    }
    class renshu implements Runnable{

        @Override
        public void run() {

        }
    }
}