package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.function.openpersonsnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivityfortizhong extends AppCompatActivity {
    private Button buttontijiao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityfortizhong);
        buttontijiao=(Button)findViewById(R.id.buttontijiao);
        buttontijiao.setOnClickListener(new MyClick());

    }
    private class MyClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            EditText tz = (EditText) findViewById(R.id.test_input);
            TextView sc = (TextView) findViewById(R.id.test_output);
            int tizhong = Integer.parseInt(tz.getText().toString());
            int temp = tizhong%100;
            if (temp==tizhong){
                sc.setText("你的体重小于100斤,很轻了哦，仙女姐姐~");
            }
            else{
                sc.setText("你的体重在"+(tizhong-temp)+"斤左右");
            }




        }
    }

}