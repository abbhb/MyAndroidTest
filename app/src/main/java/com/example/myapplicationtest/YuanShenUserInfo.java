package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class YuanShenUserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yuan_shen_user_info);
        SharedPreferences useraaa = getSharedPreferences("user",0);
        String cookie =useraaa.getString("cookie","");
        String uid = useraaa.getString("uid","");
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//注意要清除 FLAG_TRANSLUCENT_STATUS flag
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        getWindow().setStatusBarColor(getResources().getColor(R.color.wltsztlcolor));
        WebView webView = (WebView) findViewById(R.id.yuanshenuserinfowebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);//启用js
        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决视频无法播放问题
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.setDownloadListener(new MyDownLoadListener(this));
        webView.loadUrl("http://121.5.71.186/dist");//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.loadUrl("javascript:fpp('" + uid + "')");
//                webView.loadUrl("javascript:foo('" + cookie + "')");
//                webView.loadUrl( "javascript:window.location.reload( true )" );
                Log.d("JavaScriptObject",cookie);
                String javastr = "javascript:fpp(\""+uid+"\");";
                String javastruid = "javascript:foo(\""+cookie+"\");";
                view.evaluateJavascript(javastr, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
                view.evaluateJavascript(javastruid, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                    }
                });
            }
        });
    }
}