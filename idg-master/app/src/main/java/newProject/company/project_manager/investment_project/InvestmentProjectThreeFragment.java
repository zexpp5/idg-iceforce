package newProject.company.project_manager.investment_project;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.ContractListBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.tv_title;

/**
 * Created by Administrator on 2017/11/27.
 * 基金投资
 */
public class InvestmentProjectThreeFragment extends Fragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectThreeAdapter mAdapter;
    private List<ContractListBean.DataBean.ContractsBean> mDataLists = new ArrayList<>();
    private String mEid = "";

    public static Fragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        InvestmentProjectThreeFragment fragment = new InvestmentProjectThreeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getIn();
        initRefresh();
        initData();
        getNetData();
    }

    private void getIn()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getInt("EID", 0) + "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_two, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerview != null)
                {
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        }

    }


    private void getNetData()
    {
        ListHttpHelper.getContractList(getActivity(), mEid, new SDRequestCallBack(ContractListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (error.getExceptionCode() == 400)
                {
                    mLoadingView.showNoContent(msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ContractListBean listBean = (ContractListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().getContracts().isEmpty())
                {
                    mLoadingView.hide();
                    setData(listBean);
                } else
                {
                    mLoadingView.showNoContent("暂未有基金投资数据");
                }
            }
        });
    }

    private void setData(ContractListBean listBean)
    {
        ContractListBean.DataBean data = listBean.getData();
        mAdapter.setNewData(listBean.getData().getContracts());

        View head = LayoutInflater.from(getContext()).inflate(R.layout.item_investment_three_head_layout, null);
        TextView item_one = (TextView) head.findViewById(R.id.item_one);
        TextView item_one2 = (TextView) head.findViewById(R.id.item_one2);
        TextView item_two = (TextView) head.findViewById(R.id.item_two);
        TextView item_two2 = (TextView) head.findViewById(R.id.item_two2);

        item_one.setText(null == data.getRmbCost() ? "" : data.getRmbCost());
        item_one2.setText(null == data.getRmbGrowth() ? "" : data.getRmbGrowth());
        item_two.setText(null == data.getUsdCost() ? "" : data.getUsdCost());
        item_two2.setText(null == data.getUsdGrowth() ? "" : data.getUsdGrowth());
        mAdapter.setHeaderView(head);
    }

    private void initData()
    {
        mAdapter = new InverstmentProjectThreeAdapter(R.layout.item_investment_project_three_layout, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.gray_shape, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class InverstmentProjectThreeAdapter extends BaseQuickAdapter<ContractListBean.DataBean.ContractsBean, BaseViewHolder>
    {


        public InverstmentProjectThreeAdapter(@LayoutRes int layoutResId, @Nullable List<ContractListBean.DataBean
                .ContractsBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ContractListBean.DataBean.ContractsBean item)
        {
            holder.setText(tv_title, null == item.getFund() ? "" : item.getFund())
                    .setText(R.id.item_two, null == item.getCurrency() ? "" : item.getCurrency())
                    .setText(R.id.item_three, null == item.getCost() ? "" : item.getCost())
                    .setText(R.id.item_four, null == item.getOwnership() ? "" : item.getOwnership())
                    .setText(R.id.item_five, null == item.getMultiply() ? "" : item.getMultiply());

        }
    }


}
