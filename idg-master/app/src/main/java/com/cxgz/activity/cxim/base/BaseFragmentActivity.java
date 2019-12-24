package com.cxgz.activity.cxim.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.base.BaseApplication;
import com.chaoxiang.base.utils.SPUtils;
import com.injoy.idg.R;
import com.superdata.im.constants.CxSPIMKey;

/**
 * 公用的
 */
public abstract class BaseFragmentActivity extends AppCompatActivity
{
    private static final String TAG = "BaseFragmentActivity";

    protected String currentAccount;
    /**
     * 软键盘manager
     */
    protected InputMethodManager inputMethodManager;
    protected Fragment fragment;
    protected FragmentManager fm;

    @Override
    protected void onStart()
    {
        super.onStart();
    }



    /**
     * onCreate 执行初始化
     */
    protected void init()
    {

    }

    protected void init(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        BaseApplication.getInstance().addActivity(this);
        if (!beforeOnCreate())
        {
            return;
        }
        setContentView(getContentLayout());

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        init(savedInstanceState);
        init();

        currentAccount = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
    }


    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();

    /**
     * 如果为false不继续往下执行下去
     *
     * @return
     */
    protected boolean beforeOnCreate()
    {
        return true;
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard()
    {
        if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        {
            if (getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }

    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyBoard()
    {
        if (inputMethodManager.isActive())
        {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 打开Activity
     *
     * @param activity
     * @param requestCode
     */
    public void openActivityForResult(Class<? extends Activity> activity, int requestCode)
    {
        openActivityForResult(activity, null, requestCode);
    }

    /**
     * 打开Activity,带Bundle参数
     *
     * @param activity
     * @param bundle
     * @param requestCode
     */
    public void openActivityForResult(Class<? extends Activity> activity, Bundle bundle, int requestCode)
    {
        Intent intent = new Intent(this, activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 打开Activity
     *
     * @param activity
     */
    public void openActivity(Class<? extends Activity> activity)
    {
        openActivity(activity, null);
    }

    /**
     * 打开Activity,带Bundle参数
     * @param activity
     * @param bundle
     */
    public void openActivity(Class<? extends Activity> activity, Bundle bundle)
    {
        Intent intent = new Intent(this, activity);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        //销毁观察者
        BaseApplication.getInstance().removeActivity(this);
    }

    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment)
    {
        this.fragment = fragment;
        if (fragment == null)
        {
            return;
        }
        if (fm == null)
        {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_content, fragment);
        ft.commit();
    }

    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment, String stackName)
    {
        this.fragment = fragment;
        if (fragment == null)
        {
            return;
        }
        if (fm == null)
        {
            fm = getSupportFragmentManager();
        }
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frag_content, fragment);
        ft.addToBackStack(stackName);
        ft.commit();
    }
}
