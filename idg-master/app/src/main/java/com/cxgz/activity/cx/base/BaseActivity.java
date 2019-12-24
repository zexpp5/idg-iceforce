package com.cxgz.activity.cx.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.db.dao.PushUnreadDao;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.cxgz.activity.db.entity.UnreadEntity;
import com.http.SDHttpHelper;
import com.injoy.idg.R;
import com.lidroid.xutils.DbUtils;
import yunjing.processor.UnreadObservale;
import yunjing.processor.UnreadObserver;
import com.utils.SPUtils;


/**
 * 父activity
 */
public abstract class BaseActivity extends BusinessBaseActivity implements OnClickListener, UnreadObserver.UnreadChangeListener
{
    protected TextView tvTitle;

    private LinearLayout llLeft;
    public LinearLayout llRight;

    protected String userName;
    protected String userId;
    protected long companyId;
    protected ProgressBar pb;

    protected static long lastClickTime;
    protected int annexWay;
    protected int userType;

    protected SDDepartmentDao departmentDao;
    protected SDUserDao userDao;
    protected DbUtils mDbUtils;
    protected SDGson mGson;
    protected SDHttpHelper mHttpHelper;

    /**
     * 软键盘manager
     */
    protected InputMethodManager inputMethodManager;
    protected int mScreenWidth;

    private int testSize = 14;

    protected PushUnreadDao unreadDao;
    protected UnreadObserver unreadObserver;
    protected SDDepartmentDao mDepartmentDao;

    protected SDUserDao mUserDao;
    protected float dialogLitterFontSize;
    protected float litterFontSize;

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    /**
     * onCreate 执行初始化
     */
    protected abstract void init();

    protected void initBeforeCreateView()
    {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() == MotionEvent.ACTION_DOWN)
        {
            if (isFastDoubleClick())
            {
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public static boolean isFastDoubleClick()
    {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (timeD >= 0 && timeD <= 250)
        {
            return true;
        } else
        {
            lastClickTime = time;
            return false;
        }
    }

    public String getToken()
    {
        return SPUtils.get(this, SPUtils.ACCESS_TOKEN, "").toString();
    }

    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();

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

    protected void setToolbar()
    {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null)
        {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setElevation(25);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        hideSoftKeyboard();
    }

    /**
     * 设置标题左边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected void addLeftBtn(String msg, OnClickListener listener)
    {
        Button LeftBtn = new Button(this);
        LeftBtn.setText(msg);
        LeftBtn.setTextColor(Color.WHITE);
        LeftBtn.setTextSize(testSize);
//        LeftBtn.setBackgroundResource(R.drawable.tab_right_bg);
        LeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LeftBtn.setOnClickListener(listener);
        llLeft.addView(LeftBtn);
    }

    protected void addLogo()
    {
        ImageButton imgLeftBtn = new ImageButton(this);
        imgLeftBtn.setImageResource(R.mipmap.talking_talking);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imgLeftBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BaseActivity.this, SuperMainActivity.class);
                startActivity(intent);
            }
        });
        llRight.addView(imgLeftBtn);
    }

    /**
     * 初始化黄色聊一聊
     */
    protected void addSettingYellowLogo()
    {
        ImageButton imgLeftBtn = new ImageButton(this);
        imgLeftBtn.setImageResource(R.mipmap.talking_yellow_talking);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imgLeftBtn.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(BaseActivity.this, SuperMainActivity.class);
                startActivity(intent);
            }
        });
        llRight.addView(imgLeftBtn);
    }

    /**
     * 设置标题右边的按钮
     * @param msg      文字
     * @param listener 点击事件
     */
    protected Button addRightBtn(String msg, OnClickListener listener)
    {
        Button Btn = new Button(this);
        Btn.setText(msg);
        Btn.setTextColor(Color.WHITE);
        Btn.setTextSize(testSize);
        Btn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LinearLayout.LayoutParams linearParams =
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
        linearParams.gravity = Gravity.RIGHT | Gravity.CENTER;
        Btn.setLayoutParams(linearParams);
        Btn.setOnClickListener(listener);
        llRight.addView(Btn, 0);
        return Btn;
    }


    /**
     * 设置返回按钮
     *
     * @param msg
     */
    protected void setLeftBack(String msg)
    {
        addLeftBtn(msg, new OnClickListener()
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
        addLeftBtn(resId, new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                BaseActivity.super.onBackPressed();
            }
        });

    }

    /**
     * 设置标题左边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected void addLeftBtn(int resId, OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setImageResource(resId);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        imgBtn.setOnClickListener(listener);
        llLeft.addView(imgBtn, 0);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected ImageButton addRightBtn(int resId, OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
        return imgBtn;
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected ImageButton addRightBtn_Im(int resId, OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(
                48, LinearLayout.LayoutParams.MATCH_PARENT));
        LinearLayout.LayoutParams linearParams =
                (LinearLayout.LayoutParams) imgBtn.getLayoutParams(); //取控件当前的布局参数

        int tmpHeight = ScreenUtils.dp2px(BaseActivity.this, 30);

        linearParams.setMargins(0, 0, 10, 0);

        linearParams.height = tmpHeight;
        linearParams.width = tmpHeight;

        imgBtn.setLayoutParams(linearParams);
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
        return imgBtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        initBeforeCreateView();

        mGson = new SDGson();
        mHttpHelper = new SDHttpHelper(BaseActivity.this);

        departmentDao = new SDDepartmentDao(BaseActivity.this);
        userDao = new SDUserDao(BaseActivity.this);

        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        dialogLitterFontSize = mScreenWidth * 0.011f;
        litterFontSize = mScreenWidth * 0.011f;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        if (!beforeOnCreate())
        {
            return;
        }
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        
        //if (getContentLayout() != 0) {
        setContentView(getContentLayout());
        //}
        tvTitle = (TextView) findViewById(R.id.tvtTopTitle);
//        title_search_icon = (ImageView) findViewById(R.id.title_search_icon);
        llLeft = (LinearLayout) findViewById(R.id.llLeft);
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);

//        ativice_img = (ImageView) findViewById(R.id.activice_img);//广告
//        ativice_img_colse = (ImageView) findViewById(activice_img_cose);//关闭R.id.

//        example_account_layout = (RelativeLayout) findViewById(R.id.example_account_layout);//例子帐

        pb = (ProgressBar) findViewById(R.id.top_pb);
        if (pb != null)
        {
            pb.setMax(100);
        }
        mUserDao = new SDUserDao(this);
        mDbUtils = SDDbHelp.createDbUtils(BaseActivity.this);

        mDepartmentDao = new SDDepartmentDao(this);
        unreadDao = new PushUnreadDao(this);

        //创建观察者对象
        unreadObserver = new UnreadObserver(this);
        //注册观察者
        UnreadObservale.getInstance().addObserver(unreadObserver);

        init(savedInstanceState);
        init();
//        setToolbar();
    }

    protected void init(Bundle savedInstanceState)
    {
    }

    /**
     * 如果为false不继续往下执行下去
     *
     * @return
     */
    protected boolean beforeOnCreate()
    {
        return true;
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard()
    {
        if (getCurrentFocus() != null)
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE)
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
        if (isShowSoftKeyBoard())
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE)
            {
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public boolean isShowSoftKeyBoard()
    {
        return inputMethodManager.isActive();
    }

    int startX;
    int startY;
    boolean isMove;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                isMove = false;
                startX = (int) event.getX();
                startY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) event.getX();
                int endY = (int) event.getY();
                int distance = (int) Math.sqrt(Math.pow((startX - endX), 2) + Math.pow((startY - endY), 2));
                if (distance > 10)
                {
                    isMove = true;
                } else
                {
                    isMove = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMove)
                {
                    hideSoftKeyboard();
                }
                break;
        }
        return false;
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

    public void showToast(String msg)
    {
        MyToast.showToast(this, msg);
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

    @Override
    public void unreadChange(UnreadEntity unreadEntity)
    {

    }

}
