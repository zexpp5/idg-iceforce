package com.cxgz.activity.cxim.ui.business;

import android.app.Activity;
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
import com.cxgz.activity.cxim.ui.business.bean.UserBean;
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
import com.utils.SPUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontEditext;

import static android.app.Activity.RESULT_OK;
import static com.cxgz.activity.cxim.ui.business.BusinessWorkJournalFragment.REQUEST_WORK_SUBMIT;

/**
 * User: Selson
 * Date: 2016-11-17
 * FIXME 公司圈-类似朋友圈提交
 */
public class BusinessWorkSubmitFragment extends ERPSumbitBaseFragment
{
    @Bind(R.id.im_business_work_submit_remark_edt)
    FontEditext imBusinessWorkSubmitRemarkEdt;
    //系统时间
    private String nowadaysTime;

    public static final int REQUEST_CODE_FOR_WORK = 108;

    private String mUserId = "";

    /**
     *
     */
    public static Fragment newInstance(String flage)
    {
        BusinessWorkSubmitFragment fragment = new BusinessWorkSubmitFragment();
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
    }

    @Override
    protected void init(View view)
    {
        mUserId = (String) SPUtils.get(getActivity(), SPUtils.USER_ID, "");
    }

    private void initViews()
    {
        setTitle(this.getResources().getString(R.string.im_business_work_group));
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
                if (TextUtils.isEmpty(imBusinessWorkSubmitRemarkEdt.getText().toString().trim()))
                {
                    MyToast.showToast(getActivity(), getString(R.string.im_business_submit_content));
                    return;
                }
//                isCallBack = true;
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, true);
                intent.putExtra(SDSelectContactActivity.NEED_ONCLICK_TO_CONFIRM, true);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
                startActivityForResult(intent, REQUEST_CODE_FOR_WORK);
            }
        });

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
            case REQUEST_CODE_FOR_WORK:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    userList = (List<SDUserEntity>) data.getSerializableExtra(com.cxgz.activity.cxim.base
                            .SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        if (StringUtils.notEmpty(files) || StringUtils.notEmpty(imgPaths) ||
                                StringUtils.notEmpty(voice))
                        {
                            postFile(getList(userList));
                        } else
                        {
                             getData(getList(userList), "");
                        }


                    } else
                    {
                        getData("", "");
                    }
                }
                break;
        }
    }

    private String getList(List<SDUserEntity> userList)
    {
        List<UserBean> userBeanList = new ArrayList<UserBean>();
        String userString = "";
        if (StringUtils.notEmpty(userList))
        {
            for (int i = 0; i < userList.size(); i++)
            {
                UserBean bean = new UserBean();
                bean.setEid(userList.get(i).getEid());
                bean.setName(userList.get(i).getName());
                bean.setImAccount(userList.get(i).getImAccount());
                userBeanList.add(bean);
            }
            try
            {
                userString = copyListToStringArray(userBeanList);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            ;
        }

        return userString;
    }

    /**
     * 转抄送
     *
     * @param copyLists
     * @return
     * @throws Exception
     */
    public String copyListToStringArray(List<UserBean> copyLists) throws Exception
    {
        JSONArray array = new JSONArray();
        if (copyLists == null)
        {
            return null;
        }
        if (copyLists.size() > 0)
        {
            for (int i = 0; i < copyLists.size(); i++)
            {
                JSONObject object = new JSONObject();
                object.put("name", copyLists.get(i).getName());
                object.put("eid", copyLists.get(i).getEid());
                object.put("imAccount", copyLists.get(i).getImAccount());
                array.put(object);
            }
        } else
        {
            return null;
        }

        return array.toString();
    }

    private void postFile(final String reId)
    {
        showProgress();

        ImHttpHelper.submitFileApi(getActivity().getApplication(), "", false, files, imgPaths, voice, new FileUpload
                .UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
            {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new
                        TypeToken<FileSubmitBean>()
                        {
                        }.getType());
                getData(reId, new Gson().toJson(callBean.getData()));
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

    private void getData(String userInfo, String annex)
    {
        if (TextUtils.isEmpty(imBusinessWorkSubmitRemarkEdt.getText().toString().trim()))
        {
            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_content));
            return;
        }

        pairs.clear();

        String remark = imBusinessWorkSubmitRemarkEdt.getText().toString().trim();

        showProgress();
        BasicDataHttpHelper.findImBusinessWorkSubmit2(getActivity(), "", remark, userInfo, annex, new SDRequestCallBack()
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

                Intent data = new Intent();
                data.putExtra(REQUEST_WORK_SUBMIT, true);
                getActivity().setResult(Activity.RESULT_OK, data);

                getActivity().finish();
            }
        });
    }
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