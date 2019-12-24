package newProject.company.hr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cxgz.activity.cxim.base.BaseFragmentActivity;
import com.injoy.idg.R;

import newProject.company.vacation.VacationFragment;
import yunjing.utils.FragmentCallBackInterface;

/**
 * Created by selson on 2018/6/13.
 */
public class LeaveApplyActivity extends BaseFragmentActivity implements FragmentCallBackInterface
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        VacationFragment fragment = new VacationFragment();
        replaceFragment(fragment);
    }


    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_leave_apply_main;
    }

    @Override
    public void setSelectedFragment(Fragment fragment)
    {

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
