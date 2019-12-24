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
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
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
import com.utils.DialogImUtils;
import com.utils.SDToast;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanDirector;
import newProject.company.invested_project.bean.BeanDirector;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.invested_project.editinfo.ActivityDirectorInfo;
import newProject.company.invested_project.web.ProjectWebVacationActivity;
import newProject.company.vacation.WebVacationActivity;
import newProject.view.DialogTextFilter;
import yunjing.processor.eventbus.UnReadCommon;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.DialogFragmentProject;
import yunjing.view.StatusTipsView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.injoy.idg.R.id.rl_shrink;
import static com.injoy.idg.R.id.view;

/**
 * 董事会信息
 */
public class FragmentDirectorInfo extends BaseFragment
{
    @Bind(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;
    private InverstmentAdapter mAdapter;
    private List<BeanDirector.DataBeanX.DataBean> mDataLists = new ArrayList<>();
    private String mEid;
    private String mTitle = "";
    private String userName = "";

    private int pageNo = 1;
    private int pageSize = 10;

    public static Fragment newInstance(String eid)
    {
        Bundle args = new Bundle();
        args.putString(Constants.PROJECT_EID, eid);
        FragmentDirectorInfo fragment = new FragmentDirectorInfo();
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

    public void reFresh()
    {
        pageNo = 1;
        getNetData();
    }

    private void getNetData()
    {
        ListHttpHelper.postDirectorList(getActivity(), mEid, pageNo + "", pageSize + "", userName,
                new SDRequestCallBack(BeanDirector.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        mLoadingView.showNoContent("暂无数据");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanDirector listBean = (BeanDirector) responseInfo.getResult();
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
        mAdapter = new InverstmentAdapter(R.layout.item_investment_director_info, mDataLists);
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
                BeanDirector.DataBeanX.DataBean bean = mAdapter.getData().get(position);
                if (StringUtils.notEmpty(bean.getFileInfoList().size()) && bean.getFileInfoList().size() > 0)
                {
                    if (bean.getFileInfoList().size() > 1)
                    {
                        showFileChoose(bean.getFileInfoList());
                    } else
                    {
                        String urlString = HttpURLUtil.newInstance().append("iceforce/preview/file/view/holder.htm?fileId=")
                                .toString() + bean.getFileInfoList().get(0).getFileId() + "&username=" + userName +
                                "&fileName=" + bean.getFileInfoList().get(0).getFileName();
                        if (true)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", bean.getFileInfoList().get(0).getFileName());
                            bundle.putString("URL", urlString);
                            Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else
                        {
//                            final TextView tv_progress = (TextView) view.findViewById(R.id.tv_progress);
//                            File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
//                            if (tmpFile.exists())
//                            {
//
//                            } else
//                            {
//                                tv_progress.setText("下载中...");
//                            }
//                            String unCodeFileName = "";
//                            if (StringUtils.notEmpty(dataBean.getFileName()))
//                                unCodeFileName = dataBean.getFileName();
//                            if (StringUtils.notEmpty(dataBean.getFileName()))
//                            {   //后台要求必须转码两次。
//                                try
//                                {
//                                    unCodeFileName = URLEncoder.encode(URLEncoder.encode(dataBean.getFileName(), "utf-8"),
//                                            "utf-8");
//
//                                } catch (UnsupportedEncodingException e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            } else
//                            {
//                                unCodeFileName = "";
//                            }
//                            urlString = HttpURLUtil.newInstance().append("iceforce/down/file/download/holder.htm?fileId=")
//                                    .toString() + dataBean.getFileId() + "&username=" + userName + "&fileName=" +
// unCodeFileName;
//
//                            FileDownLoadUtils.getInstance().downLoadFileOpen(getActivity(), urlString, dataBean.getFileName(),
//                                    tv_progress, new FileDownLoadUtils.OnYesOrNoListener()
//                                    {
//                                        @Override
//                                        public void onYes(File response)
//                                        {
//                                            tv_progress.setText("下载完成");
//                                            if (response.exists())
//                                            {
//                                                FileUtill.openFile(response, getActivity());
//                                            } else
//                                            {
//                                                MyToast.showToast(getActivity(), "文件不存在");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onNo(Request request, Exception e)
//                                        {
//                                            tv_progress.setText("下载失败");
//                                            MyToast.showToast(getActivity(), "下载失败，请稍后再试");
//                                        }
//                                    });
                        }
                    }
                } else
                {
                    DialogTextFilter dialogTextFilter = new DialogTextFilter();
                    dialogTextFilter.setTitleString("提 示");
                    dialogTextFilter.setYesString("确定");
                    dialogTextFilter.setHaveBtn(1);
                    dialogTextFilter.setContentString("暂无相关文件");

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
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                BeanDirector.DataBeanX.DataBean bean = mAdapter.getData().get(position);
                if (view.getId() == R.id.rl_shrink)
                {
                    Intent intent = new Intent(getActivity(), ActivityDirectorInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PROJECT_EID, bean.getProjId());
                    bundle.putString(Constants.PROJECT_NAME, bean.getProjName());
                    bundle.putString(Constants.COMMON_INFO, SDGson.toJson(bean));
                    bundle.putBoolean(Constants.TYPE_EDIT, true);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, mRequestCode8);
                }
            }
        });
    }

    public static int mRequestCode8 = 1008;
    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    private void showFileChoose(List<BeanDirector.DataBeanX.DataBean.FileInfoListBean> dataBeanList)
    {
        beanIceProjectList.clear();
        for (int i = 0; i < dataBeanList.size(); i++)
        {
            beanIceProjectList.add(new BeanIceProject(0, dataBeanList.get(i).getFileId(), dataBeanList.get(i).getFileName()));
        }

        BaseDialogUtils.showDialogProject(getActivity(), false, false, false, "文件", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        String urlString = HttpURLUtil.newInstance().append("iceforce/preview/file/view/holder.htm?fileId=")
                                .toString() + content.getKey() + "&username=" + userName +
                                "&fileName=" + content.getValue();
                        if (true)
                        {
                            Bundle bundle = new Bundle();
                            bundle.putString("TITLE", content.getValue());
                            bundle.putString("URL", urlString);
                            Intent intent = new Intent(getActivity(), WebVacationActivity.class);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else
                        {
//                            final TextView tv_progress = (TextView) view.findViewById(R.id.tv_progress);
//                            File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
//                            if (tmpFile.exists())
//                            {

//
//                            } else
//                            {
//                                tv_progress.setText("下载中...");
//                            }
//                            String unCodeFileName = "";
//                            if (StringUtils.notEmpty(dataBean.getFileName()))
//                                unCodeFileName = dataBean.getFileName();
//                            if (StringUtils.notEmpty(dataBean.getFileName()))
//                            {   //后台要求必须转码两次。
//                                try
//                                {
//                                    unCodeFileName = URLEncoder.encode(URLEncoder.encode(dataBean.getFileName(), "utf-8"),
//                                            "utf-8");
//
//                                } catch (UnsupportedEncodingException e)
//                                {
//                                    e.printStackTrace();
//                                }
//                            } else
//                            {
//                                unCodeFileName = "";
//                            }
//                            urlString = HttpURLUtil.newInstance().append("iceforce/down/file/download/holder.htm?fileId=")
//                                    .toString() + dataBean.getFileId() + "&username=" + userName + "&fileName=" +
// unCodeFileName;
//
//                            FileDownLoadUtils.getInstance().downLoadFileOpen(getActivity(), urlString, dataBean.getFileName(),
//                                    tv_progress, new FileDownLoadUtils.OnYesOrNoListener()
//                                    {
//                                        @Override
//                                        public void onYes(File response)
//                                        {
//                                            tv_progress.setText("下载完成");
//                                            if (response.exists())
//                                            {
//                                                FileUtill.openFile(response, getActivity());
//                                            } else
//                                            {
//                                                MyToast.showToast(getActivity(), "文件不存在");
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onNo(Request request, Exception e)
//                                        {
//                                            tv_progress.setText("下载失败");
//                                            MyToast.showToast(getActivity(), "下载失败，请稍后再试");
//                                        }
//                                    });
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


    private class InverstmentAdapter extends BaseQuickAdapter<BeanDirector.DataBeanX.DataBean, BaseViewHolder>
    {

        public InverstmentAdapter(@LayoutRes int layoutResId, @Nullable List<BeanDirector.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanDirector.DataBeanX.DataBean item)
        {
            String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
            if (StringUtils.notEmpty(myNickName))
            {
                ImageUtils.waterMarking(getActivity(), true, false, holder.getView(R.id.ll_water), myNickName);
            }

            holder.setText(R.id.tv_title, item.getContent());
            holder.setText(R.id.item_one, DateUtils.getDate("yyyy-MM-dd", item.getMeetingDateStr()));
            if (StringUtils.notEmpty(item.getImportantFlag()) && item.getImportantFlag() == 1)
            {
                holder.setText(R.id.item_two, "是");
            } else
            {
                holder.setText(R.id.item_two, "否");
            }
//            holder.setText(R.id.item_two, item.getImportantFlagStr());
            holder.setText(R.id.item_three, item.getDecisionTypeStr());
            holder.setText(R.id.item_four, item.getMeetingDesc());
            holder.addOnClickListener(R.id.rl_shrink);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == mRequestCode8 && resultCode == RESULT_OK)
        {
            getNetData();
        }
    }
}
