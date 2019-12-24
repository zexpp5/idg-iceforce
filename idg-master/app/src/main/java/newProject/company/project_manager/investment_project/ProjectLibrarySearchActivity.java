package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by zsz on 2019/5/23.
 */

public class ProjectLibrarySearchActivity extends BaseActivity {
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.et_keyword)
    EditText etKeyword;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.tv_project)
    TextView tvProject;
    @Bind(R.id.tv_member)
    TextView tvMember;

    int flag = 0;
    boolean isProject = false;
    boolean isMember = false;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_project_library_search;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        mNavigatorBar.setRightTextVisible(false);
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setMidText("项目查询");
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        flag = 1;
        isProject = true;
        tvProject.setBackgroundResource(R.drawable.tv_bg_top_bg_radius);
        tvProject.setTextColor(getResources().getColor(R.color.white));
        isMember = false;
        tvMember.setBackgroundResource(R.drawable.tv_bg_solid_gray_radius);
        tvMember.setTextColor(getResources().getColor(R.color.top_bg));

    }

    @OnClick({R.id.iv_search,R.id.tv_project,R.id.tv_member})
    public void onItemClick(View view){
        switch (view.getId()){
            case R.id.iv_search:
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                if (flag == 1) {
                    bundle.putString("projQueryStr", etKeyword.getText().toString());
                    bundle.putString("teamQueryStr", "");
                    bundle.putString("queryStr", "");
                }else if (flag == 2){
                    bundle.putString("projQueryStr", "");
                    bundle.putString("teamQueryStr", etKeyword.getText().toString());
                    bundle.putString("queryStr", "");
                }else {
                    bundle.putString("projQueryStr", "");
                    bundle.putString("teamQueryStr", "");
                    bundle.putString("queryStr", etKeyword.getText().toString());
                }
                intent.putExtra("bundle",bundle);
                setResult(RESULT_OK, intent);
                finish();

                break;
            case R.id.tv_project:
                if (!isProject){
                    flag = 1;
                    isProject = true;
                    tvProject.setBackgroundResource(R.drawable.tv_bg_top_bg_radius);
                    tvProject.setTextColor(getResources().getColor(R.color.white));
                    isMember = false;
                    tvMember.setBackgroundResource(R.drawable.tv_bg_solid_gray_radius);
                    tvMember.setTextColor(getResources().getColor(R.color.top_bg));
                }else {
                    flag = 0;
                    isProject = false;
                    tvProject.setBackgroundResource(R.drawable.tv_bg_solid_gray_radius);
                    tvProject.setTextColor(getResources().getColor(R.color.top_bg));
                }
                break;
            case R.id.tv_member:
                if (!isMember){
                    flag = 2;
                    isMember = true;
                    tvMember.setBackgroundResource(R.drawable.tv_bg_top_bg_radius);
                    tvMember.setTextColor(getResources().getColor(R.color.white));
                    isProject = false;
                    tvProject.setBackgroundResource(R.drawable.tv_bg_solid_gray_radius);
                    tvProject.setTextColor(getResources().getColor(R.color.top_bg));
                }else {
                    flag = 0;
                    isMember = false;
                    tvMember.setBackgroundResource(R.drawable.tv_bg_solid_gray_radius);
                    tvMember.setTextColor(getResources().getColor(R.color.top_bg));
                }
                break;

        }
    }

}
