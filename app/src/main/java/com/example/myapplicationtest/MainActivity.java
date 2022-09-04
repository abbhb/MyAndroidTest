package com.example.myapplicationtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.myapplicationtest.ObjectDetection.ObjectDetectionActivity;
//import com.example.myapplicationtest.ObjectDetection.PrePostProcessor;
//import com.example.myapplicationtest.ObjectDetection.Result;
import com.example.myapplicationtest.server.MyService;
import com.example.utils.Updater;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.leaf.library.StatusBarUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.widget.Toolbar;

//import org.pytorch.IValue;
//import org.pytorch.LiteModuleLoader;
//import org.pytorch.Module;
//import org.pytorch.Tensor;
//import org.pytorch.torchvision.TensorImageUtils;

public class MainActivity extends AppCompatActivity{
    private static final int COOKIE_NOT = 345322;
    private static final int UPADTA_WIDGET = 143322;
    private Button buttontijiao;
    private TextView renshu1;
    private ImageView userphoto;
    private TextView title;
    private static final int UPDATA_PEOPLE=2313114;
    private SharedPreferences userav;
    private SharedPreferences.Editor editorav;
    Toolbar tl_content;
    private Typeface typeface;
//    private Bitmap mBitmap = null;
//    private Module mModule = null;
//    private float mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        typeface = Typeface.createFromAsset(getAssets(), "font/05汉仪乐喵字体.ttf");
        //去掉状态栏
        tl_content = (Toolbar) findViewById(R.id.tl_content);
        //和状态栏一起显示渐变色
        StatusBarUtil.setGradientColor(this, tl_content);
        //把toolbar作为系统自带的Actionbar
        setSupportActionBar(tl_content);

        for (int i = 0; i < tl_content.getChildCount(); i++) {
            View view = tl_content.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if ("趣魔盒".equals(textView.getText())) {
                    textView.setTypeface(typeface);
//                    textView.setLayoutParams(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }

        AppUpdater appUpdater = new AppUpdater(this)
                .setUpdateFrom(UpdateFrom.JSON)
                .setUpdateJSON("http://121.5.71.186/updata.json")
                .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Updater.clickBtn2(MainActivity.this);
                    }
                })
                .setButtonDoNotShowAgain(null);
        appUpdater.start();

        renshu1 = (TextView)findViewById(R.id.cishutexty);
        userav = getSharedPreferences("user",0);
        editorav = userav.edit();
        userphoto = (ImageView) findViewById(R.id.userphoto);
        userphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
            }
        });
        if (userav.getBoolean("confighiddenService",false)){
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            if (!am.getAppTasks().isEmpty()){
                am.getAppTasks().get(0).setExcludeFromRecents(true);
            }
        }else {
            ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
            if (!am.getAppTasks().isEmpty()){
                am.getAppTasks().get(0).setExcludeFromRecents(false);
            }
        }
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

//            case R.id.wtsbcard:
//                try {
//                    mModule = LiteModuleLoader.load(MainActivity.assetFilePath(getApplicationContext(), "yolov5s.torchscript.ptl"));
//                    BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open("classes.txt")));
//                    String line;
//                    List<String> classes = new ArrayList<>();
//                    while ((line = br.readLine()) != null) {
//                        classes.add(line);
//                    }
//                    PrePostProcessor.mClasses = new String[classes.size()];
//                    classes.toArray(PrePostProcessor.mClasses);
//                    Intent intent322 = new Intent(MainActivity.this, ObjectDetectionActivity.class);
//                    startActivity(intent322);
//                } catch (IOException e) {
//                    Log.e("Object Detection", "Error reading assets", e);
//                }
//                break;
        }
    }

//    @Override
//    public void run() {
//        Bitmap resizedBitmap = Bitmap.createScaledBitmap(mBitmap, PrePostProcessor.mInputWidth, PrePostProcessor.mInputHeight, true);
//        final Tensor inputTensor = TensorImageUtils.bitmapToFloat32Tensor(resizedBitmap, PrePostProcessor.NO_MEAN_RGB, PrePostProcessor.NO_STD_RGB);
//        IValue[] outputTuple = mModule.forward(IValue.from(inputTensor)).toTuple();
//        final Tensor outputTensor = outputTuple[0].toTensor();
//        final float[] outputs = outputTensor.getDataAsFloatArray();
//        final ArrayList<Result> results =  PrePostProcessor.outputsToNMSPredictions(outputs, mImgScaleX, mImgScaleY, mIvScaleX, mIvScaleY, mStartX, mStartY);
//
////        runOnUiThread(() -> {
////            mButtonDetect.setEnabled(true);
////            mButtonDetect.setText(getString(R.string.detect));
////            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
////            mResultView.setResults(results);
////            mResultView.invalidate();
////            mResultView.setVisibility(View.VISIBLE);
////        });
//    }

    class renshu implements Runnable{

        @Override
        public void run() {

        }
    }

    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //菜单显示图标
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    //菜单选项点击事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.i_setVisible:
                //设置的事件
                Intent intent3 = new Intent(this,SetApplication.class);
                startActivity(intent3);
                break;

            case R.id.i_lxwm:
                Intent intent2 = new Intent(this,AskMe.class);
                startActivity(intent2);
                break;
            case R.id.i_about:
                Intent intent1 = new Intent(this,About.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }

}