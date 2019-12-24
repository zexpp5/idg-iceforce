package com.cxgz.activity.cxim.ui.business;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.http.FileSubmitBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontEditext;

import static android.app.Activity.RESULT_OK;


/**
 * 工作日志
 * 2017/08/01
 * selson
 */
public class BusinessWorkJournalFragment extends ERPSumbitBaseFragment
{
    @Bind(R.id.im_business_work_submit_date_edt)
    FontEditext imBusinessWorkSubmitDateEdt;
    @Bind(R.id.im_business_work_submit_title_edt)
    FontEditext imBusinessWorkSubmitTitleEdt;
    @Bind(R.id.im_business_work_submit_remark_edt)
    FontEditext imBusinessWorkSubmitRemarkEdt;
    //系统时间
    private String nowadaysTime;

    public static final int REQUEST_CODE_FOR_WORK_JOURNAL = 111;
    public static final String REQUEST_WORK_SUBMIT = "request_work_submit";

    private boolean isCallBack = true;

    /**
     *
     */
    public static Fragment newInstance(String flage)
    {
        BusinessWorkJournalFragment fragment = new BusinessWorkJournalFragment();
        return fragment;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.erp_im_fragment_work_submit_main;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        isCallBack = true;
    }

    @Override
    protected void init(View view)
    {

    }

    private void initViews()
    {
        setTitle(this.getResources().getString(R.string.im_business_work_record));
        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().finish();
            }
        });

        addRightBtn(this.getResources().getString(R.string.button_send), new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (TextUtils.isEmpty(imBusinessWorkSubmitTitleEdt.getText().toString().trim()))
                {
                    MyToast.showToast(getActivity(), getString(R.string.im_business_submit_title));
                    return;
                }

                if (TextUtils.isEmpty(imBusinessWorkSubmitRemarkEdt.getText().toString().trim()))
                {
                    MyToast.showToast(getActivity(), getString(R.string.im_business_submit_content));
                    return;
                }

                isCallBack = true;
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
                intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, true);
                startActivityForResult(intent, REQUEST_CODE_FOR_WORK_JOURNAL);
            }
        });

        nowadaysTime = getNowTime();
        imBusinessWorkSubmitDateEdt.setText(nowadaysTime);
        imBusinessWorkSubmitDateEdt.setEnabled(false);

        //附件
        setFile();
        recordVoice();
        selectPic();
        talkPhoto();
    }

    List<SDUserEntity> userList = null;

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_WORK_JOURNAL:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    userList = (List<SDUserEntity>) data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        if (isCallBack)
                        {
                            isCallBack = false;
                            if (StringUtils.notEmpty(files) || StringUtils.notEmpty(imgPaths) ||
                                    StringUtils.notEmpty(voice))
                            {
                                postFile();
                            } else
                            {
                                getData(String.valueOf(userList.get(0).getUserId()), "");
                            }
                        }

                    } else
                    {

                        if (isCallBack)
                        {
                            isCallBack = false;
                            getData("", "");
                        }
//                        MyToast.showToast(getActivity(), R.string.request_fail);
                    }
                }
                break;
        }
    }

    private void postFile()
    {
        ImHttpHelper.submitFileApi(getActivity().getApplication(), "", false, files, imgPaths, voice, new FileUpload.UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
            {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new TypeToken<FileSubmitBean>()
                {
                }.getType());
                getData(String.valueOf(userList.get(0).getUserId()), new Gson().toJson(callBean.getData()));
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {

            }

            @Override
            public void onFailure(Exception ossException)
            {
                MyToast.showToast(getActivity(), getResources().getString(R.string.request_fail));
            }

        });
    }

    /**
     * 提交数据
     */
    private void getData(String yourBossId, String annex)
    {
        if (TextUtils.isEmpty(imBusinessWorkSubmitTitleEdt.getText().toString().trim()))
        {
            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_title));
            return;
        }

        if (TextUtils.isEmpty(imBusinessWorkSubmitRemarkEdt.getText().toString().trim()))
        {
            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_content));
            return;
        }

        pairs.clear();

        String name = imBusinessWorkSubmitTitleEdt.getText().toString().trim();
        String remark = imBusinessWorkSubmitRemarkEdt.getText().toString().trim();
        String receiveId = yourBossId;

        showProgress();
        BasicDataHttpHelper.findImBusinessWorkJournal2(getActivity(), name, remark, receiveId, annex, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_fail));
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (progresDialog != null)
                    progresDialog.dismiss();
                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_succeed));
                getActivity().finish();
            }
        });
    }
//
//    /**
//     * 提交数据
//     */
//    private void getData(String yourBossId)
//    {
//        if (TextUtils.isEmpty(imBusinessWorkSubmitTitleEdt.getText().toString().trim()))
//        {
//            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_title));
//            return;
//        }
//
//        if (TextUtils.isEmpty(imBusinessWorkSubmitRemarkEdt.getText().toString().trim()))
//        {
//            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_content));
//            return;
//        }
//
//        pairs.clear();
//
//        String name = imBusinessWorkSubmitTitleEdt.getText().toString().trim();
//        String remark = imBusinessWorkSubmitRemarkEdt.getText().toString().trim();
//        String receiveId = yourBossId;
//
//        showProgress();
//        BasicDataHttpHelper.findImBusinessWorkJournal(getActivity().getApplication(), name, remark, receiveId, false, files, imgPaths, voice, new FileUpload.UploadListener()
//        {
//            @Override
//            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_succeed));
//                getActivity().finish();
//
////                ((BusinessActivity) getActivity()).replaceFragment(new BusinessWorkSubmitFragment());
//            }
//
//            @Override
//            public void onProgress(int byteCount, int totalSize)
//            {
//
//            }
//
//            @Override
//            public void onFailure(Exception ossException)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_fail));
//            }
//        });
//
//
//    }

    private void showProgress()
    {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {

            @Override
            public void onCancel(DialogInterface dialog)
            {
                upload.cancel();
            }
        });
        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialogInterface)
            {
//                if (upload != null ) {
//                    upload.cancel();
//                    ProgressBar pb = (ProgressBar) findViewById(R.id.top_pb);
//                    if(pb != null) {
//                        pb.setProgress(0);
//                    }
//                }
            }
        });
        progresDialog.show();
    }


    @Override
    public void onSelectedImg(List<String> imgPaths)
    {
        this.imgPaths = imgPaths;
    }

    @Override
    public void onClickLocationFunction(double latitude, double longitude, String address)
    {

    }

    @Override
    public void onClickSendRange(List<SDUserEntity> userEntities)
    {

    }

    @Override
    public void onClickSendPerson(List<SDUserEntity> userEntities)
    {

    }

    @Override
    public void onClickAttach(List<File> pickerFile)
    {
        files = pickerFile;
    }

    @Override
    public void onDelAttachItem(View v)
    {

    }

    @Override
    public void onClickVoice(VoiceEntity voiceEntity)
    {
        if (voiceEntity != null)
        {
            voice = new File(voiceEntity.getFilePath());
        }
    }


    @Override
    public int getDraftDataType()
    {
        return 0;
    }

    @Override
    public void onClickMoneyFile(String moneyString)
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        initViews();
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}