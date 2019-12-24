package newProject.company.invested_project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.cxgz.activity.cxim.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selson on 2019/4/26.
 */
public class BasePagerAdapter extends FragmentPagerAdapter
{
    private List<Fragment> mViewList = new ArrayList<>();
    private String[] mTitle;
    private Fragment currentFragment;

    public BasePagerAdapter(FragmentManager fm, List<Fragment> fragmentList, String[] title)
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

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object)
    {
        currentFragment = (Fragment) object;
        super.setPrimaryItem(container, position, object);
    }

    public Fragment getCurrentFg()
    {
        return currentFragment;
    }
}
