package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationtest.login.LoginReturn;
import com.example.myapplicationtest.login.LoginUser;

import com.example.utils.httpd;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityforcharroom extends AppCompatActivity {
    private static final String UPDATE_TIME_KEY = "123";
    private static final int UPDATE_TIME_VALUE = 2223;
    private static final int UPDATE_ERROR_VALUE = 5555;
    private OutputStream outputStream=null;
    private Socket socket=null;
    private String ip="121.5.71.186";
    private Button btn_cnt;
    private EditText et_ip;
    private EditText et_name;
    private TextView myName;
    private String tempstr1;
    public static LoginReturn lrt;
    public static String username;
    public String message;
    public static boolean state = false;
    public String temp;
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityforcharroom);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        et_name=findViewById(R.id.et_name);
        et_ip = findViewById(R.id.et_ip);
        Intent getint = getIntent();
        tempstr1 = getint.getStringExtra("way");
        if(tempstr1.equals("registertologin")){
            registertologin();
        }
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATE_TIME_VALUE) {
                    Intent intent = new Intent(MainActivityforcharroom.this,MainActivityforcharroomInroom.class);
                    //intent相当于传播的一种媒介，本行的意思是：使内部代码从MainActivity.this到ChatRoom.class进行转变，但只是设置了目标，还未开始执行
                    intent.putExtra("name",et_name.getText().toString());//这个意思相当于分类处理，在从Activity到ChatRoom，需要将数据送过去，那么就需要对即将传送的数据进行分类处理，使name，ip，port分别对应指定内容
                    intent.putExtra("ip",String.valueOf(R.string.ipaddress));
                    intent.putExtra("port",String.valueOf(R.string.port));
                    intent.putExtra("password",et_ip.getText().toString());
                    startActivity(intent);//开始执行
                }
                else if(msg.what == UPDATE_ERROR_VALUE){
                    Toast.makeText(MainActivityforcharroom.this, "请正确输入用户名和密码", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                }
            }
        };
    }

    public void clickchar(View v){
        switch(v.getId()){
            case R.id.btn_cnt :
                state = false;
                Log.e("clickchar","通过xml文件绑定监听");
                String name = et_name.getText().toString();
                if("".equals(name)){
                    Toast.makeText(this, "请正确输入用户名和密码", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                }else if ("".equals(et_ip.getText().toString())){
                    Toast.makeText(this, "请正确输入用户名和密码", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                }
                else{//反之
                    username = et_name.getText().toString();
                    Thread tt = new Thread(new LoginRunnable());
                    tt.start();
                    LoadingDialog.getInstance(this).show();//显示

                    //中间加2s动画再往下
                    Timer timer = new Timer();
                    //void schedule(TimerTask task, long delay)
                    // 在指定的delay延迟时间后，执行task任务
                    timer.schedule(new MyTask(timer),2000); //这里的3000是3000毫秒，即3秒
                }
                break;
            case R.id.btn_cnt_zc:
                Intent intentbbb = new Intent(MainActivityforcharroom.this,MainActivityforcharroomregister.class);
                startActivity(intentbbb);
                break;

        }

    }
    public void registertologin(){
        LoadingDialog.getInstance(this).show();//显示
        //中间加2s动画再往下
        Timer timer = new Timer();
        //void schedule(TimerTask task, long delay)
        // 在指定的delay延迟时间后，执行task任务
        timer.schedule(new MyTask(timer),2000); //这里的3000是3000毫秒，即3秒

    }
    class MyTask extends TimerTask {
        private Timer timer;
        public MyTask(){}
        public MyTask(Timer t){
            this.timer = t;
        }
        @Override
        public void run() {
            if(MainActivityforcharroom.lrt.getToken()!=null&&!MainActivityforcharroom.lrt.getUsername().equals("")){
                MainActivityforcharroom.state = true;
            }
            else{
                MainActivityforcharroom.state = false;

            }
            LoadingDialog a = LoadingDialog.getInstance(MainActivityforcharroom.this);
            a.dismiss();
            LoadingDialog.setInstance(null);//设置为空 然会报错
            if(state){

                Message updateTimeMessage = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                updateTimeMessage.what = MainActivityforcharroom.UPDATE_TIME_VALUE;
                updateTimeMessage.setData(bundle);
                mHandler.sendMessage(updateTimeMessage);
            }
            else{//此处也应套用上面的方法
                Message updateTimeMessage = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                updateTimeMessage.what = MainActivityforcharroom.UPDATE_ERROR_VALUE;
                updateTimeMessage.setData(bundle);
                mHandler.sendMessage(updateTimeMessage);

            }
        }
    }
    public class LoginRunnable implements Runnable{
        @Override
        public void run() {
            Gson gson = new Gson();
            LoginUser user = new LoginUser(username,et_ip.getText().toString());
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
                message = response.message();
                String responseData = response.body().string();
                MainActivityforcharroom.lrt = gson.fromJson(responseData,LoginReturn.class);
//                if(message.equals("OK")&&MainActivityforcharroom.lrt.getToken()!=null&&!MainActivityforcharroom.lrt.getUsername().equals("")){
//                    state = true;
//                }
//                else{
//                    state = false;
//
//                }
            } catch (Exception e) {
                e.printStackTrace();
                state = false;
            }
        }

    }
}