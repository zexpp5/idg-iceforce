package newProject.company.announce;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.base_erp.ERPSumbitBaseFragment;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.io.File;
import java.util.List;

import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

/**
 * 通知
 */
public class CompanyNotifyDetailFragment extends ERPSumbitBaseFragment {
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private FontEditext mNotifyTitle;
    private FontEditext mNotifyExplain;
    private FontTextView mNotifyTime;
    private long mEid;
    private Button mAddBtn;
    private boolean mIsSuper=false;

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
        return R.layout.company_notify_fragment;
    }

    @Override
    protected void init(View view) {
        view.setOnClickListener(this);
        Bundle bundle=getArguments();
        if (bundle!=null){
            mEid=bundle.getLong("EID");
            mIsSuper= bundle.getBoolean("ISSUPER");
        }

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("通知公告");
        mNavigatorBar.setRightTextVisible(false);

        mNotifyTitle = (FontEditext) view.findViewById(R.id.work_journal_title_ed);
        mNotifyTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50),DisplayUtil.getFilter(getActivity())});
        mNotifyExplain = (FontEditext) view.findViewById(R.id.work_journal_explain_content_ed);
        mNotifyExplain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500),DisplayUtil.getFilter(getActivity())});
        mNotifyTime = (FontTextView) view.findViewById(R.id.work_journal_chaosong_text);

        mAddBtn= (Button) view.findViewById(R.id.add_btn);
        mAddBtn.setText("提交");
        mAddBtn.setOnClickListener(mOnClickListener);

        if (mIsSuper){
            mAddBtn.setVisibility(View.GONE);
            DisplayUtil.editTextable(mNotifyTitle,false);
            DisplayUtil.editTextable(mNotifyExplain,false);
        }else {
            mAddBtn.setVisibility(View.GONE);
            DisplayUtil.editTextable(mNotifyTitle,false);
            DisplayUtil.editTextable(mNotifyExplain,false);
        }

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
        //setTitleBar(isOpen);
        DisplayUtil.editTextable(mNotifyTitle,isOpen);
        DisplayUtil.editTextable(mNotifyExplain,isOpen);
    }

    /**
     * 获取详情
     * @param mEid
     */
    public void getFragmentData(long mEid){
        ListHttpHelper.getNotify(mEid+"", mHttpHelper, new SDRequestCallBack(NotifyDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort("获取失败"+msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NotifyDetailBean detailBean = (NotifyDetailBean) responseInfo.getResult();
                if (detailBean != null) {
                    if (detailBean.getData() != null) {
                        NotifyDetailBean.DataBean bean = detailBean.getData();
                        if (bean != null) {
                            String selfId=bean.getComNotice().getCreateId()+"";
                            //判断是否是自己，不是的话，设置页面不可以编辑修改
                            if ((selfId).equals(DisplayUtil.getUserInfo(getActivity(),3))) {
                                setInfo(detailBean,true);
                            }else {
                                setInfo(detailBean,false);
                            }

                        }
                    }
                }
            }
        });
    }


    public void setInfo(NotifyDetailBean bean,boolean isEdit){
        if (bean.getData()==null){
            return;
        }
       // controllerLayout(isEdit);
        NotifyDetailBean.DataBean.ComNoticeBean comNotice = bean.getData().getComNotice();
        if (comNotice==null){
            return;
        }
        //时间
        if (comNotice.getCreateTime()!=null) {
            mNotifyTime.setText(comNotice.getCreateTime());
        }
        //标题
        if (comNotice.getTitle()!=null) {
            mNotifyTitle.setText(comNotice.getTitle());
        }
        //内容
        if (comNotice.getRemark()!=null) {
            mNotifyExplain.setText(comNotice.getRemark());
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
            if (v==mAddBtn){
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
        String ygId=DisplayUtil.getUserInfo(getActivity(),3);
        String ygName=DisplayUtil.getUserInfo(getActivity(),1);
        String ygJob=DisplayUtil.getUserInfo(getActivity(),7);
        String ygDeptId=DisplayUtil.getUserInfo(getActivity(),6);
        String ygDeptName=DisplayUtil.getUserInfo(getActivity(),2);

        String title = mNotifyTitle.getText().toString();
        String remark = mNotifyExplain.getText().toString();
        //进行自己判断是否为空
        if (TextUtils.isEmpty(title)) {
            SDToast.showShort("请输入标题");
            return;
        }else if(TextUtils.isEmpty(remark)){
            SDToast.showShort("请输入内容");
            return;
        }
        showProgress();
        postData(title,remark, ygId,"",mEid+"",ygDeptId,ygDeptName,ygJob,ygName);
    }

    /**
     * 通知
     */
    public void postData(String title,String remark, String ygId,String annex,
                         String eid,String ygDeptId,String ygDeptName,String ygJob,String ygName){

        ListHttpHelper.commitNotify(title, remark,ygId,annex,eid,ygDeptId,ygDeptName,
                ygJob,ygName, mHttpHelper, new SDRequestCallBack() {
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
