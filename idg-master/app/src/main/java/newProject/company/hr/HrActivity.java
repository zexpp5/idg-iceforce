package newProject.company.hr;

import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.company.resumption.LeaveMsgListActivity;
import newProject.company.resumption.ReLeaveListActivity;
import newProject.company.vacation.VacationListActivity;
import newProject.ui.news.NewsListActivity;
import tablayout.view.textview.FontTextView;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by selson on 2017/12/28.
 * 移动HR
 */
public class HrActivity extends BaseActivity
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work_hr_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        initTitle();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        setUnreadCount(unreadMsgNumberTravel1, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ));
        setUnreadCount(unreadMsgNumberTravel2, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO));
        setUnreadCount(unreadMsgNumberTravel3, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY));
        setUnreadCount(unreadMsgNumberTravel4, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS));
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_hr_title));
        titleBar.setLeftImageVisible(true);
        titleBar.setRightSecondImageVisible(false);
        titleBar.setRightImageVisible(true);
        titleBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(HrActivity.this, LeaveApplyActivity.class));
            }
        });
        titleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

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

    @OnClick({R.id.rl_travel_1, R.id.rl_travel_2, R.id.rl_travel_3, R.id.rl_travel_4})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_travel_1:
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_QJ);
                startActivity(new Intent(HrActivity.this, VacationListActivity.class));
                setUnreadCount(unreadMsgNumberTravel1, -1);
                break;
            case R.id.rl_travel_2:
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_XIAO);
                startActivity(new Intent(HrActivity.this, ReLeaveListActivity.class));
                setUnreadCount(unreadMsgNumberTravel2, -1);
                break;
            case R.id.rl_travel_3:
                setUnreadCount(unreadMsgNumberTravel3, -1);
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_PUSH_HOLIDAY);
                startActivity(new Intent(HrActivity.this, NewsListActivity.class));
                break;
            case R.id.rl_travel_4:
                setUnreadCount(unreadMsgNumberTravel4, -1);
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_PROGRESS);
                startActivity(new Intent(HrActivity.this, LeaveMsgListActivity.class));
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
                case Constants.IM_PUSH_QJ: //请假
                    setUnreadCount(unreadMsgNumberTravel1, info.count);
                    break;
                case Constants.IM_PUSH_XIAO:
                    setUnreadCount(unreadMsgNumberTravel2, info.count);
                    break;
                case Constants.IM_PUSH_PUSH_HOLIDAY:
                    setUnreadCount(unreadMsgNumberTravel3, info.count);
                    break;
                case Constants.IM_PUSH_PROGRESS:
                    setUnreadCount(unreadMsgNumberTravel4, info.count);
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
