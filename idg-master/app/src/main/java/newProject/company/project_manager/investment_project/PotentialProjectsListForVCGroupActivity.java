package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.project_manager.investment_project.adapter.PotentialProjectsForVCGroupAdapter;
import newProject.company.project_manager.investment_project.adapter.PotentialProjectsPersonalAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * 潜在项目
 * Created by zsz on 2019/4/11.
 */

public class PotentialProjectsListForVCGroupActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 1;
    private int mBackListSize = 0;
    PotentialProjectsForVCGroupAdapter adapter;

    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> datas = new ArrayList<>();

    String stsId;
    String key;
    String vcSts;
    String dateRange;

    List<IconListBean> popuList = new ArrayList<>();

    @Override
    public void onStart() {
        super.onStart();
        stsId = null;
        key = null;
        vcSts = null;
        dateRange = null;
        getData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_potential_project_list_2;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_work_all);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setMidText("新项目");
        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initlistPopupWindow((View) view.getParent());
            }
        });
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initPopupwindow();
            }
        });
        initPOpupDatas();
        initViews();
    }

    private void initPOpupDatas(){
        popuList.add(new IconListBean(R.mipmap.icon_popup_all,"全部","All"));
        popuList.add(new IconListBean(R.mipmap.icon_popup_one_week,"最近一周","1Week"));
        popuList.add(new IconListBean(R.mipmap.icon_popup_a_month,"最近一月","1Month"));
        popuList.add(new IconListBean(R.mipmap.icon_popup_a_season,"最近三月","3Month"));
        popuList.add(new IconListBean(R.mipmap.icon_popup_important,"跟进项目","1"));
    }

    private void initViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(PotentialProjectsListForVCGroupActivity.this));
        adapter = new PotentialProjectsForVCGroupAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.ll_item:
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjType())) {
                            intent.setClass(PotentialProjectsListForVCGroupActivity.this, PotentialProjectsDetailActivity.class);
                        } else if ("INVESTED".equals(((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjType())) {
                            intent.setClass(PotentialProjectsListForVCGroupActivity.this, InvestedProjectsDetailActivity.class);
                        } else if ("FOLLOW_ON".equals(((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjType())) {
                            intent.setClass(PotentialProjectsListForVCGroupActivity.this, FollowProjectDetailActivity.class);
                        } else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projId",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjId());
                        intent.putExtra("projName",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getProjName());
                        intent.putExtra("permission",((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getPermission());
                        startActivity(intent);
                        break;
                    case R.id.tv_state:
                        mPage = 1;
                        stsId = ((PotentialProjectsPersonalBean.DataBeanX.DataBean)adapter.getData().get(position)).getStsId();
                        key = null;
                        vcSts = null;
                        dateRange = null;
                        getData();
                        break;
                }

            }
        });
        initRefresh();
        mTips.setOnVisibleChangeListener(new StatusTipsView.OnVisibleChangeListener()
        {

            @Override
            public void onVisibleChanged(boolean visible)
            {
                if (recyclerView != null)
                {
                    recyclerView.setVisibility(visible ? GONE : VISIBLE);
                }
            }
        });

        mTips.setOnRetryListener(new StatusTipsView.OnRetryListener()
        {
            @Override
            public void onRetry()
            {
                mTips.showLoading();
                stsId = null;
                key = null;
                vcSts = null;
                dateRange = null;
                getData();
            }
        });

    }

    private void initRefresh()
    {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                if (DisplayUtil.hasNetwork(PotentialProjectsListForVCGroupActivity.this))
                {
                    mPage = 1;
                    stsId = null;
                    key = null;
                    vcSts = null;
                    dateRange = null;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
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
                if (DisplayUtil.hasNetwork(PotentialProjectsListForVCGroupActivity.this))
                {
                    if (adapter.getData().size() >= mBackListSize)
                    {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else
                    {
                        mPage = mPage + 1;
                        getData();
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

    public void getData(){

            ListHttpHelper.getPotentialPersonalListForVCGroup(PotentialProjectsListForVCGroupActivity.this, SPUtils.get(PotentialProjectsListForVCGroupActivity.this, USER_ACCOUNT, "").toString(), mPage + "", "10",  key, "", "", stsId,"",vcSts,dateRange, new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
                @Override
                public void onRequestFailure(HttpException error, String msg) {
                    mTips.showAccessFail();
                }

                @Override
                public void onRequestSuccess(SDResponseInfo responseInfo) {
                    PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                    mBackListSize = bean.getData().getTotal();
                    if (mPage == 1) {
                        adapter.getData().clear();
                        if (bean.getData().getData().size() > 0) {
                            mTips.hide();
                        } else {
                            mTips.showNoContent("暂无数据");
                        }
                    }
                    adapter.getData().addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                }
            });
    }

    public void initPopupWindow(View view,String flag,final String projId, final int pos){

        View contentView = LayoutInflater.from(PotentialProjectsListForVCGroupActivity.this).inflate(R.layout.popupwindow_star, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        LinearLayout llHalf = (LinearLayout) contentView.findViewById(R.id.ll_half);
        llHalf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"1",pos);
                popupWindow.dismiss();
            }
        });
        LinearLayout llAll = (LinearLayout) contentView.findViewById(R.id.ll_all);
        llAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"2",pos);
                popupWindow.dismiss();
            }
        });
        LinearLayout llHollow = (LinearLayout) contentView.findViewById(R.id.ll_hollow);
        llHollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFollowUpStatus(projId,"0",pos);
                popupWindow.dismiss();
            }
        });
        if (StringUtils.notEmpty(flag)) {
            if (flag.equals("1")) {
                llHalf.setVisibility(GONE);
                llAll.setVisibility(VISIBLE);
                llHollow.setVisibility(VISIBLE);
            } else if (flag.equals("2")) {
                llAll.setVisibility(GONE);
                llHalf.setVisibility(VISIBLE);
                llHollow.setVisibility(VISIBLE);
            } else {
                llHollow.setVisibility(GONE);
                llHalf.setVisibility(VISIBLE);
                llAll.setVisibility(VISIBLE);
            }
        } else {
            llHollow.setVisibility(GONE);
            llHalf.setVisibility(VISIBLE);
            llAll.setVisibility(VISIBLE);
        }

        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int xPos = view.getWidth() / 2 - popupWindow.getContentView().getMeasuredWidth() + 50 ;
        popupWindow.showAsDropDown(view, xPos,30);
    }

    public void updateFollowUpStatus(String projId,final String follUpStatus,final int pos){
        ListHttpHelper.updateFollowUpStatus(PotentialProjectsListForVCGroupActivity.this, SPUtils.get(PotentialProjectsListForVCGroupActivity.this, USER_ACCOUNT, "").toString(), projId, follUpStatus, new SDRequestCallBack(IDGBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())){
                    datas.get(pos).setFollowUpStatus(follUpStatus);
                    adapter.notifyDataSetChanged();
                }else {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    private void initPopupwindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_research_report_search, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });

        final EditText etKeyWord = (EditText) contentView.findViewById(R.id.et_keyword);
        Button btnReset = (Button) contentView.findViewById(R.id.btn_reset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etKeyWord.setText("");
            }
        });
        Button btnSearch = (Button) contentView.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.notEmpty(etKeyWord.getText())) {
                    stsId = null;
                    key = etKeyWord.getText().toString();
                    vcSts = null;
                    dateRange = null;
                    getData();
                }
                popupWindow.dismiss();
            }
        });

    }

    public void initlistPopupWindow(View view){

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(340);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setDropDownGravity(Gravity.RIGHT);
        final WorkCircleListPopupAdapter listPopupAdapter = new WorkCircleListPopupAdapter(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                listPopupWindow.dismiss();

                    if (i == 4){
                        mPage = 1;
                        stsId = null;
                        key = null;
                        dateRange = null;
                        vcSts = popuList.get(i).getKey();
                        getData();
                    }else {
                        mPage = 1;
                        stsId = null;
                        key = null;
                        vcSts = null;
                        dateRange = popuList.get(i).getKey();
                        getData();
                    }


            }
        });
        listPopupWindow.show();
    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

}
