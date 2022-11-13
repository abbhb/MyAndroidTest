package com.example.myapplicationtest.test;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.myapplicationtest.R;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;

public class TestActivity extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        webView = (WebView) findViewById(R.id.yuanshenckupwebssss);
//        webView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
//        webView.getSettings().setSupportZoom(true); //可以缩放
        webView.getSettings().setDomStorageEnabled(true);//开启DOM
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");//h5地址
        webView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                webView.evaluateJavascript("javascript:jsFunc('" + "sadwfwfewfefwef" + "')", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        // 如果不需要JS返回数据，该回调方法参数可以写成null
                    }
                });
                // 这么写
                /// mWebView.evaluateJavascript("javascript:jsFunc('" + msg + "')", null);
            }
        });
    }
}