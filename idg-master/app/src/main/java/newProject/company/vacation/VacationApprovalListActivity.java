package newProject.company.vacation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.vacation.bean.HolidayApprovalListBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static com.injoy.idg.R.id.three_title;


/**
 * Created by tujingwu on 2017/10/26.
 */

public class VacationApprovalListActivity extends AppCompatActivity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTopBar;

    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private int mRequestCode = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_approval_list_layout);
        ButterKnife.bind(this);
        initTopBar();
        initRefresh();
        setData();
        getNetData();

    }

    private void initTopBar()
    {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText("请假审批");
//        mTopBar.setTitleBarBackground(DisplayUtil.mTitleColor);
        View.OnClickListener topBarListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == mTopBar.getLeftImageView())
                {
                    finish();
                }
            }
        };
        mTopBar.setLeftImageOnClickListener(topBarListener);
    }

    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(VacationApprovalListActivity.this))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });


        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                getNetData();
            }
        });

    }


    private void getNetData()
    {
        ListHttpHelper.getAllApproveDetail(this, new SDRequestCallBack(HolidayApprovalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                reFresh();
                ToastUtils.show(VacationApprovalListActivity.this, msg);
                mLoadingView.setVisibility(View.VISIBLE);
                mLoadingView.showLoading();
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                HolidayApprovalListBean holidayApprovalListBean = (HolidayApprovalListBean) responseInfo.getResult();
                if (null != holidayApprovalListBean && null != holidayApprovalListBean.getData() && holidayApprovalListBean
                        .getData().size() > 0)
                {
                    mLoadingView.setVisibility(View.GONE);
                    dataBeanList.clear();
                    dataBeanList.addAll(holidayApprovalListBean.getData());
                    mApplyAdapter.notifyDataSetChanged();
                } else
                {
                    reFresh();
                    mLoadingView.showNoContent(getResources().getString(R.string.had_no_data));
                }
            }
        });
    }

    private void reFresh()
    {
        dataBeanList.clear();
        mApplyAdapter.notifyDataSetChanged();
    }

    List<HolidayApprovalListBean.DataBean> dataBeanList = new ArrayList<>();

    private void setData()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.vacation_item_layout, dataBeanList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mApplyAdapter);
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                int approveId = mApplyAdapter.getData().get(position).getApproveId();
                Intent intent = new Intent(VacationApprovalListActivity.this, VacationApprovalDetailActivity.class);
                intent.putExtra("eid", approveId + "");
                startActivityForResult(intent, mRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == 2000)
            new Handler().postDelayed(new Runnable()
            {
                public void run()
                {
                    getNetData();
                }
            }, 1500);
    }

    private class ApplyAdapter extends BaseQuickAdapter<HolidayApprovalListBean.DataBean, BaseViewHolder>
    {

        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<HolidayApprovalListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, HolidayApprovalListBean.DataBean item)
        {
            holder.setText(R.id.one_title, (null == item.getName() ? "" : item.getName()) + "的请假")
                    .setText(R.id.two_title, "请假类型：" + (null == item.getHolidayType() ? "" : item.getHolidayType()))
                    .setText(three_title, "开始时间：" + (null == item.getLeaveStart() ? "" : item.getLeaveStart()))
                    .setText(R.id.four_title, "结束时间：" + (null == item.getLeaveEnd() ? "" : item.getLeaveEnd()))
                    .setText(R.id.five_title, "请假时长：" + item.getLeaveDay() + "天");

            if ("0".equals(item.getIsApprove()))
            {//0-未审批  1-同意  2-不同意
                holder.setText(R.id.one_content, "未批审")
                        .setTextColor(R.id.one_content, 0xffcd7555);
            } else if ("1".equals(item.getIsApprove()))
            {
                holder.setText(R.id.one_content, "同意")
                        .setTextColor(R.id.one_content, 0xff648a50);
            } else if ("2".equals(item.getIsApprove()))
            {
                holder.setText(R.id.one_content, "不同意")
                        .setTextColor(R.id.one_content, 0xffb75c5d);
            }
        }
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
