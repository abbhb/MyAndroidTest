package com.example.View;


import android.app.Activity;
import android.app.ProgressDialog;

public class MaskUtil {

    /**
     * 遮罩层
     * @param message 遮罩层的文字显示
     * @param mActivity 使用的activity
     */
    public static ProgressDialog showProgressDialog(String message, Activity mActivity) {
        ProgressDialog mProgressDialog = null;
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setMessage(message);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }
}

