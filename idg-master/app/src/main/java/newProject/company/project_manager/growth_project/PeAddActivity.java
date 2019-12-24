package newProject.company.project_manager.growth_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.chaoxiang.base.utils.ButtonUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.PickerDialog;
import com.chaoxiang.base.utils.StringUtils;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import newProject.api.ListHttpHelper;
import newProject.company.project_manager.growth_project.bean.AddPeBean;
import newProject.company.project_manager.growth_project.bean.InduListBean;
import newProject.company.project_manager.growth_project.bean.PeDetailBean;
import tablayout.view.textview.FontEditext;
import tablayout.view.textview.FontTextView;
import yunjing.bean.SelectItemBean;
import yunjing.utils.CommonDialog;
import yunjing.utils.DisplayUtil;
import yunjing.utils.PopupDialog;
import yunjing.view.CustomNavigatorBar;

public class PeAddActivity extends Activity
{

    @Bind(R.id.title_bar)
    CustomNavigatorBar mTitleBar;
    @Bind(R.id.project_name)
    FontEditext projectName;
    @Bind(R.id.invest_present)
    FontEditext investPresent;
    @Bind(R.id.invest_invite)
    FontTextView investInvite;
    @Bind(R.id.invest_trade)
    FontTextView investTrade;
    @Bind(R.id.invest_gov)
    FontEditext investGov;
    @Bind(R.id.invest_city)
    FontEditext investCity;
    @Bind(R.id.invest_state)
    FontTextView investState;
    @Bind(R.id.connect_time)
    FontTextView connectTime;
    @Bind(R.id.response_man)
    FontEditext responseMan;
    @Bind(R.id.group_situation)
    FontTextView groupSituation;
    @Bind(R.id.trade_introduce_text)
    FontEditext tradeIntroduceText;
    @Bind(R.id.follow_layout)
    LinearLayout followLayout;
    @Bind(R.id.response_layout)
    LinearLayout responseLayout;
    @Bind(R.id.response_view)
    View responseView;
    @Bind(R.id.btn_emphases_project)
    FontTextView btn_emphases_project;

    private List<SelectItemBean> mInvestList = new ArrayList<>();
    private String[] mInvest = {"未约见", "已约见"};
    private SelectItemBean mInvestBean;
    private String invContactStatus = "unDate";

    private List<SelectItemBean> mCapitalList = new ArrayList<>();
    private String[] mCapital = {"未投资", "已投资"};
    private SelectItemBean mCapitalBean;
    private String invStatus = "";

    private List<SelectItemBean> statusList = new ArrayList<>();
    private String[] mStatus = {"继续跟进", "放弃", "观望"};
    private SelectItemBean statusBean;
    private String status = "";


    private String invDate;
    private List<SelectItemBean> mInduList = new ArrayList<>();
    private SelectItemBean mInduBean;
    private String comIndus;
    private String projName;
    private int mEid;
    private String mTitle;
    private String mProjectId;

    private String[] mImportantString = {"是", "是(新建)", "否"};
    private int importantInt = 3;
    private SelectItemBean mImportantBean;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pe_add);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            mTitle = bundle.getString("TITLE");
            mEid = bundle.getInt("EID");
        }
        initViews();
        toStatusDatas();
    }

    private void initViews()
    {
        mTitleBar.setLeftImageVisible(true);
        mTitleBar.setRightTextVisible(true);
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
        mTitleBar.setRightTextOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commitInfo();
            }
        });
        investInvite.setOnClickListener(mOnClicklistner);
        investTrade.setOnClickListener(mOnClicklistner);
        groupSituation.setOnClickListener(mOnClicklistner);
        btn_emphases_project.setOnClickListener(mOnClicklistner);
        connectTime.setOnClickListener(mOnClicklistner);
        investState.setOnClickListener(mOnClicklistner);
        DisplayUtil.editTextable(responseMan, false);
        if (mEid != 0 && mTitle != null)
        {
            mTitleBar.setMidText(mTitle);
            followLayout.setVisibility(View.VISIBLE);
            responseLayout.setVisibility(View.VISIBLE);
            responseView.setVisibility(View.VISIBLE);
            getPageInfo();
        } else
        {
            responseView.setVisibility(View.GONE);
            mTitleBar.setMidText("新建项目");
        }
    }

    private void getPageInfo()
    {

        ListHttpHelper.getPEDetail(PeAddActivity.this, mEid + "", new SDRequestCallBack(PeDetailBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                MyToast.showToast(PeAddActivity.this, msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                PeDetailBean bean = (PeDetailBean) responseInfo.getResult();
                if (bean != null && bean.getData() != null)
                {
                    setInfo(bean.getData());
                }
            }
        });
    }

    private void setInfo(PeDetailBean.DataBean data)
    {
        mProjectId = data.getProjId() + "";
        if (data.getProjName() != null)
        {
            projectName.setText(data.getProjName());
        }
        if (data.getInvRound() != null)
        {
            investPresent.setText(data.getInvRound());
        }
        if (data.getInvContactStatus() != null)
        {
            invContactStatus = data.getInvContactStatus();
            if (data.getInvContactStatus().equals("unDate"))
            {
                investInvite.setText(mInvest[0]);
            } else
            {
                investInvite.setText(mInvest[1]);
            }
            /*if (mInvestBean==null){
                if (!TextUtils.isEmpty(data.getInvContactStatus())) {
                    mInvestBean = new SelectItemBean(mClasss[data.getFileCat()], data.getFileCat(), data.getFileCat()+"");
                    mInvestBean.setSelectIndex(data.getFileCat());
                }
            }*/

        }
        if (data.getComIndus() != null)
        {
            investTrade.setText(data.getComIndus());
            comIndus = data.getComIndus();
        }
        if (data.getInvGroup() != null)
        {
            investGov.setText(data.getInvGroup());
        }
        if (data.getInvFlowUp() != null)
        {
            String status;
            switch (data.getInvFlowUp())
            {
                case "flowUp":
                    status = "继续跟进";
                    break;
                case "abandon":
                    status = "放弃";
                    break;
                case "WS":
                    status = "观望";
                    break;
                default:
                    status = "";
                    break;
            }
            investState.setText(status);
        }
        if (data.getRegion() != null)
        {
            investCity.setText(data.getRegion());
        }

        if (data.getInvDate() != null)
        {
            connectTime.setText(DisplayUtil.timesTwo(data.getInvDate()));
        }

        if (data.getInvStatus() != null)
        {
            invStatus = data.getInvStatus();
            if (data.getInvStatus().equals("unInv"))
            {
                groupSituation.setText(mCapital[0]);
            } else
            {
                groupSituation.setText(mCapital[1]);
            }
        }

        if (StringUtils.notEmpty(data.getImportantStatus()))
        {
            importantInt = data.getImportantStatus();
            if (data.getImportantStatus() == 1)
            {
                btn_emphases_project.setText(mImportantString[0]);
            } else if (data.getImportantStatus() == 2)
            {
                btn_emphases_project.setText(mImportantString[1]);
            } else if (data.getImportantStatus() == 3)
            {
                btn_emphases_project.setText(mImportantString[2]);
            }
        }


        if (data.getUserName() != null)
        {
            responseMan.setText(data.getUserName());
        }

        if (data.getBizDesc() != null)
        {
            tradeIntroduceText.setText(data.getBizDesc());
        }
    }

    public View.OnClickListener mOnClicklistner = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (v == investInvite)
            {
                showLogisticsClass();
            } else if (v == groupSituation)
            {
                showCapital();
            } else if (v == btn_emphases_project)
            {
                showImportant();
            } else if (v == investTrade)
            {
                boolean fastDoubleClick = ButtonUtils.isFastDoubleClick(R.id.invest_trade);
                if (!fastDoubleClick)
                {
                    investTrade.setEnabled(false);
                    getTrade();

                }
            } else if (v == connectTime)
            {
               /* final Calendar calendar = Calendar.getInstance();
                new YearPickerDialog(PeAddActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        invDate = DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd");
                        connectTime.setText(invDate);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();*/
                final Calendar calendar = Calendar.getInstance();
                new PickerDialog(PeAddActivity.this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        calendar.set(year, monthOfYear, dayOfMonth);
                        invDate = DateUtils.clanderTodatetime(calendar, "yyyy-MM-dd");
                        connectTime.setText(invDate);

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE)).show();
            } else if (v == investState)
            {

                //跟进状态
                PopupDialog.showmLogisticDialogUtil(mCapitalBean, PeAddActivity.this, "跟进状态", statusList, "0", new CommonDialog
                        .CustomerTypeInterface()
                {
                    @Override
                    public void menuItemClick(SelectItemBean bean, int index)
                    {
                        statusBean = bean;
                        statusBean.setSelectIndex(index);
                        investState.setText(bean.getSelectString());
                        status = bean.getId();
                    }
                });
            }
        }
    };

    private void commitInfo()
    {
        projName = projectName.getText().toString();
        String present = investPresent.getText().toString();

        String invGroup = investGov.getText().toString();
        final String region = investCity.getText().toString();
        //String invFlowUp=investState.getText().toString();


        for (int i = 0; i < statusList.size(); i++)
        {
            if (statusList.get(i).getSelectString().equals(investState.getText().toString()))
            {
                status = statusList.get(i).getId();
            }
        }

        invDate = connectTime.getText().toString();
        //String userName = responseMan.getText().toString();
        //String invStatus=groupSituation.getText().toString();
        String bizDesc = tradeIntroduceText.getText().toString();

        if (TextUtils.isEmpty(projName))
        {
            SDToast.showShort("请输入项目名称");
            return;
        }/* else if (TextUtils.isEmpty(comIndus)) {
            SDToast.showShort("请选择行业");
            return;
        }*/
        commit(present, invGroup, region, bizDesc);

    }

    public void commit(String present, String invGroup, String region, String bizDesc)
    {
        ListHttpHelper.commitPe(projName, present, invContactStatus, comIndus, invGroup, region,
                status, invDate, "", invStatus, bizDesc, mEid != 0 ? mEid + "" : "", this, importantInt, new SDRequestCallBack
                        (AddPeBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {

                        AddPeBean bean = (AddPeBean) responseInfo.getResult();
                        if (bean.getData() != 0)
                        {
                            if (mEid != 0)
                            {
                                setResult(RESULT_OK);
                            } else
                            {
                                Bundle bundle = new Bundle();
                                bundle.putString("TITLE", projName);
                                bundle.putInt("EID", bean.getData());
                                Intent intent = new Intent(PeAddActivity.this, PEDetailActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                            finish();
                        }

                    }
                });
    }


    @Override
    protected void onDestroy()
    {
        ButterKnife.unbind(this);
        super.onDestroy();
    }

    private void showLogisticsClass()
    {
        toClassDatas();
        PopupDialog.showmLogisticDialogUtil(mInvestBean, this, "约见状态", mInvestList, "0", new CommonDialog.CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mInvestBean = bean;
                mInvestBean.setSelectIndex(index);
                investInvite.setText(bean.getSelectString());
                invContactStatus = bean.getId();
            }
        });
    }

    //文档的类别
    private void toClassDatas()
    {
        mInvestList.clear();
        mInvestList.add(new SelectItemBean(mInvest[0], 0, "unDate"));
        mInvestList.add(new SelectItemBean(mInvest[1], 1, "date"));
    }


    private void showCapital()
    {
        toCapitalDatas();
        PopupDialog.showmLogisticDialogUtil(mCapitalBean, this, "投资状态", mCapitalList, "0", new CommonDialog
                .CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mCapitalBean = bean;
                mCapitalBean.setSelectIndex(index);
                groupSituation.setText(bean.getSelectString());
                invStatus = bean.getId();
            }
        });
    }

    private void showImportant()
    {
        toImportantDatas();
        PopupDialog.showmLogisticDialogUtil(mImportantBean, this, "是否重点项目", mCapitalList, "0", new CommonDialog
                .CustomerTypeInterface()
        {
            @Override
            public void menuItemClick(SelectItemBean bean, int index)
            {
                mImportantBean = bean;
                mImportantBean.setSelectIndex(index);
                btn_emphases_project.setText(mImportantString[index]);
                importantInt = Integer.parseInt(bean.getId());
            }
        });
    }

    /**
     * 跟进情况
     */
    private void toStatusDatas()
    {
        statusList.clear();
        statusList.add(new SelectItemBean(mStatus[0], 0, "flowUp"));
        statusList.add(new SelectItemBean(mStatus[1], 1, "abandon"));
        statusList.add(new SelectItemBean(mStatus[2], 2, "WS"));
    }

    //文档的类别
    private void toCapitalDatas()
    {
        mCapitalList.clear();
        mCapitalList.add(new SelectItemBean(mCapital[0], 0, "unInv"));
        mCapitalList.add(new SelectItemBean(mCapital[1], 1, "inv"));
    }

    //文档的类别
    private void toImportantDatas()
    {
        mCapitalList.clear();
        mCapitalList.add(new SelectItemBean(mImportantString[0], 0, "1"));
        mCapitalList.add(new SelectItemBean(mImportantString[1], 1, "2"));
        mCapitalList.add(new SelectItemBean(mImportantString[2], 2, "3"));
    }

    /**
     * hangye
     */
    private void getTrade()
    {

        ListHttpHelper.getInduList(PeAddActivity.this, new SDRequestCallBack(InduListBean.class)
        {
            @Override
            public void onRequestFailure(HttpException error, String msg)
            {
                SDToast.showShort(msg);
                investTrade.setEnabled(true);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo)
            {
                investTrade.setEnabled(true);
                InduListBean bean = (InduListBean) responseInfo.result;
                List<String> datas = bean.getData();
                mInduList.clear();
                for (int i = 0; i < datas.size(); i++)
                {
                    if (datas.get(i) != null)
                    {
                        mInduList.add(new SelectItemBean(datas.get(i), i, datas.get(i)));
                    }
                }

                PopupDialog.showmMonthDialogUtil(mInduBean, PeAddActivity.this, "行  业", mInduList, "0", new CommonDialog
                        .CustomerTypeInterface()
                {
                    @Override
                    public void menuItemClick(SelectItemBean bean, int index)
                    {
                        mInduBean = bean;
                        mInduBean.setSelectIndex(index);
                        investTrade.setText(bean.getSelectString());
                        comIndus = bean.getId();
                    }
                });

            }
        });

    }


}
