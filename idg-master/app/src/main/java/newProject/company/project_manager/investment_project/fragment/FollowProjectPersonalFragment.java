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
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.adapter.FollowProjectListAdapter;
import newProject.company.project_manager.investment_project.adapter.PotentialProjectsPersonalAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/25.
 */

public class FollowProjectPersonalFragment extends Fragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;
    FollowProjectListAdapter adapter;

    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String stsId;
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
        stsId = "";
        comIndu = "";
        key = "";
        getData();
    }

    private void initViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new FollowProjectListAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.ll_item:
                        Intent intent = new Intent(getActivity(), FollowProjectDetailActivity.class);
                        intent.putExtra("projId",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjId());
                        intent.putExtra("projName",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjName());
                        intent.putExtra("permission",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getPermission());
                        startActivity(intent);
                        break;
                    case R.id.tv_state:
                        mPage = 1;
                        stsId = ((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getStsId();
                        comIndu = "";
                        key = "";
                        getData();
                        break;
                    case R.id.tv_state2:
                        mPage = 1;
                        stsId = "";
                        key = "";
                        comIndu = ((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getComIndu();
                        getData();
                        break;
                    case R.id.iv_star:
                        initPopupWindow(view,((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getFollowUpStatus(),((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjId(),position);
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
                stsId = "";
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
                    stsId = "";
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
        ListHttpHelper.getPotentialPersonalList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "FOLLOW_ON", mPage+"", "10", key, "", "", comIndu, "",stsId,new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
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

    public void initPopupWindow(View view,String flag,final String projId, final int pos){

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_star, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        LinearLayout llHalf = (LinearLayout) contentView.findViewById(R.id.ll_half);
        llHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"1",pos);
                popupWindow.dismiss();
            }
        });
        LinearLayout llAll = (LinearLayout) contentView.findViewById(R.id.ll_all);
        llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"2",pos);
                popupWindow.dismiss();
            }
        });
        LinearLayout llHollow = (LinearLayout) contentView.findViewById(R.id.ll_hollow);
        llHollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"0",pos);
                popupWindow.dismiss();
            }
        });
        if (StringUtils.notEmpty(flag)) {
            if (flag.equals("1")) {
                llHalf.setVisibility(GONE);
                llAll.setVisibility(VISIBLE);
                llHollow.setVisibility(VISIBLE);
            } else if (flag.equals("2")) {
                llAll.setVisibility(GONE);
                llHalf.setVisibility(VISIBLE);
                llHollow.setVisibility(VISIBLE);
            } else {
                llHollow.setVisibility(GONE);
                llHalf.setVisibility(VISIBLE);
                llAll.setVisibility(VISIBLE);
            }
        } else {
            llHollow.setVisibility(GONE);
            llHalf.setVisibility(VISIBLE);
            llAll.setVisibility(VISIBLE);
        }

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xPos = view.getWidth() / 2 - popupWindow.getContentView().getMeasuredWidth() + 50 ;
        popupWindow.showAsDropDown(view, xPos,30);
    }

    public void updateFollowUpStatus(String projId,final String follUpStatus,final int pos){
        ListHttpHelper.updateFollowUpStatus(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), projId, follUpStatus, new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    datas.get(pos).setFollowUpStatus(follUpStatus);
                    adapter.notifyDataSetChanged();
                }else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void getSearchData(String data){
        stsId = "";
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
