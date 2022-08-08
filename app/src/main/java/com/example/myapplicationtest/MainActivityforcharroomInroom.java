package com.example.myapplicationtest;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import androidx.appcompat.app.AlertDialog;

import androidx.recyclerview.widget.LinearLayoutManager;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.View.StrokeImageView;

import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityforcharroomInroom extends AppCompatActivity implements View.OnClickListener {
    private List<Msg> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private Button syncMessage;
    private Button back;
    private RecyclerView msgRecyclerView;
    private MsgAdapter adapter;
    private Socket socketSend;
    private int countActivity = 0;
    //是否进入后台
    private boolean isBackground = false;
    private Drawable drawableimage;


    private Socket st;
    private String ip="121.5.71.186";
    private String port="10088";
    ObjectInputStream dis;
    ObjectOutputStream dos;
    boolean isRunning = false;
    private TextView myName;
    private TextView usernametest;
    private StrokeImageView userphoto;
    private String recMsg;
    private ListMsg temp;
    private boolean isRecMsg = false;
    private boolean isSend=false;
    private  String name;
    private String content;
    private boolean isduankau = true;
    private String TimeGood="您好";
    private Handler handler = new Handler(Looper.myLooper()){//获取当前进程的Looper对象传给handler
        @Override
        public void handleMessage(@NonNull Message msg){//?
            if(msg.obj!=null){
                addNewMessage((List<Msg>) msg.obj,Msg.TYPE_RECEIVED);//添加新数据
            }
        }
    };
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_DEL                                            //退格的拦截，用来过滤返回键
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {                                  //返回键的操作

            return true;
        }

        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBackgroundCallBack();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_main_activityforcharroom_inroom);
        Date d = new Date();
        if(d.getHours()<8&&d.getHours()>=5){
            TimeGood = "早上好";
        }else if(d.getHours()<11){
            TimeGood = "上午好";
        }else if(d.getHours()<13){
            TimeGood = "中午好";
        }else if(d.getHours()<18){
            TimeGood = "下午好";
        }else if(d.getHours()<24){
            TimeGood = "晚上好";
        }
        usernametest = findViewById(R.id.usernametext);
        usernametest.setText(MainActivityforcharroom.lrt.getUsername()
                +","
                +TimeGood
                +"!");
        userphoto = findViewById(R.id.userphoto);
        drawableimage = LoadImageFromWebOperations(MainActivityforcharroom.lrt.getFirstName());
        userphoto.setImageDrawable(drawableimage);
        name=MainActivityforcharroom.lrt.getLastName();
        inputText = findViewById(R.id.input_text);
        send=findViewById(R.id.send);
        send.setOnClickListener(this);
        View statusBar = findViewById(R.id.statusBarView);
        ViewGroup.LayoutParams layoutParams = statusBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                AlertDialog.Builder dialog= new AlertDialog.Builder(MainActivityforcharroomInroom.this);
                dialog.setTitle("退出");
                dialog.setMessage("退出登录?");
                dialog.setCancelable(false);
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();//finish()是在程序执行的过程中使用它来将对象销毁,finish（）方法用于结束一个Activity的生命周期
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();//让返回键开始启动
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivityforcharroomInroom.this);
                msgRecyclerView= findViewById(R.id.msg_recycler_view);
                msgRecyclerView.setLayoutManager(layoutManager);
                adapter = new MsgAdapter(msgList);
                msgRecyclerView.setAdapter(adapter);
            }
        });
        new Thread(new Runnable(){
            @Override
            public void run(){

                try{
                    socketSend = new Socket(ip,Integer.parseInt(port));
                    if(socketSend==null){
                        Log.d("ttw","发送了一条消息1");
                        inputText.setText("");
                    }
                    else{

                        isRunning = true;
                        Log.d("ttw","连接成功");
                        dis = new ObjectInputStream(socketSend.getInputStream());
                        dos = new ObjectOutputStream(socketSend.getOutputStream());
                        new Thread(new receive(),"接收线程").start();
                        new Thread(new Send(),"发送线程").start();
//                        new Thread(new jiance(),"监测socket线程").start();
                    }

                }catch (IOException e){
                    isRunning = false;
                    finish();
                }catch(Exception e){

                    e.printStackTrace();

                    Toast.makeText(MainActivityforcharroomInroom.this, "连接服务器失败！！！", Toast.LENGTH_SHORT).show();
                    finish();
                    try{
                        socketSend.close();
                    }catch(IOException e1){
                        e1.printStackTrace();
                    }

                }
            }
        }).start();
    }
    public void addNewMessage(List<Msg> msg,int type){//接受消息
        for (Msg a:msg){
            a.setType(type);
            msgList.add(a);
            adapter.notifyItemInserted(msgList.size()-1);
            msgRecyclerView.scrollToPosition(msgList.size()-1);
        }
    }
    public static Boolean isServerClose(Socket socket) {
        try {
            socket.sendUrgentData(0);//发送1个字节的紧急数据，默认情况下，服务器端没有开启紧急数据处理，不影响正常通信
            return false;
        } catch (Exception se) {
            return true;
        }
    }

        private void initBackgroundCallBack() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                    @Override
                    public void onActivityCreated(Activity activity, Bundle bundle) {

                    }

                    @Override
                    public void onActivityStarted(Activity activity) {
                        countActivity++;
                        if (countActivity == 1 && isBackground) {
                            Log.e("MainActivityforcharroomInroom", "onActivityStarted: 应用进入前台");
                            isBackground = false;
                            //说明应用重新进入了前台
                            Toast.makeText(MainActivityforcharroomInroom.this, "应用进入前台", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onActivityResumed(Activity activity) {

                    }

                    @Override
                    public void onActivityPaused(Activity activity) {

                    }

                    @Override
                    public void onActivityStopped(Activity activity) {
                        countActivity--;
                        if (countActivity <= 0 && !isBackground) {
                            Log.e("MainActivityforcharroomInroom", "onActivityStarted: 应用进入后台");
                            isBackground = true;
                            //说明应用进入了后台
                            Toast.makeText(MainActivityforcharroomInroom.this, "应用进入后台", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                    }

                    @Override
                    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

                    }

                    @Override
                    public void onActivityDestroyed(Activity activity) {

                    }
                });
            }
        }

    class receive implements Runnable{
        public void run(){
            temp = null;
            while(isRunning){
                try{
                    temp = (ListMsg) dis.readObject();
                }catch(Exception e){
                    e.printStackTrace();
                }
                if(temp!=null){
                    Log.d("ttw","inputStream:"+dis);
                    Message message = new Message();
                    message.obj=temp.getList();
                    handler.sendMessage(message);
                }
            }
        }
    }
    class jiance implements Runnable{
        public void run(){
            while(isRunning){
                try {
                    if(dis.read()==-1){
                        isduankau = false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onClick(View view){
        content = inputText.getText().toString();
        if (content.equals("")){
            Toast.makeText(MainActivityforcharroomInroom.this, "请勿发送空消息！", Toast.LENGTH_SHORT).show();
            return;
        }
        @SuppressLint("SimpleDateFormat")
        String date = new SimpleDateFormat("kk:mm:ss").format(new Date());
        String datem = new SimpleDateFormat("kk:mm").format(new Date());
        String dater = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String dateaaa = new SimpleDateFormat("yyyyMMdd").format(new Date());
        long dqdate = new Date().getTime();
        StringBuilder sb = new StringBuilder();
        Log.d("twwww", String.valueOf(msgList.size()));
        Log.d("twwww", String.valueOf(dqdate));
        sb.append(content);
        content = sb.toString();
        if(!"".equals(content)){
            if (msgList.size()==0){
                Msg msg = new Msg(dqdate,dqdate,name,content,datem,dateaaa,Msg.TYPE_SENT,true);
                msgList.add(msg);
                adapter.notifyItemInserted(msgList.size()-1);
                msgRecyclerView.scrollToPosition(msgList.size()-1);
                isSend = true;
            }
           else {
                Msg tmsg = msgList.get(msgList.size()-1);
                if(dqdate-tmsg.getLastdate()>100000l){
                    Msg msg = new Msg(tmsg.getLastdate(),dqdate,name,content,datem,dater,Msg.TYPE_SENT,true);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    isSend = true;
                }
                else{
                    Msg msg = new Msg(tmsg.getLastdate(),dqdate,name,content,datem,dater,Msg.TYPE_SENT,false);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);
                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    isSend = true;
                }
            }
        }
        sb.delete(0,sb.length());
    }
    class Send implements Runnable{
        @Override
        public void run(){
            while(isRunning&&isduankau){

//                try{
//                    socketSend.sendUrgentData(0xFF);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Toast.makeText(MainActivityforcharroomInroom.this, "网络错误！", Toast.LENGTH_SHORT).show();
//                    finish();
//                }

//                Log.d("ttw","发了一条消息");
                if(!"".equals(content)&&isSend){
                    @SuppressLint("SimpleDateFormat")
                    String dater = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String date = new SimpleDateFormat("kk:mm:ss").format(new Date());
                    StringBuilder sb = new StringBuilder();
                    sb.append(content).append("\n\n来自：").append(name).append("\n"+date);
                    content = sb.toString();
                    try{
                        List<Msg> msglisttemp = new ArrayList<Msg>();
                        Msg tmsg = msgList.get(msgList.size()-1);
                        msglisttemp.add(tmsg);
                        ListMsg temp = new ListMsg();
                        temp.setList(msglisttemp);
                        dos.writeObject(temp);
                        dos.flush();
                        sb.delete(0,sb.length());
                        Log.d("ttw","真发送了一条消息");
                        inputText.setText("");
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                    content = "";
                    isSend = false;
                    inputText.setText("");
                }
            }
        }
    }
    private Drawable LoadImageFromWebOperations(String url)
    {
        try
        {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        }catch (Exception e) {
            System.out.println("Exc="+e);
            return null;
        }
    }
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

}
class ListMsg implements Serializable {
    private List<Msg> list;

    public List<Msg> getList() {
        return list;
    }

    public void setList(List<Msg> list) {
        this.list = list;
    }

}
