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
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
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
import java.util.List;

import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

/**
 * 我的日志详情
 */
public class MyDailyDetailFragment extends ERPSumbitBaseFragment {
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private static final int REQUEST_CODE_FOR_BORROW_WORK = 209;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private List<String> mEidLists=new ArrayList<>();
    private CustomNavigatorBar mNavigatorBar;
    private RelativeLayout mBarLayout;
    private RoundedImageView mBarRoundImage;
    private TextView mBarName,mBarDepartment;
    private TextView mBarTime,mBarNumber,mBarJob;
    private FontEditext mWorkJournalTitle;
    private FontEditext mWorkJournalExplain;
    private FontTextView mWorkJournalCopy;
    private MyDailyDetailBean.DataBean mDetailBean;
    private String mTime;
    private long mEid;
    private long mBackEid;


    public static Fragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(id)) {
            args.putString("id", id);
        }
        if (!TextUtils.isEmpty(id)) {
            args.putString("type", type);
        }

        MyDailyDetailFragment fragment = new MyDailyDetailFragment();
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
        Bundle bundle=getArguments();
        if (bundle!=null){
            mEid=bundle.getLong("EID");
        }

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("我的日志");
        mNavigatorBar.setRightTextVisible(false);
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
        controllerLayout(false);

        getFragmentData(mEid);
    }

    /**
     * 用于重新设置是否可以提交
     * @param isOpen
     */
    public void setTitleBar(boolean isOpen){
        if (isOpen){
            mNavigatorBar.setRightTextVisible(true);
            mNavigatorBar.setRightText("提交");
        }else{
            mNavigatorBar.setRightTextVisible(false);
        }
    }

    /**
     * 设置是否可以编辑和提交按钮显示
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen){
        setTitleBar(isOpen);
        if (isOpen){
            mBarTime.setOnClickListener(mOnClickListener);
        }else{
            mBarTime.setOnClickListener(null);
        }
        DisplayUtil.editTextable(mWorkJournalTitle,isOpen);
        DisplayUtil.editTextable(mWorkJournalExplain,isOpen);
    }

    /**
     * 设置部门详情内容
     * @param data
     * @param isEdit 控制是否可以编辑
     */
    public void setInfo(MyDailyDetailBean.DataBean data, boolean isEdit){
        controllerLayout(isEdit);
        MyDailyDetailBean.DataBean.WorkLogBean workLog = data.getWorkLog();
        if (workLog==null){
            return;
        }
        //头像
        if (workLog.getIcon()!=null) {
            Glide.with(getActivity())
                    .load(workLog.getIcon())
                    .error(R.mipmap.contact_icon)
                    .into(mBarRoundImage);
        }else{
            Glide.with(getActivity())
                    .load("")
                    .placeholder(R.mipmap.contact_icon)
                    .error(R.mipmap.contact_icon)
                    .into(mBarRoundImage);
        }
        //名字
        if (workLog.getYgName()!=null){
            mBarName.setText(workLog.getYgName());
        }
        //部门
        if (workLog.getYgDeptName()!=null) {
            mBarDepartment.setText(workLog.getYgDeptName());
        }
        //职位
        if (workLog.getYgJob()!=null) {
            mBarJob.setText(workLog.getYgJob());
        }
        //时间
        if (workLog.getCreateTime()!=null) {
            mBarTime.setText("日期："+workLog.getCreateTime());
        }
        //编号
        setNumberVisiable(true);
        if (workLog.getSerNo()!=null) {
            mBarNumber.setText("编号："+ workLog.getSerNo());
        }
        //抄送显示人数
        //抄送
        if (data.getCcList().size()>0){
            mWorkJournalCopy.setOnClickListener(mOnClickListener);
            StringBuilder builder=new StringBuilder();
            for (int i=0;i<data.getCcList().size();i++){
                if (data.getCcList().get(i)!=null && data.getCcList().get(i).getUserName()!=null) {
                    builder.append(data.getCcList().get(i).getUserName()+"、");
                    mEidLists.add(data.getCcList().get(i).getUserId()+"");
                }
            }
            mWorkJournalCopy.setText(builder.substring(0,builder.length()-1));
        }else {
            mWorkJournalCopy.setOnClickListener(null);
        }
        //标题
        if (workLog.getTitle()!=null) {
            mWorkJournalTitle.setText(workLog.getTitle());
        }
        //内容
        if (workLog.getRemark()!=null) {
            mWorkJournalExplain.setText(workLog.getRemark());
        }
        //附件
        if (data.getAnnexList()!=null) {
            checkFileOrImgOrVoice(data.getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
        }


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
                commitData(mBackEid+"");
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
                List<SDUserEntity> userLists=new ArrayList<>();
                if (mEidLists!=null && mEidLists.size()>0) {
                    userLists= mUserDao.findUserByUserId(mEidLists);
                }
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
                intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) userLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_WORK);
            }
        }
    };

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
     * 获取详情
     * @param mEid
     */
    public void getFragmentData(long mEid){
        ListHttpHelper.getMyDaily(mEid+"", mHttpHelper, new SDRequestCallBack(MyDailyDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort("获取失败"+msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                MyDailyDetailBean detailBean = (MyDailyDetailBean) responseInfo.getResult();
                if (detailBean != null) {
                    if (detailBean.getData() != null) {
                        MyDailyDetailBean.DataBean bean = detailBean.getData();
                        if (bean != null) {
                            mDetailBean=bean;
                            mBackEid = bean.getWorkLog().getEid();
                            String selfId=bean.getWorkLog().getYgId()+"";
                            //判断是否是自己，不是的话，设置页面不可以编辑修改
                            if ((selfId).equals(DisplayUtil.getUserInfo(getActivity(),3))) {
                                setInfo(bean,true);
                            }else {
                                setInfo(bean,false);
                            }

                        }
                    }
                }
            }
        });
    }


    /**
     * 提交
     */
    public void commitData(String eid){
        if (mDetailBean==null){
            return;
        }
        MyDailyDetailBean.DataBean.WorkLogBean workLog = mDetailBean.getWorkLog();
        if (workLog==null){
            return;
        }
        String ygId=workLog.getYgId()+"";
        String ygName=workLog.getYgName();
        String ygJob=workLog.getYgJob();
        String ygDeptId=workLog.getYgDeptId()+"";
        String ygDeptName=workLog.getYgDeptName();
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

        postData(title,remark,"", ygId,"",eid,ygDeptId,ygDeptName,ygJob,ygName,mTime);

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
