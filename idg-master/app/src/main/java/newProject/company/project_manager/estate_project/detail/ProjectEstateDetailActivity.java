package newProject.company.project_manager.estate_project.detail;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cxgz.activity.cx.base.BaseActivity;
import com.injoy.idg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.project_manager.investment_project.AnnexListFragment;
import yunjing.view.CustomNavigatorBar;

public class ProjectEstateDetailActivity extends BaseActivity {
    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.vp_view)
    ViewPager mViewPager;
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    private String[] mTitleList = {"基本资料", "详细信息", "会议信息", "附件"};//页卡标题集合
    private List<Fragment> mViewList = new ArrayList<>();//页卡视图集合


    @Override
    protected int getContentLayout() {
        return R.layout.activity_project_estate_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        int projId = getIntent().getIntExtra("projId", 0);
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        //标题
        mNavigatorBar = (CustomNavigatorBar) findViewById(R.id.title_bar);
        mNavigatorBar.setLeftImageOnClickListener(onClickListener);
        mNavigatorBar.setLeftTextOnClickListener(onClickListener);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("地产项目");

        mViewList.clear();
        mViewList.add(BaseInformationFragment.newInstance(projId));
        mViewList.add(InformationDetailFragment.newInstance(projId));

        mViewList.add(MeetingMsgFragment.newInstance(projId));
        mViewList.add(AnnexListFragment.newInstance(projId));
        MyPagerAdapter mAdapter = new MyPagerAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(mAdapter);//给ViewPager设置适配器
        mTabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        mTabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
