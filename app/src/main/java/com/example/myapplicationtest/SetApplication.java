package com.example.myapplicationtest;

import static android.graphics.Color.parseColor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.utils.Log;
import com.leaf.library.StatusBarUtil;
import com.suke.widget.SwitchButton;

public class SetApplication extends AppCompatActivity {
    private SharedPreferences userav;
    private SharedPreferences.Editor editorav;
    private Typeface typeface;
    private com.suke.widget.SwitchButton IgnoringBatteryOptimization;
    private com.suke.widget.SwitchButton HiddenService;
    private LinearLayout wuzhangai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_application);
        userav = getSharedPreferences("user",0);
        editorav = userav.edit();
        Toolbar toolbar = (Toolbar) findViewById(R.id.tl_conttt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        typeface = Typeface.createFromAsset(getAssets(), "font/05汉仪乐喵字体.ttf");
        IgnoringBatteryOptimization = (com.suke.widget.SwitchButton)findViewById(R.id.IgnoringBatteryOptimization);
        boolean booleanis = isignoreBatteryOptimization(this);//判断是否开启了电池优化
        if (booleanis){
            IgnoringBatteryOptimization.setChecked(true);
        }else {
            IgnoringBatteryOptimization.setChecked(false);
        }
        IgnoringBatteryOptimization.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                //TODO do your job
                if (isChecked){
                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                    Toast.makeText(SetApplication.this, "请手动关闭电池优化", Toast.LENGTH_SHORT).show();
                }
                else{
                    Log.d("ttw","取消");

                    Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }
                    Toast.makeText(SetApplication.this, "请手动打开电池优化", Toast.LENGTH_SHORT).show();

                }

            }
        });
        HiddenService = (com.suke.widget.SwitchButton)findViewById(R.id.HiddenService);
        if (userav.getBoolean("confighiddenService",false)){
            HiddenService.setChecked(true);
        }else {
            HiddenService.setChecked(false);
        }


        HiddenService.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked){
                    ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                    if (!am.getAppTasks().isEmpty()){
                        am.getAppTasks().get(0).setExcludeFromRecents(true);
                    }
                    editorav.putBoolean("confighiddenService",true);
                    editorav.commit();
                }
                else{
                    ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
                    if (!am.getAppTasks().isEmpty()){
                        am.getAppTasks().get(0).setExcludeFromRecents(false);
                        editorav.putBoolean("confighiddenService",false);
                        editorav.commit();
                    }
                }
            }
        });
        wuzhangai = (LinearLayout)findViewById(R.id.wuzhangai);
        wuzhangai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });




        //和状态栏一起显示渐变色
        StatusBarUtil.setGradientColor(this, toolbar);
        //把toolbar作为系统自带的Actionbar
        setSupportActionBar(toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if ("设置".equals(textView.getText())) {
                    textView.setTypeface(typeface);
//                    textView.setGravity(Gravity.CENTER);
//                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
//                    params.gravity = Gravity.CENTER;
//                    textView.setLayoutParams(params);
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isignoreBatteryOptimization(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);

            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(activity.getPackageName());
            //  判断当前APP是否有加入电池优化的白名单，如果没有，弹出加入电池优化的白名单的设置对话框。
            if (!hasIgnored) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }
}