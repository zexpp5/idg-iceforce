package newProject.company.announce;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

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

import newProject.api.ListHttpHelper;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

/**
 * 通知
 */
public class CompanyNotifyFragment extends ERPSumbitBaseFragment {
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress=false;
    private CustomNavigatorBar mNavigatorBar;
    private FontEditext mNotifyTitle;
    private FontEditext mNotifyExplain;
    private FontTextView mNotifyTime;
  /*  private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private String mTime;*/
    private Button mAddBtn;

    public static Fragment newInstance(String id, String type) {
        Bundle args = new Bundle();
        if (!TextUtils.isEmpty(id)) {
            args.putString("id", id);
        }
        if (!TextUtils.isEmpty(id)) {
            args.putString("type", type);
        }

        CompanyNotifyFragment fragment = new CompanyNotifyFragment();
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
        return R.layout.company_notify_fragment;
    }

    @Override
    protected void init(View view) {
        view.setOnClickListener(this);
        talkPhoto();
        setFile();
        recordVoice();
        selectPic();

        mNavigatorBar= (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("通知公告");
        mNavigatorBar.setRightTextVisible(false);

        mNotifyTitle = (FontEditext) view.findViewById(R.id.work_journal_title_ed);
        mNotifyTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(15),DisplayUtil.getFilter(getActivity())});
        mNotifyExplain = (FontEditext) view.findViewById(R.id.work_journal_explain_content_ed);
        mNotifyExplain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500),DisplayUtil.getFilter(getActivity())});
        mNotifyTime = (FontTextView) view.findViewById(R.id.work_journal_chaosong_text);
        mNotifyTime.setText(DisplayUtil.getTime());

        mAddBtn= (Button) view.findViewById(R.id.add_btn);
        mAddBtn.setText("提  交");
        mAddBtn.setOnClickListener(mOnClickListener);

    }



    private View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v==mNavigatorBar.getLeftImageView() || v==mNavigatorBar.getLeftText()){
                getActivity().onBackPressed();
            }
            if (v==mAddBtn){
                //提交或者发送
                commitData();
            }
            /*if (v== mNotifyTime){
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
            }*/
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
     */
    public void commitData(){
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

        if (StringUtils.notEmpty(files)||StringUtils.notEmpty(voice)||StringUtils.notEmpty(imgPaths)){
            postFile(title,remark, ygId,"",ygDeptId, ygDeptName, ygJob,ygName);
        }else{
            postData(title,remark, ygId,"","",ygDeptId,ygDeptName,ygJob,ygName);
        }
    }

    /**
     * 提交附件后提交
     * @param title
     * @param remark
     * @param eid
     */
    public void postFile(final String title, final String remark, final String ygId,
                         final String eid, final String ygDeptId, final String ygDeptName,
                         final String ygJob,final String ygName){
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload.UploadListener() {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result) {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                //附件上传完成后的。调用这个接口。
                postData(title,remark, ygId,new Gson().toJson(callBean.getData())
                        ,eid,ygDeptId,ygDeptName,ygJob,ygName);
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
     * @param title
     * @param remark
     * @param eid
     * @param annex
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


   /* private SlideDateTimeListener editimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            mNotifyTime.setText(mFormatter.format(date));
            //用于提交的
            mTime=mFormatter.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };*/


}
