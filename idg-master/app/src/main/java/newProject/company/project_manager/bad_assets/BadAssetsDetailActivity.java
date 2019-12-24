package newProject.company.project_manager.bad_assets;

import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.cx.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.CommonUtils;
import com.utils.SDToast;
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.DetailHttpHelper;
import newProject.api.ListHttpHelper;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BadAssetsDetailActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_date)
    TextView tv_date;
    @Bind(R.id.tv_respond_man)
    TextView tv_respond_man;
    @Bind(R.id.tv_lawyer)
    TextView tv_lawyer;
    @Bind(R.id.tv_project_name)
    TextView tv_project_name;
    @Bind(R.id.tv_invest_money)
    TextView tv_invest_money;
    @Bind(R.id.tv_situation)
    TextView tv_situation;
    @Bind(R.id.tv_confirm_info)
    TextView tv_confirm_info;
    @Bind(R.id.tv_project_analysis)
    TextView tv_project_analysis;
    @Bind(R.id.tv_risk)
    TextView tv_risk;
    @Bind(R.id.tv_percentage)
    TextView tv_percentage;
    @Bind(R.id.tv_inside)
    TextView tv_inside;
    @Bind(R.id.tv_outside)
    TextView tv_outside;
    @Bind(R.id.tv_grade)
    TextView tv_grade;
    @Bind(R.id.tv_industry)
    TextView tv_industry;
    @Bind(R.id.tv_unknown)
    TextView tv_unknown;
    @Bind(R.id.scrollView)
    NestedScrollView scrollView;
    @Bind(R.id.recyclerview)
    RecyclerView recyclerview;
    @Bind(R.id.linear_follow)
    LinearLayout linear_follow;

    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    int projId;
    FollowUpAdapter followUpAdapter;
    int mPage = 1, mBackListSize = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bad_assets_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setRightTextVisible(false);
        projId = getIntent().getIntExtra("projId", 0);
        getData();


        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setNestedScrollingEnabled(false);
        followUpAdapter = new FollowUpAdapter(new ArrayList<FollowUpBean.DataBean>());
        recyclerview.setAdapter(followUpAdapter);
        initRefresh();
    }

    private void initRefresh() {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(BadAssetsDetailActivity.this)) {
                    if (followUpAdapter.getData().size() >= mBackListSize) {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        mPage = mPage + 1;
                        getFollowData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }


    /**
     * 获取网络数据
     */
    public void getData() {
        DetailHttpHelper.getBadAssetsList(this, projId + "", new SDRequestCallBack(BadAssetsDetailBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                BadAssetsDetailBean badAssetsDetailBean = (BadAssetsDetailBean) responseInfo.getResult();
                BadAssetsDetailBean.DataBean data = badAssetsDetailBean.getData();
                mNavigatorBar.setMidText(data.getEname());
                tv_date.setText(data.getProjInDate());
                tv_respond_man.setText(data.getDealLeadName());
                tv_lawyer.setText(data.getDealLegalName());
                tv_project_name.setText(data.getEname());
                tv_invest_money.setText(data.getInvestAmt());
                tv_situation.setText(data.getSituation());
                tv_confirm_info.setText(data.getConfirmInfo());
                tv_project_analysis.setText(data.getAnalysis());
                tv_risk.setText(data.getRiskControl());
                tv_percentage.setText(data.getPercent());
                tv_inside.setText(data.getEntity());
                tv_outside.setText(data.getEntityOversea());
                tv_unknown.setText(data.getClassType());
                tv_grade.setText(data.getGrade());
                tv_industry.setText(data.getIndusName());

                getFollowData();
                mRefreshLayout.setEnableLoadmore(true);

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    /**
     * 获取网络数据
     */

    public void getFollowData() {

        ListHttpHelper.getFollowUpList(this, projId + "", mPage + "", new SDRequestCallBack(FollowUpBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                if (mPage == 1) {
                    linear_follow.setVisibility(GONE);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowUpBean result = (FollowUpBean) responseInfo.getResult();
                List<FollowUpBean.DataBean> data = result.getData();

                if (mPage == 1) {
                    followUpAdapter.getData().clear();
                    mBackListSize = result.getTotal();
                }
                followUpAdapter.getData().addAll(data);
                followUpAdapter.notifyDataSetChanged();

                if (followUpAdapter.getData().size() > 0) {
                    linear_follow.setVisibility(VISIBLE);
                } else {
                    linear_follow.setVisibility(GONE);

                }
            }
        });
    }
}
