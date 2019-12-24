package newProject.company.tools;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.StringUtils;
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
import newProject.company.vacation.WebVacationActivity;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/11/27.
 */

public class ToolsListActivity extends Activity
{

    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.all_recycler_layout)
    RecyclerView mRecyclerview;
    @Bind(R.id.all_fragment_container)
    FrameLayout allFragmentContainer;
    private ToolsAdapter mToolsAdapter;
    private int mPage = 1;
    private List<ToolsListBean.DataBean> mList = new ArrayList<>();
    private int mBackListSize = 0;
    private String mSearch = "";
    private int i_kind = 1;   //1=规章制度，2=行政办公，3=常用模板

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_list_activity_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        i_kind = intent.getIntExtra("i_kind", 1);

        initRefresh();
        getNetData(mPage + "", mSearch, true);
    }

    public void initRefresh()
    {
        if (i_kind == 1)
        {
            titleBar.setMidText("规章制度");
        } else if (i_kind == 2)
        {
            titleBar.setMidText("行政办公");
        }
        else if (i_kind ==3)
        {
            titleBar.setMidText("常用模板");
        }
        titleBar.setLeftImageOnClickListener(mOnClickListener);
        titleBar.setRightImageVisible(false);
        titleBar.setRightSecondImageVisible(false);
        titleBar.setRightTextVisible(false);
        titleBar.setRightSecondImageOnClickListener(mOnClickListener);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(ToolsListActivity.this))
                {
                    mPage = 1;
                    mList.clear();
                    mToolsAdapter.getData().clear();
                    getNetData(mPage + "", mSearch, true);
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
                if (DisplayUtil.hasNetwork(ToolsListActivity.this))
                {
                    if (mToolsAdapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getNetData(mPage + "", mSearch, false);
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
        setData();

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
                getNetData(mPage + "", mSearch, true);
            }
        });

        if (!DisplayUtil.hasNetwork(ToolsListActivity.this))
        {
            mLoadingView.showAccessFail();
        }

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == titleBar.getLeftImageView())
            {
                finish();
            } else if (v == titleBar.getRihtSecondImage())
            {
                final NewCommonDialog dialog = new NewCommonDialog(ToolsListActivity.this);
                dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener()
                {
                    @Override
                    public void onClick(String inputText, String select)
                    {

                    }

                    @Override
                    public void onSearchClick(String content)
                    {
                        mSearch = content;
                        refresh();
                    }
                });
                dialog.initDialogSearch(ToolsListActivity.this).show();
            }
        }
    };

    public void refresh()
    {
        if (mRecyclerview != null)
        {
            mRecyclerview.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    mPage = 1;
                    mList.clear();
                    mToolsAdapter.getData().clear();
                    getNetData(mPage + "", mSearch, true);
                }
            }, 500);
        }
    }

    private void getNetData(String page, String s_title, final boolean isRefresh)
    {
        ListHttpHelper.getToolsList(ToolsListActivity.this, page, s_title, i_kind, new SDRequestCallBack(ToolsListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showAccessFail();
                ToastUtils.show(ToolsListActivity.this, msg);

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                boolean hasData = false;
                String tips = "暂无内容";
                ToolsListBean listBean = (ToolsListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mList = listBean.getData();
                    hasData = true;
                    if (isRefresh)
                    {
                        mToolsAdapter.setNewData(mList);
                    } else
                    {
                        mToolsAdapter.addData(mList);
                        mToolsAdapter.notifyDataSetChanged();
                    }
                    mBackListSize = listBean.getTotal();
                } else
                {
                    if (mToolsAdapter.getData().size() > 0)
                    {
                        hasData = true;
                    }
                }
                if (hasData)
                {
                    mLoadingView.hide();
                } else
                {
                    mLoadingView.showNoContent(tips);
                }
            }
        });
    }

    private void setData()
    {
        mToolsAdapter = new ToolsAdapter(R.layout.tools_item_layout, mList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ToolsListActivity.this));
        mRecyclerview.setAdapter(mToolsAdapter);
        mToolsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                ToolsListBean.DataBean bean = (ToolsListBean.DataBean) adapter.getData().get(position);
                if (adapter.getData().size() > 0 && bean != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", getURL(bean.getEid()));
                    bundle.putString("TITLE", bean.getTitle());
                    Intent intent = new Intent(ToolsListActivity.this, WebVacationActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    public String getURL(int eid)
    {
        String token = SPUtils.get(this, SPUtils.ACCESS_TOKEN, "").toString();
        String url = Constants.getIp() + "/tool/pdf/view/" + eid + ".htm?token=" + token;
        return url;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class ToolsAdapter extends BaseQuickAdapter<ToolsListBean.DataBean, BaseViewHolder>
    {


        public ToolsAdapter(@LayoutRes int layoutResId, @Nullable List<ToolsListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, ToolsListBean.DataBean item)
        {
            if (item.getTitle() != null)
            {
                holder.setText(R.id.pdf_item_title, item.getTitle());
            } else
            {
                holder.setText(R.id.pdf_item_title, "");
            }
            if (item.getPublishTime() != null)
            {
                holder.setText(R.id.pdf_item_time, item.getPublishTime());
            } else
            {
                holder.setText(R.id.pdf_item_time, "");
            }
        }
    }


}
