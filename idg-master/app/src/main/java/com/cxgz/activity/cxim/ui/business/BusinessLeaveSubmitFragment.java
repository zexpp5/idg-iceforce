package com.cxgz.activity.cxim.ui.business;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.base.utils.YearPickerDialog;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.injoy.idg.R;
import com.cxgz.activity.cxim.ui.business.bean.LeaveBean;
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;

import static android.app.Activity.RESULT_OK;

/**
 * User: Selson
 * Date: 2016-11-17
 * FIXME 请假提交
 */
public class BusinessLeaveSubmitFragment extends ERPSumbitBaseFragment
{

    private SimpleDateFormat mFormatter = new SimpleDateFormat("MM月dd日  HH:mm");

    private SimpleDateFormat mFormatterSubmit = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private String startTimeString = "";
    private String endTimeString = "";

    @Bind(R.id.im_business_work_submit_date_edt)
    FontEditext imBusinessWorkSubmitDateEdt;
    @Bind(R.id.im_business_work_submit_title_edt)
    FontEditext imBusinessWorkSubmitTitleEdt;
    @Bind(R.id.im_business_work_submit_remark_edt)
    FontEditext imBusinessWorkSubmitRemarkEdt;
    @Bind(R.id.im_business_leave_start_time_edt)
    FontTextView imBusinessLeaveStartTimeEdt;
    @Bind(R.id.im_business_leave_end_time_edt)
    FontTextView imBusinessLeaveEndTimeEdt;
    //系统时间
    private String nowadaysTime;

    public static final int REQUEST_CODE_FOR_BORROW_ACHIEVEMENT = 110;
    private boolean isCallBack = true;

    /**
     *
     */
    public static Fragment newInstance(String flage)
    {
        BusinessLeaveSubmitFragment fragment = new BusinessLeaveSubmitFragment();
        return fragment;
    }

    @Override
    public void onStart()
    {
        super.onStart();
        isCallBack = true;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.erp_im_fragment_leave_submit_main;
    }

    @Override
    protected void init(View view)
    {

    }

    private void initViews()
    {
        setTitle(this.getResources().getString(R.string.im_business_leave_submit));

        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
//                getActivity().finish();
                final Calendar calendar = Calendar.getInstance();
                new YearPickerDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(Calendar.YEAR, year);
                        MyToast.showToast(getActivity(), DateUtils.clanderTodatetime(calendar, "yyyy"));
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();

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

                if (StringUtils.empty(startTimeString) || StringUtils.empty(endTimeString))
                {
                    MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_time));
                    return;
                }

                if (!DateUtils.compareTwoDate(startTimeString, endTimeString))
                {
                    MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_start_time_than_end));
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
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_ACHIEVEMENT);
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
            case REQUEST_CODE_FOR_BORROW_ACHIEVEMENT:
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
        /*ImHttpHelper.submitFileApi(getActivity().getApplication(), "", false, files, imgPaths, voice, new FileUpload.UploadListener()
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

        });*/
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

        if (StringUtils.empty(startTimeString) || StringUtils.empty(endTimeString))
        {
            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_time));
            return;
        }

        if (!DateUtils.compareTwoDate(startTimeString, endTimeString))
        {
            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_start_time_than_end));
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

        LeaveBean leaveBean = new LeaveBean();
        leaveBean.setName(name);
        leaveBean.setRemark(remark);
        leaveBean.setReceiveId(receiveId);
        leaveBean.setStartDate(startTimeString);
        leaveBean.setEndDate(endTimeString);

//        BasicDataHttpHelper.findLeaveSubmit2(getActivity(), new Gson().toJson(leaveBean), annex, new SDRequestCallBack()
//        {
//            @Override
//            public void onRequestFailure(HttpException error, String msg)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_fail));
//            }
//
//            @Override
//            public void onRequestSuccess(SDResponseInfo responseInfo)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_succeed));
//                getActivity().finish();
//            }
//        });

    }
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
//        if (StringUtils.empty(startTimeString) && StringUtils.empty(endTimeString))
//        {
//            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_time));
//            return;
//        }
//
//        if (!DateUtils.compareTwoDate(startTimeString, endTimeString))
//        {
//            MyToast.showToast(getActivity(), getString(R.string.im_business_submit_leave_start_time_than_end));
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
//
//        String receiveId = yourBossId;
//
//        showProgress();
//        BasicDataHttpHelper.findLeaveSubmit(getActivity().getApplication(), name, remark, receiveId, startTimeString, endTimeString, false, files, imgPaths, voice, new FileUpload.UploadListener()
//        {
//            @Override
//            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
//            {
//                if (progresDialog != null)
//                    progresDialog.dismiss();
//                MyToast.showToast(getActivity(), getActivity().getResources().getString(R.string.request_succeed));
//                getActivity().finish();
//                // ((BusinessActivity) getActivity()).replaceFragment(new BusinessAchievementSubmitFragment());
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
    public void onClickMoneyFile(String moneyString) {

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

    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {

        @Override
        public void onDateTimeSet(Date date)
        {
            imBusinessLeaveStartTimeEdt.setText(mFormatter.format(date));
            //用于提交的
            startTimeString = mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    private SlideDateTimeListener endTimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            imBusinessLeaveEndTimeEdt.setText(mFormatter.format(date));
            //用于提交的
            endTimeString = mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    @OnClick({R.id.im_business_leave_start_time_edt, R.id.im_business_leave_end_time_edt})
    public void onClick(View view)
    {
        super.onClick(view);
        switch (view.getId())
        {
            case R.id.im_business_leave_start_time_edt:
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

            case R.id.im_business_leave_end_time_edt:
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
        }
    }

//    @OnTextChanged(value = R.id.im_business_money_submit_edt, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
//    void afterTextChanged(Editable edt)
//    {
//        String temp = imBusinessMoneySubmitEdt.getText().toString();
//        int posDot = temp.indexOf(".");
//        if (posDot <= 0)
//        {
//            if (temp.length() <= 4)
//            {
//                return;
//            } else
//            {
//                edt.delete(4, 5);
//                return;
//            }
//        }
//        if (temp.length() - posDot - 1 > 2)
//        {
//            edt.delete(posDot + 3, posDot + 4);
//        }
//    }

}