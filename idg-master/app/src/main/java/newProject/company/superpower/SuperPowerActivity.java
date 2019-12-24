package newProject.company.superpower;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.injoy.idg.R;

/**
 * Created by tujingwu on 2017/10/27.
 */

public class SuperPowerActivity extends AppCompatActivity {
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_power_layout);

        replaceFragment(new SuperPowerFragment());
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

}
