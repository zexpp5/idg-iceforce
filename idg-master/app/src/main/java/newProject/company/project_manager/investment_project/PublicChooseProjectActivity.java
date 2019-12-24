package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.PublicChooseProjectAdapter;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/6/19.
 */

public class PublicChooseProjectActivity extends BaseActivity {
    @Bind(R.id.recyclerview)
    RecyclerView recyclerView;
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

    private int pageNo = 1;
    private int pageSize = 10;
    private int index;

    PublicChooseProjectAdapter adapter;
    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_public_choose_project;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        titleBar.setMidText("选择项目");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        titleBar.setRightTextVisible(false);
        titleBar.setRightTextOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        tvSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                getNetData(editable.toString());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PublicChooseProjectAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                for (int i = 0; i < datas.size(); i++){
                    datas.get(i).setFlag(false);
                }
                datas.get(position).setFlag(true);
                index = position;
                adapter.notifyDataSetChanged();
                Intent intent = new Intent();
                intent.putExtra("projName",datas.get(index).getProjName());
                intent.putExtra("projId",datas.get(index).getProjId());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        initRefresh();
        getNetData("");
    }

    public void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                pageNo = 1;
                if (DisplayUtil.hasNetwork(PublicChooseProjectActivity.this))
                {
                    getNetData("");
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
                if (DisplayUtil.hasNetwork(PublicChooseProjectActivity.this))
                {
//                    if (projectLibAdapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {
                    pageNo = pageNo + 1;
                    getNetData("");
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
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData("");
            }
        });

        if (!DisplayUtil.hasNetwork(PublicChooseProjectActivity.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData(String name)
    {
        ListHttpHelper.getProjectListData(PublicChooseProjectActivity.this, SPUtils.get(this, USER_ACCOUNT, "").toString(), name, new SDRequestCallBack(PotentialProjectsPersonalBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PotentialProjectsPersonalBean listBean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                        if (pageNo == 1)
                        {
                            if (StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData().getData().size() > 0)
                            {
                                datas.clear();
                                datas.addAll(listBean.getData().getData());
                                mLoadingView.hide();
                            } else {
                                datas.clear();
                                mLoadingView.showNoContent("暂无数据");
                            }
                        } else {
                            if (StringUtils.notEmpty(listBean.getData().getData()) || listBean.getData().getData().size() > 0) {
                                datas.addAll(listBean.getData().getData());
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
