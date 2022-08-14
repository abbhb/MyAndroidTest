package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

public class AskMe extends AppCompatActivity {
    private Button askdev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_ask_me);
        askdev = (Button) findViewById(R.id.calldev);
        askdev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isQQClientAvailable(AskMe.this)){
                    final String qqUrl = "mqqwpa://im/chat?chat_type=wpa&uin=1057117849&version=1";
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(qqUrl)));
                }else{
                    Toast.makeText(AskMe.this,"请先安装QQ客户端",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public static boolean isQQClientAvailable(Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

}