package newProject.company.invested_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanQxbSearch;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

/**
 * 启信宝
 */
public class ActivityQxbSearch extends BaseActivity
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;

    @Bind(R.id.tv_search)
    FontEditext tvSearch;

    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;

    private InverstmentProjectFileAdapter mAdapter;
    private List<BeanQxbSearch.DataBeanX.DataBean> mDataLists = new ArrayList<>();

    private int pageNo = 1;
    private int pageSize = 10;

    private String companyName = "";

    private void getIn()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            companyName = bundle.getString("companyName");
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_qxb_search_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        titleBar.setMidText("启信宝查询结果");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        tvSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (StringUtils.notEmpty(s.toString()))
                {
                    companyName = s.toString();
                    pageNo = 1;
                    getNetData();
                }
            }
        });

        getIn();
        initRefresh();
        initAdapter();
        getNetData();
    }

    public void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                pageNo = 1;
                if (DisplayUtil.hasNetwork(ActivityQxbSearch.this))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
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
                if (DisplayUtil.hasNetwork(ActivityQxbSearch.this))
                {
//                    if (projectLibAdapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {
                    pageNo = pageNo + 1;
                    getNetData();
                    mRefreshLayout.finishLoadmore(1000);
//                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
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

        if (!DisplayUtil.hasNetwork(ActivityQxbSearch.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postQxbSearch(ActivityQxbSearch.this, companyName, pageNo + "", pageSize + "",
                new SDRequestCallBack(BeanQxbSearch.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanQxbSearch listBean = (BeanQxbSearch) responseInfo.getResult();
                        if (pageNo == 1)
                        {
                            if (StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData().getData().size() > 0)
                            {
                                mDataLists.clear();
                                mDataLists.addAll(listBean.getData().getData());
                                mLoadingView.hide();
                                setData();
                            } else
                            {
                                mDataLists.clear();
                                mLoadingView.showNoContent("暂无数据");
                                setData();
                            }
                        } else
                        {
                            if (StringUtils.notEmpty(listBean.getData().getData()) || listBean.getData().getData().size() > 0)
                                mDataLists.addAll(listBean.getData().getData());
                            setData();
                        }
                    }
                });
    }

    private void setData()
    {
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter()
    {
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_qxb_search, mDataLists);
//        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityFileTypeChoose.this,
//                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider_white, ScreenUtils.dp2px
// (ActivityFileTypeChoose
//                .this, 10));
//        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityQxbSearch.this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BeanQxbSearch.DataBeanX.DataBean dataBean = mDataLists.get(position);
                Intent intent = new Intent(ActivityQxbSearch.this, ActivityQxb.class);
                intent.putExtra("companyName", dataBean.getName());
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
//                if (view.getId() == R.id.)
//                {
//                }
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rl_search)
    public void onViewClicked()
    {

    }

    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<BeanQxbSearch.DataBeanX.DataBean,
            BaseViewHolder>
    {
        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<BeanQxbSearch.DataBeanX
                .DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanQxbSearch.DataBeanX.DataBean item)
        {
            if (StringUtils.notEmpty(item.getName()))
                holder.setText(R.id.tv_title, item.getName());
            holder.setText(R.id.tv_content, "启信宝");
        }
    }

}
