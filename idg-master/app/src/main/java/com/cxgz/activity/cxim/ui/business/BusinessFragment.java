package com.cxgz.activity.cxim.ui.business;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.entity.IMDaoManager;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.ui.voice.list.VoiceMeetingListActivity;
import com.cxgz.activity.cxim.utils.MainTopMenuUtils;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.cxim.workCircle.activity.WorkCircleMainActivity;
import com.chaoxiang.base.config.Constants;
import com.injoy.idg.R;
import com.superdata.im.constants.PlushType;
import com.superdata.im.utils.Observable.CxWorkCircleObservale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.ApplyListActivity;
import newProject.company.affairs.AffairListActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;


/**
 * User: Selson
 * Date: 2016-10-31
 * FIXME 事务Fragment
 */
public class BusinessFragment extends BaseFragment
{
    //item布局
    @Bind(R.id.ll_im_business_work_group)
    RelativeLayout llImBusinessWorkGroup;
    @Bind(R.id.ll_im_business_project_cooperate)
    RelativeLayout llImBusinessWorkSubmit;
    @Bind(R.id.ll_im_business_work_out)
    RelativeLayout llImBusinessBorrowMoneySubmit;
    @Bind(R.id.ll_im_business_voice_meeting)
    RelativeLayout llImBusinessAchievementSubmit;
    @Bind(R.id.ll_im_business_borrow)
    RelativeLayout llImBusinessLeaveSubmit;
    @Bind(R.id.ll_im_business_leave)
    RelativeLayout llImBusinessPrivateChat;
    @Bind(R.id.ll_im_business_work_journal)
    RelativeLayout llImBusinessScan;

    //公司圈
    @Bind(R.id.unread_msg_number_work_group)
    FontTextView unreadMsgNumberWorkGroup;


    @Bind(R.id.unread_msg_number_work_report)
    FontTextView unreadMsgNumberWorkReport;

    @Bind(R.id.unread_msg_number_project_cooperate)
    FontTextView unreadMsgNumberProjectCooperate;

    @Bind(R.id.unread_msg_number_voice_meeting)
    FontTextView unreadMsgNumberVoiceMeeting;

    @Bind(R.id.unread_msg_number_work_out)
    FontTextView unreadMsgNumberWorkOut;

    @Bind(R.id.unread_msg_number_borrow)
    FontTextView unreadMsgNumberBorrow;

    @Bind(R.id.unread_msg_number_leave)
    FontTextView unreadMsgNumberLeave;

    @Bind(R.id.unread_msg_number_work_journal)
    FontTextView unreadMsgNumberWorkJournal;
    //未读信息


    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_im_business_main;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setAddWorkCircle();

        findUnReadCompanyCount();

        findUnReadLeave();

        findUnReadDayMeeting();

        findUnReadVoiceMeeting();
    }

    //公司公告
    private void findUnReadCompanyCount()
    {
        UnReadUtils.getInstance().findUnReadCompanyCount();
    }

    //请假
    private void findUnReadLeave()
    {
        UnReadUtils.getInstance().findUnReadLeave();
    }

    //日常会议
    private void findUnReadDayMeeting()
    {
        UnReadUtils.getInstance().findUnReadDayMeeting();
    }

    private void findUnReadVoiceMeeting()
    {
        UnReadUtils.getInstance().findUnReadVoiceMeeting();
    }

    /**
     * 推送接收
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnReadEvent(UnReadMessage info)
    {
        if (info.isShow)
        {
            //显示小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_QJ: //请假
                    showUnreadCompanyNotice(unreadMsgNumberLeave, true, info.count);
                    break;
                case Constants.IM_PUSH_VM: //语音会议
                    showUnreadCompanyNotice(unreadMsgNumberVoiceMeeting, true, info.count);
                    break;

                case Constants.IM_PUSH_GT: //公司通知

                    break;

                case Constants.IM_PUSH_DM: //日常会议

                    break;


            }

        } else
        {
            //隐藏小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_QJ: //请假
                    showUnreadCompanyNotice(unreadMsgNumberLeave, false, info.count);
                    break;
                case Constants.IM_PUSH_VM: //语音会议
                    showUnreadCompanyNotice(unreadMsgNumberVoiceMeeting, false, info.count);
                    break;

                case Constants.IM_PUSH_GT: //公司通知

                    break;

                case Constants.IM_PUSH_DM: //日常会议

                    break;
            }
        }
    }

    //推送
    private void showUnreadCompanyNotice(TextView view, boolean isShow, int unReadCount)
    {
        if (view != null)
            if (isShow)
            {
                view.setVisibility(View.VISIBLE);
                if (unReadCount < 99)
                    view.setText(unReadCount + "");
                else
                    view.setText("99");
            } else
            {
                view.setVisibility(View.GONE);
            }
    }

    @Override
    protected void init(View view)
    {
        setTitle(R.string.im_business);

//        addLeftBtn(R.drawable.folder_back, new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                getActivity().finish();
//            }
//        });

        addRightBtn(R.mipmap.menu_add, new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MainTopMenuUtils.getInstance(getActivity()).showMenu(view, "1");
            }
        });
    }

    @Override
    protected void updateWorkCircle()
    {
        super.updateWorkCircle();
        setUnRead(1, true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.ll_im_business_work_group, R.id.ll_im_business_project_cooperate,
            R.id.ll_im_business_work_out, R.id.ll_im_business_voice_meeting,
            R.id.ll_im_business_borrow, R.id.ll_im_business_leave,
            R.id.ll_im_business_work_journal, R.id.ll_im_business_work_report})
    public void onClick(View view)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (view.getId())
        {
            //公司圈
            case R.id.ll_im_business_work_group:

                IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().upDateWorkCircleRead(PlushType
                        .PLUSH_NEW_WORK_CIRCLE);
                getActivity().startActivity(intent.setClass(getActivity(), WorkCircleMainActivity.class));
                setUnRead(1, false);
                break;
            //事务报告
            case R.id.ll_im_business_work_report:
                Intent intent1 = new Intent(getActivity(), AffairListActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("CHOOSE", 0);
                intent1.putExtras(bundle1);
                startActivity(intent1);


                break;
            //项目协同
            case R.id.ll_im_business_project_cooperate:
                Intent intent2 = new Intent(getActivity(), AffairListActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt("CHOOSE", 1);
                intent2.putExtras(bundle2);
                startActivity(intent2);

                break;

            //语音会议
            case R.id.ll_im_business_voice_meeting:
                getActivity().startActivity(intent.setClass(getActivity(), VoiceMeetingListActivity.class));

                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_VM);
                break;

            //工作外出
            case R.id.ll_im_business_work_out:
                intent.setClass(getActivity(), ApplyListActivity.class);
                bundle.putInt("CHOOSE", 2);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);


                break;
            //借支申请
            case R.id.ll_im_business_borrow:
                intent.setClass(getActivity(), ApplyListActivity.class);
                bundle.putInt("CHOOSE", 0);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;
            //请假申请
            case R.id.ll_im_business_leave:
                intent.setClass(getActivity(), ApplyListActivity.class);
                bundle.putInt("CHOOSE", 1);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_QJ);
                break;
            //工作日志
            case R.id.ll_im_business_work_journal:
                intent.setClass(getActivity(), ApplyListActivity.class);
                bundle.putInt("CHOOSE", 3);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);

                break;

        }
    }

    private void setUnRead(int index, boolean isShow)
    {
        switch (index)
        {
            case 1:
                if (isShow)
                    unreadMsgNumberWorkGroup.setVisibility(View.VISIBLE);
                else
                    unreadMsgNumberWorkGroup.setVisibility(View.GONE);
                break;
        }
    }

    private void setAddWorkCircle()
    {
        boolean status = IMDaoManager.getInstance().getDaoSession().getIMWorkCircleDao().findWorkCircleStatusList();
        if (status)
        {
            CxWorkCircleObservale.getInstance().sendWorkUnRead(1);
        }
    }

}