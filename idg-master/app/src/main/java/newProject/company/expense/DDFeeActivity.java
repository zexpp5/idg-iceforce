package newProject.company.expense;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
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
import newProject.api.ListHttpHelper;
import newProject.company.expense.bean.BeanSubmit;
import newProject.company.expense.bean.ExpenseDetailBean;
import newProject.company.project_manager.estate_project.detail.EstateAnnexListFragment;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

public class DDFeeActivity extends BaseActivity
{


    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.apply_name)
    FontEditext applyName;
    @Bind(R.id.expense_time)
    FontEditext expenseTime;
    @Bind(R.id.company_name)
    FontEditext companyName;
    @Bind(R.id.ticket_all)
    FontEditext ticketAll;
    @Bind(R.id.one_approval)
    FontEditext oneApproval;
    @Bind(R.id.two_approval)
    FontEditext twoApproval;
    @Bind(R.id.three_approval)
    FontEditext threeApproval;
    @Bind(R.id.four_approval)
    FontEditext fourApproval;
    @Bind(R.id.pay_fund)
    FontEditext payFund;
    @Bind(R.id.budget_approve)
    FontEditext budgetApprove;
    @Bind(R.id.receive_bank_no)
    FontEditext receiveBankNo;
    @Bind(R.id.button_layout)
    LinearLayout buttonLayout;
    @Bind(R.id.tv_annex)
    TextView tv_annex;
    @Bind(R.id.tv_payforentity)
    TextView tv_payforentity;
    @Bind(R.id.tv_payentitymainland)
    TextView tv_payentitymainland;
    @Bind(R.id.tv_payentityoversea)
    TextView tv_payentityoversea;
    @Bind(R.id.tv_project)
    TextView tv_project;
    @Bind(R.id.tv_payforlast)
    TextView tv_payforlast;
    @Bind(R.id.tv_feedesc)
    TextView tvFeedesc;
    @Bind(R.id.tv_recommendvendor)
    TextView tvRecommendvendor;
    @Bind(R.id.tv_choosereason)
    TextView tvChoosereason;
    @Bind(R.id.tv_recommendamount)
    TextView tv_recommendamount;
    @Bind(R.id.tv_comparable1vendor)
    TextView tvComparable1vendor;
    @Bind(R.id.tv_comparable1amount)
    TextView tvComparable1amount;
    @Bind(R.id.tv_comparable2vendor)
    TextView tvComparable2vendor;
    @Bind(R.id.tv_comparable2amount)
    TextView tvComparable2amount;
    @Bind(R.id.tv_overbudgetdesc)
    TextView tvOverbudgetdesc;
    @Bind(R.id.tv_reason_content)
    FontTextView tvReasonContent;
    @Bind(R.id.ll_psyj_main)
    LinearLayout llPsyjMain;
    private String mEid;
    private String mBid;
    private boolean mShow = true;
    private String expenseType = "DDFee";

    @Bind(R.id.fet_comment)
    FontEditext fetComment;
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
    String flag = "1";

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mEid = bundle.getString("EID");
            mBid = bundle.getString("BID");
            mShow = bundle.getBoolean("YES");
            expenseType = bundle.getString("expenseType");
        }
        initViews();

        setAgreeStatus();
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

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_ddfee;
    }

    private void initViews()
    {
        controllerLayout(false);
        titleBar.setMidText("尽调费用单");
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setRightImageVisible(false);
        titleBar.setRightSecondImageVisible(false);
        titleBar.setRightTextVisible(false);
        if (mShow)
        {
            buttonLayout.setVisibility(View.VISIBLE);
            llPsyjMain.setVisibility(View.GONE);
        } else
        {
            buttonLayout.setVisibility(View.GONE);
            llPsyjMain.setVisibility(View.VISIBLE);
        }

        getPageInfo(mEid);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == titleBar.getLeftImageView())
            {
                finish();
            }
        }
    };

    @OnClick({R.id.ll_agree, R.id.ll_dis_agree, R.id.btn_ok})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
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
                if (flag.equals("1"))
                {
                    commitApproval("同意", "1");
                } else if (flag.equals("2"))
                {
                    commitDisAgreeApproval(fetComment.getText().toString());
                }
                break;
        }
    }

    public void commitApproval(String inputText, String select)
    {
        ListHttpHelper.approvalBill(DDFeeActivity.this, expenseType, mEid, "", inputText, new SDRequestCallBack(BeanSubmit.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(final SDResponseInfo responseInfo)
            {
                final BeanSubmit beanSubmit = (BeanSubmit) responseInfo.getResult();
                if (beanSubmit.getStatus() == 200)
                {
                    ToolMainUtils.getInstance().getUnreadNum(DDFeeActivity.this, ToolMainUtils.TYPE_COST);
                    new Handler().postDelayed(new Runnable()
                    {
                        public void run()
                        {
                            if (StringUtils.notEmpty(beanSubmit.getData().getReturnMessage()))
                                SDToast.showShort(beanSubmit.getData().getReturnMessage());
                            setResult(RESULT_OK);
                            finish();
                        }
                    }, 1000);
                }
            }
        });
    }

    public void commitDisAgreeApproval(String inputText)
    {
        String senUser = DisplayUtil.getUserInfo(this, 11);
        ListHttpHelper.approvalDisAgreeBill(DDFeeActivity.this, expenseType, mEid, senUser, inputText, new
                SDRequestCallBack(BeanSubmit.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(final SDResponseInfo responseInfo)
                    {
                        final BeanSubmit beanSubmit = (BeanSubmit) responseInfo.getResult();
                        if (beanSubmit.getStatus() == 200)
                        {
                            ToolMainUtils.getInstance().getUnreadNum(DDFeeActivity.this, ToolMainUtils.TYPE_COST);
                            new Handler().postDelayed(new Runnable()
                            {
                                public void run()
                                {
                                    if (StringUtils.notEmpty(beanSubmit.getData().getReturnMessage()))
                                        SDToast.showShort(beanSubmit.getData().getReturnMessage());
                                    setResult(RESULT_OK);
                                    finish();
                                }
                            }, 1000);
                        }
                    }
                });
    }


    public void getPageInfo(String eid)
    {
        ListHttpHelper.getDDFeeDetail(DDFeeActivity.this, eid, new SDRequestCallBack(ExpenseDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ExpenseDetailBean bean = (ExpenseDetailBean) responseInfo.getResult();
                if (bean != null)
                {
                    setInfo(bean.getData());
                }
            }
        });

    }

    private void setInfo(ExpenseDetailBean.DataBean data)
    {
        if (data.getFileList().size() > 0)
        {
            tv_annex.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction().add(R.id.relative_fragment, EstateAnnexListFragment.newInstance(data
                    .getFileList())).commit();
        } else
        {
            tv_annex.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(data.getType()))
        {
            titleBar.setMidText(data.getType());
        }


        if (data.getApply() != null)
        {
            applyName.setText(data.getApply());
        }
        if (data.getStartDate() != null)
        {
            expenseTime.setText(data.getStartDate());
        }

        //项目名称
        if (!TextUtils.isEmpty(data.getProjName()))
        {
            tv_project.setText(data.getProjName());
        }

        // 收款公司
        if (data.getReceiveCompany() != null)
        {
            companyName.setText(data.getReceiveCompany());
        }
        //收款公司银行账户
        if (!TextUtils.isEmpty(data.getReceiveBankNo()))
        {
            receiveBankNo.setText(data.getReceiveBankNo());
        }

        //代垫管理公司
        if (!TextUtils.isEmpty(data.getPayForEntity()))
        {
            tv_payforentity.setText(data.getPayForEntity());
        }
        //付款境内管理公司
        if (!TextUtils.isEmpty(data.getPayEntityMainland()))
        {
            tv_payentitymainland.setText(data.getPayEntityMainland());
        }
        //付款境外管理公司
        if (!TextUtils.isEmpty(data.getPayEntityOversea()))
        {
            tv_payentityoversea.setText(data.getPayEntityOversea());
        }
        //最终承担主体
        if (!TextUtils.isEmpty(data.getPayForLast()))
        {
            tv_payforlast.setText(data.getPayForLast());
        }

        //支付金额
        if (!TextUtils.isEmpty(data.getPayAmt()))
        {
            ticketAll.setText(data.getPayAmt());
        }

        //付款基金
        if (!TextUtils.isEmpty(data.getPayFund()))
        {
            payFund.setText(data.getPayFund());
        }

        //预算审批人
        if (!TextUtils.isEmpty(data.getBudgetApprove()))
        {
            budgetApprove.setText(data.getBudgetApprove());
        }

        //审批人1
        if (!TextUtils.isEmpty(data.getApprove1()))
        {
            oneApproval.setText(data.getApprove1());
        }
        //审批人2
        if (!TextUtils.isEmpty(data.getApprove2()))
        {
            twoApproval.setText(data.getApprove2());
        }
        //审批人3
        if (!TextUtils.isEmpty(data.getApprove3()))
        {
            threeApproval.setText(data.getApprove3());
        }
        //审批人4
        if (!TextUtils.isEmpty(data.getApprove4()))
        {
            fourApproval.setText(data.getApprove4());
        }


        //费用说明
        if (!TextUtils.isEmpty(data.getFeeDesc()))
        {
            tvFeedesc.setText(data.getFeeDesc());
        }

        //超预算说明
        if (!TextUtils.isEmpty(data.getOverBudgetDesc()))
        {
            tvOverbudgetdesc.setText(data.getOverBudgetDesc());
        }

        //推荐服务商
        if (!TextUtils.isEmpty(data.getRecommendVendor()))
        {
            tvRecommendvendor.setText(data.getRecommendVendor());
        }
        //推荐服务商价格
        if (!TextUtils.isEmpty(data.getRecommendAmount()))
        {
            tv_recommendamount.setText(data.getRecommendAmount() + data.getRecommendCurrency());
        }


        //可比服务商1
        if (!TextUtils.isEmpty(data.getComparable1Vendor()))
        {
            tvComparable1vendor.setText(data.getComparable1Vendor());
        }
        //可比服务商1价格
        if (!TextUtils.isEmpty(data.getComparable1Amount()))
        {
            tvComparable1amount.setText(data.getComparable1Amount() + data.getComparable1Currency());
        }


        //可比服务商
        if (!TextUtils.isEmpty(data.getComparable2Vendor()))
        {
            tvComparable2vendor.setText(data.getComparable2Vendor());
        }

        //可比服务商2价格
        if (!TextUtils.isEmpty(data.getComparable2Amount()))
        {
            tvComparable2amount.setText(data.getComparable2Amount() + data.getComparable2Currency());
        }

        //推荐理由
        if (!TextUtils.isEmpty(data.getChooseReason()))
        {
            tvChoosereason.setText(data.getChooseReason());
        }

//        if ("1".equals(data.getFinishFlag()))
//        {
//            buttonLayout.setVisibility(View.GONE);
//        } else
//        {
//            buttonLayout.setVisibility(View.VISIBLE);
//        }

        //ps

        if (StringUtils.notEmpty(data.getReason()))
        {
            llPsyjMain.setVisibility(View.VISIBLE);
            tvReasonContent.setText(data.getReason());
        }
        llPsyjMain.setVisibility(View.GONE);
//            else
//            {
//                llPsyjMain.setVisibility(View.GONE);
//                tvReasonContent.setText("无");
//            }

    }

    /**
     * 设置是否可以编辑
     *
     * @param isOpen
     */
    public void controllerLayout(boolean isOpen)
    {
        DisplayUtil.editTextable(companyName, isOpen);
        DisplayUtil.editTextable(applyName, isOpen);
        DisplayUtil.editTextable(expenseTime, isOpen);
        DisplayUtil.editTextable(ticketAll, isOpen);
        DisplayUtil.editTextable(oneApproval, isOpen);
        DisplayUtil.editTextable(twoApproval, isOpen);
        DisplayUtil.editTextable(threeApproval, isOpen);
        DisplayUtil.editTextable(fourApproval, isOpen);
        DisplayUtil.editTextable(payFund, isOpen);
        DisplayUtil.editTextable(budgetApprove, isOpen);
        DisplayUtil.editTextable(receiveBankNo, isOpen);
    }

}
