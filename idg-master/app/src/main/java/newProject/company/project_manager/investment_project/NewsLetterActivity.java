package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.config.Constants;
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
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.NewsLetterAdapter;
import newProject.company.project_manager.investment_project.adapter.TabsAdapter;
import newProject.company.project_manager.investment_project.bean.NewsLetterItemListBean;
import newProject.company.project_manager.investment_project.bean.ReSearchReportTabsBean;
import newProject.company.vacation.WebVacationActivity;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.baidu.location.d.g.S;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/8.
 */

public class NewsLetterActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tab_recycler_view)
    RecyclerView tabRecyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage = 1;
    private int mBackListSize = 0;

    private int tabIndex = 0;

    TabsAdapter tabsAdapter;
    List<ReSearchReportTabsBean.DataBeanX.DataBean> tabs = new ArrayList<>();
    String groupId = "";
    NewsLetterAdapter adapter;
    List<NewsLetterItemListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String keyword;

    //手指按下的点为(x1, y1)手指离开屏幕的点为(x2, y2)
    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initPopupwindow();
            }
        });
        mNavigatorBar.setMidText("NewsLetter");

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        tabRecyclerView.setLayoutManager(lm);
        tabsAdapter = new TabsAdapter(tabs);
        tabsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                for (int i = 0; i < tabs.size(); i++)
                {
                    tabs.get(i).setFlag(false);
                }
                tabs.get(position).setFlag(true);
                tabsAdapter.notifyDataSetChanged();
                groupId = tabs.get(position).getIndusGroup();
                mPage = 1;
                tabIndex = position;
                getGroupData();
            }
        });
        tabRecyclerView.setAdapter(tabsAdapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsLetterAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Bundle bundle = new Bundle();
                bundle.putString("URL", getURL(datas.get(position).getFileId()));
                bundle.putString("TITLE", datas.get(position).getDocName());
                Intent intent = new Intent(NewsLetterActivity.this, WebVacationActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        if (x1 - x2 > 20)
                        {
                            if (tabIndex < tabs.size() - 1)
                            {
                                tabs.get(tabIndex).setFlag(false);
                                tabIndex++;
                                tabs.get(tabIndex).setFlag(true);
                                tabRecyclerView.smoothScrollToPosition(tabIndex);
                                tabsAdapter.notifyDataSetChanged();
                                groupId = tabs.get(tabIndex).getIndusGroup();
                                getGroupData();
                            }
                            return true;

                        } else if (x2 - x1 > 20)
                        {
                            if (tabIndex > 0)
                            {
                                tabs.get(tabIndex).setFlag(false);
                                tabIndex--;
                                tabs.get(tabIndex).setFlag(true);
                                tabRecyclerView.smoothScrollToPosition(tabIndex);
                                tabsAdapter.notifyDataSetChanged();
                                groupId = tabs.get(tabIndex).getIndusGroup();
                                getGroupData();
                            }
                            return true;
                        } else
                        {
                            return false;
                        }
                }
                return onTouchEvent(motionEvent);
            }
        });
        initRefresh();

        getTabsData();

    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(NewsLetterActivity.this))
                {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getGroupData();
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
                if (DisplayUtil.hasNetwork(NewsLetterActivity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getGroupData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else
                {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getTabsData()
    {
        ListHttpHelper.getNewsLetterTabsData(this, new SDRequestCallBack(ReSearchReportTabsBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                ReSearchReportTabsBean bean = (ReSearchReportTabsBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                {
                    tabs.clear();
                    bean.getData().getData().get(0).setFlag(true);
                    tabs.addAll(bean.getData().getData());
                    tabsAdapter.notifyDataSetChanged();
                    getGroupData();
                }
            }
        });
    }

    public void getGroupData()
    {
        ListHttpHelper.getGroupData(NewsLetterActivity.this, groupId, "", mPage + "", "10", true, new ListHttpHelper.ResCallBack()
        {
            @Override
            public void reTurnData(NewsLetterItemListBean newsLetterItemListBean)
            {
                NewsLetterItemListBean bean = newsLetterItemListBean;
                if (StringUtils.notEmpty(bean) && StringUtils.notEmpty(bean.getData()) && StringUtils.notEmpty(bean.getData()
                        .getData()))
                {
                    mBackListSize = bean.getData().getTotal();
                    if (mPage == 1)
                    {
                        adapter.getData().clear();
                    }
                    adapter.getData().addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public String getURL(String eid)
    {
        String url = Constants.getIp() + "/iceforce/preview/file/readFile/holder.htm?fileId=" + eid + "&username=" + SPUtils
                .get(this, USER_ACCOUNT, "").toString();
        return url;
    }

    private void initPopupwindow()
    {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_research_report_search, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });

        final EditText etKeyWord = (EditText) contentView.findViewById(R.id.et_keyword);
        Button btnReset = (Button) contentView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                etKeyWord.setText("");
            }
        });
        Button btnSearch = (Button) contentView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (StringUtils.notEmpty(etKeyWord.getText()))
                {
                    keyword = etKeyWord.getText().toString();
                    mPage = 1;
                    getGroupData();
                }
                popupWindow.dismiss();
            }
        });

    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_news_letter;
    }
}
