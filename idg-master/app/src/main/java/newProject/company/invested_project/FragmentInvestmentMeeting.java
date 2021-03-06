package newProject.company.invested_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
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
import com.utils.DialogImUtils;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanMeeting;
import newProject.company.invested_project.web.ProjectWebVacationActivity;
import newProject.view.DialogTextFilter;
import yunjing.processor.eventbus.UnReadCommon;
import yunjing.processor.eventbus.UnReadMessage;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.ll_water;

/**
 * 会议讨论
 */
public class FragmentInvestmentMeeting extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectMeetingAdapter mAdapter;
    private List<BeanMeeting.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid;
    private String mTitle = "";
    private String userName = "";

    private int pageNo = 1;
    private int pageSize = 10;


    public static Fragment newInstance(String eid, String company)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        args.putString("title", company);
        FragmentInvestmentMeeting fragment = new FragmentInvestmentMeeting();
        fragment.setArguments(args);
        return fragment;
    }

    private void getIn()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
            mTitle = bundle.getString("title", "会议详情");
        }

        userName = loginUserName;
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
        EventBus.getDefault().register(this);
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
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mLoadingView.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {

            @Override
            public void onRetry()
            {
                pageNo = 1;
                mLoadingView.showLoading();
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
        ListHttpHelper.postInvestementMeeting(getActivity(), mEid, pageNo + "", pageSize + "", userName,
                new SDRequestCallBack(BeanMeeting.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanMeeting listBean = (BeanMeeting) responseInfo.getResult();
                        if (pageNo == 1)
                        {
                            if (StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData().getData().size() > 0)
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
                            if (StringUtils.notEmpty(listBean.getData().getData()) || listBean.getData().getData().size() > 0)
                                mDataLists.addAll(listBean.getData().getData());
                            setData();
                        }
                    }
                });
    }
    
    private void setData()
    {
        mAdapter.notifyDataSetChanged();
    }

    private void initAdapter()
    {
        mAdapter = new InverstmentProjectMeetingAdapter(R.layout.item_investment_meeting, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BeanMeeting.DataBeanX.DataBean bean = mAdapter.getData().get(position);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isUrl", false);
                bundle.putString("URL", bean.getContent());
                bundle.putString("TITLE", "会议详情");
                bundle.putBoolean("SHARE", false);
//                if (StringUtils.notEmpty(bean.getApprovedByStr()))
//                {
//                    bundle.putString("isBtn", "");
//                } else
//                {
                bundle.putString("isBtn", "审批");
//                }
                bundle.putString("id", bean.getId());
                bundle.putString("typeActivity", "FragmentInvestmentMeeting");
                Intent intent = new Intent(getActivity(), ProjectWebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                if (view.getId() == R.id.item_four2)
                {
                    BeanMeeting.DataBeanX.DataBean bean = mAdapter.getData().get(position);
                    if (StringUtils.notEmpty(bean.getApproveOpinion()))
                    {
                        DialogTextFilter dialogTextFilter = new DialogTextFilter();
                        dialogTextFilter.setTitleString("审批结果");
                        dialogTextFilter.setYesString("确定");
                        dialogTextFilter.setHaveBtn(1);
                        dialogTextFilter.setContentString(bean.getApproveOpinion());

                        DialogImUtils.getInstance().showCommonDialog(getActivity(), dialogTextFilter, new
                                DialogImUtils.OnYesOrNoListener()
                                {
                                    @Override
                                    public void onYes()
                                    {

                                    }

                                    @Override
                                    public void onNo()
                                    {

                                    }
                                });
                    } else
                    {
                        MyToast.showToast(getActivity(), "审批内容为空");
                    }

                }
            }
        });
    }

    /**
     * 推送接收
     *
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onUnReadEvent(UnReadCommon info)
    {
        if (info != null)
        {
            if (info.isShow)
            {
                pageNo = 1;
                getNetData();
            }
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }


    private class InverstmentProjectMeetingAdapter extends BaseQuickAdapter<BeanMeeting.DataBeanX.DataBean, BaseViewHolder>
    {

        public InverstmentProjectMeetingAdapter(@LayoutRes int layoutResId, @Nullable List<BeanMeeting.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanMeeting.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            holder.addOnClickListener(R.id.item_four2);
            if (StringUtils.notEmpty(item.getApproveOpinion()))
            {
                holder.getView(R.id.item_four2).setVisibility(View.VISIBLE);
            } else
            {
                holder.getView(R.id.item_four2).setVisibility(View.GONE);
            }
            holder.setText(R.id.tv_title, item.getOpinionTypeStr());
            holder.setText(R.id.item_one, DateUtils.getDate("yyyy-MM-dd", item.getOpinionDate()));
            holder.setText(R.id.item_two, item.getConclusion());
            holder.setText(R.id.item_three, item.getCreateByStr());
            holder.setText(R.id.item_four, item.getApprovedByStr());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10000 && resultCode == RESULT_OK)
        {

        }
    }


}
