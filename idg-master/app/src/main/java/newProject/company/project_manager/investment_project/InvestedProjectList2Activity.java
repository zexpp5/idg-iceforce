package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.InvestedProjectListAdapter;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/27.
 */

public class InvestedProjectList2Activity extends BaseActivity{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
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
    @Override
    protected int getContentLayout() {
        return R.layout.activity_invested_project_list_2;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        //标题
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//      mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupwindow();
            }
        });
        mNavigatorBar.setMidText("已投资项目");
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
        recyclerView.setLayoutManager(new LinearLayoutManager(InvestedProjectList2Activity.this));
        adapter = new InvestedProjectListAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.ll_item:
                        Intent intent = new Intent(InvestedProjectList2Activity.this, InvestedProjectsDetailActivity.class);
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
                if (DisplayUtil.hasNetwork(InvestedProjectList2Activity.this))
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
                if (DisplayUtil.hasNetwork(InvestedProjectList2Activity.this))
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

    public void getData() {
        //根据权限调用小组还是个人的接口列表
        if (SPUtils.get(InvestedProjectList2Activity.this,SPUtils.IS_MANAGER_MY,"0").equals("1")) {
            //个人
            ListHttpHelper.getPotentialPersonalList(InvestedProjectList2Activity.this, SPUtils.get(InvestedProjectList2Activity.this, USER_ACCOUNT, "").toString(), "INVESTED", mPage + "", "10", key, "", "", comIndu, projInvestedStatus, "", new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
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
        }else {
            //小组
            ListHttpHelper.getPotentialGroupList(InvestedProjectList2Activity.this, SPUtils.get(InvestedProjectList2Activity.this, USER_ACCOUNT, "").toString(), "INVESTED", mPage+"", "10", key, "", "", comIndu, projInvestedStatus,"",new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
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
                    projInvestedStatus = "";
                    comIndu = "";
                    key = etKeyWord.getText().toString();
                    getData();
                }
                popupWindow.dismiss();
            }
        });

    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

}
