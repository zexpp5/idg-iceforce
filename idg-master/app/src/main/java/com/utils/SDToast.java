/**
 * @title:SDToash.java TODO包含类名列表
 * Copyright (C) oa.cn All right reserved.
 * @version：v3.0,2015年4月6日
 */
package com.utils;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import static com.cxgz.activity.cxim.utils.MainTopMenuUtils.mContext;


/**
 * Toast 提示公类
 *
 * @name SDToast
 * @description
 */
public class SDToast
{
    private static Context context;
    private static Toast mToast;

    public static void init(Context context)
    {
        SDToast.context = context;
    }

    private static Handler mHandler = new Handler();
    private static Runnable r = new Runnable()
    {
        public void run()
        {
            mToast.cancel();
        }
    };

    public static void showShort(String text)
    {
        if (TextUtils.isEmpty(text))
        {
            return;
        }
        mHandler.removeCallbacks(r);
        if (mToast != null)
        {
            mToast.setText(text);
        } else
        {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }
        mHandler.postDelayed(r, 2000);
        mToast.show();
    }

    public static void showLong(String text)
    {
        if (mToast != null)
        {
            mToast.setText(text);
        } else
        {
            mToast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

//    public static void showShort(String text)
//    {
//        if (mToast != null)
//        {
//            mToast.setText(text);
//        } else
//        {
//            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
//        }
//        mToast.setGravity(Gravity.CENTER, 0, 0);
//        mToast.show();
//    }

    public static void showLong(int resId)
    {
        if (mToast != null)
        {
            mToast.setText(context.getResources().getString(resId));
        } else
        {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_LONG);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();

    }

    public static void showShort(int resId)
    {
        if (mToast != null)
        {
            mToast.setText(context.getResources().getString(resId));
        } else
        {
            mToast = Toast.makeText(context, resId, Toast.LENGTH_SHORT);
        }
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mToast.show();
    }

    public static void dismiss()
    {
        mToast.cancel();
    }
}

