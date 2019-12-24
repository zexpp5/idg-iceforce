package newProject.company.investment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseFragment;
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
import newProject.company.expense.DDFeeActivity;
import newProject.company.expense.ExpenseAndPayActivity;
import newProject.company.expense.ExpenseApprovalListActivity;
import newProject.company.expense.ExpenseBillActivity;
import newProject.company.expense.LoanBillActivity;
import newProject.company.expense.PayBillActivity;
import newProject.company.expense.bean.ExpenseApprovalBean;
import newProject.company.investment.ApprovalBusinessTripDetailActivity;
import newProject.company.investment.ApproveDetailActivity;
import newProject.company.investment.ApproveHistoryActivity;
import newProject.company.investment.adapter.ApproveAdapter;
import newProject.company.investment.bean.ApproveListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by zsz on 2019/8/22.
 */

public class ApproveFragment extends BaseFragment
{
    @Bind(R.id.tv_history)
    TextView tvHistory;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    ApproveAdapter adapter;

    List<ApproveListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    public static int REFRESHCODE = 1055;

    private int mPage = 0;
    private int mBackListSize = 0;

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_approve;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        tvHistory.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), ApproveHistoryActivity.class);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ApproveAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                ApproveListBean.DataBeanX.DataBean bean = datas.get(position);
                if (bean.getOaItemType().equals("holiday"))
                {
                    Intent intent = new Intent(getActivity(), ApproveDetailActivity.class);
                    intent.putExtra("id", bean.getId());
                    startActivityForResult(intent, REFRESHCODE);
                }
                if (bean.getOaItemType().equals("trip"))
                {
                    Intent intent = new Intent(getActivity(), ApprovalBusinessTripDetailActivity.class);
                    intent.putExtra("id", bean.getId());
                    startActivityForResult(intent, REFRESHCODE);
                }
                if (bean.getOaItemType().equals("Reimbursement") || bean.getOaItemType().equals("EmployeeVoucher") || bean
                        .getOaItemType().equals("GeneralVoucher") || bean.getOaItemType().equals("SpecialVoucher") || bean
                        .getOaItemType().equals("AssetsVoucher"))
                {
                    //报销单
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    bundle.putString("expenseType", bean.getOaItemType());
                    intent.setClass(getActivity(), ExpenseBillActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REFRESHCODE);
                } else if (bean.getOaItemType().equals("Payment") || bean.getOaItemType().equals("PaymentVoucher"))
                {
                    //付款单
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    bundle.putString("expenseType", bean.getOaItemType());
                    intent.setClass(getActivity(), PayBillActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REFRESHCODE);
                } else if (bean.getOaItemType().equals("Reim2Pay"))
                {
                    //报销付款单
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    bundle.putString("expenseType", bean.getOaItemType());
                    intent.setClass(getActivity(), ExpenseAndPayActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REFRESHCODE);
                } else if (bean.getOaItemType().equals("Loan") || bean.getOaItemType().equals("EmployeeLoan"))
                {
                    //借款单
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    bundle.putString("expenseType", bean.getOaItemType());
                    intent.setClass(getActivity(), LoanBillActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REFRESHCODE);
                } else if (bean.getOaItemType().equals("DDFee"))
                {
                    //尽调费用单
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getId());
                    bundle.putBoolean("YES", true);
                    Intent intent = new Intent();
                    bundle.putString("expenseType", bean.getOaItemType());
                    intent.setClass(getActivity(), DDFeeActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REFRESHCODE);
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
                getData();
            }
        });

        getData();

    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    mPage = 0;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
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
                if (DisplayUtil.hasNetwork(getActivity()))
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

    public void getData()
    {
        ListHttpHelper.getApproveListData(getActivity(), SPUtils.get(getActivity(), SPUtils.USER_ACCOUNT, "").toString(), mPage
                + "", 10 + "", new SDRequestCallBack(ApproveListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ApproveListBean bean = (ApproveListBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 0)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESHCODE)
        {
            if (resultCode == RESULT_OK)
            {
                mPage = 0;
                getData();
            }
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
