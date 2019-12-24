package newProject.company.fileLib;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.ScreenUtils;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.utils.HttpHelperUtils;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.DialogFileType;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 文档库-个人文档
 */
public class FragmentPersonalFileLib extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectFileAdapter mAdapter;
    private List<BeanFileLib.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String queryString = "";

    private int pageNo = 1;
    private int pageSize = 10;

    private String userName = "";

//    public static Fragment newInstance(String eid)
//    {
//        Bundle args = new Bundle();
//        args.putString(Constants.PROJECT_EID, eid);
//        FragmentFileLib fragment = new FragmentFileLib();
//        fragment.setArguments(args);
//        return fragment;
//    }

    private void getIn()
    {
        Bundle bundle = getArguments();
        if (bundle != null)
        {

        }
        userName = loginUserName;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_investment_file;
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
                queryString = "";
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
                queryString = "";
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
        ListHttpHelper.postPersonalFileLibrary(getActivity(), queryString, pageNo + "", pageSize + "", loginUserName,
                new SDRequestCallBack(BeanFileLib.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanFileLib listBean = (BeanFileLib) responseInfo.getResult();
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
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_file_lib_main, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, ScreenUtils.dp2px(getActivity(), 15));
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                getFileType(position);
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()

        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
//                if (view.getId() == R.id.)
//                {
//                }
            }
        });
    }

    private void getFileType(final int position)
    {
        HttpHelperUtils.getInstance().getType(getActivity(), true, Constants.TYPE_FILE_TYPE, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogFileType(getActivity(), false, false, false, "选择要设置的文档类型", icApprovedList, new
                            DialogFileType.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    BeanFileLib.DataBeanX.DataBean dataBean = mDataLists.get(position);
                                    Intent intent = new Intent(getActivity(), ActivityFileTypeChoose.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("fileId", dataBean.getFileId());
                                    bundle.putString("busType", content.getKey());
                                    bundle.putString("busTypeStr", content.getValue());
                                    intent.putExtras(bundle);
                                    startActivityForResult(intent, 100);
                                }
                            });
                } else
                {
                    MyToast.showToast(getActivity(), "请重新");
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK)
        {
            pageNo = 1;
            getNetData();
        }
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<BeanFileLib.DataBeanX.DataBean, BaseViewHolder>
    {
        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<BeanFileLib.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanFileLib.DataBeanX.DataBean item)
        {
//            if (holder.getLayoutPosition() == 0)
//            {
//                holder.getView(R.id.rl_line).setVisibility(View.GONE);
//            } else
//            {
//                holder.getView(R.id.rl_line).setVisibility(View.VISIBLE);
//            }
//            if (StringUtils.notEmpty(item.getProjName()))
//                holder.setText(R.id.tv_title, StringUtil.returnSharp(item.getProjName()));
            if (StringUtils.notEmpty(item.getFileName()))
                holder.setText(R.id.tv_content, item.getFileName());
        }
    }

    public void setQueryString(String queryStr)
    {
        queryString = queryStr;
        reFresh();
    }

    public void reFresh()
    {
        pageNo = 1;
        getNetData();
    }

}
