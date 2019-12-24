package newProject.company.expense;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chaoxiang.base.utils.NumberUtil;
import com.chaoxiang.base.utils.StringUtils;
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

import static yunjing.utils.DisplayUtil.getUserInfo;


public class ExpenseBillActivity extends AppCompatActivity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.apply_name)
    FontEditext applyName;
    @Bind(R.id.expense_time)
    FontEditext expenseTime;
    @Bind(R.id.company_name)
    FontEditext companyName;
    @Bind(R.id.item_container_layout)
    LinearLayout itemContainerLayout;
    @Bind(R.id.ticket_all)
    FontEditext ticketAll;
    @Bind(R.id.rmb_all)
    FontEditext rmbAll;
    @Bind(R.id.one_approval)
    FontEditext oneApproval;
    @Bind(R.id.two_approval)
    FontEditext twoApproval;
    @Bind(R.id.accountant_text)
    FontEditext accountantText;
    @Bind(R.id.teller_text)
    FontEditext tellerText;
    @Bind(R.id.item_layout)
    LinearLayout itemLayout;
    @Bind(R.id.button_layout)
    LinearLayout buttonLayout;
    @Bind(R.id.tv_annex)
    TextView tv_annex;

    @Bind(R.id.tv_remark)
    TextView tv_remark;

    @Bind(R.id.tv_reason_content)
    FontTextView tvReasonContent;
    @Bind(R.id.ll_psyj_main)
    LinearLayout llPsyjMain;
    @Bind(R.id.feiyong1)
    TextView feiyong1;
    @Bind(R.id.feiyong2)
    TextView feiyong2;
    @Bind(R.id.expenses_name_tv)
    FontEditext expensesNameTv;
    @Bind(R.id.specialVoucher_ll)
    LinearLayout specialVoucherLl;
    @Bind(R.id.expenses_illustration_tv)
    FontEditext expensesIllustrationTv;

    @Bind(R.id.fet_comment)
    FontEditext fetComment;
    String flag = "1";

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

    private String mEid;
    private String mBid;
    private boolean mShow = true;
    private String expenseType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_bill);
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

    private String urlEndString = "";

    private void initViews()
    {
        controllerLayout(false);

        specialVoucherLl.setVisibility(View.GONE);

        if (expenseType.equals("Reimbursement"))
        {
            titleBar.setMidText(getString(R.string.string_expense_title_Reimbursement));
            urlEndString = "reim";
        } else if (expenseType.equals("EmployeeVoucher"))
        {
            titleBar.setMidText(getString(R.string.string_expense_title_EmployeeVoucher));
            urlEndString = "ev";
        } else if (expenseType.equals("GeneralVoucher"))
        {
            titleBar.setMidText(getString(R.string.string_expense_title_GeneralVoucher));
            urlEndString = "gv";
        } else if (expenseType.equals("SpecialVoucher"))
        {
            titleBar.setMidText(getString(R.string.string_expense_title_SpecialVoucher));
            urlEndString = "sv";
            specialVoucherLl.setVisibility(View.VISIBLE);
        } else if (expenseType.equals("AssetsVoucher"))
        {
            titleBar.setMidText(getString(R.string.string_expense_title_AssetsVoucher));
            urlEndString = "av";
            feiyong1.setText("本次支付金额：");
            feiyong2.setText("累计支付金额：");
        } else
        {
            titleBar.setMidText(getString(R.string.string_expense_title_Reimbursement));
            urlEndString = "reim";
        }

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

    public void commitApproval(String inputText, String select)
    {
        ListHttpHelper.approvalBill(ExpenseBillActivity.this, expenseType, mEid, "", inputText, new SDRequestCallBack(BeanSubmit
                .class)
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
                    ToolMainUtils.getInstance().getUnreadNum(ExpenseBillActivity.this, ToolMainUtils.TYPE_COST);
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
        ListHttpHelper.approvalDisAgreeBill(ExpenseBillActivity.this, expenseType, mEid, senUser, inputText, new
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
                            ToolMainUtils.getInstance().getUnreadNum(ExpenseBillActivity.this, ToolMainUtils.TYPE_COST);
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
        ListHttpHelper.getExpenseDetail(ExpenseBillActivity.this, eid, urlEndString, new SDRequestCallBack(ExpenseDetailBean
                .class)
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
        if (data.getApply() != null)
        {
            applyName.setText(data.getApply());
        }
        if (data.getStartDate() != null)
        {
            expenseTime.setText(data.getStartDate());
        }
        if (data.getCompany() != null)
        {
            companyName.setText(data.getCompany());
        }
        if (data.getTotal() != null)
        {
            ticketAll.setText(NumberUtil.reTurnNumber(data.getTotal()));
        }
        if (data.getTotalRmb() != null)
        {
            rmbAll.setText(NumberUtil.reTurnNumber(data.getTotalRmb()));
        }
        if (data.getFirstApprove() != null)
        {
            oneApproval.setText(data.getFirstApprove());
        }
        if (data.getSecondApprove() != null)
        {
            twoApproval.setText(data.getSecondApprove());
        }
        if (data.getAccounting() != null)
        {
            accountantText.setText(data.getAccounting());
        }
        if (data.getCashier() != null)
        {
            tellerText.setText(data.getCashier());
        }

        if (data.getRemark() != null)
        {
            tv_remark.setText(data.getRemark());
        }

        if (data.getFeeList() != null && data.getFeeList().size() > 0)
        {
            itemLayout.setVisibility(View.VISIBLE);
            for (int i = 0; i < data.getFeeList().size(); i++)
            {
                ExpenseItemView itemView = new ExpenseItemView(ExpenseBillActivity.this);
                itemView.setInfo(data.getFeeList().get(i));
                itemContainerLayout.addView(itemView);
            }
        } else
        {
            itemLayout.setVisibility(View.GONE);
        }

        //ps
        if (StringUtils.notEmpty(data.getReason()))
        {
            llPsyjMain.setVisibility(View.VISIBLE);
            tvReasonContent.setText(data.getReason());
        }

        //专项费用的新增字段
        if (expenseType.equals("SpecialVoucher"))
        {
            if (StringUtils.notEmpty(data.getSpecialName()))
            {
                expensesNameTv.setText(data.getSpecialName());
            }

            if (StringUtils.notEmpty(data.getSpecialDesc()))
            {
                expensesIllustrationTv.setText(data.getSpecialDesc());
            }
        }

        llPsyjMain.setVisibility(View.GONE);
//            else
//            {
//                llPsyjMain.setVisibility(View.GONE);
//                tvReasonContent.setText("无");
//            }
    }

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
        DisplayUtil.editTextable(rmbAll, isOpen);
        DisplayUtil.editTextable(oneApproval, isOpen);
        DisplayUtil.editTextable(twoApproval, isOpen);
        DisplayUtil.editTextable(accountantText, isOpen);
        DisplayUtil.editTextable(tellerText, isOpen);
        DisplayUtil.editTextable(expensesNameTv, isOpen);
        DisplayUtil.editTextable(expensesIllustrationTv, isOpen);

    }
}
