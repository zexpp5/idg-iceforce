package com.chaoxiang.base.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.ViewConfiguration;
import android.view.WindowManager;

/**
 * User: Selson
 * Date: 2016-05-17
 * FIXME
 */
public class ScreenUtils
{
    /**
     * 返回屏幕的宽高
     */
    public static ScreenBean getScreen(Context context)
    {
        ScreenBean info = new ScreenBean();
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        info.setScreenWidth(dm.widthPixels);
        info.setScreenHeigh(dm.heightPixels);
        return info;
    }

    /**
     * 获得屏幕高度
     */
    public static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 获得屏幕宽度
     */
    public static int getScreenHeight(Context context)
    {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * @param mContext
     * @param dp       把dp转换为px
     * @return
     */
    public static int dp2px(Context mContext, float dp)
    {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param mContext
     * @param sp       把sp转换为px
     * @return
     */
    public static int sp2px(Context mContext, float sp)
    {
        final float scale = mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    /**
     * @param context
     * @param px      把px转换为dp
     * @return
     */
    public static int px2dp(Context context, float px)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static float px2sp(Context context, float pxValue)
    {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (pxValue / fontScale);
    }

    public static boolean checkDeviceHasNavigationBar(Context context)
    {

        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = false;
        boolean hasBackKey = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE)
        {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            {
                hasMenuKey = ViewConfiguration.get(context)
                        .hasPermanentMenuKey();
            }
            hasBackKey = KeyCharacterMap
                    .deviceHasKey(KeyEvent.KEYCODE_BACK);
        }

        if (!hasMenuKey && !hasBackKey)
        {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

    static class ScreenBean
    {
        private int screenHeigh;
        private int screenWidth;

        public int getScreenHeigh()
        {
            return screenHeigh;
        }

        public void setScreenHeigh(int screenHeigh)
        {
            this.screenHeigh = screenHeigh;
        }

        public int getScreenWidth()
        {
            return screenWidth;
        }

        public void setScreenWidth(int screenWidth)
        {
            this.screenWidth = screenWidth;
        }
    }
} 