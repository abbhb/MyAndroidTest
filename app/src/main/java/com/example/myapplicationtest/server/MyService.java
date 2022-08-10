package com.example.myapplicationtest.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.myapplicationtest.FirstView;
import com.example.myapplicationtest.MainActivity;
import com.example.myapplicationtest.NewAppWidget;
import com.example.myapplicationtest.R;

import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private NotificationManager notificationManager;
    private Notification notification;
    private NotificationChannel notificationChannel;
    private final static String NOTIFICATION_CHANNEL_ID = "CHANNEL_ID";
    private final static String NOTIFICATION_CHANNEL_NAME = "CHANNEL_NAME";
    private final static int FOREGROUND_ID=1;
    private final static int UPDATA_MSGG=100992;
    private Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                // TODO Auto-generated method stub
                if(msg.what == UPDATA_MSGG){
                    //这里可以进行UI操作，如Toast，Dialog等
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
                    int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(getApplicationContext(), NewAppWidget.class));
                    if (appWidgetIds.length > 0) {
                        new NewAppWidget().onUpdate(getApplicationContext(), appWidgetManager, appWidgetIds);
                    }

                }
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                //若操作UI线程需要借助Handler
                //获取Widgets管理器
                Message msg = new Message();
                msg.what = UPDATA_MSGG;
                mHandler.sendMessage(msg);



            }
        }, 500, 3600000);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);






        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        intent = new Intent(getApplicationContext(), FirstView.class);  //点击通知栏后想要被打开的页面MainActivity.class
        PendingIntent pendingIntent;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.tubiao)
                    .setContentTitle("原神辅助")
                    .setContentText("守护进程~")
                    .setContentIntent(pendingIntent)
                    .build();
        }
        notification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(FOREGROUND_ID, notification);
        return Service.START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TAG","onDestroy myService");
    }



}