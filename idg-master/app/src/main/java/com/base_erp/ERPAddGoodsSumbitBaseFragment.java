package com.base_erp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.basic.BaseFragment;
import com.cxgz.activity.cx.utils.DensityUtil;
import com.cxgz.activity.cx.utils.TimeUtils;
import com.cxgz.activity.cx.workcircle.SDBigImagePagerActivity;
import com.cxgz.activity.cxim.LocationActiviy;
import com.cxgz.activity.cxim.adapter.CommonAdapter;
import com.cxgz.activity.cxim.adapter.ViewHolder;
import com.cxgz.activity.cxim.manager.AudioPlayManager;
import com.cxgz.activity.cxim.view.NoScrollWithGridView;
import com.cxgz.activity.db.dao.SDDepartmentDao;
import com.cxgz.activity.db.dao.SDUserDao;
import com.entity.VoiceEntity;
import com.entity.administrative.employee.Annexdata;
import com.entity.appro.AnnexListEntity;
import com.entity.appro.ApproDataEntity;
import com.entity.crm.MineCustomerEntity;
import com.entity.gztutils.ZTUtils;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDHttpHelper;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.receiver.SendMsgCallback;
import com.ui.activity.VoicePlayActivity;
import com.utils.BitmapUtil;
import com.utils.BitmapWaterMarkUtil;
import com.utils.CachePath;
import com.utils.DateUtils;
import com.utils.FileDownloadUtil;
import com.utils.FileUtil;
import com.utils.SDImgHelper;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.StringUtil;
import com.utils.VoicePlayUtil;
import com.utils_erp.FileDealUtil;
import com.view.TotalMoneyView;

import org.apache.http.NameValuePair;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tablayout.view.dialog.SelectImgDialog;
import tablayout.widget.CustomSpinner;

/**
 * 提交的Fragment的父类
 */
public abstract class ERPAddGoodsSumbitBaseFragment extends BaseFragment implements View.OnClickListener, SendMsgCallback, AudioPlayManager.OnVoiceListener {
    protected TextView tvTitle;
    protected LinearLayout ll_bottom_share_right;
    protected LinearLayout share_left;
    protected LinearLayout llLeft;
    protected LinearLayout ll_bottom_page_left;
    private int mScreenWidth;
    protected String userId;
    protected SDHttpHelper mHttpHelper;
    protected SDUserDao mUserDao;
    protected SDDepartmentDao mDepartmentDao;
    protected LinearLayout addLL, listLL;
    protected RelativeLayout delete_bottom_rl;
    protected int clickIndex = 0;
//    protected CustomerTypeBean lobean[] = new CustomerTypeBean[4];
    protected View view_titile_approval;

    protected CustomSpinner typeCustomSpinner;//类型列表
    public LinearLayout findLL;
    protected FileDealUtil.GetLogisticsInterface gli;

    /**
     * 相机拍照保存图片目录
     */
    protected static File imgFolder = new File(FileUtil.getSDFolder(), CachePath.CAMERA_IMG_PATH);

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
    protected ArrayList<String> addImgPaths[] = new ArrayList[4];
    /**
     * 已选附件
     */
    protected ArrayList<File> selectedAttachData[] = new ArrayList[4];

    /**
     * 相机拍照保存图片路径
     */
    public static File cameraImgPath;

    /**
     * 最多选取多少张图片
     */
    private final int maxSelectedImgNum = 9;

    //protected AddViewForRelativeLayout addImgView, addImgApproView;
    /**
     * 是否为原图
     */
    protected boolean isOriginalImg = false;

    /**
     * 是否已经选择了附件
     */
    private boolean hasSelectedAttach;

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

    protected List<NameValuePair> pairs = new ArrayList<>();

    protected VoiceEntity mVoiceEntity[] = new VoiceEntity[4];
    private AudioPlayManager audioPlayManager;

    protected List<File> files[] = new List[4];
    protected List<String> imgPaths[] = new List[4];
    protected File voice[] = new File[4];
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
    private Type type = new TypeToken<ArrayList<Annexdata>>() {
    }.getType();

    public TextView bottomLeftBtn;
    public TextView bottomRightBtn;

    protected LinearLayout ll_img_content_show, ll_voice_show, ll_file_show;
    protected TextView tv_file_name_show;
    protected NoScrollWithGridView show_add_img;
    protected int annexWay;
    protected RelativeLayout submitRl;
    protected LinearLayout detailLl;


    protected ProgressDialog progresDialog;
    protected FileUpload upload;


    protected List<Annexdata> annexdata;//附件
    protected Button add_pic_btn_detail, add_void_btn_detail, add_file_btn_detail;

    protected ImageView add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img;

    protected ImageView warehouse_add_photo_btn_detail_img, warehouse_add_pic_btn_detail_img, warehouse_add_void_btn_detail_img, warehouse_add_file_btn_detail_img;

    private LinearLayout show_file_img_ll_img;


    protected TotalMoneyView total_money_view_app, total_money_view;
    public static final int MONEY = 10;// 电话
    public static final int MONEY_SUMBIT = 11;// 电话

    protected String flag_copy = "0";//0是一层嵌套，1是两层或者多层

    protected RelativeLayout add_rl_rl;//审批提交

    //0=抄送1=只获取上级,2=查询领导3=查询领导+财务业务+财务管理4=查询仓库业务/管理 + 财务管理，
    private String approalType = "0";//
    //private String flag_read="0";//0是一层嵌套，1是两层或者多层

    protected MineCustomerEntity.DatasBean customerEntity;

    private View file_btn_include;
    protected boolean isVisibleToUser;
    private String flage = "0";
    private LinearLayout approver_btn;
    private RelativeLayout bottom_send;
    private LinearLayout show_file_img_ll_app;

    protected boolean reFresh = true;//是否刷新
    private LinearLayout log_position_ll;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
    }

    protected void setTitle(String text) {
        tvTitle.setText(text);

    }

    protected void addLogo() {
        ImageButton imgLeftBtn = new ImageButton(getActivity());
        imgLeftBtn.setImageResource(R.mipmap.ic_launcher);
        imgLeftBtn.setBackgroundColor(getResources().getColor(R.color.transparency));
//        imgLeftBtn.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llLeft.addView(imgLeftBtn);
    }

    protected void showProgress() {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
    }

    protected ImageButton addLeftBtn(int resId, View.OnClickListener listener) {
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
    protected Button addLeftBtn(String msg, View.OnClickListener listener) {
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
    protected void addRightBtn(String msg, View.OnClickListener listener) {
        Button Btn = new Button(getActivity());
        Btn.setText(msg);
        Btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, mScreenWidth * 0.05f);
        Btn.setTextColor(Color.WHITE);
        Btn.setBackgroundResource(R.drawable.tab_right_bg);
        Btn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        Btn.setOnClickListener(listener);
        ll_bottom_page_left.addView(Btn, 0);
    }

    /**
     * 设置返回按钮
     *
     * @param msg
     */
    protected void setLeftBack(String msg, View.OnClickListener onclick) {
        addLeftBtn(msg, onclick);
    }

    /**
     * 设置标题右边的按钮
     *
     * @param resId    资源文件
     * @param listener 点击事件
     */
    protected void addRightBtn(int resId, View.OnClickListener listener) {
        ImageButton imgBtn = new ImageButton(getActivity());
        imgBtn.setImageResource(resId);
        imgBtn.setBackgroundResource(R.drawable.tab_right_bg);
        imgBtn.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material), LinearLayout.LayoutParams.MATCH_PARENT));
        imgBtn.setOnClickListener(listener);
        ll_bottom_page_left.addView(imgBtn, 0);
    }

    /**
     * @return 当前activity 的布局资源文件
     */
    protected abstract int getContentLayout();

    protected abstract void init(View view);

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHttpHelper = new SDHttpHelper(getActivity());
        mUserDao = new SDUserDao(getActivity());
        mDepartmentDao = new SDDepartmentDao(getActivity());

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        mHttpHelper = new SDHttpHelper(getActivity());
        userId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "-1");
        annexWay = Integer.parseInt(SPUtils.get(getActivity(), SPUtils.ANNEX_WAY, 2).toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentLayout(), container, false);
        tvTitle = (TextView) view.findViewById(R.id.tvtTopTitle);
        llLeft = (LinearLayout) view.findViewById(R.id.llLeft);
        ll_bottom_page_left = (LinearLayout) view.findViewById(R.id.ll_bottom_page_left);

        show_add_img = (NoScrollWithGridView) view.findViewById(R.id.add_img_gridview);//图片区域
        ll_img_content_show = (LinearLayout) view.findViewById(R.id.ll_img_content_show);
        ll_voice_show = (LinearLayout) view.findViewById(R.id.ll_voice_show);//语音区域
        tv_file_name_show = (TextView) view.findViewById(R.id.tv_file_name_show);//附件
        ll_file_show = (LinearLayout) view.findViewById(R.id.ll_file_show);//附件
        submitRl = (RelativeLayout) view.findViewById(R.id.rl_log_submit);
        detailLl = (LinearLayout) view.findViewById(R.id.rl_log_detail);

        add_photo_btn_detail_img = (ImageView) view.findViewById(R.id.add_photo_btn_detail_img);//拍照
        add_pic_btn_detail_img = (ImageView) view.findViewById(R.id.add_pic_btn_detail_img);
        add_void_btn_detail_img = (ImageView) view.findViewById(R.id.add_void_btn_detail_img);
        add_file_btn_detail_img = (ImageView) view.findViewById(R.id.add_file_btn_detail_img);

        show_file_img_ll_img = (LinearLayout) view.findViewById(R.id.show_file_img_ll_img);

        delete_bottom_rl = (RelativeLayout) view.findViewById(R.id.delete_bottom_rl);

        file_btn_include = (View) view.findViewById(R.id.file_btn_include);

        init(view);

        return view;
    }

    protected String setAddress(final String address) {
        if (!TextUtils.isEmpty(address)) {
            log_position_ll.setVisibility(View.VISIBLE);
        }
        log_position_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SDToast.showShort(address);
            }
        });
        return address;
    }

    protected void setShowBaiduMapOnclick(final String postion, final String address, final String userId) {
        log_position_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(postion)) {
                    SDToast.showShort("无定位信息");
                    return;
                }
                String[] nt = postion.split(",");
                Intent intent = new Intent(getActivity(), LocationActiviy.class);
                // 通过Bundle来获取数据,通过key-Value的方式放入数据
                Bundle bl = new Bundle();
                bl.putString("longitude", nt[0]);//经度longitude
                bl.putString("latitude", nt[1]);// 纬度latitude
                bl.putString("address", address);//地址address
                bl.putString("userId", userId);//发的那个人的userId
                intent.putExtras(bl);
                getActivity().startActivity(intent);
            }
        });
    }
    //附件
    protected void setFile() {
        if (warehouse_add_file_btn_detail_img != null) {
            warehouse_add_file_btn_detail_img.setOnClickListener(this);
        } else {
            add_file_btn_detail_img.setOnClickListener(this);
        }
    }


    //录音
    protected void recordVoice() {
        if (warehouse_add_void_btn_detail_img != null) {
            warehouse_add_void_btn_detail_img.setOnClickListener(this);
        } else {
            add_void_btn_detail_img.setOnClickListener(this);
        }
    }


    //拍照
    protected void talkPhoto() {
        if (warehouse_add_photo_btn_detail_img != null) {
            warehouse_add_photo_btn_detail_img.setOnClickListener(this);
        } else {
            add_photo_btn_detail_img.setOnClickListener(this);
        }
    }

    //选择图片
    protected void selectPic() {
        if (warehouse_add_pic_btn_detail_img != null) {
            warehouse_add_pic_btn_detail_img.setOnClickListener(this);
        } else {
            add_pic_btn_detail_img.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_photo_btn_detail_img://拍照
                showSelectImgDialog();
                break;
            case R.id.add_pic_btn_detail_img://选择图片
                FileDealUtil.selectImgFromDir(null, getRootFragment(), addImgPaths[clickIndex], Constants.ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE);
                break;
            case R.id.add_file_btn_detail_img://文件
                FileDealUtil.selectFile(null, getRootFragment(), selectedAttachData[clickIndex], Constants.ADD_GOODS_OPEN_FILE_PICKER_REQUEST_CODE);
                break;
            case R.id.add_void_btn_detail_img://语音
                FileDealUtil.recordVoice(null, getRootFragment(), Constants.ADD_GOODS_OPEN_VOICE_REQUEST_CODE);
                break;
        }
    }

    public Fragment getRootFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for (Fragment fragment : fragments) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }

    protected void showSelectImgDialog() {
        if (selectImgDialog == null) {
            selectImgDialog = new SelectImgDialog(getActivity(),
                    new String[]{StringUtil.getResourceString(getActivity(), R.string.camera), StringUtil.getResourceString(getActivity(), R.string.album)});
            selectImgDialog.setOnSelectImgListener(new SelectImgDialog.OnSelectImgListener() {
                @Override
                public void onClickCanera(View v) {
                    cameraImgPath = FileDealUtil.takePhoto(null, getRootFragment(), addImgPaths[clickIndex], Constants.ADD_GOODS_OPEN_SYSTEM_CAMERA_REQUEST_CODE);
                }

                @Override
                public void onClickCancel(View v) {
                    //TODO
                }

                @Override
                public void onClickAlum(View v) {
                    FileDealUtil.selectImgFromDir(null, getRootFragment(), addImgPaths[clickIndex], Constants.ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE);
                }
            });
        }
        selectImgDialog.show();
    }

    private boolean cameraResult = false;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.ADD_GOODS_OPEN_SYSTEM_CAMERA_REQUEST_CODE:
                //相机选取图片
                if (cameraResult) {
                    return;
                } else {
                    cameraResult = true;
                }
                if (resultCode == getActivity().RESULT_OK) {

                    if (cameraImgPath != null) {
                        isHasFileAndPicAndVoice(true);

                        final String imgPath = cameraImgPath.getAbsolutePath();
                        final SimpleDraweeView draweeView = createImageView(imgPath);
                        AsyncTask compressTask = new AsyncTask() {  //异步任务压缩图片
                            @Override
                            protected void onPreExecute() {
                                SDImgHelper.getInstance(getActivity()).loadSmallImg(R.mipmap.load_img_init, draweeView);
                            }

                            @Override
                            protected Object doInBackground(Object[] params) {
//                                addImgWaterMark(imgPath, draweeView);
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Object o) {
                                insertToAblum(imgPath);
                                SDImgHelper.getInstance(getActivity()).loadSmallImg(
                                        imgPath,
                                        R.mipmap.load_img_init,
                                        draweeView);
                                addImgPaths[clickIndex].add(imgPath);
                                onSelectedImg(addImgPaths[clickIndex]);
                                if (null != warehouse_add_pic_btn_detail_img) {
                                    FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], warehouse_add_photo_btn_detail_img, warehouse_add_pic_btn_detail_img, warehouse_add_void_btn_detail_img, warehouse_add_file_btn_detail_img);
                                } else if (null != add_pic_btn_detail_img) {
                                    FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
                                } else {
//                                    FileDealUtil.showAddGoodsFileDialog(getActivity(), gli, lobean[clickIndex], getRootFragment(), addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], cameraImgPath);
                                }
                                cameraResult = false;
                            }

                            /**
                             * 将图片地址插入到数据库
                             *
                             * @param imgPath
                             */
                            private void insertToAblum(String imgPath) {
                                ContentValues values = new ContentValues();
                                values.put(MediaStore.Images.Media.DATA, imgPath);
                                values.put(MediaStore.Images.Media.DISPLAY_NAME, imgPath.substring(imgPath.lastIndexOf("/")));
                                values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
                                getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                            }
                        };
                        compressTask.execute();
                    } else {
                        MyToast.showToast(getActivity(), R.string.get_photo_img_fail);
                    }
                }
                break;
            case Constants.ADD_GOODS_OPEN_SYSTEM_ABLUM_REQUEST_CODE:
                //相册选取的图片
//                if (resultCode == getActivity().RESULT_OK && data != null) {
//                    ArrayList<String> selectedImgData = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
//                    isOriginalImg = data.getBooleanExtra(MultiImageSelectorActivity.ORIGINAL, false);
//                    addImgFormAblum(selectedImgData);
//                    if (null != warehouse_add_pic_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], warehouse_add_photo_btn_detail_img, warehouse_add_pic_btn_detail_img, warehouse_add_void_btn_detail_img, warehouse_add_file_btn_detail_img);
//                    } else if (null != add_pic_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
//                    } else {
//                        FileDealUtil.showAddGoodsFileDialog(getActivity(), gli, lobean[clickIndex], getRootFragment(), addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], cameraImgPath);
//                    }
//
//                }
                break;
            case Constants.ADD_GOODS_OPEN_FILE_PICKER_REQUEST_CODE:
//                if (resultCode == getActivity().RESULT_OK && data != null) {
//                    //TODO 获取附件 需让激活的activity返回一个列表
//                    ArrayList<File> mPickerFile = (ArrayList<File>) data.getSerializableExtra(FileSelectionActivity.FILES_TO_UPLOAD); //file array list
//                    addPickFileInfo(mPickerFile);
//                    if (null != warehouse_add_file_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], warehouse_add_photo_btn_detail_img, warehouse_add_pic_btn_detail_img, warehouse_add_void_btn_detail_img, warehouse_add_file_btn_detail_img);
//                    } else if (null != add_file_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
//                    } else {
//                        FileDealUtil.showAddGoodsFileDialog(getActivity(), gli, lobean[clickIndex], getRootFragment(), addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], cameraImgPath);
//                    }
//                    // showVisibleOrInVisibleImg();
//                }
                break;
            case Constants.ADD_GOODS_OPEN_VOICE_REQUEST_CODE:
                //语音
//                if (resultCode == getActivity().RESULT_OK && data != null) {
//                    mVoiceEntity[clickIndex] = (VoiceEntity) data.getSerializableExtra(VoiceVideoActivity.RESULT);
//                    addVoiceInfo(mVoiceEntity[clickIndex]);
//                    if (null != warehouse_add_void_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], warehouse_add_photo_btn_detail_img, warehouse_add_pic_btn_detail_img, warehouse_add_void_btn_detail_img, warehouse_add_file_btn_detail_img);
//                    } else if (null != add_void_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
//                    } else {
//                        FileDealUtil.showAddGoodsFileDialog(getActivity(), gli, lobean[clickIndex], getRootFragment(), addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], cameraImgPath);
//                    }
//                }
                break;
            case MONEY:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bundle bundle = data.getExtras();
                    String money = bundle.getString("value");
                    if (null != total_money_view_app) {
                        total_money_view_app.bindTotalMoney(Double.parseDouble(money));
                    }
                    Log.i("zy", "money:" + money + "Double.parseDouble(money)：" + Double.parseDouble(money));
                }
                break;
            case MONEY_SUMBIT:
                if (resultCode == getActivity().RESULT_OK && data != null) {
                    Bundle bundle1 = data.getExtras();
                    String money1 = bundle1.getString("value");
                    total_money_view.bindTotalMoney(Double.parseDouble(money1));
                }
                break;
            case Constants.OPEN_SELECT_BIG_IMG:
                //TODO 获取被删除图片
//                if (resultCode == getActivity().RESULT_OK && data != null) {
//
//                    List<Integer> delImgPositions = data.getIntegerArrayListExtra(SDBigImagePagerActivity.DELETE_DATA);
//                    String[] bigUrls = data.getStringArrayExtra(Constants.EXTRA_BIG_IMG_URIS_DELETE);
//                    addImgPaths[clickIndex] = (ArrayList) arrayToList(bigUrls);
//                    onSelectedImg(addImgPaths[clickIndex]);
//                    // showVisibleOrInVisibleImg();
//                    if (null != add_pic_btn_detail_img) {
//                        FileDealUtil.checkFilePicVoiceIsNullOrNot(addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], add_photo_btn_detail_img, add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
//                    } else {
//                        FileDealUtil.showAddGoodsFileDialog(getActivity(), gli, lobean[clickIndex], getRootFragment(), addImgPaths[clickIndex], mVoiceEntity[clickIndex], selectedAttachData[clickIndex], cameraImgPath);
//                    }
//                }
                break;


        }
    }

    //增加了或者清除了附件，图片，语音回调后，在子类调用
    protected void isHasFileAndPicAndVoice(boolean isHasFile) {
        //图片等被清空则参数为false,有附件则true.
    }

    //当图片，附件，语音清空
    private void showVisibleOrInVisibleImg() {
        if ((mVoiceEntity[clickIndex] == null) && (selectedAttachData[clickIndex].size() == 0) && (addImgPaths[clickIndex].size() == 0)) {
            if (null != show_file_img_ll_img) {
                show_file_img_ll_img.setVisibility(View.GONE);
            }

        } else {
            if (null != show_file_img_ll_img) {//附件
                show_file_img_ll_img.setVisibility(View.VISIBLE);
                if (add_void_btn_detail_img != null) {
                    if (mVoiceEntity[clickIndex] == null) {
                        add_void_btn_detail_img.setImageResource(R.mipmap.voice_img_bg_gray);
                    } else {
                        add_void_btn_detail_img.setImageResource(R.mipmap.voice_img_bg);
                    }
                }
                if (null != add_pic_btn_detail_img) {
                    if (addImgPaths[clickIndex].size() == 0) {//图片
                        add_pic_btn_detail_img.setImageResource(R.mipmap.pic_img_bg_gray);
                    } else {
                        add_pic_btn_detail_img.setImageResource(R.mipmap.pic_img_bg);
                    }
                }
                if (null != add_file_btn_detail_img) {
                    if (selectedAttachData[clickIndex].size() == 0) {//
                        add_file_btn_detail_img.setImageResource(R.mipmap.fj_img_bg_gray);
                    } else {
                        add_file_btn_detail_img.setImageResource(R.mipmap.fj_img_bg);
                    }
                }
            }
        }
    }

    private List<String> arrayToList(String[] smallUrls) {
        List<String> resList = new ArrayList<String>();
        for (String smallUrl : smallUrls) {
            resList.add(smallUrl);
        }
        return resList;
    }

    /**
     * 添加语音信息
     */
    private void addVoiceInfo(VoiceEntity mVoiceEntity) {
        if (mVoiceEntity != null) {
            Object[] datas = new Object[3];
            datas[0] = PLUS_ITEM_TYPE_VOICE;
            if (!hasVoice) {
                View[] views = addPlusItem(datas, StringUtil.getResourceString(getActivity(), R.string.recording)
                                + "<font color='gray'>" + "(" +
                                +mVoiceEntity.getLength() + StringUtil.getResourceString(getActivity(), R.string.second)
                                + ")" + "</font>"
                        , StringUtil.getResourceString(getActivity(), R.string.recording_length) + mVoiceEntity.getLength() + StringUtil.getResourceString(getActivity(), R.string.second), this);
                mVoiceDesc = (TextView) views[0];
                mVoiceSize = (TextView) views[1];
                hasVoice = true;
            } else {
                mVoiceDesc.setText(Html.fromHtml(StringUtil.getResourceString(getActivity(), R.string.recording)
                        + "<font color='gray'>" + "(" +
                        +mVoiceEntity.getLength() + StringUtil.getResourceString(getActivity(), R.string.second)
                        + ")" + "</font>"));
                mVoiceSize.setText(StringUtil.getResourceString(getActivity(), R.string.recording_length));
            }
            onClickVoice(mVoiceEntity);
        }
    }


    /**
     * 添加选择文件信息
     *
     * @param mPickerFile
     */
    private void addPickFileInfo(ArrayList<File> mPickerFile) {
        if (mPickerFile != null && !mPickerFile.isEmpty()) {
            selectedAttachData[clickIndex] = mPickerFile;
            Object[] datas = new Object[2];
            datas[0] = PLUS_ITEM_TYPE_ATTACH;
            String fileSize = "0";
            if (!hasSelectedAttach) {
                View[] views = null;
                fileSize = FileUtil.calcFileSize(mPickerFile);
                views = addPlusItem(datas, StringUtil.getResourceString(getActivity(), R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>", StringUtil.getResourceString(getActivity(), R.string.total) + fileSize, this);

                mAttachDes = (TextView) views[0];
                mAttachSize = (TextView) views[1];
                mAttachItem = views[3];
                hasSelectedAttach = true;
            } else {
                if (selectedAttachData[clickIndex].isEmpty()) {

                    hasSelectedAttach = false;
                } else {
                    if (mPickerFile.size() <= 1) {
                        String fileName = mPickerFile.get(0).toString();
                        fileSize = FileUtil.calcFileSize(fileName);
                        mAttachDes.setText(Html.fromHtml(StringUtil.getResourceString(getActivity(), R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>"));
                    } else {
                        fileSize = FileUtil.calcFileSize(mPickerFile);
                        mAttachDes.setText(Html.fromHtml(StringUtil.getResourceString(getActivity(), R.string.selected_attach) + "<font color='gray'>" + "(" + mPickerFile.size() + "个)" + "</font>"));
                    }
                    mAttachSize.setText(StringUtil.getResourceString(getActivity(), R.string.total) + fileSize);
                }
            }

        } else {

        }
        onClickAttach(selectedAttachData[clickIndex]);
    }

    protected View[] addPlusItem(Object[] datas, String str1, String str2, View.OnClickListener clickItem) {
        return addPlusItem(datas, str1, str2, clickItem, null, isReplyOrShare);
    }

    protected View[] addPlusItem(Object[] datas, String str1, String str2, View.OnClickListener clickItem, ViewGroup parent, boolean isReply) {
        final int type = (int) datas[0];
        View[] views = new View[4];
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.sd_workcircle_plus_item, null);
        ImageView closeBtn = (ImageView) view.findViewById(R.id.iv_close);
        closeBtn.setTag(type);
        closeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TranslateAnimation animation = new TranslateAnimation(0, view.getMeasuredWidth(), 0, 0);
                animation.setFillAfter(true);
                animation.setDuration(100);
                animation.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (view.findViewById(R.id.plus_item_top_line).getVisibility() == View.VISIBLE) {
                            View line = view.findViewById(R.id.plus_item_top_line);
                            if (line.getVisibility() != View.VISIBLE) {
                                line.setVisibility(View.VISIBLE);
                            }
                        }

                    }
                });
                switch (type) {

                    case PLUS_ITEM_TYPE_ATTACH:
                        hasSelectedAttach = false;
                        selectedAttachData[clickIndex] = null;
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
        switch (type) {
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
        if (parent != null) {
            parent.addView(view, 0);
        } else {
        }
        return views;
    }

    /**
     * 添加来自相册的图片
     *
     * @param selectedImgData
     */
    private void addImgFormAblum(ArrayList<String> selectedImgData) {
        if (selectedImgData != null) {//&& !selectedImgData.isEmpty()
            addImgPaths[clickIndex] = selectedImgData;
            SDLogUtil.syso("==addImgPaths==" + addImgPaths[clickIndex].toString());
            addImage(selectedImgData);
        }
    }


    /**
     * 添加图片到addView中
     *
     * @param imgPaths
     */
    private void addImage(List<String> imgPaths) {
        if (imgPaths == null) {
            return;
        }
        for (String imgPath : imgPaths) {
            addImage(imgPath);
        }

        onSelectedImg(addImgPaths[clickIndex]);
    }

    /**
     * 添加图片到addView中
     *
     * @param imgPath
     */
    private void addImage(final String imgPath) {
        if (imgPath == null || imgPath.equals("")) {
            return;
        }
        SimpleDraweeView draweeView = createImageView(imgPath);
        SDImgHelper.getInstance(getActivity()).loadSmallImg(imgPath, draweeView, new BaseControllerListener() {
            @Override
            public void onFailure(String id, Throwable throwable) {
                loadFailImg.add(imgPath);
            }
        });

    }


    private void addImgWaterMark(String imgPath, ImageView iv) {//+ " " + own.getRealName()
        String mark = DateUtils.formatDate("yyyy-MM-dd HH:mm:ss").toString();
        Bitmap bm = BitmapWaterMarkUtil.addWaterMark(imgPath, mark, getActivity());
        BitmapUtil.saveBitmap(bm, 90, imgPath);
    }


    @Override
    protected String getCurrPosition() {
        return super.getCurrPosition();
    }

    /**
     * 创建imageview
     *
     * @param imgPath
     * @return
     */
    private SimpleDraweeView createImageView(String imgPath) {
        int widthAndHeight = DensityUtil.dip2px(getActivity(), 75);
        SimpleDraweeView draweeView = new SimpleDraweeView(getActivity());
        draweeView.setScaleType(ImageView.ScaleType.CENTER);
        draweeView.setTag(imgPath);
        draweeView.setLayoutParams(new ViewGroup.LayoutParams(widthAndHeight, widthAndHeight));
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
                String imgPath = (String) v.getTag();
                int index = addImgPaths[clickIndex].indexOf(imgPath);
                intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, addImgPaths[clickIndex].toArray(new String[addImgPaths[clickIndex].size()]));
                intent.putExtra(Constants.EXTRA_IMAGE_INDEX, index);
                startActivityForResult(intent, Constants.OPEN_SELECT_BIG_IMG);
            }
        });
        return draweeView;
    }

    //1图片，2语音，3附件
    //显示附件
    protected void showFile(List<Annexdata> annexs, long identify, int flage) {
        List<Annexdata> imageFiles = null;
        List<Annexdata> voiceFiles = null;
        List<Annexdata> attachFiles = null;
        cacheImage.clear();
        cacheVoice.clear();
        cacheAttach.clear();
        if (cacheImage.containsKey(identify) || cacheVoice.containsKey(identify) || cacheAttach.containsKey(identify)) {
            if (cacheImage.containsKey(identify)) {
                SDLogUtil.debug("从内存缓存获取图片");
                imageFiles = mGson.fromJson(cacheImage.get(identify), type);
                //showImageFile(imageFiles);
                if (flage == 1) {
                    showImgFileReturn(imageFiles);
                }
            }
            if (cacheVoice.containsKey(identify)) {
                SDLogUtil.debug("从内存缓存获取语音");
                voiceFiles = mGson.fromJson(cacheVoice.get(identify), type);
                // showVoicFileView(voiceFiles);
                if (flage == 2) {
                    showVoicFileViewReturn(voiceFiles);
                }
            }
            if (cacheAttach.containsKey(identify)) {
                SDLogUtil.debug("从内存缓存获取附件");
                attachFiles = mGson.fromJson(cacheAttach.get(identify), type);
                //showAttachFile(attachFiles);
                if (flage == 3) {
                    showAttachFileReturn(attachFiles);
                }
            }
        } else {
            SDLogUtil.debug("从网络存获取文件信息");
            Object[] objs = getFileList(annexs);
            if (objs != null) {
                imageFiles = (List<Annexdata>) objs[0];
                voiceFiles = (List<Annexdata>) objs[1];
                attachFiles = (List<Annexdata>) objs[2];
                cacheImage.put(identify, mGson.toJson(imageFiles));
                cacheVoice.put(identify, mGson.toJson(voiceFiles));
                cacheAttach.put(identify, mGson.toJson(attachFiles));
                // showImageFile(imageFiles);
                if (flage == 1) {
                    showImgFileReturn(imageFiles);
                } else if (flage == 2) {
                    showVoicFileViewReturn(voiceFiles);
                } else if (flage == 3) {
                    showAttachFileReturn(attachFiles);
                }
            } else {
                ll_file_show.setVisibility(View.GONE);
                ll_voice_show.setVisibility(View.GONE);
                ll_img_content_show.setVisibility(View.GONE);
            }
        }
    }


    protected void showAttachFileReturn(final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            FileUtil.startFileNewDialog(getActivity(), attachFiles);
        }
    }

    /**
     * 设置附件
     *
     * @param
     * @param attachFiles
     */
    protected void showAttachFile(final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            //helper.setText(R.id.tv_file_name, "" + attachFiles.size() + "个");
            tv_file_name_show.setText("" + attachFiles.size() + "个");
            //helper.setVisibility(R.id.ll_file, View.VISIBLE);
            ll_file_show.setVisibility(View.VISIBLE);
            ll_file_show.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileUtil.startFileNewDialog(getActivity(), attachFiles);
                }
            });
        } else {
            ll_file_show.setVisibility(View.GONE);
        }
    }


    /**
     * 获取不同附件列表
     *
     * @param annexs
     * @return
     */
    protected Object[] getFileList(List<Annexdata> annexs) {
        annexWay = 1;
        if (annexs != null && !annexs.isEmpty()) {
            List<Annexdata> imageFiles = new ArrayList<Annexdata>();
            List<Annexdata> voiceFiles = new ArrayList<Annexdata>();
            List<Annexdata> attachFiles = new ArrayList<Annexdata>();
            for (Annexdata fileListEntity : annexs) {
                if (annexWay == 1) {
                    String srcName = fileListEntity.getSrcName();
                    SDLogUtil.debug("srcName===" + srcName);
                    switch (FileUtil.getNewFileType(fileListEntity)) {
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
                } else if (annexWay == 2) {
                    String type = fileListEntity.getType();
                    int showType = fileListEntity.getShowType();
                    if (showType == 1) {
                        attachFiles.add(fileListEntity);
                    } else {
                        if (type.equals("spx")) {
                            voiceFiles.add(fileListEntity);
                        } else {
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
        } else {
            return null;
        }
    }

    private String mySbtring(String srcName) {
        if (srcName.contains(Constants.IMAGE_PREFIX_NEW)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_01)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_01)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_02)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_02)));
        } else if (srcName.contains(Constants.IMAGE_PREFIX_NEW_03)) {
            return (srcName.substring(0, srcName.indexOf(Constants.IMAGE_PREFIX_NEW_03)));
        } else {
            return (srcName.substring(0, srcName.indexOf(Constants.RADIO_PREFIX_NEW)));
        }
    }

    //显示返回的图片
    private void showImgFileReturn(final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            String[] bigImgUrl = new String[attachFiles.size()];
            String[] smallImgUrl = new String[attachFiles.size()];
            for (int i = 0; i < attachFiles.size(); i++) {
                bigImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
                smallImgUrl[i] = FileDownloadUtil.getDownloadIP(2, attachFiles.get(i).getPath());
            }
            Intent intent = new Intent(getActivity(), SDBigImagePagerActivity.class);
            intent.putExtra(Constants.EXTRA_BIG_IMG_URIS, bigImgUrl);
            intent.putExtra(Constants.EXTRA_SMALL_IMG_URIS, smallImgUrl);
            intent.putExtra(SDBigImagePagerActivity.IS_NEED_DEL, false);
            intent.putExtra(Constants.EXTRA_IMAGE_INDEX, 0);
            getActivity().startActivity(intent);
        }
    }

    /**
     * 显示图片文件
     *
     * @param attachFiles
     */
    protected void showImageFile(final List<Annexdata> attachFiles) {
        if (attachFiles != null && !attachFiles.isEmpty()) {
            ll_img_content_show.setVisibility(View.VISIBLE);
            //NoScrollWithGridView gridView = helper.getView(R.id.add_img);
            CommonAdapter<Annexdata> adapter = new CommonAdapter<Annexdata>(getActivity(), attachFiles, R.layout.sd_img_item) {
                @Override
                public void convert(ViewHolder helper, final Annexdata item, final int position) {
                    helper.setImageByUrl(R.id.iv_img, FileDownloadUtil.getDownloadIP(item.getAnnexWay(), item.getPath()));
                    helper.setOnclickListener(R.id.iv_img, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String[] bigImgUrl = new String[attachFiles.size()];
                            String[] smallImgUrl = new String[attachFiles.size()];
                            for (int i = 0; i < attachFiles.size(); i++) {
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

        } else {
            ll_img_content_show.setVisibility(View.GONE);
        }
    }


    private void showVoicFileViewReturn(final List<Annexdata> voiceFile) {
        if (voiceFile != null && !voiceFile.isEmpty()) {
            //VoicePlayActivity.startVoiceActivity(getActivity(), voiceFile.get(0));
            VoicePlayUtil.getInstance().setData(getActivity(), voiceFile.get(0));
//            audioPlayManager = AudioPlayManager.getInstance();
//            audioPlayManager.setOnVoiceListener(this);
//            AudioPlayManager.getInstance().play(getActivity(), voiceFile.get(0).getAndroidFilePath(), null);
        }
    }

    /**
     * 显示语音
     *
     * @param voiceFile
     */
    protected void showVoicFileView(final List<Annexdata> voiceFile) {
        if (voiceFile != null && !voiceFile.isEmpty()) {
            ll_voice_show.setVisibility(View.VISIBLE);
            if (voiceFile.size() <= 1) {
                ll_voice_show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VoicePlayActivity.startVoiceActivity(getActivity(), voiceFile.get(0));
                    }
                });
            } else {
//                helper.setText(R.id.tv_voice_size, voiceFile.size() + "个");
            }
        } else {
            //helper.setVisibility(R.id.ll_voice, View.GONE);
            ll_voice_show.setVisibility(View.GONE);
        }
    }


    //判断是否是图片，语音，或者文件
    private void checkFileOrImgOrVoiceAppro(final List<ApproDataEntity> approData, final int a, final List<Annexdata> annexdatas, ImageView img, ImageView voiceImg, ImageView fileImg) {
        boolean isFile = false;
        boolean isImg = false;
        boolean isVoice = false;
        for (int i = 0; i < annexdatas.size(); i++) {
            String type = annexdatas.get(i).getSrcName();
            if (!TextUtils.isEmpty(type)) {
                //图片
                if (type.endsWith(Constants.IMAGE_PREFIX_NEW) || type.endsWith(Constants.IMAGE_PREFIX_NEW_01) ||
                        type.endsWith(Constants.IMAGE_PREFIX_NEW_02) || type.endsWith(Constants.IMAGE_PREFIX_NEW_03)) {
                    isImg = true;
                } else if (type.endsWith("spx")) {
                    isVoice = true;
                } else {
                    isFile = true;
                }
            } else {
                isFile = true;
            }
        }


        if (isFile) {
            fileImg.setImageResource(R.mipmap.fj_img_bg);
            fileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // toShowAnnex(annexdatas, approData.get(a).getbId() + "");
                    showFile(annexdatas, approData.get(a).getbId(), 3);
                }
            });
        } else {
            fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        }
        if (isImg) {
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //toShowAnnex(annexdatas, approData.get(a).getbId() + "");
                    showFile(annexdatas, approData.get(a).getbId(), 1);
                }
            });
            img.setImageResource(R.mipmap.pic_img_bg);
        } else {
            img.setImageResource(R.mipmap.pic_img_bg_gray);
        }

        if (isVoice) {
            voiceImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //toShowAnnex(annexdatas, approData.get(a).getbId() + "");
                    showFile(annexdatas, approData.get(a).getbId(), 2);
                }
            });
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
        } else {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        }

    }

    //判断是否是图片，语音，或者文件
    protected void checkFileOrImgOrVoice(final List<Annexdata> annexdatas, ImageView img, ImageView voiceImg, ImageView fileImg) {
        fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
        img.setImageResource(R.mipmap.pic_img_bg_gray);
        voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
        boolean isFile = false;
        boolean isImg = false;
        boolean isVoice = false;
        for (int i = 0; i < annexdatas.size(); i++) {
            String type = annexdatas.get(i).getType();
            if (!TextUtils.isEmpty(type)) {
                //图片
                if (type.equals(Constants.IMAGE_PREFIX_NEW_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_01_RETURN) ||
                        type.equals(Constants.IMAGE_PREFIX_NEW_02_RETURN) || type.equals(Constants.IMAGE_PREFIX_NEW_03_RETURN)) {
                    isImg = true;
                } else if (type.equals("spx")) {
                    isVoice = true;
                } else {
                    isFile = true;
                }
            } else {
                isFile = true;
            }
        }
        if (isFile) {
            fileImg.setImageResource(R.mipmap.fj_img_bg);
            fileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(annexdatas, 3L, 3);
                }
            });
        } else {
            fileImg.setImageResource(R.mipmap.fj_img_bg_gray);
            fileImg.setOnClickListener(null);
        }
        if (isImg) {
            img.setImageResource(R.mipmap.pic_img_bg);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(annexdatas, 1L, 1);
                }
            });
        } else {
            img.setImageResource(R.mipmap.pic_img_bg_gray);
            img.setOnClickListener(null);
        }

        if (isVoice) {
            voiceImg.setImageResource(R.mipmap.voice_img_bg);
            voiceImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showFile(annexdatas, 2L, 2);
                }
            });
        } else {
            voiceImg.setImageResource(R.mipmap.voice_img_bg_gray);
            voiceImg.setOnClickListener(null);
        }
    }


    //
    private List<Annexdata> turnToAnnexdata(List<ApproDataEntity> approData, int a) {
        List<Annexdata> annexdatas = new ArrayList<>();
        List<AnnexListEntity> annexList = approData.get(a).getAnnexList();
        if (null != annexList) {
            for (int i = 0; i < annexList.size(); i++) {
                Annexdata data = new Annexdata();
                data.setId(0);
                data.setBid(approData.get(0).getbId());
                data.setPath(annexList.get(i).getPath());
                data.setCompanyId(0);
                data.setSrcName(annexList.get(i).getSrcName());
                data.setFileSize("100000");
                data.setShowType(Integer.parseInt(annexList.get(i).getShowType()));
                data.setType("jpg");
                annexdatas.add(data);
            }
            return annexdatas;
        } else {
            return annexdatas;
        }
    }

    /**
     * 设置审阅人是否只能选择一个
     *
     * @param isSelectedOne
     */
    protected void setIsSelectedOne(boolean isSelectedOne) {
        this.isSelectedOne = isSelectedOne;
    }


    //清空附件和图片，语音
    protected void clearFileAndPic() {
        mVoiceEntity[clickIndex] = null;
        if (selectedAttachData[clickIndex] != null) {
            selectedAttachData[clickIndex].clear();
        }
        if (addImgPaths[clickIndex] != null) {
            addImgPaths[clickIndex].clear();
        }
        if (null != show_file_img_ll_img) {
            show_file_img_ll_img.setVisibility(View.GONE);
        }
    }

    @Override
    public void playPositionChange(int currentPosition) {
    }

    @Override
    public void playCompletion() {

    }

    @Override
    public void playDuration(int duration) {
        SDLogUtil.debug("duration===" + duration);

    }

    @Override
    public void playException(Exception e) {
        e.printStackTrace();
        //SDToast.showLong("语音文件已损坏");

    }

    @Override
    public void playStopVioce() {

    }

    public String getNowTime() {
        return TimeUtils.timeFormat(System.currentTimeMillis(), "yyyy-MM-dd");
    }

}
