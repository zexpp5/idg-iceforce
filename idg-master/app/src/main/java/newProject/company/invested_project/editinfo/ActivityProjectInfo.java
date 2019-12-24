package newProject.company.invested_project.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2019/5/8.
 * 项目信息-编辑-修改
 */

public class ActivityProjectInfo extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edt_program_fund)
    EditText edtProgramFund;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_save)
    LinearLayout llSave;

    private String mTitle = "";
    private String mProjId = "";
    private String mCode = "";
    private String mContent = "";
    private String mUserName = "";
    private String mMidTitle = "编辑";
    String urlEndString = "currVersion";

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_situation_main;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mTitle = bundle.getString("mTitle");
            mProjId = bundle.getString("mProjId");
            mCode = bundle.getString("mCode");
            mContent = bundle.getString("mContent");
            mMidTitle = bundle.getString("mMidTitle");
        }
        if (mMidTitle.equals("新增"))
        {
            urlEndString = "newVersion";
        }

        tvTitle.setText(mTitle);

        edtProgramFund.setText(mContent);
        mUserName = loginUserAccount;

        titleBar.setMidText(mMidTitle);
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
                setResult(RESULT_OK);
            }
        });
    }

    @OnClick(R.id.tv_save)
    public void onViewClicked()
    {
        postSituation();
    }


    private void postSituation()
    {
        if (StringUtils.empty(mProjId))
        {
            MyToast.showToast(ActivityProjectInfo.this, "项目ID为空");
            return;
        }
        if (StringUtils.empty(mCode))
        {
            MyToast.showToast(ActivityProjectInfo.this, "项目Code为空");
            return;
        }
        mContent = edtProgramFund.getText().toString().trim();
        if (StringUtils.empty(mContent))
        {
            MyToast.showToast(ActivityProjectInfo.this, "请填写内容");
            return;
        }
        if (StringUtils.empty(mUserName))
        {
            MyToast.showToast(ActivityProjectInfo.this, "用户ID为空");
            return;
        }
        ListHttpHelper.postSituation(ActivityProjectInfo.this, urlEndString, mProjId, mCode, mContent, mUserName, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ActivityProjectInfo.this, "保存失败");
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        MyToast.showToast(ActivityProjectInfo.this, "保存成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });

    }
}

