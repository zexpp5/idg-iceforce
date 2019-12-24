package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
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
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter2;
import newProject.company.project_manager.investment_project.adapter.ScoreRecordAdapter;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.ScoreItemBaseBean;
import newProject.company.project_manager.investment_project.bean.VCGroupDetailList;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/7/4.
 */

public class FollowProjectDetailForVCGroupActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_bottom)
    TextView tvBottom;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_comIndusStr)
    TextView tvComIndusStr;
    @Bind(R.id.tv_zhDesc)
    TextView tvZhDesc;
    @Bind(R.id.tv_projTeamNames)
    TextView tvProjTeamNames;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_more)
    TextView tvMore;

    ScoreRecordAdapter scoreRecordAdapter;
    List<ScoreItemBaseBean> baseItems = new ArrayList<>();

    List<String> popuList = new ArrayList<>();

    List<IDGTpyeBaseBean.DataBeanX.DataBean> datas = new ArrayList<>();

    @Override
    protected int getContentLayout() {
        return R.layout.activity_follow_project_detail_vc;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        //标题
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setRightImageResouce(R.mipmap.at);
        mNavigatorBar.setMidText("");
        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowProjectDetailForVCGroupActivity.this, AtActivity.class);
                intent.putExtra("projName", getIntent().getStringExtra("projName"));
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        scoreRecordAdapter = new ScoreRecordAdapter(baseItems);
        recyclerView.setAdapter(scoreRecordAdapter);
        getBaseData();
        getData();
    }


    public void getData() {
        ListHttpHelper.getFollowProjectDetailForVCGroupDatas(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), new SDRequestCallBack(VCGroupDetailList.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                VCGroupDetailList bean = (VCGroupDetailList) responseInfo.getResult();
                VCGroupDetailList.DataBeanX.DataBean.BaseInfoMapBean baseInfoMapBean =  bean.getData().getData().getBaseInfoMap();
                tvName.setText(baseInfoMapBean.getProjName());
                tvComIndusStr.setText(baseInfoMapBean.getComInduStr());
                tvZhDesc.setText(baseInfoMapBean.getZhDesc());
                tvProjTeamNames.setText(baseInfoMapBean.getProjTeamNames());

                if (bean.getData().getData().getVcFollowProjDiscussionVO() != null) {
                    VCGroupDetailList.DataBeanX.DataBean.VcFollowProjDiscussionVOBean vcBean = bean.getData().getData().getVcFollowProjDiscussionVO();
                    if (StringUtils.notEmpty(vcBean.getDate()))
                        tvDate.setText(vcBean.getDate().split(" ")[0]);
                    tvContent.setText(vcBean.getContent());
                    baseItems.clear();
                    baseItems.addAll(vcBean.getProjScoreList());
                    scoreRecordAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initPopupWindow(View view) {
        if (popuList.size() == 0){
            SDToast.showShort("字典数据获取失败");
            return;
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter2 listPopupAdapter = new ListPopupAdapter2(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0){
                    Intent intent = new Intent(FollowProjectDetailForVCGroupActivity.this, TrackProgressAddActivity.class);
                    intent.putExtra("projName", getIntent().getStringExtra("projName"));
                    intent.putExtra("projId", getIntent().getStringExtra("projId"));
                    intent.putExtra("title",datas.get(i).getCodeNameZhCn());
                    intent.putExtra("codeKey",datas.get(i).getCodeKey());
                    intent.putExtra("flag", "DETAIL");
                    startActivity(intent);
                    finish();
                }else {
                    sendData(i);
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void getBaseData(){
        ListHttpHelper.getIDGBaseData(this, "vcStatus", new SDRequestCallBack(IDGTpyeBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGTpyeBaseBean bean = (IDGTpyeBaseBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(bean.getData().getData());
                for (int i = 0; i < datas.size(); i++)
                    popuList.add(datas.get(i).getCodeNameZhCn());
            }
        });
    }

    private void sendData(int position) {
        ListHttpHelper.sendVCTrackProgressAddData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra("projId"), "", "", "", "", datas.get(position).getCodeKey(),new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode())) {
                            SDToast.showShort("提交成功");
                            finish();
                        } else {
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }
                    }
                });
    }

    @OnClick({R.id.tv_bottom,R.id.tv_more})
    public void OnItemClick(View view){
        switch (view.getId()){
            case R.id.tv_bottom:
                initPopupWindow(view);
                break;
            case R.id.tv_more:
                Intent intent = new Intent(FollowProjectDetailForVCGroupActivity.this,ScoreRecordForVCGroupActivity.class);
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);
                break;
        }
    }


}
