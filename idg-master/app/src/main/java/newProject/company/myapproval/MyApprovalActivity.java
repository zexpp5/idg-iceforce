package newProject.company.myapproval;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.company.expense.ExpenseApprovalListActivity;
import newProject.company.expense.ExpenseListActivity;
import newProject.company.leave_approval.LeaveAppListActivity;
import newProject.company.leave_approval.VacationAgreeActivity;
import newProject.company.resumption.LeaveMsgListActivity;
import newProject.company.resumption.ReLeaveListActivity;
import newProject.company.vacation.VacationApprovalListActivity;
import newProject.company.vacation.VacationFragment;
import newProject.company.vacation.VacationListActivity;
import newProject.mine.CollectHttpHelper;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;
import yunjing.utils.FragmentCallBackInterface;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by tujingwu on 2017/10/26.
 */
public class MyApprovalActivity extends BaseActivity implements FragmentCallBackInterface
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;
    @Bind(R.id.vacation_apply_layout)
    RelativeLayout mVacationApply;
    @Bind(R.id.expense_apply_layout)
    RelativeLayout mExpenseApply;
    @Bind(R.id.vacation_approval_layout)
    RelativeLayout vacationApprovalLayout;
    @Bind(R.id.expense_approval_layout)
    RelativeLayout expenseApprovalLayout;
    @Bind(R.id.vacation_layout)
    RelativeLayout vacationLayout;
    @Bind(R.id.unread_msg_number_work_one)
    FontTextView unreadMsgNumberWorkOne;
    @Bind(R.id.unread_msg_number_work_two)
    FontTextView unreadMsgNumberWorkTwo;
    @Bind(R.id.unread_msg_number_work_five)
    FontTextView unreadMsgNumberWorkFive;
    @Bind(R.id.unread_msg_number_work_six)
    FontTextView unreadMsgNumberWorkSix;
    @Bind(R.id.unread_msg_number_work_night)
    FontTextView unreadMsgNumberWorkNight;
    @Bind(R.id.unread_msg_number_work_ten)
    FontTextView unreadMsgNumberWorkTen;
    @Bind(R.id.two_layout)
    RelativeLayout twoLayout;
    @Bind(R.id.unread_msg_number_work_seven)
    FontTextView unreadMsgNumberWorkSeven;
    @Bind(R.id.seven_layout)
    RelativeLayout sevenLayout;
    @Bind(R.id.unread_msg_number_work_eleven)
    FontTextView unreadMsgNumberWorkEleven;
    @Bind(R.id.eleven_layout)
    RelativeLayout elevenLayout;
    @Bind(R.id.unread_msg_number_work_three)
    FontTextView unreadMsgNumberWorkThree;
    @Bind(R.id.three_layout)
    RelativeLayout threeLayout;
    private FragmentManager mFragmentManager;
    private Fragment mSelectFragment;


    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initTopBar();
        initListener();
        hasApproval();
    }

    private void hasApproval()
    {
        ListHttpHelper.getHasApproval(MyApprovalActivity.this, new SDRequestCallBack(HasBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(MyApprovalActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HasBean numbean = (HasBean) responseInfo.getResult();
                if (numbean.isData())
                {
                    elevenLayout.setVisibility(View.VISIBLE);
                } else
                {
                    elevenLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_my_approval_layout;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        //0-请假审批,1-我的请假
        setUnreadCount(0, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK), true);
        setUnreadCount(1, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ), true);
        setUnreadCount(2, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS), true);
        //  setUnreadCount(3, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO), true);
        setBXnum();
    }

    //0-请假审批,1-我的请假 2-流程消息  3-我的销假
    public void setUnreadCount(int which, int count, boolean isShow)
    {
        if (which == 0)
        {
            if (isShow)
            {
                if (count > 0)
                {
                    if (count > 99)
                    {
                        unreadMsgNumberWorkNight.setText("99+");
                    } else
                    {
                        unreadMsgNumberWorkNight.setText(String.valueOf(count));
                    }
                    unreadMsgNumberWorkNight.setVisibility(View.VISIBLE);
                } else
                {
                    unreadMsgNumberWorkNight.setVisibility(View.GONE);
                }
            } else
            {
                unreadMsgNumberWorkNight.setVisibility(View.GONE);
            }
        } else if (which == 1)
        {
            if (isShow)
            {
                if (count > 0)
                {
                    if (count > 99)
                    {
                        unreadMsgNumberWorkFive.setText("99+");
                    } else
                    {
                        unreadMsgNumberWorkFive.setText(String.valueOf(count));
                    }
                    unreadMsgNumberWorkFive.setVisibility(View.VISIBLE);
                } else
                {
                    unreadMsgNumberWorkFive.setVisibility(View.GONE);
                }
            } else
            {
                unreadMsgNumberWorkFive.setVisibility(View.GONE);
            }

        } else if (which == 2)
        {
            if (isShow)
            {
                if (count > 0)
                {
                    if (count > 99)
                    {
                        unreadMsgNumberWorkThree.setText("99+");
                    } else
                    {
                        unreadMsgNumberWorkThree.setText(String.valueOf(count));
                    }
                    unreadMsgNumberWorkThree.setVisibility(View.VISIBLE);
                } else
                {
                    unreadMsgNumberWorkThree.setVisibility(View.GONE);
                }
            } else
            {
                unreadMsgNumberWorkThree.setVisibility(View.GONE);
            }
        } else if (which == 3)
        {
            if (isShow)
            {
                if (count > 0)
                {
                    if (count > 99)
                    {
                        unreadMsgNumberWorkSeven.setText("99+");
                    } else
                    {
                        unreadMsgNumberWorkSeven.setText(String.valueOf(count));
                    }
                    unreadMsgNumberWorkSeven.setVisibility(View.VISIBLE);
                } else
                {
                    unreadMsgNumberWorkSeven.setVisibility(View.GONE);
                }
            } else
            {
                unreadMsgNumberWorkSeven.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 推送接收
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
                case Constants.IM_PUSH_CK: //
//                    setUnreadCount(0, info.count, true);
                    break;
                case Constants.IM_PUSH_QJ: //请假
                    setUnreadCount(1, info.count, true);
                    break;
                case Constants.IM_PUSH_PROGRESS: //流程消息
                    setUnreadCount(2, info.count, true);
                    break;
                case Constants.IM_PUSH_XIAO:
                    //       setUnreadCount(3, info.count, true);
                    break;
            }
        } else
        {
            //隐藏小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_CK: //
//                    setUnreadCount(0, 0, false);
                    break;
                case Constants.IM_PUSH_QJ: //请假
                    setUnreadCount(1, 0, false);
                    break;
                case Constants.IM_PUSH_PROGRESS: //流程消息
                    setUnreadCount(2, info.count, false);
                    break;
                case Constants.IM_PUSH_XIAO:
                    setUnreadCount(3, info.count, false);
                    break;
            }
        }
    }

    private void initTopBar()
    {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText("我的流程");
        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == mTopBar.getLeftImageView())
                {
                    finish();
                }
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        elevenLayout.setVisibility(View.INVISIBLE);
    }

    private void initListener()
    {
        twoLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MyApprovalActivity.this, VacationAgreeActivity.class));
            }
        });
        //消息列表
        threeLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_PROGRESS);
                startActivity(new Intent(MyApprovalActivity.this, LeaveMsgListActivity.class));
                setUnreadCount(2, 0, false);
            }
        });
        //请假批审
        vacationApprovalLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CK);
                startActivity(new Intent(MyApprovalActivity.this, VacationApprovalListActivity.class));
                setUnreadCount(0, 0, false);
            }
        });
        //报销审批
        expenseApprovalLayout.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                setUnReadCount(unreadMsgNumberWorkTen, 0);
                startActivity(new Intent(MyApprovalActivity.this, ExpenseListActivity.class));
            }
        });
        //请假申请
        mVacationApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                VacationFragment fragment = new VacationFragment();
                replaceFragment(fragment);
            }
        });
        //我的请假
        vacationLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_QJ);
                startActivity(new Intent(MyApprovalActivity.this, VacationListActivity.class));
                setUnreadCount(1, 0, false);
            }
        });
        //我的报销
        mExpenseApply.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MyApprovalActivity.this, ExpenseApprovalListActivity.class));
            }
        });
        //我的销假
        sevenLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_XIAO);
                startActivity(new Intent(MyApprovalActivity.this, ReLeaveListActivity.class));
                setUnreadCount(3, 0, false);
            }
        });

        //销假审批
        elevenLayout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(MyApprovalActivity.this, LeaveAppListActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment)
    {
        if (fragment == null)
        {
            return;
        }
        if (mFragmentManager == null)
        {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.apply_fragment_container, fragment);
        ft.commit();
    }


    @Override
    public void setSelectedFragment(Fragment fragment)
    {
        mSelectFragment = fragment;
    }

    @Override
    public void refreshList()
    {

    }

    @Override
    public void callBackObject(Object object)
    {

    }

    @Override
    public void onBackPressed()
    {
        //直接返回
        if (mSelectFragment == null)
        {
            super.onBackPressed();
        } else
        {
            //进入fragment 返回
            mFragmentManager.beginTransaction().remove(mSelectFragment).commit();
            mSelectFragment.onDestroyView();
            mSelectFragment = null;
            DisplayUtil.hideInputSoft(this);
        }
    }

    private void setBXnum()
    {
        CollectHttpHelper.getBxNum(MyApprovalActivity.this, new SDRequestCallBack(Numbean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
//                MyToast.showToast(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                Numbean numbean = (Numbean) responseInfo.getResult();
                if (numbean != null)
                {
                    setUnReadCount(unreadMsgNumberWorkTen, numbean.getData());
                } else
                {
                    unreadMsgNumberWorkTen.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void setUnReadCount(TextView view, int count)
    {
        if (StringUtils.notEmpty(view) && StringUtils.notEmpty(count))
        {
            if (count > 1)
            {
                if (count > 99)
                {
                    view.setText("99 +");
                    view.setVisibility(View.VISIBLE);
                } else
                {
                    view.setText(count);
                }
            } else
            {
                view.setVisibility(View.GONE);
            }
        } else
        {

        }
    }

}
