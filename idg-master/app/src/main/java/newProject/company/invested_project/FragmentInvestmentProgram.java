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
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
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
import newProject.company.invested_project.bean.BeanProgram;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;

import static com.injoy.idg.R.id.tv_fund;
import static com.injoy.idg.R.id.view;


/**
 * 投资方案
 */
public class FragmentInvestmentProgram extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private String mEid = "";
    private List<BeanProgram.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String userName = "";

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentInvestmentProgram fragment = new FragmentInvestmentProgram();
        fragment.setArguments(args);
        return fragment;
    }

    public void reFresh()
    {
        getNetData();
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_investment_program;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }
        userName = loginUserName;
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
        ListHttpHelper.getInvestementProgram(getActivity(), mEid, userName, new SDRequestCallBack(BeanProgram.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showNoContent("暂无数据");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanProgram listBean = (BeanProgram) responseInfo.getResult();
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
        mApplyAdapter = new ApplyAdapter(R.layout.item_investment_program, mDataLists);
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
                if (view.getId() == R.id.six_content)
                {
//                    final BeanProgram.DataBean bean = mApplyAdapter.getData().get(position);
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
//                            commitApproval(bean.getPlanId() + "");
//                        }
//                    });
//                    dialog.initDialogTips(getActivity(), "", "请确定本条信息的准确性！").show();
                }
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

    private class ApplyAdapter extends BaseQuickAdapter<BeanProgram.DataBeanX.DataBean, BaseViewHolder>
    {
        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<BeanProgram.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanProgram.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            if (StringUtils.notEmpty(item.getCreateDate()))
            {
                holder.setText(R.id.tv_time, DateUtils.getDate("yyyy-MM-dd", item.getPlanDate()));
            } else
            {
                holder.setText(R.id.tv_time, "");
            }

            if (StringUtils.notEmpty(item.getPlanDesc()))
            {
                holder.setText(R.id.tv_content, item.getPlanDesc());
            } else
            {
//                holder.setText(R.id.tv_content, " - -");
            }

            if (StringUtils.notEmpty(item.getPipelineAmtStr()))
            {
                holder.setText(R.id.tv_money, item.getPipelineAmtStr());
            } else
            {
                holder.setText(R.id.tv_money, "");
            }

            if (StringUtils.notEmpty(item.getPlanType()))
            {
                holder.setText(R.id.tv_program_type, item.getPlanTypeStr());
            } else
            {
//                holder.setText(R.id.tv_program_type, " - -");
            }

            if (StringUtils.notEmpty(item.getIsIcApprovedStr()))
            {
                holder.setText(R.id.tv_pass, item.getIsIcApprovedStr());
            } else
            {
//                holder.setText(R.id.tv_pass, " - -");
            }

            if (StringUtils.notEmpty(item.getFund()))
            {
                holder.setText(R.id.tv_fund, item.getFund());
            } else
            {
//                holder.setText(R.id.tv_pass, " - -");
            }
        }
    }


}
