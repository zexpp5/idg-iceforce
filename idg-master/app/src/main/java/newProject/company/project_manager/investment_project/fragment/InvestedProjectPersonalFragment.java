package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.adapter.FollowProjectListAdapter;
import newProject.company.project_manager.investment_project.adapter.InvestedProjectListAdapter;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/25.
 */

public class InvestedProjectPersonalFragment extends Fragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;
    InvestedProjectListAdapter adapter;

    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String projInvestedStatus;
    String comIndu;
    String key;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_potential_project_personal, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void onStart() {
        super.onStart();
        projInvestedStatus = "";
        comIndu = "";
        key = "";
        getData();
    }

    private void initViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new InvestedProjectListAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.ll_item:
                        Intent intent = new Intent(getActivity(), InvestedProjectsDetailActivity.class);
                        intent.putExtra("projId",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjId());
                        intent.putExtra("projName",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjName());
                        startActivity(intent);
                        break;
                    case R.id.tv_word:
                        mPage = 1;
                        projInvestedStatus = ((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjInvestedStatus();
                        comIndu = "";
                        key = "";
                        getData();
                        break;
                    case R.id.tv_state:
                        mPage = 1;
                        projInvestedStatus = "";
                        key = "";
                        comIndu = ((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getComIndu();
                        getData();
                        break;
                }

            }
        });
        initRefresh();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
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

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                projInvestedStatus = "";
                comIndu = "";
                key = "";
                getData();
            }
        });

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
                    projInvestedStatus = "";
                    comIndu = "";
                    key = "";
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
                    if (adapter.getData().size() >= mBackListSize)
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

    public void getData(){
        ListHttpHelper.getPotentialPersonalList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "INVESTED", mPage+"", "10", key, "", "", comIndu, projInvestedStatus,"",new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 1)
                {
                    adapter.getData().clear();
                    if (bean.getData().getData().size() > 0)
                    {
                        mTips.hide();
                    } else
                    {
                        mTips.showNoContent("暂无数据");
                    }
                }
                adapter.getData().addAll(bean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    public void getSearchData(String data){
        projInvestedStatus = "";
        comIndu = "";
        key = data;
        getData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
