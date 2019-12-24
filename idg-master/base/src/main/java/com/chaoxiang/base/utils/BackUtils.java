package com.chaoxiang.base.utils;

import android.app.Activity;
import android.content.Context;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by selson on 2017/9/13.
 */
public class BackUtils
{
    private static long firstTime= 0;

    public static void isBack(Activity activity)
    {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000)
        {
            MyToast.showToast(activity, "再按一次后退键，退出应用程序");
            firstTime = secondTime;
        } else
        {
            System.exit(0);
        }
    }
}
