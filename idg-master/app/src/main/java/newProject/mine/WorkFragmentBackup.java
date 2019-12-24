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
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SPUtils;
import com.utils.DialogUtilsIm;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.ApplyListActivity;
import newProject.api.ListHttpHelper;
import newProject.bean.HBean;
import newProject.company.magazine.MaganizeListActivity;
import newProject.company.myapproval.MyApprovalActivity;
import newProject.company.project_manager.investment_project.ProjectActivity;
import newProject.company.tools.ToolsListActivity;
import newProject.company.vacation.VacationActivity;
import newProject.company.vacation.WebVacationActivity;
import newProject.company.collect.AllListActivity;
import newProject.company.superpower.SuperPowerActivity;
import newProject.company.superuser.SuperUserListActivity;
import newProject.ui.annual_meeting.AnnualMeetingDetailBean;
import newProject.ui.annual_meeting.AnnualMeetingMainActivity;
import newProject.ui.work.GeneralMeetingActivity;
import newProject.xzs.XZSActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;

public class WorkFragmentBackup extends BaseFragment
{
    //    private CustomNavigatorBar mTitleBar;
    private LinearLayout mContainerLayout;
    private List<RelativeLayout> mLayoutList = new ArrayList<>();
    private List<FontTextView> mMsgReadList = new ArrayList<>();
    private int mSuper = 5;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work;
    }
    
    @Override
    protected void setBeforeOnCreate()
    {
        super.setBeforeOnCreate();
//        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    @Override
    protected void init(View view)
    {
        EventBus.getDefault().register(this);

//        mTitleBar = (CustomNavigatorBar) view.findViewById(R.id.title_bar);
//        mTitleBar.setMidText("工作");
//        mTitleBar.setLeftImageVisible(false);
//        mTitleBar.setRightTextVisible(false);
//        mTitleBar.setRightImageVisible(true);
//        mTitleBar.setRightSecondImageVisible(false);
        // mTitleBar.setRightSecondImageResouce(R.mipmap.public_d);

//        mTitleBar.setRightImageOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                MainTopMenuUtils.getInstance(getActivity()).showMenu(mTitleBar.getRightImage(), "1");
//            }
//        });

//        mTitleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                //群发短信
//                MainTopMenuUtils.getInstance(getActivity()).showMsnUser();
//            }
//        });
        mContainerLayout = (LinearLayout) view.findViewById(R.id.container_layout);
        initItem(view);
    }

    private void initItem(View view)
    {
        mLayoutList.clear();
        mMsgReadList.clear();
        mContainerLayout.removeAllViews();
        int isSuper = (int) SPUtils.get(getActivity(), SPUtils.IS_SUPER, 0);
        int SuperStatus = (int) SPUtils.get(getActivity(), SPUtils.IS_SUPER_STATUS, 0);
        if (isSuper == 1 && SuperStatus == 1)
        {
            mSuper = 5;
        } else
        {
            mSuper = 4;
        }

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
                    itemView.setTitleText("常用应用");
                    itemView.setItemTextAndImage(1, "年会", R.mipmap.icon_nianhui);
//                    itemView.setItemText(2, "资本快报", R.mipmap.capital_icon);
//                itemView.setItemText(3, "新闻资讯", R.mipmap.news_infor_icon);
//                    itemView.setItemText(4, "会议", R.mipmap.schedule_meet_icon);
                    itemView.setHideView(false);
                    itemView.setTitleVisible(false);

                    itemView.setLayoutVisible(2, View.GONE);
                    itemView.setLayoutVisible(3, View.GONE);
                    itemView.setLayoutVisible(4, View.GONE);
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
            for (int i = 1; i < mSuper; i++)
            {
                WorkItemView itemView = null;
                if (i == 1)
                {
                    itemView = new WorkItemView(getActivity(), 0);
                    itemView.setTitleText("资讯 · 公告");
                    itemView.setItemTextAndImage(1, "通知公告", R.mipmap.company_notify_icon);
                    itemView.setItemTextAndImage(2, "资本快报", R.mipmap.capital_icon);
//                itemView.setItemText(3, "新闻资讯", R.mipmap.news_infor_icon);
                    itemView.setItemTextAndImage(4, "会议", R.mipmap.schedule_meet_icon);
                    itemView.setHideView(false);
                    itemView.setTitleVisible(false);
                    itemView.setLayoutVisible(3, View.GONE);

                } else if (i == 2)
                {
                    itemView = new WorkItemView(getActivity(), 1);
                    itemView.setTitleText("常用应用");
                    itemView.setItemTextAndImage(1, "项目管理", R.mipmap.project_mannager);
                    itemView.setItemTextAndImage(2, "人事", R.mipmap.hr_icon);
                    itemView.setItemTextAndImage(3, "差旅", R.mipmap.travel_plan_icon);
                    itemView.setItemTextAndImage(4, "企业小助手", R.mipmap.enterprise_assistant_icon);
                    itemView.setTitleVisible(false);
                    itemView.setHideView(false);
//                itemView.setAllLayoutPadding(20, 20);
                } else if (i == 3)
                {
                    itemView = new WorkItemView(getActivity(), 1);
                    itemView.setTitleText("");
                    itemView.setTitleVisible(true);
                    itemView.setItemTextAndImage(1, "我的流程", R.mipmap.my_approval);
//                    itemView.setItemTextAndImage(2, "邮箱提醒", R.mipmap.email_icon);
                    itemView.setItemTextAndImage(2, "内刊", R.mipmap.magazine_icon);
                    itemView.setItemTextAndImage(3, "工具", R.mipmap.tools_icon);
                    itemView.setItemTextAndImage(4, "年会", R.mipmap.icon_nianhui);
                    String isOpen = (String) SPUtils.get(getActivity(), SPUtils.IS_OPEN_ANNUAL_MEETING, "");
                    if (isOpen.equals("1"))
                    {
                        itemView.setLayoutVisible(4, View.VISIBLE);
                    } else
                    {
                        itemView.setLayoutVisible(4, View.GONE);
                    }

                    itemView.setHideView(false);
//                itemView.setAllLayoutPadding(0, 30);
                } else if (i == 4)
                {
                    itemView = new WorkItemView(getActivity(), 1);
//                    String isOpen = (String) SPUtils.get(getActivity(), SPUtils.IS_OPEN_ANNUAL_MEETING, "");
//                    if (isOpen.equals("1"))
//                    {
//                        itemView.setTitleText("");
//                        itemView.setTitleVisible(true);
//                        itemView.setLayoutVisible(1, View.VISIBLE);
//                        itemView.setLayoutVisible(2, View.GONE);
//                        itemView.setLayoutVisible(3, View.GONE);
//                        itemView.setLayoutVisible(4, View.GONE);
//                        itemView.setItemTextAndImage(1, "年会", R.mipmap.icon_nianhui);
//                        itemView.setHideView(false);
//                    } else if (isOpen.equals("0"))
//                    {
                    itemView.setLayoutVisible(3, View.GONE);
                    itemView.setTitleText("管理员应用");
                    itemView.setItemTextAndImage(1, "超级用户", R.mipmap.super_user_icon);
                    itemView.setItemTextAndImage(2, "超级权限", R.mipmap.super_power_icon);
                    // itemView.setItemText(3, "超级搜索", R.mipmap.super_search_icon);
                    itemView.setItemTextAndImage(4, "发票信息", R.mipmap.invoice_icon);
                    itemView.setHideView(true);
                    itemView.setTitleVisible(false);
//                    }
                } else if (i == 5)
                {
                    itemView = new WorkItemView(getActivity(), 1);
                    itemView.setLayoutVisible(3, View.GONE);
                    itemView.setTitleText("管理员应用");
                    itemView.setItemTextAndImage(1, "超级用户", R.mipmap.super_user_icon);
                    itemView.setItemTextAndImage(2, "超级权限", R.mipmap.super_power_icon);
                    // itemView.setItemText(3, "超级搜索", R.mipmap.super_search_icon);
                    itemView.setItemTextAndImage(4, "发票信息", R.mipmap.invoice_icon);
                    itemView.setHideView(true);
                    itemView.setTitleVisible(false);
                }
                for (int j = 1; j < 5; j++)
                {
                    mLayoutList.add(itemView.getLayout(j));
                    mMsgReadList.add(itemView.getMSGText(j));
                }
                mContainerLayout.addView(itemView);
            }
        }

        for (int m = 0; m < mLayoutList.size(); m++)
        {
            mLayoutList.get(m).setOnClickListener(mOnClickListener);
        }
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
                        if (isOpen.equals("1"))
                        {
                            jumpHasOpen(k);
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
            mMsgReadList.get(which).setText(count);
            if (isShow)
                mMsgReadList.get(which).setVisibility(View.VISIBLE);
            else
                mMsgReadList.get(which).setVisibility(View.GONE);
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
            case 0: //公司公告

                if (mSuper == 1)
                {
                    startActivity(new Intent(getActivity(), AnnualMeetingMainActivity.class));
                } else
                {
                    intent.setClass(getActivity(), ApplyListActivity.class);
                    bundle.putInt("CHOOSE", 4);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                    UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_GT);
                    setMsgTextVisible(0, "", false);
                }

                break;
            case 1://资本快报
//                startActivity(new Intent(getActivity(), CapitalExpressActivity.class));
                break;
            case 2://新闻资讯

                break;
            case 3://会议
                intent.setClass(getActivity(), GeneralMeetingActivity.class);
                getActivity().startActivity(intent);
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_DM);
                setMsgTextVisible(3, "", false);
                break;
            case 4://项目管理
                startActivity(new Intent(getActivity(), ProjectActivity.class));
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_VM);
                setMsgTextVisible(4, "", false);
                break;
            case 5://人事
                intent.setClass(getActivity(), VacationActivity.class);
                getActivity().startActivity(intent);
                setMsgTextVisible(5, "", false);
                break;
            case 6: //差旅
                getH5();
                break;
            case 7://企业小助手
                intent.setClass(getActivity(), XZSActivity.class);
                getActivity().startActivity(intent);
                break;
            case 8://我的批审
                startActivity(new Intent(getActivity(), MyApprovalActivity.class));
                setMsgTextVisible(8, "", false);
                break;
            case 9://内刊
                startActivity(new Intent(getActivity(), MaganizeListActivity.class));
                break;
            case 10://工具
                startActivity(new Intent(getActivity(), ToolsListActivity.class));
                break;
            case 11:

                // postAnnualMeetingInfo();
                break;
            case 12: //超级用户
                startActivity(new Intent(getActivity(), SuperUserListActivity.class));
                break;
            case 13: //超级权限
                startActivity(new Intent(getActivity(), SuperPowerActivity.class));
                break;
            case 14:

                break;
            case 15://发票信息
                intent.setClass(getActivity(), AllListActivity.class);
                bundle.putInt("POSITION", 1);
                bundle.putString("TITLE", "发票信息");
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                break;
        }
    }

    private void jumpHasOpen(int k)
    {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        switch (k)
        {
            case 0: //公司公告

                if (mSuper == 1)
                {
                    startActivity(new Intent(getActivity(), AnnualMeetingMainActivity.class));
                } else
                {
                    intent.setClass(getActivity(), ApplyListActivity.class);
                    bundle.putInt("CHOOSE", 4);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                    UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_GT);
                    setMsgTextVisible(0, "", false);
                }

                break;
            case 1://资本快报
//                startActivity(new Intent(getActivity(), CapitalExpressActivity.class));
                break;
            case 2://新闻资讯

                break;
            case 3://会议
                intent.setClass(getActivity(), GeneralMeetingActivity.class);
                getActivity().startActivity(intent);
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_DM);
                setMsgTextVisible(3, "", false);
                break;
            case 4://项目管理
                startActivity(new Intent(getActivity(), ProjectActivity.class));
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_VM);
                setMsgTextVisible(4, "", false);
                break;
            case 5://人事
                intent.setClass(getActivity(), VacationActivity.class);
                getActivity().startActivity(intent);
                setMsgTextVisible(5, "", false);
                break;
            case 6: //差旅
                getH5();
                break;
            case 7://企业小助手
                intent.setClass(getActivity(), XZSActivity.class);
                getActivity().startActivity(intent);
                break;
            case 8://我的流程
                startActivity(new Intent(getActivity(), MyApprovalActivity.class));
                setMsgTextVisible(8, "", false);
                break;
            case 9://内刊
                startActivity(new Intent(getActivity(), MaganizeListActivity.class));
                break;
            case 10://工具
                startActivity(new Intent(getActivity(), ToolsListActivity.class));
                break;
            case 11://年会
                postAnnualMeetingInfo();
                break;
            case 12:
                //超级用户
                startActivity(new Intent(getActivity(), SuperUserListActivity.class));
                break;
            case 13:
                //超级权限
                startActivity(new Intent(getActivity(), SuperPowerActivity.class));
                break;
            case 14:
                break;
            case 15:
                //发票信息
                intent.setClass(getActivity(), AllListActivity.class);
                bundle.putInt("POSITION", 1);
                bundle.putString("TITLE", "发票信息");
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                break;
        }
    }

    public class WorkItemView extends LinearLayout
    {
        private LayoutInflater mInflater;
        private View mView, mBottomLine;
        private TextView mTitle;
        private RelativeLayout mOneLayout, mTwoLayout, mThreeLayout, mFourLayout;
        private ImageView mOneImage, mTwoImage, mThreeImage, mFourImage;
        private TextView mOneText, mTwoText, mThreeText, mFourText;
        private FontTextView mOneMSGText, mTwoMSGText, mThreeMSGText, mFourMSGText;
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
            mFourLayout = (RelativeLayout) mView.findViewById(R.id.item_four);

            mOneImage = (ImageView) mView.findViewById(R.id.item_one_image);
            mTwoImage = (ImageView) mView.findViewById(R.id.item_two_image);
            mThreeImage = (ImageView) mView.findViewById(R.id.item_three_image);
            mFourImage = (ImageView) mView.findViewById(R.id.item_four_image);

            mOneText = (TextView) mView.findViewById(R.id.item_one_text);
            mTwoText = (TextView) mView.findViewById(R.id.item_two_text);
            mThreeText = (TextView) mView.findViewById(R.id.item_three_text);
            mFourText = (TextView) mView.findViewById(R.id.item_four_text);

            mOneMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_one);
            mTwoMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_two);
            mThreeMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_three);
            mFourMSGText = (FontTextView) mView.findViewById(R.id.unread_msg_number_work_four);

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
            } else if (which == 4)
            {
                mFourLayout.setVisibility(visibility);
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
                mOneImage.setBackgroundResource(resource);
            } else if (which == 2)
            {
                mTwoText.setText(text);
                mTwoImage.setBackgroundResource(resource);
            } else if (which == 3)
            {
                mThreeText.setText(text);
                mThreeImage.setBackgroundResource(resource);
            } else if (which == 4)
            {
                mFourText.setText(text);
                mFourImage.setBackgroundResource(resource);
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
            } else if (which == 4)
            {
                mFourText.setTextColor(color);
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
            } else if (which == 4)
            {
                mFourImage.setBackgroundResource(resource);
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
            } else if (which == 4)
            {
                return mFourLayout;
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
            } else if (which == 4)
            {
                return mFourMSGText;
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
                case Constants.IM_PUSH_QJ: //请假
                    setMsgTextVisible(8, getAllXJUnRead() + "", true);
                    break;
                case Constants.IM_PUSH_VM: //语音会议
                    setMsgTextVisible(3, info.count + "", true);
                    break;
                case Constants.IM_PUSH_GT: //公司通知
                    setMsgTextVisible(0, info.count + "", true);
                    break;
                case Constants.IM_PUSH_DM: //日常会议
                    setMsgTextVisible(3, info.count + "", true);
                    break;
                case Constants.IM_PUSH_CK:
                    setMsgTextVisible(8, getAllXJUnRead() + "", true);
                    break;
                //消息
                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setMsgTextVisible(5, getAllItemUnRead() + "", true);
                    break;
                //流程消息
                case Constants.IM_PUSH_PROGRESS:
                    setMsgTextVisible(8, getAllXJUnRead() + "", true);
                    break;
                //销假审批
                case Constants.IM_PUSH_XIAO:
                    setMsgTextVisible(8, getAllXJUnRead() + "", true);
                    break;
            }
        } else
        {
            //隐藏小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_QJ: //请假
                    setMsgTextVisible(8, getAllXJUnRead() + "", false);
                    break;
                case Constants.IM_PUSH_VM: //语音会议
                    setMsgTextVisible(3, info.count + "", false);
                    break;

                case Constants.IM_PUSH_GT: //公司通知
                    setMsgTextVisible(0, info.count + "", false);
                    break;

                case Constants.IM_PUSH_DM: //日常会议
                    setMsgTextVisible(3, info.count + "", false);
                    break;

                case Constants.IM_PUSH_CK:
                    setMsgTextVisible(8, getAllXJUnRead() + "", false);
                    break;

                //消息
                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setMsgTextVisible(5, getAllItemUnRead() + "", false);
                    break;
                //流程消息
                case Constants.IM_PUSH_PROGRESS:
                    setMsgTextVisible(8, getAllXJUnRead() + "", false);
                    break;
                //销假审批
                case Constants.IM_PUSH_XIAO:
                    setMsgTextVisible(8, getAllXJUnRead() + "", false);
                    break;
            }
        }
    }

    //人事所有未读的数
    private int getAllItemUnRead()
    {
        int unRead = 0;
        int b = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY);
        unRead = b;
        return unRead;
    }

    //休假
    private int getAllXJUnRead()
    {
        int unRead = 0;
        int c = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK);
        int a = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ);
        int b = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS);
        int d = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO);
        unRead = a + c + b + d;
        return unRead;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        findUnReadCompanyCount();
        //休假
        findUnReadLeave();
        //消息
        findUnReadNews();

        findUnReadDayMeeting();

        findUnReadVoiceMeeting();

        findUnReadLeaveCKCount();

        findUnReadProgress();

        findUnReadReleave();
        reFresh();

    }

    //公司公告
    private void findUnReadCompanyCount()
    {
        UnReadUtils.getInstance().findUnReadCompanyCount();
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
    private void findUnReadReleave()
    {
        UnReadUtils.getInstance().findUnReadXJ();
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
    public void onDestroyView()
    {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void getH5()
    {
        ListHttpHelper.getH5(getContext(), new SDRequestCallBack(HBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(getActivity(), msg);
                } else
                {
                    MyToast.showToast(getActivity(), "获取失败");
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HBean bean = (HBean) responseInfo.getResult();
                if (bean != null)
                {
                    if (bean.getData() != null && bean.getData().length() > 0)
                    {
                        Bundle bundle = new Bundle();
                        bundle.putString("TITLE", "差旅");
                        bundle.putString("URL", bean.getData());
                        Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
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
                            DialogUtilsIm
                                    .OnYesOrNoListener2()

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
