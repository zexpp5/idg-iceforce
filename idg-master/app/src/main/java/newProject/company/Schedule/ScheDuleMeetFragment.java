package newProject.company.Schedule;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.base_erp.ERPSumbitBaseFragment;
import com.chaoxiang.base.utils.DateUtils;
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
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import newProject.api.ListHttpHelper;
import newProject.bean.CCBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.http.BaseHttpHelper;
import yunjing.http.FileSubmitBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

import static android.app.Activity.RESULT_OK;
import static yunjing.utils.PopupDialog.showmLogisticDialogUtil;

/**
 * 日常会议
 */
public class ScheDuleMeetFragment extends ERPSumbitBaseFragment
{
    private static final int REQUEST_CODE_FOR_BORROW_LEAVE = 803;
    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    private CustomNavigatorBar mNavigatorBar;
    private FontEditext mMeetTitle;
    private FontEditext mMeetAddress;
    private FontEditext mMeetContent;
    private FontTextView mMeetType, mMeetParticipate;
    private FontTextView mMeetStartTime, mMeetEndTime;
    private Button mAddBtn;
    private String mStartTime;
    private String mEndTime;
    private List<CCBean> mJoinLists = new ArrayList<>();
    private List<SDUserEntity> mJoinUserLists = new ArrayList<>();
    private List<SelectItemBean> mTypeList = new ArrayList<>();
    private List<String> mEidLists = new ArrayList<>();
    private SelectItemBean mTypeBean;
    private int mType = 1;
    private long mEid = 0;
    private boolean mIsAdd = false;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_daily_meet_layout;
    }

    @Override
    public void onSelectedImg(List<String> imgPaths)
    {
        if (imgPaths != null)
        {
            this.imgPaths = imgPaths;
        }
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
        if (pickerFile != null)
        {
            this.files = pickerFile;
        }
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
            this.voice = new File(voiceEntity.getFilePath());
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
    protected void init(View view)
    {
        view.setOnClickListener(this);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getLong("EID");
            mIsAdd = bundle.getBoolean("ADD");
        }
        if (mIsAdd)
        {
            talkPhoto();
            setFile();
            recordVoice();
            selectPic();
        }

        mNavigatorBar = (CustomNavigatorBar) view.findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setMidText("日常会议");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightTextOnClickListener(mOnClickListener);

        mMeetTitle = (FontEditext) view.findViewById(R.id.et_title);
        mMeetAddress = (FontEditext) view.findViewById(R.id.et_meet_address);
        mMeetContent = (FontEditext) view.findViewById(R.id.et_meet_content);
        mMeetStartTime = (FontTextView) view.findViewById(R.id.tv_start_time);
        mMeetStartTime.setOnClickListener(mOnClickListener);
        mMeetEndTime = (FontTextView) view.findViewById(R.id.tv_end_time);
        mMeetEndTime.setOnClickListener(mOnClickListener);
        mMeetType = (FontTextView) view.findViewById(R.id.tv_meet_type);
        mMeetType.setOnClickListener(mOnClickListener);
        mMeetParticipate = (FontTextView) view.findViewById(R.id.tv_participant);
        mMeetParticipate.setOnClickListener(mOnClickListener);

        controllerLayout(mIsAdd);
    }

    /**
     * 设置是否可以编辑和提交按钮显示
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen)
    {
        //setTitleBar(isOpen);
        DisplayUtil.editTextable(mMeetTitle, isOpen);
        DisplayUtil.editTextable(mMeetAddress, isOpen);
        DisplayUtil.editTextable(mMeetContent, isOpen);
        DisplayUtil.editTextable(mMeetAddress, isOpen);
        if (isOpen)
        {
            mMeetStartTime.setOnClickListener(mOnClickListener);
            mMeetEndTime.setOnClickListener(mOnClickListener);
            mMeetType.setOnClickListener(mOnClickListener);
            mMeetParticipate.setOnClickListener(mOnClickListener);
        } else
        {
            mMeetStartTime.setOnClickListener(null);
            mMeetEndTime.setOnClickListener(null);
            mMeetType.setOnClickListener(null);
            mMeetParticipate.setOnClickListener(null);
        }
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                getActivity().finish();
            }
            if (v == mNavigatorBar.getRightText())
            {
                //提交或者发送
                commitData();
            }
            if (v == mMeetParticipate)
            {
                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
                if (mIsAdd)
                {
                    intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "选择参与人");
                    intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, true);
                    intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
                } else
                {
                    intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "参与人");
                    intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
                    intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) mEidLists);
                    intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
                }
                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mJoinUserLists);
                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_LEAVE);
            }

            if (v == mMeetStartTime)
            {
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

            }
            if (v == mMeetEndTime)
            {
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
            }

            if (v == mMeetType)
            {
                showType();
            }
        }
    };

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_FOR_BORROW_LEAVE:
                if (resultCode == RESULT_OK && data != null)
                {
                    //返回来的字符串
                    final List<SDUserEntity> userList = (List<SDUserEntity>)
                            data.getSerializableExtra(SDSelectContactActivity.SELECTED_DATA);// str即为回传的值
                    if (userList != null && userList.size() > 0)
                    {
                        mJoinLists.clear();
                        StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < userList.size(); i++)
                        {
                            CCBean bean = new CCBean();
                            bean.setEid(userList.get(i).getEid());
                            bean.setName(userList.get(i).getName());
                            bean.setImAccount(userList.get(i).getImAccount());
                            mJoinLists.add(bean);
                            builder.append(userList.get(i).getName() + "、");
                        }
                        mMeetParticipate.setText(builder.substring(0, builder.length() - 1));
                        mJoinUserLists.clear();
                        mJoinUserLists.addAll(userList);

                    } else
                    {
                        mMeetParticipate.setText("");
                        mJoinLists.clear();
                        mJoinUserLists.clear();
                    }
                }
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (!(getActivity() instanceof FragmentCallBackInterface))
        {
            throw new ClassCastException("Hosting activity must implement FragmentCallBackInterface");
        } else
        {
            mCallBack = (FragmentCallBackInterface) getActivity();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        mCallBack.setSelectedFragment(this);
    }

    public boolean onBackPressed()
    {
        if (!mHandledPress)
        {
            mHandledPress = true;
            return true;
        }
        return false;
    }


    /**
     * 日常会议
     */
    public void commitData()
    {
        String title = mMeetTitle.getText().toString();
        String content = mMeetContent.getText().toString();
        String address = mMeetAddress.getText().toString();
        String type = mMeetType.getText().toString();
        String join = mMeetParticipate.getText().toString();

        //进行自己判断是否为空
        if (TextUtils.isEmpty(title))
        {
            SDToast.showShort("请输入会议标题");
            return;
        } else if (TextUtils.isEmpty(mStartTime))
        {
            SDToast.showShort("请选择开始时间");
            return;
        } else if (TextUtils.isEmpty(mEndTime))
        {
            SDToast.showShort("请选择结束时间");
            return;
        } else if (TextUtils.isEmpty(address))
        {
            SDToast.showShort("请输入会议地点");
            return;
        } else if (TextUtils.isEmpty(type))
        {
            SDToast.showShort("请选择会议种类");
            return;
        } else if (TextUtils.isEmpty(join))
        {
            SDToast.showShort("请选择参与人");
            return;
        } else if (TextUtils.isEmpty(content))
        {
            SDToast.showShort("请输入会议内容");
            return;
        }

        if (!DateUtils.compareTwoDate(mStartTime, mEndTime))
        {
            SDToast.showShort("开始时间不能大于结束时间");
            return;
        }

        showProgress();
        String copyList = "";
        try
        {
            if (mJoinLists != null && mJoinLists.size() > 0)
            {
                copyList = DisplayUtil.copyListToStringArray(mJoinLists);
            } else
            {
                copyList = "";
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (mEid != 0)
        {
            if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))
            {
                postFile(title, copyList, content, mType + "", address, mStartTime, mEndTime, mEid + "");
            } else
            {
                postData(title, copyList, content, mType + "", address, mStartTime, mEndTime, "", mEid + "");
            }
        } else
        {
            if (StringUtils.notEmpty(files) || StringUtils.notEmpty(voice) || StringUtils.notEmpty(imgPaths))
            {
                postFile(title, copyList, content, mType + "", address, mStartTime, mEndTime, "");
            } else
            {
                postData(title, copyList, content, mType + "", address, mStartTime, mEndTime, "", "");
            }
        }
    }

    /**
     * 提交会议
     */
    public void postData(String title, String cc, String remark, String type,
                         String meetPlace, String startTime, String endTime, String annex, String eid)
    {

        ListHttpHelper.commitScheduleMeet(title, cc, remark, type, meetPlace, startTime, endTime, annex, eid, mHttpHelper, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        if (progresDialog != null)
                        {
                            progresDialog.dismiss();
                        }
                        if (msg != null)
                        {
                            SDToast.showShort(msg);
                        } else
                        {
                            SDToast.showShort(R.string.request_fail);
                        }
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (progresDialog != null)
                        {
                            progresDialog.dismiss();
                        }
                        if (mCallBack != null)
                        {
                            mCallBack.refreshList();
                        }
                        getActivity().onBackPressed();
                        SDToast.showShort(R.string.request_succeed);
                    }
                });
    }

    /**
     * 提交附件后提交
     */
    public void postFile(final String title, final String cc, final String remark, final String type,
                         final String meetPlace, final String startTime, final String endTime, final String eid)
    {
        BaseHttpHelper.submitFileApi(getActivity().getApplication(), "", isOriginalImg, files, imgPaths, voice, new FileUpload
                .UploadListener()
        {
            @Override
            public void onSuccess(SDResponseInfo responseInfo, HashMap<String, Object> result)
            {
                FileSubmitBean callBean = new SDGson().fromJson(responseInfo.getResult().toString(), new
                        TypeToken<FileSubmitBean>()
                        {
                        }.getType());
                //附件上传完成后的。调用这个接口。
                postData(title, cc, remark, type, meetPlace, startTime, endTime, new Gson().toJson(callBean.getData()), eid);
            }

            @Override
            public void onProgress(int byteCount, int totalSize)
            {

            }

            @Override
            public void onFailure(Exception ossException)
            {
                if (progresDialog != null)
                {
                    progresDialog.dismiss();
                }
                SDToast.showShort(R.string.request_fail);
            }
        });
    }


    protected void showProgress()
    {
        progresDialog = new ProgressDialog(getActivity());
        progresDialog.setCanceledOnTouchOutside(false);
        progresDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            @Override
            public void onCancel(DialogInterface dialog)
            {
//                upload.cancel();
            }
        });
        progresDialog.setMessage(getString(R.string.Is_post));
        progresDialog.show();
    }


    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {

        @Override
        public void onDateTimeSet(Date date)
        {
            mMeetStartTime.setText(DisplayUtil.mFormatterSubmit.format(date));
            //用于提交的
            mStartTime = DisplayUtil.mFormatterSubmit.format(date);
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
            mMeetEndTime.setText(DisplayUtil.mFormatterSubmit.format(date));
            //用于提交的
            mEndTime = DisplayUtil.mFormatterSubmit.format(date);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };

    private void showType()
    {
        toTypeDatas();
        showmLogisticDialogUtil(mTypeBean, getActivity(), "会议种类", mTypeList, "1", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mTypeBean = bean;
                mTypeBean.setSelectIndex(index);
                mMeetType.setText(bean.getSelectString());
                mType = bean.getIndex();
            }
        });
    }

    //
    private void toTypeDatas()
    {
        mTypeList.clear();
        mTypeList.add(new SelectItemBean("普通会议", 1, "1"));
        mTypeList.add(new SelectItemBean("周会", 2, "2"));
        mTypeList.add(new SelectItemBean("月会", 3, "3"));
    }


}
