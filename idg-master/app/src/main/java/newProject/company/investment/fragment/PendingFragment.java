package newProject.company.investment.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.investment.adapter.HistoryAdapter;
import newProject.company.investment.adapter.PendingAdapter;
import newProject.company.investment.bean.HistoryListBean;
import newProject.company.investment.bean.PendingListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 */

public class PendingFragment extends Fragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 0;
    private int mBackListSize = 0;
    PendingAdapter adapter;

    List<PendingListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String flag;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_need_to_do, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    public static int REFRESHCODE = 1055;

    private void initViews(){
        flag = getArguments().getString("flag");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PendingAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                PendingListBean.DataBeanX.DataBean bean = (PendingListBean.DataBeanX.DataBean) adapter.getData().get(position);
//                ExpenseApprovalBean.DataBean bean = (ExpenseApprovalBean.DataBean) adapter.getData().get(position);
                if (adapter.getData().size() > 0 && bean != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", bean.getBusId());
                    bundle.putString("BID", bean.getMsgId());
                    bundle.putBoolean("YES", false);
                    Intent intent = new Intent();
                    if (bean.getBusType().equals("Reimbursement") || bean.getBusType().equals("EmployeeVoucher") || bean
                            .getBusType().equals("GeneralVoucher") || bean.getBusType().equals("SpecialVoucher") || bean
                            .getBusType().equals("AssetsVoucher"))
                    {
                        //报销单
                        bundle.putString("expenseType", bean.getBusType());
                        intent.setClass(getActivity(), ExpenseBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getBusType().equals("Payment") || bean.getBusType().equals("PaymentVoucher"))
                    {
                        //付款单
                        bundle.putString("expenseType", bean.getBusType());
                        intent.setClass(getActivity(), PayBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getBusType().equals("Reim2Pay"))
                    {
                        //报销付款单
                        bundle.putString("expenseType", bean.getBusType());
                        intent.setClass(getActivity(), ExpenseAndPayActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getBusType().equals("Loan") || bean.getBusType().equals("EmployeeLoan"))
                    {
                        //借款单
                        bundle.putString("expenseType", bean.getBusType());
                        intent.setClass(getActivity(), LoanBillActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    } else if (bean.getBusType().equals("DDFee"))
                    {
                        //尽调费用单
                        bundle.putString("expenseType", bean.getBusType());
                        intent.setClass(getActivity(), DDFeeActivity.class);
                        intent.putExtras(bundle);
                        startActivityForResult(intent, REFRESHCODE);
                    }
                }
            }
        });

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){

                }
                
            }
        });

        initRefresh();
        mTips.showLoading();
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
        adapter.notifyDataSetChanged();
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

    @Override
    public void onStart() {
        super.onStart();
        mPage = 0;
        getData();
    }

    private void getData(){
        ListHttpHelper.getApproveHolidayListData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), mPage+"", "10",flag, new SDRequestCallBack(PendingListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PendingListBean bean = (PendingListBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())) {
                    mBackListSize = bean.getData().getTotal();
                    if (mPage == 0) {
                        adapter.getData().clear();
                        if (bean.getData().getData().size() > 0) {
                            mTips.hide();
                        } else {
                            mTips.showNoContent("暂无数据");
                        }
                    }
                    adapter.getData().addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                }else {
                    mTips.showNoContent(bean.getData().getReturnMessage());
                }
            }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
