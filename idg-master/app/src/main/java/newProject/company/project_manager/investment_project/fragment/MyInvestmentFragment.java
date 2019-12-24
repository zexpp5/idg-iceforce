package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
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
import newProject.company.project_manager.investment_project.FeedBackActivity;
import newProject.company.project_manager.investment_project.FollowOnSummaryActivity;
import newProject.company.project_manager.investment_project.FollowProjectList2Activity;
import newProject.company.project_manager.investment_project.FollowProjectListActivity;
import newProject.company.project_manager.investment_project.InvestedProjectList2Activity;
import newProject.company.project_manager.investment_project.InvestedProjectListActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsList2Activity;
import newProject.company.project_manager.investment_project.WorkCircleActivity;
import newProject.company.project_manager.investment_project.WorkLogActivity;
import newProject.company.project_manager.investment_project.WorkSummaryListActivity;
import newProject.company.project_manager.investment_project.adapter.MyInvestmentAdapter;
import newProject.company.project_manager.investment_project.bean.MyDoneNumberBean;
import newProject.company.project_manager.investment_project.bean.MyFollowOnNumberBean;
import newProject.company.project_manager.investment_project.bean.ProjectNumberBean;
import newProject.company.project_manager.investment_project.bean.Top3ListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static com.utils.SPUtils.USER_ACCOUNT;
import static com.utils.SPUtils.USER_NAME;

/**
 * 我的--投资经理
 * Created by zsz on 2019/4/29.
 */

public class MyInvestmentFragment extends BaseFragment
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.head_bar_icon)
    RoundedImageView headBarIcon;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_invest_number)
    TextView tvInvestNumber;
    @Bind(R.id.tv_follow_number)
    TextView tvFollowNumber;
    @Bind(R.id.tv_potential_number)
    TextView tvPotentialNumber;
    @Bind(R.id.tv_follow_date)
    TextView tvFollowDate;
    @Bind(R.id.tv_follow_undo)
    TextView tvFollowUndo;
    @Bind(R.id.tv_follow_done)
    TextView tvFollowDone;
    @Bind(R.id.tv_project_personal)
    TextView tvProjectPersonal;
    @Bind(R.id.tv_project_group)
    TextView tvProjectGroup;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_done_potential_number)
    TextView tvDonePotentialNumber;
    @Bind(R.id.tv_done_recommend_number)
    TextView tvDoneRecommendNumber;
    @Bind(R.id.tv_done_group_number)
    TextView tvDoneGroupNumber;
    @Bind(R.id.tv_done_title)
    TextView tvDoneTitle;

    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.ll_water1)
    LinearLayout ll_water1;
    @Bind(R.id.ll_water2)
    LinearLayout ll_water2;

    MyInvestmentAdapter adapter;
    List<Top3ListBean.DataBeanX.DataBean> personalLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    //日期，带到详情页面
    String followDate;

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_my_investment;
    }



    @Override
    protected void init(View view)
    {
        ButterKnife.bind(this, view);
        mNavigatorBar.setLeftImageVisible(false);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new MyInvestmentAdapter(datas);
        recyclerView.setAdapter(adapter);

        tvUserName.setText(SPUtils.get(getActivity(), USER_NAME, "").toString());

        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(), 0))
                .error(R.mipmap.temp_user_head)
                .into(headBarIcon);

        getNumberData();
        getFollowOnTotalData();
        getDoneData();
        getTop3Data("false");
        getTop3Data("true");

        setPicView(ll_water);
        setPicView(ll_water1);
        setPicView(ll_water2);
    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(getActivity(), true, false, ll_water, myNickName);
        }
    }

    @OnClick({R.id.tv_detail, R.id.tv_more, R.id.tv_project_personal, R.id.tv_project_group,R.id.rl_post,R.id.rl_work_circle,R.id.rl_feedback,
            R.id.ll_invest,R.id.ll_follow,R.id.ll_potential,R.id.ll_follow_on_done,R.id.ll_follow_on_undo,
            R.id.ll_work_group,R.id.ll_work_potential,R.id.ll_work_recommend,R.id.rl_work_log})
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.ll_follow_on_done:
            case R.id.ll_follow_on_undo:
            case R.id.tv_detail:
                Intent de = new Intent(getActivity(), FollowOnSummaryActivity.class);
                de.putExtra("date", followDate);
                startActivity(de);
                break;
            case R.id.tv_more:
                Intent intent = new Intent(getActivity(), WorkSummaryListActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_project_personal:
                datas.clear();
                datas.addAll(personalLists);
                adapter.notifyDataSetChanged();
                tvProjectPersonal.setTextColor(getResources().getColor(R.color.white));
                tvProjectPersonal.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvProjectGroup.setTextColor(getResources().getColor(R.color.top_bg));
                tvProjectGroup.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                break;
            case R.id.tv_project_group:
                datas.clear();
                datas.addAll(groupLists);
                adapter.notifyDataSetChanged();
                tvProjectGroup.setTextColor(getResources().getColor(R.color.white));
                tvProjectGroup.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_choose));
                tvProjectPersonal.setTextColor(getResources().getColor(R.color.top_bg));
                tvProjectPersonal.setBackground(getResources().getDrawable(R.drawable.tv_my_bg_unchoose));
                break;
            case R.id.rl_post:
                Intent post = new Intent(getActivity(), PostInvestmentManagementActivity.class);
                startActivity(post);
                break;
            case R.id.rl_work_circle:
                Intent wrok = new Intent(getActivity(), WorkCircleActivity.class);
                startActivity(wrok);
                break;
            case R.id.rl_work_log:
                Intent log = new Intent(getActivity(), WorkLogActivity.class);
                startActivity(log);
                break;
            case R.id.rl_feedback:
                Intent feedback = new Intent(getActivity(), FeedBackActivity.class);
                startActivity(feedback);
                break;
            case R.id.ll_invest:
                /*if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    Intent intent1 = new Intent(getActivity(), InvestedProjectListActivity.class);
                    startActivity(intent1);
                } else
                {
                    Intent intent2 = new Intent(getActivity(), InvestedProjectList2Activity.class);
                    startActivity(intent2);
                }*/
                Intent intent2 = new Intent(getActivity(), PostInvestmentManagementActivity.class);
                startActivity(intent2);
                break;
            case R.id.ll_potential:
                if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    Intent intent3 = new Intent(getActivity(), PotentialProjectsActivity.class);
                    startActivity(intent3);
                } else
                {
                    Intent intent4 = new Intent(getActivity(), PotentialProjectsList2Activity.class);
                    startActivity(intent4);
                }
                break;
            case R.id.ll_follow:
                if (SPUtils.get(getActivity(), SPUtils.IS_MANAGER_MY, "0").equals("1") && SPUtils.get(getActivity(), SPUtils
                        .IS_MANAGER_TEAM, "0").equals("1"))
                {
                    Intent intent5 = new Intent(getActivity(), FollowProjectListActivity.class);
                    startActivity(intent5);
                } else
                {
                    Intent intent6 = new Intent(getActivity(), FollowProjectList2Activity.class);
                    startActivity(intent6);
                }
                break;
            case R.id.ll_work_group:
                Intent itg = new Intent(getActivity(), WorkSummaryListActivity.class);
                itg.putExtra("flag","group");
                startActivity(itg);
                break;
            case R.id.ll_work_potential:
                Intent itg2 = new Intent(getActivity(), WorkSummaryListActivity.class);
                itg2.putExtra("flag","potential");
                startActivity(itg2);
                break;
            case R.id.ll_work_recommend:
                Intent itg3 = new Intent(getActivity(), WorkSummaryListActivity.class);
                itg3.putExtra("flag","recommend");
                startActivity(itg3);
                break;
        }
    }

    public void getNumberData()
    {
        ListHttpHelper.getMyInvestmentNumberData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), new
                SDRequestCallBack(ProjectNumberBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        ProjectNumberBean bean = (ProjectNumberBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode()))
                        {
                            tvInvestNumber.setText(bean.getData().getData().getInvested() + "");
                            tvFollowNumber.setText(bean.getData().getData().getFollowUp() + "");
                            tvPotentialNumber.setText(bean.getData().getData().getPotencial() + "");
                        }
                    }
                });
    }

    public void getFollowOnTotalData()
    {
        ListHttpHelper.getMyInvestmentFollowOnTotalData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(),
                new SDRequestCallBack(MyFollowOnNumberBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        MyFollowOnNumberBean bean = (MyFollowOnNumberBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode()))
                        {
                            tvFollowDate.setText("项目跟踪进展(" + bean.getData().getData().getDate() + ")");
                            tvFollowUndo.setText(bean.getData().getData().getUnRead() + "");
                            tvFollowDone.setText(bean.getData().getData().getDone() + "");
                            followDate = bean.getData().getData().getDate();
                        }
                    }
                });
    }

    public void getDoneData()
    {
        ListHttpHelper.getMyInvestmentDoneData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), new
                SDRequestCallBack(MyDoneNumberBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        MyDoneNumberBean bean = (MyDoneNumberBean) responseInfo.getResult();
                        if ("success".equals(bean.getData().getCode()))
                        {
                            tvDoneTitle.setText("已完成工作汇总(" + bean.getData().getData().getDate() + ")");
                            tvDoneRecommendNumber.setText(bean.getData().getData().getRecommendedPartner() + "");
                            tvDonePotentialNumber.setText(bean.getData().getData().getPotential() + "");
                            tvDoneGroupNumber.setText(bean.getData().getData().getGroup() + "");
                        }
                    }
                });
    }

    public void getTop3Data(final String flag)
    {
        ListHttpHelper.getMyInvestmentTop3Data(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), flag,"3",
                new SDRequestCallBack(Top3ListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        Top3ListBean bean = (Top3ListBean) responseInfo.getResult();
                        if (flag.equals("false"))
                        {
                            //个人
                            personalLists.clear();
                            personalLists.addAll(bean.getData().getData());
                            datas.clear();
                            datas.addAll(personalLists);
                            adapter.notifyDataSetChanged();
                        } else if (flag.equals("true"))
                        {
                            //小组
                            groupLists.clear();
                            groupLists.addAll(bean.getData().getData());
                        }
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
