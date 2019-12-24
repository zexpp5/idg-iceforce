package com.cxgz.activity.cx.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.http.SDHttpHelper;
import com.lidroid.xutils.DbUtils;
import com.utils.MyPreferences;
import com.utils.SPUtils;

/**
 * 父Fragment-cx
 */
public abstract class BaseFragment extends Fragment implements OnClickListener
{
    protected TextView tvTitle;
    protected LinearLayout llLeft;
    protected LinearLayout llRight;
    protected DbUtils mDbUtils;
    protected SDHttpHelper mHttpHelper;
    protected SDUserDao mUserDao;
    protected SDDepartmentDao mDepartmentDao;
    protected String userId;
    protected String compannyId;
    private int mScreenWidth;
    protected int userType;

    protected void setTitle(String text)
    {
        tvTitle.setText(text);
        tvTitle.setTextColor(this.getResources().getColor(R.color.white));
    }

    protected void addLogo()
    {
        ImageButton imgLeftBtn = new ImageButton(getActivity());
        imgLeftBtn.setImageResource(R.mipmap.logo);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
//        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llLeft.addView(imgLeftBtn);
    }

    protected ImageButton addLeftBtn(int resId, OnClickListener listener)
    {
        ImageButton imgLeftBtn = new ImageButton(getActivity());
        imgLeftBtn.setImageResource(resId);
        imgLeftBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        imgLeftBtn.setOnClickListener(listener);
        llLeft.addView(imgLeftBtn);
        return imgLeftBtn;
    }

    /**
     * 设置标题左边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected Button addLeftBtn(String msg, OnClickListener listener)
    {
        Button LeftBtn = new Button(getActivity());
        LeftBtn.setText(msg);
        LeftBtn.setTextColor(Color.WHITE);
        LeftBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mScreenWidth * 0.05f);
        LeftBtn.setBackgroundResource(R.drawable.tab_right_bg);
        LeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LeftBtn.setOnClickListener(listener);
        llLeft.addView(LeftBtn);
        return LeftBtn;
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
        Btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mScreenWidth * 0.05f);
        Btn.setTextColor(Color.WHITE);
        Btn.setBackgroundResource(R.drawable.tab_right_bg);
        Btn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        Btn.setOnClickListener(listener);
        llRight.addView(Btn, 0);
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
        imgBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
    }

    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();

    protected abstract void init(View view);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(getContentLayout(), container, false);
        tvTitle = (TextView) view.findViewById(R.id.tvtTopTitle);
        llLeft = (LinearLayout) view.findViewById(R.id.llLeft);
        llRight = (LinearLayout) view.findViewById(R.id.ll_bottom_page_left);
        init(view);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mDbUtils = SDDbHelp.createDbUtils(getActivity());
        mHttpHelper = new SDHttpHelper(getActivity());
        mUserDao = new SDUserDao(getActivity());
        mDepartmentDao = new SDDepartmentDao(getActivity());
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "-1");
        compannyId = MyPreferences.getCompanyId(getActivity())+"";
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        userType = (int) SPUtils.get(getActivity(), SPUtils.USER_TYPE, -1);
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
        openActivityForResult(clazz, null, requestCode);
    }

    protected void openActivityForResult(Class<? extends Activity> clazz, Bundle bundle, int requestCode)
    {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        getActivity().startActivityForResult(intent, requestCode);
    }

}
