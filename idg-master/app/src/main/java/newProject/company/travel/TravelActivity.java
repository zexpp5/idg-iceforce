package newProject.company.travel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.bean.HBean;
import newProject.company.business_trip.BusinessTripAddActivity;
import newProject.company.business_trip.BusinessTripListActivity;
import newProject.company.vacation.WebVacationActivity;
import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2017/12/28.
 * 差旅
 */
public class TravelActivity extends BaseActivity
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work_travel_main;
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
        setUnreadCount(unreadMsgNumberTravel3, SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP));
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_travel_title));
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

    @OnClick({R.id.rl_travel_1, R.id.rl_travel_2, R.id.rl_travel_3})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_travel_1:
                setUnreadCount(unreadMsgNumberTravel1, -1);
                startActivity(new Intent(TravelActivity.this, BusinessTripAddActivity.class));
                break;
            case R.id.rl_travel_2:
                setUnreadCount(unreadMsgNumberTravel2, -1);
                getH5();
                break;
            case R.id.rl_travel_3:
                UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CLSP);
                setUnreadCount(unreadMsgNumberTravel3, -1);
                startActivity(new Intent(TravelActivity.this, BusinessTripListActivity.class));
                break;
        }
    }

    public void getH5()
    {
        ListHttpHelper.getH5(TravelActivity.this, new SDRequestCallBack(HBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (msg != null)
                {
                    MyToast.showToast(TravelActivity.this, msg);
                } else
                {
                    MyToast.showToast(TravelActivity.this, "获取失败");
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
                        bundle.putString("TITLE", "差旅预定");
                        bundle.putString("URL", bean.getData());
                        Intent intent = new Intent(TravelActivity.this, WebVacationActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
