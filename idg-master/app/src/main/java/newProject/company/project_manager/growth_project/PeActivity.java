package newProject.company.project_manager.growth_project;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.injoy.idg.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.company.project_manager.investment_project.ProjectManagerListActivity;
import yunjing.view.CustomNavigatorBar;

public class PeActivity extends Activity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    @Bind(R.id.add_layout)
    RelativeLayout addLayout;
    @Bind(R.id.oppor_layout)
    RelativeLayout opporLayout;
    @Bind(R.id.unoppor_layout)
    RelativeLayout unopporLayout;
    @Bind(R.id.all_layout)
    RelativeLayout allLayout;

    @Bind(R.id.influence_rl)
    RelativeLayout influence_rl;  //影响力

    private String mTitle = "PE潜在项目";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pe);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null)
        {
            mTitle = bundle.getString("TITLE");
        }
        
        mTitleBar.setMidText(mTitle);
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(false);
        mTitleBar.setRightImageVisible(false);
        mTitleBar.setRightSecondImageVisible(false);
        mTitleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        addLayout.setOnClickListener(mOnClickListener);
        opporLayout.setOnClickListener(mOnClickListener);
        unopporLayout.setOnClickListener(mOnClickListener);
        allLayout.setOnClickListener(mOnClickListener);
        influence_rl.setOnClickListener(mOnClickListener);
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == addLayout)
            {
                startActivity(new Intent(PeActivity.this, PeAddActivity.class));
            } else if (v == opporLayout)
            {
                Intent intent = new Intent(PeActivity.this, ProjectManagerListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CHOOSE", 3);
                bundle.putString("TITLE", "已约见项目");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v == unopporLayout)
            {
                Intent intent = new Intent(PeActivity.this, ProjectManagerListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CHOOSE", 4);
                bundle.putString("TITLE", "未约见项目");
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (v == allLayout)
            {
                Intent intent = new Intent(PeActivity.this, ProjectManagerListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CHOOSE", 5);
                bundle.putString("TITLE", "潜在项目(所有)");
                intent.putExtras(bundle);
                startActivity(intent);

            } else if (v == influence_rl)
            {
                Intent intent = new Intent(PeActivity.this, ProjectManagerListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("CHOOSE", 6);
                bundle.putString("TITLE", "最具影响力公司");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onDestroy()
    {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
