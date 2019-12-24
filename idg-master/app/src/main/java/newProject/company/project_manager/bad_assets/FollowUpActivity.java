package newProject.company.project_manager.bad_assets;

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
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FollowUpActivity extends BaseActivity {
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    int mPage = 1, mBackListSize = 1;
    FollowUpAdapter projectEstateAdapter;
    int projId;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_follow_up;
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

        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        Bundle extras = getIntent().getExtras();
        String title = extras.getString("title", "");
        mNavigatorBar.setMidText(title);
        projId = extras.getInt("projId", 0);

        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        projectEstateAdapter = new FollowUpAdapter(new ArrayList<FollowUpBean.DataBean>());
        recycler_view.setAdapter(projectEstateAdapter);
        initRefresh();


        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {

            @Override
            public void onVisibleChanged(boolean visible) {
                if (recycler_view != null) {
                    recycler_view.setVisibility(visible ? GONE : VISIBLE);
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
        getData();


    }


    /**
     * 获取网络数据
     */
    public void getData() {

        ListHttpHelper.getFollowUpList(this, projId + "", mPage + "", new SDRequestCallBack(FollowUpBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                // ToastUtils.show(getActivity(), msg);
                if (mPage == 1) {
                    mTips.showAccessFail();
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowUpBean result = (FollowUpBean) responseInfo.getResult();
                List<FollowUpBean.DataBean> data = result.getData();

                if (mPage == 1) {
                    projectEstateAdapter.getData().clear();
                    mBackListSize = result.getTotal();
                    if (data.size() > 0) {
                        mTips.hide();
                    } else {
                        mTips.showNoContent("暂无数据");
                    }
                }
                projectEstateAdapter.getData().addAll(data);
                projectEstateAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(FollowUpActivity.this)) {
                    /*s_projName="";
                    s_induIds="";
                    s_stageIds ="";*/
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
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
                if (DisplayUtil.hasNetwork(FollowUpActivity.this)) {
                    if (projectEstateAdapter.getData().size() >= mBackListSize) {
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
