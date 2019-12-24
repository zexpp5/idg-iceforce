package newProject.company.investment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.cxgz.activity.cxim.base.BaseFragmentActivity;
import com.cxgz.activity.superqq.fragment.SDPersonalAlterFragment;
import com.injoy.idg.R;

import newProject.company.announce.CompanyAnnounceFragment;
import newProject.company.collect.allfragment.TicketFragment;
import newProject.company.vacation.VacationFragment;
import yunjing.utils.FragmentCallBackInterface;

/**
 * Created by zsz on 2019/9/4.
 */

public class FragmentActivity extends BaseFragmentActivity implements FragmentCallBackInterface
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Fragment fragment;
        if (getIntent().getStringExtra("flag").equals("personal")) {
            fragment = new SDPersonalAlterFragment();
        }else if (getIntent().getStringExtra("flag").equals("ticket")){
            fragment = new TicketFragment();
        }else if (getIntent().getStringExtra("flag").equals("GG")){
            fragment = new CompanyAnnounceFragment();
        }else {
            fragment = new SDPersonalAlterFragment();
        }

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
