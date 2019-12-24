package com.cxgz.activity.cxim.manager;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;

/**
 * 屏幕亮度调节管理器
 *
 * @author zjh
 */
public class ScreenLightManager
{
    private static ScreenLightManager screenLightManager;
    private Activity activity;
    private int initBrighLight;
    private View rootView;
    /**
     * 黑色背景的view
     */
    private View blackView;

    private ScreenLightManager(Activity activity)
    {
        init(activity);
    }


    public static ScreenLightManager getInstance(Activity activity)
    {
        if (screenLightManager == null)
        {
            screenLightManager = new ScreenLightManager(activity);
        }
        return screenLightManager;
    }

    public void init(Activity activity)
    {
        this.activity = activity;
        initBrighLight = getScreenBrightness();
        SDLogUtil.debug("initBrighLight==" + initBrighLight);
        SDLogUtil.debug("isAutoBrightness=" + isAutoBrightness());
        rootView = ((ViewGroup) (activity.findViewById(android.R.id.content))).getChildAt(0);
        blackView = new View(activity);
        blackView.setBackgroundColor(activity.getResources().getColor(R.color.black));
        blackView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                return true;
            }
        });
        blackView.setVisibility(View.GONE);

        if (rootView instanceof LinearLayout)
        {
            ((LinearLayout) rootView).addView(blackView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            SDLogUtil.debug("LinearLayout=========");
        } else if (rootView instanceof RelativeLayout)
        {
            ((RelativeLayout) rootView).addView(blackView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
            SDLogUtil.debug("RelativeLayout=========");
        } else if (rootView instanceof FrameLayout)
        {
            ((FrameLayout) rootView).addView(blackView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            SDLogUtil.debug("FrameLayout=========");
        } else
        {
            SDLogUtil.debug("View=========");
        }
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard()
    {
        if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (activity.getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    /**
     * 恢复屏幕亮度
     */
    public void turnOnScreen()
    {
        if (isAutoBrightness())
        {
            setScreenLight(initBrighLight);
            startAutoBrightness();
        } else
        {
            setScreenLight(initBrighLight);
        }
        SDLogUtil.debug("isAutoBrightness=" + isAutoBrightness());
        blackView.setVisibility(View.GONE);
        SDLogUtil.debug("turnOnScreen");
    }

    /**
     * 关闭屏幕
     */
    public void turnOffScreen()
    {
        blackView.setVisibility(View.VISIBLE);
        hideSoftKeyboard();
        setScreenLight(0);
        SDLogUtil.debug("turnOffScreen");
    }

    /**
     * 设置屏幕亮度
     *
     * @param light
     */
    public void setScreenLight(int light)
    {
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        params.screenBrightness = light / 255.0f;
        activity.getWindow().setAttributes(params);
        saveBrightness(light);
    }

    /**
     * 判断是否开启了自动亮度调节
     */
    public boolean isAutoBrightness()
    {
        if (getScreenMode() == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC)
        {
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * 获得当前屏幕亮度的模式
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    private int getScreenMode()
    {
        int screenMode = 0;
        try
        {
            screenMode = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException)
        {

        }
        return screenMode;
    }

    private ContentResolver getContentResolver()
    {
        return activity.getContentResolver();
    }

    /**
     * 获取屏幕的亮度
     */
    public int getScreenBrightness()
    {
        int nowBrightnessValue = 0;
        try
        {
            nowBrightnessValue = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     * 停止自动亮度调节
     */
    public void stopAutoBrightness()
    {
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        Uri uri = Settings.System
                .getUriFor("screen_brightness");
        getContentResolver().notifyChange(uri, null);
    }

    /**
     * 开启亮度自动调节
     */
    public void startAutoBrightness()
    {
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        Uri uri = Settings.System
                .getUriFor("screen_brightness");
        getContentResolver().notifyChange(uri, null);

    }

    /**
     * 保存亮度设置状态
     */
    public void saveBrightness(int brightness)
    {
        ContentResolver resolver = getContentResolver();
        Uri uri = Settings.System.getUriFor("screen_brightness");
        Settings.System.putInt(resolver, "screen_brightness", brightness);
        // resolver.registerContentObserver(uri, true, myContentObserver);
        resolver.notifyChange(uri, null);
    }

    public void clear()
    {
        if (screenLightManager != null)
        {
            screenLightManager = null;
        }
    }

    public void removeBlackView()
    {
        if (rootView != null)
        {
            if (rootView instanceof LinearLayout)
            {
                ((LinearLayout) rootView).removeView(blackView);
            } else if (rootView instanceof RelativeLayout)
            {
                ((RelativeLayout) rootView).removeView(blackView);
            } else if (rootView instanceof FrameLayout)
            {
                ((FrameLayout) rootView).removeView(blackView);
            }
        }
    }

    public int getInitBrighLight()
    {
        return initBrighLight;
    }
}
