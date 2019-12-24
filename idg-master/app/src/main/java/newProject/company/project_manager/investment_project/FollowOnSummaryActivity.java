package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.FollowOnSummaryAdapter;
import newProject.company.project_manager.investment_project.bean.FollowOnSummaryListBean;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/5/5.
 */

public class FollowOnSummaryActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_done)
    TextView tvDone;
    @Bind(R.id.recycler_view_done)
    RecyclerView recyclerViewDone;
    @Bind(R.id.tv_undone)
    TextView tvUndone;
    @Bind(R.id.recycler_view_undone)
    RecyclerView recyclerViewUndone;

    FollowOnSummaryAdapter adapterDone;
    FollowOnSummaryAdapter adapterUnDone;
    List<FollowOnSummaryListBean.DataBeanX.DataBean> doneDatas = new ArrayList<>();
    List<FollowOnSummaryListBean.DataBeanX.DataBean> unDoneDatas = new ArrayList<>();

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setMidText("项目跟进汇总");
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tvDone.setText("已完成(" + getIntent().getStringExtra("date") + ")");
        tvUndone.setText("未完成(" + getIntent().getStringExtra("date") + ")");
        recyclerViewDone.setLayoutManager(new LinearLayoutManager(this));
        adapterDone = new FollowOnSummaryAdapter(doneDatas);
        adapterDone.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_name:
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(doneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, PotentialProjectsDetailActivity.class);
                        }else if ("INVESTED".equals(doneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, InvestedProjectsDetailActivity.class);
                        }else if("FOLLOW_ON".equals(doneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, FollowProjectDetailActivity.class);
                        }else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projName",doneDatas.get(position).getProjName());
                        intent.putExtra("projId",doneDatas.get(position).getProjId());
                        startActivity(intent);
                        break;
                    case R.id.ll_item:
                        Intent intent2 = new Intent(FollowOnSummaryActivity.this,FollowOnDetailActivity.class);
                        intent2.putExtra("projId",doneDatas.get(position).getProjId());
                        intent2.putExtra("projName",doneDatas.get(position).getProjName());
                        intent2.putExtra("date",getIntent().getStringExtra("date") );
                        startActivity(intent2);
                        break;
                }
            }
        });
        recyclerViewDone.setAdapter(adapterDone);

        recyclerViewUndone.setLayoutManager(new LinearLayoutManager(this));
        adapterUnDone = new FollowOnSummaryAdapter(unDoneDatas);
        adapterUnDone.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.tv_name:
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(unDoneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, PotentialProjectsDetailActivity.class);
                        }else if ("INVESTED".equals(unDoneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, InvestedProjectsDetailActivity.class);
                        }else if("FOLLOW_ON".equals(unDoneDatas.get(position).getProjType())){
                            intent.setClass(FollowOnSummaryActivity.this, FollowProjectDetailActivity.class);
                        }else {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projName",unDoneDatas.get(position).getProjName());
                        intent.putExtra("projId",unDoneDatas.get(position).getBusId());
                        startActivity(intent);
                        break;
                    case R.id.ll_item:
                        Intent intent2 = new Intent(FollowOnSummaryActivity.this,FollowOnDetailActivity.class);
                        intent2.putExtra("projId",doneDatas.get(position).getBusId());
                        intent2.putExtra("projName",doneDatas.get(position).getProjName());
                        intent2.putExtra("date",doneDatas.get(position).getValidDate());
                        startActivity(intent2);
                        break;
                }
            }
        });
        recyclerViewUndone.setAdapter(adapterUnDone);

        getDoneData();
        getUnDoneData();
    }

    public void getDoneData(){
        ListHttpHelper.getFollowOnSummaryDoneData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), new SDRequestCallBack(FollowOnSummaryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowOnSummaryListBean bean = (FollowOnSummaryListBean) responseInfo.getResult();
                if(bean.getData().getData() != null && bean.getData().getData().size() > 0){
                    doneDatas.addAll(bean.getData().getData());
                    adapterDone.notifyDataSetChanged();
                }else {
                    tvDone.setText("已完成(无记录)");
                }
            }
        });
    }

    public void getUnDoneData(){
        ListHttpHelper.getFollowOnSummaryUnDoneData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), new SDRequestCallBack(FollowOnSummaryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowOnSummaryListBean bean = (FollowOnSummaryListBean) responseInfo.getResult();
                if(bean.getData().getData() != null && bean.getData().getData().size() > 0){
                    unDoneDatas.addAll(bean.getData().getData());
                    adapterUnDone.notifyDataSetChanged();
                }else{
                    tvUndone.setText("未完成(无记录)");
                }
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_follow_on_summary;
    }

}
