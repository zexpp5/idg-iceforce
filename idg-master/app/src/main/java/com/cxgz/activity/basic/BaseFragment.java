package com.cxgz.activity.basic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.cxgz.activity.db.dao.SDUserDao;
import com.http.HttpURLUtil;
import com.http.SDHttpHelper;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.http.RequestParams;
import com.superdata.im.utils.Observable.CxAddFriendObservale;
import com.superdata.im.utils.Observable.CxAddFriendSubscribe;
import com.superdata.im.utils.Observable.CxAddUnReadObservale;
import com.superdata.im.utils.Observable.CxAddUnReadSubscribe;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;
import com.superdata.im.utils.Observable.CxWorkCircleSubscribe;
import com.ui.utils.DateTimePickDialogUtil;
import com.ui.utils.DoubleDatePickerDialog;
import com.utils.MyPreferences;
import com.utils.SDToast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import tablayout.view.dateview.wheelview.OnWheelScrollListener;
import tablayout.view.dateview.wheelview.WheelView;
import tablayout.view.dateview.wheelview.adapter.NumericWheelAdapter;

public class BaseFragment extends Fragment
{
    protected String userId;
    protected SDUserDao mUserDao;
    protected SDGson mGson;
    protected SDHttpHelper mHttpHelper;
    protected double latitude = -1;
    protected double longitude = -1;
    protected String postion;
    //时间模块
    protected LinearLayout ll;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private TextView cancerTv;
    private TextView sureTv;

    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    private String dateStr = "";
    private LayoutInflater inflater = null;

    View view = null;
    private PopupWindow popupWindow;
    private ViewGroup container;
    protected List<NameValuePair> pairs = new ArrayList<>();

    //添加好友
    private CxAddFriendSubscribe cxAddFriendSubscribe;
    //未读信息
    private CxAddUnReadSubscribe cxAddUnReadSubscribe;

    private CxWorkCircleSubscribe cxWorkCircleSubscribe;
    /**
     * 软键盘manager
     */
    protected InputMethodManager inputMethodManager;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGson = new SDGson();
        mHttpHelper = new SDHttpHelper(getActivity());
        mUserDao = new SDUserDao(getActivity());

        cxAddUnReadSubscribe = new CxAddUnReadSubscribe(new CxAddUnReadSubscribe.AddUnReadListener()
        {
            @Override
            public void acceptAddUnReadInfo(int addUnReadStatus)
            {
                addUnReadInfo(addUnReadStatus);
            }
        });

        CxAddUnReadObservale.getInstance().addObserver(cxAddUnReadSubscribe);

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
        CxAddFriendObservale.getInstance().addObserver(cxAddFriendSubscribe);

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

    protected void changBaiduView(BDLocation location)
    {

    }

    /**
     * 为读信息回调
     */
    protected void addUnReadInfo(int num)
    {

    }

    @Override
    public void onStart()
    {
        super.onStart();
        // -----------location config ------------

    }

    @Override
    public void onStop()
    {
        super.onStop();

    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();

        CxAddFriendObservale.getInstance().deleteObserver(cxAddFriendSubscribe);
        CxAddUnReadObservale.getInstance().deleteObserver(cxAddUnReadSubscribe);
        CxWorkCircleObservale.getInstance().deleteObserver(cxWorkCircleSubscribe);
//        locationService.unregisterListener(mListener); //注销掉监听
//        locationService.stop(); //停止定位服务
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        this.inflater = inflater;
        this.container = container;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    /**
     */
    public void startActivity(Bundle bundle, Class<?> target)
    {
        Intent intent = new Intent(getActivity(), target);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }


    /**
     * 带回调的跳转
     *
     * @param bundle
     * @param requestCode
     * @param target
     */
    public void startForResult(Bundle bundle, int requestCode, Class<?> target)
    {
        Intent intent = new Intent(getActivity(), target);
        if (bundle != null)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
//        getActivity().overridePendingTransition(R.anim.slide_left_in,
//                R.anim.slide_left_out);
    }

    public void finish()
    {
        getActivity().finish();
//        getActivity().overridePendingTransition(R.anim.slide_right_in,
//                R.anim.slide_right_out);
    }

    public void back(View view)
    {
        finish();
    }

    public void onResume()
    {
        super.onResume();
        /**
         * Fragment页面起始 (注意： 如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         */
//		StatService.onResume(this);
    }

    public void onPause()
    {
        super.onPause();
        /**
         *Fragment 页面结束（注意：如果有继承的父Fragment中已经添加了该调用，那么子Fragment中务必不能添加）
         */
//		StatService.onPause(this);
    }

    protected Fragment getRootFragment()
    {
        Fragment fragment = getParentFragment();
        while (fragment.getParentFragment() != null)
        {
            fragment = fragment.getParentFragment();
        }
        return fragment;
    }


    //获取当前位置
    protected String getCurrPosition()
    {
        return postion;
    }



    //时间显示控件
    protected void addDateView(final TextView tv)
    {
        if (null != tv)
        {
            tv.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        }
        tv.setOnClickListener(new View.OnClickListener()
        {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v)
            {
                new DoubleDatePickerDialog(getActivity(), 0, new DoubleDatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker startDatePicker, int startYear, int startMonthOfYear,
                                          int startDayOfMonth)
                    {
                        String monthString = (startMonthOfYear + 1) + "";
                        String dayString = startDayOfMonth + "";
                        if (monthString.length() == 1)
                        {
                            monthString = "0" + monthString;
                        }
                        if (dayString.length() == 1)
                        {
                            dayString = "0" + dayString;
                        }
                        String textString = startYear + "-" + monthString + "-" + dayString;
                        tv.setText(textString);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), false).show();
            }
        });


    }

    private void addAdateViewToPop(View parent, View view)
    {
        if (popupWindow == null)
        {
            popupWindow = new PopupWindow();
        }
        popupWindow.setContentView(view);
        popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        popupWindow.showAsDropDown(parent);
    }

    //时间pop
    private View getDataPick(final TextView tv)
    {
        Calendar c = Calendar.getInstance();
        int norYear = c.get(Calendar.YEAR);
//		int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
//		int curDate = c.get(Calendar.DATE);

        int curYear = mYear;
        int curMonth = mMonth + 1;
        int curDate = mDay;

        //view = inflater.inflate(R.layout_city.wheel_date_picker, container,false);
        view = getActivity().getLayoutInflater().inflate(R.layout.wheel_date_picker, container, false);
        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(getActivity(), 2010, norYear + 25);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(getActivity(), 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(norYear, c.get(Calendar.MONTH));
        day.setCyclic(true);

        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);

        year.setCurrentItem(curYear - 2010);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        cancerTv = (TextView) view.findViewById(R.id.date_cancer);
        sureTv = (TextView) view.findViewById(R.id.date_sure);

        sureTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != popupWindow)
                {
                    tv.setText(dateStr);
                    popupWindow.dismiss();
                }
            }
        });
        cancerTv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != popupWindow)
                {

                    popupWindow.dismiss();
                }
            }
        });
        return view;
    }

    /**
     */
    private void initDay(int arg1, int arg2)
    {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(getActivity(), 1, getDay(arg1, arg2), "%02d");
        numericWheelAdapter.setLabel("日");
        day.setViewAdapter(numericWheelAdapter);
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month)
    {
        int day = 30;
        boolean flag = false;
        switch (year % 4)
        {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month)
        {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }


    OnWheelScrollListener scrollListener = new OnWheelScrollListener()
    {
        @Override
        public void onScrollingStarted(WheelView wheel)
        {

        }

        @Override
        public void onScrollingFinished(WheelView wheel)
        {
            int n_year = year.getCurrentItem() + 2010;//年
            int n_month = month.getCurrentItem() + 1;//月
            initDay(n_year, n_month);
            dateStr = new StringBuilder().append((year.getCurrentItem() + 2010)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").
                    append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1))
                    .toString();
        }
    };

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
        startActivity(intent);
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
        startActivityForResult(intent, requestCode);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard()
    {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        {
            if (getActivity().getCurrentFocus() != null)
            {
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
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

//    private void startLocation() {
//        if(Build.VERSION.SDK_INT>=23){
//            int checkPermission = ContextCompat.checkSelfPermission(WorkCircleMainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
//            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(WorkCircleMainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
//                return;
//            } else {
//                mLocationClient.start();
//            }
//        }else{
//            　　mLocationClient.start();
//        }
//    }
}
