package newProject.company.borrowapplay;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base_erp.ERPSumbitBaseFragment;
import com.bumptech.glide.Glide;
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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import newProject.api.SubmitHttpHelper;
import newProject.bean.CCBean;
import newProject.bean.CompanyNoBean;
import newProject.company.borrowapplay.bean.BoorowSubmitBean;
import newProject.utils.ConvertMoneyUtil;
import newProject.utils.TypeConstant;
import yunjing.bean.ListDialogBean;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogListView;
import yunjing.view.RoundedImageView;

import static android.app.Activity.RESULT_OK;


/**
 * Created by tujingwu on 2017/10/21.
 * 随便写写吧，心累
 */

public class BorrowApplySubmitFragment extends ERPSumbitBaseFragment {
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


    @Bind(R.id.loan_borrow_reason)
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
    @Bind(R.id.currency_layut)
    LinearLayout mCurrencyLayout;//币种布局

    private static final int mREQUEST_CODE = 1;
    private ArrayList<CCBean> mCCList;

    private List<SDUserEntity> mSendList;//选择的发送人
    private List<ListDialogBean> mCurrencyData;//币种弹窗数据
    private String mCurrencyType = "CNY";

    @Override
    protected void init(View view) {
        //初始化附件
        view.setOnClickListener(this);
        selectPic();
        recordVoice();
        setFile();

        initTopBar();
        initListener();
        setInfo();
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setMidText(getResources().getString(R.string.im_business_leave_submit));
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView())
                    getActivity().onBackPressed();
                else if (v == mTopBar.getRightText())
                    excuteSubmit();
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

        SubmitHttpHelper.getCompanyNo(getActivity(), TypeConstant.JK_CODE[0], new SDRequestCallBack(CompanyNoBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                mHead_bar_number.setText("");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                CompanyNoBean companyNoBean = (CompanyNoBean) responseInfo.getResult();
                if (null != companyNoBean && null != companyNoBean.getData() && !companyNoBean.getData().isEmpty())
                    mHead_bar_number.setText("编号：" + companyNoBean.getData());
                else
                    mHead_bar_number.setText("");
            }
        });
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
        if (requestCode == mREQUEST_CODE && resultCode == RESULT_OK && data != null)
            getCC(data);
    }

    /**
     * 得到发送人(审批人)
     *
     * @return
     */
    private void getCC(Intent data) {
        //返回来的字符串
        mSendList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值

        if (null == mSendList || mSendList.isEmpty())
            return;

        if (mCCList != null) {
            mCCList.clear();
            mCCList = null;
        }
        mCCList = new ArrayList<>();
        String ccStr = "";
        for (int i = 0; i < mSendList.size(); i++) {
            if (i != mSendList.size() - 1)
                ccStr += mSendList.get(i).getName() + "、";
            else
                ccStr += mSendList.get(i).getName();

            //审批人
            CCBean ccListBean = new CCBean();
            ccListBean.setName(mSendList.get(i).getName());
            ccListBean.setUserId(Long.valueOf(mSendList.get(i).getUserId()));
            ccListBean.setJob(mSendList.get(i).getJob());
            ccListBean.setNo((i + 1) + "");
            mCCList.add(ccListBean);
        }

        mTVSend.setText(ccStr);
    }


    private void initListener() {
        mTVSend.setOnClickListener(onclickLisetener);
        mTotalMoneyView.setOnClickListener(onclickLisetener);
        mCurrencyLayout.setOnClickListener(onclickLisetener);
    }

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


    //提交
    private void excuteSubmit() {
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
            ToastUtils.show(getActivity(), "小写金额不能为空！");
            return;
        }


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

        showProgress();

        try {
            bsb.setApprovalPerson(DisplayUtil.approvalListToStringArray(mCCList));
        } catch (Exception e) {
            e.printStackTrace();
        }


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
                ToastUtils.show(getContext(), "提交成功");
                progresDialog.dismiss();
                if (mCallBack != null) {
                    mCallBack.refreshList();
                }
                getActivity().onBackPressed();
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


    private void setMoney() {
        Intent intent8 = new Intent(getActivity(), SDCrmEditTextActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putString("title", "输入金额");
        bundle1.putString(SDCrmEditTextActivity.VALUES, mTotalMoneyView.getTotalMoney());
        bundle1.putInt(SDCrmEditTextActivity.MAX_SIZE, 9);
        bundle1.putBoolean(SDCrmEditTextActivity.IS_MONEY, true);
        intent8.putExtras(bundle1);
        if (flag_copy.equals("1")) {//多层
            getParentFragment().startActivityForResult(intent8, MONEY_SUMBIT);
        } else {
            startActivityForResult(intent8, MONEY_SUMBIT);
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

    public boolean onBackPressed() {
        if (!mHandledPress) {
            mHandledPress = true;
            return true;
        }
        return false;
    }

    /**
     * 去到全公司联系人列表
     */
    private void toContactsList() {
        Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
        intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
        intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
        intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
        //能选
        intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, true);
        //是否是单选
        intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
        if (null != mSendList && !mSendList.isEmpty())
            intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mSendList);
        startActivityForResult(intent, mREQUEST_CODE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != progresDialog)
            progresDialog = null;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_borrow_submit_applay;
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
