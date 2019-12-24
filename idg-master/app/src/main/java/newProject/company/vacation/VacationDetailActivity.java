package newProject.company.vacation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.injoy.idg.R;

import yunjing.utils.FragmentCallBackInterface;

/**
 * 请假申请-有销假按钮的
 */
public class VacationDetailActivity extends AppCompatActivity implements FragmentCallBackInterface
{
    private FragmentManager mFragmentManager;
    private Fragment mSelectFragment;
    private int eid = 0;
    private int mSign = 1;
    private boolean isShowBtn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);
        Bundle getBundle = getIntent().getExtras();
        if (getBundle != null)
        {
            eid = getBundle.getInt("EID", 0);
            mSign = getBundle.getInt("SIGN", 1);
            isShowBtn = getBundle.getBoolean("isShowBtn", true);
        }
        Bundle bundle = new Bundle();
        bundle.putInt("EID", eid);
        bundle.putInt("SIGN", mSign);
        bundle.putBoolean("isShowBtn", isShowBtn);
        VacationDetailFragment fragment = new VacationDetailFragment();
        fragment.setArguments(bundle);
        replaceFragment(fragment);
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
        ft.replace(R.id.apply_fragment_container, fragment);
        ft.commit();
    }

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


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
