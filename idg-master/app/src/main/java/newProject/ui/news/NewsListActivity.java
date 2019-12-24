package newProject.ui.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ui.http.BasicDataHttpHelper;
import com.utils.SDToast;
import com.utils.slide.RecycleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.expense.ExpenseApprovalListActivity;
import newProject.company.vacation.VacationDetailActivity;
import newProject.ui.news.detail.NewsDetailAcitity;
import newProject.ui.work.SuperGMeetingActivity;
import newProject.ui.work.list.Content;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static com.injoy.idg.R.string.delete;
import static com.injoy.idg.R.string.failed;

/**
 * Created by selson on 2017/12/28.
 * 消息 -请假申请的结果
 */
public class NewsListActivity extends BaseActivity
{
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;

    private String mSearch = "";
    private int mPage = 1;
    private int pageCount = 1;

    private List<NewsListBean.Data> mDatas = new ArrayList<>();

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        initTitle();
        setAdapter();
        refresh();
    }

    private void refresh()
    {
        mSearch = "";
        mPage = 1;
        getData(true);
    }


    private void initTitle()
    {
        titleBar.setMidText(getResources().getString(R.string.im_business_news));
        titleBar.setLeftImageVisible(true);
//        titleBar.setRightSecondImageVisible(true);
        titleBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });

        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getData(true);
            }
        });

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(NewsListActivity.this))
                {
                    refresh();
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
                if (DisplayUtil.hasNetwork(NewsListActivity.this))
                {
                    if (mPage >= pageCount)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getData(false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });

        if (!DisplayUtil.hasNetwork(NewsListActivity.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getData(final boolean isRefresh)
    {
        BasicDataHttpHelper.postNewsList(NewsListActivity.this, "", mSearch, mPage, new SDRequestCallBack(NewsListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showAccessFail();
                MyToast.showToast(NewsListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                NewsListBean newsListBean = (NewsListBean) responseInfo.getResult();
                if (newsListBean != null && newsListBean.getData().size() > 0)
                {
                    mLoadingView.hide();
                    if (isRefresh)
                    {
                        mDatas.clear();
                        mDatas.addAll(newsListBean.getData());
                    } else
                    {
                        mDatas.addAll(newsListBean.getData());
                    }

                } else
                {
                    mLoadingView.showNoContent("暂无数据");
                }

                pageCount = newsListBean.getPageCount();

                if (recycleAdapter != null)
                    recycleAdapter.updateData(mDatas);
            }
        });
    }

    RecycleAdapter recycleAdapter;

    //填充详情
    private void setAdapter()
    {
        recycleAdapter = new RecycleAdapter(NewsListActivity.this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(NewsListActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration2(this,
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, 0));
        recyclerView.setAdapter(recycleAdapter);
        recycleAdapter.setOnSlidingViewClickListener(new RecycleAdapter.IonSlidingViewClickListener()
        {
            @Override
            public void onItemClick(View view, int position)
            {
                Content content = SDGson.toObject(mDatas.get(position).getContent(), Content.class);

                if (StringUtils.notEmpty(content.getApproveId()) && content.getApproveId() != 0)
                {
                    Intent intent = new Intent(NewsListActivity.this, NewsDetailAcitity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("eid", content.getApproveId() + "");
                    bundle.putString("createTime", DateUtils.getData(mDatas.get(position).getCreateTime()));
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                {
                    if (StringUtils.notEmpty(content.getApplyId()))
                    {
                        Bundle bundle = new Bundle();
                        bundle.putInt("EID", Integer.parseInt(content.getApplyId()));
                        bundle.putInt("SIGN", 2);
                        bundle.putBoolean("isShowBtn", false);
                        Intent intent = new Intent(NewsListActivity.this, VacationDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onDeleteBtnClick(View view, int position)
            {
                delete(mDatas.get(position).getEid() + "", position);
            }
        });
    }

    //滑动删除
    private void delete(String eid, final int position)
    {
        BasicDataHttpHelper.postDelNews(NewsListActivity.this, eid, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(NewsListActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(NewsListActivity.this, "删除成功");
                recycleAdapter.removeItem(position);
            }
        });
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_work_news_main;
    }

}
