package newProject.company.project_manager.investment_project;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

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
import newProject.company.project_manager.investment_project.bean.InvestWayListBean;
import newProject.company.project_manager.investment_project.bean.MeetingListBean;
import newProject.utils.NewCommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;


/**
 * Created by Administrator on 2017/11/27.
 */

public class TwoFragment extends Fragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private ApplyAdapter mApplyAdapter;
    private int mEid;
    private List<InvestWayListBean.DataBean> mDataLists = new ArrayList<>();

    public static Fragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt("EID", id);
        TwoFragment fragment = new TwoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getInt("EID", 0);
        }
        initRefresh();
        initRV();

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        } else
        {
            getNetData();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_two, null);
        ButterKnife.bind(this, view);
        return view;
    }


    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setReboundInterpolator(new DecelerateInterpolator());
        mRefreshLayout.setDragRate(0.25f);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(getActivity()))
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
    }


    private void getNetData()
    {
        ListHttpHelper.getInvestWayList(getActivity(), mEid + "", new SDRequestCallBack(InvestWayListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (error.getExceptionCode() == 400)
                {
                    mLoadingView.showNoContent(msg);
                }
                //  ToastUtils.show(getActivity(), msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                InvestWayListBean listBean = (InvestWayListBean) responseInfo.getResult();
                if (null != listBean && null != listBean.getData() && !listBean.getData().isEmpty())
                {
                    mLoadingView.hide();
                    setData(listBean);
                } else
                {
                    mLoadingView.showNoContent("暂未有投资方案数据");
                }
            }
        });
    }

    private void setData(InvestWayListBean listBean)
    {
        mApplyAdapter.setNewData(listBean.getData());
    }


    private void initRV()
    {
        mApplyAdapter = new ApplyAdapter(R.layout.seven_item_layout, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.gray_shape, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mApplyAdapter);

        mApplyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()==R.id.six_content){
                    final InvestWayListBean.DataBean bean= mApplyAdapter.getData().get(position);
                    final NewCommonDialog dialog=new NewCommonDialog(getActivity());
                    dialog.setPositiveListener(new NewCommonDialog.DialogPositiveListener() {
                        @Override
                        public void onClick(String inputText, String select) {

                        }

                        @Override
                        public void onSearchClick(String content) {
                            commitApproval(bean.getPlanId()+"");
                        }
                    });
                    dialog.initDialogTips(getActivity(),"","请确定本条信息的准确性！").show();
                }
            }
        });
    }

    public void commitApproval(String eid){
        ListHttpHelper.approvalInvest(getActivity(), eid, new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                if (error.getExceptionCode() == 400)
                {
                    mLoadingView.showNoContent(msg);
                }
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                if (responseInfo.getMsg()!=null) {
                    ToastUtils.show(getActivity(), responseInfo.getMsg());
                }else{
                    ToastUtils.show(getActivity(), "审批成功");
                }

                getNetData();
            }
        });
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class ApplyAdapter extends BaseQuickAdapter<InvestWayListBean.DataBean, BaseViewHolder>
    {


        public ApplyAdapter(@LayoutRes int layoutResId, @Nullable List<InvestWayListBean.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, InvestWayListBean.DataBean item)
        {
            holder.setText(R.id.item_title_text, null == item.getStatus() ? "方案类型：" : "方案类型：" + item.getStatus())
                    .setText(R.id.item_time, null == item.getPlanDate() ? "" : item.getPlanDate() + "")
                    .setText(R.id.one_content, null == item.getPlanDesc() ? "" : item.getPlanDesc())
                    .setText(R.id.two_content, null == item.getCurrency() ? "" : item.getCurrency())
                    .setText(R.id.three_content, null == item.getAmt() ? "" : item.getAmt())
                    .setText(R.id.four_content, null == item.getApprovedFlag() ? "" : item.getApprovedFlag())
                    .setText(R.id.five_content, null == item.getEditByName() ? "" : item.getEditByName())
                    .setText(R.id.six_content, null == item.getApprovedByName() ? "" : item.getApprovedByName());


            if (item.getApprovedByName() != null && item.getApprovedByName().length() > 0){
                holder.setText(R.id.six_content, null == item.getApprovedByName() ? "" : item.getApprovedByName());
                holder.setTextColor(R.id.six_content,0xff333333);
            }else{
                if (item.getTeamName()!=null && item.getTeamName().size()>0 ) {
                    String account=DisplayUtil.getUserInfo(getActivity(),8);
                    for (int i = 0; i < item.getTeamName().size(); i++) {
                        if (item.getTeamName().get(i).equals(account)){
                            holder.setTextColor(R.id.six_content,0xfff70909);
                            holder.setText(R.id.six_content,"我要审批");
                            holder.addOnClickListener(R.id.six_content);
                        }
                    }
                }
            }

        }
    }


}
