package newProject.company.project_manager.estate_project.detail;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.estate_project.bean.InforDetailBean;
import newProject.company.project_manager.investment_project.InvestmentProjectFourFragment;
import newProject.company.project_manager.investment_project.bean.MoreListBean;
import newProject.company.project_manager.investment_project.bean.MoreListBean2;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.loading_view;

/**
 * author: Created by aniu on 2018/6/15.
 */

public class InformationDetailFragment extends Fragment
{

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectFourAdapter mAdapter;
    private List<MoreListBean2> mDataLists = new ArrayList<>();

    private String mEid = "";
    String[] mItemTitles = {"建筑面积相关", "土地指标", "证照及法律手续", "市场描述"
            , "风险提示", "交易对手及合作伙伴", "时间节点", "交易思路", "退出总结"};

    public static Fragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        InformationDetailFragment fragment = new InformationDetailFragment();
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

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        } else
        {
            getNetData();
        }
    }

    private void setIsShow(boolean isShow, String msg)
    {
        if (isShow)
        {
            mLoadingView.hide();
        } else
        {
            mLoadingView.showNoContent(msg);
        }
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
        View view = inflater.inflate(R.layout.fragment_information_detail, container, false);
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
    }

    private void getNetData()
    {
        ListHttpHelper.getEstateProjectDetailList(getActivity(), mEid, new SDRequestCallBack(InforDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                // ToastUtils.show(getActivity(), msg);
                if (error.getExceptionCode() == 400)
                {
                    mLoadingView.showNoContent(msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InforDetailBean listBean = (InforDetailBean) responseInfo.getResult();
                if (null != listBean && StringUtils.notEmpty(listBean.getData()))
                {
                    mLoadingView.hide();
                    setData(listBean);
                } else
                {
                    //没有数据
                    setIsShow(true, "暂未有详细信息");
                }
            }
        });
    }

    private void setData(InforDetailBean listBean)
    {
        if (null == listBean.getData())
            return;
        /**
         * 接口返回数据也是坑逼
         */
        List<MoreListBean2> list = new ArrayList<>();
        list.clear();
        MoreListBean2 item1 = new MoreListBean2();
        item1.setTitle(mItemTitles[0]);
        item1.setContent(null == listBean.getData().getFloorSpace() ? "" : listBean.getData().getFloorSpace());
        MoreListBean2 item2 = new MoreListBean2();
        item2.setTitle(mItemTitles[1]);
        item2.setContent(null == listBean.getData().getLandIndicator() ? "" : listBean.getData().getLandIndicator());
        MoreListBean2 item3 = new MoreListBean2();
        item3.setTitle(mItemTitles[2]);
        item3.setContent(null == listBean.getData().getLegalFormalities() ? "" : listBean.getData().getLegalFormalities());
        MoreListBean2 item4 = new MoreListBean2();
        item4.setTitle(mItemTitles[3]);
        item4.setContent(null == listBean.getData().getMarketDesc() ? "" : listBean.getData().getMarketDesc());
        MoreListBean2 item5 = new MoreListBean2();
        item5.setTitle(mItemTitles[4]);
        item5.setContent(null == listBean.getData().getRiskDesc() ? "" : listBean.getData().getRiskDesc());
        MoreListBean2 item6 = new MoreListBean2();
        item6.setTitle(mItemTitles[5]);
        item6.setContent(null == listBean.getData().getTeamDesc() ? "" : listBean.getData().getTeamDesc());
        MoreListBean2 item7 = new MoreListBean2();
        item7.setTitle(mItemTitles[6]);
        item7.setContent(null == listBean.getData().getTimeNode() ? "" : listBean.getData().getTimeNode());
        MoreListBean2 item8 = new MoreListBean2();
        item8.setTitle(mItemTitles[7]);
        item8.setContent(null == listBean.getData().getTradeThinking() ? "" : listBean.getData().getTradeThinking());
        MoreListBean2 item9 = new MoreListBean2();
        item9.setTitle(mItemTitles[8]);
        item9.setContent(null == listBean.getData().getWithdrawSummary() ? "" : listBean.getData().getWithdrawSummary());

        item1.setShow(true);
        item2.setShow(true);
        item3.setShow(true);
        item4.setShow(true);
        item5.setShow(true);
        item6.setShow(true);
        item7.setShow(true);
        item8.setShow(true);
        item9.setShow(true);

        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);
        list.add(item5);
        list.add(item6);
        list.add(item7);
        list.add(item8);
        list.add(item9);


        Iterator<MoreListBean2> iterator = list.iterator();
        while (iterator.hasNext())
        {
            MoreListBean2 next = iterator.next();
            if (TextUtils.isEmpty(next.getContent()))
            {
                iterator.remove();
            }
        }
        mAdapter.setNewData(list);
    }

    private void initData()
    {
        mAdapter = new InverstmentProjectFourAdapter(R.layout.item_investment_estate_project_four_layout, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.gray_shape, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position)
            {
                TextView contentView = (TextView) mAdapter.getViewByPosition(mRecyclerview, position, R.id.tv_content);
                if (contentView.getVisibility() == VISIBLE)
                {
                    mAdapter.getData().get(position).setShow(false);
                } else
                {
                    mAdapter.getData().get(position).setShow(true);
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class InverstmentProjectFourAdapter extends BaseQuickAdapter<MoreListBean2, BaseViewHolder>
    {


        public InverstmentProjectFourAdapter(@LayoutRes int layoutResId, @Nullable List<MoreListBean2> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MoreListBean2 item)
        {
            holder.setText(R.id.tv_title, item.getTitle())
                    .setText(R.id.tv_content, item.getContent())
                    .addOnClickListener(R.id.iv);

            if (null == item.getContent() || "".equals(item.getContent()))
            {
                holder.setVisible(R.id.iv, false);
            } else
            {
                holder.setVisible(R.id.iv, true);
            }

            if (item.isShow() && null != item.getContent() && !item.getContent().isEmpty())
            {
                holder.setImageResource(R.id.iv, R.mipmap.ic_expand3x)
                        .setVisible(R.id.tv_content, true);
            } else
            {
                holder.setImageResource(R.id.iv, R.mipmap.ic_collapse3x)
                        .setVisible(R.id.tv_content, false);
            }


        }
    }

}
