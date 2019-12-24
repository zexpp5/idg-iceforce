package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ButtonUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.InVestedProjectInfoActivity;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.project_manager.investment_project.adapter.InvestAdapter;
import newProject.company.project_manager.investment_project.adapter.SegmentAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.FundInvestListBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.InvestTotalListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import newProject.company.project_manager.investment_project.utils.WordColorUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;
import static newProject.company.invested_project.InVestedProjectInfoActivity.mRequestCode6;

/**
 * Created by zsz on 2019/4/26.
 * 已投资项目-详情
 */

public class InvestedProjectsDetailActivity extends BaseActivity
{

    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_word)
    TextView tvWord;
    @Bind(R.id.invest_recycler_view)
    RecyclerView investRecyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_total)
    TextView tvTotal;
    @Bind(R.id.tv_month)
    TextView tvMonth;
    @Bind(R.id.tv_ownership)
    TextView tvOwnership;
    @Bind(R.id.ll_total)
    LinearLayout llTotal;
    @Bind(R.id.tv_total_1)
    TextView tvTotal1;
    @Bind(R.id.tv_month_1)
    TextView tvMonth1;
    @Bind(R.id.tv_ownership_1)
    TextView tvOwnership1;
    @Bind(R.id.ll_total_1)
    LinearLayout llTotal1;
    @Bind(R.id.tv_invest_more)
    TextView tvMore;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.btn_appl)
    Button btnAppl;
    @Bind(R.id.ll_1)
    LinearLayout llPriv;

    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.ll_water1)
    LinearLayout ll_water1;

    InvestAdapter adapter;
    List<FundInvestListBean.DataBeanX.DataBean> investDatas = new ArrayList<>();
    List<FundInvestListBean.DataBeanX.DataBean> investDatasMore = new ArrayList<>();

    SegmentAdapter segmentAdapter;
    List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists = new ArrayList<>();

    private String proId = "";

    List<IconListBean> popuList = new ArrayList<>();

    private String projectName = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invested_projects;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        projectName = getIntent().getStringExtra("projName");
        tvTitle.setText(projectName);
        investRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InvestAdapter(investDatas);
        investRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        proId = getIntent().getStringExtra("projId");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        segmentAdapter = new SegmentAdapter(segmentLists);
        segmentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                //跳转七个页面
                boolean fastDoubleClick = ButtonUtils.isFastDoubleClick();
                if (!fastDoubleClick)
                {
                    setFGTitleString(segmentLists);
                    Intent intent = new Intent(InvestedProjectsDetailActivity.this, InVestedProjectInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.FG_ALL, (Serializable) segmentLists); //权限的
                    bundle.putSerializable(Constants.FG_LIST_TITLE, (Serializable) listFgTitle);  //带多少title就显示多少
                    bundle.putSerializable(Constants.FG_LIST, (Serializable) listFgList);           //带多少Fgment就显示多少FG
                    bundle.putString(Constants.FG_DODE, segmentLists.get(position).getCode());  //这个是要跳转的 默认是baseInfo，第一个页面
                    bundle.putString(Constants.PROJECT_EID, proId);    //这个是projectID.
                    bundle.putString(Constants.USER_NAME, DisplayUtil.getUserInfo(InvestedProjectsDetailActivity.this, 11));
                    bundle.putString(Constants.PROJECT_NAME, projectName);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });

        recyclerView.setAdapter(segmentAdapter);

        btnAppl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initPopupWindowForApplyPermissions();
            }
        });

        getData();
        getInvestTotalData();
        getFundInvestData();

        setPicView(ll_water);
        setPicView(ll_water1);

    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(this, 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(this, true, false, ll_water, myNickName);
        }
    }

    List<String> listFgTitle = new ArrayList<>();
    List<String> listFgList = new ArrayList<>();

    private void setFGTitleString(List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists)
    {
        listFgTitle.clear();
        listFgList.clear();
        for (int i = 0; i < segmentLists.size(); i++)
        {
//            if (!segmentLists.get(i).getCode().equals("fundInvest") || !segmentLists.get(i).getCode().equals("meeting"))
//            {
            listFgTitle.add(segmentLists.get(i).getName());
            listFgList.add(segmentLists.get(i).getCode());
//            }
        }
    }

    public void getData()
    {
        ListHttpHelper.getPotentialProjectsDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent()
                .getStringExtra("projId"), new SDRequestCallBack(PotentialProjectsDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PotentialProjectsDetailBean bean = (PotentialProjectsDetailBean) responseInfo.getResult();
                String word = bean.getData().getData().getBaseInfoMap().getProjInvestedStatus();
                if (!StringUtils.isEmpty(word))
                {
                    tvWord.setText(word);
                    if ("S".equals(word))
                    {
                        tvWord.setBackgroundColor(getResources().getColor(R.color.blue));
                    } else if ("A".equals(word))
                    {
                        tvWord.setBackgroundColor(getResources().getColor(R.color.green));
                    } else if ("B".equals(word))
                    {
                        tvWord.setBackgroundColor(getResources().getColor(R.color.yellow));
                    } else if ("C".equals(word))
                    {
                        tvWord.setBackgroundColor(getResources().getColor(R.color.red));
                    } else if ("X".equals(word))
                    {
                        tvWord.setBackgroundColor(getResources().getColor(R.color.black));
                    } else
                    {
                        tvWord.setBackground(null);
                    }
                }

                if (bean.getData().getData().getBaseInfoMap().getApplyFlag() == 0)
                {
                    btnAppl.setVisibility(View.GONE);
                } else if (bean.getData().getData().getBaseInfoMap().getApplyFlag() == 1)
                {
                    btnAppl.setText("申请更多查看权限");
                } else if (bean.getData().getData().getBaseInfoMap().getApplyFlag() == 2)
                {
                    btnAppl.setText("权限申请正在处理中");
                    btnAppl.setClickable(false);
                } else if (bean.getData().getData().getBaseInfoMap().getApplyFlag() == 3)
                {
                    btnAppl.setText("申请权限成功");
                    btnAppl.setVisibility(View.GONE);
                } else
                {
                    btnAppl.setText("申请被驳回");
                }

                if (bean.getData().getData().getSegmentList() != null && bean.getData().getData().getSegmentList().size() > 0)
                {
                    if (bean.getData().getData().getSegmentList().size() == 1)
                    {
                        llPriv.setVisibility(View.GONE);
                    } else
                    {
                        llPriv.setVisibility(View.VISIBLE);
                    }
                    segmentLists.clear();
                    segmentLists.addAll(bean.getData().getData().getSegmentList());
                    segmentAdapter.notifyDataSetChanged();
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void getInvestTotalData()
    {
        ListHttpHelper.getInvestTotalData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), new SDRequestCallBack(InvestTotalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InvestTotalListBean bean = (InvestTotalListBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                {
                    for (int i = 0; i < bean.getData().getData().size(); i++)
                    {
                        String moneyType;
                        if ("USD".equals(bean.getData().getData().get(i).getCurrency()))
                        {
                            moneyType = "$";
                        } else
                        {
                            moneyType = "￥";
                        }
                        if (i == 0)
                        {
                            llTotal.setVisibility(View.VISIBLE);
                            if (StringUtils.notEmpty(bean.getData().getData().get(i).getInvTotal()))
                            {
                                String str = moneyType + bean.getData().getData().get(i).getInvTotal();
                                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
                                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(40);
                                ssbuilder.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tvTotal.setText(ssbuilder);
                            } else
                            {
                                tvTotal.setText("--");
                            }

                            if (StringUtils.notEmpty(bean.getData().getData().get(i).getMonthToMonth()))
                            {
                                tvOwnership.setText(bean.getData().getData().get(i).getMonthToMonth());
                                if (bean.getData().getData().get(i).getMonthToMonth().contains("-"))
                                {
                                    tvOwnership.setTextColor(getResources().getColor(R.color.green));
                                } else
                                {
                                    tvOwnership.setTextColor(getResources().getColor(R.color.red));
                                }
                            } else
                            {
                                tvOwnership.setText("--");
                            }

                        }
                        if (i == 1)
                        {
                            llTotal1.setVisibility(View.VISIBLE);
                            if (StringUtils.notEmpty(bean.getData().getData().get(i).getInvTotal()))
                            {
                                String str = moneyType + bean.getData().getData().get(i).getInvTotal();
                                SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
                                AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(40);
                                ssbuilder.setSpan(absoluteSizeSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                tvTotal1.setText(ssbuilder);
                            } else
                            {
                                tvTotal1.setText("--");
                            }

                            if (StringUtils.notEmpty(bean.getData().getData().get(i).getMonthToMonth()))
                            {
                                tvOwnership1.setText(bean.getData().getData().get(i).getMonthToMonth());
                                if (bean.getData().getData().get(i).getMonthToMonth().contains("-"))
                                {
                                    tvOwnership1.setTextColor(getResources().getColor(R.color.green));
                                } else
                                {
                                    tvOwnership1.setTextColor(getResources().getColor(R.color.red));
                                }
                            } else
                            {
                                tvOwnership1.setText("--");
                            }
                        }
                    }
                }
            }
        });
    }

    public void getFundInvestData()
    {
        ListHttpHelper.getFundInvestData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), new SDRequestCallBack(FundInvestListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                FundInvestListBean bean = (FundInvestListBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                {
                    for (int i = 0; i < bean.getData().getData().size(); i++)
                    {
                        if (i > 1)
                        {
                            investDatasMore.add(bean.getData().getData().get(i));
                        } else
                        {
                            investDatas.add(bean.getData().getData().get(i));
                            tvDate.setText("项目数据(截止" + investDatas.get(i).getValueDate() + ")");
                        }
                    }
                    if (investDatasMore.size() == 0)
                    {
                        tvMore.setVisibility(View.GONE);
                    } else
                    {
                        tvMore.setVisibility(View.VISIBLE);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }


    public void initPopupWindowForApplyPermissions()
    {

        View contentView = LayoutInflater.from(InvestedProjectsDetailActivity.this).inflate(R.layout.popupwindow_iafi, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(btnAppl, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        tvContent.setText("申请查看更多权限？");
        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnAgree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                applPermissions();
                popupWindow.dismiss();
            }
        });
        btnDisagree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });

    }

    public void applPermissions()
    {
        ListHttpHelper.applyForPermissions(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), "", new SDRequestCallBack(IDGBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode()))
                {
                    SDToast.showShort("权限申请正在处理中");
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }


    @OnClick({R.id.tv_invest_more, /*R.id.iv_at,*/ R.id.iv_back})
    public void onItemClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_invest_more:
                tvMore.setVisibility(View.GONE);
                investDatas.addAll(investDatasMore);
                adapter.notifyDataSetChanged();
                break;
            /*case R.id.iv_at:
                *//*Intent intent = new Intent(InvestedProjectsDetailActivity.this, AtActivity.class);
                intent.putExtra("projName", getIntent().getStringExtra("projName"));
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);*//*
                initlistPopupWindow((View) view.getParent());
                break;*/
            case R.id.iv_back:
                finish();
                break;
        }

    }

    public void initlistPopupWindow(View view)
    {
        popuList.clear();
        popuList.add(new IconListBean(R.mipmap.icon_public_add, "企信宝查询", "1"));
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(340);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setDropDownGravity(Gravity.RIGHT);
        final WorkCircleListPopupAdapter listPopupAdapter = new WorkCircleListPopupAdapter(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0)
                {

                }
                listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }
}
