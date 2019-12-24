package newProject.company.affairs;


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

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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
 * 事务申请添加
 */
public class AffairsReportFragment extends ERPSumbitBaseFragment {
    private static final int REQUEST_CODE_FOR_BORROW_AFFAIR = 33;
    private static final int REQUEST_CODE_FOR_AFFAIR = 34;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private RelativeLayout mBarLayout;
    private RoundedImageView mBarRoundImage;
    private TextView mBarName,mBarDepartment;
    private TextView mBarTime,mBarNumber,mBarJob;
    private FontEditext mAffairTitle;
    private FontEditext mAffairExplain;
    private FontTextView mAffairCopy,mAffairSend;
    private List<CCBean> mCopyLists = new ArrayList<>();
    private List<CCBean> mSendLists = new ArrayList<>();
    private List<SDUserEntity> mCopyUserLists = new ArrayList<>();
    private List<SDUserEntity> mSendUserLists = new ArrayList<>();


    public static Fragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(id)) {
            args.putString("id", id);
        }
        if (!TextUtils.isEmpty(id)) {
            args.putString("type", type);
        }

        AffairsReportFragment fragment = new AffairsReportFragment();
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
        return R.layout.fragment_affaris_apply;
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
        mNavigatorBar.setMidText("事务报告");
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);

        mBarLayout= (RelativeLayout) view.findViewById(R.id.head_bar_layout);
        mBarRoundImage= (RoundedImageView) view.findViewById(R.id.head_bar_icon);
        mBarName= (TextView) view.findViewById(R.id.head_bar_name);
        mBarDepartment= (TextView) view.findViewById(R.id.head_bar_department);
        mBarTime= (TextView) view.findViewById(R.id.head_bar_time);
        mBarNumber= (TextView) view.findViewById(R.id.head_bar_number);
        mBarLayout.setOnClickListener(mOnClickListener);
        mBarTime.setText("日期："+ DisplayUtil.getTime());
        mBarJob= (TextView) view.findViewById(R.id.head_bar_job);

        mAffairTitle = (FontEditext) view.findViewById(R.id.affaris_title_ed);
        mAffairTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});
        mAffairExplain = (FontEditext) view.findViewById(R.id.affaris_explain_content_ed);
        mAffairExplain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500),DisplayUtil.getFilter(getActivity())});
        mAffairCopy = (FontTextView) view.findViewById(R.id.affaris_chaosong_text);
        mAffairCopy.setOnClickListener(mOnClickListener);
        mAffairSend = (FontTextView) view.findViewById(R.id.affaris_fasong_text);
        mAffairSend.setOnClickListener(mOnClickListener);
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
                commitData();
            }
            //发送
            if (v==mAffairSend){
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//                intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mSendUserLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_AFFAIR);
            }
            if(v==mAffairCopy){
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//                intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mCopyUserLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_AFFAIR);
            }
        }
    };


    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_BORROW_AFFAIR:
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
                        mAffairCopy.setText(builder.substring(0,builder.length()-1));
                        mCopyUserLists.clear();
                        mCopyUserLists.addAll(userList);

                    } else
                    {
                        mCopyLists.clear();
                        mCopyUserLists.clear();
                        mAffairCopy.setText("");
                    }
                }
            break;
            case REQUEST_CODE_FOR_AFFAIR:
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
                        mAffairSend.setText(builder.substring(0,builder.length()-1));
                        mSendUserLists.clear();
                        mSendUserLists.addAll(userList);

                    } else
                    {
                        mSendLists.clear();
                        mSendUserLists.clear();
                        mAffairSend.setText("");
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

        String send = mAffairSend.getText().toString();
        String title = mAffairTitle.getText().toString();
        String remark = mAffairExplain.getText().toString();
        //进行自己判断是否为空
        if(TextUtils.isEmpty(send)){
            SDToast.showShort("请选择发送人");
            return;
        }else if (TextUtils.isEmpty(title)) {
            SDToast.showShort("请输入标题");
            return;
        }else if(TextUtils.isEmpty(remark)){
            SDToast.showShort("请输入说明");
            return;
        }
        showProgress();
        String copyList = "";
        String sendList="";
        try
        {
            if (mCopyLists != null && mCopyLists.size() > 0)
            {
                copyList = DisplayUtil.copyListToStringArray(mCopyLists);
            } else
            {
                copyList = "";
            }
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
            postFile(title,remark,copyList, ygId,"",ygDeptId, ygDeptName, ygJob,ygName, sendList);
        }else{
            postData(title,remark,copyList, ygId,"","",ygDeptId,ygDeptName,ygJob,ygName,sendList);
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
            final String ygJob,final String ygName,final String approvalPerson){
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                //附件上传完成后的。调用这个接口。
                postData(title,remark,cc, ygId,new Gson().toJson(callBean.getData())
                        ,eid,ygDeptId,ygDeptName,ygJob,ygName,approvalPerson);
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
     * 事务
     * @param title
     * @param remark
     * @param eid
     * @param annex
     */
    public void postData(String title,String remark, String cc, String ygId,String annex,
                         String eid,String ygDeptId,String ygDeptName,String ygJob,String ygName, String approvalPerson){

        ListHttpHelper.commitAffair(title, remark, cc,ygId, annex, eid,ygDeptId,ygDeptName,ygJob,
                ygName, approvalPerson,mHttpHelper, new SDRequestCallBack() {
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
