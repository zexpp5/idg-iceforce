package newProject.ui.news.detail;

import android.os.Bundle;
import android.view.View;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.DetailHttpHelper;
import newProject.company.vacation.bean.VacationApprovalDetailBean;
import tablayout.view.textview.FontTextView;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by selson on 2017/12/29.
 * 消息详情
 */
public class NewsDetailAcitity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_user)
    FontTextView tvUser;
    @Bind(R.id.tv_create_time)
    FontTextView tvCreateTime;
    @Bind(R.id.tv_type)
    FontTextView tvType;
    @Bind(R.id.tv_start_time)
    FontTextView tvStartTime;
    @Bind(R.id.tv_end_time)
    FontTextView tvEndTime;
    @Bind(R.id.tv_use_time)
    FontTextView tvUseTime;
    @Bind(R.id.tv_reason)
    FontTextView tvReason;
    @Bind(R.id.tv_remark_content)
    FontTextView tvRemarkContent;

    private String mEid = "";
    private String createTime = "";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();

        initTitle();

        if (bundle != null)
        {
            mEid = bundle.getString("eid");
            createTime = bundle.getString("createTime");
        }

        if (StringUtils.notEmpty(mEid))
            getData();
    }

    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_push_type_02));
//        titleBar.setLeftImageVisible(true);
//        titleBar.setRightSecondImageVisible(true);
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

    private void getData()
    {
        DetailHttpHelper.getApproveDetail(NewsDetailAcitity.this, mEid, new SDRequestCallBack(VacationApprovalDetailBean
                .class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(NewsDetailAcitity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                VacationApprovalDetailBean vacationApprovalDetailBean = (VacationApprovalDetailBean) responseInfo.getResult();
                if (vacationApprovalDetailBean!=null){
                    setInfo(vacationApprovalDetailBean);
                }
            }
        });
    }

    private void setInfo(VacationApprovalDetailBean vacationApprovalDetailBean)
    {
        VacationApprovalDetailBean.DataBeanX.DataBean.LeaveInfoBean info = vacationApprovalDetailBean.getData().getData()
                .getLeaveInfo();
        tvUser.setText(info.getName());
        tvCreateTime.setText(createTime);
        tvType.setText(info.getLeaveType());
        tvStartTime.setText(info.getLeaveStart());
        tvEndTime.setText(info.getLeaveEnd());
        tvUseTime.setText(info.getLeaveDay() + " 天");
        tvReason.setText(info.getLeaveReason());
        tvRemarkContent.setText(info.getLeaveMemo());
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_news_detail_layout;
    }

}
