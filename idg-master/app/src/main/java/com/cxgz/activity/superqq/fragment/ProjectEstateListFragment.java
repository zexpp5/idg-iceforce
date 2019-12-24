package com.cxgz.activity.superqq.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cx.base.BaseActivity;
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
import newProject.company.project_manager.estate_project.ProjectEstateAdapter;
import newProject.company.project_manager.estate_project.bean.EstateList;
import newProject.company.project_manager.estate_project.detail.ProjectEstateDetailActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

//项目库
public class ProjectEstateListFragment extends BaseActivity implements BaseQuickAdapter.OnItemClickListener
{
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private int mPage = 1;
    private int mBackListSize = 0;
    ProjectEstateAdapter projectEstateAdapter;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_project_estate_list2;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        };
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        String title = getIntent().getExtras().getString("TITLE", "地产项目");
        mNavigatorBar.setMidText(title);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        projectEstateAdapter = new ProjectEstateAdapter(new ArrayList<EstateList.DataBean>());
        recycler_view.setAdapter(projectEstateAdapter);
        initRefresh();


        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recycler_view != null)
                {
                    recycler_view.setVisibility(visible ? GONE : VISIBLE);
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
        projectEstateAdapter.setOnItemClickListener(this);
    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ProjectEstateListFragment.this))
                {
                    /*s_projName="";
                    s_induIds="";
                    s_stageIds ="";*/
                    mPage = 1;
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
                if (DisplayUtil.hasNetwork(ProjectEstateListFragment.this))
                {
                    if (projectEstateAdapter.getData().size() >= mBackListSize)
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


    /**
     * 获取网络数据
     */
    public void getData()
    {

        ListHttpHelper.getProjectEstateList(this, mPage + "", new SDRequestCallBack(EstateList.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                // ToastUtils.show(getActivity(), msg);
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                EstateList result = (EstateList) responseInfo.getResult();
                List<EstateList.DataBean> data = result.getData();

                if (mPage == 1)
                {
                    projectEstateAdapter.getData().clear();
                    mBackListSize = result.getTotal();
                    if (data.size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                projectEstateAdapter.getData().addAll(data);
                projectEstateAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 地产详情
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
    {
        Intent intent = new Intent(this, ProjectEstateDetailActivity.class);
        int projId = projectEstateAdapter.getData().get(position).getProjId();
        intent.putExtra("projId", projId);
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


}
