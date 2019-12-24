package newProject.company.expense;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.bean.HBean;
import newProject.company.hr.HrActivity;
import newProject.company.vacation.WebVacationActivity;
import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2017/12/28.
 * 我的报销
 */
public class ExpenseActivity extends BaseActivity
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work_my_expense_main;
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
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_my_expense_title));
        titleBar.setLeftImageVisible(true);
        titleBar.setRightSecondImageVisible(false);
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

    @OnClick({R.id.rl_travel_1, R.id.rl_travel_2})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_travel_1:
                setUnreadCount(unreadMsgNumberTravel1, -1);
                startActivity(new Intent(ExpenseActivity.this, ExpenseApprovalListActivity.class));
                break;
            case R.id.rl_travel_2:
                setUnreadCount(unreadMsgNumberTravel2, -1);
                startActivity(new Intent(ExpenseActivity.this, ExpenseApprovalListActivity.class));
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
