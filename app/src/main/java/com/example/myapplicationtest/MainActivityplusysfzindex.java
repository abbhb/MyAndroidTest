package com.example.myapplicationtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivityplusysfzindex extends AppCompatActivity {
    private WebView myWebView;
    private long exitTime = 0;
    private String cookie;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activityplusysfzindex);
//        WebView webView = (WebView) findViewById(R.id.ysfuzhu);
//        WebSettings webSettings = webView.getSettings();
//        webSettings.setJavaScriptEnabled(true);//启用js
////        webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//解决视频无法播放问题
//        webView.setWebChromeClient(new WebChromeClient());
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("https://m.bbs.mihoyo.com/ys/#/login");//覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
//        webView.setWebViewClient(new WebViewClient() {
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//                view.loadUrl(request.getUrl().toString());
//                return true;
//            }
//        });
        myWebView = (WebView) findViewById(R.id.ysfuzhu);
        myWebView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        myWebView.loadUrl("https://bbs.mihoyo.com/ys/accountCenter/postList");    //设置网址
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                CookieManager cookieManager = CookieManager.getInstance();
                String CookieStr = cookieManager.getCookie(url);

                Log.d("papattt", CookieStr);
                if (CookieStr.indexOf("cookie_token")!=-1) {
                    Toast.makeText(MainActivityplusysfzindex.this, "已获取cookie", Toast.LENGTH_SHORT).show();
                    cookie = CookieStr;
                    SharedPreferences user = getSharedPreferences("user", 0);
                    SharedPreferences.Editor editor = user.edit();
                    editor.putString("cookie",cookie);
                    editor.commit();







                } else {
                    Toast.makeText(MainActivityplusysfzindex.this, "不存在cookie", Toast.LENGTH_SHORT).show();
                }


                super.onPageFinished(view, url);
            }
        });

        WebSettings webSettings = myWebView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAllowFileAccess(true);
        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.TEXT_AUTOSIZING); //支持内容重新布局
        webSettings.setDatabaseEnabled(true);
        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
        webSettings.setTextZoom(2);//设置文本的缩放倍数，默认为 100
        webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        webSettings.setStandardFontFamily("");//设置 WebView 的字体，默认字体为 "sans-serif"
        webSettings.setDefaultFontSize(20);//设置 WebView 字体的大小，默认大小为 16
        webSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        Button jsbutton = (Button) findViewById(R.id.jsbutton);
        jsbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d("clicklist","点击了");
                if(cookie.equals("")){
                    Toast.makeText(MainActivityplusysfzindex.this, "没有cookie", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivityplusysfzindex.this,yuanshenlist.class);
                intent.putExtra("cookie",cookie);
                startActivity(intent);
                finish();
            };
        });
    }
}