package com.cxgz.activity.cxim.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.BaseApplication;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.dao.DaoSession;
import com.chaoxiang.entity.dao.IMConversationDao;
import com.cxgz.activity.cx.utils.DensityUtil;
import com.cxgz.activity.cx.view.MultiImageSelectorActivity;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.ui.voice.detail.MeetingActivity;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.db.dao.SDUserDao;
import com.entity.VoiceEntity;
import com.facebook.drawee.view.SimpleDraweeView;
import com.http.SDHttpHelper;
import com.injoy.idg.R;
import com.lidroid.xutils.DbUtils;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.superdata.im.utils.Observable.CxAddFriendSubscribe;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;
import com.superdata.im.utils.Observable.CxWorkCircleSubscribe;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.ui.SDLoginActivity;

import com.ui.activity.VoiceVideoActivity;
import com.utils.DialogImUtils;
import com.utils.FileUtil;
import com.utils.SDImgHelper;
import com.utils.StringUtil;
import com.utils_erp.FileDealUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import newProject.view.DialogTextFilter;
import paul.arian.fileselector.FileSelectionActivity;
import tablayout.view.dialog.SelectImgDialog;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;

import static com.injoy.idg.R.id.add_file_btn_detail_img;
import static com.injoy.idg.R.id.add_pic_btn_detail_img;
import static com.injoy.idg.R.id.add_void_btn_detail_img;
import static com.injoy.idg.R.mipmap.icon_public_back;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * @desc
 */
public abstract class BaseActivity extends AppCompatActivity
{
    protected String currentAccount;
    protected String loginUserAccount = "";
    /**
     * 会话dao
     */
    protected IMConversationDao conversionDao;
    /**
     * 软键盘manager
     */
    protected InputMethodManager inputMethodManager;

    protected ProgressDialog loadingDialog;

    protected static long lastClickTime;
    protected TextView tvTitle;
    private LinearLayout llLeft;
    public LinearLayout llRight;
    protected LinearLayout titles;
    protected SDGson mGson;
    private int mScreenWidth;
    protected DaoSession daoSession;
    protected String userId;

    protected SDUserDao userDao;
    protected DbUtils mDbUtils;

    protected SDHttpHelper mHttpHelper;

    //添加好友 后期优化。进入主界面时候，获取到新添加的申请好友请求的东西。通过数据库字段。
    private CxAddFriendSubscribe cxAddFriendSubscribe;

    private CxWorkCircleSubscribe cxWorkCircleSubscribe;

    @Override
    protected void onStart()
    {
        super.onStart();
        BaseApplication.getInstance().addActivity(BaseActivity.this);
        hideSoftKeyboard();
    }

    /**
     * 如果为false不继续往下执行下去
     * @return
     */
    protected boolean beforeOnCreate()
    {
        return true;
    }

    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();

    /**
     * onCreate 执行初始化
     */
    protected abstract void init();

    protected void init(Bundle savedInstanceState)
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        if (!beforeOnCreate())
        {
            return;
        }

        if (getContentLayout() != 0)
        {
            setContentView(getContentLayout());
        }

        currentAccount = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
        userId = (String) SPUtils.get(this, com.utils.SPUtils.USER_ID, "");

        loginUserAccount = DisplayUtil.getUserInfo(this, 11);

        mDbUtils = SDDbHelp.createDbUtils(BaseActivity.this);
        mDbUtils.configDebug(SDLogUtil.FLAG);

        mHttpHelper = new SDHttpHelper(BaseActivity.this);
        userDao = new SDUserDao(BaseActivity.this);

        if (!isTaskRoot())
        {
            Intent intent = getIntent();
            String action = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && action.equals(Intent.ACTION_MAIN))
            {
                finish();
                return;
            }
        }

        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        mGson = new SDGson();

        tvTitle = (TextView) findViewById(R.id.tvtTopTitle);
        llLeft = (LinearLayout) findViewById(R.id.llLeft);
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);
        titles = (LinearLayout) findViewById(R.id.titles);
        loadingDialog = new ProgressDialog(this);

        init();
        init(savedInstanceState);

        initDao();

        //好友订阅
        cxAddFriendSubscribe = new CxAddFriendSubscribe(new CxAddFriendSubscribe.AddFriendListener()
        {
            @Override
            public void acceptAddFriendInfo(int addFriendStatus)
            {
                if (addFriendStatus == 2)
                {
                    acceptFriendInfo();
                } else if (addFriendStatus == 1)
                {
                    //刷新通讯录
                    notityRefreshContact();
                } else if (addFriendStatus == 3)
                {
                    setInVisableFriendInfo();
                }
            }
        });

        cxWorkCircleSubscribe = new CxWorkCircleSubscribe(new CxWorkCircleSubscribe.WorkCircleListener()
        {
            @Override
            public void acceptWorkCircleInfo(int workCircleStatus)
            {
                if (workCircleStatus == 1)
                {
                    updateWorkCircle();
                }
            }
        });

        CxWorkCircleObservale.getInstance().addObserver(cxWorkCircleSubscribe);
        CxAddFriendObservale.getInstance().addObserver(cxAddFriendSubscribe);

    }

    /**
     * 通知刷新通讯录
     */
    protected void notityRefreshContact()
    {
//        HttpHelpEstablist.getInstance().refreshContact(this, userDao);
    }

    /**
     * 工作圈
     */
    protected void updateWorkCircle()
    {

    }

    protected void acceptFriendInfo()
    {

    }

    protected void setInVisableFriendInfo()
    {

    }

    protected void setViewShow(View view, boolean isShow)
    {
        if (isShow)
        {
            view.setVisibility(View.VISIBLE);
        } else
        {
            view.setVisibility(View.GONE);
        }
    }

    protected void showLoadingDialog(String title, String msg)
    {
        loadingDialog.setMessage(msg);
        loadingDialog.setTitle(title);
        loadingDialog.show();
    }

    public void setBarBackGround(int color)
    {
        if (titles != null)
        {
            titles.setBackgroundColor(color);
        }
    }

    protected void dismissLoadingDialog()
    {
        if (loadingDialog != null && loadingDialog.isShowing())
        {
            loadingDialog.dismiss();
        }
    }

    protected void setTitle(String text)
    {
        tvTitle.setText(text);
        tvTitle.setTextColor(this.getResources().getColor(R.color.white));
    }

    protected void setTitle(String text, int resId)
    {
        tvTitle.setText(text);
        tvTitle.setTextColor(this.getResources().getColor(R.color.white));
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumWidth());
        tvTitle.setCompoundDrawables(null, null, drawable, null);
    }

    /**
     * 设置标题左边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected void addLeftBtn(String msg, View.OnClickListener listener)
    {
        Button LeftBtn = new Button(this);
        LeftBtn.setText(msg);
        LeftBtn.setTextColor(Color.WHITE);
        LeftBtn.setTextSize(14);
//        LeftBtn.setBackgroundResource(R.drawable.tab_right_bg);
        LeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LeftBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        LeftBtn.setOnClickListener(listener);
        llLeft.addView(LeftBtn);
    }

    /**
     * 设置标题左边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected void addLeftBtn(int resId, View.OnClickListener listener)
    {   
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        imgBtn.setOnClickListener(listener);
        llLeft.addView(imgBtn, 0);
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) imgBtn.getLayoutParams();
//        lp.height = DisplayUtil.dp2px(this, 36);
//        lp.width = DisplayUtil.dp2px(this, 36);
//        imgBtn.setLayoutParams(lp);
    }

    protected void addLogo()
    {
        ImageButton imgLeftBtn = new ImageButton(this);
        imgLeftBtn.setImageResource(R.mipmap.logo);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
//        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout
// .LayoutParams.MATCH_PARENT));
        llLeft.addView(imgLeftBtn);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected void addRightBtn(String msg, View.OnClickListener listener)
    {
        Button Btn = new Button(this);
        Btn.setText(msg);
        Btn.setTextColor(Color.WHITE);
        Btn.setTextSize(14);
        Btn.setBackgroundColor(getResources().getColor(R.color.transparency));
        Btn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        Btn.setOnClickListener(listener);
        llRight.addView(Btn, 0);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected void addRightBtn(int resId, View.OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material),
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        imgBtn.setLayoutParams(lp);
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected void addRightBtn2(int resId, View.OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ScreenUtils.dp2px(this, 45),
                ScreenUtils.dp2px(this, 45));
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        lp.rightMargin = ScreenUtils.dp2px(this, 10);
        imgBtn.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imgBtn.setLayoutParams(lp);
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
    }

    /**
     * 设置返回按钮
     *
     * @param msg
     */
    protected void setLeftBack(String msg)
    {
        addLeftBtn(msg, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    /**
     * 设置返回按钮-资源文件
     */
    protected void setLeftBack(int resId)
    {
        addLeftBtn(resId, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BaseActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        CxAddFriendObservale.getInstance().deleteObserver(cxAddFriendSubscribe);
        CxWorkCircleObservale.getInstance().deleteObserver(cxWorkCircleSubscribe);

        BaseApplication.getInstance().removeActivity(this);
    }

    protected void initDao()
    {
        if (this.getClass().getName().equals(SDLoginActivity.class.getName()))
        {
            //数据库是在登录完成后初始化的,所有必须先登录才能初始化dao
            return;
        }

        daoSession = IMDaoManager.getInstance().getDaoSession();

        if (daoSession != null)
        {
            conversionDao = daoSession.getIMConversationDao();
        }
    }

    public void showToast(String msg)
    {
        MyToast.showToast(this, msg);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard()
    {
        if (getCurrentFocus() != null)
        {
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示软键盘
     */
    public void showSoftKeyBoard()
    {
        if (isShowSoftKeyBoard())
        {
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public boolean isShowSoftKeyBoard()
    {
        return inputMethodManager.isActive();
    }

    /**
     * 注销广播
     *
     * @param broadcastReceiver
     */
    public void unregisterSDReceiver(BroadcastReceiver broadcastReceiver)
    {
        if (broadcastReceiver != null)
        {
            unregisterReceiver(broadcastReceiver);
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void back(View view)
    {
        finish();
    }

    public void openActivity(Class<? extends Activity> clazz)
    {
        openActivity(clazz, null);
    }

    public void openActivity(Class<? extends Activity> clazz, Bundle bundle)
    {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivityForResult(Class<? extends Activity> clazz, int requestCode)
    {
        openActivityForResult(clazz, null, requestCode);
    }

    public void openActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode)
    {
        Intent intent = new Intent(this, clazz);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    //权限
    //**************** Android M Permission (Android 6.0权限控制代码封装)**********************//
    private int permissionRequestCode = 88;
    private PermissionCallback permissionRunnable;

    public interface PermissionCallback
    {
        void hasPermission();

        void noPermission();
    }

    /**
     * Android M运行时权限请求封装
     *
     * @param permissionDes 权限描述
     * @param runnable      请求权限回调
     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String...
            permissions)
    {
        if (permissions == null || permissions.length == 0) return;
//        this.permissionrequestCode = requestCode;
        this.permissionRunnable = runnable;
        if ((Build.VERSION.SDK_INT < 23) || checkPermissionGranted(permissions))
        {
            if (permissionRunnable != null)
            {
                permissionRunnable.hasPermission();
                permissionRunnable = null;
            }
        } else
        {
            //permission has not been granted.
            requestPermission(permissionDes, permissionRequestCode, permissions);
        }

    }

    private boolean checkPermissionGranted(String[] permissions)
    {
        boolean flag = true;
        for (String p : permissions)
        {
            if (ActivityCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED)
            {
                flag = false;
                break;
            }
        }
        return flag;
    }

    private void requestPermission(String permissionDes, final int requestCode, final String[] permissions)
    {
        if (shouldShowRequestPermissionRationale(permissions))
        {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

//            Snackbar.make(getWindow().getDecorView(), requestName,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.common_ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(BaseAppCompatActivity.this,
//                                    permissions,
//                                    requestCode);
//                        }
//                    })
//                    .show();
            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(permissionDes)
                    .setPositiveButton("允许", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
                        }
                    }).show();

        } else
        {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions)
    {
        boolean flag = false;
        for (String p : permissions)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p))
            {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        if (requestCode == permissionRequestCode)
        {
            if (verifyPermissions(grantResults))
            {
                if (permissionRunnable != null)
                {
                    permissionRunnable.hasPermission();
                    permissionRunnable = null;
                }
            } else
            {
                showToast("暂无权限执行相关操作！");
                if (permissionRunnable != null)
                {
                    permissionRunnable.noPermission();
                    permissionRunnable = null;
                }
            }
        } else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults)
    {
        // At least one result must be checked.
        if (grantResults.length < 1)
        {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults)
        {
            if (result != PackageManager.PERMISSION_GRANTED)
            {
                return false;
            }
        }
        return true;
    }

    //********************** END Android M Permission ****************************************
    protected void finishMeeting()
    {

    }
}

