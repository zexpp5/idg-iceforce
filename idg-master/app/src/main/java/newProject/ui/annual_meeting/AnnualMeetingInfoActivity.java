package newProject.ui.annual_meeting;

import android.content.Intent;
import android.view.View;

import com.chaoxiang.base.utils.DialogUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.base.SDSelectContactActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;

/**
 *
 */
public class AnnualMeetingInfoActivity extends BaseActivity
{
    AnnualMeetingDetailBean annualMeetingDetailBean = null;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_title)
    FontTextView tvTitle;
    @Bind(R.id.tv_year)
    FontTextView tvYear;
    @Bind(R.id.tv_time)
    FontTextView tvTime;
    @Bind(R.id.tv_member)
    FontTextView tvMember;
    @Bind(R.id.btn_more)
    FontTextView btnMore;
    @Bind(R.id.tv_plan)
    FontTextView tvPlan;

    private List<String> mEidLists = new ArrayList<>();

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        initTitle();
        getNetData();
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_work_annual_m_first_01));
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_annual_meeting_info_main;
    }

    private void getNetData()
    {
        BasicDataHttpHelper.postNianInfo(AnnualMeetingInfoActivity.this, new SDRequestCallBack(AnnualMeetingDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(AnnualMeetingInfoActivity.this, msg);
                AnnualMeetingInfoActivity.this.finish();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                annualMeetingDetailBean = (AnnualMeetingDetailBean) responseInfo.getResult();
                if (annualMeetingDetailBean.getStatus() == 200)
                {
                    setInfo(annualMeetingDetailBean);

                } else if (annualMeetingDetailBean.getStatus() == 400)
                {
                    DialogUtils.getInstance().showDialog(AnnualMeetingInfoActivity.this, annualMeetingDetailBean.getMsg(), new
                            DialogUtils.OnYesOrNoListener()
                            {
                                @Override
                                public void onYes()
                                {
                                    AnnualMeetingInfoActivity.this.finish();
                                }

                                @Override
                                public void onNo()
                                {

                                }
                            });
                }
            }
        });
    }

    private void setInfo(AnnualMeetingDetailBean info)
    {
        if (StringUtils.notEmpty(info))
        {
            tvTitle.setText(info.getData().getDetail().getTitle());
            tvYear.setText(info.getData().getDetail().getYear());
            tvTime.setText(info.getData().getDetail().getMeetTime());
            tvMember.setText(setMember(info));
            tvPlan.setText(info.getData().getDetail().getRemark());
        }
    }

    private String setMember(AnnualMeetingDetailBean info)
    {
        String memberString = "";
        if (StringUtils.notEmpty(info.getData().getSignList()))
        {
            List<AnnualMeetingDetailBean.DataBean.SignListBean> signList = info.getData().getSignList();
            if (signList.size() > 0)
            {
                for (int i = 0; i < signList.size(); i++)
                {
                    mEidLists.add(signList.get(i).getUserId() + "");
                    if (i + 1 < signList.size())
                        memberString += signList.get(i).getName() + "、";
                }
            }
        }
        return memberString;

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.btn_more)
    public void onViewClicked()
    {
        if (mEidLists.size() > 0)
        {
            Intent intent = new Intent(AnnualMeetingInfoActivity.this, SDSelectContactActivity.class);
            intent.putExtra(SDSelectContactActivity.HIDE_TAB, false);
            intent.putExtra(SDSelectContactActivity.IS_NO_CHECK_BOX, true);
            intent.putExtra(SDSelectContactActivity.SELECTED_ONE, false);
            intent.putExtra(SDSelectContactActivity.TITLE_TEXT, "签到人员");
            intent.putExtra(SDSelectContactActivity.IS_NEED_REMOVEOWER, false);
            intent.putExtra(SDSelectContactActivity.IM_ACCOUNT_LIST, (Serializable) mEidLists);
            intent.putExtra(SDSelectContactActivity.IS_CAN_CHANGE, false);
            startActivity(intent);
        }
    }
}
