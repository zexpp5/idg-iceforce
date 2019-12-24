package newProject.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.DialogUtilsIm;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.company.expense.ExpenseApprovalListActivity;
import newProject.company.hr.HrActivity;
import newProject.company.myapproval.HasBean;
import newProject.company.myapproval.HaveApprovalBean;
import newProject.company.myapproval.MyApprovalNewActivity;
import newProject.company.project_manager.investment_project.EmployeeGuideActivity;
import newProject.company.project_manager.investment_project.ProjectActivity;
import newProject.company.project_manager.investment_project.ProjectLibraryActivity;
import newProject.company.travel.TravelActivity;
import newProject.ui.annual_meeting.AnnualMeetingDetailBean;
import newProject.ui.annual_meeting.AnnualMeetingMainActivity;
import newProject.ui.work.GeneralMeetingActivity;
import newProject.xzs.XZSActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;

public class WorkFragment extends BaseFragment
{
    private LinearLayout mContainerLayout;
    private List<RelativeLayout> mLayoutList = new ArrayList<>();
    private List<FontTextView> mMsgReadList = new ArrayList<>();
    private int mSuper = 4;
    private View mView;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work;
    }

    @Override
    protected void setBeforeOnCreate()
    {
        super.setBeforeOnCreate();
    }

    @Override
    protected void init(View view)
    {
        EventBus.getDefault().register(this);
        mContainerLayout = (LinearLayout) view.findViewById(R.id.container_layout);
        this.mView = view;
        hasApproval();
    }

    private void initItem(View view)
    {
        mLayoutList.clear();
        mMsgReadList.clear();
        mContainerLayout.removeAllViews();

        String IsTemp = (String) DisplayUtil.getUserInfo(getActivity(), 9);
        if (IsTemp.equals("1"))
        {
            mSuper = 1;
            for (int i = 0; i < mSuper; i++)
            {
                WorkItemView itemView = null;
                if (i == 0)
                {
                    itemView = new WorkItemView(getActivity(), 0);
                    itemView.setItemTextAndImage(1, "年会", R.mipmap.icon_nianhui);
//                    itemView.setItemText(2, "资本快报", R.mipmap.capital_icon);
//                itemView.setItemText(3, "新闻资讯", R.mipmap.news_infor_icon);
//                    itemView.setItemText(4, "会议", R.mipmap.schedule_meet_icon);
                    itemView.setHideView(false);
                    itemView.setTitleVisible(true);

                    itemView.setLayoutVisible(2, View.GONE);
                    itemView.setLayoutVisible(3, View.GONE);
                }

                for (int j = 1; j < 2; j++)
                {
                    mLayoutList.add(itemView.getLayout(j));
                    mMsgReadList.add(itemView.getMSGText(j));
                }
                mContainerLayout.addView(itemView);
            }
        } else
        {
            mSuper = 4;
            for (int i = 1; i < mSuper; i++)
            {
                WorkItemView itemView = null;
                if (i == 1)
                {
                    itemView = new WorkItemView(getActivity(), 0);
                    itemView.setItemTextAndImage(1, "ICEFORCE", R.mipmap.project_mannager);
                    itemView.setItemTextAndImage(2, "月会安排", R.mipmap.icon_yuehui);
                    itemView.setItemTextAndImage(3, "差旅预定", R.mipmap.icon_chailv);
                    itemView.setHideView(false);
                    itemView.setTitleVisible(true);
                } else if (i == 2)
                {
                    itemView = new WorkItemView(getActivity(), 1);
                    itemView.setItemTextAndImage(1, "我的报销", R.mipmap.icon_baoxiao);
                    itemView.setItemTextAndImage(2, "移动HR", R.mipmap.icon_hr);
                    itemView.setItemTextAndImage(3, "发票信息", R.mipmap.icon_fapiao);
                    itemView.setTitleVisible(true);
                    itemView.setHideView(false);
//                itemView.setAllLayoutPadding(20, 20);
                } else if (i == 3)
                {
                    itemView = new WorkItemView(getActivity(), 1);
                    itemView.setTitleText("");
                    itemView.setTitleVisible(true);
                    itemView.setItemTextAndImage(1, "员工指南", R.mipmap.icon_zhinan);
                    itemView.setItemTextAndImage(2, "我的审批", R.mipmap.icon_shenpi);
                    if (travelApprove || holidatApprove || costApprove || finshleaveApprove)
                    {
                        itemView.setLayoutVisible(2, View.VISIBLE);
                    } else
                    {
                        itemView.setLayoutVisible(2, View.GONE);
                    }
                    itemView.setItemTextAndImage(3, "年会", R.mipmap.icon_nianhui);

                    String isOpen = (String) SPUtils.get(getActivity(), SPUtils.IS_OPEN_ANNUAL_MEETING, "");
                    if (isOpen.equals("1"))
                    {
                        itemView.setLayoutVisible(3, View.VISIBLE);
                    } else
                    {
                        itemView.setLayoutVisible(3, View.GONE);
                    }

                    itemView.setHideView(false);
                    itemView.setTitleVisible(true);
                }
                for (int j = 1; j < mSuper; j++)
                {
                    mLayoutList.add(itemView.getLayout(j));
                    mMsgReadList.add(itemView.getMSGText(j));
                }
                mContainerLayout.addView(itemView);
            }
        }

        for (int i = 0; i < mLayoutList.size(); i++)
        {
            mLayoutList.get(i).setOnClickListener(mOnClickListener);
        }

        setUnReadCount();
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mLayoutList != null && mLayoutList.size() > 0)
            {
                String isOpen = (String) SPUtils.get(getActivity(), SPUtils.IS_OPEN_ANNUAL_MEETING, "");
                for (int k = 0; k < mLayoutList.size(); k++)
                {
                    if (v == mLayoutList.get(k))
                    {
                        String IsTemp = (String) DisplayUtil.getUserInfo(getActivity(), 9);
                        if (IsTemp.equals("1"))
                        {
                            jump(8);
                        } else
                        {
                            jump(k);
                        }

                        break;
                    }
                }
            }
        }
    };

    /**
     * @param which 第几个 0-15
     * @param count 数字
     */
    public void setMsgTextVisible(int which, String count, boolean isShow)
    {
        if (mMsgReadList != null && mMsgReadList.size() > 0 && mMsgReadList.size() > which)
        {
            if (StringUtils.notEmpty(count))
            {
                if (Integer.parseInt(count) > 0)
                {
                    if (Integer.parseInt(count) > 99)
                    {
                        mMsgReadList.get(which).setText("99+");
                    } else
                    {
                        mMsgReadList.get(which).setText(count + "");
                    }
                    if (isShow)
                        mMsgReadList.get(which).setVisibility(View.VISIBLE);
                    else
                        mMsgReadList.get(which).setVisibility(View.GONE);
                } else
                {
                    mMsgReadList.get(which).setVisibility(View.GONE);
                }
            } else
            {
                mMsgReadList.get(which).setVisibility(View.GONE);
            }
        }
    }

    private void reFresh()
    {
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ((SuperMainActivity) getActivity()).setFourUnRead(1);
                } catch (Exception e)
                {
                    SDLogUtil.error("refreshUI出错！");
                }
            }
        }, 50);
    }

    private void jump(int k)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (k)
        {
            case 0://ProjectActivity
                if (SPUtils.get(getActivity(), SPUtils.ROLE_FLAG, "0").equals("0")){
                    SDToast.showShort("您暂无权限查看该页面，请联系管理员!");
                }else {
                    String flag = (String)SPUtils.get(getActivity(), SPUtils.ROLE_FLAG, "0");
                    if (flag.equals("205") || flag.equals("206") || flag.equals("207") || flag.equals("208") || flag.equals("10") || flag.equals("12")){
                        startActivity(new Intent(getActivity(), ProjectActivity.class));
                        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_VM);
                        setMsgTextVisible(0, "", false);
                    }else if(flag.equals("209") || flag.equals("210") || flag.equals("211") || flag.equals("212") || flag.equals("216") || flag.equals("217") || flag.equals("220") || flag.equals("11") || flag.equals("13") || flag.equals("14")){
                        startActivity(new Intent(getActivity(), ProjectLibraryActivity.class));
                        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_VM);
                        setMsgTextVisible(0, "", false);
                    }else {
                        SDToast.showShort("未知权限!");
                    }
                }
                break;
            case 1:
                //会议
                intent.setClass(getActivity(), GeneralMeetingActivity.class);
                getActivity().startActivity(intent);
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_DM);
                setMsgTextVisible(1, "", false);
                break;
            case 2:
                //我的差旅
                intent.setClass(getActivity(), TravelActivity.class);
                getActivity().startActivity(intent);
                setMsgTextVisible(2, "", false);
                break;
            case 3:
                //我的报销
//                startActivity(new Intent(getActivity(), ExpenseActivity.class));
                startActivity(new Intent(getActivity(), ExpenseApprovalListActivity.class));
                setMsgTextVisible(3, "", false);
                break;
            case 4:
                //移动HR
                intent.setClass(getActivity(), HrActivity.class);
                getActivity().startActivity(intent);
                setMsgTextVisible(4, "", false);
                break;
            case 5://发票
                intent.setClass(getActivity(), XZSActivity.class);
                getActivity().startActivity(intent);
                break;
            case 6:
                //员工指南
                intent.setClass(getActivity(), EmployeeGuideActivity.class);
                getActivity().startActivity(intent);
                setMsgTextVisible(6, "", false);
                break;
            case 7:
                //我的审批
                intent.setClass(getActivity(), MyApprovalNewActivity.class);
                bundle.putBoolean("holidatApprove", holidatApprove);
                bundle.putBoolean("costApprove", costApprove);
                bundle.putBoolean("travelApprove", travelApprove);
                bundle.putBoolean("finshleaveApprove", finshleaveApprove);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
//                setMsgTextVisible(7, "", false);
                break;
            //年会
            case 8:

                String isOpen = (String) SPUtils.get(getActivity(), SPUtils.IS_OPEN_ANNUAL_MEETING, "");
                if (isOpen.equals("1"))
                {
                    postAnnualMeetingInfo();
                }
                break;
        }
    }

    private boolean holidatApprove = false;
    private boolean costApprove = false;
    private boolean travelApprove = false;
    private boolean finshleaveApprove = false;
    private boolean allHolidayApprove = false;

    private void hasApproval()
    {

        ListHttpHelper.getHasApproval(getActivity(), new SDRequestCallBack(HasBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
                hasApproval2();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HasBean numbean = (HasBean) responseInfo.getResult();
                if (numbean.isData())
                {
                    finshleaveApprove = true;
                } else
                {
                    finshleaveApprove = false;
                }
                hasApproval2();
            }
        });
    }

    private void hasApproval2()
    {
        ListHttpHelper.getHasSomeApproval(getActivity(), new SDRequestCallBack(HaveApprovalBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(getActivity(), msg);
                initItem(mView);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HaveApprovalBean numbean = (HaveApprovalBean) responseInfo.getResult();
                if (numbean != null)
                {
                    if (StringUtils.notEmpty(numbean.getData().isTravelApprove()))
                    {
                        if (numbean.getData().isTravelApprove())
                        {
                            travelApprove = true;
                        } else
                        {
                            travelApprove = false;
                        }
                    }
                    if (StringUtils.notEmpty(numbean.getData().isHolidatApprove()))
                    {
                        if (numbean.getData().isHolidatApprove())
                        {
                            holidatApprove = true;
                        } else
                        {
                            holidatApprove = false;
                        }
                    }
                    if (StringUtils.notEmpty(numbean.getData().isCostApprove()))
                    {
                        if (numbean.getData().isCostApprove())
                        {
                            costApprove = true;
                        } else
                        {
                            costApprove = false;
                        }
                    }
                    initItem(mView);
                }
            }
        });
    }

    public class WorkItemView extends LinearLayout
    {
        private LayoutInflater mInflater;
        private View mView, mBottomLine;
        private TextView mTitle;
        private RelativeLayout mOneLayout, mTwoLayout, mThreeLayout;
        private ImageView mOneImage, mTwoImage, mThreeImage;
        private TextView mOneText, mTwoText, mThreeText;
        private FontTextView mOneMSGText, mTwoMSGText, mThreeMSGText;
        private LinearLayout mAllItemLayout;
        private int type = 0;

        public WorkItemView(Context context, int type)
        {
            super(context);
            this.type = type;
            initView(context);
        }

        private void initView(Context context)
        {
            mInflater = LayoutInflater.from(context);
            if (type == 0)
            {
                mView = mInflater.inflate(R.layout.work_item_layout2, null, false);
            } else
            {
                mView = mInflater.inflate(R.layout.work_item_layout, null, false);
            }

            addView(mView);

            mTitle = (TextView) mView.findViewById(R.id.item_title);
            mAllItemLayout = (LinearLayout) mView.findViewById(R.id.work_all_layout);
            mOneLayout = (RelativeLayout) mView.findViewById(R.id.item_one);
            mTwoLayout = (RelativeLayout) mView.findViewById(R.id.item_two);
            mThreeLayout = (RelativeLayout) mView.findViewById(R.id.item_three);

            mOneImage = (ImageView) mView.findViewById(R.id.item_one_image);
            mTwoImage = (ImageView) mView.findViewById(R.id.item_two_image);
            mThreeImage = (ImageView) mView.findViewById(R.id.item_three_image);

            mOneText = (TextView) mView.findViewById(R.id.item_one_text);
            mTwoText = (TextView) mView.findViewById(R.id.item_two_text);
            mThreeText = (TextView) mView.findViewById(R.id.item_three_text);

            mOneMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_one);
            mTwoMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_two);
            mThreeMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_three);

            mBottomLine = mView.findViewById(R.id.bottom_line);

        }

        /**
         * 设置 item 可见隐藏
         *
         * @param which
         * @param visibility
         */
        public void setLayoutVisible(int which, int visibility)
        {
            if (which == 1)
            {
                mOneLayout.setVisibility(visibility);
            } else if (which == 2)
            {
                mTwoLayout.setVisibility(visibility);
            } else if (which == 3)
            {
                mThreeLayout.setVisibility(visibility);
            }
        }

        public void setTitleText(String title)
        {
            mTitle.setText(title);
        }

        /**
         * 设置 item 文字图标
         *
         * @param which
         * @param text
         */
        public void setItemTextAndImage(int which, String text, int resource)
        {
            if (which == 1)
            {
                mOneText.setText(text);
                mOneImage.setImageResource(resource);
            } else if (which == 2)
            {
                mTwoText.setText(text);
                mTwoImage.setImageResource(resource);
            } else if (which == 3)
            {
                mThreeText.setText(text);
                mThreeImage.setImageResource(resource);
            }
        }

        /**
         * 设置 item 颜色
         *
         * @param which
         * @param color
         */
        public void setItemTextColor(int which, int color)
        {
            if (which == 1)
            {
                mOneText.setTextColor(color);
            } else if (which == 2)
            {
                mTwoText.setTextColor(color);
            } else if (which == 3)
            {
                mThreeText.setTextColor(color);
            }
        }

        /**
         * 设置 item 图片
         *
         * @param which
         * @param resource
         */
        public void setItemImage(int which, int resource)
        {
            if (which == 1)
            {
                mOneImage.setBackgroundResource(resource);
            } else if (which == 2)
            {
                mTwoImage.setBackgroundResource(resource);
            } else if (which == 3)
            {
                mThreeImage.setBackgroundResource(resource);
            }
        }

        public RelativeLayout getLayout(int which)
        {
            if (which == 1)
            {
                return mOneLayout;
            } else if (which == 2)
            {
                return mTwoLayout;
            } else if (which == 3)
            {
                return mThreeLayout;
            } else
            {
                return null;
            }
        }

        public FontTextView getMSGText(int which)
        {
            if (which == 1)
            {
                return mOneMSGText;
            } else if (which == 2)
            {
                return mTwoMSGText;
            } else if (which == 3)
            {
                return mThreeMSGText;
            } else
            {
                return null;
            }
        }

        public void setHideView(boolean isHide)
        {
            if (isHide)
            {
                mBottomLine.setVisibility(INVISIBLE);
            } else
            {
                mBottomLine.setVisibility(VISIBLE);
            }
        }

        public void setTitleVisible(boolean visible)
        {
            if (visible)
            {
                mTitle.setVisibility(GONE);
            } else
            {
                mTitle.setVisibility(VISIBLE);
            }
        }

        private void setAllLayoutPadding(int top, int bootom)
        {
            mAllItemLayout.setPadding(0, DisplayUtil.getRealPixel2(top), 0, DisplayUtil.getRealPixel2(bootom));
        }
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
                case Constants.IM_PUSH_CLSP: //请假
                    setMsgTextVisible(2, getTripUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_QJ: //请假
                case Constants.IM_PUSH_XIAO: //我的销假
                case Constants.IM_PUSH_PUSH_HOLIDAY:  //消息
                case Constants.IM_PUSH_PROGRESS://流程消息
                    setMsgTextVisible(4, getAllItemUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_CK://请假审批
                case Constants.IM_PUSH_XIAO2: //销假审批
                case Constants.IM_PUSH_CLSP2: //差旅审批
                case Constants.IM_PUSH_BSPS: //报销审批
                    setMsgTextVisible(7, getAllXJUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_VM: //语音会议
//                    setMsgTextVisible(3, info.count + "", true);
                    break;
                case Constants.IM_PUSH_DM: //日常会议
                    setMsgTextVisible(1, info.count + "", true);
                    break;
            }
        } else
        {
            //隐藏小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_CLSP: //请假
                    setMsgTextVisible(2, getTripUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_QJ: //请假
                case Constants.IM_PUSH_XIAO: //我的销假
                case Constants.IM_PUSH_PUSH_HOLIDAY:  //消息
                case Constants.IM_PUSH_PROGRESS://流程消息
                    setMsgTextVisible(4, getAllItemUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_CK://请假审批
                case Constants.IM_PUSH_XIAO2: //销假审批
                case Constants.IM_PUSH_CLSP2: //差旅审批
                case Constants.IM_PUSH_BSPS: //差旅审批
                    setMsgTextVisible(7, getAllXJUnRead() + "", true);
                    break;

                case Constants.IM_PUSH_VM: //语音会议
//                    setMsgTextVisible(3, i "-1", true);
                    break;
                case Constants.IM_PUSH_DM: //日常会议
                    setMsgTextVisible(1, "-1", false);
                    break;
            }
        }
    }

    //我的出差
    private int getTripUnRead()
    {
        int unRead = 0;
        int c = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP);
        unRead = c;
        return unRead;
    }

    //移动HR
    private int getAllItemUnRead()
    {
        int unRead = 0;
        int a = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ);
        int b = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO);
        int c = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY);
        int d = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS);
        unRead = a + b + c + d;
        return unRead;
    }

    //我的审批
    private int getAllXJUnRead()
    {
        int unRead = 0;
        int c1 = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK);
        int c2 = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP2);
        int c3 = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO2);
        int c4 = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_BSPS);
        unRead = c1 + c2 + c3 + c4;
        return unRead;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        setUnReadCount();
    }

    private void setUnReadCount()
    {
        //休假
        findUnReadLeave();

        findUnReadLeaveCKCount();

        findUnReadXJ();

        findUnReadXJPS();

        findUnReadBSPS();

        findUnReadCC();

        findUnReadCCPS();

        //消息
        findUnReadNews();

        findUnReadDayMeeting();

        findUnReadVoiceMeeting();

        findUnReadProgress();

        reFresh();
    }

    private void findUnReadLeaveCKCount()
    {
        UnReadUtils.getInstance().findUnReadLeaveCKCount();
    }

    //请假
    private void findUnReadLeave()
    {
        UnReadUtils.getInstance().findUnReadLeave();
    }

    //消息
    private void findUnReadNews()
    {
        UnReadUtils.getInstance().findUnReadNews();
    }

    //消息
    private void findUnReadProgress()
    {
        UnReadUtils.getInstance().findUnReadProgress();
    }

    //销假
    private void findUnReadXJ()
    {
        UnReadUtils.getInstance().findUnReadXJ();
    }

    //销假
    private void findUnReadXJPS()
    {
        UnReadUtils.getInstance().findUnReadXJPS();
    }

    //报销审批
    private void findUnReadBSPS()
    {
        UnReadUtils.getInstance().findUnReadBSPS();
    }


    //出差
    private void findUnReadCC()
    {
        UnReadUtils.getInstance().findUnReadCC();
    }

    //出差
    private void findUnReadCCPS()
    {
        UnReadUtils.getInstance().findUnReadCCPS();
    }

    //日常会议
    private void findUnReadDayMeeting()
    {
        UnReadUtils.getInstance().findUnReadDayMeeting();
    }

    //语音会议
    private void findUnReadVoiceMeeting()
    {
        UnReadUtils.getInstance().findUnReadVoiceMeeting();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    //年会信息
    private void postAnnualMeetingInfo()
    {
        BasicDataHttpHelper.postNianInfo(getActivity(), new SDRequestCallBack(AnnualMeetingDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                DialogUtilsIm.dialogPayFinish(getActivity(), "提 示", msg, "确定", "", new DialogUtilsIm
                        .OnYesOrNoListener2()

                {
                    @Override
                    public void onYes()
                    {

                    }
                });
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                AnnualMeetingDetailBean annualMeetingDetailBean = (AnnualMeetingDetailBean) responseInfo.getResult();
                if (annualMeetingDetailBean.getStatus() == 200)
                {
                    startActivity(new Intent(getActivity(), AnnualMeetingMainActivity.class));
                } else if (annualMeetingDetailBean.getStatus() == 400)
                {
                    DialogUtilsIm.dialogPayFinish(getActivity(), "提 示", annualMeetingDetailBean.getMsg(), "确定", "", new
                            DialogUtilsIm.OnYesOrNoListener2()
                            {
                                @Override
                                public void onYes()
                                {

                                }
                            });
                }
            }
        });
    }

}
