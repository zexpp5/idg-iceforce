package com.cxgz.activity.cxim.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.injoy.idg.R;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.http.SDHttpHelper;
import com.lidroid.xutils.DbUtils;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.superdata.im.utils.Observable.CxAddFriendSubscribe;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;
import com.superdata.im.utils.Observable.CxWorkCircleSubscribe;
import com.superdata.marketing.view.percent.PercentLinearLayout;
import com.utils.SPUtils;

import yunjing.utils.DisplayUtil;

/**
 * fg鸡肋
 */
public abstract class BaseFragment extends Fragment implements OnClickListener
{
    protected TextView tvTitle;
    protected LinearLayout llLeft;
    protected LinearLayout llRight;
    protected PercentLinearLayout titles;

    protected String userId;

    protected String loginUserName = "";
    //用户类型
    protected int userType;

    private int textSize = 14;

    protected DbUtils mDbUtils;
    protected SDUserDao mUserDao;
    protected SDDepartmentDao mDepartmentDao;

    //添加好友
    private CxAddFriendSubscribe cxAddFriendSubscribe;
    private CxWorkCircleSubscribe cxWorkCircleSubscribe;

    protected SDHttpHelper mHttpHelper;

    protected void setTitle(String text)
    {
        tvTitle.setText(text);
        tvTitle.setTextColor(this.getResources().getColor(R.color.white));
    }

    protected void setTitle(int resId)
    {
        tvTitle.setText(getResources().getString(resId));
        tvTitle.setTextColor(this.getResources().getColor(R.color.white));
    }

    /**
     * @return 当前的布局资源文件
     */
    protected abstract int getContentLayout();

    protected abstract void init(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        setBeforeOnCreate();
        View view = inflater.inflate(getContentLayout(), container, false);
        tvTitle = (TextView) view.findViewById(R.id.tvtTopTitle);
        llLeft = (LinearLayout) view.findViewById(R.id.llLeft);
        llRight = (LinearLayout) view.findViewById(R.id.ll_bottom_page_left);
        titles = (PercentLinearLayout) view.findViewById(R.id.titles);

        loginUserName = DisplayUtil.getUserInfo(getActivity(), 11);

        init(view);

        return view;
    }

    protected void setBeforeOnCreate()
    {

    }

    protected void addLogo()
    {
        ImageButton imgLeftBtn = new ImageButton(getActivity());
        imgLeftBtn.setImageResource(R.mipmap.logo);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        llLeft.addView(imgLeftBtn);
    }

    /**
     * @param resId
     * @param listener
     * @return 设置左边资源文件按钮
     */
    protected void addLeftBtn(int resId, OnClickListener listener)
    {
        ImageButton imgLeftBtn = new ImageButton(getActivity());
        imgLeftBtn.setImageResource(resId);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material),
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        imgLeftBtn.setLayoutParams(lp);
        imgLeftBtn.setOnClickListener(listener);
        llLeft.addView(imgLeftBtn);
    }

    /**
     * 设置标题左边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected void addLeftBtn(String msg, OnClickListener listener)
    {
        Button LeftBtn = new Button(getActivity());
        LeftBtn.setText(msg);
        LeftBtn.setTextColor(Color.WHITE);
        LeftBtn.setTextSize(textSize);
//        LeftBtn.setBackgroundResource(R.color.transparency);
        LeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams
                .MATCH_PARENT));
        LeftBtn.setOnClickListener(listener);
        llLeft.addView(LeftBtn);
    }


    /**
     * 设置标题右边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected void addRightBtn(String msg, OnClickListener listener)
    {
        Button Btn = new Button(getActivity());
        Btn.setText(msg);
        Btn.setTextSize(textSize);
        Btn.setTextColor(Color.WHITE);
//        Btn.setBackgroundResource(R.color.transparency);
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
    protected void addRightBtn(int resId, OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(getActivity());
        imgBtn.setImageResource(resId);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material),
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;
        imgBtn.setLayoutParams(lp);
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
    }

    protected void addRightBtn2(int resId, OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(getActivity());
        imgBtn.setImageResource(resId);
//        imgBtn.setBackgroundResource(null);
        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen
                .abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
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
        addLeftBtn(msg, new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });
    }

    ProgressDialog progresDialog = null;

    protected void showProgress(String content)
    {
        if (progresDialog == null)
        {
            progresDialog = new ProgressDialog(getActivity());
            progresDialog.setCanceledOnTouchOutside(false);
            progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
            {
                @Override
                public void onCancel(DialogInterface dialog)
                {
//                upload.cancel();
                }
            });
        }
        if (progresDialog.isShowing())
        {
            progresDialog.dismiss();
        }
        progresDialog.setMessage(content);
        progresDialog.show();
    }

    protected void dismissProgress()
    {
        if (progresDialog != null && progresDialog.isShowing())
            progresDialog.dismiss();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mDbUtils = SDDbHelp.createDbUtils(getActivity());
        mHttpHelper = new SDHttpHelper(getActivity());
        mUserDao = new SDUserDao(getActivity());
        mDepartmentDao = new SDDepartmentDao(getActivity());

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "-1");
        userType = (int) SPUtils.get(getActivity(), SPUtils.USER_TYPE, 1);

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
        //创建观察者对象
        CxAddFriendObservale.getInstance().addObserver(cxAddFriendSubscribe);
    }

    /**
     * 通知刷新通讯录
     */
    protected void notityRefreshContact()
    {
//        HttpHelpEstablist.getInstance().refreshContact(getActivity(), mUserDao);
    }

    protected void updateWorkCircle()
    {

    }

    /**
     * 添加好友的观察模式回调
     */
    protected void acceptFriendInfo()
    {

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

    public void setBarBackGround(int color)
    {
        if (titles != null)
        {
            titles.setBackgroundColor(color);
        }
    }

    protected void openActivity(Class<? extends Activity> clazz)
    {
        openActivity(clazz, null);
    }

    protected void openActivity(Class<? extends Activity> clazz, Bundle bundle)
    {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    protected void openActivityForResult(Class<? extends Activity> clazz, int requestCode)
    {
        openActivityForResult(clazz, requestCode, null);
    }

    protected void openActivityForResult(Class<? extends Activity> clazz, int requestCode, Bundle bundle)
    {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent, requestCode);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        CxAddFriendObservale.getInstance().deleteObserver(cxAddFriendSubscribe);
        CxWorkCircleObservale.getInstance().deleteObserver(cxWorkCircleSubscribe);
    }
}
