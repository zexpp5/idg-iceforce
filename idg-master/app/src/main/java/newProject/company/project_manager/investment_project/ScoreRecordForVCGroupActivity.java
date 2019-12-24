package newProject.company.project_manager.investment_project;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.entity.MultiItemEntity;
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
import newProject.company.project_manager.investment_project.adapter.ScoreRecordAdapter;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemAdapter;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordExpandableItemForVCGroupAdapter;
import newProject.company.project_manager.investment_project.bean.Level0Item;
import newProject.company.project_manager.investment_project.bean.ScoreItemBaseBean;
import newProject.company.project_manager.investment_project.bean.ScoreRecordForVCGroupListBean;
import newProject.company.project_manager.investment_project.bean.ScoreRecordItemList;
import newProject.company.project_manager.investment_project.bean.ScoreRecordListBean;
import newProject.company.project_manager.investment_project.bean.VCGroupLevel0Item;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/12.
 */

public class ScoreRecordForVCGroupActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.smart_refresh_layout)
    SmartRefreshLayout mRefreshLayout;

    private int mPage = 1;
    private int mBackListSize = 0;

    ScoreRecordExpandableItemForVCGroupAdapter adapter;

    List<ScoreItemBaseBean> baseItems = new ArrayList<>();

    List<MultiItemEntity> datas = new ArrayList<>();
    List<VCGroupLevel0Item> level0Items = new ArrayList<>();


    @Override
    protected int getContentLayout() {
        return R.layout.activity_score_record_vc;
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
        mNavigatorBar.setMidText("查看更多记录");
        initRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ScoreRecordExpandableItemForVCGroupAdapter(datas);
        recyclerView.setAdapter(adapter);

        getData();
    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(ScoreRecordForVCGroupActivity.this)) {
                    mPage = 1;
                    mRefreshLayout.finishRefresh(1000);
                    mRefreshLayout.setLoadmoreFinished(false);
                    mRefreshLayout.setEnableLoadmore(true);
                    getData();
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishRefresh(1000);
                }
            }
        });
        mRefreshLayout.setEnableLoadmoreWhenContentNotFull(true);
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (DisplayUtil.hasNetwork(ScoreRecordForVCGroupActivity.this)) {
                    if (adapter.getData().size() >= mBackListSize) {
                        mRefreshLayout.finishLoadmore(1000);
                        SDToast.showShort("没有更多数据了");
                    } else {
                        mPage = mPage + 1;
                        getData();
                        mRefreshLayout.finishLoadmore(1000);
                    }
                } else {
                    SDToast.showShort("请检查网络连接");
                    mRefreshLayout.finishLoadmore(1000);
                }

            }
        });
    }

    public void getData() {
        ListHttpHelper.getScoreRecordListForVCGroupData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), mPage + "", "10", new SDRequestCallBack(ScoreRecordForVCGroupListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                ScoreRecordForVCGroupListBean bean = (ScoreRecordForVCGroupListBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0) {

                    for (int i = 0; i < bean.getData().getData().size(); i++) {
                        level0Items.add(new VCGroupLevel0Item(bean.getData().getData().get(i)));
                        for (int j = 0; j < bean.getData().getData().get(i).getProjScoreList().size(); j++) {
                            //设置讨论内容
                            bean.getData().getData().get(i).getProjScoreList().get(j).setContent(bean.getData().getData().get(i).getContent());
                            //
                            level0Items.get(i).addSubItem(bean.getData().getData().get(i).getProjScoreList().get(j));
                        }
                    }
                    datas.addAll(level0Items);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

    /*public void getItemData(final Level0Item item,final int pos){

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
    }*/

}
