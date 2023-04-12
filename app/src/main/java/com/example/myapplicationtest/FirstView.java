package com.example.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.myapplicationtest.novel.utils.UtilityAppConfig;
import com.renrui.libraries.util.LibUtility;

public class FirstView extends AppCompatActivity {
    private TextView skip;
    private boolean isSkip = false;
    private int remainingtime = 800;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_first_view);
        LibUtility.initStatusHeight(this);

        // 更新配置信息
        UtilityAppConfig.updateConfigInfo();
        //创建子线程
        Thread myThread= new Thread(() -> {
            try{
                Thread.sleep(remainingtime);//使程序休眠3秒
                Intent it=new Intent(getApplicationContext(),MainActivity.class);//启动MainActivity
                startActivity(it);
                finish();//关闭当前活动
            }catch (Exception e){
                e.printStackTrace();//有时间给跳过加个倒计时
            }
        });
        myThread.start();//启动线程
        /*
        * 跳过按钮
        * */
//        skip = (TextView) findViewById(R.id.skip);
//        skip.setOnClickListener(new View.OnClickListener() {  // 设置跳过按键的监听器
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(FirstView.this, MainActivity.class);
//                isSkip = true;
//                startActivity(intent);
//                FirstView.this.finish();
//            }
//        });
//
//        final Handler handler = new Handler() {
//            @Override
//            public void handleMessage(@NonNull Message msg) {
//                switch (msg.what) {
//                    case -2:
//                        skip.setText("跳过( "+remainingtime+"s )");
//                        break;
//                    case 1:
//                        // 这里记得要判断是否选择跳过，防止重复加载LoginActivity
//                        if (!isSkip) {
//                            Intent intent = new Intent(FirstView.this, MainActivity.class);
//                            startActivity(intent);
//                            isSkip = true;
//                            FirstView.this.finish();
//                        }
//                        break;
//                }
//            }
//        };
//
//        new Thread(new Runnable() {  // 开启一个线程倒计时
//            @Override
//            public void run() {
//                for (; remainingtime>0; remainingtime--){
//                    handler.sendEmptyMessage(-2);
//                    if (remainingtime<=0)
//                        break;
//                    try {
//                        Thread.sleep(1000);
//                    }catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//                handler.sendEmptyMessage(1);
//            }
//        }).start();

    }
}