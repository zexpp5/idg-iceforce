package newProject.company.project_manager.investment_project;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.ButtonUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.ImageUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.invested_project.InVestedProjectInfoActivity;
import newProject.company.invested_project.bean.BeanDirector;
import newProject.company.invested_project.editinfo.ActivityAddFollowUp;
import newProject.company.invested_project.editinfo.ActivityDirectorInfo;
import newProject.company.project_manager.investment_project.adapter.PIMDReportAdapter;
import newProject.company.project_manager.investment_project.bean.PIMDChartListBean;
import newProject.company.project_manager.investment_project.bean.PIMDFinanceListBean;
import newProject.company.project_manager.investment_project.bean.PIMDInfoBean;
import newProject.company.project_manager.investment_project.bean.PIMDReportListBean;
import newProject.company.project_manager.investment_project.bean.PotentialProjectsDetailBean;
import yunjing.utils.DisplayUtil;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;

import static com.utils.SPUtils.USER_ACCOUNT;
import static newProject.company.invested_project.InVestedProjectInfoActivity.mRequestCode7;
import static yunjing.utils.DividerItemDecoration2.VERTICAL_LIST;

/**
 * Created by zsz on 2019/5/9.
 */

public class PostInvestmentManagementDetailActivity extends BaseActivity
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar mNavigatorBar;
    @Bind(R.id.tv_projTeam)
    TextView tvProjTeam;
    @Bind(R.id.tv_significantDeal)
    TextView tvSignificantDeal;
    @Bind(R.id.tv_projType)
    TextView tvProjType;
    @Bind(R.id.tv_investDate)
    TextView tvInvestDate;
    @Bind(R.id.tv_fundName)
    TextView tvFundName;
    @Bind(R.id.tv_investCondition)
    TextView tvInvestCondition;
    @Bind(R.id.tv_income_flag)
    TextView tvIncomeFlag;
    @Bind(R.id.tv_rate_flag)
    TextView tvRateFlag;
    @Bind(R.id.tv_reportFrequency)
    TextView tvReportFrequency;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_predictIncome)
    TextView tvPredictIncome;
    @Bind(R.id.tv_actualIncome)
    TextView tvActualIncome;
    @Bind(R.id.tv_incomeDiffRatio)
    TextView tvIncomeDiffRatio;
    @Bind(R.id.tv_predictNetProfit)
    TextView tvPredictNetProfit;
    @Bind(R.id.tv_actualNetProfit)
    TextView tvActualNetProfit;
    @Bind(R.id.tv_netProfitDiffRatio)
    TextView tvNetProfitDiffRatio;
    @Bind(R.id.view_light)
    View viewLight;
    @Bind(R.id.tv_report)
    TextView tvReport;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.ll_finance)
    LinearLayout llFinance;
    @Bind(R.id.tv_income_diff)
    TextView tvIncomeDiff;
    @Bind(R.id.view_income_diff)
    View viewIncomeDiff;
    @Bind(R.id.tv_profit_diff)
    TextView tvProfitDiff;
    @Bind(R.id.view_profit_diff)
    View viewProfitDiff;
    @Bind(R.id.chart)
    CombinedChart mCombinedChart;
    @Bind(R.id.iv_edit)
    ImageView ivEdit;
    @Bind(R.id.ll_water)
    LinearLayout ll_water;


    @Bind(R.id.ll_chart)
    LinearLayout llChart;

    int indexFinance = 0;
    List<PIMDFinanceListBean.DataBeanX.DataBean> financeDatas = new ArrayList<>();

    PIMDReportAdapter adapter;
    List<PIMDReportListBean.DataBeanX.DataBean.ListBean> reports = new ArrayList<>();

    float x1 = 0;
    float x2 = 0;
    float y1 = 0;
    float y2 = 0;

    List<PIMDChartListBean.DataBeanX.DataBean> chartLists = new ArrayList<>();
    String[] suppliers;
    int chartFlag;

    @Bind(R.id.tv_director_info)
    TextView tvDirectorInfo;
    @Bind(R.id.img_director_add)
    ImageView imgDirectorAdd;
    @Bind(R.id.recycler_director)
    RecyclerView recyclerDirector;
    @Bind(R.id.ll_more)
    LinearLayout ll_more;

    private String projectId = "";
    private String projectName = "";
    InverstmentAdapter inverstmentAdapter = null;
    List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists = new ArrayList<>();
    private List<BeanDirector.DataBeanX.DataBean> directorList = new ArrayList<>();


    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_post_investment_managemnet_detail;
    }

    @Override
    protected void init()
    {
        ButterKnife.bind(this);

        projectId = getIntent().getStringExtra("projId");
        projectName = getIntent().getStringExtra("projName");
        mNavigatorBar.setRightTextVisible(true);
        mNavigatorBar.setRightText("项目详情");
        mNavigatorBar.setRightTextOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(PostInvestmentManagementDetailActivity.this, InvestedProjectsDetailActivity.class);
                intent.putExtra("projId", projectId);
                intent.putExtra("projName", projectName);
                startActivity(intent);
            }
        });
        mNavigatorBar.setRightImageVisible(false);
        mNavigatorBar.setRightSecondImageVisible(false);
        mNavigatorBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
        mNavigatorBar.setMidText(projectName);

        if (SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("207") || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("10")
                || SPUtils.get(this, SPUtils.ROLE_FLAG, "0").equals("12"))
        {
            ivEdit.setVisibility(View.GONE);
        } else
        {
            ivEdit.setVisibility(View.VISIBLE);
        }

        llFinance.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent)
            {
                switch (motionEvent.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        x1 = motionEvent.getX();
                        y1 = motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        x2 = motionEvent.getX();
                        y2 = motionEvent.getY();
                        if (x1 - x2 > 10)
                        {
                            if (indexFinance < financeDatas.size() - 1)
                            {
                                indexFinance++;
                                setFinanceDatas();
                            }

                        } else if (x2 - x1 > 10)
                        {
                            if (indexFinance > 0)
                            {
                                indexFinance--;
                                setFinanceDatas();
                            }
                        }
                        break;
                }
                return true;
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PIMDReportAdapter(reports);
        recyclerView.setAdapter(adapter);


        getInfoData();
        getFinanceData();

        //判断是否VC组ID，是就隐藏
        if (SPUtils.get(this, SPUtils.DEPT_ID, "").toString().equals("1"))
        {
            llChart.setVisibility(View.GONE);
        } else
        {
            llChart.setVisibility(View.VISIBLE);
            getChartData();
        }

        getListData();

        initAdapter();
        postDirectorList();

        setPicView(ll_water);
    }

    public void getInfoData()
    {
        String username = DisplayUtil.getUserInfo(this, 11);
        ListHttpHelper.getPostInvestmentManagementDetailInfoData(this, projectId, username, new
                SDRequestCallBack(PIMDInfoBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PIMDInfoBean bean = (PIMDInfoBean) responseInfo.getResult();
                        if (bean.getData().getData() != null)
                        {
                            tvProjTeam.setText(bean.getData().getData().getProjTeam());
                            tvSignificantDeal.setText(bean.getData().getData().getSignificantDealStr());
                            tvProjType.setText(bean.getData().getData().getProjType());
                            tvInvestDate.setText(bean.getData().getData().getInvestDate());
                            tvFundName.setText(bean.getData().getData().getFundName());
                            tvInvestCondition.setText(bean.getData().getData().getInvestCondition().replace("\\n", "\n"));
                        }
                    }
                });
    }

    public void getFinanceData()
    {
        ListHttpHelper.getPostInvestmentManagementDetailFinanceData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),
                projectId, new SDRequestCallBack(PIMDFinanceListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PIMDFinanceListBean bean = (PIMDFinanceListBean) responseInfo.getResult();
                        if (bean.getData().getData() != null && bean.getData().getData().size() > 0)
                        {
                            financeDatas.clear();
                            financeDatas.addAll(bean.getData().getData());
                            setFinanceDatas();
                        } else
                        {
                            SDToast.showShort("无财务数据");
                        }
                    }
                });
    }

    public void setFinanceDatas()
    {
        tvReportFrequency.setText(financeDatas.get(indexFinance).getReportFrequency());
        tvEndTime.setText(financeDatas.get(indexFinance).getStartDate() + "~" + financeDatas.get(indexFinance).getEndDate());
        tvPredictIncome.setText(financeDatas.get(indexFinance).getPredictIncome());
        tvActualIncome.setText(financeDatas.get(indexFinance).getActualIncome());
        if (financeDatas.get(indexFinance).getChainGrowthOfActualIncome().contains("-"))
        {
            tvIncomeFlag.setText("\u2193");
            tvIncomeFlag.setTextColor(getResources().getColor(R.color.green));
        } else
        {
            tvIncomeFlag.setText("\u2191");
            tvIncomeFlag.setTextColor(getResources().getColor(R.color.red));
        }
        tvIncomeDiffRatio.setText(financeDatas.get(indexFinance).getIncomeDiffRatio());
        tvPredictNetProfit.setText(financeDatas.get(indexFinance).getPredictNetProfit());
        tvActualNetProfit.setText(financeDatas.get(indexFinance).getActualNetProfit());
        if (financeDatas.get(indexFinance).getChainGrowthOfActualNetProfit().contains("-"))
        {
            tvRateFlag.setText("\u2193");
            tvRateFlag.setTextColor(getResources().getColor(R.color.green));
        } else
        {
            tvRateFlag.setText("\u2191");
            tvRateFlag.setTextColor(getResources().getColor(R.color.red));
        }
        tvNetProfitDiffRatio.setText(financeDatas.get(indexFinance).getNetProfitDiffRatio());
        //信号灯颜色  S 蓝   A绿  B 黄 C 红 X 黑
        if ("S".equals(financeDatas.get(indexFinance).getSignalFlag()))
        {
            viewLight.setBackground(getResources().getDrawable(R.drawable.bg_circle_blue));
        } else if ("A".equals(financeDatas.get(indexFinance).getSignalFlag()))
        {
            viewLight.setBackground(getResources().getDrawable(R.drawable.bg_circle_green));
        } else if ("B".equals(financeDatas.get(indexFinance).getSignalFlag()))
        {
            viewLight.setBackground(getResources().getDrawable(R.drawable.bg_circle_yellow));
        } else if ("C".equals(financeDatas.get(indexFinance).getSignalFlag()))
        {
            viewLight.setBackground(getResources().getDrawable(R.drawable.bg_circle_red));
        } else if ("X".equals(financeDatas.get(indexFinance).getSignalFlag()))
        {
            viewLight.setBackground(getResources().getDrawable(R.drawable.bg_circle_black));
        } else
        {
            viewLight.setBackground(null);
        }
        tvReport.setText("报告期：" + financeDatas.get(indexFinance).getReportFrequency());
        getReportData(financeDatas.get(indexFinance).getProjId(), financeDatas.get(indexFinance).getEndDate(), financeDatas.get
                (indexFinance).getYear(), financeDatas.get(indexFinance).getDateStr(), financeDatas.get(indexFinance)
                .getFrequency());
    }

    public void getReportData(String projId, String endDate, String year, String dateStr, String reportFrequency)
    {

        ListHttpHelper.getPostInvestmentManagementDetailReportData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(),
                reportFrequency, year, dateStr, projId, endDate, new SDRequestCallBack(PIMDReportListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                        setReportData();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PIMDReportListBean bean = (PIMDReportListBean) responseInfo.getResult();
                        reports.clear();
                        reports.addAll(bean.getData().getData().getList());
                        setReportData();
                    }
                });
    }

    private void setReportData()
    {
        adapter.notifyDataSetChanged();
    }

    public void getChartData()
    {
        ListHttpHelper.getPostInvestmentManagementDetailChartData(this, projectId, new
                SDRequestCallBack(PIMDChartListBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showShort(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PIMDChartListBean bean = (PIMDChartListBean) responseInfo.getResult();
                        chartLists.clear();
                        chartLists.addAll(bean.getData().getData());
                        chartFlag = 1;
                        suppliers = new String[chartLists.size()];
                        for (int i = 0; i < chartLists.size(); i++)
                        {
                            suppliers[i] = chartLists.get(i).getReportFrequency();
                        }
                        leftAxis = mCombinedChart.getAxisLeft();
                        rightAxis = mCombinedChart.getAxisRight();
                        xAxis = mCombinedChart.getXAxis();
                        setChartDatas();

                    }
                });
    }

    private void setChartDatas()
    {
        //x轴数据
        List<String> xData = new ArrayList<>();
        for (int i = 0; i < chartLists.size(); i++)
        {
            xData.add(chartLists.get(i).getReportFrequency());
        }
        //y轴数据集合
        List<List<Float>> yBarDatas = new ArrayList<>();
        //2种直方图
        //y轴数
        List<Float> yData = new ArrayList<>();
        List<Float> yData2 = new ArrayList<>();
        for (int j = 0; j < chartLists.size(); j++)
        {
            if (chartFlag == 1)
            {
                yData.add(Float.parseFloat(chartLists.get(j).getPredictIncome()) / 1000000);
                yData2.add(Float.parseFloat(chartLists.get(j).getActualIncome()) / 1000000);
            } else
            {
                yData.add(Float.parseFloat(chartLists.get(j).getPredictNetProfit()) / 1000000);
                yData2.add(Float.parseFloat(chartLists.get(j).getActualNetProfit()) / 1000000);
            }
        }
        yBarDatas.add(yData);
        yBarDatas.add(yData2);
        //y轴数据集合
        List<List<Float>> yLineDatas = new ArrayList<>();
        //1种折线图
        for (int i = 0; i < 1; i++)
        {
            //y轴数
            List<Float> lData = new ArrayList<>();
            for (int j = 0; j < chartLists.size(); j++)
            {
                if (chartFlag == 1)
                {
                    if (chartLists.get(j).getIncomeDiffRatio().equals("-"))
                    {
                        lData.add((0f));
                    } else
                    {
                        lData.add(Float.parseFloat(chartLists.get(j).getIncomeDiffRatio()));
                    }
                } else
                {
                    if (chartLists.get(j).getNetProfitDiffRatio().equals("-"))
                    {
                        lData.add(0f);
                    } else
                    {
                        lData.add(Float.parseFloat(chartLists.get(j).getNetProfitDiffRatio()));
                    }
                }

            }
            yLineDatas.add(lData);
        }
        //名字集合
        List<String> barNames = new ArrayList<>();
        barNames.add("预测收入");
        barNames.add("实际收入");

        //颜色集合
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#788FB5"));
        colors.add(Color.parseColor("#FD8183"));
        //colors.add(Color.YELLOW);
        //colors.add(Color.CYAN);
        //竖状图管理类

        List<String> lineNames = new ArrayList<>();
        lineNames.add("收入差额比例");

        List<Integer> lineColors = new ArrayList<>();
        lineColors.add(Color.parseColor("#85B167"));

        //showCombinedChart(xData, yBarDatas.get(0), yLineDatas.get(0), "直方图", "线性图", colors.get(0), colors.get(1));
        showCombinedChart(xData, yBarDatas, yLineDatas, barNames, lineNames,
                colors, lineColors);
    }

    /**
     * 初始化Chart
     */
    private void initChart()
    {
        //不显示描述内容
        mCombinedChart.getDescription().setEnabled(false);

        mCombinedChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR,
                CombinedChart.DrawOrder.LINE
        });

//        mCombinedChart.setBackgroundColor(Color.WHITE);

        mCombinedChart.setDrawGridBackground(false);
        mCombinedChart.setDrawBarShadow(false);
        mCombinedChart.setHighlightFullBarEnabled(false);
        //显示边界
        mCombinedChart.setDrawBorders(true);
        //图例说明
        Legend legend = mCombinedChart.getLegend();
        legend.setWordWrapEnabled(true);

        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        //Y轴设置
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                return decimalFormat.format(value * 100) + "%";
            }
        });

        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawZeroLine(false);
        leftAxis.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                return value + "百万";
            }
        });

    }

    /**
     * 设置X轴坐标值
     *
     * @param xAxisValues x轴坐标集合
     */
    public void setXAxis(final List<String> xAxisValues)
    {

        //设置X轴在底部
        XAxis xAxis = mCombinedChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);

        xAxis.setLabelCount(xAxisValues.size() - 1, false);
        xAxis.setValueFormatter(new ValueFormatter()
        {
            @Override
            public String getFormattedValue(float value)
            {
                int index = (int) value;
                if (index < 0 || index >= xAxisValues.size())
                {
                    return "";
                } else
                {
                    return xAxisValues.get(index);
                }
            }
        });
        mCombinedChart.invalidate();
    }

    /**
     * 得到折线图(一条)
     *
     * @param lineChartY 折线Y轴值
     * @param lineName   折线图名字
     * @param lineColor  折线颜色
     * @return
     */
    private LineData getLineData(List<Float> lineChartY, String lineName, int lineColor)
    {
        LineData lineData = new LineData();

        ArrayList<Entry> yValue = new ArrayList<>();
        for (int i = 0; i < lineChartY.size(); i++)
        {
            yValue.add(new Entry(i, lineChartY.get(i)));
        }
        LineDataSet dataSet = new LineDataSet(yValue, lineName);

        dataSet.setColor(lineColor);
        dataSet.setCircleColor(lineColor);
        dataSet.setValueTextColor(lineColor);

        dataSet.setCircleRadius(3);
        //显示值
        dataSet.setDrawValues(false);
        dataSet.setValueTextSize(10f);
        dataSet.setMode(LineDataSet.Mode.LINEAR);
        dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        lineData.addDataSet(dataSet);
        return lineData;
    }

    /**
     * 得到折线图(多条)
     *
     * @param lineChartYs 折线Y轴值
     * @param lineNames   折线图名字
     * @param lineColors  折线颜色
     * @return
     */
    private LineData getLineData(List<List<Float>> lineChartYs, List<String> lineNames, List<Integer> lineColors)
    {
        LineData lineData = new LineData();

        for (int i = 0; i < lineChartYs.size(); i++)
        {
            ArrayList<Entry> yValues = new ArrayList<>();
            for (int j = 0; j < lineChartYs.get(i).size(); j++)
            {
                yValues.add(new Entry(j, lineChartYs.get(i).get(j)));
            }
            LineDataSet dataSet = new LineDataSet(yValues, lineNames.get(i));
            dataSet.setColor(lineColors.get(i));
            dataSet.setCircleColor(lineColors.get(i));
            dataSet.setValueTextColor(lineColors.get(i));

            dataSet.setCircleRadius(3);
            dataSet.setDrawValues(false);
            dataSet.setLineWidth(3);
            dataSet.setValueTextSize(10f);
            dataSet.setMode(LineDataSet.Mode.LINEAR);
            dataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            lineData.addDataSet(dataSet);
        }
        return lineData;
    }

    /**
     * 得到柱状图
     *
     * @param barChartY Y轴值
     * @param barName   柱状图名字
     * @param barColor  柱状图颜色
     * @return
     */

    private BarData getBarData(List<Float> barChartY, String barName, int barColor)
    {
        BarData barData = new BarData();
        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 0; i < barChartY.size(); i++)
        {
            yValues.add(new BarEntry(i, barChartY.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(yValues, barName);
        barDataSet.setColor(barColor);
        barDataSet.setValueTextSize(10f);
        barDataSet.setValueTextColor(barColor);
        barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        barData.addDataSet(barDataSet);

        //以下是为了解决 柱状图 左右两边只显示了一半的问题 根据实际情况 而定
        xAxis.setAxisMinimum(-0.5f);
        xAxis.setAxisMaximum((float) (barChartY.size() - 0.5));
        return barData;
    }

    /**
     * 得到柱状图(多条)
     *
     * @param barChartYs Y轴值
     * @param barNames   柱状图名字
     * @param barColors  柱状图颜色
     * @return
     */

    private BarData getBarData(List<List<Float>> barChartYs, List<String> barNames, List<Integer> barColors)
    {
        List<IBarDataSet> lists = new ArrayList<>();
        for (int i = 0; i < barChartYs.size(); i++)
        {
            ArrayList<BarEntry> entries = new ArrayList<>();

            for (int j = 0; j < barChartYs.get(i).size(); j++)
            {
                entries.add(new BarEntry(j, barChartYs.get(i).get(j)));
            }
            BarDataSet barDataSet = new BarDataSet(entries, barNames.get(i));

            barDataSet.setColor(barColors.get(i));
            barDataSet.setValueTextColor(barColors.get(i));
            barDataSet.setValueTextSize(10f);
            barDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            lists.add(barDataSet);
        }
        BarData barData = new BarData(lists);

        int amount = 2; //需要显示柱状图的类别 数量
        float groupSpace = 0.12f; //柱状图组之间的间距
        float barSpace = (float) ((1 - 0.12) / amount / 10); // x4 DataSet
        float barWidth = (float) ((1 - 0.12) / amount / 10 * 9); // x4 DataSet

        // (0.2 + 0.02) * 4 + 0.12 = 1.00 即100% 按照百分百布局
        //柱状图宽度
        barData.setBarWidth(0.27f);
        //(起始点、柱状图组间距、柱状图之间间距)
        barData.groupBars(0, groupSpace, barSpace);

       /* //以下是为了解决 柱状图 左右两边只显示了一半的问题 根据实际情况 而定
        xAxis.setAxisMinimum(-0.1f);
        xAxis.setAxisMaximum((float) (barChartYs.size()- 0.1));*/
        barData.setDrawValues(true);
        return barData;
    }

    /**
     * 显示混合图(柱状图+折线图)
     *
     * @param xAxisValues X轴坐标
     * @param barChartY   柱状图Y轴值
     * @param lineChartY  折线图Y轴值
     * @param barName     柱状图名字
     * @param lineName    折线图名字
     * @param barColor    柱状图颜色
     * @param lineColor   折线图颜色
     */

    public void showCombinedChart(
            List<String> xAxisValues, List<Float> barChartY, List<Float> lineChartY
            , String barName, String lineName, int barColor, int lineColor)
    {
        initChart();
        setXAxis(xAxisValues);

        CombinedData combinedData = new CombinedData();

        combinedData.setData(getBarData(barChartY, barName, barColor));
        combinedData.setData(getLineData(lineChartY, lineName, lineColor));
        mCombinedChart.setData(combinedData);
        mCombinedChart.invalidate();
    }

    /**
     * 显示混合图(柱状图+折线图)
     *
     * @param xAxisValues X轴坐标
     * @param barChartYs  柱状图Y轴值
     * @param lineChartYs 折线图Y轴值
     * @param barNames    柱状图名字
     * @param lineNames   折线图名字
     * @param barColors   柱状图颜色
     * @param lineColors  折线图颜色
     */

    public void showCombinedChart(
            List<String> xAxisValues, List<List<Float>> barChartYs, List<List<Float>> lineChartYs,
            List<String> barNames, List<String> lineNames, List<Integer> barColors, List<Integer> lineColors)
    {
        initChart();
        setXAxis(xAxisValues);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(getLineData(lineChartYs, lineNames, lineColors));
        combinedData.setData(getBarData(barChartYs, barNames, barColors));
        mCombinedChart.setData(combinedData);
        mCombinedChart.animateX(1000); // 立即执行的动画,x轴
        mCombinedChart.invalidate();
    }

    @OnClick({R.id.ll_income_diff, R.id.ll_profit_diff, R.id.iv_edit, R.id.img_director_add, R.id.ll_more})
    public void onItemClick(View view)
    {
        switch (view.getId())
        {
            case R.id.ll_income_diff:
                tvIncomeDiff.setTextColor(getResources().getColor(R.color.blue));
                viewIncomeDiff.setVisibility(View.VISIBLE);
                tvProfitDiff.setTextColor(getResources().getColor(R.color.text_black_l));
                viewProfitDiff.setVisibility(View.INVISIBLE);
                chartFlag = 1;
                setChartDatas();
                break;
            case R.id.ll_profit_diff:
                tvIncomeDiff.setTextColor(getResources().getColor(R.color.text_black_l));
                viewIncomeDiff.setVisibility(View.INVISIBLE);
                tvProfitDiff.setTextColor(getResources().getColor(R.color.blue));
                viewProfitDiff.setVisibility(View.VISIBLE);
                chartFlag = 2;
                setChartDatas();
                break;
            case R.id.iv_edit:
                Intent intent = new Intent(PostInvestmentManagementDetailActivity.this, ActivityAddFollowUp.class);
                Bundle bundle6 = new Bundle();
                bundle6.putString("projId", financeDatas.get(indexFinance).getProjId());
                bundle6.putString("endDateString", financeDatas.get(indexFinance).getEndDate());
                bundle6.putString("signalFlag", financeDatas.get(indexFinance).getSignalFlag());
                bundle6.putString("reportFrequency", financeDatas.get(indexFinance).getFrequency());
                intent.putExtras(bundle6);
                startActivityForResult(intent, 100);
                break;

            case R.id.img_director_add:

                Intent intentDirector = new Intent(PostInvestmentManagementDetailActivity.this, ActivityDirectorInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PROJECT_EID, projectId);
                bundle.putString(Constants.PROJECT_NAME, projectName);
                intentDirector.putExtras(bundle);
                startActivityForResult(intentDirector, 1000);

                break;

            case R.id.ll_more:
                //跳转七个页面
                boolean fastDoubleClick = ButtonUtils.isFastDoubleClick();
                if (!fastDoubleClick)
                {
                    if (segmentLists != null && segmentLists.size() > 0)
                    {
                        setFGTitleString(segmentLists);
                        Intent intentFGList = new Intent(PostInvestmentManagementDetailActivity.this, InVestedProjectInfoActivity
                                .class);
                        Bundle bundleFGList = new Bundle();
                        bundleFGList.putSerializable(Constants.FG_ALL, (Serializable) segmentLists); //权限的
                        bundleFGList.putSerializable(Constants.FG_LIST_TITLE, (Serializable) listFgTitle);  //带多少title就显示多少
                        bundleFGList.putSerializable(Constants.FG_LIST, (Serializable) listFgList);           //带多少Fgment就显示多少FG
                        bundleFGList.putString(Constants.FG_DODE, InVestedProjectInfoActivity.director);  //这个是要跳转的
                        // 默认是baseInfo，第一个页面
                        bundleFGList.putString(Constants.PROJECT_EID, projectId);    //这个是projectID.
                        bundleFGList.putString(Constants.USER_NAME, loginUserAccount);
                        bundleFGList.putString(Constants.PROJECT_NAME, projectName);
                        intentFGList.putExtras(bundleFGList);
                        startActivity(intentFGList);
                    }
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        getFinanceData();
        if (requestCode == 1000 && resultCode == RESULT_OK)
        {
            postDirectorList();
        }

    }

    private void setPicView(View ll_water)
    {
        String myNickName = DisplayUtil.getUserInfo(this, 1);
        if (StringUtils.notEmpty(myNickName))
        {
            ImageUtils.waterMarking(this, true, false, ll_water, myNickName);
        }
    }

    private void postDirectorList()
    {
        ListHttpHelper.postDirectorList(PostInvestmentManagementDetailActivity.this, projectId, 1 + "", 5 + "", loginUserAccount,
                new SDRequestCallBack(BeanDirector.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
//                        mLoadingView.showNoContent("暂无数据");
                        setData();
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        BeanDirector listBean = (BeanDirector) responseInfo.getResult();
                        if (StringUtils.notEmpty(listBean.getData().getData()) && listBean.getData().getData().size() > 0)
                        {
                            directorList.clear();
                            directorList.addAll(listBean.getData().getData());
                            setData();
                        } else
                        {
                            directorList.clear();
                            setData();
                        }
                    }
                });
    }

    private void initAdapter()
    {
        if (inverstmentAdapter == null)
        {
            inverstmentAdapter = new InverstmentAdapter(R.layout.item_investment_director_info2, directorList);
        }
        DividerItemDecoration2 dividerItemDecoration2 = new DividerItemDecoration2(PostInvestmentManagementDetailActivity.this,
                DividerItemDecoration2.VERTICAL_LIST, R.drawable.recyclerview_divider, 0);
        recyclerDirector.addItemDecoration(dividerItemDecoration2);
        recyclerDirector.setLayoutManager(new LinearLayoutManager(PostInvestmentManagementDetailActivity.this));
        recyclerDirector.setAdapter(inverstmentAdapter);
        inverstmentAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {

            }
        });
    }

    private void setData()
    {
        inverstmentAdapter.notifyDataSetChanged();
    }

    private class InverstmentAdapter extends BaseQuickAdapter<BeanDirector.DataBeanX.DataBean, BaseViewHolder>
    {

        public InverstmentAdapter(@LayoutRes int layoutResId, @Nullable List<BeanDirector.DataBeanX.DataBean> data)
        {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, BeanDirector.DataBeanX.DataBean item)
        {
            holder.setText(R.id.tv_title, item.getContent());
            holder.setText(R.id.item_one, DateUtils.getDate("yyyy-MM-dd", item.getMeetingDateStr()));

            if (StringUtils.notEmpty(item.getImportantFlagStr()))
            {
                holder.setText(R.id.item_two, item.getImportantFlagStr());
            } else
            {

            }
        }
    }

    List<String> listFgTitle = new ArrayList<>();
    List<String> listFgList = new ArrayList<>();

    private void setFGTitleString(List<PotentialProjectsDetailBean.DataBeanX.DataBean.SegmentListBean> segmentLists)
    {
        listFgTitle.clear();
        listFgList.clear();
        for (int i = 0; i < segmentLists.size(); i++)
        {
//            if (!segmentLists.get(i).getCode().equals("fundInvest") || !segmentLists.get(i).getCode().equals("meeting"))
//            {
            listFgTitle.add(segmentLists.get(i).getName());
            listFgList.add(segmentLists.get(i).getCode());
//            }
        }
    }

    public void getListData()
    {
        ListHttpHelper.getPotentialProjectsDetailList(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), projectId, new
                SDRequestCallBack(PotentialProjectsDetailBean.class)
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        SDToast.showLong(msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        PotentialProjectsDetailBean bean = (PotentialProjectsDetailBean) responseInfo.getResult();

                        if (bean.getData().getData().getSegmentList() != null && bean.getData().getData().getSegmentList().size
                                () > 0)
                        {
                            segmentLists.clear();
                            segmentLists.addAll(bean.getData().getData().getSegmentList());
                        } else
                        {
                            SDToast.showShort(bean.getData().getReturnMessage());
                        }

                        if (segmentLists.size() > 0)
                        {
                            ll_more.setVisibility(View.VISIBLE);
                        } else
                        {
                            ll_more.setVisibility(View.GONE);
                        }
                    }
                });
    }
}
