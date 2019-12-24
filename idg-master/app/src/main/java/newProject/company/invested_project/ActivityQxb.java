package newProject.company.invested_project;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.DialogImUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanQxb;
import newProject.company.invested_project.bean.BeanQxbHazardInfo;
import newProject.company.invested_project.bean.BeanQxbLawsuit;
import newProject.company.invested_project.bean.BeanQxbMajorMember;
import newProject.company.invested_project.bean.BeanQxbShareholder;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.view.DialogTextFilter;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2019/6/10.
 * 启信宝
 */
public class ActivityQxb extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_company_name)
    TextView tvCompanyName;
    @Bind(R.id.ll_state)
    LinearLayout llState;
    @Bind(R.id.tv_summary)
    TextView tvSummary;
    @Bind(R.id.tv_phone)
    TextView tvPhone;

    String companyName;
    @Bind(R.id.ll_business)
    LinearLayout llBusiness;
    @Bind(R.id.ll_business_more)
    LinearLayout llBusinessMore;
    @Bind(R.id.ll_major_member)
    LinearLayout llMajorMember;
    @Bind(R.id.ll_major_member_more)
    LinearLayout llMajorMemberMore;
    @Bind(R.id.ll_shareholder)
    LinearLayout llShareholder;
    @Bind(R.id.ll_shareholder_more)
    LinearLayout llShareholderMore;
    @Bind(R.id.ll_lawsuit)
    LinearLayout llLawsuit;
    @Bind(R.id.ll_lawsuit_more)
    LinearLayout llLawsuitMore;
    @Bind(R.id.ll_hazard_info)
    LinearLayout llHazardInfo;
    @Bind(R.id.ll_hazard_info_more)
    LinearLayout llHazardInfoMore;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.tv_legal_representative)
    TextView tvLegalRepresentative;
    @Bind(R.id.tv_create_date)
    TextView tvCreateDate;
    @Bind(R.id.tv_register_number)
    TextView tvRegisterNumber;
    @Bind(R.id.tv_register_money)
    TextView tvRegisterMoney;
    @Bind(R.id.tv_reality_money)
    TextView tvRealityMoney;

    @Bind(R.id.ll_top)
    LinearLayout llTop;

    String phoneString = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_qxb_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        companyName = getIntent().getStringExtra("companyName");

        titleBar.setMidText("启信宝查询");
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        getStatus();

        getMajorMember();
        getShareholder();
        getLwsuite();
        gethazardInfo();
    }

    private View addStateView(String str)
    {
        View view2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_child_qxb_main_info_state, null);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);
        TextView tv_state = (TextView) view2.findViewById(R.id.tv_state);
        if (StringUtils.notEmpty(str))
            tv_state.setText(str);
        return view2;
    }

    //主要成员
    private View addMajorMemberView(String title, String content)
    {
        View view2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_child_qxb_main_info_major_member,
                llMajorMember,
                false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);

        TextView tv_title = (TextView) view2.findViewById(R.id.tv_title);
        if (StringUtils.notEmpty(title))
            tv_title.setText(title);
        TextView tv_content = (TextView) view2.findViewById(R.id.tv_content);
        if (StringUtils.notEmpty(content))
            tv_content.setText(content);

        return view2;
    }

    //股东信息
    private View addShareHolderView(String title, String content)
    {
        View view2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_child_qxb_main_info_shareholder,
                llShareholder,
                false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);

        TextView tv_title = (TextView) view2.findViewById(R.id.tv_title);
        if (StringUtils.notEmpty(title))
            tv_title.setText(title);
        TextView tv_content = (TextView) view2.findViewById(R.id.tv_content);
        if (StringUtils.notEmpty(content))
            tv_content.setText("持股比例: " + content);

        return view2;
    }

    //诉讼信息
    private View addLawsuitInfoView(String title, String content, String type, String date)
    {
        View view2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_child_qxb_main_info_lawsuit_info, llLawsuit,
                false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);

        TextView tv_title = (TextView) view2.findViewById(R.id.tv_title);
        if (StringUtils.notEmpty(title))
            tv_title.setText(title);
        TextView tv_content = (TextView) view2.findViewById(R.id.tv_content);
        if (StringUtils.notEmpty(content))
            tv_content.setText(content);
        TextView tv_type = (TextView) view2.findViewById(R.id.tv_type);
        if (StringUtils.notEmpty(type))
            tv_type.setText(type);
        else
            tv_type.setVisibility(View.INVISIBLE);
        TextView tv_date = (TextView) view2.findViewById(R.id.tv_date);
        if (StringUtils.notEmpty(date))
            tv_date.setText(date);

        return view2;
    }

    //风险信息
    private View addHazardInfoView(String title, String content, String type, String date)
    {
        View view2 = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_child_qxb_main_info_risk_info, llHazardInfo,
                false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);

        TextView tv_title = (TextView) view2.findViewById(R.id.tv_title);
        if (StringUtils.notEmpty(title))
            tv_title.setText(title);
        TextView tv_content = (TextView) view2.findViewById(R.id.tv_content);
        if (StringUtils.notEmpty(content))
            tv_content.setText(content);
        TextView tv_type = (TextView) view2.findViewById(R.id.tv_type);
        if (StringUtils.notEmpty(type))
            tv_type.setText(type);
        else
            tv_type.setVisibility(View.INVISIBLE);
        TextView tv_date = (TextView) view2.findViewById(R.id.tv_date);
        if (StringUtils.notEmpty(date))
            tv_date.setText(date);

        return view2;
    }

    private void getStatus()
    {
        ListHttpHelper.getQXBStatus(ActivityQxb.this, companyName, new SDRequestCallBack(BeanQxb.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ActivityQxb.this, msg);
                llTop.setVisibility(View.GONE);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanQxb beanQxb = (BeanQxb) responseInfo.getResult();
                if (beanQxb != null && beanQxb.getStatus() == 200)
                {
                    if (StringUtils.notEmpty(beanQxb.getData().getData()) && StringUtils.notEmpty(beanQxb.getData()))
                    {
                        if (StringUtils.notEmpty(beanQxb.getData().getData().getTelephone()))
                            phoneString = beanQxb.getData().getData().getTelephone();
                        if (StringUtils.notEmpty(beanQxb.getData().getData().getName()))
                            companyName = beanQxb.getData().getData().getName();

                        setInfo(beanQxb.getData().getData());
                    }
                    llTop.setVisibility(View.VISIBLE);
                } else
                {
                    llTop.setVisibility(View.GONE);
                }
            }
        });
    }

    private void setInfo(final BeanQxb.DataBeanX.DataBean dataBean)
    {
        if (StringUtils.notEmpty(dataBean.getName()))
            tvCompanyName.setText(dataBean.getName());
        if (StringUtils.notEmpty(dataBean.getTelephone()))
            tvPhone.setText(dataBean.getTelephone());
        if (StringUtils.notEmpty(dataBean.getScope()))
            tvSummary.setText(dataBean.getScope());

        tvSummary.post(new Runnable()
        {
            @Override
            public void run()
            {
                if (tvSummary.getLineCount() > 8)
                {
                    tvSummary.setMaxLines(8);
                    tvSummary.postInvalidate();
                    tvSummary.setEllipsize(TextUtils.TruncateAt.END);
                    tvSummary.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            if (StringUtils.notEmpty(dataBean.getScope()))
                            {
                                DialogTextFilter dialogTextFilter = new DialogTextFilter();
                                dialogTextFilter.setTitleString("简介");
                                dialogTextFilter.setYesString("确定");
                                dialogTextFilter.setHaveBtn(1);
                                dialogTextFilter.setContentString(dataBean.getScope());

                                DialogImUtils.getInstance().showCommonDialog(ActivityQxb.this, dialogTextFilter, new
                                        DialogImUtils.OnYesOrNoListener()
                                        {
                                            @Override
                                            public void onYes()
                                            {

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
            }
        });

        if (StringUtils.notEmpty(dataBean.getStatus()))
        {
            llState.addView(addStateView(dataBean.getStatus()));
        } else
        {
            llState.setVisibility(View.GONE);
        }

        if (StringUtils.notEmpty(dataBean.getOperName()))
            tvLegalRepresentative.setText(dataBean.getOperName());
        if (StringUtils.notEmpty(dataBean.getStartDate()))
            tvCreateDate.setText(dataBean.getStartDate());
        if (StringUtils.notEmpty(dataBean.getRegNo()))
            tvRegisterNumber.setText(dataBean.getRegNo());
        if (StringUtils.notEmpty(dataBean.getRegistCapi()))
            tvRegisterMoney.setText(dataBean.getRegistCapi());
        if (StringUtils.notEmpty(dataBean.getRealCapi()))
            tvRealityMoney.setText(dataBean.getRealCapi());
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_phone, R.id.ll_business_more, R.id.ll_major_member_more, R.id.ll_shareholder_more, R.id.ll_lawsuit_more,
            R.id.ll_hazard_info_more})
    public void onViewClicked(View view)
    {
        Intent intent = null;
        switch (view.getId())
        {
            case R.id.tv_phone:
                if (StringUtils.notEmpty(phoneString))
                {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phoneString));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

                break;
            case R.id.ll_business_more:

                break;
            case R.id.ll_major_member_more:
                intent = new Intent(ActivityQxb.this, ActivityQxbMajorMember.class);
                intent.putExtra("companyName", companyName);
                startActivity(intent);

                break;
            case R.id.ll_shareholder_more:
                intent = new Intent(ActivityQxb.this, ActivityQxbShareholderInfo.class);
                intent.putExtra("companyName", companyName);
                startActivity(intent);

                break;
            case R.id.ll_lawsuit_more:
                intent = new Intent(ActivityQxb.this, ActivityQxbLawsuitInfo.class);
                intent.putExtra("companyName", companyName);
                startActivity(intent);

                break;
            case R.id.ll_hazard_info_more:
                intent = new Intent(ActivityQxb.this, ActivityQxbRiskInfo.class);
                intent.putExtra("companyName", companyName);
                startActivity(intent);

                break;
        }

    }

    //工商
    private void getBusiness()
    {

    }

    //人员
    private void getMajorMember()
    {
        ListHttpHelper.getMajorMember(ActivityQxb.this, companyName, 1, 10, new SDRequestCallBack(BeanQxbMajorMember.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanQxbMajorMember beanQxbMajorMember = (BeanQxbMajorMember) responseInfo.getResult();
                if (StringUtils.notEmpty(beanQxbMajorMember) && StringUtils.notEmpty(beanQxbMajorMember.getData()) &&
                        StringUtils.notEmpty(beanQxbMajorMember.getData().getData()) && beanQxbMajorMember.getData().getData()
                        .size() > 0)
                {
                    llMajorMember.removeAllViews();
                    int m = beanQxbMajorMember.getData().getData().size();
                    if (m >= 4)
                    {
                        m = 4;
                    }
                    for (int i = 0; i < m; i++)
                    {
                        BeanQxbMajorMember.DataBeanX.DataBean dataBean = beanQxbMajorMember.getData().getData().get(i);
                        llMajorMember.addView(addMajorMemberView(dataBean.getName(), dataBean.getJobTitle()));
                    }
                } else
                {
                    llMajorMember.setVisibility(View.GONE);
                }
            }
        });
    }

    //股东
    private void getShareholder()
    {
        ListHttpHelper.getShareholder(ActivityQxb.this, companyName, 1, 10, new SDRequestCallBack(BeanQxbShareholder.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanQxbShareholder beanQxbShareholder = (BeanQxbShareholder) responseInfo.getResult();
                if (StringUtils.notEmpty(beanQxbShareholder) && StringUtils.notEmpty(beanQxbShareholder.getData()) &&
                        StringUtils.notEmpty(beanQxbShareholder.getData().getData()) && beanQxbShareholder.getData().getData()
                        .size() > 0)
                {
                    llShareholder.removeAllViews();
                    int m = beanQxbShareholder.getData().getData().size();
                    if (m >= 4)
                    {   
                        m = 4;
                    }
                    for (int i = 0; i < m; i++)
                    {
                        BeanQxbShareholder.DataBeanX.DataBean dataBean = beanQxbShareholder.getData().getData().get(i);
                        llShareholder.addView(addShareHolderView(dataBean.getShareholderName(), dataBean.getInvsumfundedratio()));
                    }
                } else
                {
                    llShareholder.setVisibility(View.GONE);
                }
            }
        });
    }

    //诉讼
    private void getLwsuite()
    {
        ListHttpHelper.getLawsuite(ActivityQxb.this, companyName, 1, 10, new SDRequestCallBack(BeanQxbLawsuit.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanQxbLawsuit beanQxbLawsuit = (BeanQxbLawsuit) responseInfo.getResult();
                if (StringUtils.notEmpty(beanQxbLawsuit) && StringUtils.notEmpty(beanQxbLawsuit.getData()) &&
                        StringUtils.notEmpty(beanQxbLawsuit.getData().getData()) && beanQxbLawsuit.getData().getData()
                        .size() > 0)
                {
                    llLawsuit.removeAllViews();
                    int m = beanQxbLawsuit.getData().getData().size();
                    if (m >= 4)
                    {
                        m = 4;
                    }
                    for (int i = 0; i < m; i++)
                    {
                        BeanQxbLawsuit.DataBeanX.DataBean dataBean = beanQxbLawsuit.getData().getData().get(i);
                        llLawsuit.addView(addLawsuitInfoView(dataBean.getTitle(), dataBean.getContent(), dataBean.getType(),
                                dataBean.getDate()));
                    }
                } else
                {
                    llLawsuit.setVisibility(View.GONE);
                }
            }
        });
    }

    //风险
    private void gethazardInfo()
    {
        ListHttpHelper.gethazardInfo(ActivityQxb.this, companyName, 1, 10, new SDRequestCallBack(BeanQxbHazardInfo.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanQxbHazardInfo beanQxbHazardInfo = (BeanQxbHazardInfo) responseInfo.getResult();
                if (StringUtils.notEmpty(beanQxbHazardInfo) && StringUtils.notEmpty(beanQxbHazardInfo.getData()) &&
                        StringUtils.notEmpty(beanQxbHazardInfo.getData().getData()) && beanQxbHazardInfo.getData().getData()
                        .size() > 0)
                {
                    llHazardInfo.removeAllViews();
                    int m = beanQxbHazardInfo.getData().getData().size();
                    if (m >= 4)
                    {
                        m = 4;
                    }
                    for (int i = 0; i < m; i++)
                    {
                        BeanQxbHazardInfo.DataBeanX.DataBean dataBean = beanQxbHazardInfo.getData().getData().get(i);
                        llHazardInfo.addView(addHazardInfoView(dataBean.getTitle(), dataBean.getContent(), dataBean.getType(),
                                dataBean.getDate()));
                    }
                } else
                {
                    llHazardInfo.setVisibility(View.GONE);
                }
            }
        });
    }
}
