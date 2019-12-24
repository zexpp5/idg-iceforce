package newProject.company.superpower;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.superpower.bean.SupertiorBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;


/**
 * Created by tujingwu on 2017/10/26.
 */

public class SuperiorListActivity extends AppCompatActivity {
    private static final int mRESULT_CODE = 2;

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private SupertiorListAdapter mAdapter;
    private SupertiorBean mSupertiorData;
    private String mUserId;//详情item用户id


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supertior_list);
        ButterKnife.bind(this);
        getIn();
        initTopBar();
        initRefresh();
        getNetData();
    }

    private void getIn() {
        mUserId = getIntent().getStringExtra("userId");
        mSupertiorData = (SupertiorBean) getIntent().getSerializableExtra("current_superior");
    }

    private void initTopBar() {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(true);
        mTopBar.setMidText("上级");
        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == mTopBar.getLeftImageView()) {
                    finish();
                } else if (v == mTopBar.getRightText()) {
                    backSelectData();
                }
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
        mTopBar.setRightTextOnClickListener(topBarListener);
    }

    public void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(SuperiorListActivity.this)) {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });


        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener() {

            @Override
            public void onRetry() {
                getNetData();
            }
        });

    }

    private void getNetData() {
        ListHttpHelper.getUsersByCode(this, mUserId, new SDRequestCallBack() {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                ToastUtils.show(SuperiorListActivity.this, msg);
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.showLoading();
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                try {
                    JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if (null != dataArray && dataArray.length() != 0) {
                        List<SupertiorBean> beanList = new ArrayList<>();
                        for (int i = 0; i < dataArray.length(); i++) {
                            SupertiorBean supertiorBean = new SupertiorBean();
                            int userId = dataArray.getJSONObject(i).getInt("userId");
                            String userName = dataArray.getJSONObject(i).getString("userName");
                            supertiorBean.setCheck(false);
                            supertiorBean.setUserId(userId);
                            supertiorBean.setUserName(userName);

                            beanList.add(supertiorBean);
                        }
                        mLoadingView.hide();
                        setData(beanList);
                    } else {
                        mLoadingView.setVisibility(View.VISIBLE);
                        mLoadingView.showLoading();
                        mLoadingView.showNoContent("暂无数据");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void setData(final List<SupertiorBean> data) {
        for (SupertiorBean d : data) {//用于返回
            if (null != mSupertiorData && d.getUserId() == mSupertiorData.getUserId()) {
                d.setCheck(true);
                mSupertiorData = d;
            }
        }


        mAdapter = new SupertiorListAdapter(R.layout.item_supertior_list_layout, data);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SupertiorBean dataBean = (SupertiorBean) adapter.getData().get(position);
                mSupertiorData = dataBean;
                for (SupertiorBean bean : data) {
                    if (bean.getUserId() == dataBean.getUserId()) {
                        bean.setCheck(true);
                    } else {
                        bean.setCheck(false);
                    }
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }


    private void backSelectData() {
        if (null == mSupertiorData) {
            ToastUtils.show(this, "选择不能为空!");
            return;
        }

        Intent intent = new Intent();
        intent.putExtra("supertior", mSupertiorData);
        setResult(mRESULT_CODE, intent);
        finish();
    }

    private class SupertiorListAdapter extends BaseQuickAdapter<SupertiorBean, BaseViewHolder> {


        public SupertiorListAdapter(@LayoutRes int layoutResId, @Nullable List<SupertiorBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, SupertiorBean item) {
            holder.setText(R.id.tv_name, item.getUserName());
            if (item.isCheck()) {
                holder.setChecked(R.id.rb, true);
            } else {
                holder.setChecked(R.id.rb, false);
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
