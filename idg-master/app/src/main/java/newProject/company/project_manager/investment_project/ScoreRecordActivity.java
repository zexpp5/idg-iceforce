package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordAdapter;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;
import newProject.company.project_manager.investment_project.bean.Level0Item;
import newProject.company.project_manager.investment_project.bean.Level1Item;
import newProject.company.project_manager.investment_project.bean.Level2Item;
import newProject.company.project_manager.investment_project.bean.ScoreItemBaseBean;
import newProject.company.project_manager.investment_project.bean.ScoreRecordItemHistoryBean;
import newProject.company.project_manager.investment_project.bean.ScoreRecordItemList;
import newProject.company.project_manager.investment_project.bean.ScoreRecordListBean;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by zsz on 2019/4/12.
 */

public class ScoreRecordActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.top_recycler_view)
    RecyclerView topRecyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_new)
    TextView tvNew;

    ScoreRecordAdapter scoreRecordAdapter;
    ScoreRecordExpandableItemAdapter adapter;

    List<ScoreItemBaseBean> baseItems = new ArrayList<>();

    List<MultiItemEntity> datas = new ArrayList<>();
    List<Level0Item> level0Items = new ArrayList<>();


    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mNavigatorBar.setMidText("评分记录");

        tvNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ScoreRecordActivity.this,ItemScoringActivity.class);
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);
            }
        });

        topRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreRecordAdapter = new ScoreRecordAdapter(baseItems);
        topRecyclerView.setAdapter(scoreRecordAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreRecordExpandableItemAdapter(datas);
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

    public void getData() {
        ListHttpHelper.getScoreRecordListData(this, getIntent().getStringExtra("projId"), new SDRequestCallBack(ScoreRecordListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ScoreRecordListBean bean = (ScoreRecordListBean) responseInfo.getResult();
                if (bean.getData().getData().getLatest() != null && bean.getData().getData().getLatest().size() > 0) {
                    baseItems.clear();
                    baseItems.addAll(bean.getData().getData().getLatest());
                    scoreRecordAdapter.notifyDataSetChanged();

                    if (bean.getData().getData().getHistory() != null && bean.getData().getData().getHistory().size() > 0) {
                        datas.clear();
                        for (int i = 0; i < bean.getData().getData().getHistory().size(); i++) {
                            //item.addSubItem(bean.getData().getData().getLatest().get(i));
                            level0Items.add(new Level0Item(bean.getData().getData().getHistory().get(i).getBaseInfo()));
                            for (int j = 0; j < bean.getData().getData().getHistory().get(i).getList().size(); j++){
                                level0Items.get(i).addSubItem(bean.getData().getData().getHistory().get(i).getList().get(j));
                            }
                        }
                        datas.addAll(level0Items);
                        adapter.notifyDataSetChanged();

                    }
                }
            }
        });
    }

    public void getItemData(final Level0Item item,final int pos){

        ListHttpHelper.getScoreRecordItemListData(this, item.getData().getProjId(),item.getData().getScoreDate(), new SDRequestCallBack(ScoreRecordItemList.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ScoreRecordItemList bean =  (ScoreRecordItemList) responseInfo.getResult();
                if (bean.getData().getData().size() > 0){
                    level0Items.get(pos).setSubItems(bean.getData().getData());
                }
                datas.clear();
                datas.addAll(level0Items);
                adapter.notifyDataSetChanged();
                adapter.expand(pos);
            }
        });
    }

}
