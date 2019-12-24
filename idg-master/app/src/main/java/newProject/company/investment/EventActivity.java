package newProject.company.investment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.event.fragment.NeedToDoFragment;
import newProject.company.event.fragment.RemindFragment;
import newProject.company.investment.fragment.NeedToDo2Fragment;
import newProject.company.investment.fragment.Remind2Fragment;
import yunjing.view.CustomNavigatorBar;

//事项
public class EventActivity extends BaseActivity
{
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private String[] mTitleList = {"待办", "提醒"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected int getContentLayout()
    {
        return R.layout.fragment_event;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
//        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
//        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setLeftImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText(getString(R.string.super_tab_03));

        mViewList.clear();
        mViewList.add(new NeedToDo2Fragment());
        mViewList.add(new Remind2Fragment());
        EventActivity.MyPagerAdapter mAdapter = new EventActivity.MyPagerAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
        mViewPager.setCurrentItem(getIntent().getIntExtra("index",0));
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
