package newProject.company.business_trip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.bean.CityListBean;
import newProject.company.business_trip.approval.BusinessTripDetailActivity;
import newProject.company.business_trip.bean.BusinessTripListBean;
import newProject.company.vacation.VacationDetailActivity;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;


/**
 * 我的出差-审批中
 */
public class BusinessTripStateOneFragment extends Fragment
{
    private static String SINGNED = "signed";
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private String signed = "1";//1=批审中，2=同意，3=拒绝
    private int mPage = 1;
    //总条数
    private int mPageSize;

//    List<CityListBean.DataBean> cityData;

    public static Fragment newInstance(String signed)
    {
        BusinessTripStateOneFragment businessTripStateOneFragment = new BusinessTripStateOneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SINGNED, signed);
        businessTripStateOneFragment.setArguments(bundle);
        return businessTripStateOneFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        if (this.getArguments() != null)
        {
            Bundle bundle = this.getArguments();
            signed = bundle.getString(SINGNED);
        }
        setData();
        initRefresh();
        getNetData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.vacation_approval_list_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void reFreshList()
    {
        mPage = 1;
        businessTripList.clear();
    }

    public void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {

                if (DisplayUtil.hasNetwork(getActivity()))
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
                if (DisplayUtil.hasNetwork(getActivity()))
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
                    MyToast.showToast(getActivity(), "请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });

        mLoadingView.showLoading();
        mLoadingView.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (mRecyclerview != null)
                {
                    mRecyclerview.setVisibility(visible ? View.GONE : View.VISIBLE);
                }
            }
        });

        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mLoadingView.showLoading();
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void refresh()
    {
        mApplyAdapter.notifyDataSetChanged();
    }

    List<BusinessTripListBean.DataBean> businessTripList = new ArrayList<>();

    private void getNetData()
    {
//        if (cityData == null || cityData.size() <= 0)
//        {
//            getCityData();
//            return;
//        }
        ListHttpHelper.postTravelApplyList(getActivity(), mPage, signed, new SDRequestCallBack(BusinessTripListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showAccessFail();
                ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BusinessTripListBean listBean = (BusinessTripListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mLoadingView.hide();
                    mPageSize = listBean.getPageCount();
//                    for (int i = 0; i < listBean.getData().size(); i++)
//                    {
//                        List<String> cityId = listBean.getData().get(i).getCity();
//                        for (int j = 0; j < cityId.size(); j++)
//                        {
//                            for (int k = 0; k < cityData.size(); k++)
//                            {
//                                if (cityId.get(j).equals(cityData.get(k).getId() + ""))
//                                {
//                                    cityId.set(j, cityData.get(k).getName());
//                                    break;
//                                }
//                            }
//                        }
//                    }

                    businessTripList.addAll(listBean.getData());
                    refresh();
                } else
                {
                    mLoadingView.showNoContent(getResources().getString(R.string.had_no_data));
                }
            }
        });
    }

    private void getCityData()
    {
        ListHttpHelper.getCityTrip(getContext(), "", true, new SDRequestCallBack(CityListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDLogUtil.debug("error_", msg);
                mLoadingView.showAccessFail();
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                CityListBean cityListBean = (CityListBean) responseInfo.getResult();
//                if (StringUtils.notEmpty(cityListBean) && StringUtils.notEmpty(cityListBean.getData()))
//                {
//                    cityData = cityListBean.getData();
//                }
//                if (cityData != null && cityData.size() > 0)
//                {
//                    mPage = 1;
//                    getNetData();
//                } else
//                {
//                    mLoadingView.showAccessFail();
//                }
            }
        });
    }

    private void setData()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.item_travel_layout, businessTripList);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mApplyAdapter);
        mApplyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BusinessTripListBean.DataBean departDataBean = (BusinessTripListBean.DataBean) adapter.getData().get
                        (position);
                if (adapter.getData().size() > 0 && departDataBean != null)
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("EID", departDataBean.getBusinessId() + "");
                    bundle.putString(SINGNED, signed);
                    Intent intent = new Intent(getActivity(), BusinessTripDetailActivity.class);
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class ApplyAdapter extends BaseQuickAdapter<BusinessTripListBean.DataBean, BaseViewHolder>
    {
        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<BusinessTripListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BusinessTripListBean.DataBean item)
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
            for (int i = 0; i < item.getCity().size(); i++)
            {
                cityString += item.getCity().get(i);
                if (i != item.getCity().size() - 1)
                {
                    cityString += ",";
                }
            }
            holder.setText(R.id.four_content, cityString);
            holder.setText(R.id.five_content, item.getRemark() + "");

            holder.getView(R.id.ll_reason).setVisibility(View.GONE);

            if (item.getIsApprove() == 1)
            {//0-未审批  1-同意  2-不同意
                if (item.getCurrentApprove() != null)
                {
                    holder.setText(R.id.one_content, item.getCurrentApprove() + "批审中")
                            .setTextColor(R.id.one_content, 0xffff5f13);
                }
            } else if (item.getIsApprove() == 2)
            {
                holder.setText(R.id.one_content, "批审通过")
                        .setTextColor(R.id.one_content, 0xff2a8301);
            } else if (item.getIsApprove() == 3)
            {
                holder.setText(R.id.one_content, "批审驳回")
                        .setTextColor(R.id.one_content, 0xffeb4849);

                holder.getView(R.id.ll_reason).setVisibility(View.VISIBLE);
                if (StringUtils.notEmpty(item.getReason()))
                {
                    holder.setText(R.id.tv_content_reason, item.getReason());
                } else
                {
                    holder.setText(R.id.tv_content_reason, "无");
                }
            }
        }
    }


}
