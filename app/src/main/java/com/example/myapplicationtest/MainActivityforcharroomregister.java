package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplicationtest.login.LoginReturn;
import com.example.myapplicationtest.login.LoginUser;
import com.example.myapplicationtest.login.RegisterUser;
import com.example.myapplicationtest.login.UserNameIF;
import com.example.myapplicationtest.login.UserNameIFReturn;
import com.example.myapplicationtest.login.YQM;
import com.example.utils.AbStrUtil;
import com.google.gson.Gson;

import java.util.Timer;
import java.util.TimerTask;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivityforcharroomregister extends AppCompatActivity {
    private static final String UPDATE_TIME_KEY = "123";
    private static final int UPDATE_TIME_VALUE = 2253;
    private static final int UPDATE_ERROR_VALUE = 444;
    private static final int UPDATE_NOT_FIND_VALUE = 2910;
    private static final int UPDATE_FIND_VALUE = 2911;
    private boolean isacceptregister = true;
    private EditText username;
    private EditText password;
    private EditText firstname;
    private EditText lastname;
    private EditText passwordqueren;
    private EditText yqm;
    private String YQMstring;
    private Handler mHandler;
    private TextView pwd_weak;
    private TextView pwd_in;
    private TextView pwd_strong;
    private String usernameifreturnstring;
    private boolean usernameisfound=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityforcharroomregister);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                |View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        username = findViewById(R.id.registerusernameedit);
        password = findViewById(R.id.registerpasswordedit);
        passwordqueren = findViewById(R.id.registerpasswordedit_qr);
        firstname = findViewById(R.id.registerfirstnameedit);//头像
        lastname = findViewById(R.id.registerlastnameedit);
        yqm = findViewById(R.id.registeryqm);
        pwd_weak = findViewById(R.id.pwd_weak);
        pwd_in = findViewById(R.id.pwd_in);
        pwd_strong = findViewById(R.id.pwd_strong);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what == UPDATE_TIME_VALUE) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityforcharroomregister.this);
                    builder.setTitle("提示");
                    builder.setMessage("注册成功");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.setNeutralButton("登录", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivityforcharroomregister.this,MainActivityforcharroom.class);
                            intent.putExtra("way","registertologin");
                            startActivity(intent);//开始执行
                            finish();
                        }
                    });
                    builder.show();
                }
                else if(msg.what == UPDATE_ERROR_VALUE){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityforcharroomregister.this);
                    builder.setTitle("提示");
                    builder.setMessage("很抱歉，注册失败了，可以稍后尝试，也可以联系开发者反馈！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                    builder.show();
                }
                else if(msg.what == UPDATE_FIND_VALUE){
                    //用户名重复
                    username.setError("用户名已经存在");
                }
                else if(msg.what == UPDATE_NOT_FIND_VALUE){
                    //用户名可以使用
                    username.setError("用户名可用",getDrawable(R.drawable.truetip));
                }
            }
        };
        Thread tt2 = new Thread(new GetYQMc());
        tt2.start();
        username.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    Thread tt3 = new Thread(new GetUserNameIsFound());
                    tt3.start();

                }
            }
        });
        username.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = username.getText().toString().trim();
                int length = str.length();
                if (length > 0) {
                    if (!AbStrUtil.isNumberLetter(str)) {
                        str = str.substring(0, length - 1);
                        username.setText(str);
                        String str1 = username.getText().toString().trim();
                        username.setSelection(str1.length());
                        username.setError("用户名只能是字母和数字");
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = password.getText().toString().trim();
                int length = str.length();
                if (length > 0) {
                    if (!AbStrUtil.isNumberLetter(str)) {
                        str = str.substring(0, length - 1);
                        password.setText(str);
                        String str1 = password.getText().toString().trim();
                        password.setSelection(str1.length());
                        password.setError("密码只能是字母和数字和特殊符号");
                    }
                }
                //输入框为0
                if (str.length() == 0)
                {
                    pwd_weak.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的纯数字为弱
                if (str.matches ("^[0-9]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的纯小写字母为弱
                else if (str.matches ("^[a-z]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的纯大写字母为弱
                else if (str.matches ("^[A-Z]+$"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和数字，输入的字符小于7个密码为弱
                else if (str.matches ("^[A-Z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和数字，输入的字符大于7个密码为中
                else if (str.matches ("^[A-Z0-9]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的小写字母和数字，输入的字符小于7个密码为弱
                else if (str.matches ("^[a-z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的小写字母和数字，输入的字符大于7个密码为中
                else if (str.matches ("^[a-z0-9]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和小写字母，输入的字符小于7个密码为弱
                else if (str.matches ("^[A-Za-z]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和小写字母，输入的字符大于7个密码为中
                else if (str.matches ("^[A-Za-z]{6,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和小写字母和数字，输入的字符小于5个个密码为弱
                else if (str.matches ("^[A-Za-z0-9]{1,5}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(205,205,205));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //输入的大写字母和小写字母和数字，输入的字符大于6个个密码为中
                else if (str.matches ("^[A-Za-z0-9]{6,8}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(205,205,205));
                }
                //大小字母加特殊符号
                else if (str.matches ("^[A-Za-z0-9#?!@$%^&*-]{8,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                }
                else if (str.matches ("^[a-z0-9#?!@$%^&*-]{8,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                }
                else if (str.matches ("^[A-Z0-9#?!@$%^&*-]{8,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                }
                //输入的大写字母和小写字母和数字，输入的字符大于8个密码为强
                else if (str.matches ("^[A-Za-z0-9]{8,16}"))
                {
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                }
                else{
                    pwd_weak.setBackgroundColor(Color.rgb(255,129,128));
                    pwd_in.setBackgroundColor(Color.rgb(255,184,77));
                    pwd_strong.setBackgroundColor(Color.rgb(113,198,14));
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {

            }
        });
    }
    public void clickcharaaa(View v){
        switch(v.getId()){
            case R.id.registerbutton:
                if(!isacceptregister){
                    Toast.makeText(this, "网路错误!", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }
                if(username.getText().toString().equals("")||password.getText().toString().equals("")||passwordqueren.getText().equals("")||lastname.getText().equals("")||yqm.getText().equals("")){
                    Toast.makeText(this, "请检查是否全部输入了数据", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }
                if (!password.getText().toString().equals(passwordqueren.getText().toString())){
                    Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }
                if(lastname.getText().toString().equals("")){
                    Toast.makeText(this, "请正确输入昵称", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }
                if (firstname.getText().toString().equals("")){
                    firstname.setText("https://imgloc.com/image/L2Jn7");
                }
                Drawable drawable = pwd_strong.getBackground();
                ColorDrawable dra = (ColorDrawable) drawable;
                if(dra.getColor()!=Color.rgb(113,198,14)){
                    Toast.makeText(this, "密码强度必须为“强”", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }
                if (!yqm.getText().toString().equals(YQMstring)){
                    Toast.makeText(this, "邀请码错误!", Toast.LENGTH_SHORT).show();//如果输入的用户名为空的话，那么下端会出现提示
                    return;
                }

                Thread tat = new Thread(new RegisterRunnable());
                tat.start();
                LoadingDialog.getInstance(this).show();//显示

                //中间加2s动画再往下
                Timer timer = new Timer();
                //void schedule(TimerTask task, long delay)
                // 在指定的delay延迟时间后，执行task任务
                timer.schedule(new MyTask(timer),2000); //这里的3000是3000毫秒，即3秒
            break;

        }

    }
    class MyTask extends TimerTask {
        private Timer timer;
        public MyTask(){}
        public MyTask(Timer t){
            this.timer = t;
        }
        @Override
        public void run() {

            LoadingDialog a = LoadingDialog.getInstance(MainActivityforcharroomregister.this);
            a.dismiss();
            LoadingDialog.setInstance(null);//设置为空 然会报错
            if(MainActivityforcharroom.lrt.getToken()!=null&&!MainActivityforcharroom.lrt.getUsername().equals("")){
                //弹出注册成功确认
                Message updateTimeMessage = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                updateTimeMessage.what = UPDATE_TIME_VALUE;
                updateTimeMessage.setData(bundle);
                mHandler.sendMessage(updateTimeMessage);
            }
            else{
                //注册失败，确认后返回主界面
                System.out.println(MainActivityforcharroom.lrt.toString());
                Message updateTimeMessage = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                updateTimeMessage.what = UPDATE_ERROR_VALUE;
                updateTimeMessage.setData(bundle);
                mHandler.sendMessage(updateTimeMessage);
            }
        }
    }
    public class GetYQMc implements Runnable{
        @Override
        public void run() {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        // 指定访问的服务器地址
                        .url("http://121.5.71.186:4000/users/getYQM").get()
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                System.out.println(responseData);
                Gson gson = new Gson();
                YQM yqmobject = gson.fromJson(responseData, YQM.class);
                YQMstring = yqmobject.getYqm();
                Log.d("YQM",yqmobject.getYqm());
            } catch (Exception e) {
                e.printStackTrace();
                isacceptregister = false;
            }
        }

    }
    public class GetUserNameIsFound implements Runnable{
        @Override
        public void run() {
            Gson gson = new Gson();
            UserNameIF usernameif = new UserNameIF(username.getText().toString());
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody RequestBody2 = RequestBody.create(type,gson.toJson(usernameif));
            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        // 指定访问的服务器地址
                        .url("http://121.5.71.186:4000/users/getUserNameIsFound").post(RequestBody2)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                System.out.println(responseData);
                UserNameIFReturn usernameifreturnobject = gson.fromJson(responseData, UserNameIFReturn.class);
                usernameifreturnstring = usernameifreturnobject.getFound();
                Log.d("usernameifreturnobject",usernameifreturnobject.getFound());
                if(usernameifreturnstring.equals("User not found")){
                    Message updateTimeMessage = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                    updateTimeMessage.what = UPDATE_NOT_FIND_VALUE;
                    updateTimeMessage.setData(bundle);
                    mHandler.sendMessage(updateTimeMessage);
                }
                else if(usernameifreturnstring.equals("User found")){
                    Message updateTimeMessage = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putString(UPDATE_TIME_KEY, "[Handle.sendMessage]" + "text");
                    updateTimeMessage.what = UPDATE_FIND_VALUE;
                    updateTimeMessage.setData(bundle);
                    mHandler.sendMessage(updateTimeMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                isacceptregister = false;
            }
        }

    }

    public class RegisterRunnable implements Runnable{
        @Override
        public void run() {

            Gson gson = new Gson();
            RegisterUser user = new RegisterUser(firstname.getText().toString(),lastname.getText().toString(),username.getText().toString().toLowerCase(),password.getText().toString());
            String jsonInString = gson.toJson(user);
            MediaType type = MediaType.parse("application/json;charset=utf-8");
            RequestBody RequestBody2 = RequestBody.create(type,jsonInString);
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        // 指定访问的服务器地址
                        .url("http://121.5.71.186:4000/users/register").post(RequestBody2)
                        .build();
                Response response = client.newCall(request).execute();
                String responseData = response.body().string();
                System.out.println(responseData);
                MainActivityforcharroom.lrt = gson.fromJson(responseData,LoginReturn.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}