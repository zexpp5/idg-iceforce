package newProject.company.project_manager.investment_project.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseFragment;
import com.cxgz.activity.cxim.bean.PersonalListBean;
import com.cxgz.activity.cxim.http.ImHttpHelper;
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
import newProject.company.project_manager.investment_project.FollowProjectList2Activity;
import newProject.company.project_manager.investment_project.FollowProjectListActivity;
import newProject.company.project_manager.investment_project.InvestedProjectList2Activity;
import newProject.company.project_manager.investment_project.InvestedProjectListActivity;
import newProject.company.project_manager.investment_project.PostInvestmentManagementForPartnerActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsActivity;
import newProject.company.project_manager.investment_project.PotentialProjectsList2Activity;
import newProject.company.project_manager.investment_project.WorkCircleActivity;
import newProject.company.project_manager.investment_project.WorkLogActivity;
import newProject.company.project_manager.investment_project.WorkSummaryDetailActivity;
import newProject.company.project_manager.investment_project.adapter.MyInvestmentAdapter;
import newProject.company.project_manager.investment_project.adapter.MyPartnerGroupAdapter;
import newProject.company.project_manager.investment_project.bean.MyPartnerGroupBean;
import newProject.company.project_manager.investment_project.bean.ProjectNumberBean;
import newProject.company.project_manager.investment_project.bean.Top3ListBean;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.RoundedImageView;

import static com.utils.SPUtils.USER_ACCOUNT;
import static com.utils.SPUtils.USER_NAME;

/**
 * 我的--合伙人
 * Created by zsz on 2019/4/29.
 */

public class MyPartnerFragment extends BaseFragment
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_user_name)
    TextView tvUserName;
    @Bind(R.id.tv_invest_number)
    TextView tvInvestNumber;
    @Bind(R.id.tv_follow_number)
    TextView tvFollowNumber;
    @Bind(R.id.tv_potential_number)
    TextView tvPotentialNumber;
    @Bind(R.id.tv_project_personal)
    TextView tvProjectPersonal;
    @Bind(R.id.tv_project_group)
    TextView tvProjectGroup;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_group_title)
    TextView tvGroupTitle;
    @Bind(R.id.tv_follow)
    TextView tvFollow;
    @Bind(R.id.tv_new)
    TextView tvNew;
    @Bind(R.id.tv_partner)
    TextView tvPartner;
    @Bind(R.id.tv_group)
    TextView tvGroup;
    @Bind(R.id.recycler_view_group)
    RecyclerView recyclerViewGroup;
    @Bind(R.id.ll_priv)
    LinearLayout llPriv;
    @Bind(R.id.ll_water)
    LinearLayout ll_water;
    @Bind(R.id.ll_water1)
    LinearLayout ll_water1;


    MyInvestmentAdapter adapter;
    List<Top3ListBean.DataBeanX.DataBean> personalLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> groupLists = new ArrayList<>();
    List<Top3ListBean.DataBeanX.DataBean> datas = new ArrayList<>();

    MyPartnerGroupAdapter groupAdapter;
    List<MyPartnerGroupBean.DataBeanX.DataBean> myPartnerGroupLists = new ArrayList<>();
    @Bind(R.id.head_bar_icon)
    RoundedImageView headBarIcon;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    @Bind(R.id.iv_img3)
    ImageView ivImg3;
    @Bind(R.id.rl_post)
    RelativeLayout rlPost;
    @Bind(R.id.iv_img5)
    ImageView ivImg5;
    @Bind(R.id.rl_work_circle)
    RelativeLayout rlWorkCircle;
    @Bind(R.id.iv_img4)
    ImageView ivImg4;
    @Bind(R.id.rl_feedback)
    RelativeLayout rlFeedback;

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_my_partner;
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

        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(getActivity()));
        groupAdapter = new MyPartnerGroupAdapter(myPartnerGroupLists);
        recyclerViewGroup.setAdapter(groupAdapter);

        String flag = (String) SPUtils.get(getActivity(), SPUtils.ROLE_FLAG, "0");
        if (flag.equals("12") || flag.equals("207") || flag.equals("10"))
        {
            llPriv.setVisibility(View.GONE);
        } else
        {
            llPriv.setVisibility(View.VISIBLE);
        }

        Glide.with(getActivity())
                .load(DisplayUtil.getUserInfo(getActivity(), 0))
                .error(R.mipmap.temp_user_head)
                .into(headBarIcon);
        //updatePersonalInfo();
        getNumberData();
        getGroupRecycleData(1);
        getTop3Data("false");
        getTop3Data("true");

        setPicView(ll_water);
        setPicView(ll_water1);
    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(getActivity(), 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(getActivity(), true, false, ll_water, myNickName);
        }
    }

    @OnClick({R.id.tv_follow, R.id.tv_new, R.id.tv_partner, R.id.tv_group, R.id.tv_detail, R.id.tv_project_personal, R.id
            .tv_project_group, R.id.rl_post, R.id.rl_work_circle, R.id.rl_feedback,
            R.id.ll_invest, R.id.ll_follow, R.id.ll_potential, R.id.rl_work_log})
    public void onItemClick(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_follow:
                tvFollow.setTextColor(getResources().getColor(R.color.blue));
                tvNew.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvPartner.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvGroup.setTextColor(getResources().getColor(R.color.text_black_xl));
                getGroupRecycleData(1);
                break;
            case R.id.tv_new:
                tvFollow.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvNew.setTextColor(getResources().getColor(R.color.blue));
                tvPartner.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvGroup.setTextColor(getResources().getColor(R.color.text_black_xl));
                getGroupRecycleData(2);
                break;
            case R.id.tv_partner:
                tvFollow.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvNew.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvPartner.setTextColor(getResources().getColor(R.color.blue));
                tvGroup.setTextColor(getResources().getColor(R.color.text_black_xl));
                getGroupRecycleData(3);
                break;
            case R.id.tv_group:
                tvFollow.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvNew.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvPartner.setTextColor(getResources().getColor(R.color.text_black_xl));
                tvGroup.setTextColor(getResources().getColor(R.color.blue));
                getGroupRecycleData(4);
                break;
            case R.id.tv_detail:
                Intent detail = new Intent(getActivity(), WorkSummaryDetailActivity.class);
                startActivity(detail);
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
                Intent post = new Intent(getActivity(), PostInvestmentManagementForPartnerActivity.class);
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
                Intent intent2 = new Intent(getActivity(), PostInvestmentManagementForPartnerActivity.class);
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
        }
    }

    private String imAccount;

    private void updatePersonalInfo()
    {
        imAccount = (String) SPUtils.get(getActivity(), SPUtils.IM_NAME, "");
        if (StringUtils.empty(imAccount))
        {
            MyToast.showToast(getActivity(), R.string.request_fail_data);
            return;
        }

        ImHttpHelper.findPersonInfoByImAccount(getActivity(), true, mHttpHelper, imAccount, new
                SDRequestCallBack(PersonalListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
//                MyToast.showToast(SDPersonalAlterActivity.this, "刷新个人信息失败！");
                        MyToast.showToast(getActivity(), msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PersonalListBean personalListBean = (PersonalListBean) responseInfo.getResult();
                        if (personalListBean != null)
                        {

                        }

                    }
                });
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

    public void getTop3Data(final String flag)
    {
        ListHttpHelper.getMyInvestmentTop3Data(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), flag,
                "3", new SDRequestCallBack(Top3ListBean.class)
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

    public void getGroupRecycleData(int index)
    {
        ListHttpHelper.getMyPartnerOneToFourData(getActivity(), SPUtils.get(getActivity(), USER_ACCOUNT, "").toString(), index,
                new SDRequestCallBack(MyPartnerGroupBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyPartnerGroupBean bean = (MyPartnerGroupBean) responseInfo.getResult();
                if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                {
                    myPartnerGroupLists.clear();
                    for (int i = 0; i < 4; i++)
                    {
                        myPartnerGroupLists.add(bean.getData().getData().get(i));
                    }
                    groupAdapter.notifyDataSetChanged();
                    tvGroupTitle.setText("小组工作汇总(" + myPartnerGroupLists.get(0).getCurMonth() + ")");
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
