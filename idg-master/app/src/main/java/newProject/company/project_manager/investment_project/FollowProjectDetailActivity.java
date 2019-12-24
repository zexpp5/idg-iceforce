package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ButtonUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.ActivityFollowUp;
import newProject.company.invested_project.ActivityQxb;
import newProject.company.invested_project.ActivityQxbSearch;
import newProject.company.invested_project.InVestedProjectInfoActivity;
import newProject.company.project_manager.investment_project.adapter.HorizonAdapter;
import newProject.company.project_manager.investment_project.adapter.ListPopupAdapter;
import newProject.company.project_manager.investment_project.adapter.SegmentAdapter;
import newProject.company.project_manager.investment_project.adapter.WorkCircleListPopupAdapter;
import newProject.company.project_manager.investment_project.bean.FollowonListBean;
import newProject.company.project_manager.investment_project.bean.IDGBaseBean;
import newProject.company.project_manager.investment_project.bean.IDGTpyeBaseBean;
import newProject.company.project_manager.investment_project.bean.IconListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/25.
 * 跟进项目
 */

public class FollowProjectDetailActivity extends BaseActivity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.horizon_recycler_view)
    RecyclerView horizonRecyclerView;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    HorizonAdapter horizonAdapter;

    SegmentAdapter segmentAdapter;
    List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists = new ArrayList<>();

    @Bind(R.id.tv_headCityStr)
    TextView tvHeadCityStr;
    @Bind(R.id.tv_comInduStr)
    TextView tvComInduStr;
    @Bind(R.id.tv_currentRound)
    TextView tvCurrentRound;
    @Bind(R.id.tv_indusGroupStr)
    TextView tvIndusGroupStr;
    @Bind(R.id.tv_projManagerNames)
    TextView tvProjManagerNames;
    @Bind(R.id.tv_projTeamNames)
    TextView tvProjTeamNames;
    @Bind(R.id.iv_star)
    ImageView ivStar;
    @Bind(R.id.tv_star_state)
    TextView tvStarState;
    @Bind(R.id.btn_appl)
    Button btnAppl;
    @Bind(R.id.ll_bottom)
    LinearLayout llBottom;


    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.ll_water1)
    LinearLayout ll_water1;
    @Bind(R.id.ll_water2)
    LinearLayout ll_water2;
    @Bind(R.id.btn_prof)
    Button btnProf;

    List<IDGTpyeBaseBean.DataBeanX.DataBean> IDGDatas = new ArrayList<>();
    List<FollowonListBean.DataBeanX.DataBean> datas = new ArrayList<>();
    List<FollowonListBean.DataBeanX.DataBean> followStates = new ArrayList<>();
    PotentialProjectsDetailBean.DataBeanX.DataBean.BaseInfoMap baseInfoMap;
    private String proId = "";

    List<IconListBean> popuList = new ArrayList<>();

    int permission;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_follow_project_detail;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        mNavigatorBar.setMidText(getIntent().getStringExtra("projName"));
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(true);
        mNavigatorBar.setRightImageResouce(R.mipmap.icon_more);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                /*Intent intent = new Intent(FollowProjectDetailActivity.this, AtActivity.class);
                intent.putExtra("projName", getIntent().getStringExtra("projName"));
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                startActivity(intent);*/
                initlistPopupWindow((View) view.getParent());
            }
        });

        proId = getIntent().getStringExtra("projId");

        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        horizonRecyclerView.setLayoutManager(lm);
        horizonAdapter = new HorizonAdapter(datas);
        horizonRecyclerView.setAdapter(horizonAdapter);
        horizonAdapter.notifyDataSetChanged();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        segmentAdapter = new SegmentAdapter(segmentLists);
        segmentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                //跳转七个页面
                boolean fastDoubleClick = ButtonUtils.isFastDoubleClick();
                if (!fastDoubleClick)
                {
                    if (segmentLists.get(position).getCode().equals("followUp"))
                    {
                        Intent intent = new Intent(FollowProjectDetailActivity.this, ActivityFollowUp.class);
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.PROJECT_EID, proId);    //这个是projectID.
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (segmentLists.get(position).getCode().equals("gradeInfo"))
                    {
                        Intent intent = new Intent(FollowProjectDetailActivity.this, ScoreRecordActivity.class);
                        intent.putExtra("projId", proId);
                        startActivity(intent);
                    } else
                    {
                        setFGTitleString(segmentLists);
                        Intent intent = new Intent(FollowProjectDetailActivity.this, InVestedProjectInfoActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.FG_ALL, (Serializable) segmentLists); //权限的
                        bundle.putSerializable(Constants.FG_LIST_TITLE, (Serializable) listFgTitle);  //带多少title就显示多少
                        bundle.putSerializable(Constants.FG_LIST, (Serializable) listFgList);           //带多少Fgment就显示多少FG
                        bundle.putString(Constants.FG_DODE, segmentLists.get(position).getCode());  //这个是要跳转的 默认是baseInfo，第一个页面
                        bundle.putString(Constants.PROJECT_EID, proId);    //这个是projectID.
                        bundle.putString(Constants.USER_NAME, DisplayUtil.getUserInfo(FollowProjectDetailActivity.this, 11));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            }
        });
        recyclerView.setAdapter(segmentAdapter);

        btnAppl.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                initPopupWindowForApplyPermissions();
            }
        });

        //判断是否VC组
        if (SPUtils.get(this,SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(this,SPUtils.DEPT_ID,"").toString().equals("30")){
            btnProf.setText("转入跟进");
        }

        getProjectStateData();
        getFollowStateData();
        getData();
        getFollowon();

        setPicView(ll_water);
        setPicView(ll_water1);
        setPicView(ll_water2);
    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(this, 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(this, true, false, ll_water, myNickName);
        }
    }

    List<String> listFgTitle = new ArrayList<>();
    List<String> listFgList = new ArrayList<>();

    private void setFGTitleString(List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists)
    {
        listFgTitle.clear();
        listFgList.clear();
        for (int i = 0; i < segmentLists.size(); i++)
        {
            if (!segmentLists.get(i).getCode().equals("followUp"))
            {
                listFgTitle.add(segmentLists.get(i).getName());
                listFgList.add(segmentLists.get(i).getCode());
            }
        }
    }

    public void getData()
    {
        ListHttpHelper.getPotentialProjectsDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent()
                .getStringExtra("projId"), new SDRequestCallBack(PotentialProjectsDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PotentialProjectsDetailBean bean = (PotentialProjectsDetailBean) responseInfo.getResult();

                baseInfoMap = bean.getData().getData().getBaseInfoMap();
                if (baseInfoMap.getApplyFlag() == 0)
                {
                    btnAppl.setVisibility(View.GONE);
                } else if (baseInfoMap.getApplyFlag() == 1)
                {
                    btnAppl.setText("申请更多查看权限");
                } else if (baseInfoMap.getApplyFlag() == 2)
                {
                    btnAppl.setText("权限申请正在处理中");
                    btnAppl.setClickable(false);
                } else if (baseInfoMap.getApplyFlag() == 3)
                {
                    btnAppl.setText("申请权限成功");
                    btnAppl.setVisibility(View.GONE);
                } else
                {
                    btnAppl.setText("申请被驳回");
                }

                permission = baseInfoMap.getPermission();
                if (permission == 1)
                {
                    llBottom.setVisibility(View.GONE);
                } else
                {
                    llBottom.setVisibility(View.VISIBLE);
                }

                tvComInduStr.setText(baseInfoMap.getComInduStr());
                tvCurrentRound.setText(baseInfoMap.getCurrentRound());
                tvHeadCityStr.setText(baseInfoMap.getHeadCityStr());
                tvIndusGroupStr.setText(baseInfoMap.getIndusGroupStr());
                tvProjManagerNames.setText(baseInfoMap.getProjManagerNames());
                tvProjTeamNames.setText(baseInfoMap.getProjTeamNames());
                if (!StringUtils.isEmpty(baseInfoMap.getFollowUpStatus()))
                {
                    if (baseInfoMap.getFollowUpStatus().equals("0"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_hollow);
                        tvStarState.setText("取消跟踪");
                    } else if (baseInfoMap.getFollowUpStatus().equals("1"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_half);
                        tvStarState.setText("跟踪");
                    } else if (baseInfoMap.getFollowUpStatus().equals("2"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_all);
                        tvStarState.setText("重点跟踪");
                    }
                } else
                {
                    ivStar.setImageResource(R.mipmap.icon_star_hollow);
                    tvStarState.setText("取消跟踪");
                }
                if (bean.getData().getData().getSegmentList() != null && bean.getData().getData().getSegmentList().size() > 0)
                {
                    segmentLists.clear();
                    segmentLists.addAll(bean.getData().getData().getSegmentList());
                    segmentAdapter.notifyDataSetChanged();
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void getFollowon()
    {
        ListHttpHelper.getFollowonData(this, getIntent().getStringExtra("projId"), new SDRequestCallBack(FollowonListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                FollowonListBean bean = (FollowonListBean) responseInfo.getResult();
                datas.addAll(bean.getData().getData());
                horizonAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.ll_star, R.id.btn_add, R.id.btn_upload,R.id.btn_change,R.id.btn_prof})
    public void onItemClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            case R.id.ll_star:
                initPopupWindow(view);
                break;
            case R.id.btn_add:
                intent = new Intent(FollowProjectDetailActivity.this, TrackProgressAddActivity.class);
                intent.putExtra("projName", getIntent().getStringExtra("projName"));
                intent.putExtra("projId", getIntent().getStringExtra("projId"));
                intent.putExtra("flag", "DETAIL");
                startActivity(intent);
                break;
            case R.id.btn_upload:
                break;
            case R.id.btn_change:
                if (IDGDatas.size() > 0)
                {
                    initIDGDatasPopupWindow(view,1);
                } else
                {
                    SDToast.showShort("未获取数据");
                }
                break;
            case R.id.btn_prof:
                if (SPUtils.get(this,SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(this,SPUtils.DEPT_ID,"").toString().equals("30")){
                    updateFollowState();
                }else {
                    if (IDGDatas.size() > 0) {
                        initIDGDatasPopupWindow(view, 2);
                    } else {
                        SDToast.showShort("未获取数据");
                    }
                }
                break;
        }
    }

    private void initPopupWindow(View view)
    {
        final List<String> popuList = new ArrayList<>();

        if (!StringUtils.isEmpty(baseInfoMap.getFollowUpStatus()))
        {
            if (baseInfoMap.getFollowUpStatus().equals("0"))
            {
                popuList.add("跟踪");
                popuList.add("重点跟踪");
            } else if (baseInfoMap.getFollowUpStatus().equals("1"))
            {
                popuList.add("重点跟踪");
                popuList.add("取消跟踪");
            } else if (baseInfoMap.getFollowUpStatus().equals("2"))
            {
                popuList.add("跟踪");
                popuList.add("取消跟踪");
            }
        } else
        {
            popuList.add("跟踪");
            popuList.add("重点跟踪");
        }
        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter listPopupAdapter = new ListPopupAdapter(popuList, FollowProjectDetailActivity.this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {

                int type;
                if (popuList.get(i).equals("跟踪"))
                {
                    type = 1;
                } else if (popuList.get(i).equals("重点跟踪"))
                {
                    type = 2;
                } else
                {
                    type = 0;
                }
                updateFollowUpStatus(type + "");

                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    public void initPopupWindowForApplyPermissions()
    {

        View contentView = LayoutInflater.from(FollowProjectDetailActivity.this).inflate(R.layout.popupwindow_iafi, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(btnAppl, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
        TextView tvContent = (TextView) contentView.findViewById(R.id.tv_content);
        tvContent.setText("申请查看更多权限？");
        Button btnAgree = (Button) contentView.findViewById(R.id.btn_agree);
        Button btnDisagree = (Button) contentView.findViewById(R.id.btn_disagree);
        btnAgree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                applPermissions();
                popupWindow.dismiss();
            }
        });
        btnDisagree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                popupWindow.dismiss();
            }
        });

    }

    public void updateFollowUpStatus(final String follUpStatus)
    {
        ListHttpHelper.updateFollowUpStatus(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), follUpStatus, new SDRequestCallBack(IDGBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode()))
                {
                    baseInfoMap.setFollowUpStatus(follUpStatus);
                    if (follUpStatus.equals("0"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_hollow);
                        tvStarState.setText("取消跟踪");
                    } else if (follUpStatus.equals("1"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_half);
                        tvStarState.setText("跟踪");
                    } else if (follUpStatus.equals("2"))
                    {
                        ivStar.setImageResource(R.mipmap.icon_star_all);
                        tvStarState.setText("重点跟踪");
                    }

                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void applPermissions()
    {
        ListHttpHelper.applyForPermissions(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), getIntent().getStringExtra
                ("projId"), "", new SDRequestCallBack(IDGBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGBaseBean bean = (IDGBaseBean) responseInfo.getResult();
                if ("success".equals(bean.getData().getCode()))
                {
                    SDToast.showShort("权限申请正在处理中");
                } else
                {
                    SDToast.showShort(bean.getData().getReturnMessage());
                }
            }
        });
    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = alpha;
        getWindow().setAttributes(lp);
    }

    public void initlistPopupWindow(View view)
    {
        popuList.clear();
        if (permission == 1)
        {
            popuList.add(new IconListBean(R.mipmap.icon_popup_qxb, "企信宝查询", "2"));
        } else
        {
            popuList.add(new IconListBean(R.mipmap.at, "同事", "1"));
            popuList.add(new IconListBean(R.mipmap.icon_popup_qxb, "企信宝查询", "2"));
        }

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(340);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        listPopupWindow.setDropDownGravity(Gravity.RIGHT);
        final WorkCircleListPopupAdapter listPopupAdapter = new WorkCircleListPopupAdapter(popuList, this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (i == 0)
                {

                    if (permission == 1)
                    {
                        if (StringUtils.notEmpty(getIntent().getStringExtra("projName")))
                        {
                            Intent intent = new Intent(FollowProjectDetailActivity.this, ActivityQxbSearch.class);
                            intent.putExtra("companyName", getIntent().getStringExtra("projName"));
                            startActivity(intent);
                        }
                    } else
                    {
                        Intent intent = new Intent(FollowProjectDetailActivity.this, AtActivity.class);
                        intent.putExtra("projName", getIntent().getStringExtra("projName"));
                        intent.putExtra("projId", getIntent().getStringExtra("projId"));
                        startActivity(intent);
                    }

                } else if (i == 1)
                {
                    if (StringUtils.notEmpty(getIntent().getStringExtra("projName")))
                    {
                        Intent intent = new Intent(FollowProjectDetailActivity.this, ActivityQxbSearch.class);
                        intent.putExtra("companyName", getIntent().getStringExtra("projName"));
                        startActivity(intent);
                    }
                }
                listPopupWindow.dismiss();

            }
        });
        listPopupWindow.show();
    }

    private void getProjectStateData()
    {
        ListHttpHelper.getIDGBaseData(this, "potentialStatus", new SDRequestCallBack(IDGTpyeBaseBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                IDGTpyeBaseBean bean = (IDGTpyeBaseBean) responseInfo.getResult();
                IDGDatas.clear();
                IDGDatas.addAll(bean.getData().getData());
            }
        });
    }

    private void getFollowStateData(){
        ListHttpHelper.getFollowonStatusBaseData(this, new SDRequestCallBack(FollowonListBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showLong(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                FollowonListBean bean = (FollowonListBean) responseInfo.getResult();
                followStates.clear();
                followStates.addAll(bean.getData().getData());
            }
        });
    }

    private void updateProjectState(String pid, String sid)
    {
        ListHttpHelper.updateProjectStateData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), pid, sid, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (responseInfo.getStatus() == 200)
                        {
                            SDToast.showShort("修改成功!");
                        } else
                        {
                            SDToast.showShort("修改失败!");
                        }

                    }
                });
    }

    private void initIDGDatasPopupWindow(View view,final int flag)
    {
        final List<String> popuList = new ArrayList<>();

        if (flag == 1){
            for (int i = 0; i < followStates.size(); i++)
                popuList.add(followStates.get(i).getStatusCodeName());
        }else {
            for (int i = 0; i < IDGDatas.size(); i++)
                popuList.add(IDGDatas.get(i).getCodeNameZhCn());
        }

        final ListPopupWindow listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        listPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        ListPopupAdapter listPopupAdapter = new ListPopupAdapter(popuList, FollowProjectDetailActivity.this);
        listPopupWindow.setAdapter(listPopupAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setAnchorView(view);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                if (flag == 1){
                    updateProjectState(proId, followStates.get(i).getStatusCode());
                }else {
                    updateProjectState(proId, IDGDatas.get(i).getCodeKey());
                }
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
    }

    private void updateFollowState()
    {
        ListHttpHelper.updateFollowStateData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), proId, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        if (responseInfo.getStatus() == 200)
                        {
                            SDToast.showShort("转入跟进成功!");
                        } else
                        {
                            SDToast.showShort("转入跟进失败!");
                        }

                    }
                });
    }

}
