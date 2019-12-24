package newProject.company.project_manager.bad_assets;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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
import newProject.company.project_manager.estate_project.ProjectEstateListActivity;
import newProject.company.project_manager.estate_project.bean.EstateList;
import newProject.company.project_manager.estate_project.detail.ProjectEstateDetailActivity;
import newProject.company.project_manager.investment_project.ProjectManagerListActivity;
import newProject.company.project_manager.investment_project.SearchListActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BadAssetsListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener
{
    private static final int REQUEST_SEARCH = 1001;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    //    @Bind(R.id.search_et)
//    EditText etSearch;
    private int mPage = 1;
    private int mBackListSize = 0;
    BadAssetsAdapter projectEstateAdapter;
    String search;
    String title;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_bad_assets_list;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(mOnClickListener);
        mNavigatorBar.setLeftTextOnClickListener(mOnClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageOnClickListener(mOnClickListener);
        mNavigatorBar.setRightSecondImageVisible(true);
        title = getIntent().getExtras().getString("TITLE", "不良资产管理");
        mNavigatorBar.setMidText(title);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        projectEstateAdapter = new BadAssetsAdapter(new ArrayList<BadAssetsListBean.DataBean>());
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

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mNavigatorBar.getLeftImageView() || v == mNavigatorBar.getLeftText())
            {
                finish();
            } else if (v == mNavigatorBar.getRihtSecondImage())
            {
                Intent intent = new Intent(BadAssetsListActivity.this, SearchListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("TYPE", 2);
                bundle.putString("TITLE", title);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_SEARCH);
            }
        }
    };

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(BadAssetsListActivity.this))
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
                if (DisplayUtil.hasNetwork(BadAssetsListActivity.this))
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

        ListHttpHelper.getBadAssetsList(this, search, mPage + "", new SDRequestCallBack(BadAssetsListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                // ToastUtils.show(getActivity(), msg);
                if (mPage == 1)
                {
                    mTips.showAccessFail();
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BadAssetsListBean result = (BadAssetsListBean) responseInfo.getResult();
                List<BadAssetsListBean.DataBean> data = result.getData();

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
     * 不良项目详情
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
    {
        Intent intent = new Intent(this, BadAssetsDetailActivity.class);
        int projId = projectEstateAdapter.getData().get(position).getProjId();
        intent.putExtra("projId", projId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_SEARCH:
                if (resultCode == RESULT_OK && data != null)
                {
                    search = data.getStringExtra("SEARCH_CONTENT");
                    if (TextUtils.isEmpty(search))
                    {
                        search = "";
                        getData();
                    }
                    mPage = 1;
                    getData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
