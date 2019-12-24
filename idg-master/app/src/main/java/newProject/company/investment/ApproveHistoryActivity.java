package newProject.company.investment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.investment.fragment.HistoryFragment;
import newProject.company.investment.fragment.NeedToDo2Fragment;
import newProject.company.investment.fragment.PendingFragment;
import newProject.company.investment.fragment.Remind2Fragment;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by zsz on 2019/8/26.
 */

public class ApproveHistoryActivity extends BaseActivity {
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    private String[] mTitleList = {"请假", "报销", "差旅"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected int getContentLayout() {
        return R.layout.activity_approve_history;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mViewList.clear();
        HistoryFragment fragment1 = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flag","0");
        fragment1.setArguments(bundle);
        PendingFragment fragment2 = new PendingFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("flag","1");
        fragment2.setArguments(bundle2);
        HistoryFragment fragment3 = new HistoryFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putString("flag","2");
        fragment3.setArguments(bundle3);
        mViewList.add(fragment1);
        mViewList.add(fragment2);
        mViewList.add(fragment3);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
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
}
