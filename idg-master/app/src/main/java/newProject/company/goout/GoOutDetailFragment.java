package newProject.company.goout;

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
import newProject.company.goout.bean.GoOutDetailBean;
import newProject.company.goout.bean.GoOutSubmitBean;
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

public class GoOutDetailFragment extends ERPSumbitBaseFragment {
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


    @Bind(R.id.travel_copy_to_text)
    TextView mTVSend;
    @Bind(R.id.travel_address_text)
    EditText mET_TravelAddress;//目的地
    @Bind(R.id.travel_way_to_text)
    EditText mET_TravelWay;//交通工具
    @Bind(R.id.travel_time_start_text)
    TextView mTV_TimeStart;
    @Bind(R.id.travel_time_end_text)
    TextView mTV_TimeEnd;
    @Bind(R.id.travel_reson_text)
    EditText mET_Reason;
    @Bind(R.id.travel_explain_content_ed)
    EditText mET_ExplainContent;//说明

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
    private String mApprovalPersonData;

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
        mTopBar.setMidText(getResources().getString(R.string.im_business_borrow_money_submit));
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
        mHead_bar_number.setText("");
    }


    private void getNetData() {
        DetailHttpHelper.getOutWorkDetail(getActivity(), mEid + "", new SDRequestCallBack(GoOutDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                GoOutDetailBean gdb = (GoOutDetailBean) responseInfo.getResult();
                if (null != gdb && null != gdb.getData())
                    setData(gdb);
            }

        });


        DetailHttpHelper.getApprovalList(getActivity(), mEid + "", TypeConstant.OW_CODE[0] + "", new SDRequestCallBack(ApprovalBean.class) {
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
    private void setData(GoOutDetailBean gdb) {
        //附件
        if (gdb.getData().getAnnexList() != null) {
            checkFileOrImgOrVoice(gdb.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
        }
        GoOutDetailBean.DataBean.OutWorkBean owb = gdb.getData().getOutWork();
        if (null == owb)
            return;

        setEditext(owb);


        //头像
        if (owb.getIcon() != null) {
            Glide.with(getActivity())
                    .load(owb.getIcon())
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
        if (owb.getYgName() != null) {
            mHead_bar_name.setText(owb.getYgName());
        }
        //部门
        if (owb.getYgDeptName() != null) {
            mHead_bar_department.setText(owb.getYgDeptName());
        }
        //职位
        if (owb.getYgJob() != null) {
            mHead_bar_job.setText(owb.getYgJob());
        }
        //时间
        if (owb.getCreateTime() != null) {
            mHead_bar_time.setText("日期：" + owb.getCreateTime());
        }

        if (owb.getSerNo() != null) {
            mHead_bar_number.setText("编号：" + owb.getSerNo());
        }

        //发送
        List<GoOutDetailBean.DataBean.OutWorkBean.ApprovalPersonBean> sendList = owb.getApprovalPerson();
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

        //目的地
        if (owb.getTargetAddress() != null) {
            mET_TravelAddress.setText(owb.getTargetAddress());
        }
        //交通工具
        if (owb.getRemark() != null) {
            mET_TravelWay.setText(owb.getVehicles());
        }

        //外出时间
        if (null != owb.getStartTime())
            mTV_TimeStart.setText(owb.getStartTime());
        //至
        if (null != owb.getEndTime())
            mTV_TimeEnd.setText(owb.getEndTime());

        //事由
        if (null != owb.getReason()) {
            mET_Reason.setText(owb.getReason());
        }
        //说明
        if (null != owb.getRemark())
            mET_ExplainContent.setText(owb.getRemark());

        mApprovalPersonData = new Gson().toJson(owb.getApprovalPerson());
    }

    private void setEditext(GoOutDetailBean.DataBean.OutWorkBean owb) {
        String userId = DisplayUtil.getUserInfo(getActivity(), 3);
        String applayUserId = owb.getYgId() + "";
        String currentApprovalUserId = owb.getApprovalUserId() + "";//当前审批人
        int approvalFinal = owb.getApprovalSta();//审批状态：-1=还没人批审，0=审批中；1=审批完成；


        if (userId.equals(applayUserId) && approvalFinal == -1) {//自己发布并且还没审批，可修改
            IS_CAN_CHANGE = true;
            mTV_TimeStart.setOnClickListener(onclickLisetener);
            mTV_TimeEnd.setOnClickListener(onclickLisetener);
        } else {//不可修改编辑
            IS_CAN_CHANGE = false;
            setNoEnable();
            mTopBar.setRightTextVisible(false);

            if (null != owb.getApprovalPerson())
                for (GoOutDetailBean.DataBean.OutWorkBean.ApprovalPersonBean apb : owb.getApprovalPerson()) {
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
        DisplayUtil.editTextable(mET_TravelAddress, false);
        DisplayUtil.editTextable(mET_TravelWay, false);
        DisplayUtil.editTextable(mET_Reason, false);
        DisplayUtil.editTextable(mET_ExplainContent, false);

        mTV_TimeStart.setCompoundDrawables(null, null, null, null);
        mTV_TimeEnd.setCompoundDrawables(null, null, null, null);
    }

    private void initListener() {
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
                case R.id.travel_copy_to_text://发送
                    toContactsList();
                    break;
                case R.id.travel_time_start_text://开始时间
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
                case R.id.travel_time_end_text://结束时间
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
                default:
                    break;
            }
        }
    };


    //提醒批阅
    private void sendNotify() {
        SubmitHttpHelper.postRemind(getContext(), TypeConstant.RM_CODE[3], mEid + "", new SDRequestCallBack() {
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
            mTV_TimeStart.setText(DisplayUtil.mFormatterSubmit.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener() {
        @Override
        public void onDateTimeSet(Date date) {
            mTV_TimeEnd.setText(DisplayUtil.mFormatterSubmit.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {

        }
    };

    /**
     * 去到全公司联系人列表
     */
    private void toContactsList() {
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


    //检查提交
    private void checkSubmit() {
        if (IS_CAN_CHANGE) {//修改详情

            if (TextUtils.isEmpty(mTVSend.getText())) {
                ToastUtils.show(getActivity(), "发送人不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_TravelAddress.getText())) {
                ToastUtils.show(getActivity(), "外出地不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_TravelWay.getText())) {
                ToastUtils.show(getActivity(), "交通工具不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mTV_TimeStart.getText().toString()) || TextUtils.isEmpty(mTV_TimeEnd.getText().toString())) {
                ToastUtils.show(getActivity(), "外出时间不能为空！");
                return;
            }

            if (!DateUtils.compareTwoDate(mTV_TimeStart.getText().toString(), mTV_TimeEnd.getText().toString())) {
                SDToast.showShort("外出开始时间不能大于结束时间");
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
        GoOutSubmitBean gsb = new GoOutSubmitBean();
        gsb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        gsb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        gsb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        gsb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        gsb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));

        gsb.setTargetAddress(mET_TravelAddress.getText().toString());
        gsb.setVehicles(mET_TravelWay.getText().toString());
        gsb.setReason(mET_Reason.getText().toString());
        gsb.setRemark(mET_ExplainContent.getText().toString());
        gsb.setStartTime(mTV_TimeStart.getText().toString());
        gsb.setEndTime(mTV_TimeEnd.getText().toString());
        gsb.setEid(mEid + "");

        try {
            gsb.setApprovalPerson(mApprovalPersonData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Progress();

        //提交
        if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))//先传附件
            posFile(gsb);
        else
            posData(gsb, null);
    }

    private void posFile(final GoOutSubmitBean gsb) {
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>() {
                }.getType());
                //附件上传完成后的。调用这个接口。
                posData(gsb, new Gson().toJson(callBean.getData()));
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

    private void posData(GoOutSubmitBean gsb, String annex) {
        SubmitHttpHelper.submitOutWork(getActivity(), new Gson().toJson(gsb), annex, new SDRequestCallBack() {
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
        SubmitHttpHelper.postApproval(getContext(), mEid + "", TypeConstant.OW_CODE[0], inputText, select, new SDRequestCallBack() {
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
        return R.layout.fragment_travel_detail;
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
