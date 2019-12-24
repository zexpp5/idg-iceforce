package newProject.company.leaveapplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base_erp.ERPSumbitBaseFragment;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import newProject.api.DetailHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.bean.ApprovalBean;
import newProject.company.leaveapplay.bean.LeaveDetailBean;
import newProject.company.leaveapplay.bean.LeaveSubmitBean;
import newProject.utils.NewCommonDialog;
import newProject.utils.TypeConstant;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogNomalView;
import yunjing.view.FullyLinearLayoutManager;
import yunjing.view.RoundedImageView;

import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by tujingwu on 2017/10/21.
 * 随便写写吧，心累
 */

public class LeaveApplyDetailFragment extends ERPSumbitBaseFragment {
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;


    //个人信息栏
    @Bind(R.id.head_bar_icon)
    RoundedImageView mHead_bar_icon;
    @Bind(R.id.head_bar_name)
    TextView mHead_bar_name;
    @Bind(R.id.head_bar_time)
    TextView mHead_bar_time;
    @Bind(R.id.head_bar_department)
    TextView mHead_bar_department;//部门
    @Bind(R.id.head_bar_job)
    TextView mHead_bar_job;//职位
    @Bind(R.id.head_bar_number)
    TextView mHead_bar_number;//序号


    @Bind(R.id.leave_type_text)
    TextView mTVSend;
    @Bind(R.id.leave_copy_to_text)
    TextView mTVCC;
    @Bind(R.id.leave_time_start_text)
    TextView mTV_StartTime;
    @Bind(R.id.leave_time_end_text)
    TextView mTV_EndTime;
    @Bind(R.id.leave_reson_ed)
    EditText mET_Reason;//事由
    @Bind(R.id.leave_explain_content_ed)
    EditText mET_Content;//说明内容


    @Bind(R.id.approval_recycerview)
    RecyclerView mApproval_recycerview;
    @Bind(R.id.approval_layout)
    LinearLayout mApproval_layout;

    @Bind(R.id.marking_layout)
    LinearLayout mMarking_layout;//提醒批阅
    @Bind(R.id.marking_button)
    Button mMarking_Button;//提醒批阅

    private long mEid;
    private static final int mREQUEST_CODE = 1;
    private boolean IS_CAN_CHANGE = false;
    private List<String> mEidLists = new ArrayList<>();
    private List<String> mEidCCLists = new ArrayList<>();
    private String mApprovalPersonData;//发送人
    private String mCCData;//抄送人

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

    public boolean onBackPressed() {
        if (!mHandledPress) {
            mHandledPress = true;
            return true;
        }
        return false;
    }


    @Override
    protected void init(View view) {
        //初始化附件
        view.setOnClickListener(this);
        selectPic();
        recordVoice();
        setFile();

        getBund();
        initTopBar();
        initListener();
        setInfo();
        getNetData();
    }

    private void getBund() {
        mEid = getArguments().getLong("EID");
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setMidText("休假申请");
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView()) {
                    getActivity().onBackPressed();
                } else if (v == mTopBar.getRightText())
                    checkSubmit();
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mTopBar.setRightTextOnClickListener(topBarListener);
    }

    /**
     * 设置添加页面个人信息
     */
    public void setInfo() {
        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(), 0))
                .error(R.mipmap.contact_icon)
                .into(mHead_bar_icon);
        mHead_bar_name.setText(DisplayUtil.getUserInfo(getActivity(), 1));
        mHead_bar_department.setText(DisplayUtil.getUserInfo(getActivity(), 2));
        mHead_bar_job.setText(DisplayUtil.getUserInfo(getActivity(), 7));

        mHead_bar_time.setText("日期：" + DisplayUtil.getTime());
    }


    private void getNetData() {
        DetailHttpHelper.getHolidayDetail(getActivity(), mEid + "", new SDRequestCallBack(LeaveDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                LeaveDetailBean ldb = (LeaveDetailBean) responseInfo.getResult();
                if (null != ldb && null != ldb.getData())
                    setData(ldb);
            }

        });


        DetailHttpHelper.getApprovalList(getActivity(), mEid + "", TypeConstant.QJ_CODE[0] + "", new SDRequestCallBack(ApprovalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ApprovalBean approvalBean = (ApprovalBean) responseInfo.getResult();
                if (null != approvalBean && null != approvalBean.getData() && approvalBean.getData().size() > 0) {
                    setApprovalData(approvalBean);
                }

            }
        });

    }

    //设置审批数据
    private void setApprovalData(ApprovalBean approvalBean) {
        ApprovalAdapter approvalAdapter = new ApprovalAdapter(R.layout.approval_layout, approvalBean.getData());
        LinearLayoutManager llManger = new FullyLinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        mApproval_recycerview.setNestedScrollingEnabled(false);//防止嵌套卡顿
        mApproval_recycerview.setLayoutManager(llManger);
        mApproval_recycerview.setAdapter(approvalAdapter);
        mApproval_recycerview.setFocusable(false);
    }

    //设置请求回来的数据
    private void setData(LeaveDetailBean ldb) {
        //附件
        if (ldb.getData().getAnnexList() != null) {
            checkFileOrImgOrVoice(ldb.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
        }
        LeaveDetailBean.DataBean.HolidayBean hb = ldb.getData().getHoliday();
        if (null == hb)
            return;

        setEditext(hb);


        //头像
        if (hb.getIcon() != null) {
            Glide.with(getActivity())
                    .load(hb.getIcon())
                    .error(R.mipmap.contact_icon)
                    .into(mHead_bar_icon);
        } else {
            Glide.with(getActivity())
                    .load("")
                    .placeholder(R.mipmap.contact_icon)
                    .error(R.mipmap.contact_icon)
                    .into(mHead_bar_icon);
        }
        //名字
        if (hb.getYgName() != null) {
            mHead_bar_name.setText(hb.getYgName());
        }
        //部门
        if (hb.getYgDeptName() != null) {
            mHead_bar_department.setText(hb.getYgDeptName());
        }
        //职位
        if (hb.getYgJob() != null) {
            mHead_bar_job.setText(hb.getYgJob());
        }
        //时间
        if (hb.getCreateTime() != null) {
            mHead_bar_time.setText("日期：" + hb.getCreateTime());
        }

        if (hb.getSerNo() != null) {
            mHead_bar_number.setText("编号：" + hb.getSerNo());
        }

        if (hb.getStartTime() != null)
            mTV_StartTime.setText(hb.getStartTime());

        if (null != hb.getEndTime())
            mTV_EndTime.setText(hb.getEndTime());

        if (hb.getTitle() != null)
            mET_Reason.setText(hb.getTitle());

        if (null != hb.getRemark())
            mET_Content.setText(hb.getRemark());

        //发送
        List<LeaveDetailBean.DataBean.HolidayBean.ApprovalPersonBean> sendList = hb.getApprovalPerson();
        if (null != sendList && sendList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < sendList.size(); i++) {
                if (sendList.get(i) != null && sendList.get(i).getName() != null) {
                    builder.append(sendList.get(i).getName() + "、");
                    mEidLists.add(sendList.get(i).getUserId() + "");
                }
            }
            mTVSend.setText(builder.substring(0, builder.length() - 1));
        }

        //抄送
        List<LeaveDetailBean.DataBean.CcListBean> ccList = ldb.getData().getCcList();
        if (null != ccList && ccList.size() > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < ccList.size(); i++) {
                if (ccList.get(i) != null && ccList.get(i).getUserName() != null) {
                    builder.append(ccList.get(i).getUserName() + "、");
                    mEidCCLists.add(ccList.get(i).getUserId() + "");
                }
            }
            mTVCC.setText(builder.substring(0, builder.length() - 1));
        }


        mApprovalPersonData = new Gson().toJson(hb.getApprovalPerson());
        mCCData = new Gson().toJson(ldb.getData().getCcList());
    }

    private void setEditext(LeaveDetailBean.DataBean.HolidayBean hb) {
        String userId = DisplayUtil.getUserInfo(getActivity(), 3);
        String applayUserId = hb.getYgId() + "";
        String currentApprovalUserId = hb.getApprovalUserId() + "";//当前审批人
        int approvalFinal = hb.getApprovalSta();//审批状态：-1=还没人批审，0=审批中；1=审批完成；


        if (userId.equals(applayUserId) && approvalFinal == -1) {//自己发布并且还没审批，可修改
            IS_CAN_CHANGE = true;
            mTV_StartTime.setOnClickListener(onclickLisetener);
            mTV_EndTime.setOnClickListener(onclickLisetener);
        } else {//不可修改编辑
            IS_CAN_CHANGE = false;
            setNoEnable();
            mTopBar.setRightTextVisible(false);

            if (null != hb.getApprovalPerson())
                for (LeaveDetailBean.DataBean.HolidayBean.ApprovalPersonBean apb : hb.getApprovalPerson()) {
                    String approvalUserId = apb.getUserId() + "";
                    if (approvalUserId.equals(userId) && currentApprovalUserId.equals(userId)) { //自己是审批人并且到自己审批了，不能编辑，能批审
                        setNoEnable();
                        mTopBar.setRightText("批阅");
                        mTopBar.setRightTextVisible(true);

                        if (approvalFinal == 1)//审批完成了
                            mTopBar.setRightTextVisible(false);
                    }
                    if (userId.equals(applayUserId) || approvalUserId.equals(userId)){//自己是申请人或审批人
                        mApproval_layout.setVisibility(View.VISIBLE);
                    }

                }
        }

        //审批状态：-1=还没人批审，0=审批中；1=审批完成；
        if (userId.equals(applayUserId) && approvalFinal != 1){//申请人是自己，并且审批没完成，显示提示审批按钮
            mMarking_layout.setVisibility(View.VISIBLE);
        }else{
            mMarking_layout.setVisibility(View.GONE);
        }


    }

    private void setNoEnable() {
        DisplayUtil.editTextable(mET_Reason, false);
        DisplayUtil.editTextable(mET_Content, false);

        mTV_StartTime.setCompoundDrawables(null, null, null, null);
        mTV_EndTime.setCompoundDrawables(null, null, null, null);
    }

    private void initListener() {
        mTVCC.setOnClickListener(onclickLisetener);
        mTVSend.setOnClickListener(onclickLisetener);
        mMarking_Button.setOnClickListener(onclickLisetener);
    }


    private class ApprovalAdapter extends BaseQuickAdapter<ApprovalBean.DataBean, BaseViewHolder> {


        public ApprovalAdapter(@LayoutRes int layoutResId, @Nullable List<ApprovalBean.DataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ApprovalBean.DataBean item) {
            holder.setText(R.id.approval_user_name, item.getUserName() + "")
                    .setText(R.id.approval_user_dept, item.getDeptName())
                    .setText(R.id.approval_user_job, item.getJob())
                    .setText(R.id.tv_approval_time, item.getCreateTime())
                    .setText(R.id.tv_approval_content, item.getApprovalRemark());

            Glide.with(getActivity())
                    .load(item.getIcon())
                    .error(R.mipmap.contact_icon)
                    .into((ImageView) holder.getView(R.id.approval_head_icon));

        }
    }


    private DialogNomalView mMarkDialog;
    View.OnClickListener onclickLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.leave_type_text://发送
                    toSendContactsList();
                    break;
                case R.id.leave_copy_to_text://抄送
                    toCCContactsList();
                    break;

                case R.id.leave_time_start_text://开始时间
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
                    break;
                case R.id.leave_time_end_text://结束时间
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
                    break;
                case R.id.marking_button://提醒批阅
                    mMarkDialog = DialogNomalView.newBuilder(getActivity())
                            .setContentLayout(R.layout.dialog_marking_layout)
                            .cancelTouchout(false)
                            .style(R.style.dialogStyle)
                            .addViewOnclick(R.id.btnOk, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendNotify();
                                }
                            })
                            .addViewOnclick(R.id.btnCancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mMarkDialog.dismiss();
                                }
                            })
                            .build();
                    mMarkDialog.show();
                    break;
                default:
                    break;
            }
        }
    };

    //提醒批阅
    private void sendNotify() {
        SubmitHttpHelper.postRemind(getContext(), TypeConstant.RM_CODE[1], mEid + "", new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                    String msg = jsonObject.getString("msg");
                    ToastUtils.show(getContext(), msg);
                    mMarkDialog.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            mTV_StartTime.setText(DisplayUtil.mFormatterSubmit.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            mTV_EndTime.setText(DisplayUtil.mFormatterSubmit.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };


    /**
     * 去到全公司联系人列表
     */
    private void toSendContactsList() {
        List<SDUserEntity> userLists = new ArrayList<>();
        if (mEidLists != null && mEidLists.size() > 0) {
            userLists = mUserDao.findUserByUserId(mEidLists);
        }
        Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        //是否能修改
        intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
        intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) userLists);
        startActivityForResult(intent, mREQUEST_CODE);
    }

    private void toCCContactsList() {
        List<SDUserEntity> userLists = new ArrayList<>();
        if (mEidCCLists != null && mEidCCLists.size() > 0) {
            userLists = mUserDao.findUserByUserId(mEidCCLists);
        }
        Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        //是否能修改
        intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
        intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) userLists);
        startActivityForResult(intent, mREQUEST_CODE);
    }


    //检查提交
    private void checkSubmit() {
        if (IS_CAN_CHANGE) {//修改详情

            if (TextUtils.isEmpty(mTVSend.getText())) {
                ToastUtils.show(getActivity(), "发送人不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mTV_StartTime.getText().toString()) || TextUtils.isEmpty(mTV_EndTime.getText().toString())) {
                ToastUtils.show(getActivity(), "请假时间不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_Reason.getText())) {
                ToastUtils.show(getActivity(), "请假事由不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_Content.getText().toString())) {
                ToastUtils.show(getActivity(), "请假说明不能为空！");
                return;
            }

            if (!DateUtils.compareTwoDate(mTV_StartTime.getText().toString(), mTV_EndTime.getText().toString())) {
                SDToast.showShort("请假开始时间不能大于结束时间");
                return;
            }

            posDetail();
        } else {//批阅
            NewCommonDialog dialog = new NewCommonDialog(getActivity());
            dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener() {
                @Override
                public void onClick(String inputText, String select) {
                    posApprrvoal(inputText, select);
                }

                @Override
                public void onSearchClick(String content) {

                }
            });
            dialog.initDialog().show();
        }
    }

    //提交修改的详情
    private void posDetail() {
        if (null == mApprovalPersonData)
            return;
        LeaveSubmitBean lsb = new LeaveSubmitBean();
        lsb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        lsb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        lsb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        lsb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        lsb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));

        lsb.setStartTime(mTV_StartTime.getText().toString());
        lsb.setEndTime(mTV_EndTime.getText().toString());
        lsb.setTitle(mET_Reason.getText().toString());
        lsb.setRemark(mET_Content.getText().toString());
        lsb.setEid(mEid + "");

        try {
            lsb.setApprovalPerson(mApprovalPersonData);
            lsb.setCc(mCCData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Progress();
        //提交
        if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))//先传附件
            posFile(lsb);
        else
            posData(lsb, null);
    }

    private void posFile(final LeaveSubmitBean lsb) {
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>() {
                }.getType());
                //附件上传完成后的。调用这个接口。
                posData(lsb, new Gson().toJson(callBean.getData()));
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {
                ToastUtils.show(getContext(), ossException.getMessage());
                progresDialog.dismiss();
            }
        });
    }

    private void posData(LeaveSubmitBean lsb, String annex) {
        SubmitHttpHelper.submitHoliday(getActivity(), new Gson().toJson(lsb), annex, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
                progresDialog.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(getContext(), "提交成功");
                progresDialog.dismiss();
                if (mCallBack != null) {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
            }
        });
    }


    //提交审批
    private void posApprrvoal(String inputText, String select) {
        SubmitHttpHelper.postApproval(getContext(), mEid + "", TypeConstant.QJ_CODE[0], inputText, select, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(getContext(), "批阅成功");
                if (mCallBack != null) {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
            }
        });
    }

    protected void Progress() {
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
    public void onDestroy() {
        super.onDestroy();
        if (null != progresDialog)
            progresDialog = null;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_leave_apply;
    }


    //---------------------------------------附件---------------------------------//

    @Override
    public void onSelectedImg(List<String> imgPaths) {
        if (imgPaths != null) {
            this.imgPaths = imgPaths;
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
        if (pickerFile != null) {
            this.files = pickerFile;
        }
    }

    @Override
    public void onDelAttachItem(View v) {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity) {
        if (voiceEntity != null) {
            this.voice = new File(voiceEntity.getFilePath());
        }
    }

    @Override
    public int getDraftDataType() {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString) {

    }
}
