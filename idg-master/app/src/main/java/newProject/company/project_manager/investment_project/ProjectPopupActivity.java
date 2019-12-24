package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;
import com.utils.SPUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zsz on 2019/6/27.
 */

public class ProjectPopupActivity extends BaseActivity {
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.ll_potential)
    LinearLayout llPotential;
    @Bind(R.id.ll_follow)
    LinearLayout llFollow;
    @Bind(R.id.iv_icon_1)
    ImageView ivIcon1;
    @Bind(R.id.tv_text_1)
    TextView tvText1;
    @Bind(R.id.iv_icon_2)
    ImageView ivIcon2;
    @Bind(R.id.tv_text_2)
    TextView tvText2;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_project_popup;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        llPotential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectPopupActivity.this, PotentialProjectsAddActivity.class);
                startActivity(intent);
                finish();
            }
        });

        llFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SPUtils.get(ProjectPopupActivity.this, SPUtils.DEPT_ID, "").toString().equals("1") || SPUtils.get(ProjectPopupActivity.this, SPUtils.DEPT_ID, "").toString().equals("30")) {
                    Intent intent = new Intent(ProjectPopupActivity.this, PotentialProjectsAddForVCActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(ProjectPopupActivity.this, TrackProgressAddActivity.class);
                    intent.putExtra("flag", "ADD");
                    startActivity(intent);
                }
                finish();
            }
        });


        //判断是否VC组
        if (SPUtils.get(this, SPUtils.DEPT_ID, "").toString().equals("1") || SPUtils.get(this, SPUtils.DEPT_ID, "").toString().equals("30")) {
            /*Intent intent = new Intent(ProjectPopupActivity.this, PotentialProjectsAddActivity.class);
            startActivity(intent);*/
            ivIcon2.setBackgroundResource(R.mipmap.icon_popup_project_for_vc);
            tvText2.setText("合伙人见过的项目");

        } else if (SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("207") || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("10") || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("12")) {
            llFollow.setVisibility(View.INVISIBLE);
        }

    }

}
