package newProject.company.collect.allfragment;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
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

import java.io.File;
import java.util.HashMap;
import java.util.List;

import newProject.mine.CollectHttpHelper;
import newProject.company.collect.allbean.CardDetailBean;
import tablayout.view.textview.FontEditext;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

/**
 * 个人卡号
 */
public class CardFragment extends ERPSumbitBaseFragment {
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private FontEditext mCardNumber;
    private FontEditext mOpenBank;
    private FontEditext mPhone;
    private long mEid=0;
    private boolean mIsAdd=false;

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
        return R.layout.card_fragment;
    }

    @Override
    protected void init(View view) {
        view.setOnClickListener(this);
        Bundle bundle=getArguments();
        if (bundle!=null){
            mEid=bundle.getLong("EID");
            mIsAdd=bundle.getBoolean("ADD");
        }
        if (mIsAdd){
            setFile();
            recordVoice();
            selectPic();
        }

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("个人卡号");
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);
        mNavigatorBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        mCardNumber = (FontEditext) view.findViewById(R.id.card_number_text);
        mCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30),DisplayUtil.getFilter(getActivity())});
        mCardNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        mOpenBank = (FontEditext) view.findViewById(R.id.open_bank_ed);
        mOpenBank.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20),DisplayUtil.getFilter(getActivity())});
        mPhone = (FontEditext) view.findViewById(R.id.phone_ed);
        mPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20),DisplayUtil.getFilter(getActivity())});
        mPhone.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if (!mIsAdd && mEid!=0) {
            getFragmentData(mEid);
        }
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
        DisplayUtil.editTextable(mCardNumber,isOpen);
        DisplayUtil.editTextable(mOpenBank,isOpen);
        DisplayUtil.editTextable(mPhone,isOpen);
    }

    /**
     * 获取详情
     * @param mEid
     */
    public void getFragmentData(long mEid){
        CollectHttpHelper.getCard(mEid+"", mHttpHelper, new SDRequestCallBack(CardDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort("获取失败"+msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                CardDetailBean detailBean = (CardDetailBean) responseInfo.getResult();
                if (detailBean != null) {
                    if (detailBean.getData() != null) {
                        CardDetailBean.DataBean bean = detailBean.getData();
                        if (bean != null) {
                            setInfo(detailBean,true);
                        }
                    }
                }
            }
        });
    }


    public void setInfo(CardDetailBean bean, boolean isEdit){
        if (bean.getData()==null){
            return;
        }
        controllerLayout(isEdit);
        CardDetailBean.DataBean.PersonageCardBean comNotice = bean.getData().getPersonageCard();
        if (comNotice==null){
            return;
        }
        if (comNotice.getTelephone()!=null) {
            mPhone.setText(comNotice.getTelephone());
        }
        //标题
        if (comNotice.getCard()!=null) {
            mCardNumber.setText(comNotice.getCard());
        }
        //内容
        if (comNotice.getBank()!=null) {
            mOpenBank.setText(comNotice.getBank());
        }
        //附件
        if (bean.getData().getAnnexList()!=null) {
            checkFileOrImgOrVoice(bean.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img, add_file_btn_detail_img);
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
                commitData(mEid);
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
     * 提交
     * @param mEid
     */
    public void commitData(long mEid){
        String card = mCardNumber.getText().toString();
        String openBank = mOpenBank.getText().toString();
        String phone = mPhone.getText().toString();
        //进行自己判断是否为空
        if (TextUtils.isEmpty(openBank)) {
            SDToast.showShort("请输入开户行");
            return;
        }else if(TextUtils.isEmpty(card)){
            SDToast.showShort("请输入卡号");
            return;
        }else if(TextUtils.isEmpty(phone)){
            SDToast.showShort("请输入电话");
            return;
        }
        showProgress();
        if (mEid==0){
            if (StringUtils.notEmpty(files)||StringUtils.notEmpty(voice)|| StringUtils.notEmpty(imgPaths)){
                postFile(card, openBank,phone,"");
            }else{
                postData(card, openBank,phone,"","");
            }
        }else {
            postData(card, openBank, phone,"", mEid + "");
        }
    }

    /**
     * 提交附件后提交
     */
    public void postFile(final String card, final String openBank, final String phone, final String eid){
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                //附件上传完成后的。调用这个接口。
                postData(card,openBank,phone,new Gson().toJson(callBean.getData()),eid);
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
     * 通知
     */
    public void postData(String card,String openBank, String phone,String annex,String eid){

        CollectHttpHelper.commitCard(card, openBank,phone,annex,eid,
                mHttpHelper, new SDRequestCallBack() {
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


}
