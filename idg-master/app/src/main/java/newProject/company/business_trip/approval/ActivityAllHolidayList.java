package newProject.company.business_trip.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
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
import newProject.api.ListHttpHelper;
import newProject.company.business_trip.bean.BeanAllHoliday;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

/**
 * 休假汇总
 */
public class ActivityAllHolidayList extends BaseActivity
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    private InverstmentProjectFileAdapter mAdapter;
    private List<BeanAllHoliday.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid;

    private int pageNo = 0;
    private int pageSize = 10;

    private String userName = "";

    private void getIn()
    {
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null)
//        {
//            mEid = bundle.getString(Constants.PROJECT_EID, "");
//        }

        userName = loginUserAccount;

        titleBar.setMidText("休假汇总");
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_all_holiday_list;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
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
                pageNo = 0;
                if (DisplayUtil.hasNetwork(ActivityAllHolidayList.this))
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
                if (DisplayUtil.hasNetwork(ActivityAllHolidayList.this))
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

        if (!DisplayUtil.hasNetwork(ActivityAllHolidayList.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postAllHolidayList(ActivityAllHolidayList.this, pageNo + "", pageSize + "", userName,
                new SDRequestCallBack(BeanAllHoliday.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanAllHoliday listBean = (BeanAllHoliday) responseInfo.getResult();
                        if (pageNo == 0)
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
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_all_holiday_list, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityAllHolidayList.this,
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityAllHolidayList.this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (mDataLists.get(position).getHolidayType().intValue() == 11)
                {
                    Intent intent = new Intent(ActivityAllHolidayList.this, ActivityPersonalHolidayList.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.COMMON_INFO, mDataLists.get(position).getUserName());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()

        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
//                if (view.getId() == )
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

    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<BeanAllHoliday.DataBeanX.DataBean, BaseViewHolder>
    {

        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<BeanAllHoliday.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanAllHoliday.DataBeanX.DataBean item)
        {
            if (StringUtils.notEmpty(item.getName()))
                holder.setText(R.id.tv_title, item.getName() + "的休假");
            if (StringUtils.notEmpty(item.getHolidayTypeStr()))
                holder.setText(R.id.item_one, item.getHolidayTypeStr());
            if (StringUtils.notEmpty(item.getHolidayDays()))
                holder.setText(R.id.item_two, item.getHolidayDays() + "");
            if (StringUtils.notEmpty(item.getDateRange()))
                holder.setText(R.id.item_three, item.getDateRange());

            if (StringUtils.notEmpty(item.getHolidayType()))
            {
                if (item.getHolidayType().intValue() == 11)
                {
                    holder.getView(R.id.img_right).setVisibility(View.VISIBLE);
                } else
                {
                    holder.getView(R.id.img_right).setVisibility(View.GONE);
                }
            } else
            {
                holder.getView(R.id.img_right).setVisibility(View.GONE);
            }
        }
    }
}
