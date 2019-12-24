package newProject.company.event.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
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
import newProject.company.event.fragment.adapter.NeedToDoAdapter;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.investment.ProjectForInvestmentActivity;
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.ItemRatingActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.ProjectActivity;
import newProject.company.project_manager.investment_project.TrackProgressAddActivity;
import newProject.company.project_manager.investment_project.TrackProgressStatusActivity;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.ToDoListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.StatusTipsView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 */

public class NeedToDoFragment extends Fragment {
    @Bind(R.id.loading_view)
    StatusTipsView mTips;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;
    private int mPage = 0;
    private int mBackListSize = 0;
    NeedToDoAdapter adapter;

    List<ToDoListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_need_to_do, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NeedToDoAdapter(datas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_name:
                        Intent in = new Intent();
                        if ("POTENTIAL".equals(datas.get(position).getProjType())){
                            in.setClass(getActivity(), PotentialProjectsDetailActivity.class);
                        }else if ("INVESTED".equals(datas.get(position).getProjType())){
                            in.setClass(getActivity(), InvestedProjectsDetailActivity.class);
                        }else if("FOLLOW_ON".equals(datas.get(position).getProjType())){
                            in.setClass(getActivity(), FollowProjectDetailActivity.class);
                        }else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        in.putExtra("projName",datas.get(position).getProjName());
                        in.putExtra("projId",datas.get(position).getBusId());
                        startActivity(in);
                        break;
                    case R.id.ll_item:
                        ToDoListBean.DataBeanX.DataBean dataBean = (ToDoListBean.DataBeanX.DataBean)adapter.getData().get(position);
                        String busType = dataBean.getBusType();
                        if ("ICE_FOLLOW_ON_SCORE".equals(busType)){
                            Intent intent = new Intent(getActivity(), ItemRatingActivity.class);
                            intent.putExtra("projId",dataBean.getBusId());
                            startActivity(intent);
                        }else if ("ICE_FOLLOW_ON_PROJ".equals(busType)){
                            Intent intent = new Intent(getActivity(), TrackProgressAddActivity.class);
                            intent.putExtra("projName",dataBean.getProjName());
                            intent.putExtra("projId",dataBean.getBusId());
                            intent.putExtra("validDate",dataBean.getValidDate());
                            intent.putExtra("flag","DETAIL");
                            startActivity(intent);
                        }else if ("ICE_PERMISSION_APPLY".equals(busType)){
                            initPopupWindow(view,position);
                        }else if ("ICE_FOLLOW_ON_EXCEED".equals(busType)){
                            Intent intent = new Intent(getActivity(), TrackProgressStatusActivity.class);
                            intent.putExtra("projId",dataBean.getBusId());
                            intent.putExtra("origBusId",dataBean.getOrigBusId());
                            intent.putExtra("projName",dataBean.getProjName());
                            startActivity(intent);
                        }else if("ICE_FOLLOW_ON_REVIEW".equals(busType)){
                            Intent intent6 = new Intent(getActivity(), ActivityAddFollowUp.class);
                            Bundle bundle6 = new Bundle();
                            bundle6.putString("projId", dataBean.getBusId());
                            bundle6.putString("year", dataBean.getYear());
                            bundle6.putString("dateStr", dataBean.getDateStr());
                            bundle6.putString("reportFrequency", dataBean.getReportFrequency());
                            bundle6.putString("mTitle", "新增投后跟踪");
                            intent6.putExtras(bundle6);
                            startActivity(intent6);
                        }else if("ICE_FOLLOW_ON_QUARTER".equals(busType)){
                            Intent intent = new Intent(getActivity(), TrackProgressAddActivity.class);
                            intent.putExtra("projName",dataBean.getProjName());
                            intent.putExtra("projId",dataBean.getBusId());
                            intent.putExtra("origBusId",dataBean.getOrigBusId());
                            intent.putExtra("busType",dataBean.getBusType());
                            intent.putExtra("applyUser",dataBean.getApplyUser());
                            intent.putExtra("flag","DETAIL");
                            startActivity(intent);
                        }
                        break;
                }
                
            }
        });
        initRefresh();
        mTips.showLoading();
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
                if (DisplayUtil.hasNetwork(getActivity()))
                {
                    mPage = 0;
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
                if (DisplayUtil.hasNetwork(getActivity()))
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

    @Override
    public void onStart() {
        super.onStart();
        mPage = 0;
        getData();
    }

    private void getData(){
        ListHttpHelper.getToDoEventList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(),"TODO", mPage+"", "10", new SDRequestCallBack(ToDoListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ToDoListBean bean = (ToDoListBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode())) {
                    mBackListSize = bean.getData().getTotal();
                    if (getActivity() instanceof ProjectActivity){
                        ((ProjectActivity)getActivity()).setThreeUnRead(mBackListSize);
                    }else if (getActivity() instanceof ProjectForInvestmentActivity){
                        ((ProjectForInvestmentActivity)getActivity()).setThreeUnRead(mBackListSize);
                    }

                    if (mPage == 0) {
                        adapter.getData().clear();
                        if (bean.getData().getData().size() > 0) {
                            mTips.hide();
                        } else {
                            mTips.showNoContent("暂无数据");
                        }
                    }
                    adapter.getData().addAll(bean.getData().getData());
                    adapter.notifyDataSetChanged();
                }else {
                    mTips.showNoContent(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void initPopupWindow(View view, final int position) {

        StringBuffer sb = new StringBuffer(datas.get(position).getShowDesc());
        sb.insert(sb.indexOf(datas.get(position).getProjName()),"#");
        sb.insert(sb.indexOf(datas.get(position).getProjName()) + datas.get(position).getProjName().length(),"#");
        String str = sb.toString();
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(str);
        //ForegroundColorSpan--文字前景色，BackgroundColorSpan--文字背景色
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.blue));
        ssbuilder.setSpan(colorSpan, str.indexOf("#"), str.lastIndexOf("#") + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_need_to_da, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1f);
            }
        });
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        tvContent.setText(ssbuilder);
        final EditText etSeason = (EditText) contentView.findViewById(R.id.et_season) ;
        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        btnAgree.setText("同意");
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnDisagree.setText("不同意");
        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHttpHelper.sendProcessData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), datas.get(position).getBusId(), datas.get(position).getApplyUser(),datas.get(position).getOrigBusId(),"1",etSeason.getText().toString(),new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean baseBean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(baseBean.getData().getCode())) {
                            mPage = 0;
                            getData();
                        } else {
                            SDToast.showShort(baseBean.getData().getReturnMessage());
                        }
                    }
                });
                popupWindow.dismiss();
            }
        });

        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListHttpHelper.sendProcessData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), datas.get(position).getBusId(), datas.get(position).getApplyUser(),datas.get(position).getOrigBusId(),"0",etSeason.getText().toString(),new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean baseBean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(baseBean.getData().getCode())) {
                            mPage = 0;
                            getData();
                        } else {
                            SDToast.showShort(baseBean.getData().getReturnMessage());
                        }
                    }
                });
                popupWindow.dismiss();
            }
        });

    }

    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
