package com.example.myapplicationtest;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

import com.example.utils.Log;

public class MyDownLoadListener implements DownloadListener {
    private Context mContext;

    public MyDownLoadListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        Uri uri = Uri.parse(url);
        Log.d("JavaScriptObject",url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        mContext.startActivity(intent);
    }

}
