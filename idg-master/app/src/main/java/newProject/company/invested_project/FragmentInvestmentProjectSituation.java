package newProject.company.invested_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.bean.TabLayoutBtnMessage;
import newProject.company.invested_project.bean.BeanProSituation;
import newProject.company.invested_project.editinfo.ActivityProjectInfo;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.loading_view;

/**
 * 项目信息
 */
public class FragmentInvestmentProjectSituation extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectSiuationAdapter mAdapter;
    List<BeanProSituation.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid = "";
    private String userName = "";

    private boolean isShowEdit = false;

    private int mRequestCode = 1001;

    String[] mItemTitles = {"业务介绍", "财务数据", "技术特点"
            , "投资亮点", "团队介绍", "股权结构"
            , "市场规模", "门槛/竞争者"
            , "同类公司", "退出方式", "投资风险"};

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentInvestmentProjectSituation fragment = new FragmentInvestmentProjectSituation();
        fragment.setArguments(args);
        return fragment;
    }

    private void setIsShow(boolean isShow, String msg)
    {
        if (isShow)
        {
            mLoadingView.hide();
        } else
        {
            mLoadingView.showNoContent(msg);
        }
    }

    private void getIn()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            mEid = bundle.getString(Constants.PROJECT_EID, "");
        }

        userName = loginUserName;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_investment_project_situation;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        getIn();
        initRefresh();
        initData();

        if (!DisplayUtil.hasNetwork(getContext()))
        {
            mLoadingView.showAccessFail();
        } else
        {
            getNetData();
        }
    }

    public void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
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
                    mRecyclerview.setVisibility(visible ? GONE : VISIBLE);
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
        ListHttpHelper.getInvestementProSiuation(getActivity(), mEid, userName, new SDRequestCallBack(BeanProSituation.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                mLoadingView.showNoContent("暂无数据");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanProSituation listBean = (BeanProSituation) responseInfo.getResult();
                mDataLists.clear();
                if (StringUtils.notEmpty(listBean.getData()) && StringUtils.notEmpty(listBean.getData().getData()) && listBean
                        .getData()
                        .getData().size() > 0)
                {
                    mDataLists.addAll(listBean.getData().getData());
                    mLoadingView.hide();
                    setData(mDataLists);

                } else
                {
                    setData(mDataLists);
                    //没有数据
                    setIsShow(false, "暂无数据");
                }
            }
        });
    }


    private void setData(List<BeanProSituation.DataBeanX.DataBean> listBean)
    {
        if (listBean != null)
        {
            if (listBean.size() > 0)
            {
                Iterator<BeanProSituation.DataBeanX.DataBean> iterator = listBean.iterator();
                while (iterator.hasNext())
                {
                    BeanProSituation.DataBeanX.DataBean next = iterator.next();
                    if (next != null)
                    {
                        next.setShow(true);
//                        if (StringUtils.empty(next.getValue()))
//                        {
//                            iterator.remove();
//                        }
                    }
                }

                if (mDataLists.size() < 1)
                {
                    mLoadingView.showNoContent("暂无数据");
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initData()
    {
        mAdapter = new InverstmentProjectSiuationAdapter(R.layout.item_investment_project_situation, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, final View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.rl_shrink:
                        if (isShowEdit)
                        {
                            Intent intent1 = new Intent(getActivity(), ActivityProjectInfo.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("mTitle", mAdapter.getData().get(position).getName());
                            bundle1.putString("mProjId", mEid);
                            bundle1.putString("mCode", mAdapter.getData().get(position).getCode());
                            bundle1.putString("mContent", mAdapter.getData().get(position).getValue());
                            bundle1.putString("mMidTitle", "编辑");
                            intent1.putExtras(bundle1);
                            startActivityForResult(intent1, mRequestCode);
                        }
                        break;

                    case R.id.rl_shrink2:
                        if (isShowEdit)
                        {
                            Intent intent1 = new Intent(getActivity(), ActivityProjectInfo.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putString("mTitle", mAdapter.getData().get(position).getName());
                            bundle1.putString("mProjId", mEid);
                            bundle1.putString("mCode", mAdapter.getData().get(position).getCode());
                            bundle1.putString("mContent", "");
                            bundle1.putString("mMidTitle", "新增");
                            intent1.putExtras(bundle1);
                            startActivityForResult(intent1, mRequestCode);
                        }
                        break;

                    case R.id.iv:
                        TextView contentView = (TextView) mAdapter.getViewByPosition(mRecyclerview, position, R.id
                                .tv_content);
                        if (contentView.getVisibility() == VISIBLE)
                        {
                            mAdapter.getData().get(position).setShow(false);
                        } else
                        {
                            mAdapter.getData().get(position).setShow(true);
                        }
                        mAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode && resultCode == RESULT_OK)
            getNetData();
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
        EventBus.getDefault().unregister(this);
    }

    private class InverstmentProjectSiuationAdapter extends BaseQuickAdapter<BeanProSituation.DataBeanX.DataBean, BaseViewHolder>
    {
        public InverstmentProjectSiuationAdapter(@LayoutRes int layoutResId, @Nullable List<BeanProSituation.DataBeanX
                .DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanProSituation.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            if (StringUtils.notEmpty(item.getName()))
            {
                holder.setText(R.id.tv_title, item.getName());
            } else
            {
                holder.setText(R.id.tv_title, "");
            }
            if (StringUtils.notEmpty(item.getValue()))
            {
                holder.setText(R.id.tv_content, item.getValue());
            } else
            {
                holder.setText(R.id.tv_content, "");
            }
            holder.addOnClickListener(R.id.rl_shrink);
            holder.addOnClickListener(R.id.rl_shrink2);
            holder.addOnClickListener(R.id.iv);

            if (isShowEdit)
            {
                holder.getView(R.id.rl_shrink).setVisibility(View.VISIBLE);
                holder.getView(R.id.rl_shrink2).setVisibility(View.VISIBLE);
                holder.getView(R.id.iv).setVisibility(View.GONE);

                if (holder.getLayoutPosition() == 0)
                    if (item.getPermission() == 2)
                    {
                        holder.getView(R.id.rl_shrink).setVisibility(View.VISIBLE);
                        holder.getView(R.id.rl_shrink2).setVisibility(View.VISIBLE);
                    } else
                    {
                        holder.getView(R.id.rl_shrink).setVisibility(View.GONE);
                        holder.getView(R.id.rl_shrink2).setVisibility(View.GONE);
                    }
            } else
            {
                holder.getView(R.id.rl_shrink).setVisibility(View.GONE);
                holder.getView(R.id.rl_shrink2).setVisibility(View.GONE);
                holder.getView(R.id.iv).setVisibility(View.VISIBLE);
                if (item.isShow())
                {
                    holder.setBackgroundRes(R.id.iv, R.mipmap.ic_expand3x)
                            .setVisible(R.id.tv_content, true);
                } else
                {
                    holder.setBackgroundRes(R.id.iv, R.mipmap.ic_collapse3x)
                            .setVisible(R.id.tv_content, false);
                }
            }
        }
    }

    public void setItemEdit(boolean isShowEdit)
    {
        this.isShowEdit = isShowEdit;
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MainThread) //默认方式, 在发送线程执行
    public void onTabLayoutBtnEvent(TabLayoutBtnMessage tabLayoutBtnMessage)
    {
        if (null != tabLayoutBtnMessage)
        {
            if (tabLayoutBtnMessage.fgCode.equals("detailInfo"))
            {
                if (tabLayoutBtnMessage.getInvestmentBean().isKey())
                {
                    setItemEdit(true);
                } else
                {
                    setItemEdit(false);
                }
            }
        }
    }

}
