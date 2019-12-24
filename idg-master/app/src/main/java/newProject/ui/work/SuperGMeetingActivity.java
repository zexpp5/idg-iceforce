package newProject.ui.work;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.injoy.idg.R;

import newProject.company.Schedule.ScheDuleMeetFragment;
import newProject.ui.work.detail.ScheDuleMeetDetailFragment;
import yunjing.utils.FragmentCallBackInterface;

/**
 * 会议
 */
public class SuperGMeetingActivity extends AppCompatActivity implements FragmentCallBackInterface
{
    private FragmentManager mFragmentManager;
    private long mEid = 0;
    private boolean mIsAdd = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_power_layout);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
        {
            mEid = bundle.getLong("EID");
            mIsAdd = bundle.getBoolean("ADD");
        }
        if (mIsAdd)
        {
            ScheDuleMeetFragment scheDuleMeetFragment = new ScheDuleMeetFragment();
            scheDuleMeetFragment.setArguments(bundle);
            replaceFragment(scheDuleMeetFragment);
        }
        else
        {
            ScheDuleMeetDetailFragment scheDuleMeetFragment2 = new ScheDuleMeetDetailFragment();
            scheDuleMeetFragment2.setArguments(bundle);
            replaceFragment(scheDuleMeetFragment2);
        }
    }

    //替换fragment，使用replace节省内存
    public void replaceFragment(Fragment fragment)
    {
        if (fragment == null)
        {
            return;
        }
        if (mFragmentManager == null)
        {
            mFragmentManager = getSupportFragmentManager();
        }
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.replace(R.id.super_fragment_container, fragment);
        ft.commit();
    }

    private Fragment mSelectFragment;

    @Override
    public void setSelectedFragment(Fragment fragment)
    {
        mSelectFragment = fragment;
    }

    @Override
    public void refreshList()
    {

    }

    @Override
    public void callBackObject(Object object)
    {

    }
}
