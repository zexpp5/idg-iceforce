package newProject.company.resumption;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.injoy.idg.R;

import newProject.company.vacation.VacationDetailFragment;
import yunjing.utils.FragmentCallBackInterface;

public class ReLeaveActivity extends AppCompatActivity implements FragmentCallBackInterface{
    private FragmentManager mFragmentManager;
    private Fragment mSelectFragment;
    private int eid=0;
    private double mLeaveDay;
    private int mLeaveType;
    private String mLeaveString;
    private int mLeaveId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_detail);
        Bundle getBundle = getIntent().getExtras();
        if (getBundle!=null) {
            eid= getBundle.getInt("EID", 0);
            mLeaveDay=getBundle.getDouble("DAY");
            mLeaveString=getBundle.getString("TYPES");
            mLeaveType=getBundle.getInt("TYPE");
            mLeaveId=getBundle.getInt("EID");
        }
        Bundle bundle = new Bundle();
        bundle.putInt("EID", eid);
        bundle.putDouble("DAY",mLeaveDay);
        bundle.putString("TYPES",mLeaveString);
        bundle.putInt("TYPE",mLeaveType);
        bundle.putInt("EID",mLeaveId);
        ReLeaveFragment fragment=new ReLeaveFragment();
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
    public void setSelectedFragment(Fragment fragment) {
        mSelectFragment = fragment;
    }

    @Override
    public void refreshList() {

    }

    @Override
    public void callBackObject(Object object) {

    }


    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }
}
