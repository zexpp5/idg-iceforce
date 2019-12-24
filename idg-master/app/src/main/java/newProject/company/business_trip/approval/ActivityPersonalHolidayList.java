package newProject.company.business_trip.approval;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
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
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.business_trip.bean.BeanAllHoliday;
import newProject.company.business_trip.bean.BeanPersonalHoliday;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static com.baidu.location.d.g.D;

/**
 * 个人年假列表
 */
public class ActivityPersonalHolidayList extends BaseActivity
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
    private List<BeanPersonalHoliday.DataBean> mDataLists = new ArrayList<>();

    private int pageNo = 0;
    private int pageSize = 10;

    private String userName = "";

    private void getIn()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            userName = bundle.getString(Constants.COMMON_INFO, "");
        }

        titleBar.setMidText("休假详情");
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
        return R.layout.activity_common_list_view;
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
                if (DisplayUtil.hasNetwork(ActivityPersonalHolidayList.this))
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
                if (DisplayUtil.hasNetwork(ActivityPersonalHolidayList.this))
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

        if (!DisplayUtil.hasNetwork(ActivityPersonalHolidayList.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        String yearString = DateUtils.getNowDate(Calendar.YEAR) + "/01/01";
        ListHttpHelper.getPersonalHolidayList(ActivityPersonalHolidayList.this, userName, pageNo + "", pageSize + "", "11",
                "2", yearString, new SDRequestCallBack(BeanPersonalHoliday.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanPersonalHoliday listBean = (BeanPersonalHoliday) responseInfo.getResult();
                        if (pageNo == 0)
                        {
                            if (StringUtils.notEmpty(listBean.getData()) && listBean.getData().size() > 0)
                            {
                                mDataLists.clear();
                                mDataLists.addAll(listBean.getData());
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
                            if (StringUtils.notEmpty(listBean.getData()) || listBean.getData().size() > 0)
                                mDataLists.addAll(listBean.getData());
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
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_personal_holiday_list, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityPersonalHolidayList.this,
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityPersonalHolidayList.this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

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

    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<BeanPersonalHoliday.DataBean, BaseViewHolder>
    {

        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<BeanPersonalHoliday.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanPersonalHoliday.DataBean item)
        {
            if (StringUtils.notEmpty(item.getUserName()))
                holder.setText(R.id.tv_title, item.getUserName() + "的休假");
            if (StringUtils.notEmpty(item.getLeaveType()))
                holder.setText(R.id.item_one, item.getLeaveType());
            if (StringUtils.notEmpty(item.getLeaveDay()))
                holder.setText(R.id.item_two, item.getLeaveDay() + "");

            String timeString = "";
            if (StringUtils.notEmpty(item.getLeaveStart()))
            {
                timeString = item.getLeaveStart();
            } else
            {
                timeString = "0";
            }
            if (StringUtils.notEmpty(item.getLeaveEnd()))
            {
                timeString = timeString + " - " + item.getLeaveEnd();
            } else
            {
                timeString = timeString + " - 0";
            }
            holder.setText(R.id.item_three, timeString);

        }
    }
}
