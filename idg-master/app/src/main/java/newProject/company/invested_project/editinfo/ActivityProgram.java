package newProject.company.invested_project.editinfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.slidedatetimepicker.SlideDateTimeListener;
import com.utils.slidedatetimepicker.SlideDateTimePicker;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.PostProgramBean;
import newProject.utils.HttpHelperUtils;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.utils.DisplayUtil;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFragmentProject;

/**
 * Created by selson on 2019/5/7.
 * 新增投资方案
 */

public class ActivityProgram extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.edt_program_time)
    FontTextView edtProgramTime;
    @Bind(R.id.edt_program_content)
    FontEditext edtProgramContent;
    @Bind(R.id.edt_program_menony)
    FontEditext edtProgramMenony;
    @Bind(R.id.edt_program_menony_type)
    FontTextView edtProgramMenonyType;
    @Bind(R.id.edt_program_plan_type)
    FontTextView edtProgramPlanType;
    @Bind(R.id.edt_program_pass)
    FontTextView edtProgramPass;
    @Bind(R.id.edt_program_fund)
    FontEditext edtProgramFund;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_save)
    LinearLayout llSave;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private String tmpprojId = "";
    private String tmpUserName = "";
    private String tmpMenoyType = "";
    private String tmpIcApproved = "";
    private String tmpReamrk = "";
    private String tmpPlanType = "";

    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invested_edt_program;
    }

    @Override
    protected void init()
    {
        tmpUserName = loginUserAccount;
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            tmpprojId = bundle.getString("mProjId");
        }

        ButterKnife.bind(this);
        titleBar.setMidText("新增投资方案");
        titleBar.setLeftImageVisible(true);
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
//        getPlanType();
//        getMoneyType();
    }

    private void ShowPlanTypeDialog()
    {
        HttpHelperUtils.getInstance().getType(ActivityProgram.this, true, Constants.TYPE_PROJECT_PLAN, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityProgram.this, false, false, false, "方案类型", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    tmpPlanType = content.getKey();
                                    edtProgramPlanType.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityProgram.this, "请重新获取方案类型");
                }
            }
        });
    }


    private void ShowMoneyTypeDialog()
    {
        HttpHelperUtils.getInstance().getType(ActivityProgram.this, true, Constants.TYPE_PROJECT_MONEY, new HttpHelperUtils
                .InputListener()
        {
            @Override
            public void onData(List<BeanIceProject> icApprovedList)
            {
                if (icApprovedList.size() > 0)
                {
                    BaseDialogUtils.showDialogProject(ActivityProgram.this, false, false, false, "币种", icApprovedList, new
                            DialogFragmentProject.InputListener()
                            {
                                @Override
                                public void onData(BeanIceProject content)
                                {
                                    tmpMenoyType = content.getKey();
                                    edtProgramMenonyType.setText(content.getValue());
                                }
                            });
                } else
                {
                    MyToast.showToast(ActivityProgram.this, "请重新获取货币类型");
                }
            }
        });
    }

    private void setIcApproved()
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "1", "否"));
        beanIceProjectList.add(new BeanIceProject(1, "2", "是"));
        beanIceProjectList.add(new BeanIceProject(2, "3", "待定"));
    }

    private void showIcApproved()
    {
        setIcApproved();
        BaseDialogUtils.showDialogProject(ActivityProgram.this, false, false, false, "是否通过", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        tmpIcApproved = content.getKey();
                        edtProgramPass.setText(content.getValue());
                    }
                });
    }

    private void postProgram()
    {
        // 是否通过IC，否("1"),是("2"),待定("3");
        if (StringUtils.empty(edtProgramTime.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program1));
            return;
        }
        if (StringUtils.empty(edtProgramContent.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program2));
            return;
        }
        if (StringUtils.empty(edtProgramMenony.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program3));
            return;
        }
        if (StringUtils.empty(edtProgramMenonyType.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program4));
            return;
        }
        if (StringUtils.empty(edtProgramPlanType.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program5));
            return;
        }
        if (StringUtils.empty(edtProgramPass.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program6));
            return;
        }
        if (StringUtils.empty(edtProgramFund.getText().toString().trim()))
        {
            MyToast.showToast(ActivityProgram.this, getResources().getString(R.string.hint_iceforce_program7));
            return;
        }

        PostProgramBean postProgramBean = new PostProgramBean();
        postProgramBean.setProjId(tmpprojId);
        postProgramBean.setPlanDate(edtProgramTime.getText().toString().trim());
        postProgramBean.setPlanDesc(edtProgramContent.getText().toString().trim());
        postProgramBean.setPipelineAmt(edtProgramMenony.getText().toString().trim());
        postProgramBean.setCurrId(tmpMenoyType);
        postProgramBean.setIsIcApproved(tmpIcApproved);
        postProgramBean.setRemarks(tmpReamrk); //备注
        postProgramBean.setPlanType(tmpPlanType);
        postProgramBean.setFund(edtProgramFund.getText().toString().trim());
        postProgramBean.setUsername(tmpUserName);

        ListHttpHelper.postProgram(ActivityProgram.this, SDGson.toJson(postProgramBean), new SDRequestCallBack()
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ActivityProgram.this, "新增失败");
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                MyToast.showToast(ActivityProgram.this, "新增成功");
                setResult(RESULT_OK);
                finish();
            }
        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.edt_program_time, R.id.edt_program_menony_type, R.id.edt_program_plan_type, R.id.edt_program_pass, R.id
            .tv_save})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.edt_program_time:
                new SlideDateTimePicker.Builder(getSupportFragmentManager())
                        .setListener(startTimeListener)
                        .setInitialDate(new Date())
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                        .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                        .build()
                        .show();
                break;
            case R.id.edt_program_menony_type:
                ShowMoneyTypeDialog();
                break;
            case R.id.edt_program_plan_type:
                ShowPlanTypeDialog();
                break;
            case R.id.edt_program_pass:
                showIcApproved();
                break;
            case R.id.tv_save:
                postProgram();
                break;
        }
    }

    private SlideDateTimeListener startTimeListener = new SlideDateTimeListener()
    {
        @Override
        public void onDateTimeSet(Date date)
        {
            edtProgramTime.setText(DisplayUtil.mFormatter.format(date));
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel()
        {

        }
    };
}
