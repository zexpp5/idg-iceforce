package newProject.company.investment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.MyInvestmentAdapter;
import newProject.company.project_manager.investment_project.bean.Top3ListBean;
import yunjing.utils.DisplayUtil;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/9/9.
 */

public class TopDataActivity extends BaseActivity
{
    @Bind(R.id.tv_personal)
    TextView tvPersonal;
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
//    @Bind(R.id.smart_refresh_layout)
//    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;

    String flag = "false";

    MyInvestmentAdapter adapter;
    List<Top3ListBean.DataBeanX.DataBean> personalLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_top_data;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyInvestmentAdapter(datas);
        recyclerView.setAdapter(adapter);
//        initRefresh();
        getTop3Data("false");
        getTop3Data("true");
    }

//    private void initRefresh()
//    {
//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
//        {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout)
//            {
//                if (DisplayUtil.hasNetwork(TopDataActivity.this))
//                {
//                    mPage = 1;
//                    mRefreshLayout.finishRefresh(1000);
//                    mRefreshLayout.setLoadmoreFinished(false);
//                    mRefreshLayout.setEnableLoadmore(true);
//                    getTop3Data(flag);
//                } else
//                {
//                    SDToast.showShort("请检查网络连接");
//                    mRefreshLayout.finishRefresh(1000);
//                }
//            }
//        });
//        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
//        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener()
//        {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout)
//            {
//                if (DisplayUtil.hasNetwork(TopDataActivity.this))
//                {
//                    /*if (adapter.getData().size() >= mBackListSize)
//                    {
//                        mRefreshLayout.finishLoadmore(1000);
//                        SDToast.showShort("没有更多数据了");
//                    } else
//                    {*/
//                    mPage = mPage + 1;
//                    getTop3Data(flag);
//                    mRefreshLayout.finishLoadmore(1000);
//                    //}
//                } else
//                {
//                    SDToast.showShort("请检查网络连接");
//                    mRefreshLayout.finishLoadmore(1000);
//                }
//
//            }
//        });
//    }

    @OnClick({R.id.iv_back, R.id.tv_personal, R.id.tv_group})
    public void OnViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_personal:
                flag = "false";
                mPage = 1;
                datas.clear();
                datas.addAll(personalLists);
                adapter.notifyDataSetChanged();
                tvPersonal.setTextColor(getResources().getColor(R.color.white));
                tvPersonal.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvGroup.setTextColor(getResources().getColor(R.color.top_bg));
                tvGroup.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                getTop3Data(flag);
                break;
            case R.id.tv_group:
                flag = "true";
                mPage = 1;
                datas.clear();
                datas.addAll(groupLists);
                adapter.notifyDataSetChanged();
                tvGroup.setTextColor(getResources().getColor(R.color.white));
                tvGroup.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvPersonal.setTextColor(getResources().getColor(R.color.top_bg));
                tvPersonal.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                getTop3Data(flag);
                break;
        }
    }

    public void getTop3Data(final String flagStr)
    {
        ListHttpHelper.getMyInvestmentTop3Data(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), flagStr, mPage * 5 + "",
                new SDRequestCallBack(Top3ListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        Top3ListBean bean = (Top3ListBean) responseInfo.getResult();
                        if (flagStr.equals("false"))
                        {
                            //个人
                            personalLists.clear();
                            personalLists.addAll(bean.getData().getData());
                            datas.clear();
                            datas.addAll(personalLists);
                            adapter.notifyDataSetChanged();
                        } else if (flagStr.equals("true"))
                        {
                            //小组
                            groupLists.clear();
                            groupLists.addAll(bean.getData().getData());
                        }
                    }
                });
    }

}
