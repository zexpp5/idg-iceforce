package newProject.company.borrowapplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.cxgz.activity.cx.ui.SDCrmEditTextActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDDictCodeDao;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.cxgz.activity.db.entity.SDDictionaryEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.view.TotalMoneyView;

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
import newProject.company.borrowapplay.bean.BoorowDetailBean;
import newProject.company.borrowapplay.bean.BoorowSubmitBean;
import newProject.utils.ConvertMoneyUtil;
import newProject.utils.NewCommonDialog;
import newProject.utils.TypeConstant;
import yunjing.bean.ListDialogBean;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogListView;
import yunjing.view.DialogNomalView;
import yunjing.view.FullyLinearLayoutManager;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;
import static com.umeng.socialize.utils.DeviceConfig.context;


/**
 * Created by tujingwu on 2017/10/21.
 * 随便写写吧，心累
 */

public class BorrowApplyDetailFragment extends ERPSumbitBaseFragment {
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


    @Bind(R.id.loan_boorow_reason)
    EditText mET_Borrow_Reason;//借支事由
    @Bind(R.id.loan_explain_content)
    EditText mET_Borrow_Explain;//借支说明
    @Bind(R.id.total_money_view)
    TotalMoneyView mTotalMoneyView;
    @Bind(R.id.tv_currency)
    TextView mTv_Currency;//币种
    @Bind(R.id.loan_chaosong_text)
    TextView mTVSend;
    @Bind(R.id.max_money)
    EditText mMax_money;//大写金额

    @Bind(R.id.approval_recycerview)
    RecyclerView mApproval_recycerview;
    @Bind(R.id.approval_layout)
    LinearLayout mApproval_layout;

    @Bind(R.id.marking_layout)
    LinearLayout mMarking_layout;//提醒批阅
    @Bind(R.id.marking_button)
    Button mMarking_Button;//提醒批阅

    @Bind(R.id.currency_layut)
    LinearLayout mCurrencyLayout;//币种布局

    private long mEid;
    private static final int mREQUEST_CODE = 1;
    private boolean IS_CAN_CHANGE = false;
    private List<String> mEidLists = new ArrayList<>();
    private String mApprovalPersonData;

    private List<ListDialogBean> mCurrencyData;//币种弹窗数据
    private String mCurrencyType = "CNY";


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
        mTopBar.setMidText(getResources().getString(R.string.im_business_leave_submit));
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
        DetailHttpHelper.getLoanDetail(getActivity(), mEid + "", new SDRequestCallBack(BoorowDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                BoorowDetailBean bdb = (BoorowDetailBean) responseInfo.getResult();
                if (null != bdb && null != bdb.getData())
                    setData(bdb);
            }

        });


        DetailHttpHelper.getApprovalList(getActivity(), mEid + "", TypeConstant.JK_CODE[0] + "", new SDRequestCallBack(ApprovalBean.class) {
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
    private void setData(BoorowDetailBean bdb) {
        //附件
        if (bdb.getData().getAnnexList() != null) {
            checkFileOrImgOrVoice(bdb.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
        }
        BoorowDetailBean.DataBean.LoanBean loan = bdb.getData().getLoan();
        if (null == loan)
            return;

        setEditext(loan);

        //编号
        if (null != loan.getSerNo())
            mHead_bar_number.setText(loan.getSerNo());
        //头像
        if (loan.getIcon() != null) {
            Glide.with(getActivity())
                    .load(loan.getIcon())
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
        if (loan.getYgName() != null) {
            mHead_bar_name.setText(loan.getYgName());
        }
        //部门
        if (loan.getYgDeptName() != null) {
            mHead_bar_department.setText(loan.getYgDeptName());
        }
        //职位
        if (loan.getYgJob() != null) {
            mHead_bar_job.setText(loan.getYgJob());
        }
        //时间
        if (loan.getCreateTime() != null) {
            mHead_bar_time.setText("日期：" + loan.getCreateTime());
        }

        if (loan.getSerNo() != null) {
            mHead_bar_number.setText("编号：" + loan.getSerNo());
        }

        //发送
        List<BoorowDetailBean.DataBean.LoanBean.ApprovalPersonBean> sendList = loan.getApprovalPerson();
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

        //标题
        if (loan.getTitle() != null) {
            mET_Borrow_Reason.setText(loan.getTitle());
        }
        //内容
        if (loan.getRemark() != null) {
            mET_Borrow_Explain.setText(loan.getRemark());
        }

        //金额小写
        mTotalMoneyView.bindTotalMoney(loan.getMoney());
        //金额大写
        if (null != loan.getBigMoney())
            mMax_money.setText(loan.getBigMoney());

        //币种
        if (null != loan.getCurrencyValue()) {
            mTv_Currency.setText(loan.getCurrencyValue());
        }

        mApprovalPersonData = new Gson().toJson(loan.getApprovalPerson());
    }

    private void setEditext(BoorowDetailBean.DataBean.LoanBean loan) {
        String userId = DisplayUtil.getUserInfo(getActivity(), 3);
        String applayUserId = loan.getYgId() + "";
        String currentApprovalUserId = loan.getApprovalUserId() + "";//当前审批人
        int approvalFinal = loan.getApprovalSta();//审批状态：-1=还没人批审，0=审批中；1=审批完成；


        if (userId.equals(applayUserId) && approvalFinal == -1) {//自己发布并且还没审批，可修改
            IS_CAN_CHANGE = true;
            mTotalMoneyView.setOnClickListener(onclickLisetener);
            mCurrencyLayout.setOnClickListener(onclickLisetener);
        } else {//不可修改编辑
            IS_CAN_CHANGE = false;
            setNoEnable();
            mTopBar.setRightTextVisible(false);

            if (null != loan.getApprovalPerson())
                for (BoorowDetailBean.DataBean.LoanBean.ApprovalPersonBean apb : loan.getApprovalPerson()) {
                    String approvalUserId = apb.getUserId() + "";
                    if (approvalUserId.equals(userId) && currentApprovalUserId.equals(userId)) { //自己是审批人并且到自己审批了，不能编辑，能批审
                        setNoEnable();
                        mTopBar.setRightText("批阅");
                        mTopBar.setRightTextVisible(true);

                        if (approvalFinal == 1)//审批完成了
                            mTopBar.setRightTextVisible(false);
                    }

                    if (userId.equals(applayUserId) || approvalUserId.equals(userId)) {//自己是申请人或审批人
                        mApproval_layout.setVisibility(View.VISIBLE);
                    }

                }

        }

        //审批状态：-1=还没人批审，0=审批中；1=审批完成；
        if (userId.equals(applayUserId) && approvalFinal != 1) {//申请人是自己，并且审批没完成，显示提示审批按钮
            mMarking_layout.setVisibility(View.VISIBLE);
        } else {
            mMarking_layout.setVisibility(View.GONE);
        }

    }

    private void setNoEnable() {
        DisplayUtil.editTextable(mET_Borrow_Reason, false);
        DisplayUtil.editTextable(mET_Borrow_Explain, false);
        DisplayUtil.editTextable(mMax_money, false);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == MONEY_SUMBIT) {
            Bundle bundle1 = data.getExtras();
            String money1 = bundle1.getString("value");
            mTotalMoneyView.bindTotalMoney(Double.parseDouble(money1));

            try {
                mMax_money.setText(ConvertMoneyUtil.convert(money1));
            } catch (Exception e) {
                mMax_money.setText("");
                e.printStackTrace();
            }
        }
    }


    private DialogNomalView mMarkDialog;
    View.OnClickListener onclickLisetener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loan_chaosong_text://发送
                    toContactsList();
                    break;
                case R.id.total_money_view://设置金额
                    setMoney();
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
                case R.id.currency_layut://币种
                    if (null == mCurrencyData || mCurrencyData.isEmpty()) {
                        List<ListDialogBean> staticDataType = getStaticDataType(getActivity(), Constants.CURRENCY_CODE);
                        if (null != staticDataType) {
                            mCurrencyData = null;
                            mCurrencyData = new ArrayList<>();
                            mCurrencyData.addAll(staticDataType);
                        }
                    }
                    initListDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void initListDialog() {
        DialogListView mDialogListView = DialogListView.newBuilder(getActivity())
                .cancelTouchout(false)
                .isMultiselect(false)
                .style(R.style.dialogStyle)
                .setItemListener(new DialogListView.SigleSelectListener() {
                    @Override
                    public void itemSelectListener(ListDialogBean selectItemBean) {
                        mTv_Currency.setText(selectItemBean.getContent());
                        mCurrencyType = selectItemBean.getCurrencyType();
                    }
                })
                .build();
        mDialogListView.show();
        mDialogListView.setTitle("币种");
        mDialogListView.setData(mCurrencyData);
    }


    /**
     * 静态数据获取
     *
     * @param activity
     * @return
     */
    private List<ListDialogBean> getStaticDataType(FragmentActivity activity, String dataType) {
        List<SDDictionaryEntity> lincenseTypeData = SDDictCodeDao.getInstance().findDictByCode(dataType);
        if (null == lincenseTypeData || lincenseTypeData.isEmpty()) {
            ToastUtils.show(activity, "暂无数据");
            return null;
        }

        List<ListDialogBean> lctData = new ArrayList<>();
        for (SDDictionaryEntity sde : lincenseTypeData) {
            ListDialogBean dialogBean = new ListDialogBean();
            dialogBean.setCheck(false);
            dialogBean.setContent(sde.getName());
            dialogBean.setCurrencyType(sde.getValue());

            lctData.add(dialogBean);
        }
        return lctData;
    }


    //提醒批阅
    private void sendNotify() {
        SubmitHttpHelper.postRemind(getContext(), TypeConstant.RM_CODE[2], mEid + "", new SDRequestCallBack() {
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

    private void setMoney() {
        Intent intent8 = new Intent(getActivity(), SDCrmEditTextActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("title", "输入金额");
        bundle1.putString(SDCrmEditTextActivity.VALUES, mTotalMoneyView.getTotalMoney());
        bundle1.putInt(SDCrmEditTextActivity.MAX_SIZE, 8);
        bundle1.putBoolean(SDCrmEditTextActivity.IS_MONEY, true);
        intent8.putExtras(bundle1);
        if (flag_copy.equals("1")) {//多层
            getParentFragment().startActivityForResult(intent8, MONEY_SUMBIT);
        } else {
            startActivityForResult(intent8, MONEY_SUMBIT);
        }

    }


    //检查提交
    private void checkSubmit() {
        if (IS_CAN_CHANGE) {//修改详情

            if (TextUtils.isEmpty(mTVSend.getText())) {
                ToastUtils.show(getActivity(), "发送人不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_Borrow_Reason.getText())) {
                ToastUtils.show(getActivity(), "借支事由不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mET_Borrow_Explain.getText())) {
                ToastUtils.show(getActivity(), "借支说明不能为空！");
                return;
            }

            if (TextUtils.isEmpty(mTotalMoneyView.getTotalMoney()) || "0".equals(mTotalMoneyView.getTotalMoney())) {
                ToastUtils.show(getActivity(), "金额不能为空！");
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
        BoorowSubmitBean bsb = new BoorowSubmitBean();
        bsb.setYgId(DisplayUtil.getUserInfo(getActivity(), 3));
        bsb.setYgName(DisplayUtil.getUserInfo(getActivity(), 1));
        bsb.setYgDeptId(DisplayUtil.getUserInfo(getActivity(), 6));
        bsb.setYgDeptName(DisplayUtil.getUserInfo(getActivity(), 2));
        bsb.setYgJob(DisplayUtil.getUserInfo(getActivity(), 7));


        bsb.setTitle(mET_Borrow_Reason.getText().toString());
        bsb.setRemark(mET_Borrow_Explain.getText().toString());
        bsb.setMoney(mTotalMoneyView.getDoubleTotalMoney() + "");
        bsb.setBigMoney(mMax_money.getText().toString());
        bsb.setCurrencyValue(mCurrencyType);
        bsb.setEid(mEid + "");

        try {
            bsb.setApprovalPerson(mApprovalPersonData);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Progress();

        //提交
        if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))//先传附件
            posFile(bsb);
        else
            posData(bsb, null);
    }

    private void posFile(final BoorowSubmitBean bsb) {
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>() {
                }.getType());
                //附件上传完成后的。调用这个接口。
                posData(bsb, new Gson().toJson(callBean.getData()));
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

    private void posData(BoorowSubmitBean bsb, String annex) {
        SubmitHttpHelper.submitLoan(getActivity(), new Gson().toJson(bsb), annex, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(getContext(), msg);
                progresDialog.dismiss();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToastUtils.show(getContext(), "修改成功");
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
        SubmitHttpHelper.postApproval(getContext(), mEid + "", TypeConstant.JK_CODE[0], inputText, select, new SDRequestCallBack() {
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
    protected int getContentLayout() {
        return R.layout.fragment_borrow_detail_applay;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != progresDialog)
            progresDialog = null;
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
