package newProject.company.fileLib;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
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
import com.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import newProject.view.DialogTextFilter;
import tablayout.view.textview.FontEditext;
import yunjing.utils.DisplayUtil;
import yunjing.utils.ToastUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DrawText;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static yunjing.utils.ToastUtils.show;

/**
 * 文档库-项目文档
 */
public class ActivityFileTypeChoose extends BaseActivity
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_search)
    FontEditext tvSearch;
    @Bind(R.id.rl_search)
    RelativeLayout rlSearch;
    private InverstmentProjectFileAdapter mAdapter;
    private List<PotentialProjectsPersonalBean.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String queryString = "";

    private int pageNo = 1;
    private int pageSize = 10;

    private String fileId = "";
    private String objectId = "";
    private String busType = "";
    private String busTypeStr = "";

    private String userName = "";

    private void getIn()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            fileId = bundle.getString("fileId");
            busType = bundle.getString("busType");
            busTypeStr = bundle.getString("busTypeStr");
        }
        userName = loginUserAccount;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_file_type_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        titleBar.setMidText("文档库");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

        tvSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if (StringUtils.notEmpty(s.toString()))
                {
                    queryString = s.toString();
                    pageNo = 1;
                    getNetData();
                }
            }
        });

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
                if (DisplayUtil.hasNetwork(ActivityFileTypeChoose.this))
                {
                    getNetData();
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                } else
                {
                    MyToast.showToast(ActivityFileTypeChoose.this, "请检查网络连接");
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
                if (DisplayUtil.hasNetwork(ActivityFileTypeChoose.this))
                {
//                    if (projectLibAdapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        MyToast.showToast(ActivityFileTypeChoose.this, "没有更多数据了");
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
                mLoadingView.showLoading();
                getNetData();
            }
        });

        if (!DisplayUtil.hasNetwork(ActivityFileTypeChoose.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postFileProjName(ActivityFileTypeChoose.this, queryString, pageNo + "", pageSize + "", userName,
                new SDRequestCallBack(PotentialProjectsPersonalBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PotentialProjectsPersonalBean listBean = (PotentialProjectsPersonalBean) responseInfo.getResult();
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
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_file_type_main, mDataLists);
//        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityFileTypeChoose.this,
//                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider_white, ScreenUtils.dp2px
// (ActivityFileTypeChoose
//                .this, 10));
//        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityFileTypeChoose.this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                PotentialProjectsPersonalBean.DataBeanX.DataBean dataBean = mDataLists.get(position);
                objectId = dataBean.getProjId();
                showMeetingFinish(dataBean.getProjName(), busTypeStr);
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

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.rl_search)
    public void onViewClicked()
    {

    }

    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<PotentialProjectsPersonalBean.DataBeanX.DataBean,
            BaseViewHolder>
    {
        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<PotentialProjectsPersonalBean.DataBeanX
                .DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, PotentialProjectsPersonalBean.DataBeanX.DataBean item)
        {
            DrawText drawText = holder.getView(R.id.draw_text);
            drawText.setColor(Color.parseColor("#F70909"));
            if (StringUtils.notEmpty(item.getIndusGroupStr()))
                drawText.setText(item.getIndusGroupStr());
            if (StringUtils.notEmpty(item.getProjName()))
                holder.setText(R.id.tv_name, item.getProjName());
        }
    }

    private void showMeetingFinish(String projName, String projFileType)
    {
        DialogTextFilter dialogTextFilter = new DialogTextFilter();
        dialogTextFilter.setContentString("提示");
        dialogTextFilter.setYesString("同意");
        dialogTextFilter.setNoString("不同意");
        dialogTextFilter.setProjName(projName);
        dialogTextFilter.setProjFileType(projFileType);
        DialogImUtils.getInstance().showFileTypeDialog(ActivityFileTypeChoose.this, dialogTextFilter, new
                DialogImUtils.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        postFileTypeSet();
                    }

                    @Override
                    public void onNo()
                    {

                    }
                });
    }

    private void postFileTypeSet()
    {
        ListHttpHelper.postFileTransToPrj(ActivityFileTypeChoose.this, fileId, objectId, busType, userName, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ActivityFileTypeChoose.this, "设置失败");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (responseInfo.getStatus() == 200)
                        {
                            MyToast.showToast(ActivityFileTypeChoose.this, "设置成功");
                            setResult(RESULT_OK);
                            finish();
                        }
                    }
                });
    }


}
