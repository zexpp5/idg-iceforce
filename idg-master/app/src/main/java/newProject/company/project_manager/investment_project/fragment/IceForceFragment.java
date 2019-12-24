package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.base.BaseFragment;
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
import newProject.company.fileLib.ActivityFileLibrary;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.project_manager.investment_project.AtActivity;
import newProject.company.project_manager.investment_project.FollowProjectDetailActivity;
import newProject.company.project_manager.investment_project.FollowProjectDetailForVCGroupActivity;
import newProject.company.project_manager.investment_project.FollowProjectList2Activity;
import newProject.company.project_manager.investment_project.FollowProjectListActivity;
import newProject.company.project_manager.investment_project.FollowProjectListForVCGroupActivity;
import newProject.company.project_manager.investment_project.InvestedProjectList2Activity;
import newProject.company.project_manager.investment_project.InvestedProjectListActivity;
import newProject.company.project_manager.investment_project.InvestedProjectsDetailActivity;
import newProject.company.project_manager.investment_project.InvestmentAndFinancingInformationActivity;
import newProject.company.project_manager.investment_project.ItemRatingActivity;
import newProject.company.project_manager.investment_project.NewsLetterActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementDetailActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementForPartnerActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsAddActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsDetailActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsList2Activity;
import newProject.company.project_manager.investment_project.PotentialProjectsListForVCGroupActivity;
import newProject.company.project_manager.investment_project.ProjectActivity;
import newProject.company.project_manager.investment_project.ProjectLibraryActivity;
import newProject.company.project_manager.investment_project.ProjectLibrarySearchActivity;
import newProject.company.project_manager.investment_project.ResearchReportActivity;
import newProject.company.project_manager.investment_project.TrackProgressAddActivity;
import newProject.company.project_manager.investment_project.TrackProgressIceForceActivity;
import newProject.company.project_manager.investment_project.TrackProgressStatusActivity;
import newProject.company.project_manager.investment_project.WorkCircleActivity;
import newProject.company.project_manager.investment_project.WorkLogActivity;
import newProject.company.project_manager.investment_project.adapter.FollowProjectMultiListAdapter;
import newProject.company.project_manager.investment_project.adapter.IceForceAdapter;
import newProject.company.project_manager.investment_project.adapter.PopuVoiceListAdapter;
import newProject.company.project_manager.investment_project.adapter.ToDoListAdapter;
import newProject.company.project_manager.investment_project.adapter.VoiceListAdapter;
import newProject.company.project_manager.investment_project.bean.FollowProjectBaseBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectListBean;
import newProject.company.project_manager.investment_project.bean.FollowProjectMultiBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsPersonalBean;
import newProject.company.project_manager.investment_project.bean.ToDoListBean;
import newProject.company.project_manager.investment_project.voice.VoiceListBean;
import newProject.company.project_manager.investment_project.voice.VoiceUtils;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/4/9.
 */

public class IceForceFragment extends BaseFragment
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;

    @Bind(R.id.to_do_list_recycler)
    RecyclerView toDoListRecycler;

    @Bind(R.id.tv_potential_str)
    TextView tvPotentialStr;

    ToDoListAdapter toDoListAdapter;

    List<ToDoListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    @Bind(R.id.follow_project_recycler)
    RecyclerView followProjectRecycler;

    FollowProjectMultiListAdapter followProjectMultiListAdapter;

    List<FollowProjectMultiBean> followProjectMultiBeans = new ArrayList<>();

    IceForceAdapter followAdapter;
    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> followDatas = new ArrayList<>();
    IceForceAdapter potentialAdapter;
    List<PotentialProjectsPersonalBean.DataBeanX.DataBean> potentialDatas = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_iceforce;
    }

    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);

        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });

        mNavigatorBar.setRightImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), PotentialProjectsAddActivity.class);
                startActivity(intent);
            }
        });
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setRightSecondImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProjectLibrarySearchActivity.class);
                startActivityForResult(intent,100);
            }
        });
        mNavigatorBar.setMidText("IceForce");

        //判断是否VC组
        if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")) {
            tvPotentialStr.setText("新项目");
        }

        toDoListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        followAdapter = new IceForceAdapter(followDatas);
        toDoListRecycler.setAdapter(followAdapter);
        followAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")) {
                    intent.setClass(getActivity(), FollowProjectDetailForVCGroupActivity.class);
                    intent.putExtra("permission", followDatas.get(position).getPermission());
                }else {
                    intent.setClass(getActivity(), FollowProjectDetailActivity.class);
                }
                intent.putExtra("projName", followDatas.get(position).getProjName());
                intent.putExtra("projId", followDatas.get(position).getProjId());
                startActivity(intent);
            }
        });

        followProjectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        potentialAdapter = new IceForceAdapter(potentialDatas);
        followProjectRecycler.setAdapter(potentialAdapter);
        potentialAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PostInvestmentManagementDetailActivity.class);
                intent.putExtra("projName", potentialDatas.get(position).getProjName());
                intent.putExtra("projId", potentialDatas.get(position).getProjId());
                startActivity(intent);
            }
        });



        /*followProjectRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        followProjectMultiListAdapter = new FollowProjectMultiListAdapter(followProjectMultiBeans);
        followProjectRecycler.setAdapter(followProjectMultiListAdapter);
        followProjectMultiListAdapter.notifyDataSetChanged();
        followProjectMultiListAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener()
        {
            @Override
            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position)
            {
                initPopupWindow((View) view.getParent(), position);
                return true;
            }
        });
        followProjectMultiListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.ll_voice:
                        if (followProjectMultiBeans.get(position).getData().getUrl().contains(","))
                        {
                            initVoiceListPopupWindow(view, followProjectMultiBeans.get(position).getData());
                        } else
                        {
                            ImageView ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
                            ivVoice.setBackgroundColor(0xffffffff);
                            ivVoice.setImageResource(R.drawable.play_annex_voice);
                            VoiceUtils.getInstance().getVoiceFromNet(getActivity(), followProjectMultiBeans.get(position)
                                    .getData().getUrl(), ivVoice);
                        }
                        break;
                    case R.id.text_name:
                        Intent intent = new Intent();
                        if ("POTENTIAL".equals(followProjectMultiBeans.get(position).getData().getProjType()))
                        {
                            intent.setClass(getActivity(), PotentialProjectsDetailActivity.class);
                        } else if ("INVESTED".equals(followProjectMultiBeans.get(position).getData().getProjType()))
                        {
                            intent.setClass(getActivity(), InvestedProjectsDetailActivity.class);
                        } else if ("FOLLOW_ON".equals(followProjectMultiBeans.get(position).getData().getProjType()))
                        {
                            intent.setClass(getActivity(), FollowProjectDetailActivity.class);
                        } else
                        {
                            SDToast.showShort("未知项目类型");
                            return;
                        }
                        intent.putExtra("projName", followProjectMultiBeans.get(position).getData().getProjName());
                        intent.putExtra("projId", followProjectMultiBeans.get(position).getData().getProjId());
                        startActivity(intent);
                        break;
                }
            }
        });
        toDoListRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        toDoListAdapter = new ToDoListAdapter(datas);
        toDoListRecycler.setAdapter(toDoListAdapter);
        toDoListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId()){
                    case R.id.tv_content:
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
                    case R.id.tv_name:
                        Intent intent = new Intent(getActivity(), PotentialProjectsDetailActivity.class);
                        intent.putExtra("projName", datas.get(position).getProjName());
                        intent.putExtra("projId", datas.get(position).getBusId());
                        startActivity(intent);
                        break;
                }
            }
        });*/

    }

    @Override
    public void onStart()
    {
        super.onStart();
        //getFollowProjectData();
        //getTodoEventData();
        //判断是否VC组
        if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")){
            getFollowForVCGroupData();
        }else {
            getFollowData();
        }

        //getPotentialData();
        getInvestedData();
    }

    @OnClick({/*R.id.ll_invested,*/R.id.ll_work_log, R.id.ll_follow_on, R.id.ll_potential_projects, R.id.tv_more, R.id.tv_more_need, R.id
            .ll_investment_and_financing, R.id.ll_research_report, R.id.ll_news_letter, R.id.ll_post_investment, R.id
            .ll_file_library})
    public void OnItemClick(View view)
    {
        Intent intent;
        switch (view.getId())
        {
            /*case R.id.ll_invested:
                if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    intent = new Intent(getActivity(), InvestedProjectListActivity.class);
                    startActivity(intent);
                } else
                {
                    intent = new Intent(getActivity(), InvestedProjectList2Activity.class);
                    startActivity(intent);
                }
                break;*/
            case R.id.ll_work_log:
                intent = new Intent(getActivity(), WaitingToSeeProjectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_follow_on:

                //判断是否VC组
                if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1") || SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")){
                    intent = new Intent(getActivity(), FollowProjectListForVCGroupActivity.class);
                    startActivity(intent);
                    return;
                }

                if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    intent = new Intent(getActivity(), FollowProjectListActivity.class);
                    startActivity(intent);
                } else
                {
                    intent = new Intent(getActivity(), FollowProjectList2Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_potential_projects:

                //判断是否VC组
                if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")){
                    intent = new Intent(getActivity(), PotentialProjectsListForVCGroupActivity.class);
                    startActivity(intent);
                    return;
                }

                if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    intent = new Intent(getActivity(), PotentialProjectsActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), PotentialProjectsList2Activity.class);
                    startActivity(intent);
                }
                break;
            case R.id.tv_more:
                /*intent = new Intent(getActivity(), WorkCircleActivity.class);
                startActivity(intent);*/
                String flag = (String)SPUtils.get(getActivity(), SPUtils.ROLE_FLAG, "0");
                if (flag.equals("205")) {
                    intent = new Intent(getActivity(), PostInvestmentManagementActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), PostInvestmentManagementForPartnerActivity.class);
                    startActivity(intent);
                }
                /*intent = new Intent(getActivity(), PotentialProjectsList2Activity.class);
                startActivity(intent);*/
                break;
            case R.id.tv_more_need:
                /*((ProjectActivity) getActivity()).changeFragment(1);*/
                //判断是否VC组
                if (SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("1")|| SPUtils.get(getActivity(),SPUtils.DEPT_ID,"").toString().equals("30")){
                    intent = new Intent(getActivity(), FollowProjectListForVCGroupActivity.class);
                    startActivity(intent);
                }else {
                    intent = new Intent(getActivity(), FollowProjectList2Activity.class);
                    startActivity(intent);
                }

                break;
            case R.id.ll_investment_and_financing:
                intent = new Intent(getActivity(), InvestmentAndFinancingInformationActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_research_report:
                intent = new Intent(getActivity(), ResearchReportActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_news_letter:
                intent = new Intent(getActivity(), NewsLetterActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_post_investment:
                if (SPUtils.get(getActivity(), SPUtils.ROLE_FLAG, "0").toString().equals("205")) {
                    intent = new Intent(getActivity(), PostInvestmentManagementActivity.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(getActivity(), PostInvestmentManagementForPartnerActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.ll_file_library:
                intent = new Intent(getActivity(), ActivityFileLibrary.class);
                startActivity(intent);
                break;
        }
    }

    private void getFollowProjectData()
    {
        ListHttpHelper.getFollowProjectList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "1", "5",
                "", "", "", "", new SDRequestCallBack(FollowProjectListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        FollowProjectListBean bean = (FollowProjectListBean) responseInfo.getResult();
                        followProjectMultiBeans.clear();
                        for (int i = 0; i < bean.getData().getData().size(); i++)
                        {
                            int itemType = 0;
                            if (bean.getData().getData().get(i).getContentType().equals("TEXT"))
                            {
                                if (bean.getData().getData().get(i).getFollowType().equals("UPDATE_STATE"))
                                {
                                    itemType = 2;
                                } else
                                {
                                    itemType = 1;
                                }
                            } else
                            {
                                //音频
                                itemType = 3;
                            }
                            followProjectMultiBeans.add(new FollowProjectMultiBean(itemType, bean.getData().getData().get(i)));
                        }
                        followProjectMultiListAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void getTodoEventData()
    {
        ListHttpHelper.getToDoEventList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "TODO", "0",
                "3", new SDRequestCallBack(ToDoListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        ToDoListBean bean = (ToDoListBean) responseInfo.getResult();
                        toDoListAdapter.getData().clear();
                        toDoListAdapter.getData().addAll(bean.getData().getData());
                        toDoListAdapter.notifyDataSetChanged();
                    }
                });
    }


    private void initPopupWindow(View view, final int pos)
    {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_share, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup
                .LayoutParams.WRAP_CONTENT, true);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, location[1]
                - popupHeight);
        LinearLayout llShare = (LinearLayout) contentView.findViewById(R.id.ll_share);
        llShare.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(getActivity(), AtActivity.class);
                intent.putExtra("projName", followProjectMultiBeans.get(pos).getData().getProjName());
                intent.putExtra("projId", followProjectMultiBeans.get(pos).getData().getProjId());
                startActivity(intent);
                popupWindow.dismiss();
            }
        });
    }

    private void initVoiceListPopupWindow(View view, FollowProjectBaseBean bean)
    {
        String[] urls = bean.getUrl().split(",");
        String[] times = bean.getAudioTime().split(",");
        List<VoiceListBean> voiceListBeen = new ArrayList<>();
        for (int i = 0; i < urls.length; i++)
        {
            voiceListBeen.add(new VoiceListBean(Integer.parseInt(times[i]), urls[i]));
        }

        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.popupwindow_voice_list, null);
        RecyclerView voiceRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler_view);
        voiceRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        PopuVoiceListAdapter adapter = new PopuVoiceListAdapter(voiceListBeen);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position)
            {
                switch (view.getId())
                {
                    case R.id.iv_voice:
                        VoiceUtils.getInstance().getVoiceFromNet(getActivity(), ((VoiceListBean) adapter.getData().get
                                (position)).getUrl(), (ImageView) view);
                        break;
                }
            }
        });
        voiceRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                .WRAP_CONTENT, true);
        setBackgroundAlpha(0.5f);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
        {
            @Override
            public void onDismiss()
            {
                setBackgroundAlpha(1f);
            }
        });
    }

    public void setBackgroundAlpha(float alpha)
    {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = alpha;
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            ((ProjectActivity) getActivity()).setProjectLibraryIntentData(data);
            ((ProjectActivity) getActivity()).changeFragment(3);
            ((ProjectActivity) getActivity()).projectLibraryFragment.postSearchData();
        }
    }

    public void getFollowData(){
        ListHttpHelper.getPotentialPersonalList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "FOLLOW_ON","1", "5", "", "", "", "", "", "", new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                followDatas.clear();
                followDatas.addAll(bean.getData().getData());
                followAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getPotentialData(){
        ListHttpHelper.getPotentialPersonalList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "POTENTIAL", "1", "5", "", "", "", "", "", "", new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                potentialDatas.clear();
                potentialDatas.addAll(bean.getData().getData());
                potentialAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getFollowForVCGroupData(){
        ListHttpHelper.getPotentialPersonalListForVCGroup(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(),  "1", "5",  "", "", "", "","","1", "",new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                followDatas.clear();
                followDatas.addAll(bean.getData().getData());
                followAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getInvestedData(){
        ListHttpHelper.getPotentialPersonalList(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), "INVESTED", "1", "5", "", "", "", "", "", "", new SDRequestCallBack(PotentialProjectsPersonalBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {

            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                PotentialProjectsPersonalBean bean = (PotentialProjectsPersonalBean) responseInfo.getResult();
                potentialDatas.clear();
                potentialDatas.addAll(bean.getData().getData());
                potentialAdapter.notifyDataSetChanged();
            }
        });
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
