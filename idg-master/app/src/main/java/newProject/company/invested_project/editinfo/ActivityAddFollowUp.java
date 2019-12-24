package newProject.company.invested_project.editinfo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.base.view.CustomRadioGroup;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.bean.BeanFollowDate;
import newProject.company.invested_project.bean.BeanFollowUp;
import newProject.company.invested_project.bean.BeanIceProject;
import newProject.company.invested_project.bean.BeanPostFollowUp;
import newProject.company.invested_project.bean.BeanSignalFlag;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.utils.BaseDialogUtils;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.DialogFragmentProject;

/**
 * Created by selson on 2019/5/22.
 * 新增投后跟踪和编辑界面
 */
public class ActivityAddFollowUp extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.edt_follow_frequency)
    FontTextView edtFollowFrequency;
    @Bind(R.id.edt_follow_time)
    FontTextView edtFollowTime;
    @Bind(R.id.edt_follow_year)
    FontTextView edtFollowYear;
    @Bind(R.id.ll_follow_up_main)
    LinearLayout llFollowUpMain;
    @Bind(R.id.tv_save)
    TextView tvSave;
    @Bind(R.id.ll_save)
    LinearLayout llSave;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    @Bind(R.id.tv_star1)
    FontTextView tv_star1;
    @Bind(R.id.tv_star2)
    FontTextView tv_star2;
    @Bind(R.id.tv_star3)
    FontTextView tv_star3;

    @Bind(R.id.radio_group)
    CustomRadioGroup radioGroup;

    @Bind(R.id.ll_add_frequency)
    LinearLayout ll_add_frequency;
    @Bind(R.id.rl_add_frequency)
    RelativeLayout rl_add_frequency;

    @Bind(R.id.ll_time)
    LinearLayout llTime;
    @Bind(R.id.rl_time)
    RelativeLayout rlTime;

    @Bind(R.id.ll_year)
    LinearLayout llYear;
    @Bind(R.id.rl_year)
    RelativeLayout rlYear;


    BeanFollowUp.DataBeanX.DataBean tmpDataBean = null;

    String reportFrequency = "";
    String endDateString = "";
    String dateStr = "";
    String timeYearString = "";
    String projId = "";
    String signalFlag = "";
    String username = "";

    String mTitle = "";

    boolean isEdit = false;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_invested_new_follow_up;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            projId = bundle.getString("projId");
            mTitle = bundle.getString("mTitle");
            endDateString = bundle.getString("endDateString", "");
            reportFrequency = bundle.getString("reportFrequency", "");
            signalFlag = bundle.getString("signalFlag");
            timeYearString = bundle.getString("year");
            dateStr = bundle.getString("dateStr");
            tmpDataBean = (BeanFollowUp.DataBeanX.DataBean) bundle.getSerializable("list");
        }

        if (StringUtils.notEmpty(timeYearString))
        {
            edtFollowYear.setText(timeYearString + "年");
        }

        if (StringUtils.notEmpty(dateStr))
        {
            if (reportFrequency.equals("month"))
            {
                edtFollowTime.setText(dateStr + " 月");
            } else
            {
                edtFollowTime.setText(dateStr + "");
            }
        }

        if (StringUtils.notEmpty(reportFrequency))
        {
            if (reportFrequency.equals("month"))
            {
                edtFollowFrequency.setText("月份");
            } else if (reportFrequency.equals("quarter"))
            {
                edtFollowFrequency.setText("季度");
            } else if (reportFrequency.equals("halfYear"))
            {
                edtFollowFrequency.setText("半年度");
            } else if (reportFrequency.equals("year"))
            {
                edtFollowFrequency.setText("年度");
            }
        }

        getAllDeng();

        if (StringUtils.notEmpty(mTitle))
        {
            titleBar.setMidText(mTitle);
        } else
        {
            isEdit = true;
            titleBar.setMidText("编辑");
            edtFollowTime.setEnabled(false);
            edtFollowTime.setText(endDateString);
            edtFollowFrequency.setText(endDateString);
            hideAndShowTime2(false);
            setInfo();
        }

        postFollowByProjIdAndEndDate();

        username = loginUserAccount;
        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
    }

    List<BeanIceProject> beanIceProjectList = new ArrayList<>();

    private void showFrequency1()
    {
        beanIceProjectList.clear();
        beanIceProjectList.add(new BeanIceProject(0, "month", "月份"));
        beanIceProjectList.add(new BeanIceProject(1, "quarter", "季度"));
        beanIceProjectList.add(new BeanIceProject(2, "halfYear", "半年度"));
        beanIceProjectList.add(new BeanIceProject(3, "year", "年度"));
        BaseDialogUtils.showDialogProject(ActivityAddFollowUp.this, false, false, false, "报告频率", beanIceProjectList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        reportFrequency = content.getKey();
                        edtFollowFrequency.setText(content.getValue());
                        if (reportFrequency.equals("year"))
                        {
                            hideAndShowTime(false);
                        } else
                        {
                            hideAndShowTime(true);
                        }
                    }
                });
    }

    private void hideAndShowTime(boolean isShow)
    {
        if (isShow)
        {
            setViewShow(llTime, true);
            setViewShow(rlTime, true);
        } else
        {
            setViewShow(llTime, false);
            setViewShow(rlTime, false);
        }
    }

    private void hideAndShowTime2(boolean isShow)
    {
        if (isShow)
        {
            setViewShow(ll_add_frequency, true);
            setViewShow(rl_add_frequency, true);
            setViewShow(llYear, true);
            setViewShow(rlYear, true);
        } else
        {
            setViewShow(ll_add_frequency, false);
            setViewShow(rl_add_frequency, false);
            setViewShow(llYear, false);
            setViewShow(rlYear, false);
        }
    }


    List<BeanIceProject> beanYearList = new ArrayList<>();

    private void showYear()
    {
        beanYearList.clear();
        int yearNow = DateUtils.getNowDate(Calendar.YEAR);
        for (int i = 0; i < 3; i++)
        {
            beanYearList.add(new BeanIceProject(i, yearNow - i + "", yearNow - i + ""));
        }
        BaseDialogUtils.showDialogProject(ActivityAddFollowUp.this, false, false, false, "报告年度", beanYearList, new
                DialogFragmentProject.InputListener()
                {
                    @Override
                    public void onData(BeanIceProject content)
                    {
                        timeYearString = content.getKey();
                        edtFollowYear.setText(content.getValue() + "年");

                        if (reportFrequency.equals("year"))
                        {
                            postFollowByProjIdAndEndDate();
                        }
                    }
                });
    }

    private void showDate()
    {
        icDateList.clear();

        if (reportFrequency.equals("month"))
        {
            for (int i = 1; i < 13; i++)
            {
                icDateList.add(new BeanIceProject(i, i + "", i + ""));
            }

        } else if (reportFrequency.equals("quarter"))
        {
            for (int i = 1; i < 5; i++)
            {
                icDateList.add(new BeanIceProject(i, "Q" + i, "Q" + i));
            }
        } else if (reportFrequency.equals("halfYear"))
        {
            icDateList.add(new BeanIceProject(0, "上半年", "上半年"));
            icDateList.add(new BeanIceProject(1, "下半年", "下半年"));
        }

        if (icDateList.size() > 0)
        {
            BaseDialogUtils.showDialogProject(ActivityAddFollowUp.this, false, false, false, "报告期", icDateList, new
                    DialogFragmentProject.InputListener()
                    {
                        @Override
                        public void onData(BeanIceProject content)
                        {
                            dateStr = content.getKey();
                            if (reportFrequency.equals("month"))
                            {
                                edtFollowTime.setText(content.getValue() + " 月");
                            } else
                            {
                                edtFollowTime.setText(content.getValue() + "");
                            }
                            postFollowByProjIdAndEndDate();
                        }
                    });
        } else
        {

        }
    }

    final List<BeanIceProject> icDateList = new ArrayList<>();
    List<BeanFollowUp.DataBeanX.DataBean.ListBean> listBeen = new ArrayList<>();

    //显示已有的数据
    private void postFollowByProjIdAndEndDate()
    {
        ListHttpHelper.postFollowByProjIdAndEndDate(ActivityAddFollowUp.this, projId, reportFrequency, timeYearString, dateStr,
                endDateString, username, new SDRequestCallBack
                        (BeanFollowDate.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {

                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanFollowDate followDateBean = (BeanFollowDate) responseInfo.getResult();
                        listBeen.clear();
                        if (StringUtils.notEmpty(followDateBean) && StringUtils.notEmpty(followDateBean.getData()) &&
                                StringUtils.notEmpty
                                        (followDateBean.getData().getData()) && StringUtils.notEmpty(followDateBean.getData()
                                .getData()
                                .getList
                                        ()) && followDateBean.getData().getData().getList().size() > 0)
                        {
                            listBeen.addAll(followDateBean.getData().getData().getList());
                            llFollowUpMain.removeAllViews();
                            editTextArrayList.clear();
                            for (int i = 0; i < listBeen.size(); i++)
                            {
                                addChildView(listBeen.get(i));
                            }
                            if (isEdit)
                            {
                                edtFollowTime.setText(followDateBean.getData().getData().getReportDate());
                                tv_star3.setVisibility(View.INVISIBLE);
                            }
                            if (isMarg)
                            {
                                tv_star1.setVisibility(View.INVISIBLE);
                                tv_star2.setVisibility(View.INVISIBLE);
                                tv_star3.setVisibility(View.INVISIBLE);
                            }

                        } else
                        {
                            llFollowUpMain.removeAllViews();
                        }

                        if (StringUtils.notEmpty(followDateBean) && StringUtils.notEmpty(followDateBean.getData()) && StringUtils
                                .notEmpty(followDateBean.getData().getData()))
                        {
                            if (StringUtils.notEmpty(followDateBean.getData().getData().getSignalFlag()))
                            {
                                signalFlag = followDateBean.getData().getData().getSignalFlag();
                                setSignalFlag(followDateBean.getData().getData().getSignalFlag());
                            } else
                            {
                                setSignalFlag(signalFlag);
                            }

                            if (StringUtils.notEmpty(followDateBean.getData().getData().getReportFrequency()))
                            {
                                reportFrequency = followDateBean.getData().getData().getReportFrequency();
                            }

                            if (StringUtils.notEmpty(followDateBean.getData().getData().getReportDate()))
                            {
                                endDateString = followDateBean.getData().getData().getReportDate();
                            }

                            if (isEdit)
                                if (StringUtils.notEmpty(followDateBean.getData().getData().getReportDateStr()))
                                {
                                    edtFollowTime.setText(followDateBean.getData().getData().getReportDateStr());
                                }
                        }
                    }
                });
    }

    private void setInfo()
    {
        if (StringUtils.notEmpty(tmpDataBean) && tmpDataBean.getList() != null && tmpDataBean.getList().size() > 0)
        {
            listBeen.clear();
            listBeen.addAll(tmpDataBean.getList());
        }
        if (listBeen.size() > 0)
            for (int i = 0; i < listBeen.size(); i++)
            {
                addChildView(listBeen.get(i));
            }
        setSignalFlag(signalFlag);
    }

    List<BeanPostFollowUp> beanPostFollowUpList = new ArrayList<>();
    List<FontEditext> editTextArrayList = new ArrayList<>();

    private void postSave()
    {
        if (listBeen.size() > 0)
        {
            for (int i = 0; i < listBeen.size(); i++)
            {
                BeanPostFollowUp beanPostFollowUp = new BeanPostFollowUp();
                beanPostFollowUp.setIndexId(listBeen.get(i).getIndexId());
                if (StringUtils.notEmpty(editTextArrayList.get(i).getText().toString()))
                {
//                    beanPostFollowUp.setIndexValue(stringToUnicode(editTextArrayList.get(i).getText().toString()));
                    beanPostFollowUp.setIndexValue(editTextArrayList.get(i).getText().toString());
                } else
                {
                    if (StringUtils.notEmpty(listBeen.get(i).getRequiredField()) && listBeen.get(i).getRequiredField()
                            .equalsIgnoreCase("Y"))
                    {
                        MyToast.showToast(ActivityAddFollowUp.this, listBeen.get(i).getIndexName() + "不能为空");
                        return;
                    } else
                    {
                        beanPostFollowUp.setIndexValue("");

                    }
                }
                beanPostFollowUpList.add(beanPostFollowUp);
            }
        }

        if (StringUtils.empty(edtFollowFrequency.getText().toString().trim()) && !isEdit)
        {
            MyToast.showToast(ActivityAddFollowUp.this, getResources().getString(R.string.hint_iceforce_follow_up1));
            return;
        }

        if (StringUtils.empty(timeYearString) && !isEdit)
        {
            MyToast.showToast(ActivityAddFollowUp.this, getResources().getString(R.string.hint_iceforce_follow_up2));
            return;
        }

        if (StringUtils.empty(dateStr) && !isEdit && !reportFrequency.equals("year"))
        {
            MyToast.showToast(ActivityAddFollowUp.this, getResources().getString(R.string.hint_iceforce_follow_up3));
            return;
        }

        ListHttpHelper.postFollowAdd(ActivityAddFollowUp.this, reportFrequency, projId, endDateString, timeYearString, dateStr,
                username, SDGson.toJson
                        (beanPostFollowUpList), signalFlag, new SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(ActivityAddFollowUp.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        MyToast.showToast(ActivityAddFollowUp.this, "新增成功");
                        setResult(RESULT_OK);
                        finish();
                    }
                });
    }

    private boolean isMarg = false;

    private void addChildView(BeanFollowUp.DataBeanX.DataBean.ListBean bean)
    {
        View view2 = (LinearLayout) LayoutInflater.from(ActivityAddFollowUp.this).inflate(R.layout.view_child_follow_up,
                llFollowUpMain,
                false);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view2.setLayoutParams(param);
        FontTextView tv_star = (FontTextView) view2.findViewById(R.id.tv_star);
        FontTextView tv_key = (FontTextView) view2.findViewById(R.id.tv_key);
        FontEditext tv_value = (FontEditext) view2.findViewById(R.id.tv_value);
//        textView1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
//                .LayoutParams.WRAP_CONTENT, 5.0f));
//        textView2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
//                .LayoutParams.WRAP_CONTENT, 2.0f));
        if (StringUtils.notEmpty(bean.getIndexName()))
            tv_key.setText(bean.getIndexName());
        if (StringUtils.notEmpty(bean.getIndexValue()))
            tv_value.setText(bean.getIndexValue());
        if (StringUtils.notEmpty(bean.getRequiredField()) && bean.getRequiredField().equalsIgnoreCase("Y"))
        {
            tv_star.setVisibility(View.VISIBLE);
            isMarg = true;
        } else
        {
            tv_star.setVisibility(View.INVISIBLE);
        }
        editTextArrayList.add(tv_value);

        llFollowUpMain.addView(view2);
    }

    @OnClick({R.id.edt_follow_frequency, R.id.tv_save, R.id.edt_follow_time, R.id.edt_follow_year})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.edt_follow_frequency:
                showFrequency1();
                break;
            case R.id.edt_follow_year:
                showYear();
                break;
            case R.id.edt_follow_time:
                showDate();
                break;
            case R.id.tv_save:
                postSave();
                break;
        }
    }

    private void setSignalFlag(String followDate)
    {
        if (StringUtils.notEmpty(followDate))
        {
            signalFlag = followDate;
            Iterator<BeanSignalFlag.DataBeanX.DataBean> iterator = signalFlagList.iterator();
            while (iterator.hasNext())
            {
                BeanSignalFlag.DataBeanX.DataBean dataBean = iterator.next();
                if (followDate.equals(dataBean.getCodeKey()))
                {
                    RadioButton radbtn = (RadioButton) findViewById(dataBean.getBtnID());
                    radbtn.setChecked(true);
//                radioGroup.check(dataBean.getBtnID());
                }
            }
        }

    }

    private RadioButton addRadioBtn2(String text, int id)
    {
        RadioButton radioButton = (RadioButton) this.getLayoutInflater().inflate(R.layout.radiobtn_main, null);
        radioButton.setText(text);
        radioButton.setId(id);
        return radioButton;
    }

    List<BeanSignalFlag.DataBeanX.DataBean> signalFlagList = new ArrayList<>();

    private void getAllDeng()
    {
        ListHttpHelper.getProjectSignalFlag(this, projId, new SDRequestCallBack(BeanSignalFlag.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(ActivityAddFollowUp.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                BeanSignalFlag beanSignalFlag = (BeanSignalFlag) responseInfo.getResult();
                if (beanSignalFlag != null && beanSignalFlag.getData() != null && beanSignalFlag.getData().getData() != null &&
                        beanSignalFlag.getData().getData().size() > 0)
                {
                    signalFlagList.clear();
                    signalFlagList.addAll(beanSignalFlag.getData().getData());
                    setSignalFlag(signalFlagList);
                } else
                {
                    MyToast.showToast(ActivityAddFollowUp.this, "信号灯信息为空");
                }
            }
        });
    }

    private void setSignalFlag(final List<BeanSignalFlag.DataBeanX.DataBean> signalFlagList)
    {
        if (signalFlagList.size() > 0)
        {
            for (int i = 0; i < signalFlagList.size(); i++)
            {
                radioGroup.addView(addRadioBtn2(signalFlagList.get(i).getCodeNameZhCn(), 10086 + i));
                signalFlagList.get(i).setBtnID(10086 + i);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(RadioGroup group, @IdRes int checkedId)
                {
                    RadioButton radbtn = (RadioButton) findViewById(checkedId);
                    Iterator<BeanSignalFlag.DataBeanX.DataBean> iterator = signalFlagList.iterator();
                    while (iterator.hasNext())
                    {
                        BeanSignalFlag.DataBeanX.DataBean iteratorFileLibBean = iterator.next();
                        if (radbtn.getId() == iteratorFileLibBean.getBtnID())
                            signalFlag = iteratorFileLibBean.getCodeKey();
                    }
                }
            });

            radioGroup.check(10086);

            radioGroup.setHorizontalSpacing(15);
            radioGroup.setVerticalSpacing(10);
            radioGroup.setListener(new CustomRadioGroup.OnclickListener()
            {
                @Override
                public void OnText(String text)
                {
//                    Iterator<BeanSignalFlag.DataBeanX.DataBean> iterator = signalFlagList.iterator();
//                    while (iterator.hasNext())
//                    {
//                        BeanSignalFlag.DataBeanX.DataBean iteratorFileLibBean = iterator.next();
//                        if (text.equals(iteratorFileLibBean.getCodeNameZhCn()))
//                            signalFlag = iteratorFileLibBean.getCodeKey();
//                    }
                }
            });
        }
    }
}
