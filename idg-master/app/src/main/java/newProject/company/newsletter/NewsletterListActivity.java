package newProject.company.newsletter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.cxgz.activity.cxim.utils.FileDownLoadUtils;
import com.cxgz.activity.cxim.utils.FileUtill;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.http.HttpURLUtil;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.squareup.okhttp.Request;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.utils.ImUtils;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.newsletter.bean.NewsLetterListBean;
import newProject.company.project_manager.investment_project.SearchListActivity;
import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;
import newProject.company.vacation.WebVacationActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class NewsletterListActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener
{

    private final int REQUEST_SEARCH = 231;
    @Bind(R.id.recycler_view)
    RecyclerView recycler_view;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private int mPage = 1;
    private int mBackListSize = 0;

    NewsLetterAdapter newsLetterAdapter;
    private String groupId = "", docName = "";
    private String title;
    private String typeSearch = "";
    private String typeGroup = "";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_new_sletter_list;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        title = "Newsletter";
        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (v == mNavigatorBar.getRihtSecondImage())
                {
                    Intent intent = new Intent(NewsletterListActivity.this, SearchListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("TYPE", 7);
                    bundle.putString("TITLE", title);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_SEARCH);
                } else
                {
                    finish();
                }
            }
        };
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setRightSecondImageOnClickListener(onClickListener);
        mNavigatorBar.setMidText(title);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));


        newsLetterAdapter = new NewsLetterAdapter(new ArrayList<NewsLetterItemListBean.DataBeanX.DataBean>());
        recycler_view.setAdapter(newsLetterAdapter);
        initRefresh();


        mTips = (StatusTipsView) findViewById(R.id.loading_view);
        mTips.showLoading();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recycler_view != null)
                {
                    recycler_view.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                getData();
            }
        });
        getData();
        newsLetterAdapter.setOnItemClickListener(this);
    }

    private void initRefresh()
    {
        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(NewsletterListActivity.this))
                {
                    if (newsLetterAdapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishRefresh(1000);
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishRefresh(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }

            }
        });
//        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
//        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
//        {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout)
//            {
//                if (DisplayUtil.hasNetwork(NewsletterListActivity.this))
//                {
//                    if (projectEstateAdapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {
//                        mPage = mPage + 1;
//                        getData();
//                        mRefreshLayout.finishLoadmore(1000);
//                    }
//                } else
//                {
//                    SDToast.showShort("请检查网络连接");
//                    mRefreshLayout.finishLoadmore(1000);
//                }
//
//            }
//        });
    }

    /**
     * 获取网络数据
     */
    public void getData()
    {
        ListHttpHelper.getGroupData(this, groupId, docName, mPage + "", "10", false, new ListHttpHelper.ResCallBack()
        {
            @Override
            public void reTurnData(NewsLetterItemListBean newsLetterItemListBean)
            {
                NewsLetterItemListBean bean = newsLetterItemListBean;
                if (StringUtils.notEmpty(bean) && StringUtils.notEmpty(bean.getData()) && StringUtils.notEmpty(bean.getData()
                        .getData()))
                {
                    List<NewsLetterItemListBean.DataBeanX.DataBean> dataBeanList = bean.getData().getData();

                    mTips.hide();
                    if (mPage == 1)
                    {
                        newsLetterAdapter.getData().clear();
                        mBackListSize = bean.getData().getTotal();
                        if (dataBeanList.size() > 0)
                        {
                        } else
                        {
                            mTips.showNoContent("暂无数据");
                        }
                    }
                    for (NewsLetterItemListBean.DataBeanX.DataBean dataBean : dataBeanList)
                    {
                        newsLetterAdapter.getData().add(0, dataBean);
                    }
                    newsLetterAdapter.notifyDataSetChanged();

                    if (mPage == 1)
                    {
                        recycler_view.scrollToPosition(newsLetterAdapter.getItemCount() - 1);
                    }
                }
            }
        });
    }

    /**
     * 点击详情
     *
     * @param adapter
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position)
    {
        String username = DisplayUtil.getUserInfo(this, 11);
        String urlString = HttpURLUtil.newInstance().append("iceforce/preview/file/readFile/holder.htm?fileId=").toString() +
                newsLetterAdapter.getItem(position).getFileId() + "&username=" + username;
        if (true)
        {
            Bundle bundle = new Bundle();
            bundle.putString("TITLE", newsLetterAdapter.getItem(position).getDocName());
            bundle.putString("URL", urlString);
            Intent intent = new Intent(this, WebVacationActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        } else
        {
//            final TextView tv_progress = (TextView) view.findViewById(R.id.tv_progress);
//            File tmpFile = new File(Config.CACHE_FILE_DIR, dataBean.getFileName());
//            if (tmpFile.exists())
//            {
//
//            } else
//            {
//                tv_progress.setText("下载中...");
//            }
//            String unCodeFileName = "";
//            if (StringUtils.notEmpty(dataBean.getFileName()))
//                unCodeFileName = dataBean.getFileName();
//            if (StringUtils.notEmpty(dataBean.getFileName()))
//            {    //后台要求必须转码两次。
//                try
//                {
//                    unCodeFileName = URLEncoder.encode(URLEncoder.encode(dataBean.getFileName(), "utf-8"),
//                            "utf-8");
//
//                } catch (UnsupportedEncodingException e)
//                {
//                    e.printStackTrace();
//                }
//            } else
//            {
//                unCodeFileName = "";
//            }
//            urlString = HttpURLUtil.newInstance().append("iceforce/down/file/download/holder.htm?fileId=")
//                    .toString() + dataBean.getFileId() + "&username=" + userName + "&fileName=" + unCodeFileName;
//
//            FileDownLoadUtils.getInstance().downLoadFileOpen(getActivity(), urlString, dataBean.getFileName(),
//                    tv_progress, new FileDownLoadUtils.OnYesOrNoListener()
//                    {
//                        @Override
//                        public void onYes(File response)
//                        {
//                            tv_progress.setText("下载完成");
//                            if (response.exists())
//                            {
//                                FileUtill.openFile(response, getActivity());
//                            } else
//                            {
//                                MyToast.showToast(getActivity(), "文件不存在");
//                            }
//                        }
//
//                        @Override
//                        public void onNo(Request request, Exception e)
//                        {
//                            tv_progress.setText("下载失败");
//                            MyToast.showToast(getActivity(), "下载失败，请稍后再试");
//                        }
//                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || requestCode != REQUEST_SEARCH || data == null)
        {
            return;
        }
        Bundle extras = data.getExtras();
        typeGroup = extras.getString(SearchListActivity.DEPTID, "");
        typeSearch = extras.getString(SearchListActivity.SEARCH_CONTENT, "");
        if (!TextUtils.isEmpty(typeGroup) || !TextUtils.isEmpty(typeSearch))
        {
            newsLetterAdapter.getData().clear();
            mBackListSize = 0;
            docName = typeSearch;
            mPage = 1;
            groupId = typeGroup;
            getData();
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

}
