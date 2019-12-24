package newProject.company.investment;

import android.os.Bundle;
import android.widget.RelativeLayout;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;
import com.ui.UpdatePasswordActivity;
import com.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;


/**
 * Created by selson on 2019/9/20.
 * 设置-密码
 */

public class ActivitySetting extends BaseActivity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.layPsw)
    RelativeLayout layPsw;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_setting;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        titleBar.setMidText("设 置");
    }

    @OnClick(R.id.layPsw)
    public void onViewClicked()
    {
        String userAccount = DisplayUtil.getUserInfo(ActivitySetting.this, 11);
        Bundle bundle = new Bundle();
        bundle.putString("phone", userAccount);
        bundle.putBoolean("isForget", false);
        openActivity(UpdatePasswordActivity.class, bundle);
    }
}
