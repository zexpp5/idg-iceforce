package newProject.company.invested_project.editinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.group.PopularizeGroupBean;
import com.cxgz.activity.cx.adapter.FolderAdapter;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.http.ImHttpHelper;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.superdata.im.constants.CxIMMessageType;
import com.utils.DialogImUtils;
import com.utils.DialogUtilsIm;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.fileLib.ActivityFileTypeChoose;
import newProject.company.fileLib.BeanFileLib;
import newProject.company.invested_project.ActivityQxb;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.BeanSearchFile;
import newProject.utils.HttpHelperUtils;
import newProject.view.DialogTextFilter;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFileType;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.media.CamcorderProfile.get;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.view;

/**
 * 文档库-个人文档
 */
public class ActivityPersonalFileLib extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
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
    private int pageSize = 100;

    private String userName = "";

    private void getIn()
    {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            fileList = (List<BeanSearchFile>) bundle.getSerializable(Constants.TYPE_LIST);
        }
        userName = loginUserAccount;
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_investment_file_choose;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        titleBar.setMidText("个人文档库");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                DialogTextFilter dialogTextFilter = new DialogTextFilter();
                dialogTextFilter.setTitleString("提 示");
                dialogTextFilter.setYesString("确定");
                dialogTextFilter.setNoString("取消");
                dialogTextFilter.setContentString("放弃当前的选择?");

                DialogImUtils.getInstance().showCommonDialog(ActivityPersonalFileLib.this, dialogTextFilter, new
                        DialogImUtils.OnYesOrNoListener()
                        {
                            @Override
                            public void onYes()
                            {
                                finish();
                            }

                            @Override
                            public void onNo()
                            {

                            }
                        });
            }
        });

        titleBar.setRightTextVisible(true);
        titleBar.setRightText("确定");
        titleBar.setRightTextOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TYPE_LIST, (Serializable) fileList);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                ActivityPersonalFileLib.this.finish();
            }
        });


        getIn();
        initRefresh();
        initAdapter();
        getNetData();
    }

    public void initRefresh()
    {
        mRefreshLayout.setEnableRefresh(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                pageNo = 1;
                queryString = "";
                if (DisplayUtil.hasNetwork(ActivityPersonalFileLib.this))
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
                if (DisplayUtil.hasNetwork(ActivityPersonalFileLib.this))
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

        if (!DisplayUtil.hasNetwork(ActivityPersonalFileLib.this))
        {
            mLoadingView.showAccessFail();
        }
    }

    private void getNetData()
    {
        ListHttpHelper.postPersonalFileLibrary(ActivityPersonalFileLib.this, queryString, pageNo + "", pageSize + "", userName,
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
        if (fileList.size() > 0)
        {
            for (int i = 0; i < fileList.size(); i++)
            {
                BeanSearchFile beanSearchFile = fileList.get(i);

                Iterator<BeanFileLib.DataBeanX.DataBean> iterator = mDataLists.iterator();
                while (iterator.hasNext())
                {
                    BeanFileLib.DataBeanX.DataBean dataBean = iterator.next();
                    if (dataBean.getFileId().equals(beanSearchFile.getEid()))
                    {
                        dataBean.setCheck(true);
                    }
                }
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    private List<BeanSearchFile> fileList = new ArrayList<>();

    private void initAdapter()
    {
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_file_lib_choose, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(ActivityPersonalFileLib.this,
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, ScreenUtils.dp2px
                (ActivityPersonalFileLib.this, 15));
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(ActivityPersonalFileLib.this));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
//                getFileType(position);
                if (mDataLists.get(position).isCheck())
                {
                    mDataLists.get(position).setCheck(false);
                    addAndDel(false, mDataLists.get(position).getFileId(), mDataLists.get(position).getFileName());
                } else
                {
                    mDataLists.get(position).setCheck(true);
                    addAndDel(true, mDataLists.get(position).getFileId(), mDataLists.get(position).getFileName());
                }
                mAdapter.notifyDataSetChanged();
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

    private void addAndDel(boolean isDo, String eid, String fileName)
    {
        if (isDo)
        {
            boolean isInT = false;
            for (int i = 0; i < fileList.size(); i++)
            {
                if (eid.equals(fileList.get(i).getEid()))
                {
                    isInT = true;
                }
            }
            if (!isInT)
            {
                fileList.add(new BeanSearchFile(eid, fileName));
            }
        } else
        {
            if (fileList.size() > 0)
            {
                Iterator<BeanSearchFile> iterator = fileList.iterator();
                while (iterator.hasNext())
                {
                    BeanSearchFile beanSearchFile = iterator.next();
                    if (eid.equals(beanSearchFile.getEid()))
                    {
                        iterator.remove();
                    }
                }
            }
        }
    }

    private void getFileType(final int position)
    {
        HttpHelperUtils.getInstance().getType(ActivityPersonalFileLib.this, true, Constants.TYPE_FILE_TYPE, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogFileType(ActivityPersonalFileLib.this, false, false, false, "选择要设置的文档类型",
                            icApprovedList, new
                                    DialogFileType.InputListener()
                                    {
                                        @Override
                                        public void onData(BeanIceProject content)
                                        {
                                            BeanFileLib.DataBeanX.DataBean dataBean = mDataLists.get(position);
                                            Intent intent = new Intent(ActivityPersonalFileLib.this, ActivityFileTypeChoose
                                                    .class);
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
                    MyToast.showToast(ActivityPersonalFileLib.this, "请重新");
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
    protected void onDestroy()
    {
        super.onDestroy();
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

            if (StringUtils.notEmpty(item.isCheck()))
            {
                if (item.isCheck())
                {
                    ((CheckBox) holder.getView(R.id.cb_file)).setChecked(true);
                } else
                {
                    ((CheckBox) holder.getView(R.id.cb_file)).setChecked(false);
                }
            } else
            {
                ((CheckBox) holder.getView(R.id.cb_file)).setChecked(false);
            }

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
