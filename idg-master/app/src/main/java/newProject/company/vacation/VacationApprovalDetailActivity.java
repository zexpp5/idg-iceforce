package newProject.company.vacation;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.DialogMeetingUtils;
import com.utils.SDToast;
import com.utils.ToolMainUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.DetailHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.company.vacation.bean.VacationApprovalDetailBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by tujingwu on 2017/10/26.
 * 请假 审批
 */
public class VacationApprovalDetailActivity extends AppCompatActivity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.tv_apply)
    TextView mTV_Apply;
    @Bind(R.id.tv_apply_day)
    TextView mTV_ApplyDay;
    @Bind(R.id.tv_vacation_type)
    TextView mTV_VacationType;
    @Bind(R.id.tv_start_time)
    TextView mTV_StartTime;
    @Bind(R.id.tv_end_time)
    TextView mTV_EndTime;
    @Bind(R.id.et_vacation_time)
    EditText mET_VacationTime;
    @Bind(R.id.et_vacation_reson)
    EditText mET_Reson;
    @Bind(R.id.et_vacation_explain)
    EditText mET_Explain;
    @Bind(R.id.add_btn)
    Button mBtn_Approval;

    @Bind(R.id.disagree_btn)
    Button disagreeBtn;


    private String mEid = "-1";
    private String mApplyId = "-1";
    private String mApproveId = "-1";
    private String mApproveBy = "";//审批人

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_approval_detail_layout);
        ButterKnife.bind(this);
        getIn();
        initTopBar();
        initListener();
        getNetData();
    }

    private void getIn()
    {
        mEid = getIntent().getStringExtra("eid");
    }

    private void initTopBar()
    {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText("请假申请");
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
        mBtn_Approval.setText("同 意");
        disagreeBtn.setText("不同意");
    }

    private void initListener()
    {

    }

    private void getNetData()
    {
        DetailHttpHelper.getApproveDetail(this, mEid, new SDRequestCallBack(VacationApprovalDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(VacationApprovalDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                setData(responseInfo);
            }

        });
    }

    private void setData(SDResponseInfo responseInfo)
    {
        VacationApprovalDetailBean detailBean = (VacationApprovalDetailBean) responseInfo.getResult();
        if (null == detailBean)
            return;
        if (null == detailBean.getData() || null == detailBean.getData().getData())
            return;

        VacationApprovalDetailBean.DataBeanX.DataBean.LeaveInfoBean leaveInfo = detailBean.getData().getData().getLeaveInfo();
        if (null == leaveInfo)
            return;

        String isApprove = detailBean.getData().getData().getIsApprove();//0-未审批  1-同意  2-不同意
        if ("0".equals(isApprove))
        {
            mBtn_Approval.setVisibility(View.VISIBLE);
        } else
        {
            mBtn_Approval.setVisibility(View.GONE);
        }
        mApplyId = detailBean.getData().getData().getApplyId();
        mApproveId = detailBean.getData().getData().getApproveId() + "";

        mTV_Apply.setText(leaveInfo.getName());

        if (null != leaveInfo.getApplyDate())
            mTV_ApplyDay.setText(leaveInfo.getApplyDate());

        if (null != leaveInfo.getLeaveType())
            mTV_VacationType.setText(leaveInfo.getLeaveType());

        if (null != leaveInfo.getLeaveStart())
            mTV_StartTime.setText(leaveInfo.getLeaveStart());

        if (null != leaveInfo.getLeaveEnd())
            mTV_EndTime.setText(leaveInfo.getLeaveEnd());


        mET_VacationTime.setText(leaveInfo.getLeaveDay() + "天");

        if (null != leaveInfo.getLeaveReason())
            mET_Reson.setText(leaveInfo.getLeaveReason());

        if (null != leaveInfo.getLeaveMemo())
            mET_Explain.setText(leaveInfo.getLeaveMemo());


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.add_btn, R.id.disagree_btn})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.add_btn:
                posApprrvoal("同意", "1");
                break;
            case R.id.disagree_btn:
                DialogMeetingUtils.getInstance().showEditSomeThingDialog(VacationApprovalDetailActivity.this, "不同意",
                        "请输入理由", new DialogMeetingUtils
                                .onTitleClickListener()
                        {
                            @Override
                            public void setTitle(String s)
                            {
                                posApprrvoal(s, "2");
                            }
                        });
                break;
        }
    }


    //提交审批
    private void posApprrvoal(String inputText, String select)
    {//1同意、2不同意
        SubmitHttpHelper.postHolidayApprove(this, mApplyId, mApproveId, select, inputText, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                ToastUtils.show(VacationApprovalDetailActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(final SDResponseInfo responseInfo)
            {
                ToolMainUtils.getInstance().getUnreadNum(VacationApprovalDetailActivity.this, ToolMainUtils.TYPE_HOLIDAY);
                new Handler().postDelayed(new Runnable()
                {
                    public void run()
                    {
                        SDToast.showShort(responseInfo.getMsg());
                        setResult(2000);
                        finish();
                    }
                }, 1000);
            }
        });
    }
}
