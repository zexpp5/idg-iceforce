package newProject.company.documentLib;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseFragment;
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
import newProject.company.projectLib.ProjectLibAdapter;
import newProject.company.project_manager.estate_project.bean.EstateList;
import newProject.company.project_manager.estate_project.detail.ProjectEstateDetailActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

//项目库
public class DocumentLibFragment extends BaseFragment implements BaseQuickAdapter.OnItemClickListener
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
    DocumentLibAdapter documentLibAdapter;

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_document_lib_main;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        //标题
        mNavigatorBar = (CustomNavigatorBar) view.findViewById(R.id.title_bar);
//        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
//        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText(getActivity().getString(R.string.super_tab_04));
        recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));

        documentLibAdapter = new DocumentLibAdapter(new ArrayList<EstateList.DataBean>());
        recycler_view.setAdapter(documentLibAdapter);
        initRefresh();

        mTips = (StatusTipsView) view.findViewById(R.id.loading_view);
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
        documentLibAdapter.setOnItemClickListener(this);
    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
                {
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
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    if (documentLibAdapter.getData().size() >= mBackListSize)
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
        ListHttpHelper.getProjectEstateList(getActivity(), mPage + "", new SDRequestCallBack(EstateList.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                EstateList result = (EstateList) responseInfo.getResult();
                List<EstateList.DataBean> data = result.getData();

                if (mPage == 1)
                {
                    documentLibAdapter.getData().clear();
                    mBackListSize = result.getTotal();
                    if (data.size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                documentLibAdapter.getData().addAll(data);
                documentLibAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 项目库的详情
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
    {
        Intent intent = new Intent(getActivity(), ProjectEstateDetailActivity.class);
        int projId = documentLibAdapter.getData().get(position).getProjId();
        intent.putExtra("projId", projId);
        startActivity(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
