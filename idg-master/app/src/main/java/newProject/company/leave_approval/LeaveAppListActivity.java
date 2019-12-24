package newProject.company.leave_approval;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import yunjing.view.CustomNavigatorBar;

/**
 * 销假批审。
 */
public class LeaveAppListActivity extends AppCompatActivity {
    private CustomNavigatorBar mTitleBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private String[] mTitleList = {"批审中","同意","驳回"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);

        mTitleBar = (CustomNavigatorBar)findViewById(R.id.title_bar);
        mTitleBar.setMidText("销假批审");
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.getLeftImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        //添加页卡视图
        mViewList.clear();
        mViewList.add(new LeaveAppOneFragment());
        mViewList.add(new LeaveAppTwoFragment());
        mViewList.add(new LeaveAppThreeFragment());

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);//设置tab模式，当前为系统默认模式
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[0]));//添加tab选项卡
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[1]));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[2]));

        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(),mViewList,mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
    }

    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mViewList=new ArrayList<>();
        private String [] mTitle;

        public MyPagerAdapter(FragmentManager fm,List<Fragment> fragmentList,String [] title) {
            super(fm);
            this.mViewList=fragmentList;
            this.mTitle=title;
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
