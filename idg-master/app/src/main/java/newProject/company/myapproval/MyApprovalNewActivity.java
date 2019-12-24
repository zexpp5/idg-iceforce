package newProject.company.myapproval;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.company.business_trip.approval.ActivityAllHolidayList;
import newProject.company.expense.ExpenseListActivity;
import newProject.company.business_trip.approval.BusinessTripApprovalListActivity;
import newProject.company.leave_approval.LeaveAppListActivity;
import newProject.company.vacation.VacationApprovalListActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by selson on 2017/12/28.
 * 我的审批-新
 */
public class MyApprovalNewActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.unread_msg_number_travel_1)
    FontTextView unreadMsgNumberTravel1;
    @Bind(R.id.rl_travel_1)
    RelativeLayout rlTravel1;
    @Bind(R.id.unread_msg_number_travel_2)
    FontTextView unreadMsgNumberTravel2;
    @Bind(R.id.rl_travel_2)
    RelativeLayout rlTravel2;

    @Bind(R.id.unread_msg_number_travel_3)
    FontTextView unreadMsgNumberTravel3;
    @Bind(R.id.rl_travel_3)
    RelativeLayout rlTravel3;

    @Bind(R.id.unread_msg_number_travel_4)
    FontTextView unreadMsgNumberTravel4;
    @Bind(R.id.rl_travel_4)
    RelativeLayout rlTravel4;

    @Bind(R.id.rl_travel_5)
    RelativeLayout rlTravel5;
    @Bind(R.id.unread_msg_number_travel_5)
    FontTextView unreadMsgNumberTravel5;

    @Bind(R.id.rl_travel_11)
    LinearLayout rl_travel_11;
    @Bind(R.id.rl_travel_22)
    LinearLayout rl_travel_22;
    @Bind(R.id.rl_travel_33)
    LinearLayout rl_travel_33;
    @Bind(R.id.rl_travel_44)
    LinearLayout rl_travel_44;
    @Bind(R.id.rl_travel_55)
    LinearLayout rl_travel_55;





    private boolean holidatApprove = false;
    private boolean costApprove = false;
    private boolean travelApprove = false;
    private boolean finshleaveApprove = false;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_my_approval_new_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            holidatApprove=bundle.getBoolean("holidatApprove");
            costApprove=bundle.getBoolean("costApprove");
            travelApprove=bundle.getBoolean("travelApprove");
            finshleaveApprove=bundle.getBoolean("finshleaveApprove");
        }

        if (travelApprove)
        {
            rl_travel_11.setVisibility(View.VISIBLE);
        } else
        {
            rl_travel_11.setVisibility(View.GONE);
        }
        if (holidatApprove)
        {
            rl_travel_22.setVisibility(View.VISIBLE);
            rl_travel_55.setVisibility(View.VISIBLE);
        } else
        {
            rl_travel_22.setVisibility(View.GONE);
            rl_travel_55.setVisibility(View.GONE);
        }

        if (costApprove)
        {
            rl_travel_33.setVisibility(View.VISIBLE);
        } else
        {
            rl_travel_33.setVisibility(View.GONE);
        }

        if (finshleaveApprove)
        {
            rl_travel_44.setVisibility(View.VISIBLE);
        } else
        {
            rl_travel_44.setVisibility(View.GONE);
        }

        initTitle();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setUnreadCount(unreadMsgNumberTravel2, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK));
        setUnreadCount(unreadMsgNumberTravel1, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP2));
        setUnreadCount(unreadMsgNumberTravel4, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO2));
        setUnreadCount(unreadMsgNumberTravel3, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_BSPS));
//        getExpenseNum();
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_my_approve_title));
        titleBar.setLeftImageVisible(true);
        titleBar.setRightSecondImageVisible(false);

        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    public void setUnreadCount(TextView view, int count)
    {
        if (count > 0)
        {
            if (count > 99)
            {
                view.setText("99+");
            } else
            {
                view.setText(String.valueOf(count));
            }
            view.setVisibility(View.VISIBLE);
        } else
        {
            view.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.rl_travel_1, R.id.rl_travel_2, R.id.rl_travel_5, R.id.rl_travel_3, R.id.rl_travel_4})
    public void onViewClicked(View view)
    {   
        switch (view.getId())
        {
            case R.id.rl_travel_1:
//                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CLSP2);
                //差旅
                startActivity(new Intent(MyApprovalNewActivity.this, BusinessTripApprovalListActivity.class));
//                setUnreadCount(unreadMsgNumberTravel1, -1);
                break;

            case R.id.rl_travel_2:
//                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CK);
                //请假
                startActivity(new Intent(MyApprovalNewActivity.this, VacationApprovalListActivity.class));
//                setUnreadCount(unreadMsgNumberTravel2, -1);
                break;

            case R.id.rl_travel_5:
                //休假汇总
                startActivity(new Intent(MyApprovalNewActivity.this, ActivityAllHolidayList.class));
                break;

            case R.id.rl_travel_3:
//                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_BSPS);
//                setUnreadCount(unreadMsgNumberTravel3, -1);
                //报销
                startActivity(new Intent(MyApprovalNewActivity.this, ExpenseListActivity.class));
                break;

            case R.id.rl_travel_4:
//                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_XIAO2);
//                setUnreadCount(unreadMsgNumberTravel4, -1);
                //销假
                startActivity(new Intent(MyApprovalNewActivity.this, LeaveAppListActivity.class));
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnReadEvent(UnReadMessage info)
    {
        if (info.isShow)
        {
            //显示小红点
            switch (info.menuId)
            {
                case Constants.IM_PUSH_CLSP2:
                    setUnreadCount(unreadMsgNumberTravel1, info.count);
                    break;
                case Constants.IM_PUSH_CK:
                    setUnreadCount(unreadMsgNumberTravel2, info.count);
                    break;
                case Constants.IM_PUSH_XIAO2:
                    setUnreadCount(unreadMsgNumberTravel4, info.count);
                    break;
                case Constants.IM_PUSH_BSPS:
                    setUnreadCount(unreadMsgNumberTravel3, info.count);
                    break;
            }
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
