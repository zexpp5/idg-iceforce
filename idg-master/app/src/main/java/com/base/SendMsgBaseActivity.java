package com.base;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.entity.SDDepartmentEntity;
import com.injoy.idg.R;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.cxgz.activity.cx.msg.SDSelectContactActivity;
import com.cxgz.activity.cx.utils.DensityUtil;
import com.cxgz.activity.cx.view.MultiImageSelectorActivity;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.view.NoScrollWithGridView;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.entity.administrative.employee.Annexdata;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.http.SDHttpHelper;
import com.chaoxiang.base.config.Constants;
import com.receiver.SendMsgCallback;
import com.superdata.im.constants.CxSPIMKey;
import com.ui.activity.VoicePlayActivity;
import com.ui.activity.VoiceVideoActivity;
import com.utils.BitmapUtil;
import com.utils.BitmapWaterMarkUtil;
import com.utils.CachePath;
import com.utils.DateUtils;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.SDImgHelper;
import com.utils.SDToast;
import com.utils.StringUtil;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import paul.arian.fileselector.FileSelectionActivity;
import tablayout.view.addview.AddViewForRelativeLayout;
import tablayout.view.dateview.wheelview.OnWheelScrollListener;
import tablayout.view.dateview.wheelview.WheelView;
import tablayout.view.dateview.wheelview.adapter.NumericWheelAdapter;
import tablayout.view.dialog.SelectImgDialog;
import tablayout.widget.CustomSpinner;

/**
 *
 */
public abstract class SendMsgBaseActivity extends BasePlayAudioActivity implements SendMsgCallback
{
    protected TextView tv_title;
    protected String currentAccount;
    protected TextView tvTitle;//标题
    private LinearLayout llLeft;
    public LinearLayout llRight;
    public LinearLayout centerLL, findLL;
    protected ProgressBar pb;
    protected CustomSpinner typeCustomSpinner;//类型列表
    //protected CustomSpinner dateCustomSpinner;//时间列表
    protected TextView dateCustomSpinnerTv;//时间列表
    protected SDHttpHelper mHttpHelper;
    /**
     * 软键盘manager
     */
    protected InputMethodManager inputMethodManager;
    private int mScreenWidth;

    private LayoutInflater inflater = null;

    //时间模块
    protected LinearLayout ll;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    //    private WheelView time;
//    private WheelView min;
//    private WheelView sec;
    private TextView cancerTv;
    private TextView sureTv;

    private int mYear = 1996;
    private int mMonth = 0;
    private int mDay = 1;
    private String dateStr = "";

    View view = null;
    private PopupWindow popupWindow;


    protected LinearLayout ll_img_content_show, ll_voice_show, ll_file_show;
    protected TextView tv_file_name_show;
    protected NoScrollWithGridView show_add_img;
    protected TextView log_copy_to, log_read;//抄送人,送阅人
    /**
     * 是否只能选择一个联系人
     */
    protected boolean isSelectedOne = false;

    /**
     * 选择审批人时,需要被移除的user
     */
    protected int[] removeIds;


    /**
     * 附件类型
     */
    public static final int PLUS_ITEM_TYPE_ATTACH = 3;
    /**
     * 指令类型
     */
    public static final int PLUS_ITEM_TYPE_ORDER = 0;
    public static final int PLUS_ITEM_TYPE_VOICE = 4;

    /**
     * 拍照
     */
    protected RelativeLayout rl_camera;
    /**
     * 语音
     */
    protected RelativeLayout rl_mic;
    /**
     * 录音按钮
     */
    protected ImageView mic_btn;
    /**
     * 附件
     */
    protected RelativeLayout rl_plus;
    /**
     * 添加文件按钮
     */
    protected ImageView plus_file_btn;
    /**
     * 相册按钮
     */
    protected ImageView camera_btn;
    protected SelectImgDialog selectImgDialog;
    /**
     * 图片显示区的图片集合
     */
    protected ArrayList<String> addImgPaths = new ArrayList<>();
    /**
     * 已选附件
     */
    protected ArrayList<File> selectedAttachData = new ArrayList<>();

    /**
     * 相机拍照保存图片路径
     */
    private File cameraImgPath;

    /**
     * 相机拍照保存图片目录
     */
    private File imgFolder;

    /**
     * 最多选取多少张图片
     */
    private final int maxSelectedImgNum = 9;

    protected AddViewForRelativeLayout addImgView, addImgApproView;
    /**
     * 是否为原图
     */
    protected boolean isOriginalImg = false;

    /**
     * 是否已经选择了附件
     */
    private boolean hasSelectedAttach;
    /**
     * 附件内容区
     */
    protected LinearLayout plus_content, appro_plus_content;
    /**
     * 显示附件描述
     */
    private TextView mAttachDes;
    /**
     * 显示附件大小
     */
    private TextView mAttachSize;
    /**
     * 附件的Item
     */
    private View mAttachItem;
    /**
     * 无法加载图片集合
     */
    protected List<String> loadFailImg = new ArrayList<>();
    protected boolean isReplyOrShare;

    /**
     * 已选抄送人员
     */
    protected List<SDUserEntity> selectedSendRangeData = new ArrayList<>();
    protected List<SDUserEntity> selectedContactData = new ArrayList<>();
    protected List<SDDepartmentEntity> selectedDpData = new ArrayList<>();

    /**
     * 已选点评人或指派人
     */
    protected List<SDUserEntity> selectedPersonData = new ArrayList<>();
    protected List<SDDepartmentEntity> selectedPersonDpData = new ArrayList<>();
    protected List<SDUserEntity> selectedPersonContactData = new ArrayList<>();

    private VoiceEntity mVoiceEntity;
    /**
     * 是否有语音
     */
    private boolean hasVoice;

    /**
     * 显示语音描述
     */
    private TextView mVoiceDesc;
    /**
     * 显示语音长度
     */
    private TextView mVoiceSize;


    /**
     * 图片缓存集合
     */
    private Map<Long, String> cacheImage = new HashMap<>();
    /**
     * 语音缓存集合
     */
    private Map<Long, String> cacheVoice = new HashMap<>();
    /**
     * 附件缓存集合
     */
    private Map<Long, String> cacheAttach = new HashMap<>();
    private Type type = new TypeToken<ArrayList<Annexdata>>()
    {
    }.getType();
    private String app_flage = "0";//0是普通的附件，1是审批的


    protected Button add_pic_btn_detail, add_void_btn_detail, add_file_btn_detail;


    /**
     * onCreate 执行初始化
     */
    protected void init()
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
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);//
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        setContentView(getContentLayout());
        mHttpHelper = new SDHttpHelper(this);


        init(savedInstanceState);
        init();
        setToolbar();
        currentAccount = (String) SPUtils.get(this, CxSPIMKey.STRING_ACCOUNT, "");
    }

    protected void init(Bundle savedInstanceState)
    {
        intView();
    }

    private void intView()
    {
        tvTitle = (TextView) findViewById(R.id.tvtTopTitle);
        llLeft = (LinearLayout) findViewById(R.id.llLeft);
        llRight = (LinearLayout) findViewById(R.id.ll_bottom_page_left);
        centerLL = (LinearLayout) findViewById(R.id.ll_content);

//        findLL = (LinearLayout) findViewById(R.id.find_pll);
        pb = (ProgressBar) findViewById(R.id.top_pb);
//        typeCustomSpinner = (CustomSpinner) findViewById(R.id.spinner_listview_id);//类型
//        dateCustomSpinnerTv = (TextView) findViewById(R.id.listview_date_tv);//类型
//        ll = (LinearLayout) findViewById(R.id.ll);//时间显示区域
        if (pb != null)
        {
            pb.setMax(100);
        }
//        rl_camera = (RelativeLayout) findViewById(R.id.rl_camera);
//        camera_btn = (ImageView) findViewById(R.id.iv_camera);
//        rl_mic = (RelativeLayout) findViewById(R.id.rl_mic);
//        mic_btn = (ImageView) findViewById(R.id.iv_mic);
//        rl_plus = (RelativeLayout) findViewById(R.id.rl_plus);
//        plus_file_btn = (ImageView) findViewById(R.id.iv_plus_file);
        addImgView = (AddViewForRelativeLayout) findViewById(R.id.add_img);

//        addImgApproView = (AddViewForRelativeLayout) findViewById(R.id.add_img_appro);//这个是审批的时候

        plus_content = (LinearLayout) findViewById(R.id.ll_plus_content);
//        appro_plus_content = (LinearLayout) findViewById(R.id.ll_plus_content_appro);
        //抄送人，送阅人
//        log_copy_to = (TextView) findViewById(R.id.log_copy_to);
//        log_read = (TextView) findViewById(R.id.log_read_name);

        show_add_img = (NoScrollWithGridView) findViewById(R.id.add_img_gridview);//图片区域
        ll_img_content_show = (LinearLayout) findViewById(R.id.ll_img_content_show);
        ll_voice_show = (LinearLayout) findViewById(R.id.ll_voice_show);//语音区域
        tv_file_name_show = (TextView) findViewById(R.id.tv_file_name_show);//附件
        ll_file_show = (LinearLayout) findViewById(R.id.ll_file_show);//附件


//        add_pic_btn_detail = (Button) findViewById(R.id.add_pic_btn_detail);
//        add_void_btn_detail = (Button) findViewById(R.id.add_void_btn_detail);
//        add_file_btn_detail = (Button) findViewById(R.id.add_file_btn_detail);

    }


    //设置抄送的人员
    protected void setCopyName()
    {
        log_copy_to.setOnClickListener(this);
    }

    //设置送阅人的人员
    protected void setApprolName()
    {
        log_read.setOnClickListener(this);
    }

    //附件
    protected void setPlus_file_btn()
    {
        plus_file_btn.setOnClickListener(this);
    }

    //录音
    protected void setApproMic_btnOnclick()
    {
        app_flage = "1";//审批
        mic_btn.setOnClickListener(this);
    }


    //录音
    protected void setMic_btnOnclick()
    {
        app_flage = "0";//审批
        mic_btn.setOnClickListener(this);
    }

    //设置添加图片点击事件
    protected void setApproCamera_btnOnclick()
    {
        app_flage = "1";//审批
        camera_btn.setOnClickListener(this);
    }

    //设置添加图片点击事件
    protected void setCamera_btnOnclick()
    {
        app_flage = "0";//审批
        camera_btn.setOnClickListener(this);
    }


    //时间显示控件
    protected void addDateView(final TextView tv)
    {
        tv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                View viewl = getDataPick(tv);
                addAdateViewToPop(getWindow().getDecorView().findViewById(android.R.id.content), viewl);

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
        //popupWindow.showAsDropDown(parent);
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

        view = inflater.inflate(R.layout.wheel_date_picker, null);

        year = (WheelView) view.findViewById(R.id.year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(this, 2010, norYear + 20);
        numericWheelAdapter1.setLabel("年");
        year.setViewAdapter(numericWheelAdapter1);
        year.setCyclic(true);//是否可循环滑动
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(this, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        month.setViewAdapter(numericWheelAdapter2);
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        //initDay(curYear, curMonth);
        initDay(norYear, c.get(Calendar.MONTH));
        day.setCyclic(true);

//		time= (WheelView) view.findViewById(R.id.time);
//		String[] times = {"上午","下午"} ;
//		ArrayWheelAdapter<String> arrayWheelAdapter=new ArrayWheelAdapter<String>(WorkCircleMainActivity.this,times );
//		time.setViewAdapter(arrayWheelAdapter);
//		time.setCyclic(false);
//		time.addScrollingListener(scrollListener);

//        min = (WheelView) view.findViewById(R.id.min);
//        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(this, 1, 23, "%02d");
//        numericWheelAdapter3.setLabel("时");
//        min.setViewAdapter(numericWheelAdapter3);
//        min.setCyclic(true);
//        min.addScrollingListener(scrollListener);
//
//        sec = (WheelView) view.findViewById(R.id.sec);
//        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(this, 1, 59, "%02d");
//        numericWheelAdapter4.setLabel("分");
//        sec.setViewAdapter(numericWheelAdapter4);
//        sec.setCyclic(true);
//        sec.addScrollingListener(scrollListener);


        year.setVisibleItems(7);//设置显示行数
        month.setVisibleItems(7);
        day.setVisibleItems(7);
//		time.setVisibleItems(7);
//        min.setVisibleItems(7);
//        sec.setVisibleItems(7);

        //year.setCurrentItem(curYear - 1950);
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
//            int n_min = min.getCurrentItem();//时
//            int n_sec = sec.getCurrentItem();//秒
            initDay(n_year, n_month);
            //dateStr = new StringBuilder().append((year.getCurrentItem() + 1950)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1)).toString();
            dateStr = new StringBuilder().append((year.getCurrentItem() + 2010)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").
                    append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1))
                    .toString();
//            dateStr = new StringBuilder().append((year.getCurrentItem() + 2010)).append("-").append((month.getCurrentItem() + 1) < 10 ? "0" + (month.getCurrentItem() + 1) : (month.getCurrentItem() + 1)).append("-").
//                    append(((day.getCurrentItem() + 1) < 10) ? "0" + (day.getCurrentItem() + 1) : (day.getCurrentItem() + 1)).append(" ")
//                    .append(((min.getCurrentItem() + 1) < 10) ? "0" + (min.getCurrentItem() + 1) : (min.getCurrentItem() + 1)).append(":")
//                    .append(((sec.getCurrentItem() + 1) < 10) ? "0" + (sec.getCurrentItem() + 1) : (sec.getCurrentItem() + 1))
//                    .toString();
        }
    };

    /**
     */
    private void initDay(int arg1, int arg2)
    {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this, 1, getDay(arg1, arg2), "%02d");
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

    //设置列席列表
    protected void setTypeList(List<String> list, CustomSpinner.OnSpinnerItemClickListener onSpinnerItemClickListener)
    {
        typeCustomSpinner.setListStr(list);
        typeCustomSpinner.setOnSpinnerItemClickListener(onSpinnerItemClickListener);
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

    protected void setTitle(String text)
    {
        tvTitle.setText(text);
    }

    protected void setTitle(String text, int resId)
    {
        tvTitle.setText(text);
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumWidth());
        tvTitle.setCompoundDrawables(null, null, drawable, null);
    }

    protected void setFindLLOnclick(View.OnClickListener listener)
    {
        findLL.setOnClickListener(listener);
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
        LeftBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mScreenWidth * 0.05f);
        LeftBtn.setTextColor(Color.WHITE);
        LeftBtn.setBackgroundResource(R.drawable.tab_right_bg);
        LeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        LeftBtn.setOnClickListener(listener);
        llLeft.addView(LeftBtn);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param msg      文字
     * @param listener 点击事件
     */
    protected Button addRightBtn(String msg, View.OnClickListener listener)
    {
        Button Btn = new Button(this);
        Btn.setText(msg);
        Btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mScreenWidth * 0.05f);
        Btn.setTextColor(Color.WHITE);
        Btn.setBackgroundResource(R.drawable.tab_right_bg);
        Btn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
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
                finish();
            }
        });
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
//        imgBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
        imgBtn.setImageResource(resId);
        imgBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_height), getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_width)));
        imgBtn.setOnClickListener(listener);
        llLeft.addView(imgBtn, 0);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected ImageButton addRightBtn(int resId, View.OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgBtn.setImageResource(resId);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_height), getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_width)));
        imgBtn.setOnClickListener(listener);
        llRight.addView(imgBtn, 0);
        return imgBtn;
    }

    /**
     * @param resId
     * @param listener
     * @return 添加中间带图案的按钮
     */
    protected ImageButton addCenterBtn(int resId, View.OnClickListener listener)
    {
        ImageButton imgBtn = new ImageButton(this);
        imgBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgBtn.setImageResource(resId);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_height), getResources().getDimensionPixelSize(R.dimen.top_action_bar_default_width)));
        imgBtn.setOnClickListener(listener);
        centerLL.addView(imgBtn, 0);
        return imgBtn;
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
     *
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
    public void onClick(View view)
    {
        super.onClick(view);
        switch (view.getId())
        {
//            case R.id.iv_camera:
//                showSelectImgDialog();
//                break;
//            case R.id.iv_mic:
//                //语音
//                Intent intent = new Intent(this, VoiceVideoActivity.class);
//                startActivityForResult(intent, Constants.OPEN_VOICE_REQUEST_CODE);
//                break;
//            case R.id.iv_plus_file:
//                //FileUtil.startFilePicker(getActivity(), Constants.OPEN_FILE_PICKER_REQUEST_CODE, selectedAttachData);
//                Intent intent1 = new Intent(this, FileSelectionActivity.class);
//                intent1.putExtra("list", (Serializable) selectedAttachData);
//                startActivityForResult(intent1, Constants.OPEN_FILE_PICKER_REQUEST_CODE);
//                break;
//            case R.id.log_copy_to://抄送人
//                Intent rangeIntent = new Intent(this, SDSelectContactActivity.class);
//                rangeIntent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) selectedContactData);
//                rangeIntent.putExtra(SDSelectContactActivity.INIT_SELECTED_DP, (Serializable) selectedDpData);
//                startActivityForResult(rangeIntent, Constants.OPEN_SELECT_CONTACT_REQUEST_CODE);
//                break;
//            case R.id.log_read_name://送阅人
//                Intent personIntent = new Intent(this, SDSelectContactActivity.class);
//                if (isSelectedOne) {
//                    personIntent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) selectedPersonData);
//                } else {
//                    personIntent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) selectedPersonContactData);
//                }
//                if (removeIds != null) {
//                    personIntent.putExtra(SDSelectContactActivity.REMOVE_USER, removeIds);
//                }
//                personIntent.putExtra(SDSelectContactActivity.INIT_SELECTED_DP, (Serializable) selectedPersonDpData);
//                personIntent.putExtra(SDSelectContactActivity.SELECTED_ONE, isSelectedOne);
//                startActivityForResult(personIntent, Constants.OPEN_SELECT_CONTACT_PERSON_REQUEST_CODE);
//                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        BaseApplication.getInstance().removeActivity(this);
    }


    protected void showSelectImgDialog()
    {
        if (selectImgDialog == null)
        {
            selectImgDialog = new SelectImgDialog(SendMsgBaseActivity.this,
                    new String[]{StringUtil.getResourceString(SendMsgBaseActivity.this, R.string.camera), StringUtil.getResourceString(SendMsgBaseActivity.this, R.string.album)});
            selectImgDialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener()
            {
                @Override
                public void onClickCanera(View v)
                {
                    if (FileUtil.isSdcardExist())
                    {
                        if (addImgPaths.size() < 9)
                        {
                            if (imgFolder == null)
                            {
                                imgFolder = new File(FileUtil.getSDFolder(), CachePath.CAMERA_IMG_PATH);
                            }
                            if (!imgFolder.exists())
                            {
                                imgFolder.mkdirs();
                            }
                            cameraImgPath = new File(imgFolder, "sd_img_" + System.currentTimeMillis() + ".jpg");
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraImgPath));
                            startActivityForResult(cameraIntent, Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE);
                        } else
                        {
                            SDToast.showShort(StringUtil.getResourceString(SendMsgBaseActivity.this, R.string.album_at_more_select));
                        }
                    } else
                    {
                        SDToast.showShort(StringUtil.getResourceString(SendMsgBaseActivity.this, R.string.sd_not_exist));
                    }
                }

                @Override
                public void onClickCancel(View v)
                {
                    //TODO
                }

                @Override
                public void onClickAlum(View v)
                {
                    if (addImgPaths.size() <= 9)
                    {
                        Intent pictureIntent = new Intent(SendMsgBaseActivity.this, MultiImageSelectorActivity.class);
                        // 是否显示拍摄图片
                        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
                        // 最大可选择图片数量
                        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, maxSelectedImgNum);
                        pictureIntent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, addImgPaths);
                        // 选择模式
                        startActivityForResult(pictureIntent, Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE);
                    } else
                    {
                        SDToast.showShort(StringUtil.getResourceString(SendMsgBaseActivity.this, R.string.album_at_more_select));
                    }
                }
            });
        }
        selectImgDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case Constants.OPEN_SYSTEM_CAMERA_REQUEST_CODE:
                //相机选取图片
                if (resultCode == this.RESULT_OK)
                {
                    if (cameraImgPath != null)
                    {
                        final String imgPath = cameraImgPath.getAbsolutePath();
                        final SimpleDraweeView draweeView = createImageView(imgPath);

                        AsyncTask compressTask = new AsyncTask()
                        {  //异步任务压缩图片

                            @Override
                            protected void onPreExecute()
                            {
                                SDImgHelper.getInstance(SendMsgBaseActivity.this).loadSmallImg(R.mipmap.load_img_init, draweeView);
                                if ("0".equals(app_flage))
                                {//普通
                                    addImgView.addChild(draweeView);
                                    addImgView.setVisibility(View.VISIBLE);
                                } else
                                {//审批
                                    addImgApproView.addChild(draweeView);
                                    addImgApproView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            protected Object doInBackground(Object[] params)
                            {
                                addImgWaterMark(imgPath, draweeView);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o)
                            {
                                insertToAblum(imgPath);
                                SDImgHelper.getInstance(SendMsgBaseActivity.this).loadSmallImg(
                                        imgPath,
                                        R.mipmap.load_img_init,
                                        draweeView);
                                addImgPaths.add(imgPath);
                                onSelectedImg(addImgPaths);
                            }

                            /**
                             * 将图片地址插入到数据库
                             * @param imgPath
                             */
                            private void insertToAblum(String imgPath)
                            {
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.DATA, imgPath);
                                values.put(MediaStore.Images.Media.DISPLAY_NAME, imgPath.substring(imgPath.lastIndexOf("/")));
                                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                                SendMsgBaseActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            }
                        };
                        compressTask.execute();
                    } else
                    {
                        SDToast.showShort(getString(R.string.get_photo_img_fail));
                    }
                }
                break;
            case Constants.OPEN_SYSTEM_ABLUM_REQUEST_CODE:
                //相册选取的图片
                if (resultCode == SendMsgBaseActivity.this.RESULT_OK && data != null)
                {
                    ArrayList<String> selectedImgData = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                    isOriginalImg = data.getBooleanExtra(MultiImageSelectorActivity.ORIGINAL, false);
                    addImgFormAblum(selectedImgData);

                }
                break;
            case Constants.OPEN_FILE_PICKER_REQUEST_CODE:
                if (resultCode == SendMsgBaseActivity.this.RESULT_OK && data != null)
                {
                    //TODO 获取附件 需让激活的activity返回一个列表
                    ArrayList<File> mPickerFile = (ArrayList<File>) data.getSerializableExtra(FileSelectionActivity.FILES_TO_UPLOAD); //file array list
                    addPickFileInfo(mPickerFile);
                }
                break;
            case Constants.OPEN_SELECT_CONTACT_REQUEST_CODE:
                //选择抄送人
                if (resultCode == SendMsgBaseActivity.this.RESULT_OK && data != null)
                {

                    selectedSendRangeData = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);
                    selectedContactData = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_CONTACT_DATA);
                    addSendRangeInfo(selectedSendRangeData);

                }
                break;
            case Constants.OPEN_SELECT_CONTACT_PERSON_REQUEST_CODE:
                //选择点评人
                if (resultCode == SendMsgBaseActivity.this.RESULT_OK && data != null)
                {
//                    if (isSelectedWorkCircle) {
//                        selectedWorkCircleList = (List<SDWorkCircleListEntity>) data.getSerializableExtra(WorkcircleListActivity.SELECTED_DATA);
//                        SDLogUtil.syso("selectedWorkCircleList===" + selectedWorkCircleList.size());
//                        addWorkCircleRangeInfo(selectedWorkCircleList);
//                    } else {
                    selectedPersonData = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);
                    selectedPersonContactData = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_CONTACT_DATA);
                    addCommPersonInfo(selectedPersonData);
                    // }

                }
                break;
            case Constants.OPEN_VOICE_REQUEST_CODE:
                //语音
                if (resultCode == SendMsgBaseActivity.this.RESULT_OK && data != null)
                {
                    mVoiceEntity = (VoiceEntity) data.getSerializableExtra(VoiceVideoActivity.RESULT);
                    addVoiceInfo(mVoiceEntity);

                }
                break;
        }
    }


    /**
     * 添加语音信息
     */
    private void addVoiceInfo(VoiceEntity mVoiceEntity)
    {
        if (mVoiceEntity != null)
        {
            Object[] datas = new Object[3];
            datas[0] = PLUS_ITEM_TYPE_VOICE;
            if (!hasVoice)
            {
                View[] views = addPlusItem(datas, StringUtil.getResourceString(this, R.string.recording)
                                + "<font color='gray'>" + "(" +
                                +mVoiceEntity.getLength() + StringUtil.getResourceString(this, R.string.second)
                                + ")" + "</font>"
                        , StringUtil.getResourceString(this, R.string.recording_length) + mVoiceEntity.getLength() + StringUtil.getResourceString(this, R.string.second), this);
                mVoiceDesc = (TextView) views[0];
                mVoiceSize = (TextView) views[1];
                hasVoice = true;
            } else
            {
                mVoiceDesc.setText(Html.fromHtml(StringUtil.getResourceString(this, R.string.recording)
                        + "<font color='gray'>" + "(" +
                        +mVoiceEntity.getLength() + StringUtil.getResourceString(this, R.string.second)
                        + ")" + "</font>"));
                mVoiceSize.setText(StringUtil.getResourceString(this, R.string.recording_length));
            }
            onClickVoice(mVoiceEntity);
        }
    }

    /**
     * 添加点评人或执行人信息
     */
    protected void addCommPersonInfo(List<SDUserEntity> selectedPersonData)
    {

        //分派给下一个人审批
        if (selectedPersonData != null && !selectedPersonData.isEmpty())
        {
            if (selectedPersonData.size() > 1)
            {
                log_read.setText(selectedPersonData.size() + StringUtil.getResourceString(this, R.string.colleague_number));
            } else
            {
                SDUserEntity userEntity = selectedPersonData.get(0);
                if (userEntity != null)
                {
                    log_read.setText(userEntity.getName());
                    //nextPersonHxAccount = userEntity.getHxAccount();
                }
            }
        } else
        {
            log_read.setText("");
        }
        onClickSendPerson(selectedPersonData);
    }


    /**
     * 添加发送范围信息
     */
    private void addSendRangeInfo(List<SDUserEntity> selectedSendRangeData)
    {
        if (selectedSendRangeData != null && !selectedSendRangeData.isEmpty())
        {
            if (selectedSendRangeData.size() > 1)
            {
                log_copy_to.setText(selectedSendRangeData.size() + StringUtil.getResourceString(this, R.string.colleague_number));
            } else
            {
                SDUserEntity userEntity = selectedSendRangeData.get(0);
                if (userEntity != null)
                {
                    log_copy_to.setText(userEntity.getName());
                }
            }
        } else
        {
            log_copy_to.setText(StringUtil.getResourceString(this, R.string.send_range));
        }
        onClickSendRange(selectedSendRangeData);
    }

    /**
     * 添加选择文件信息
     *
     * @param mPickerFile
     */
    private void addPickFileInfo(ArrayList<File> mPickerFile)
    {
        if (mPickerFile != null && !mPickerFile.isEmpty())
        {
            selectedAttachData = mPickerFile;
            Object[] datas = new Object[2];
            datas[0] = PLUS_ITEM_TYPE_ATTACH;
            String fileSize = "0";
            if (!hasSelectedAttach)
            {
                View[] views = null;
                /*if (!mPickerFile.isEmpty() && mPickerFile.size() <= 1) {
                    String fileName = mPickerFile.get(0).toString();
                    fileSize = FileUtil.calcFileSize(fileName);
                    views = addPlusItem(datas, fileName, StringUtil.getResourceString(this, R.string.total) + fileSize, this);
                } else {
                    fileSize = FileUtil.calcFileSize(mPickerFile);
                    views = addPlusItem(datas, StringUtil.getResourceString(this, R.string.selected_attach) + "(" + mPickerFile.size() + ")", StringUtil.getResourceString(this, R.string.total) + fileSize, this);
                    }*/
                fileSize = FileUtil.calcFileSize(mPickerFile);
                views = addPlusItem(datas, StringUtil.getResourceString(this, R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>", StringUtil.getResourceString(this, R.string.total) + fileSize, this);

                mAttachDes = (TextView) views[0];
                mAttachSize = (TextView) views[1];
                mAttachItem = views[3];
                hasSelectedAttach = true;
            } else
            {
                if (selectedAttachData.isEmpty())
                {
                    if ("0".equals(app_flage))
                    {
                        plus_content.removeView(mAttachItem);
                    } else
                    {
                        appro_plus_content.removeView(mAttachItem);
                    }

                    hasSelectedAttach = false;
                } else
                {
                    if (mPickerFile.size() <= 1)
                    {
                        String fileName = mPickerFile.get(0).toString();
                        fileSize = FileUtil.calcFileSize(fileName);
                        mAttachDes.setText(Html.fromHtml(StringUtil.getResourceString(this, R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>"));
                    } else
                    {
                        fileSize = FileUtil.calcFileSize(mPickerFile);
                        mAttachDes.setText(Html.fromHtml(StringUtil.getResourceString(this, R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>"));
                    }
                    mAttachSize.setText(StringUtil.getResourceString(this, R.string.total) + fileSize);
                }
            }

        } else
        {

        }
        onClickAttach(selectedAttachData);
    }

    protected View[] addPlusItem(Object[] datas, String str1, String str2, View.OnClickListener clickItem)
    {
        return addPlusItem(datas, str1, str2, clickItem, null, isReplyOrShare);
    }

    protected View[] addPlusItem(Object[] datas, String str1, String str2, View.OnClickListener clickItem, ViewGroup parent, boolean isReply)
    {
        final int type = (int) datas[0];
        View[] views = new View[4];
        final View view = LayoutInflater.from(this).inflate(R.layout.sd_workcircle_plus_item, null);
        ImageView closeBtn = (ImageView) view.findViewById(R.id.iv_close);
        closeBtn.setTag(type);
        closeBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                TranslateAnimation animation = new TranslateAnimation(0, view.getMeasuredWidth(), 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(100);
                animation.setAnimationListener(new Animation.AnimationListener()
                {

                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        if (view.findViewById(R.id.plus_item_top_line).getVisibility() == View.VISIBLE)
                        {
                            View line = view.findViewById(R.id.plus_item_top_line);
                            if (line.getVisibility() != View.VISIBLE)
                            {
                                line.setVisibility(View.VISIBLE);
                            }
                        }
                        //plus_content.removeView(view);
                        if ("0".equals(app_flage))
                        {
                            plus_content.removeView(view);
                        } else
                        {
                            appro_plus_content.removeView(view);
                        }
                    }
                });
                switch (type)
                {

                    case PLUS_ITEM_TYPE_ATTACH:
                        hasSelectedAttach = false;
                        selectedAttachData = null;
                    default:
                        break;
                }
                onDelAttachItem(v);
                view.startAnimation(animation);
            }
        });
        TextView tv1 = (TextView) view.findViewById(R.id.tv1);
        TextView tv2 = (TextView) view.findViewById(R.id.tv2);
        tv1.setText(Html.fromHtml(str1));
        tv2.setText(Html.fromHtml(str2));
        LinearLayout plusItem = (LinearLayout) (view.findViewById(R.id.ll_plus_item));
        plusItem.setOnClickListener(clickItem);
        //根据type设置不同的图片
        ImageView leftIco = (ImageView) (view.findViewById(R.id.iv_left));
        LinearLayout ll_img_bg = (LinearLayout) view.findViewById(R.id.ll_img_bg);
        switch (type)
        {
            case PLUS_ITEM_TYPE_ORDER:
                //指令
                ll_img_bg.setBackgroundDrawable(null);
                leftIco.setImageDrawable(getResources().getDrawable(R.mipmap.order_finish_time));
                closeBtn.setVisibility(View.GONE);
                break;
            case PLUS_ITEM_TYPE_ATTACH:
                //附件
                ll_img_bg.setBackgroundDrawable(null);
                leftIco.setImageDrawable(getResources().getDrawable(R.mipmap.sd_attach_file));
                break;

            case PLUS_ITEM_TYPE_VOICE:
                //语音
                ll_img_bg.setBackgroundDrawable(null);
                leftIco.setImageDrawable(getResources().getDrawable(R.mipmap.send_voice_three));
//                AnimationDrawable drawable = (AnimationDrawable) getResources().getDrawable(R.anim.temp_play_voice);
//                drawable.stop();
                datas[1] = leftIco;
                //datas[2] = drawable;
                break;
            default:
                break;
        }
        plusItem.setTag(datas);
        views[0] = tv1;
        views[1] = tv2;
        views[2] = plusItem;
        views[3] = view;
        if (parent != null)
        {
            parent.addView(view, 0);
        } else
        {
            if (isReply)
            {
                if ("0".equals(app_flage))
                {
                    if (plus_content.getChildCount() == 0)
                    {
                        view.findViewById(R.id.plus_item_top_line).setVisibility(View.VISIBLE);
                    }
                } else
                {
                    if (appro_plus_content.getChildCount() == 0)
                    {
                        view.findViewById(R.id.plus_item_top_line).setVisibility(View.VISIBLE);
                    }
                }
            }
            if ("0".equals(app_flage))
            {
                plus_content.addView(view);
            } else
            {
                appro_plus_content.addView(view);
            }
        }
        return views;
    }

    /**
     * 添加来自相册的图片
     *
     * @param selectedImgData
     */
    private void addImgFormAblum(ArrayList<String> selectedImgData)
    {
        if (selectedImgData != null && !selectedImgData.isEmpty())
        {
            addImgPaths = selectedImgData;
            SDLogUtil.syso("==addImgPaths==" + addImgPaths.toString());
            addImage(selectedImgData);
        }
    }

    /**
     * 添加图片到addView中
     *
     * @param imgPaths
     */
    private void addImage(List<String> imgPaths)
    {
        if (imgPaths == null)
        {
            return;
        }
        if ("0".equals(app_flage))
        {
            addImgView.removeAllViews();
        } else
        {
            addImgApproView.removeAllViews();
        }
        for (String imgPath : imgPaths)
        {
            addImage(imgPath);
        }
        if ("0".equals(app_flage))
        {
            addImgView.setVisibility(View.VISIBLE);
        } else
        {
            addImgApproView.setVisibility(View.VISIBLE);
        }
        onSelectedImg(addImgPaths);
    }


    /**
     * 添加图片到addView中
     *
     * @param imgPath
     */
    private void addImage(final String imgPath)
    {
        if (imgPath == null || imgPath.equals(""))
        {
            return;
        }
        SimpleDraweeView draweeView = createImageView(imgPath);
//        SDImgHelper.getInstance(this).loadAlbumImg(imgPath, iv);
        SDImgHelper.getInstance(this).loadSmallImg(imgPath, draweeView, new BaseControllerListener()
        {
            @Override
            public void onFailure(String id, Throwable throwable)
            {
                loadFailImg.add(imgPath);
            }
        });
        if ("0".equals(app_flage))
        {
            addImgView.addChild(draweeView);
        } else
        {
            addImgApproView.addChild(draweeView);
        }
    }

    private void addImgWaterMark(String imgPath, ImageView iv)
    {//+ " " + own.getRealName()
        String mark = DateUtils.formatDate("yyyy-MM-dd HH:mm:ss").toString();
        Bitmap bm = BitmapWaterMarkUtil.addWaterMark(imgPath, mark, this);
        BitmapUtil.saveBitmap(bm, 90, imgPath);
    }

    /**
     * 创建imageview
     *
     * @param imgPath
     * @return
     */
    private SimpleDraweeView createImageView(String imgPath)
    {
//        int widthAndHeight = DensityUtil.dip2px(SendMsgBaseActivity.this, 75);
//        ImageView iv = new ImageView(this);
//        iv.setScaleType(ScaleType.CENTER);
//        iv.setTag(imgPath);
//        iv.setLayoutParams(new LayoutParams(widthAndHeight, widthAndHeight));
//        iv.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SendMsgBaseActivity.this, SDBigImagePagerActivity.class);
//                String imgPath = (String) v.getTag();
//                int index = addImgPaths.indexOf(imgPath);
//                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, addImgPaths.toArray(new String[addImgPaths.size()]));
//                intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
//                startActivityForResult(intent, Constants.OPEN_SELECT_BIG_IMG);
//            }
//        });
        int widthAndHeight = DensityUtil.dip2px(this, 75);
        SimpleDraweeView draweeView = new SimpleDraweeView(this);
        draweeView.setScaleType(ImageView.ScaleType.CENTER);
        draweeView.setTag(imgPath);
        draweeView.setLayoutParams(new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
        draweeView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SendMsgBaseActivity.this, SDBigImagePagerActivity.class);
                String imgPath = (String) v.getTag();
                int index = addImgPaths.indexOf(imgPath);
                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, addImgPaths.toArray(new String[addImgPaths.size()]));
                intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
                startActivityForResult(intent, Constants.OPEN_SELECT_BIG_IMG);
            }
        });
        return draweeView;
    }


    //显示附件
    protected void showFile(List<Annexdata> annexs, long identify)
    {
        List<Annexdata> imageFiles = null;
        List<Annexdata> voiceFiles = null;
        List<Annexdata> attachFiles = null;
        if (cacheImage.containsKey(identify) || cacheVoice.containsKey(identify) || cacheAttach.containsKey(identify))
        {
            if (cacheImage.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取图片");
                imageFiles = mGson.fromJson(cacheImage.get(identify), type);
                showImageFile(imageFiles);
            }
            if (cacheVoice.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取语音");
                voiceFiles = mGson.fromJson(cacheVoice.get(identify), type);
                showVoicFileView(voiceFiles);
            }
            if (cacheAttach.containsKey(identify))
            {
                SDLogUtil.debug("从内存缓存获取附件");
                attachFiles = mGson.fromJson(cacheAttach.get(identify), type);
                showAttachFile(attachFiles);
            }
        } else
        {
            SDLogUtil.debug("从网络存获取文件信息");
            Object[] objs = getFileList(annexs);
            if (objs != null)
            {
                imageFiles = (List<Annexdata>) objs[0];
                voiceFiles = (List<Annexdata>) objs[1];
                attachFiles = (List<Annexdata>) objs[2];
                cacheImage.put(identify, mGson.toJson(imageFiles));
                cacheVoice.put(identify, mGson.toJson(voiceFiles));
                cacheAttach.put(identify, mGson.toJson(attachFiles));
                showImageFile(imageFiles);
                showVoicFileView(voiceFiles);
                showAttachFile(attachFiles);
            } else
            {
                ll_file_show.setVisibility(View.GONE);
                ll_voice_show.setVisibility(View.GONE);
                ll_img_content_show.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置附件
     *
     * @param
     * @param attachFiles
     */
    protected void showAttachFile(final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            //helper.setText(R.id.tv_file_name, "" + attachFiles.size() + "个");
            tv_file_name_show.setText("" + attachFiles.size() + "个");
            //helper.setVisibility(R.id.ll_file, View.VISIBLE);
            ll_file_show.setVisibility(View.VISIBLE);
            ll_file_show.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    FileUtil.startFileNewDialog(SendMsgBaseActivity.this, attachFiles);
                }
            });
        } else
        {
            ll_file_show.setVisibility(View.GONE);
        }
    }


    /**
     * 获取不同附件列表
     *
     * @param annexs
     * @return
     */
    protected Object[] getFileList(List<Annexdata> annexs)
    {
        annexWay = 1;
        if (annexs != null && !annexs.isEmpty())
        {
            List<Annexdata> imageFiles = new ArrayList<Annexdata>();
            List<Annexdata> voiceFiles = new ArrayList<Annexdata>();
            List<Annexdata> attachFiles = new ArrayList<Annexdata>();
            for (Annexdata fileListEntity : annexs)
            {
                if (annexWay == 1)
                {
                    String srcName = fileListEntity.getSrcName();
                    SDLogUtil.debug("srcName===" + srcName);
                    switch (FileUtil.getNewFileType(fileListEntity))
                    {
                        case 1:
                            //图片
                            SDLogUtil.syso("图片类型");
                            srcName = mySbtring(srcName);
                            Annexdata imgFile = new Annexdata();
                            imgFile.setFileSize(fileListEntity.getFileSize());
                            imgFile.setId(fileListEntity.getId());
                            imgFile.setPath(fileListEntity.getPath());
                            imgFile.setSrcName(srcName);
                            imgFile.setType(fileListEntity.getType());
                            imageFiles.add(imgFile);
                            break;
                        case 2:
                            //语音
                            SDLogUtil.syso("语音类型");
                            srcName = srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW));
                            Annexdata voiceFile = new Annexdata();
                            voiceFile.setFileSize(fileListEntity.getFileSize());
                            voiceFile.setId(fileListEntity.getId());
                            voiceFile.setPath(fileListEntity.getPath());
                            voiceFile.setSrcName(srcName);
                            voiceFile.setType(fileListEntity.getType());
                            voiceFiles.add(voiceFile);
                            break;
                        case 3:
                            //附件
                            SDLogUtil.syso("附件类型");
                            attachFiles.add(fileListEntity);
                            break;
                    }
                } else if (annexWay == 2)
                {
                    String type = fileListEntity.getType();
                    int showType = fileListEntity.getShowType();
                    if (showType == 1)
                    {
                        attachFiles.add(fileListEntity);
                    } else
                    {
                        if (type.equals("spx"))
                        {
                            voiceFiles.add(fileListEntity);
                        } else
                        {
                            imageFiles.add(fileListEntity);
                        }
                    }
                }

            }
            Object[] objs = new Object[3];
            objs[0] = imageFiles;
            objs[1] = voiceFiles;
            objs[2] = attachFiles;
            return objs;
        } else
        {
            return null;
        }
    }

    private String mySbtring(String srcName)
    {
        if (srcName.contains(Constants.IMAGE_PREFIX_NEW))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_01))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_01)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_02))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_02)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_03))
        {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_03)));
        } else
        {
            return (srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW)));
        }
    }


    /**
     * 显示图片文件
     *
     * @param attachFiles
     */
    protected void showImageFile(final List<Annexdata> attachFiles)
    {
        if (attachFiles != null && !attachFiles.isEmpty())
        {
            ll_img_content_show.setVisibility(View.VISIBLE);
            //NoScrollWithGridView gridView = helper.getView(R.id.add_img);
            CommonAdapter<Annexdata> adapter = new CommonAdapter<Annexdata>(this, attachFiles, R.layout.sd_img_item)
            {
                @Override
                public void convert(ViewHolder helper, final Annexdata item, final int position)
                {
                    helper.setImageByUrl(R.id.iv_img, FileDownloadUtil.getDownloadIP(item.getAnnexWay(), item.getPath()));
                    helper.setOnclickListener(R.id.iv_img, new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            String[] bigImgUrl = new String[attachFiles.size()];
                            String[] smallImgUrl = new String[attachFiles.size()];
                            for (int i = 0; i < attachFiles.size(); i++)
                            {
                                bigImgUrl[i] = FileDownloadUtil.getDownloadIP(item.getAnnexWay(), attachFiles.get(i).getPath());
                                smallImgUrl[i] = FileDownloadUtil.getDownloadIP(item.getAnnexWay(), attachFiles.get(i).getPath());
                            }
                            Intent intent = new Intent(mContext, SDBigImagePagerActivity.class);
                            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, bigImgUrl);
                            intent.putExtra(Constants.EXTRA_SMALL_IMG_URIS, smallImgUrl);
                            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
                            intent.putExtra(Constants.EXTRA_IMAGE_INDEX, position);
                            mContext.startActivity(intent);
                        }
                    });
                }
            };
            if (adapter != null)
                SDLogUtil.debug("adapter after:" + adapter.getCount());
            show_add_img.setAdapter(adapter);

        } else
        {
            ll_img_content_show.setVisibility(View.GONE);
        }
    }


    /**
     * 显示语音
     *
     * @param voiceFile
     */
    protected void showVoicFileView(final List<Annexdata> voiceFile)
    {
        if (voiceFile != null && !voiceFile.isEmpty())
        {
            ll_voice_show.setVisibility(View.VISIBLE);
            if (voiceFile.size() <= 1)
            {
                ll_voice_show.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        VoicePlayActivity.startVoiceActivity(SendMsgBaseActivity.this, voiceFile.get(0));
                    }
                });
            } else
            {
//                helper.setText(R.id.tv_voice_size, voiceFile.size() + "个");
            }
        } else
        {
            //helper.setVisibility(R.id.ll_voice, View.GONE);
            ll_voice_show.setVisibility(View.GONE);
        }
    }


    //这个是提交的
    protected void setOnClickBtnView()
    {
        add_pic_btn_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showPicOnClick((String) v.getTag());
            }
        });
        add_void_btn_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showVoiceOnClick();
            }
        });
        add_file_btn_detail.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showFileOnClick();
            }
        });

    }

    //图片
    private void showPicOnClick(String imgPath)
    {
        if (addImgPaths.size() > 0)
        {
            Intent intent3 = new Intent(this, SDBigImagePagerActivity.class);
            int index = addImgPaths.indexOf(imgPath);
            intent3.putExtra(Constants.EXTRA_BIG_IMG_URIS, addImgPaths.toArray(new String[addImgPaths.size()]));
            intent3.putExtra(Constants.EXTRA_IMAGE_INDEX, 0);
            startActivityForResult(intent3, Constants.OPEN_SELECT_BIG_IMG);

        } else
        {
            //SDToast.showLong(R.string.no_pic_str);
            showSelectImgDialog();
        }
    }

    //语音
    private void showVoiceOnClick()
    {
        if (null != mVoiceEntity)
        {
            onClickVoice(mVoiceEntity);
        } else
        {
            SDToast.showLong(R.string.no_void_str);
            Intent intent4 = new Intent(this, VoiceVideoActivity.class);
            startActivityForResult(intent4, Constants.OPEN_VOICE_REQUEST_CODE);

        }
    }

    private void showFileOnClick()
    {
        if (selectedAttachData.size() > 0)
        {
            onClickAttach(selectedAttachData);
        } else
        {
            Intent intent5 = new Intent(this, FileSelectionActivity.class);
            intent5.putExtra("list", (Serializable) selectedAttachData);

            startActivityForResult(intent5, Constants.OPEN_FILE_PICKER_REQUEST_CODE);

        }
    }
}
