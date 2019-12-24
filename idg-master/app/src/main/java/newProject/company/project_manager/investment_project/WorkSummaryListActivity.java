package newProject.company.project_manager.investment_project;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.project_manager.investment_project.fragment.FollowProjectGroupFragment;
import newProject.company.project_manager.investment_project.fragment.FollowProjectPersonalFragment;
import newProject.company.project_manager.investment_project.fragment.WorkSummaryTabFragment;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by zsz on 2019/4/25.
 */

public class WorkSummaryListActivity extends BaseActivity {
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private String[] mTitleList = {"新建潜在项目", "推荐合伙人项目","行业小组讨论"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合


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
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(true);
        mNavigatorBar.setMidText("工作汇总");
        mViewList.clear();
        WorkSummaryTabFragment fragment = new WorkSummaryTabFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("flag",WorkSummaryTabFragment.POTENTIAL);
        fragment.setArguments(bundle);
        mViewList.add(fragment);
        WorkSummaryTabFragment recommendFragment = new WorkSummaryTabFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("flag",WorkSummaryTabFragment.RECOMMENDED);
        recommendFragment.setArguments(bundle1);
        mViewList.add(recommendFragment);
        WorkSummaryTabFragment groupFragment = new WorkSummaryTabFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("flag",WorkSummaryTabFragment.GROUP);
        groupFragment.setArguments(bundle2);
        mViewList.add(groupFragment);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        if (StringUtils.notEmpty(getIntent().getStringExtra("flag"))){
            if (getIntent().getStringExtra("flag").equals("potential")){
                mViewPager.setCurrentItem(0);
            }else if (getIntent().getStringExtra("flag").equals("recommend")){
                mViewPager.setCurrentItem(1);
            }else if (getIntent().getStringExtra("flag").equals("group")){
                mViewPager.setCurrentItem(2);
            }
        }

    }



    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mViewList = new ArrayList<>();
        private String[] mTitle;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title) {
            super(fm);
            this.mViewList = fragmentList;
            this.mTitle = title;
        }

        @Override
        public int getCount() {
            return mViewList.size();//页卡数
        }

        @Override
        public Fragment getItem(int position) {
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitle[position];//页卡标题
        }

    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_public_tab;
    }
}
