package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ProjectLibraryAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleInduAdapter;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import newProject.company.project_manager.investment_project.bean.WorkCircleIndusBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/6/9.
 */

public class ProjectLibraryActivity extends BaseActivity {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;

    ProjectLibraryAdapter adapter;

    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String projInvestedStatus;
    String stsId;
    String comIndu;
    String personInCharge;

    List<PPIndustryListBean.DataBeanX.DataBean> industryLists = new ArrayList<>();
    List<WorkCircleIndusBean> induLists = new ArrayList<>();
    List<WorkCircleIndusBean> otherLists = new ArrayList<>();
    PopupWindow popupWindow;

    String projQueryStr = "";
    String teamQueryStr = "";
    String queryStr = "";
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_project_library;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(ProjectLibraryActivity.this));
        adapter = new ProjectLibraryAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_item:
                        PotentialProjectsPersonalBean.DataBeanX.DataBean bean = (PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position);
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(bean.getProjType())) {
                            intent.setClass(ProjectLibraryActivity.this, PotentialProjectsDetailActivity.class);
                        } else if ("INVESTED".equals(bean.getProjType())) {
                            intent.setClass(ProjectLibraryActivity.this, InvestedProjectsDetailActivity.class);
                        } else if ("FOLLOW_ON".equals(bean.getProjType())) {
                            intent.setClass(ProjectLibraryActivity.this, FollowProjectDetailActivity.class);
                        } else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projId", bean.getProjId());
                        intent.putExtra("projName", bean.getProjName());
                        startActivity(intent);
                        break;
                    case R.id.tv_word:
                        mPage = 1;
                        //personInCharge = "";
                        projInvestedStatus = ((PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position)).getProjInvestedStatus();
                        //stsId = "";
                        //comIndu = "";
                        getData();
                        break;
                    case R.id.tv_person_in_charge:
                        mPage = 1;
                        personInCharge = ((PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position)).getProjManagers();
                        /*projInvestedStatus = "";
                        stsId = "";
                        comIndu = "";*/
                        getData();
                        break;
                    case R.id.tv_state:
                        mPage = 1;
                        /*personInCharge = "";
                        projInvestedStatus = "";*/
                        stsId = ((PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position)).getStsId();
                        //comIndu = "";
                        getData();
                        break;
                    case R.id.tv_state2:
                        mPage = 1;
                       /* personInCharge = "";
                        projInvestedStatus = "";
                        stsId = "";*/
                        comIndu = ((PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position)).getComIndu();
                        getData();
                        break;
                }

            }
        });
        initRefresh();
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {

            @Override
            public void onVisibleChanged(boolean visible) {
                if (recyclerView != null) {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener() {
            @Override
            public void onRetry() {
                mTips.showLoading();
                getData();
            }
        });

        personInCharge = "";
        projInvestedStatus = "";
        stsId = "";
        comIndu = "";
        projQueryStr = "";
        teamQueryStr = "";
        queryStr = "";
        getData();
        getIndustryData();

    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(ProjectLibraryActivity.this)) {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    personInCharge = "";
                    projInvestedStatus = "";
                    stsId = "";
                    comIndu = "";
                    projQueryStr = "";
                    teamQueryStr = "";
                    queryStr = "";
                    getData();
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(ProjectLibraryActivity.this)) {
                    if (adapter.getData().size() >= mBackListSize) {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getData() {
        ListHttpHelper.getProjectLibraryList(ProjectLibraryActivity.this, SPUtils.get(ProjectLibraryActivity.this, USER_ACCOUNT, "").toString(), "", mPage + "", "10", "", "", personInCharge, comIndu, projInvestedStatus, stsId, projQueryStr, teamQueryStr, queryStr, new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                mTips.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                mBackListSize = bean.getData().getTotal();
                if (mPage == 1) {
                    adapter.getData().clear();
                    if (bean.getData().getData().size() > 0) {
                        mTips.hide();
                    } else {
                        mTips.showNoContent("暂无数据");
                    }
                }
                adapter.getData().addAll(bean.getData().getData());
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.ll_title, R.id.iv_search})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_title:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    setPopupWindowData();
                    initTopPopupWindow(view);
                }
                break;
            case R.id.iv_search:
                Intent intent = new Intent(ProjectLibraryActivity.this, ProjectLibrarySearchActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    private void getIndustryData() {
        ListHttpHelper.getIndustryListData(ProjectLibraryActivity.this, new SDRequestCallBack(PPIndustryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PPIndustryListBean bean = (PPIndustryListBean) responseInfo.getResult();
                industryLists.clear();
                industryLists.addAll(bean.getData().getData());
            }
        });
    }

    public void setPopupWindowData() {
        induLists.clear();
        otherLists.clear();
        WorkCircleIndusBean indusBean = new WorkCircleIndusBean();
        indusBean.setDesc("全部");
        induLists.add(indusBean);

        for (int i = 0; i < industryLists.size(); i++) {
            WorkCircleIndusBean indusBean1 = new WorkCircleIndusBean();
            indusBean1.setDesc(industryLists.get(i).getCodeNameZhCn());
            indusBean1.setKey(industryLists.get(i).getCodeKey());
            induLists.add(indusBean1);
        }
        for (int j = 0; j < industryLists.get(0).getChildren().size(); j++) {
            WorkCircleIndusBean indusBean2 = new WorkCircleIndusBean();
            indusBean2.setDesc(industryLists.get(0).getChildren().get(j).getCodeNameZhCn());
            indusBean2.setKey(industryLists.get(0).getChildren().get(j).getCodeKey());
            otherLists.add(indusBean2);
        }
    }


    public void initTopPopupWindow(View view) {
        View contentView = LayoutInflater.from(ProjectLibraryActivity.this).inflate(R.layout.popupwindow_work_circle_top, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        RecyclerView recyclerViewIndus = (RecyclerView) contentView.findViewById(R.id.recycler_view_indus);
        WorkCircleInduAdapter induAdapter = new WorkCircleInduAdapter(induLists);
        recyclerViewIndus.setLayoutManager(new GridLayoutManager(ProjectLibraryActivity.this, 4));
        recyclerViewIndus.setAdapter(induAdapter);
        induAdapter.notifyDataSetChanged();

        RecyclerView recyclerViewOther = (RecyclerView) contentView.findViewById(R.id.recycler_view_other);
        final WorkCircleInduAdapter induAdapter2 = new WorkCircleInduAdapter(otherLists);
        recyclerViewOther.setLayoutManager(new GridLayoutManager(ProjectLibraryActivity.this, 4));
        recyclerViewOther.setAdapter(induAdapter2);
        induAdapter2.notifyDataSetChanged();

        induAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if (position == 0) {
                    popupWindow.dismiss();
                    mPage = 1;
                    mBackListSize = 0;
                    personInCharge = "";
                    projInvestedStatus = "";
                    stsId = "";
                    comIndu = "";
                    getData();
                    return;
                }

                for (int i = 0; i < induLists.size(); i++) {
                    induLists.get(i).setFlag(false);
                }
                induLists.get(position).setFlag(true);

                adapter.notifyDataSetChanged();
                //第一个是全部，所以要减一
                position--;

                otherLists.clear();
                for (int i = 0; i < industryLists.get(position).getChildren().size(); i++) {
                    WorkCircleIndusBean bean = new WorkCircleIndusBean();
                    bean.setDesc(industryLists.get(position).getChildren().get(i).getCodeNameZhCn());
                    bean.setKey(industryLists.get(position).getChildren().get(i).getCodeKey());
                    otherLists.add(bean);
                }
                induAdapter2.notifyDataSetChanged();
            }
        });

        induAdapter2.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();
                mPage = 1;
                mBackListSize = 0;
                personInCharge = "";
                projInvestedStatus = "";
                stsId = "";
                comIndu = otherLists.get(position).getKey();
                getData();
            }
        });

        popupWindow.setFocusable(false);
        popupWindow.setOutsideTouchable(false);
        setBackgroundAlpha(0.5f);
        //contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.GRAY));
        int xPos = view.getWidth() / 2 - popupWindow.getContentView().getMeasuredWidth() / 2;
        popupWindow.showAsDropDown(view, xPos, 3);
        //(location[0] + view.getWidth() / 2) + popupWidth / 2, location[1] + popupHeight
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            mPage = 1;
            mBackListSize = 0;
            personInCharge = "";
            projInvestedStatus = "";
            stsId = "";
            comIndu = "";
            Bundle bundle = data.getBundleExtra("bundle");
            projQueryStr = bundle.getString("projQueryStr");
            teamQueryStr = bundle.getString("teamQueryStr");
            queryStr = bundle.getString("queryStr");
            getData();
        }
    }
}
