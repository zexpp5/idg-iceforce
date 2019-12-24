package newProject.company.business_trip.approval;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2019/7/22.
 */

public class ActivityAllHolidayDetail extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_leave_name)
    FontEditext tvLeaveName;
    @Bind(R.id.tv_start_time)
    FontEditext tvStartTime;
    @Bind(R.id.tv_end_time)
    FontEditext tvEndTime;
    @Bind(R.id.tv_leave_day_num)
    FontEditext tvLeaveDayNum;
    @Bind(R.id.tv_leave_type)
    FontEditext tvLeaveType;
    @Bind(R.id.tv_leave_reason)
    FontTextView tvLeaveReason;
    @Bind(R.id.tv_leave_has_meeting)
    FontTextView tvLeaveHasMeeting;
    @Bind(R.id.tv_leave_approve_content)
    FontTextView tvLeaveApproveContent;
    @Bind(R.id.ll_water)
    LinearLayout llWater;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private String userName = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_leave_info_main;
    }

    @Override
    protected void init()
    {
        getBundle();

        ButterKnife.bind(this);
        userName = loginUserAccount;
        titleBar.setMidText("休假汇总");
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    private void getBundle()
    {

    }

    private void getDetailInfo()
    {

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
