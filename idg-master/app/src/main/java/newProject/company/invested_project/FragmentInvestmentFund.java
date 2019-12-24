package newProject.company.invested_project;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.NumberUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
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
import newProject.company.invested_project.bean.BeanFund;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;


/**
 * 基金投资
 */
public class FragmentInvestmentFund extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private String mEid = "";
    private List<BeanFund.DataBeanX.DataBean> mDataLists = new ArrayList<>();

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentInvestmentFund fragment = new FragmentInvestmentFund();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }
        initRefresh();
        initRV();

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        } else
        {
            getNetData();
        }

    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_investment_fund;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
    }

    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setReboundInterpolator(new DecelerateInterpolator());
        mRefreshLayout.setDragRate(0.25f);
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
                    mRecyclerview.setVisibility(visible ? View.GONE : View.VISIBLE);
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
    }

    private void getNetData()
    {
        ListHttpHelper.postInvestementFund(getActivity(), mEid, loginUserName, new SDRequestCallBack(BeanFund.class)
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
                BeanFund listBean = (BeanFund) responseInfo.getResult();
                mDataLists.clear();
                if (null != listBean.getData() && StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData()
                        .getData().size() > 0)
                {
                    mDataLists.addAll(listBean.getData().getData());
                    mLoadingView.hide();
                    setData();
                } else
                {
                    mLoadingView.showNoContent("暂无数据");
                    setData();
                }
            }
        });
    }

    private void setData()
    {
        mApplyAdapter.notifyDataSetChanged();
    }

    private void initRV()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.item_investment_fund, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mApplyAdapter);

        mApplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
//                if (view.getId() == R.id.six_content)
//                {
//                    final BeanFund.DataBeanX.DataBean bean = mApplyAdapter.getData().get(position);
//                    final NewCommonDialog dialog = new NewCommonDialog(getActivity());
//                    dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
//                    {
//                        @Override
//                        public void onClick(String inputText, String select)
//                        {
//
//                        }
//
//                        @Override
//                        public void onSearchClick(String content)
//                        {
//                            commitApproval(bean.getFundId() + "");
//                        }
//                    });
//                    dialog.initDialogTips(getActivity(), "", "请确定本条信息的准确性！").show();
//                }
            }
        });
    }

    public void commitApproval(String eid)
    {
        ListHttpHelper.approvalInvest(getActivity(), eid, new SDRequestCallBack()
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
                if (responseInfo.getMsg() != null)
                {
                    ToastUtils.show(getActivity(), responseInfo.getMsg());
                } else
                {
                    ToastUtils.show(getActivity(), "审批成功");
                }

                getNetData();
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class ApplyAdapter extends BaseQuickAdapter<BeanFund.DataBeanX.DataBean, BaseViewHolder>
    {

        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<BeanFund.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanFund.DataBeanX.DataBean item)
        {   
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            if (StringUtils.notEmpty(item.getFundName()))
            {
                holder.setText(R.id.tv_title, item.getFundName());
            }
            //投资金额
            if (StringUtils.notEmpty(item.getInvTotalStr()))
            {
                holder.setText(R.id.tv_fund1, item.getInvTotalStr());
            } else
            {
                holder.getView(R.id.ll_fund1).setVisibility(View.GONE);
            }
            //所占股比
            if (StringUtils.notEmpty(item.getOwnership()))
            {
                holder.setText(R.id.tv_fund3, item.getOwnership() + "");
            } else
            {
                holder.getView(R.id.ll_fund3).setVisibility(View.GONE);
            }
//            //所占估值
            if (StringUtils.notEmpty(item.getValuationOfFundStr()))
            {
                holder.setText(R.id.tv_fund4, item.getValuationOfFundStr());
            } else
            {
                holder.getView(R.id.tv_fund4).setVisibility(View.GONE);
            }
        }
    }


}
