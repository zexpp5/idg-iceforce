package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;

public class ProjectListActivity extends AppCompatActivity
{
    private CustomNavigatorBar mTitleBar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int mEid;
    private String mTitle = "";

    private String[] mTitleList = {"基本资料", "项目情况", "投资方案", "基金投资", "会议信息", "附件"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list_layout);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mEid = bundle.getInt("EID", 0);
            mTitle = bundle.getString("TITLE");
        }

        mTitleBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mTitleBar.setMidText(mTitle);
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);

        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setRightSecondImageOnClickListener(mOnClickListener);
        mTitleBar.setLeftImageOnClickListener(mOnClickListener);
        mViewPager = (ViewPager) findViewById(R.id.vp_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        //添加页卡视图
        mViewList.clear();
        mViewList.add(OneFragment.newInstance(mEid));
        mViewList.add(InvestmentProjectFourFragment.newInstance(mEid));
        //  mViewList.add(new InvestmentProjectThreeFragment());
        mViewList.add(TwoFragment.newInstance(mEid));
        mViewList.add(InvestmentProjectThreeFragment.newInstance(mEid));
        mViewList.add(InvestmentProjectSixFragment.newInstance(mEid, mTitle));
        mViewList.add(AnnexListFragment.newInstance(mEid));

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);//设置tab模式，当前为系统默认模式
        for (int i = 0; i < mTitleList.length; i++)
        {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList[i]));//添加tab选项卡
        }
//        mTabLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                LinearLayout childAt = (LinearLayout) mTabLayout.getChildAt(0);
//                for (int j = 0; j < childAt.getChildCount(); j++) {
//                    childAt.getChildAt(j).setPadding(0, 0, 0, 0);
//                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getChildAt(j)
// .getLayoutParams();
//                    layoutParams.rightMargin = DisplayUtil.dp2px(ProjectListActivity.this, 15);
//                }
//            }
//        },200);

        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == mTitleBar.getLeftImageView())
            {
                finish();
            } else if (v == mTitleBar.getRihtSecondImage())
            {

            }
        }
    };


    //ViewPager适配器
    class MyPagerAdapter extends FragmentPagerAdapter
    {
        private List<Fragment> mViewList = new ArrayList<>();
        private String[] mTitle;

        public MyPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title)
        {
            super(fm);
            this.mViewList = fragmentList;
            this.mTitle = title;
        }

        @Override
        public int getCount()
        {
            return mViewList.size();//页卡数
        }

        @Override
        public Fragment getItem(int position)
        {
            return mViewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            return mTitle[position];//页卡标题
        }

    }

}
