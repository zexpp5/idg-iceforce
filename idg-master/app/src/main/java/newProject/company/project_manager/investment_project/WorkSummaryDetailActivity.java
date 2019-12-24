package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.SPUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.WorkSummaryDetailMultiAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkSummaryDetailHorizonAdapter;
import newProject.company.project_manager.investment_project.bean.NameListsBean;
import newProject.company.project_manager.investment_project.bean.WorkSummaryDetailListBean;
import newProject.company.project_manager.investment_project.bean.WorkSummaryDetailMultiBean;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/24.
 */

public class WorkSummaryDetailActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.recycler_view_horizon)
    RecyclerView recyclerViewHorizon;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    WorkSummaryDetailHorizonAdapter horizonAdapter;
    List<NameListsBean.DataBeanX.DataBean> horizonDatas = new ArrayList<>();
    int index = 0;

    WorkSummaryDetailMultiAdapter adapter;
    List<WorkSummaryDetailMultiBean> detailDatas = new ArrayList<>();

    //记录跟踪进展的数量
    int count = 0;
    //记录日期
    String date = "";

    @Override
    protected int getContentLayout() {
        return R.layout.activity_work_summary_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("工作汇总详情");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerViewHorizon.setLayoutManager(lm);
        horizonAdapter = new WorkSummaryDetailHorizonAdapter(horizonDatas);
        recyclerViewHorizon.setAdapter(horizonAdapter);
        horizonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                horizonDatas.get(index).setChoose(false);
                index = position;
                horizonDatas.get(index).setChoose(true);
                horizonAdapter.notifyDataSetChanged();
                getDetailData(horizonDatas.get(index).getUserAccount());
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WorkSummaryDetailMultiAdapter(detailDatas);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_name:
                        if (position <= count){
                            Intent intent = new Intent(WorkSummaryDetailActivity.this, TrackProgressActivity.class);
                            intent.putExtra("projName",detailDatas.get(position).getData().getProjName());
                            intent.putExtra("projId",detailDatas.get(position).getData().getProjId());
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent();
                            if ("POTENTIAL".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, PotentialProjectsDetailActivity.class);
                            }else if ("INVESTED".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, InvestedProjectsDetailActivity.class);
                            }else if("FOLLOW_ON".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, FollowProjectDetailActivity.class);
                            }else {
                                SDToast.showShort("未知项目类型");
                                return;
                            }
                            intent.putExtra("projName",detailDatas.get(position).getData().getProjName());
                            intent.putExtra("projId",detailDatas.get(position).getData().getProjId());
                            startActivity(intent);
                        }
                        break;
                    case R.id.ll_item:
                        if (position <= count){
                            Intent intent2 = new Intent(WorkSummaryDetailActivity.this,FollowOnDetailActivity.class);
                            intent2.putExtra("projId",detailDatas.get(position).getData().getProjId());
                            intent2.putExtra("projName",detailDatas.get(position).getData().getProjName());
                            intent2.putExtra("date",date);
                            startActivity(intent2);
                        }else {
                            Intent intent = new Intent();
                            if ("POTENTIAL".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, PotentialProjectsDetailActivity.class);
                            }else if ("INVESTED".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, InvestedProjectsDetailActivity.class);
                            }else if("FOLLOW_ON".equals(detailDatas.get(position).getData().getProjType())){
                                intent.setClass(WorkSummaryDetailActivity.this, FollowProjectDetailActivity.class);
                            }else {
                                SDToast.showShort("未知项目类型");
                                return;
                            }
                            intent.putExtra("projName",detailDatas.get(position).getData().getProjName());
                            intent.putExtra("projId",detailDatas.get(position).getData().getProjId());
                            startActivity(intent);
                        }
                        break;
                }
            }
        });

        getNameListData();
    }

    public void getNameListData(){
        ListHttpHelper.getWorkSummaryDetailNameListData(WorkSummaryDetailActivity.this, SPUtils.get(this, USER_ACCOUNT, "").toString(), new SDRequestCallBack(NameListsBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NameListsBean bean = (NameListsBean) responseInfo.getResult();
                horizonDatas.clear();
                horizonDatas.addAll(bean.getData().getData());
                horizonDatas.get(index).setChoose(true);
                horizonAdapter.notifyDataSetChanged();
                getDetailData(bean.getData().getData().get(0).getUserAccount());
            }
        });
    }

    public void getDetailData(String name){
        ListHttpHelper.getWorkSummaryDetailListData(WorkSummaryDetailActivity.this, name, new SDRequestCallBack(WorkSummaryDetailListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkSummaryDetailListBean bean = (WorkSummaryDetailListBean) responseInfo.getResult();
                detailDatas.clear();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0){
                    for (int i = 0; i < bean.getData().getData().size(); i++ ){
                        detailDatas.add(new WorkSummaryDetailMultiBean(1,bean.getData().getData().get(i).getTitle()+"(" + bean.getData().getData().get(i).getCurMonth() + ")"));
                        for (int j = 0; j < bean.getData().getData().get(i).getData().size();j++){
                            detailDatas.add(new WorkSummaryDetailMultiBean(2,bean.getData().getData().get(i).getTitle(),bean.getData().getData().get(i).getData().get(j)));
                        }
                        if ( i == 0){
                            count = detailDatas.size();
                            date = bean.getData().getData().get(i).getCurMonth();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}
