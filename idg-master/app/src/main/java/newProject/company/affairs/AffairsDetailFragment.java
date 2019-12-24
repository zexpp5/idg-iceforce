package newProject.company.affairs;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import newProject.api.DetailHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.bean.ApprovalBean;
import newProject.company.affairs.bean.AffairsDetailBean;
import newProject.company.affairs.bean.AffairsSubmitBean;
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

public class AffairsDetailFragment extends ERPSumbitBaseFragment {
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


    @Bind(R.id.affaris_fasong_text)
    TextView mTVSend;
    @Bind(R.id.affaris_chaosong_text)
    TextView mTVCC;
    @Bind(R.id.affaris_title_ed)
    EditText mET_Title;//标题
    @Bind(R.id.affaris_explain_content_ed)
    EditText mET_Content;//内容


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
        mTopBar.setMidText("事务报告");
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
        DetailHttpHelper.getAffairDetail(getActivity(), mEid + "", new SDRequestCallBack(AffairsDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                AffairsDetailBean adb = (AffairsDetailBean) responseInfo.getResult();
                if (null != adb && null != adb.getData())
                    setData(adb);
            }

        });


        DetailHttpHelper.getApprovalList(getActivity(), mEid + "", TypeConstant.SW_CODE[0] + "", new SDRequestCallBack(ApprovalBean.class) {
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
    private void setData(AffairsDetailBean adb) {
        //附件
        if (adb.getData().getAnnexList() != null) {
            checkFileOrImgOrVoice(adb.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
        }
        AffairsDetailBean.DataBean.AffairBean ab = adb.getData().getAffair();
        if (null == ab)
            return;

        setEditext(ab);


        //头像
        if (ab.getIcon() != null) {
            Glide.with(getActivity())
                    .load(ab.getIcon())
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
        if (ab.getYgName() != null) {
            mHead_bar_name.setText(ab.getYgName());
        }
        //部门
        if (ab.getYgDeptName() != null) {
            mHead_bar_department.setText(ab.getYgDeptName());
        }
        //职位
        if (ab.getYgJob() != null) {
            mHead_bar_job.setText(ab.getYgJob());
        }
        //时间
        if (ab.getCreateTime() != null) {
            mHead_bar_time.setText("日期：" + ab.getCreateTime());
        }

        if (ab.getSerNo() != null) {
            mHead_bar_number.setText("编号：" + ab.getSerNo());
        }

        if (ab.getTitle() != null)
            mET_Title.setText(ab.getTitle());

        if (null != ab.getRemark())
            mET_Content.setText(ab.getRemark());

        //发送
        List<AffairsDetailBean.DataBean.AffairBean.ApprovalPersonBean> sendList = ab.getApprovalPerson();
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
        List<AffairsDetailBean.DataBean.CcListBean> ccList = adb.getData().getCcList();
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


        mApprovalPersonData = new Gson().toJson(ab.getApprovalPerson());
        mCCData = new Gson().toJson(adb.getData().getCcList());
    }

    private void setEditext(AffairsDetailBean.DataBean.AffairBean ab) {
        String userId = DisplayUtil.getUserInfo(getActivity(), 3);
        String applayUserId = ab.getYgId() + "";
        String currentApprovalUserId = ab.getApprovalUserId() + "";//当前审批人
        int approvalFinal = ab.getApprovalSta();//审批状态：-1=还没人批审，0=审批中；1=审批完成；


        if (userId.equals(applayUserId) && approvalFinal == -1) {//自己发布并且还没审批，可修改
            IS_CAN_CHANGE = true;
        } else {//不可修改编辑
            IS_CAN_CHANGE = false;
            setNoEnable();
            mTopBar.setRightTextVisible(false);

            if (null != ab.getApprovalPerson())
                for (AffairsDetailBean.DataBean.AffairBean.ApprovalPersonBean apb : ab.getApprovalPerson()) {
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
        DisplayUtil.editTextable(mET_Title, false);
        DisplayUtil.editTextable(mET_Content, false);

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


    DialogNomalView mMarkDialog;
    View.OnClickListener onclickLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.affaris_fasong_text://发送
                    toSendContactsList();
                    break;
                case R.id.affaris_chaosong_text://抄送
                    toCCContactsList();
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
        SubmitHttpHelper.postRemind(getContext(), TypeConstant.RM_CODE[0], mEid + "", new SDRequestCallBack() {
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

    //选择抄送人
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


            if (TextUtils.isEmpty(mET_Title.getText())) {
                ToastUtils.show(getActivity(), "报告标题不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_Content.getText().toString())) {
                ToastUtils.show(getActivity(), "报告内容不能为空！");
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
        AffairsSubmitBean asb = new AffairsSubmitBean();
        asb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        asb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        asb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        asb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        asb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));

        asb.setTitle(mET_Title.getText().toString());
        asb.setRemark(mET_Content.getText().toString());
        asb.setEid(mEid + "");

        try {
            asb.setApprovalPerson(mApprovalPersonData);
            asb.setCc(mCCData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //提交
        if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))//先传附件
            posFile(asb);
        else
            posData(asb, null);
    }

    private void posFile(final AffairsSubmitBean asb) {
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>() {
                }.getType());
                //附件上传完成后的。调用这个接口。
                posData(asb, new Gson().toJson(callBean.getData()));
            }

            @Override
            public void onProgress(int byteCount, int totalSize) {

            }

            @Override
            public void onFailure(Exception ossException) {
                ToastUtils.show(getContext(), ossException.getMessage());
            }
        });
    }

    private void posData(AffairsSubmitBean asb, String annex) {
        SubmitHttpHelper.submitAffair(getActivity(), new Gson().toJson(asb), annex, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(getContext(), "提交成功");
                if (mCallBack != null) {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
            }
        });
    }


    //提交审批
    private void posApprrvoal(String inputText, String select) {
        SubmitHttpHelper.postApproval(getContext(), mEid + "", TypeConstant.SW_CODE[0], inputText, select, new SDRequestCallBack() {
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


    @Override
    protected int getContentLayout() {
        return R.layout.fragment_affaris_apply;
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
