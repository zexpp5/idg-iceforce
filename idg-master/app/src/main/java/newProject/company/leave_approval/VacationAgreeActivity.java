package newProject.company.leave_approval;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.leaveapplay.bean.NewLeaveListBean;
import newProject.company.vacation.VacationDetailActivity;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.three_title;

/**
 * Created by Administrator on 2017/11/27.
 */

public class VacationAgreeActivity extends Activity {
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    private ApplyAdapter mApplyAdapter;
    private String signed = "2";//1=批审中，2=同意，3=拒绝


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vacation_agree_list_layout);
        ButterKnife.bind(this);

        mTitleBar = (CustomNavigatorBar)findViewById(R.id.title_bar);
        mTitleBar.setMidText("销假");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initRefresh();
        getNetData();
    }


    public void initRefresh() {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(VacationAgreeActivity.this)) {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener() {

            @Override
            public void onVisibleChanged(boolean visible) {
                if (mRecyclerview != null) {
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener() {

            @Override
            public void onRetry() {
                mLoadingView.showLoading();
                getNetData();
            }
        });
        if (!DisplayUtil.hasNetwork(VacationAgreeActivity.this)) {
            mLoadingView.showAccessFail();
        }

    }


    private void getNetData() {
        ListHttpHelper.getVacationApplyList(VacationAgreeActivity.this, signed, new SDRequestCallBack(NewLeaveListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                mLoadingView.showAccessFail();
                ToastUtils.show(VacationAgreeActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NewLeaveListBean listBean = (NewLeaveListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty()) {
                    mLoadingView.hide();
                    setData(listBean);
                } else {
                    mLoadingView.showNoContent("暂无数据");
                }
            }
        });
    }


    private void setData(NewLeaveListBean hab) {
        mApplyAdapter = new ApplyAdapter(R.layout.vacation_item_layout, hab.getData());
        mRecyclerview.setLayoutManager(new LinearLayoutManager(VacationAgreeActivity.this));
        mRecyclerview.setAdapter(mApplyAdapter);
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NewLeaveListBean.NewLeaveDataBean departDataBean = (NewLeaveListBean.NewLeaveDataBean) adapter.getData().get(position);
                if (adapter.getData().size() > 0 && departDataBean != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("EID", departDataBean.getLeaveId());
                    bundle.putInt("SIGN", 2);
                    Intent intent = new Intent(VacationAgreeActivity.this, VacationDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    private class ApplyAdapter extends BaseQuickAdapter<NewLeaveListBean.NewLeaveDataBean, BaseViewHolder> {


        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<NewLeaveListBean.NewLeaveDataBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, NewLeaveListBean.NewLeaveDataBean item) {
            holder.setText(R.id.one_title, (null == item.getUserName() ? "" : item.getUserName()) + "的请假")
                    .setText(R.id.two_title, "休假类型：" + (null == item.getLeaveType() ? "" : item.getLeaveType()))
                    .setText(three_title, "开始时间：" + (null == item.getLeaveStart() ? "" : item.getLeaveStart()))
                    .setText(R.id.four_title, "结束时间：" + (null == item.getLeaveEnd() ? "" : item.getLeaveEnd()))
                    .setText(R.id.five_title, "休假时长：" + item.getLeaveDay() + "天");

            if (item.getSigned() == 1) {//0-未审批  1-同意  2-不同意
                if (item.getCurrentApprove() != null) {
                    holder.setText(R.id.one_content, item.getCurrentApprove() + "批审中")
                            .setTextColor(R.id.one_content, 0xffff5f13);
                }
            } else if (item.getSigned() == 2) {
                holder.setText(R.id.one_content, "同意")
                        .setTextColor(R.id.one_content, 0xff2a8301);
            } else if (item.getSigned() == 3) {
                holder.setText(R.id.one_content, "驳回")
                        .setTextColor(R.id.one_content, 0xffeb4849);
            }
        }
    }


}
