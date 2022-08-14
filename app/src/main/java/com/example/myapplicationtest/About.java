package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.utils.Log;
import com.example.utils.Updater;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.UpdateFrom;

public class About extends AppCompatActivity {
    private TextView checkupdate;
    private TextView versionname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        checkupdate = (TextView) findViewById(R.id.checkupdate);
        versionname = (TextView)findViewById(R.id.versionname);
        versionname.setText(Updater.getLocalVersionName(this));
        checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag","按了检查更新");

                AppUpdater appUpdater = new AppUpdater(About.this)
                        .setUpdateFrom(UpdateFrom.JSON)
                        .setUpdateJSON("http://121.5.71.186/updata.json")
                        .setButtonUpdateClickListener(new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Updater.clickBtn2(About.this);
                            }
                        })
                        .setTitleOnUpdateNotAvailable("已经是最新版本了")
                        .setButtonDoNotShowAgain(null);
                appUpdater.start();


            }
        });
    }
}