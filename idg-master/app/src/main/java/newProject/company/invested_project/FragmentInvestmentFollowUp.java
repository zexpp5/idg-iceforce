package newProject.company.invested_project;

import android.app.Activity;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanFollowUp;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.DialogFragmentProject;
import yunjing.view.StatusTipsView;


/**
 * 投后跟踪
 */
public class FragmentInvestmentFollowUp extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentFollowUpAdapter mAdapter;
    private List<BeanFollowUp.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid;
    private String mTitle = "";

    private int pageNo = 1;
    private int pageSize = 10;

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentInvestmentFollowUp fragment = new FragmentInvestmentFollowUp();
        fragment.setArguments(args);
        return fragment;
    }

    private void getIn()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_investment_meeting;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);

        getIn();
        initRefresh();
        initAdapter();
        getNetData();
    }

    public void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                pageNo = 1;
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
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
                if (DisplayUtil.hasNetwork(getActivity()))
                {
//                    if (projectLibAdapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {
                    pageNo = pageNo + 1;
                    getNetData();
                    mRefreshLayout.finishLoadmore(1000);
//                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
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
                pageNo = 1;
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postInvestementFllowUp(getActivity(), mEid, pageNo + "", pageSize + "", loginUserName,
                new SDRequestCallBack(BeanFollowUp.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanFollowUp listBean = (BeanFollowUp) responseInfo.getResult();
                        if (pageNo == 1)
                        {
                            if (StringUtils.notEmpty(listBean.getData()) && StringUtils.notEmpty(listBean.getData().getData())
                                    && listBean.getData().getData().size() > 0)
                            {
                                mDataLists.clear();
                                mDataLists.addAll(listBean.getData().getData());
                                mLoadingView.hide();
                                setData();
                            } else
                            {
                                mDataLists.clear();
                                mLoadingView.showNoContent("暂无数据");
                                setData();
                            }
                        } else
                        {
                            if (StringUtils.notEmpty(listBean.getData()) && StringUtils.notEmpty(listBean.getData().getData()))
                                mDataLists.addAll(listBean.getData().getData());
                            setData();
                        }
                    }
                });
    }

    private void setData()
    {
//        if (mDataLists != null)
//        {
//            Iterator<BeanFollowUp.DataBeanX.DataBean> iterator = mDataLists.iterator();
//            while (iterator.hasNext())
//            {
//                BeanFollowUp.DataBeanX.DataBean next = iterator.next();
//                Iterator<BeanFollowUp.DataBeanX.DataBean.ListBean> iterator2 = next.getList().iterator();
//                while (iterator2.hasNext())
//                {
//                    BeanFollowUp.DataBeanX.DataBean.ListBean listBean1 = iterator2.next();
//                    if (StringUtils.empty(listBean1.getIndexValue()))
//                    {
//                        iterator2.remove();
//                    }
//                }
//            }
//
//            Iterator<BeanFollowUp.DataBeanX.DataBean> iterator22 = mDataLists.iterator();
//            while (iterator22.hasNext())
//            {
//                BeanFollowUp.DataBeanX.DataBean next22 = iterator22.next();
//                if (next22.getList().size() < 1)
//                {
//                    iterator22.remove();
//                }
//            }
//
//            if (mDataLists.size() < 1)
//            {
//                mLoadingView.showNoContent("暂无数据");
//            }
//        }

        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter()
    {
        mAdapter = new InverstmentFollowUpAdapter(R.layout.item_investment_follow_up, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);

        mAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position)
            {
                BeanFollowUp.DataBeanX.DataBean dataBean = mAdapter.getData().get(position);
                if (StringUtils.notEmpty(dataBean.getPermission()))
                    if (dataBean.getPermission() == 2)
                    {
                        showEditBtn(dataBean);
                    }
                return false;

            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (view.getId() == R.id.rl_shrink)
                {

                }
            }
        });
    }

    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    private void showEditBtn(final BeanFollowUp.DataBeanX.DataBean dataBean)
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "1", "编辑"));

        BaseDialogUtils.showDialogProject(getActivity(), false, false, false, "", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        String code = content.getKey();
                        if (code.equals("1"))
                        {
                            Intent intent6 = new Intent(getActivity(), ActivityAddFollowUp.class);
                            Bundle bundle6 = new Bundle();
                            bundle6.putString("projId", mEid);
                            bundle6.putString("endDateString", dataBean.getReportDate());
                            bundle6.putString("signalFlag", dataBean.getSignalFlag());
                            bundle6.putString("reportFrequency", dataBean.getReportFrequency());
                            bundle6.putSerializable("list", (Serializable) dataBean);
                            intent6.putExtras(bundle6);
                            startActivityForResult(intent6, InVestedProjectInfoActivity.mRequestCode6);
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

    private class InverstmentFollowUpAdapter extends BaseQuickAdapter<BeanFollowUp.DataBeanX.DataBean, BaseViewHolder>
    {
        public InverstmentFollowUpAdapter(@LayoutRes int layoutResId, @Nullable List<BeanFollowUp.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanFollowUp.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            if (StringUtils.notEmpty(item.getReportDateStr()))
            {
                holder.setText(R.id.tv_title, item.getReportDateStr());
            }

            holder.addOnClickListener(R.id.rl_shrink);

            LinearLayout ll_follow_up_main = (LinearLayout) holder.getView(R.id.ll_follow_up_main);
            ll_follow_up_main.removeAllViews();
            for (int i = 0; i < item.getList().size(); i++)
            {
                View view2 = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.item_child_follow_up,
                        ll_follow_up_main,
                        false);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                view2.setLayoutParams(param);
                TextView textView1 = (TextView) view2.findViewById(R.id.item_one1);
                TextView textView2 = (TextView) view2.findViewById(R.id.item_one);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT, 5.0f));
                textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.WRAP_CONTENT, 2.0f));
                if (StringUtils.notEmpty(item.getList().get(i).getIndexName()))
                    textView1.setText(item.getList().get(i).getIndexName());
                if (StringUtils.notEmpty(item.getList().get(i).getIndexValue()))
                    textView2.setText(item.getList().get(i).getIndexValue());
                ll_follow_up_main.addView(view2);
            }
        }
    }

    public void reFresh()
    {
        mLoadingView.showLoading();
        pageNo = 1;
        getNetData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == InVestedProjectInfoActivity.mRequestCode6 && resultCode == Activity.RESULT_OK)
        {
            reFresh();
        }
    }

}
