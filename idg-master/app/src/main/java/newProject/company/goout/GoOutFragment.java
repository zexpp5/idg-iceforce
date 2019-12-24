package newProject.company.goout;


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
import com.chaoxiang.base.utils.DateUtils;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.bean.CCBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;

/**
 * 外出工作添加
 */
public class GoOutFragment extends ERPSumbitBaseFragment {
    private static final int REQUEST_CODE_FOR_BORROW_TRAVEL = 807;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private RelativeLayout mBarLayout;
    private RoundedImageView mBarRoundImage;
    private TextView mBarName,mBarDepartment;
    private TextView mBarTime,mBarNumber,mBarJob;
    private FontEditext mTravelExplain;
    private FontTextView mTravelSendTo;
    private FontEditext mTravelReson;
    private FontEditext mTravelAdress,mTravelWay;
    private FontTextView mTravelStartTime, mTravelEndTime;
    private String mStartTime,mEndTime;
    private List<CCBean> mSendLists = new ArrayList<>();
    private List<SDUserEntity> mUserLists = new ArrayList<>();

    public static Fragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(id)) {
            args.putString("id", id);
        }
        if (!TextUtils.isEmpty(id)) {
            args.putString("type", type);
        }

        GoOutFragment fragment = new GoOutFragment();
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
    protected void init(View view) {
        view.setOnClickListener(this);
        setFile();
        recordVoice();
        selectPic();

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("工作外出");
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);

        mBarLayout= (RelativeLayout) view.findViewById(R.id.head_bar_layout);
        mBarRoundImage= (RoundedImageView) view.findViewById(R.id.head_bar_icon);
        mBarName= (TextView) view.findViewById(R.id.head_bar_name);
        mBarDepartment= (TextView) view.findViewById(R.id.head_bar_department);
        mBarTime= (TextView) view.findViewById(R.id.head_bar_time);
        mBarNumber= (TextView) view.findViewById(R.id.head_bar_number);
        mBarJob= (TextView) view.findViewById(R.id.head_bar_job);

        mTravelExplain = (FontEditext) view.findViewById(R.id.travel_explain_content_ed);
        mTravelExplain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500),DisplayUtil.getFilter(getActivity())});
        mTravelReson = (FontEditext) view.findViewById(R.id.travel_reson_text);
        mTravelReson.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});
        mTravelSendTo = (FontTextView) view.findViewById(R.id.travel_copy_to_text);
        mTravelSendTo.setOnClickListener(mOnClickListener);
        mTravelStartTime = (FontTextView) view.findViewById(R.id.travel_time_start_text);
        mTravelStartTime.setOnClickListener(mOnClickListener);
        mTravelEndTime = (FontTextView) view.findViewById(R.id.travel_time_end_text);
        mTravelEndTime.setOnClickListener(mOnClickListener);
        mTravelAdress= (FontEditext) view.findViewById(R.id.travel_address_text);
        mTravelAdress.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});
        mTravelWay= (FontEditext) view.findViewById(R.id.travel_way_to_text);
        mTravelWay.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});

        mBarLayout.setOnClickListener(mOnClickListener);
        mBarTime.setText("日期："+ DisplayUtil.getTime());

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
            if (v== mTravelStartTime){
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(startTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();

            }
            if (v== mTravelEndTime){
                new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(endTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
            }

            if (v== mTravelSendTo){
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//                intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mUserLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_TRAVEL);
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_BORROW_TRAVEL:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    final List<SDUserEntity> userList = (List<SDUserEntity>)
                            data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        mSendLists.clear();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < userList.size(); i++)
                        {
                            CCBean bean = new CCBean();
                            bean.setName(userList.get(i).getName());
                            bean.setJob(userList.get(i).getJob());
                            bean.setUserId(userList.get(i).getUserId());
                            bean.setNo(i+1+"");
                            mSendLists.add(bean);
                            builder.append(userList.get(i).getName()+"、");
                        }
                        mTravelSendTo.setText(builder.substring(0,builder.length()-1));
                        mUserLists.clear();
                        mUserLists.addAll(userList);

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
     * 请假申请
     */
    public void commitData(){
        String ygId=DisplayUtil.getUserInfo(getActivity(),3);
        String ygName=DisplayUtil.getUserInfo(getActivity(),1);
        String ygJob=DisplayUtil.getUserInfo(getActivity(),7);
        String ygDeptId=DisplayUtil.getUserInfo(getActivity(),6);
        String ygDeptName=DisplayUtil.getUserInfo(getActivity(),2);
        String remark = mTravelExplain.getText().toString();
        String reson=mTravelReson.getText().toString();
        String address=mTravelAdress.getText().toString();
        String way=mTravelWay.getText().toString();
        String send=mTravelSendTo.getText().toString();

        //进行自己判断是否为空
        if(TextUtils.isEmpty(send)){
            SDToast.showShort("请选择发送人");
            return;
        }else if(TextUtils.isEmpty(reson)){
            SDToast.showShort("请输入外出事由");
            return;
        }else if (TextUtils.isEmpty(mStartTime)) {
            SDToast.showShort("请选择开始时间");
            return;
        }else if(TextUtils.isEmpty(mEndTime)){
            SDToast.showShort("请选择结束时间");
            return;
        }else if(TextUtils.isEmpty(address)){
            SDToast.showShort("请输入目的地");
            return;
        }else if(TextUtils.isEmpty(way)){
            SDToast.showShort("请输入交通工具");
            return;
        }else if (TextUtils.isEmpty(remark)) {
            SDToast.showShort("请输入外出说明");
            return;
        }

        if (!DateUtils.compareTwoDate(mStartTime, mEndTime))
        {
            SDToast.showShort("外出开始时间不能大于结束时间");
            return;
        }

        showProgress();
        String sendList = "";
        try
        {
            if (mSendLists != null && mSendLists.size() > 0)
            {
                sendList = DisplayUtil.approvalListToStringArray(mSendLists);
            } else
            {
                sendList = "";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (StringUtils.notEmpty(files)||StringUtils.notEmpty(voice)||StringUtils.notEmpty(imgPaths)){
            postFile(reson,remark, ygId,"",ygDeptId, ygDeptName, ygJob,ygName, sendList,mStartTime,mEndTime,address,way);
        }else{
            postData(reson,remark, ygId,"","",ygDeptId,ygDeptName,ygJob,ygName,sendList,mStartTime,mEndTime,address,way);
        }


    }


    /**
     * 提交
     */
    public void postData(String reson,String remark, String ygId,String annex,
                         String eid,String ygDeptId,String ygDeptName,String ygJob,String ygName,
                         String approvalPerson,String startTime,String endTime,String targetAddress,
                         String vehicles){

        ListHttpHelper.commitGoOut(reson,remark,ygId,annex,eid,ygDeptId,ygDeptName,ygJob,ygName,
                approvalPerson,startTime, endTime,targetAddress, vehicles,mHttpHelper, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (progresDialog != null) {
                    progresDialog.dismiss();
                }
                if (msg!=null){
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

    /**
     * 提交附件后提交
     * @param eid
     */
    public void postFile(final String reson, final String remark, final String ygId, final String eid, final String ygDeptId,
                         final String ygDeptName, final String ygJob, final String ygName, final String approvalPerson,
                         final String startTime, final String endTime, final String targetAddress, final String vehicles){
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                //附件上传完成后的。调用这个接口。
                postData(reson,remark, ygId,new Gson().toJson(callBean.getData()),eid, ygDeptId,
                        ygDeptName,ygJob,ygName,approvalPerson,startTime,endTime,targetAddress,vehicles);
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


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_travel_apply;
    }



    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {

        @Override
        public void onDateTimeSet(Date date)
        {
            mTravelStartTime.setText(DisplayUtil.mFormatterSubmit.format(date));
            //用于提交的
            mStartTime = DisplayUtil.mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            mTravelEndTime.setText(DisplayUtil.mFormatterSubmit.format(date));
            //用于提交的
            mEndTime = DisplayUtil.mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

}
