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
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.FileUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.utils.FileDownload;
import com.utils.FileDownloadUtil;
import com.utils.SDToast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanFile;
import newProject.company.invested_project.bean.BeanMeeting;
import newProject.company.project_manager.investment_project.bean.MyFollowOnNumberBean;
import newProject.company.vacation.WebVacationActivity;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.utils.ToastUtils;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.item_four;
import static com.injoy.idg.R.id.textView;
import static com.injoy.idg.R.id.view;

/**
 * 相关附件
 */
public class FragmentInvestmentFile extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentProjectFileAdapter mAdapter;
    private List<BeanFile.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid;

    private int pageNo = 1;
    private int pageSize = 10;

    private String userName = "";

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentInvestmentFile fragment = new FragmentInvestmentFile();
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
        ListHttpHelper.postInvestementFile(getActivity(), mEid, pageNo + "", pageSize + "", loginUserName,
                new SDRequestCallBack(BeanFile.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanFile listBean = (BeanFile) responseInfo.getResult();
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
        mAdapter = new InverstmentProjectFileAdapter(R.layout.item_investment_file, mDataLists);
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(getContext(),
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider2, 0);
        mRecyclerview.addItemDecoration(dividerItemDecoration2);
        mRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerview.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            //            http://125.35.46.20:8081/idg-api/iceforce/down/file/view/holder
//            // .htm?fileId=b7bad3fc00b649b1b1d250354e8c0ccb&username=yi_zhang            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                BeanFile.DataBeanX.DataBean dataBean = mAdapter.getData().get(position);
                String urlString = "";
                boolean isTrue = FileUtils.getInstance().reMP3File(dataBean.getFileType());
                if (isTrue)
                {
                    urlString = HttpURLUtil.newInstance().append("iceforce/preview/file/view/holder.htm?fileId=")
                            .toString() + dataBean.getFileId() + "&username=" + userName + "&fileName=" + dataBean.getFileName();
                    Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("URL", urlString);
                    bundle.putString("TITLE", dataBean.getFileName());
                    bundle.putBoolean("SHARE", false);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else
                {
                    final TextView tv_progress = (TextView) view.findViewById(R.id.tv_progress);
                    File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
                    if (tmpFile.exists())
                    {

                    } else
                    {
                        tv_progress.setText("下载中...");
                    }
                    String unCodeFileName = "";
                    if (StringUtils.notEmpty(dataBean.getFileName()))
                        unCodeFileName = dataBean.getFileName();
                    if (StringUtils.notEmpty(dataBean.getFileName()))
                    {   //后台要求必须转码两次。
                        try
                        {
                            unCodeFileName = URLEncoder.encode(URLEncoder.encode(dataBean.getFileName(), "utf-8"),
                                    "utf-8");

                        } catch (UnsupportedEncodingException e)
                        {
                            e.printStackTrace();
                        }
                    } else
                    {
                        unCodeFileName = "";
                    }
                    urlString = HttpURLUtil.newInstance().append("iceforce/down/file/download/holder.htm?fileId=")
                            .toString() + dataBean.getFileId() + "&username=" + userName + "&fileName=" + unCodeFileName;

                    FileDownLoadUtils.getInstance().downLoadFileOpen(getActivity(), urlString, dataBean.getFileName(),
                            tv_progress, new FileDownLoadUtils.OnYesOrNoListener()
                            {
                                @Override
                                public void onYes(File response)
                                {
                                    tv_progress.setText("下载完成");
                                    if (response.exists())
                                    {
                                        FileUtill.openFile(response, getActivity());
                                    } else
                                    {
                                        MyToast.showToast(getActivity(), "文件不存在");
                                    }
                                }

                                @Override
                                public void onNo(Request request, Exception e)
                                {
                                    tv_progress.setText("下载失败");
                                    MyToast.showToast(getActivity(), "下载失败，请稍后再试");
                                }
                            });
                }
            }
        });
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()

        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
//                if (view.getId() == )
//                {
//                }
            }
        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class InverstmentProjectFileAdapter extends BaseQuickAdapter<BeanFile.DataBeanX.DataBean, BaseViewHolder>
    {

        public InverstmentProjectFileAdapter(@LayoutRes int layoutResId, @Nullable List<BeanFile.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanFile.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }
//            String s = new String(item.getFileName());
//            String str1 = s.substring(0, s.indexOf("."));
            holder.setText(R.id.tv_progress, "点击查看");
            if (StringUtils.notEmpty(item.getBusTypeStr()))
                holder.setText(R.id.tv_title, item.getBusTypeStr());
            if (StringUtils.notEmpty(item.getCreateDate()))
                holder.setText(R.id.item_one, DateUtils.getDate("yyyy-MM-dd", item.getCreateDate()));
            if (StringUtils.notEmpty(item.getBusTypeStr()))
                holder.setText(R.id.item_two, item.getBusTypeStr());
            if (StringUtils.notEmpty(item.getFileName()))
                holder.setText(R.id.item_three, item.getFileName());
            if (StringUtils.notEmpty(item.getCreateByUser()))
                holder.setText(R.id.item_four, item.getCreateByUser());
        }
    }
}
