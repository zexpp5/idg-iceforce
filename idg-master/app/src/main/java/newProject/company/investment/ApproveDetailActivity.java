package newProject.company.investment;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.ToolMainUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.api.SubmitHttpHelper;
import newProject.company.expense.DDFeeActivity;
import newProject.company.expense.bean.BeanSubmit;
import newProject.company.investment.bean.ApproveDetailBean;
import tablayout.view.textview.FontEditext;

/**
 * Created by zsz on 2019/8/27.
 */

public class ApproveDetailActivity extends BaseActivity
{
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_type)
    TextView tvType;
    @Bind(R.id.tv_sdate)
    TextView tvSdate;
    @Bind(R.id.tv_edate)
    TextView tvEdate;
    @Bind(R.id.tv_days)
    TextView tvDays;
    @Bind(R.id.tv_reason)
    TextView tvReason;
    @Bind(R.id.fet_comment)
    FontEditext fetComment;


    String flag = "1";

    ApproveDetailBean approveDetailBean;

    @Bind(R.id.iv_back)
    ImageView ivBack;

    @Bind(R.id.img_agree)
    ImageView imgAgree;
    @Bind(R.id.tv_tongyi)
    TextView tvTongyi;
    @Bind(R.id.ll_agree)
    RelativeLayout llAgree;
    @Bind(R.id.img_dis_agree)
    ImageView imgDisAgree;
    @Bind(R.id.tv_disagree)
    TextView tvDisagree;
    @Bind(R.id.ll_dis_agree)
    RelativeLayout llDisAgree;
    @Bind(R.id.btn_ok)
    Button btnOk;

    private String eid = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_approve_detail;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        eid = getIntent().getStringExtra("id");
        setAgreeStatus();
        getData();
    }

    private void setAgreeStatus()
    {
        llAgree.setBackgroundResource(R.drawable.shape_ll_pink_bg);
        llDisAgree.setBackgroundResource(R.drawable.shape_ll_bg);
        flag = "1";
        fetComment.setVisibility(View.GONE);
        imgAgree.setVisibility(View.VISIBLE);
        imgDisAgree.setVisibility(View.GONE);
    }

    private void setDisAgreeStatus()
    {
        llAgree.setBackgroundResource(R.drawable.shape_ll_bg);
        llDisAgree.setBackgroundResource(R.drawable.shape_ll_pink_bg);
        flag = "2";
        fetComment.setVisibility(View.VISIBLE);
        imgDisAgree.setVisibility(View.VISIBLE);
        imgAgree.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.ll_agree, R.id.ll_dis_agree, R.id.btn_ok})
    public void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_agree:
                setAgreeStatus();
                break;
            case R.id.ll_dis_agree:
                setDisAgreeStatus();
                break;
            case R.id.btn_ok:
                if (flag.equals("2") && StringUtils.isEmpty(fetComment.getText()))
                {
                    SDToast.showShort("请填写审批意见");
                    return;
                }
                posApprrvoal(fetComment.getText().toString(), flag);
                break;
        }
    }


    public void getData()
    {
        ListHttpHelper.getApproveDetailData(this, eid, new SDRequestCallBack(ApproveDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                approveDetailBean = (ApproveDetailBean) responseInfo.getResult();
                tvName.setText(approveDetailBean.getData().getData().getLeaveInfo().getName());
                tvType.setText(approveDetailBean.getData().getData().getLeaveInfo().getLeaveType());
                tvSdate.setText(approveDetailBean.getData().getData().getLeaveInfo().getLeaveStart());
                tvEdate.setText(approveDetailBean.getData().getData().getLeaveInfo().getLeaveEnd());
                tvDays.setText(approveDetailBean.getData().getData().getLeaveInfo().getLeaveDay() + "天");
                tvReason.setText(approveDetailBean.getData().getData().getLeaveInfo().getLeaveReason());
            }
        });
    }

    //提交审批
    private void posApprrvoal(String inputText, String select)
    {
        if (flag.equals("1"))
        {
            inputText = "同意";
        }

        //1同意、2不同意
        SubmitHttpHelper.postHolidayApprove(this, approveDetailBean.getData().getData().getApplyId(),
                approveDetailBean.getData().getData().getApproveId() + "", select, inputText, new SDRequestCallBack(BeanSubmit
                        .class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(final SDResponseInfo responseInfo)
                    {
                        final BeanSubmit beanSubmit = (BeanSubmit) responseInfo.getResult();
                        if (beanSubmit.getStatus() == 200)
                        {
                            ToolMainUtils.getInstance().getUnreadNum(ApproveDetailActivity.this, ToolMainUtils.TYPE_HOLIDAY);
                            new Handler().postDelayed(new Runnable()
                            {
                                public void run()
                                {
                                    SDToast.showShort("审批成功");
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }

}
