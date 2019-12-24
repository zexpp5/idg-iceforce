package newProject.company.investment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDGson;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.google.gson.reflect.TypeToken;
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
import newProject.company.investment.adapter.TopDataAdapter;
import newProject.company.investment.bean.TopDataBean;
import newProject.company.investment.bean.TotalBean;
import newProject.view.CommonPopWindow;
import yunjing.utils.DisplayUtil;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/12/4.
 */

public class TopData2Activity extends BaseActivity implements CommonPopWindow.ViewClickListener
{
    @Bind(R.id.tv_project_1)
    TextView tvProject1;
    @Bind(R.id.tv_project_2)
    TextView tvProject2;
    @Bind(R.id.tv_project_3)
    TextView tvProject3;
    @Bind(R.id.tv_order)
    TextView tvOrder;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private int mPage = 0;
    private int mBackListSize = 0;

    String flag;
    boolean orderFlag = true;

    TopDataAdapter adapter;
    List<TopDataBean.DataBeanX.DataBean> datas = new ArrayList<>();
    List<TotalBean.DataBeanX.DataBean> totals = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_top_data_2;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TopDataAdapter(datas);
        recyclerView.setAdapter(adapter);
        initRefresh();
        totals = SDGson.toObject(getIntent().getStringExtra("list"),new TypeToken<List<TotalBean.DataBeanX.DataBean>>(){}.getType());
        for (int i = 0; i < totals.size(); i++){
            if (i == 0)
                tvProject1.setText(totals.get(i).getName());
            if (i == 1)
                tvProject2.setText(totals.get(i).getName());
            if (i == 2) {
                tvProject3.setVisibility(View.VISIBLE);
                tvProject3.setText(totals.get(i).getName());
            }

        }
        flag = "manage";
        getTop3Data(flag);
    }
    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(TopData2Activity.this))
                {
                    mPage = 0;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getTop3Data(flag);
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
                if (DisplayUtil.hasNetwork(TopData2Activity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                    mPage = mPage + 1;
                    getTop3Data(flag);
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

    @OnClick({R.id.iv_back, R.id.tv_project_1, R.id.tv_project_2,R.id.tv_project_3,R.id.tv_order})
    public void OnViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_project_1:
                flag = "manage";
                mPage = 0;
                tvProject1.setTextColor(getResources().getColor(R.color.white));
                tvProject1.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvProject2.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject2.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                tvProject3.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject3.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                getTop3Data(flag);
                break;
            case R.id.tv_project_2:
                flag = "team";
                mPage = 0;
                tvProject1.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject1.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                tvProject2.setTextColor(getResources().getColor(R.color.white));
                tvProject2.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvProject3.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject3.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                getTop3Data(flag);
                break;
            case R.id.tv_project_3:
                flag = "group";
                mPage = 0;
                tvProject1.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject1.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                tvProject2.setTextColor(getResources().getColor(R.color.top_bg));
                tvProject2.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                tvProject3.setTextColor(getResources().getColor(R.color.white));
                tvProject3.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                getTop3Data(flag);
                break;
            case R.id.tv_order:
                CommonPopWindow.newBuilder()
                        .setView(R.layout.pop_top_data_order)
                        //.setAnimationStyle(R.style.AnimUp)
                        //.setBackgroundDrawable(new BitmapDrawable())
                        .setSize(dip2px(68), ViewGroup.LayoutParams.WRAP_CONTENT)
                        .setViewOnClickListener(TopData2Activity.this)
                        .setBackgroundDarkEnable(true)
                        .setBackgroundAlpha(1f)
                        .build(TopData2Activity.this)
                        .showAsDown(view);
                break;

        }
    }

    public void getTop3Data(String teamType)
    {
        ListHttpHelper.getProjectTop3Data(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), teamType, mPage+"", "10",orderFlag? "1":"2",
                new SDRequestCallBack(TopDataBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        TopDataBean bean = (TopDataBean) responseInfo.getResult();
                        if (bean != null && bean.getData().getData().size() > 0){
                            mBackListSize = bean.getData().getTotal();
                            if (mPage == 0){
                                datas.clear();
                            }
                            datas.addAll(bean.getData().getData());
                            adapter.notifyDataSetChanged();
                        }else {
                            datas.clear();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public int dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public void getChildView(final PopupWindow mPopupWindow, View view, int mLayoutResId) {
        switch (mLayoutResId) {
            case R.layout.pop_top_data_order:
                TextView tvText = view.findViewById(R.id.tv_text);
                tvText.setText(orderFlag ? "持有价值":"累计投资");
                tvText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        orderFlag = !orderFlag;
                        tvOrder.setText(orderFlag ? "累计投资":"持有价值");
                        getTop3Data(flag);
                        mPopupWindow.dismiss();
                    }
                });
                break;
        }
    }
}
