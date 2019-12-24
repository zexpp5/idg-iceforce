package newProject.company.daily;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base_erp.ERPSumbitBaseFragment;
import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.FragmentCallBackInterface;
import newProject.bean.CCBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;

/**
 * 我的日志添加
 */
public class MyDailyFragment extends ERPSumbitBaseFragment {
    private static final int REQUEST_CODE_FOR_BORROW_WORK = 203;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private RelativeLayout mBarLayout;
    private RoundedImageView mBarRoundImage;
    private TextView mBarName,mBarDepartment;
    private TextView mBarTime,mBarNumber,mBarJob;
    private FontEditext mWorkJournalTitle;
    private FontEditext mWorkJournalExplain;
    private FontTextView mWorkJournalCopy;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String mTime;
    private List<CCBean> mCopyLists = new ArrayList<>();
    private List<SDUserEntity> mUserLists = new ArrayList<>();

    public static Fragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(id)) {
            args.putString("id", id);
        }
        if (!TextUtils.isEmpty(id)) {
            args.putString("type", type);
        }

        MyDailyFragment fragment = new MyDailyFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSelectedImg(List<String> imgPaths) {
        if (imgPaths!=null){
            this.imgPaths=imgPaths;
        }
    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address) {

    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities) {

    }

    @Override
    public void onClickAttach(List<File> pickerFile) {
        if (pickerFile!=null){
            this.files=pickerFile;
        }
    }

    @Override
    public void onDelAttachItem(View v) {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity) {
        if (voiceEntity!=null){
            this.voice=new File(voiceEntity.getFilePath());
        }
    }

    @Override
    public int getDraftDataType() {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString) {

    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_work_journal;
    }

    @Override
    protected void init(View view) {
        view.setOnClickListener(this);
        setFile();
        recordVoice();
        selectPic();

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("我的日志");
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);

        mBarLayout= (RelativeLayout) view.findViewById(R.id.head_bar_layout);
        mBarRoundImage= (RoundedImageView) view.findViewById(R.id.head_bar_icon);
        mBarName= (TextView) view.findViewById(R.id.head_bar_name);
        mBarDepartment= (TextView) view.findViewById(R.id.head_bar_department);
        mBarTime= (TextView) view.findViewById(R.id.head_bar_time);
        mBarTime.setText("日期："+DisplayUtil.getTime());
        mTime=DisplayUtil.getTime();
        mBarNumber= (TextView) view.findViewById(R.id.head_bar_number);
        mBarLayout.setOnClickListener(mOnClickListener);
        mBarTime.setOnClickListener(mOnClickListener);
        mBarJob= (TextView) view.findViewById(R.id.head_bar_job);

        mWorkJournalTitle = (FontEditext) view.findViewById(R.id.work_journal_title_ed);
        mWorkJournalTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});
        mWorkJournalExplain = (FontEditext) view.findViewById(R.id.work_journal_explain_content_ed);
        mWorkJournalExplain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500),DisplayUtil.getFilter(getActivity())});
        mWorkJournalCopy = (FontTextView) view.findViewById(R.id.work_journal_chaosong_text);
        mWorkJournalCopy.setOnClickListener(mOnClickListener);

        setNumberVisiable(false);

        setInfo();
    }


    /**
     * 设置添加页面个人信息
     */
    public void setInfo(){
        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(),0))
                .error(R.mipmap.contact_icon)
                .into(mBarRoundImage);
        mBarName.setText(DisplayUtil.getUserInfo(getActivity(),1));
        mBarDepartment.setText(DisplayUtil.getUserInfo(getActivity(),2));
        mBarJob.setText(DisplayUtil.getUserInfo(getActivity(),7));
    }

    /**
     * 添加时候不显示
     * 修改或者查看详情显示
     * @param isShow
     */
    private void setNumberVisiable(boolean isShow){
        if (isShow) {
            mBarNumber.setVisibility(View.VISIBLE);
        }else{
            mBarNumber.setVisibility(View.GONE);
        }
    }


    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==mNavigatorBar.getLeftImageView() || v==mNavigatorBar.getLeftText()){
                getActivity().onBackPressed();
            }
            if (v==mNavigatorBar.getRightText()){
                //提交或者发送
                commitData();
            }
            if (v==mBarLayout){
                //跳转个人信息页面
            }
            if (v==mBarTime){
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(editimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }
            if (v==mWorkJournalCopy){
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//                intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mUserLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_WORK);
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_BORROW_WORK:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    final List<SDUserEntity> userList = (List<SDUserEntity>)
                            data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        mCopyLists.clear();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < userList.size(); i++)
                        {
                            CCBean bean = new CCBean();
                            bean.setEid(userList.get(i).getEid());
                            bean.setName(userList.get(i).getName());
                            bean.setImAccount(userList.get(i).getImAccount());
                            mCopyLists.add(bean);
                            builder.append(userList.get(i).getName()+"、");
                        }
                        mWorkJournalCopy.setText(builder.substring(0,builder.length()-1));
                        mUserLists.clear();
                        mUserLists.addAll(userList);

                    } else
                    {
                        mUserLists.clear();
                        mCopyLists.clear();
                        mWorkJournalCopy.setText("");
                    }
                }
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof FragmentCallBackInterface)) {
            throw new ClassCastException("Hosting activity must implement FragmentCallBackInterface");
        } else {
            mCallBack = (FragmentCallBackInterface) getActivity();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mCallBack.setSelectedFragment(this);
    }

    public boolean onBackPressed(){
        if (!mHandledPress){
            mHandledPress = true;
            return true;
        }
        return false;
    }


    /**
     * 提交
     */
    public void commitData(){
        String ygId=DisplayUtil.getUserInfo(getActivity(),3);
        String ygName=DisplayUtil.getUserInfo(getActivity(),1);
        String ygJob=DisplayUtil.getUserInfo(getActivity(),7);
        String ygDeptId=DisplayUtil.getUserInfo(getActivity(),6);
        String ygDeptName=DisplayUtil.getUserInfo(getActivity(),2);


        String title = mWorkJournalTitle.getText().toString();
        String remark = mWorkJournalExplain.getText().toString();
        //进行自己判断是否为空
        if (TextUtils.isEmpty(title)) {
            SDToast.showShort("请输入标题");
            return;
        }else if(TextUtils.isEmpty(mTime)){
            SDToast.showShort("请选择时间");
            return;
        }else if(TextUtils.isEmpty(remark)){
            SDToast.showShort("请输入内容");
            return;
        }
        showProgress();
        String copyList = "";
        try
        {
            if (mCopyLists != null && mCopyLists.size() > 0)
            {
                copyList = DisplayUtil.copyListToStringArray(mCopyLists);
            } else
            {
                copyList = "";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (StringUtils.notEmpty(files)||StringUtils.notEmpty(voice)||StringUtils.notEmpty(imgPaths)){
            postFile(title,remark,copyList, ygId,"",ygDeptId, ygDeptName, ygJob,ygName, mTime);
        }else{
            postData(title,remark,copyList, ygId,"","",ygDeptId,ygDeptName,ygJob,ygName,mTime);
        }
    }

    /**
     * 提交附件后提交
     * @param title
     * @param remark
     * @param eid
     */
    public void postFile(final String title, final String remark, final String cc, final String ygId,
                         final String eid, final String ygDeptId, final String ygDeptName,
                         final String ygJob,final String ygName,final String createTime){
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                //附件上传完成后的。调用这个接口。
                postData(title,remark,cc, ygId,new Gson().toJson(callBean.getData())
                        ,eid,ygDeptId,ygDeptName,ygJob,ygName,createTime);
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                SDToast.showShort(R.string.request_fail);
            }
        });
    }


    /**
     * 日志
     * @param title
     * @param remark
     * @param eid
     * @param annex
     */
    public void postData(String title,String remark, String cc, String ygId,String annex,
                         String eid,String ygDeptId,String ygDeptName,String ygJob,String ygName, String createTime){

        ListHttpHelper.commitMyDaily(title, remark, cc,ygId,annex,eid,ygDeptId,ygDeptName,
                ygJob,ygName,createTime, mHttpHelper, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                if(msg!=null){
                    SDToast.showShort(msg);
                }else {
                    SDToast.showShort(R.string.request_fail);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                if (mCallBack!=null) {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
                SDToast.showShort(R.string.request_succeed);
            }
        });
    }

    protected void showProgress() {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
//                upload.cancel();
            }
        });
        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
    }


    private SlideDateTimeListener editimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            mBarTime.setText("日期："+mFormatter.format(date));
            //用于提交的
            mTime=mFormatter.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };


}
