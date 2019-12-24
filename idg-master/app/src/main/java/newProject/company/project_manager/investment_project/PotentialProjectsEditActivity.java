package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.ListPopupWindow;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;
import com.utils.StringUtil;
import com.weigan.loopview.LoopView;
import com.weigan.loopview.OnItemSelectedListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.GroupListBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.NewGroupListBean;
import newProject.company.project_manager.investment_project.bean.PPIndustryListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import newProject.company.project_manager.investment_project.bean.PublicUserListBean;
import tablayout.view.textview.FontEditext;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/11.
 */

public class PotentialProjectsEditActivity extends BaseActivity {

    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.tv_state)
    TextView tvState;
    @Bind(R.id.etv_desc)
    FontEditext etvDesc;
    @Bind(R.id.tv_industry)
    TextView tvIndustry;
    @Bind(R.id.etv_money)
    FontEditext etvMoney;
    @Bind(R.id.tv_principal)
    TextView tvPrincipal;
    @Bind(R.id.tv_team_member)
    TextView tvTeamMember;

    PotentialProjectsDetailBean.DataBeanX.DataBean.BaseInfoMap dataBean;

    List<IDGTpyeBaseBean.DataBeanX.DataBean> datas = new ArrayList<>();
    int stateIndex;
    List<GroupListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
    int groupIndex;
    List<String> popuList = new ArrayList<>();

    String stateKey;
    String gruopKey;
    String industryKey;

    List<PPIndustryListBean.DataBeanX.DataBean> industryLists = new ArrayList<>();

    private List<PublicUserListBean.DataBeanX.DataBean> principalLists = new ArrayList<>();
    private List<PublicUserListBean.DataBeanX.DataBean> teamMemberrecommendLists = new ArrayList<>();

    private int indexLoopView1 = 0;
    private int indexLoopView2 = 0;


    private List<NewGroupListBean.DataBeanX.DataBean> newGroupListBeen = new ArrayList<>();

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
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        dataBean = (PotentialProjectsDetailBean.DataBeanX.DataBean.BaseInfoMap) getIntent().getSerializableExtra("data");
        tvGroup.setText(dataBean.getIndusGroupStr());
        gruopKey = dataBean.getIndusGroup();
        tvState.setText(dataBean.getStsIdStr());
        stateKey = dataBean.getStsId();
        if (!StringUtils.isEmpty(dataBean.getZhDesc())) {
            etvDesc.setText(dataBean.getZhDesc());
        }
        if (!StringUtils.isEmpty(dataBean.getComInduStr())) {
            tvIndustry.setText(dataBean.getComInduStr());
            industryKey = dataBean.getComIndu();
        }
        if (!StringUtils.isEmpty(dataBean.getPlanInvAmount())) {
            etvMoney.setText(dataBean.getPlanInvAmount());
        }
        tvPrincipal.setText(dataBean.getProjManagerNames());
        tvTeamMember.setText(dataBean.getProjTeamNames());
        getProjectStateData();
        //getGroupData();
        getGroupDataNew();
        getIndustryData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_potential_projects_edit;
    }

    @OnClick({R.id.ll_state, R.id.ll_group, R.id.ll_industry, R.id.ll_principal, R.id.ll_team_member, R.id.btn_ok})
    public void onItemClick(View view) {
        Intent intent = new Intent(PotentialProjectsEditActivity.this, PublicUserListActivity.class);
        switch (view.getId()) {
            case R.id.ll_state:
                initPopupWindow(view, "STATE");
                break;
            case R.id.ll_group:
                //initPopupWindow(view, "GROUP");
                initIndustryPopupWindow(2);
                break;
            case R.id.ll_industry:
                initIndustryPopupWindow(1);
                break;
            case R.id.ll_principal:
                intent.putExtra("userLists", (Serializable) principalLists);
                intent.putExtra("ids",dataBean.getProjManagers());
                intent.putExtra("at", true);
                startActivityForResult(intent, 100);
                break;
            case R.id.ll_team_member:
                intent.putExtra("userLists", (Serializable) teamMemberrecommendLists);
                intent.putExtra("ids",dataBean.getProjTeams());
                intent.putExtra("at", true);
                startActivityForResult(intent, 200);
                break;
            case R.id.btn_ok:

                if (StringUtils.isEmpty(tvState.getText())) {
                    SDToast.showShort("请选择项目状态");
                    return;
                }
                if (StringUtils.isEmpty(tvGroup.getText())) {
                    SDToast.showShort("请选择行业小组");
                    return;
                }
                if (StringUtils.isEmpty(tvIndustry.getText())) {
                    SDToast.showShort("请选择行业");
                    return;
                }
                /*if (StringUtils.isEmpty(etDesc.getText())){
                    SDToast.showShort("请填写项目介绍");
                    return;
                }

                if (StringUtils.isEmpty(etMoney.getText())){
                    SDToast.showShort("请填写拟投金额");
                    return;
                }*/
                updateData();
                break;
        }

    }

    private void getProjectStateData() {
        ListHttpHelper.getIDGBaseData(this, "potentialStatus", new SDRequestCallBack(IDGTpyeBaseBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                IDGTpyeBaseBean bean = (IDGTpyeBaseBean) responseInfo.getResult();
                datas.clear();
                datas.addAll(bean.getData().getData());
            }
        });
    }

    private void getGroupData() {
        ListHttpHelper.getGroupListData(this, new SDRequestCallBack(GroupListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                GroupListBean bean = (GroupListBean) responseInfo.getResult();
                groupLists.clear();
                groupLists.addAll(bean.getData().getData());
            }
        });
    }

    private void getGroupDataNew() {
        ListHttpHelper.getGroupListDataNew(this, new SDRequestCallBack(NewGroupListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NewGroupListBean bean = (NewGroupListBean) responseInfo.getResult();
                newGroupListBeen = bean.getData().getData();
            }
        });
    }

    public void updateData(){
        String pStr;
        if (principalLists.size() > 0) {
            StringBuffer sbPr = new StringBuffer();
            for (int i = 0; i < principalLists.size(); i++)
                sbPr.append(principalLists.get(i).getUserId() + ",");
            pStr = sbPr.substring(0, sbPr.length() - 1);
        }else {
            pStr = dataBean.getProjManagers();
        }
        String tStr;
        if (teamMemberrecommendLists.size() > 0) {

            StringBuffer sbTeam = new StringBuffer();
            for (int i = 0; i < teamMemberrecommendLists.size(); i++)
                sbTeam.append(teamMemberrecommendLists.get(i).getUserId() + ",");
            tStr = sbTeam.substring(0, sbTeam.length() - 1);
        }else {
            tStr = dataBean.getProjTeams();
        }

        ListHttpHelper.updatePotentialProjectsEditData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),getIntent().getStringExtra("projId"), getIntent().getStringExtra("projName"), stateKey, etvDesc.getText().toString(), gruopKey,
                etvMoney.getText().toString(),"", industryKey,pStr, tStr, new SDRequestCallBack(IDGBaseBean.class) {
                    @Override
                    public void onRequestFailure(HttpException error, String msg) {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo) {
                        IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode())){
                            SDToast.showShort("提交成功");
                            finish();
                        }else{
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }
                    }
                });
    }

    private void getIndustryData() {
        ListHttpHelper.getIndustryListData(this, new SDRequestCallBack(PPIndustryListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PPIndustryListBean bean = (PPIndustryListBean) responseInfo.getResult();
                industryLists.clear();
                industryLists.addAll(bean.getData().getData());
            }
        });
    }

    private void initPopupWindow(View view, final String flag) {
        popuList.clear();
        if ("STATE".equals(flag)) {
            for (int i = 0; i < datas.size(); i++)
                popuList.add(datas.get(i).getCodeNameZhCn());
        } else {
            for (int i = 0; i < groupLists.size(); i++)
                popuList.add(groupLists.get(i).getDeptName());
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter listPopupAdapter = new ListPopupAdapter(popuList, PotentialProjectsEditActivity.this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if ("STATE".equals(flag)) {
                    tvState.setText(datas.get(i).getCodeNameZhCn());
                    stateKey = datas.get(i).getCodeKey();
                    stateIndex = i;
                } else {
                    tvGroup.setText(groupLists.get(i).getDeptName());
                    gruopKey = groupLists.get(i).getDeptId();
                    groupIndex = i;
                }

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void initIndustryPopupWindow(final int flag) {
        final List<String> listStr = new ArrayList<>();
        final List<String> listStrChildren = new ArrayList<>();
        View contentView = LayoutInflater.from(this).inflate(R.layout.popupwindow_loopview, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        ColorDrawable cd = new ColorDrawable(0xd0000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.showAtLocation(contentView, Gravity.CENTER, 0, 0);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loopView1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loopView2);
        Button btnOk = (Button) contentView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag == 1) {
                    tvIndustry.setText(industryLists.get(indexLoopView1).getChildren().get(indexLoopView2).getCodeNameZhCn());
                    industryKey = industryLists.get(indexLoopView1).getChildren().get(indexLoopView2).getCodeKey();
                }else {
                    tvGroup.setText(newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptName());
                    gruopKey = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();
                }
                popupWindow.dismiss();
            }
        });
        Button btnCancel = (Button) contentView.findViewById(R.id.btn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        if (flag == 1) {
            for (int i = 0; i < industryLists.size(); i++) {
                listStr.add(industryLists.get(i).getCodeNameZhCn());
            }
        }else {
            for (int i = 0; i < newGroupListBeen.size(); i++) {
                listStr.add(newGroupListBeen.get(i).getDeptName());
            }
        }
        loopView.setNotLoop();
        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexLoopView1 = index;
                indexLoopView2 = 0;
                listStrChildren.clear();
                if (flag == 1) {
                    for (int i = 0; i < industryLists.get(index).getChildren().size(); i++) {
                        listStrChildren.add(industryLists.get(index).getChildren().get(i).getCodeNameZhCn());
                    }
                }else {
                    for (int i = 0; i < newGroupListBeen.get(index).getChildren().size(); i++) {
                        listStrChildren.add(newGroupListBeen.get(index).getChildren().get(i).getDeptName());
                    }
                }
                loopView2.setItems(listStrChildren);
                loopView2.setCurrentPosition(0);
                loopView2.setTextSize(18);

            }
        });
        loopView.setItems(listStr);
        loopView.setInitPosition(0);
        loopView.setTextSize(18);

        if (flag == 1) {
            for (int i = 0; i < industryLists.get(0).getChildren().size(); i++) {
                listStrChildren.add(industryLists.get(0).getChildren().get(i).getCodeNameZhCn());
            }
        }else {
            for (int i = 0; i < newGroupListBeen.get(0).getChildren().size(); i++) {
                listStrChildren.add(newGroupListBeen.get(0).getChildren().get(i).getDeptName());
            }
        }
        loopView2.setNotLoop();
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                indexLoopView2 = index;
                if (flag == 1) {
                    industryKey = industryLists.get(indexLoopView1).getChildren().get(indexLoopView2).getCodeKey();
                }else{
                    gruopKey = newGroupListBeen.get(indexLoopView1).getChildren().get(indexLoopView2).getDeptId();
                }
            }
        });
        loopView2.setItems(listStrChildren);
        loopView2.setInitPosition(0);
        loopView2.setTextSize(18);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            //返回来的字符串
            final List<PublicUserListBean.DataBeanX.DataBean> userList = (List<PublicUserListBean.DataBeanX.DataBean>) data.getSerializableExtra("userLists");// str即为回传的值
            if (userList != null && userList.size() > 0) {

                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < userList.size(); i++) {

                    builder.append(userList.get(i).getUserName() + "、");
                }

                if (requestCode == 100) {
                    principalLists = userList;
                    tvPrincipal.setText(builder.substring(0, builder.length() - 1));
                } else if (requestCode == 200) {
                    teamMemberrecommendLists = userList;
                    tvTeamMember.setText(builder.substring(0, builder.length() - 1));
                }

            } else {
                if (requestCode == 100) {
                    tvPrincipal.setText("");
                    principalLists.clear();
                } else if (requestCode == 200) {
                    tvTeamMember.setText("");
                    teamMemberrecommendLists.clear();
                }
            }
        }
    }

}
