package newProject.ui.work.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.base_erp.ERPSumbitBaseFragment;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.ContactActivity;
import com.cxgz.activity.cxim.MumberContactActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.cxgz.activity.cxim.person.SDPersonalAlterActivity;
import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.http.FileUpload;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.superdata.im.entity.Members;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.slidedatetimepicker.SlideDateTimeListener;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.bean.CCBean;
import newProject.company.Schedule.ScheduleDetailBean;
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
public class ScheDuleMeetDetailFragment extends ERPSumbitBaseFragment
{
    private static final int REQUEST_CODE_FOR_BORROW_LEAVE = 803;
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.tv_meeting_num)
    FontTextView tvMeetingNum;
    @Bind(R.id.btn_more)
    FontTextView btnCheck;
    @Bind(R.id.et_meet_content)
    FontEditext et_meet_content;

    @Bind(R.id.edt_title)
    FontTextView edt_title;

    @Bind(R.id.tv_and_view)
    FontTextView tv_and_view;
    @Bind(R.id.tv_end_time)
    FontTextView tv_end_time;

    @Bind(R.id.ll_annex)
    LinearLayout llAnnex;

    private FragmentCallBackInterface mCallBack;
    private boolean mHandledPress = false;
    private CustomNavigatorBar mNavigatorBar;
    private FontEditext mMeetAddress;
    private FontEditext mMeetContent;
    private FontTextView mMeetStartTime;
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
    //会议参与人列表
    private List<Members> TmpMemberList = new ArrayList<Members>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_daily_meet_detail_layout;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
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

        mMeetAddress = (FontEditext) view.findViewById(R.id.et_meet_address);
        mMeetContent = (FontEditext) view.findViewById(R.id.et_meet_content);
        mMeetStartTime = (FontTextView) view.findViewById(R.id.tv_start_time);
        mMeetStartTime.setOnClickListener(mOnClickListener);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        if (!mIsAdd && mEid != 0)
        {
            getFragmentData(mEid);
        }
        controllerLayout(false);
    }

    /**
     * 设置是否可以编辑和提交按钮显示
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen)
    {
        //setTitleBar(isOpen);
        DisplayUtil.editTextable(et_meet_content, isOpen);
        DisplayUtil.editTextable(mMeetAddress, isOpen);
        DisplayUtil.editTextable(mMeetContent, isOpen);
        DisplayUtil.editTextable(mMeetAddress, isOpen);

        if (isOpen)
        {
            mMeetStartTime.setOnClickListener(mOnClickListener);
        } else
        {
            mMeetStartTime.setOnClickListener(null);
        }
    }


    /**
     * 获取详情
     *
     * @param mEid
     */
    public void getFragmentData(long mEid)
    {
        ListHttpHelper.getSchedule(mEid + "", mHttpHelper, new SDRequestCallBack(ScheduleDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort("获取失败" + msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ScheduleDetailBean detailBean = (ScheduleDetailBean) responseInfo.getResult();
                if (detailBean != null)
                {
                    if (detailBean.getData() != null)
                    {
                        ScheduleDetailBean.DataBean bean = detailBean.getData();
                        if (bean != null)
                        {
                            setInfo(detailBean, true);
                        }
                    }
                }
            }
        });
    }

    public void setInfo(ScheduleDetailBean bean, boolean isEdit)
    {
        if (bean.getData() == null)
        {
            return;
        }
        ScheduleDetailBean.DataBean.MeetBean comNotice = bean.getData().getMeet();
        if (comNotice == null)
        {
            return;
        }
        controllerLayout(false);
       /* //是自己发的才可以修改。
        if ((comNotice.getCreateId()+"").equals(DisplayUtil.getUserInfo(getActivity(),3))){
            controllerLayout(false);
        }else{
            controllerLayout(false);
        }
        //超过开始时间,不可以修改
        if (DateUtils.compareTwoDate(comNotice.getStartTime(),DisplayUtil.getTimeM())){
            controllerLayout(false);
        }else {
            controllerLayout(false);
        }*/
        if (comNotice.getTitle() != null)
        {
            edt_title.setText(comNotice.getTitle());
        }

        try
        {
            if (comNotice.getStartTime() != null)
            {
                String startStringTime = "";
                String endStringTime = "";

                if (StringUtils.notEmpty(comNotice.getStartTime()) && StringUtils.notEmpty(comNotice.getEndTime()))
                {
                    String tmp01 = "";
                    Date tmp01d1 = new SimpleDateFormat("yyyy-MM-dd").parse(comNotice.getStartTime());
                    SimpleDateFormat tmp01sdf0 = new SimpleDateFormat("yyyy-MM-dd");
                    tmp01 = tmp01sdf0.format(tmp01d1);

                    String tmp02 = "";
                    Date tmp02d1 = new SimpleDateFormat("yyyy-MM-dd").parse(comNotice.getEndTime());
                    SimpleDateFormat tmp02sdf0 = new SimpleDateFormat("yyyy-MM-dd");
                    tmp02 = tmp02sdf0.format(tmp02d1);

                    if (tmp01.equals(tmp02))
                    {
                        if (StringUtils.notEmpty(comNotice.getStartTime()))
                        {
                            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comNotice.getStartTime());
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            startStringTime = sdf2.format(d1);
                        }

                        if (StringUtils.notEmpty(comNotice.getEndTime()))
                        {
                            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comNotice.getEndTime());
                            SimpleDateFormat sdf3 = new SimpleDateFormat("HH:mm");
                            endStringTime = sdf3.format(d1);
                        }

                        mMeetStartTime.setText(startStringTime + " - " + endStringTime);
                        tv_and_view.setVisibility(View.GONE);
                        tv_end_time.setVisibility(View.GONE);
                    } else
                    {
                        if (StringUtils.notEmpty(comNotice.getStartTime()))
                        {
                            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comNotice.getStartTime());
                            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            startStringTime = sdf2.format(d1);
                        }

                        if (StringUtils.notEmpty(comNotice.getEndTime()))
                        {
                            Date d1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(comNotice.getEndTime());
                            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            endStringTime = sdf3.format(d1);
                        }
                        mMeetStartTime.setText(startStringTime);
                        tv_and_view.setVisibility(View.VISIBLE);
                        tv_end_time.setText(endStringTime);
                    }
                }

            }
        } catch (Exception e)
        {

        }

        if (comNotice.getEndTime() != null)
        {
            mEndTime = comNotice.getEndTime();
        }
        if (comNotice.getMeetPlace() != null)
        {
            mMeetAddress.setText(comNotice.getMeetPlace());
        }
        //抄送
        if (bean.getData().getCcList().size() > 0)
        {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < bean.getData().getCcList().size(); i++)
            {
                if (bean.getData().getCcList().get(i) != null
                        && StringUtils.notEmpty(bean.getData().getCcList().get(i).getUserId()))
                {
                    builder.append(bean.getData().getCcList().get(i).getUserName() + "、");
                    mEidLists.add(bean.getData().getCcList().get(i).getUserId() + "");
                    SDUserEntity sdUserEntity = null;
                    if (StringUtils.notEmpty(bean.getData().getCcList().get(i).getImAccount()))
                        sdUserEntity = mUserDao.findUserByImAccount(bean.getData().getCcList().get(i).getImAccount());
                    if (StringUtils.notEmpty(sdUserEntity))
                    {
                        mJoinUserLists.add(sdUserEntity);
                    } else
                    {
                        SDUserEntity tmpSDuserEntity = new SDUserEntity();
                        tmpSDuserEntity.setImAccount(bean.getData().getCcList().get(i).getImAccount());
                        tmpSDuserEntity.setName(bean.getData().getCcList().get(i).getUserName());
                        tmpSDuserEntity.setIcon("");
                        mJoinUserLists.add(tmpSDuserEntity);
                    }
                }
            }
        } else
        {

        }
        if (comNotice.getRemark() != null)
        {
            mMeetContent.setText(comNotice.getRemark());
        }
        //附件
        if (bean.getData().getAnnexList() != null)
        {
            if (bean.getData().getAnnexList().size() < 1)
            {
                llAnnex.setVisibility(View.GONE);
            } else
            {
                llAnnex.setVisibility(View.VISIBLE);
                setAddPhotoBtnDetail(false);
                setVoidBtnDetailImg(false);
            }
            checkFileOrImgOrVoice(bean.getData().getAnnexList(), add_pic_btn_detail_img, add_void_btn_detail_img,
                    add_file_btn_detail_img);
        } else
        {

            llAnnex.setVisibility(View.GONE);
        }

        tvMeetingNum.setText("共" + bean.getData().getCcList().size() + "人");

        if (comNotice.getType() >= 0)
        {
            String type = "";
            if (comNotice.getType() == 1)
            {
                type = "普通";
            } else if (comNotice.getType() == 2)
            {
                type = "周会";
            } else if (comNotice.getType() == 3)
            {
                type = "月会";
            }
            mType = comNotice.getType();
        }

        if (mJoinUserLists.size() > 0)
        {
            setAdapter();
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
//            if (v == mMeetParticipate)
//            {
//                Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
//                intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
//                intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
//                if (mIsAdd)
//                {
//                    intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "选择参与人");
//                    intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, true);
//                    intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
//                } else
//                {
//                    intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "参与人");
//                    intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
//                    intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) mEidLists);
//                    intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
//                }
//                intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mJoinUserLists);
//                startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_LEAVE);
//            }

            if (v == mMeetStartTime)
            {
                /*new SlideDateTimePicker.Builder(getActivity().getSupportFragmentManager())
                        .setListener(startTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();*/

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
                        mJoinUserLists.clear();
                        mJoinUserLists.addAll(userList);

                    } else
                    {
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
        String title = et_meet_content.getText().toString();
        String content = mMeetContent.getText().toString();
        String address = mMeetAddress.getText().toString();

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
        }
//        else if (TextUtils.isEmpty(join))
//        {
//            SDToast.showShort("请选择参与人");
//            return;
//        }
        else if (TextUtils.isEmpty(content))
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
//            mMeetEndTime.setText(DisplayUtil.mFormatterSubmit.format(date));
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
        showmLogisticDialogUtil(mTypeBean, getActivity(), "会议种类", mTypeList, "0", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mTypeBean = bean;
                mTypeBean.setSelectIndex(index);
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
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_more)
    public void onViewClicked()
    {
        if (mIsAdd)
        {
            Intent intent = new Intent(getActivity(), SDSelectContactActivity.class);
            intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
            intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
            intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "选择参与人");
            intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, true);
            intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, true);
            intent.putExtra(SDSelectContactActivity.INIT_SELECTED_CONTACT, (Serializable) mJoinUserLists);
            startActivityForResult(intent, REQUEST_CODE_FOR_BORROW_LEAVE);
        } else
        {
            Members members;
            TmpMemberList.clear();
            Iterator<SDUserEntity> iterator = mJoinUserLists.iterator();
            while (iterator.hasNext())
            {
                members = new Members();
                SDUserEntity sdUserEntity = iterator.next();
                members.setIcon(sdUserEntity.getIcon());
                members.setName(sdUserEntity.getName());
                members.setUserId(sdUserEntity.getImAccount());
                TmpMemberList.add(members);
            }
            Intent intent = new Intent(getActivity(), MumberContactActivity.class);
            intent.putExtra(MumberContactActivity.CHECK_TYPE, -1);
            intent.putExtra(ContactActivity.WHAT_TO_DO, ContactActivity.ADD_CHAT);
            Bundle bundle = new Bundle();
            bundle.putSerializable("members", (Serializable) TmpMemberList);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private UserAdapter adapter;

    private void setAdapter()
    {
        adapter = new UserAdapter(getActivity(), mJoinUserLists);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Intent intent = new Intent(getActivity(), SDPersonalAlterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(SPUtils.USER_ID, String.valueOf(mJoinUserLists.get(position).getImAccount()));
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            }
        });
    }

    private void reFresh()
    {
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
