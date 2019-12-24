package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.project_manager.investment_project.adapter.FundHorizonAdapter;
import newProject.company.project_manager.investment_project.adapter.PostInvestmentManagementAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkSummaryDetailHorizonAdapter;
import newProject.company.project_manager.investment_project.bean.NameListsBean;
import newProject.company.project_manager.investment_project.bean.PIMFundListBean;
import newProject.company.project_manager.investment_project.bean.PostInvestmentListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;


/**
 * Created by zsz on 2019/5/9.
 */

public class PostInvestmentManagementForPartnerActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view_horizon)
    RecyclerView recyclerViewHorizon;

    FundHorizonAdapter horizonAdapter;
    List<PIMFundListBean.DataBeanX.DataBean> horizonDatas = new ArrayList<>();
    int index = 0;

    private int mPage = 1;
    private int mBackListSize = 0;
    String fundId;

    PostInvestmentManagementAdapter adapter;

    List<PostInvestmentListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String key;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_post_investment_managemnet_for_partner;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setMidText("已投项目");

        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupwindow();
            }
        });


        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHorizon.setLayoutManager(lm);
        horizonAdapter = new FundHorizonAdapter(PostInvestmentManagementForPartnerActivity.this, horizonDatas);
        recyclerViewHorizon.setAdapter(horizonAdapter);
        horizonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                horizonDatas.get(index).setChoose(false);
                index = position;
                horizonDatas.get(index).setChoose(true);
                horizonAdapter.notifyDataSetChanged();
                fundId = horizonDatas.get(index).getFundId();
                key = "";
                getData();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PostInvestmentManagementAdapter(this,datas);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.ll_item:
                        Intent intent = new Intent(PostInvestmentManagementForPartnerActivity.this,
                                PostInvestmentManagementDetailActivity.class);
                        intent.putExtra("projId", datas.get(position).getProjId());
                        intent.putExtra("projName", datas.get(position).getProjName());
                        startActivity(intent);
                        break;
                    case R.id.ll_add:
                        Intent intent6 = new Intent(PostInvestmentManagementForPartnerActivity.this, ActivityAddFollowUp.class);
                        Bundle bundle6 = new Bundle();
                        bundle6.putString("projId", datas.get(position).getProjId());
                        bundle6.putString("mTitle", "新增投后跟踪");
                        intent6.putExtras(bundle6);
                        startActivity(intent6);
                        break;
                }

            }
        });
        initRefresh();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getFundListData();
            }
        });

        getFundListData();

    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(PostInvestmentManagementForPartnerActivity.this))
                {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getFundListData();
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(PostInvestmentManagementForPartnerActivity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getFundListData()
    {
        ListHttpHelper.getPIMFPFundListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), new SDRequestCallBack
                (PIMFundListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PIMFundListBean bean = (PIMFundListBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                {
                    horizonDatas.clear();
                    horizonDatas.addAll(bean.getData().getData());
                    horizonDatas.get(index).setChoose(true);
                    horizonAdapter.notifyDataSetChanged();
                    fundId = bean.getData().getData().get(0).getFundId();
                    key = "";
                    getData();
                } else
                {
                    mTips.showNoContent("暂无数据");
                }
            }
        });
    }

    public void getData()
    {
        ListHttpHelper.getPIMFPDetailListData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), fundId, mPage + "", "10",key,
                new SDRequestCallBack(PostInvestmentListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PostInvestmentListBean bean = (PostInvestmentListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 1)
                {
                    adapter.getData().clear();
                    if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                adapter.getData().addAll(bean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void initPopupwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_research_report_search, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });

        final EditText etKeyWord = (EditText) contentView.findViewById(R.id.et_keyword);
        Button btnReset = (Button) contentView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeyWord.setText("");
            }
        });
        Button btnSearch = (Button) contentView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.notEmpty(etKeyWord.getText())) {
                    key = etKeyWord.getText().toString();
                    getData();
                }
                popupWindow.dismiss();
            }
        });

    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }
}
