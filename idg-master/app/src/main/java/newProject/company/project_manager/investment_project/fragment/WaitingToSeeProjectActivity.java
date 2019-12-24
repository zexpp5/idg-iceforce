package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
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
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailForWaitingToSeeActivity;
import newProject.company.project_manager.investment_project.ProjectActivity;
import newProject.company.project_manager.investment_project.ProjectLibrarySearchActivity;
import newProject.company.project_manager.investment_project.WaitingToSeeUserListActivity;
import newProject.company.project_manager.investment_project.adapter.ProjectLibraryAdapter;
import newProject.company.project_manager.investment_project.adapter.WaitingToSeeAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleInduAdapter;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import newProject.company.project_manager.investment_project.bean.ThemeBean;
import newProject.company.project_manager.investment_project.bean.WorkCircleIndusBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/25.
 */

public class WaitingToSeeProjectActivity extends BaseActivity {
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;
    ThemeBean bean;
    WaitingToSeeAdapter adapter;

    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();


    String comIndu;
    String tag;
    String key;
    String isAllocation;

    List<PPIndustryListBean.DataBeanX.DataBean> industryLists = new ArrayList<>();
    List<WorkCircleIndusBean> induLists = new ArrayList<>();
    PopupWindow popupWindow;
    @Override
    protected int getContentLayout() {
        return R.layout.fragment_project_library;
    }

    @Override
    protected void init() {

        ButterKnife.bind(this);
        tvTitle.setText("待看项目");
        recyclerView.setLayoutManager(new LinearLayoutManager(WaitingToSeeProjectActivity.this));
        adapter = new WaitingToSeeAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.ll_item:
                        PotentialProjectsPersonalBean.DataBeanX.DataBean bean = (PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position);
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(bean.getProjType())) {
                            intent.setClass(WaitingToSeeProjectActivity.this, PotentialProjectsDetailForWaitingToSeeActivity.class);
                        } else if ("INVESTED".equals(bean.getProjType())) {
                            intent.setClass(WaitingToSeeProjectActivity.this, InvestedProjectsDetailActivity.class);
                        } else if ("FOLLOW_ON".equals(bean.getProjType())) {
                            intent.setClass(WaitingToSeeProjectActivity.this, FollowProjectDetailActivity.class);
                        } else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projId", bean.getProjId());
                        intent.putExtra("projName", bean.getProjName());
                        startActivity(intent);
                        break;
                    case R.id.tv_dist:
                        Intent userIntent = new Intent(WaitingToSeeProjectActivity.this, WaitingToSeeUserListActivity.class);
                        userIntent.putExtra("projId",((PotentialProjectsPersonalBean.DataBeanX.DataBean) adapter.getData().get(position)).getProjId());
                        startActivity(userIntent);
                        break;
                    case R.id.tv_state2:
                        mPage = 1;
                        tag = "";
                        key = "";
                        isAllocation = "";
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


        getThemeData();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPage = 1;
        comIndu = "";
        tag = "";
        key = "";
        isAllocation = "";
        getData();
    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(WaitingToSeeProjectActivity.this)) {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    tag = "";
                    comIndu = "";
                    key = "";
                    isAllocation = "";
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
                if (DisplayUtil.hasNetwork(WaitingToSeeProjectActivity.this)) {
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
        ListHttpHelper.getWaitingToSeeProjectList(WaitingToSeeProjectActivity.this,SPUtils.get(WaitingToSeeProjectActivity.this, USER_ACCOUNT, "").toString(), mPage + "", "10",  tag, key, "", "", "", "","",comIndu,isAllocation, new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
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

    @OnClick({R.id.iv_back,R.id.ll_title, R.id.iv_search})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_title:
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    setPopupWindowData();
                    initTopPopupWindow(view);
                }
                break;
            case R.id.iv_search:
                initPopupwindow();
                break;
        }
    }

    private void initPopupwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_research_report_search, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });

        final EditText etKeyWord = (EditText) contentView.findViewById(R.id.et_keyword);
        Button btnReset = (Button) contentView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeyWord.setText("");
            }
        });
        Button btnSearch = (Button) contentView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.notEmpty(etKeyWord.getText())) {
                    mPage = 1;
                    tag = "";
                    comIndu = "";
                    isAllocation = "";
                    key = etKeyWord.getText().toString();
                    getData();
                }
                popupWindow.dismiss();
            }
        });

    }

    private void getIndustryData() {
        ListHttpHelper.getIndustryListData(WaitingToSeeProjectActivity.this, new SDRequestCallBack(PPIndustryListBean.class) {
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

    private void getThemeData() {
        ListHttpHelper.getThemeListData(WaitingToSeeProjectActivity.this, new SDRequestCallBack(ThemeBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                bean = (ThemeBean) responseInfo.getResult();

            }
        });
    }

    public void setPopupWindowData() {
       induLists.clear();
        for (int i = 0; i < bean.getData().getData().size(); i++) {
            WorkCircleIndusBean indusBean1 = new WorkCircleIndusBean();
            indusBean1.setDesc(bean.getData().getData().get(i));
            induLists.add(indusBean1);
        }

    }


    public void initTopPopupWindow(View view) {
        View contentView = LayoutInflater.from(WaitingToSeeProjectActivity.this).inflate(R.layout.popupwindow_waiting_to_see_top, null);
        popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        RecyclerView recyclerViewIndus = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        WorkCircleInduAdapter induAdapter = new WorkCircleInduAdapter(induLists);
        recyclerViewIndus.setLayoutManager(new GridLayoutManager(WaitingToSeeProjectActivity.this, 4));
        recyclerViewIndus.setAdapter(induAdapter);
        induAdapter.notifyDataSetChanged();
        induAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                popupWindow.dismiss();
                mPage = 1;
                tag = induLists.get(position).getDesc();
                comIndu = "";
                key = "";
                getData();
            }
        });

        LinearLayout llDist = (LinearLayout) contentView.findViewById(R.id.ll_dist);
        String flag = (String)SPUtils.get(WaitingToSeeProjectActivity.this, SPUtils.ROLE_FLAG, "0");
        if (flag.equals("206")) {
            TextView tvDist = (TextView) contentView.findViewById(R.id.tv_dist);
            tvDist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mPage = 1;
                    tag = "";
                    comIndu = "";
                    key = "";
                    isAllocation = "1";
                    getData();
                }
            });
            TextView tvUnDist = (TextView) contentView.findViewById(R.id.tv_undist);
            tvUnDist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                    mPage = 1;
                    tag = "";
                    comIndu = "";
                    key = "";
                    isAllocation = "0";
                    getData();
                }
            });
        }else {
            llDist.setVisibility(GONE);
        }

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
        WindowManager.LayoutParams lp = WaitingToSeeProjectActivity.this.getWindow().getAttributes();
        lp.alpha = alpha;
        WaitingToSeeProjectActivity.this.getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == WaitingToSeeProjectActivity.this.RESULT_OK) {

        }
    }

}
