package com.chaoxiang.base.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author selson
 * @time 2016/3/28
 * 自定义的Toast
 */
public class MyToast {
    private static Toast mToast;
    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable() {
        public void run() {
            mToast.cancel();
        }
    };

    public static void showToast(Context mContext, String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        mHandler.removeCallbacks(r);
        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }
        mHandler.postDelayed(r, 2000);
//        mToast.setGravity(Gravity.TOP, 0, 100);
        mToast.show();
    }

    public static void showToast(Context mContext, int resId) {
        showToast(mContext, mContext.getResources().getString(resId));
    }

    public static void showToastShort(Context mContext, int resId) {
        mHandler.removeCallbacks(r);
        if (mToast != null) {
            mToast.setText(mContext.getResources().getString(resId));
        } else {
            mToast = Toast.makeText(mContext, mContext.getResources().getString(resId), Toast.LENGTH_SHORT);
        }
        mHandler.postDelayed(r, 300);

//        mToast.setGravity(Gravity.TOP, 0, 100);

        mToast.show();
    }


}