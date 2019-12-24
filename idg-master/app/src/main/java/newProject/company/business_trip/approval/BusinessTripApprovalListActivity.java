package newProject.company.business_trip.approval;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.db.dao.SDAllConstactsDao;
import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserDao;
import com.cxgz.activity.db.dao.SDUserEntity;
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
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.bean.CityListBean;
import newProject.company.business_trip.bean.BusinessTripApprovalListBean;
import newProject.company.vacation.VacationApprovalDetailActivity;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

/**
 * 差旅审批
 */
public class BusinessTripApprovalListActivity extends AppCompatActivity
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
        getCityData();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        reFreshList();
        getNetData();
    }

    private void initTopBar()
    {
        mTopBar.setLeftTextVisible(false);
        mTopBar.setRightTextVisible(false);
        mTopBar.setMidText("差旅审批");
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
//        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(BusinessTripApprovalListActivity.this))
                {
                    reFreshList();
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

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
        {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(BusinessTripApprovalListActivity.this))
                {
                    if (mPage >= mPageSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        mRefreshLayout.setLoadmoreFinished(true);
                    } else
                    {
                        ++mPage;
                        getNetData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    MyToast.showToast(BusinessTripApprovalListActivity.this, R.string.check_network_status);
                    mRefreshLayout.finishLoadmore(1000);
                }
            }
        });

        mLoadingView.showLoading();
        if (!DisplayUtil.hasNetwork(BusinessTripApprovalListActivity.this))
        {
            mLoadingView.showAccessFail();
        }
        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData();
            }
        });
    }

    private String searchString = "";
    private List<BusinessTripApprovalListBean.DataBean> businessTripApprovalList = new ArrayList<>();
    private int mPage = 1;
    //总条数
    private int mPageSize;

    private void getNetData()
    {
        ListHttpHelper.postTravelApprovalList(this, mPage, searchString, new SDRequestCallBack(BusinessTripApprovalListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                businessTripApprovalList.clear();
                mApplyAdapter.notifyDataSetChanged();
                ToastUtils.show(BusinessTripApprovalListActivity.this, msg);
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BusinessTripApprovalListBean listBean = (BusinessTripApprovalListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mLoadingView.hide();
                    mPageSize = listBean.getPageCount();
                    businessTripApprovalList.addAll(listBean.getData());
                    mApplyAdapter.notifyDataSetChanged();
                } else
                {
                    businessTripApprovalList.clear();
                    mApplyAdapter.notifyDataSetChanged();
                    mLoadingView.showNoContent(getResources().getString(R.string.had_no_data));
                }
            }
        });
    }

    private void reFreshList()
    {
        mPage = 1;
        businessTripApprovalList.clear();
    }

    private void setData()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.item_approval_business_trip_layout, businessTripApprovalList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerview.setAdapter(mApplyAdapter);
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {   
                int approveId = mApplyAdapter.getData().get(position).getBusinessId();
                Intent intent = new Intent(BusinessTripApprovalListActivity.this, BusinessTripApprovalDetailActivity.class);
                intent.putExtra("eid", approveId + "");
                intent.putExtra("applyId", mApplyAdapter.getData().get(position).getApplyId() + "");
                startActivityForResult(intent, mRequestCode);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == mRequestCode && resultCode == 2000)
//            getNetData();
    }

    private class ApplyAdapter extends BaseQuickAdapter<BusinessTripApprovalListBean.DataBean, BaseViewHolder>
    {


        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<BusinessTripApprovalListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BusinessTripApprovalListBean.DataBean item)
        {
            SDAllConstactsEntity sdAllConstactsEntity = SDAllConstactsDao.getInstance().findAllConstactsByAccount(item.getApply() + "");
            if (sdAllConstactsEntity != null)
            {
                if (StringUtils.notEmpty(sdAllConstactsEntity.getName()))
                    holder.setText(R.id.one_title, sdAllConstactsEntity.getName() + "的出差");
                else
                    holder.setText(R.id.one_title, item.getApply() + "的出差");
            } else
            {
                holder.setText(R.id.one_title, item.getApply() + "的出差");
            }

            holder.setText(R.id.two_content, item.getApplyDate());
            holder.setText(R.id.three_content, item.getBudget());

            String cityString = "";
            if (item.getCity().size() > 1)
            {
                for (int i = 0; i < item.getCity().size(); i++)
                {
                    cityString += getCityString(item.getCity().get(i));
                    if (i != item.getCity().size() - 1)
                    {
                        cityString += ",";
                    }
                }
            } else
            {
                cityString = getCityString(item.getCity().get(0));
            }

            holder.setText(R.id.four_content, cityString);
            holder.setText(R.id.five_content, item.getRemark() + "");
//            ((TextView) holder.getView(R.id.one_title)).getPaint().setFakeBoldText(true);
//            ((TextView) holder.getView(R.id.two_title)).getPaint().setFakeBoldText(true);
//            ((TextView) holder.getView(R.id.three_title)).getPaint().setFakeBoldText(true);
//            ((TextView) holder.getView(R.id.four_title)).getPaint().setFakeBoldText(true);
//            ((TextView) holder.getView(R.id.five_title)).getPaint().setFakeBoldText(true);
            holder.getView(R.id.one_content).setVisibility(View.GONE);

//            if (item.getApply() == 1)
//            {//0-未审批  1-同意  2-不同意

//                if (item.getCurrentApprove() != null)
//                {
//                    holder.setText(R.id.one_content, item.getCurrentApprove() + "批审中")
//                            .setTextColor(R.id.one_content, 0xffff5f13);
//                }
//            } else if (item.getIsApprove() == 2)
//            {
//                holder.setText(R.id.one_content, "批审通过")
//                        .setTextColor(R.id.one_content, 0xff2a8301);
//            } else if (item.getIsApprove() == 3)
//            {
//                holder.setText(R.id.one_content, "批审驳回")
//                        .setTextColor(R.id.one_content, 0xffeb4849);
//            }
        }
    }

    List<CityListBean.DataBean> cityBeanList = new ArrayList<>();

    private void getCityData()
    {
        ListHttpHelper.getCityTrip(BusinessTripApprovalListActivity.this, "", true, new SDRequestCallBack(CityListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("error_", msg);
                getNetData();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CityListBean cityListBean = (CityListBean) responseInfo.getResult();
                if (StringUtils.notEmpty(cityListBean) && StringUtils.notEmpty(cityListBean.getData()))
                {
                    cityBeanList.clear();
                    cityBeanList.addAll(cityListBean.getData());
                }
                getNetData();
            }
        });
    }

    private String getCityString(String cityType)
    {
        String cityName = "";
        if (StringUtils.notEmpty(cityType))
        {
            cityName = cityType;
        } else
        {
            cityName = "";
        }
        if (cityBeanList.size() > 0)
        {
            Iterator<CityListBean.DataBean> iterator = cityBeanList.iterator();
            while (iterator.hasNext())
            {
                CityListBean.DataBean dataBean = iterator.next();
                if (String.valueOf(dataBean.getId()).equals(cityType))
                {
                    cityName = dataBean.getName();
                }
            }
        }

        return cityName;
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
