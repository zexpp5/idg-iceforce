package newProject.company.investment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cxgz.activity.cxim.base.BaseActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.utils.SDToast;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.api.ListHttpHelper;
import newProject.company.investment.adapter.WorkAdapter;
import newProject.company.investment.bean.WorkChartBean;
import newProject.company.investment.bean.WorkDetailNumBean;
import newProject.company.investment.bean.WorkPChartBean;
import newProject.company.project_manager.investment_project.bean.NameListsBean;

import static com.utils.SPUtils.USER_ACCOUNT;

/**
 * Created by zsz on 2019/8/30.
 */

public class WorkReportDetailActivity extends BaseActivity {
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_follow_num)
    TextView tvFollowNum;
    @Bind(R.id.tv_new_num)
    TextView tvNewNum;
    @Bind(R.id.tv_saw_num)
    TextView tvSawNum;
    @Bind(R.id.tv_not_see_num)
    TextView tvNotSeeNum;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.chart_1)
    BarChart chart1;
    @Bind(R.id.chart_2)
    BarChart chart2;
    @Bind(R.id.chart_3)
    BarChart chart3;
    @Bind(R.id.chart_4)
    BarChart chart4;
    @Bind(R.id.tv_follow_num_2)
    TextView tvFollowNum2;
    @Bind(R.id.tv_new_num_2)
    TextView tvNewNum2;
    @Bind(R.id.tv_saw_num_2)
    TextView tvSawNum2;
    @Bind(R.id.tv_not_see_num_2)
    TextView tvNotSeeNum2;
    @Bind(R.id.ll_recycle)
    LinearLayout llRecycle;
    String dateFlag;
    String date;

    WorkAdapter horizonAdapter;
    List<NameListsBean.DataBeanX.DataBean> horizonDatas = new ArrayList<>();
    int index = 0;

    String[] xData;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_work_report_detail;
    }

    @Override
    protected void init() {
        ButterKnife.bind(this);
        dateFlag = getIntent().getStringExtra("dataFlag");
        date = getIntent().getStringExtra("date");
        tvDate.setText(date);
        LinearLayoutManager lm = new LinearLayoutManager(this);
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(lm);
        horizonAdapter = new WorkAdapter(horizonDatas);
        recyclerView.setAdapter(horizonAdapter);
        horizonAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                horizonDatas.get(index).setChoose(false);
                index = position;
                horizonDatas.get(index).setChoose(true);
                horizonAdapter.notifyDataSetChanged();
                getNumData(horizonDatas.get(index).getUserAccount(), tvDate.getText().toString(), 2);
            }
        });
        getNumData(SPUtils.get(this, USER_ACCOUNT, "").toString(), date, 1);
        String flag = (String) SPUtils.get(this, SPUtils.ROLE_FLAG, "0");
        if (flag.equals("205")) {
            llRecycle.setVisibility(View.GONE);

        } else {
            getNameListData();
        }

        if (flag.equals("205")) {
            getChartDataForInvestment("trackProj");
            getChartDataForInvestment("newProj");
            getChartDataForInvestment("readProj");
            getChartDataForInvestment("toLookProj");
        } else {

            getChartDataForPartner("trackProj");
            getChartDataForPartner("newProj");
            getChartDataForPartner("readProj");
            getChartDataForPartner("toLookProj");
        }
    }

    @OnClick({R.id.iv_back, R.id.ll_reduce, R.id.ll_add})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_reduce:
                if (dateFlag.equals("month")) {
                    String dateStr = tvDate.getText().toString();
                    int month = Integer.parseInt(dateStr.substring(dateStr.indexOf("年") + 1, dateStr.indexOf("月")));
                    if (month - 1 > 0) {
                        tvDate.setText(dateStr.replace("年" + month + "月", "年" + (month - 1) + "月"));
                    } else {
                        int year = Integer.parseInt(dateStr.substring(0, dateStr.indexOf("年")));
                        tvDate.setText((year - 1) + "年12月");
                    }
                } else {
                    String dateStr = tvDate.getText().toString();
                    int month = Integer.parseInt(dateStr.substring(dateStr.indexOf("Q") + 1, dateStr.length()));
                    if (month - 1 > 0) {
                        tvDate.setText(dateStr.replace("Q" + month, "Q" + (month - 1)));
                    } else {
                        int year = Integer.parseInt(dateStr.substring(0, dateStr.indexOf("Q")));
                        tvDate.setText((year - 1) + "Q4");
                    }
                }
                getNumData(SPUtils.get(this, USER_ACCOUNT, "").toString(), tvDate.getText().toString(), 1);
                String flag = (String) SPUtils.get(this, SPUtils.ROLE_FLAG, "0");

                if (flag.equals("205")) {
                    llRecycle.setVisibility(View.GONE);

                } else {
                    horizonDatas.get(index).setChoose(false);
                    index = 0;
                    horizonDatas.get(index).setChoose(true);
                    horizonAdapter.notifyDataSetChanged();
                    getNumData(horizonDatas.get(index).getUserAccount(), tvDate.getText().toString(), 2);
                }

                if (flag.equals("205")) {
                    getChartDataForInvestment("trackProj");
                    getChartDataForInvestment("newProj");
                    getChartDataForInvestment("readProj");
                    getChartDataForInvestment("toLookProj");
                } else {

                    getChartDataForPartner("trackProj");
                    getChartDataForPartner("newProj");
                    getChartDataForPartner("readProj");
                    getChartDataForPartner("toLookProj");
                }
                break;
            case R.id.ll_add:
                if (dateFlag.equals("month")) {
                    String dateStr = tvDate.getText().toString();
                    int month = Integer.parseInt(dateStr.substring(dateStr.indexOf("年") + 1, dateStr.indexOf("月")));
                    int year = Integer.parseInt(dateStr.substring(0, dateStr.indexOf("年")));
                    int MONTH = Integer.parseInt(date.substring(date.indexOf("年") + 1, date.indexOf("月")));
                    int YEAR = Integer.parseInt(date.substring(0, date.indexOf("年")));

                    if (YEAR > year) {
                        if (month + 1 > 12) {
                            String sss = dateStr.replace(year + "年" + month + "月", (year + 1) + "年1月");
                            tvDate.setText(sss);
                        } else {
                            tvDate.setText(dateStr.replace("年" + month + "月", "年" + (month + 1) + "月"));
                        }
                    } else if (YEAR == year) {
                        if (MONTH > month) {
                            tvDate.setText(dateStr.replace("年" + month + "月", "年" + (month + 1) + "月"));
                        } else {
                            tvDate.setText(date);
                        }

                    }
                } else {
                    String dateStr = tvDate.getText().toString();
                    int month = Integer.parseInt(dateStr.substring(dateStr.indexOf("Q") + 1, dateStr.length()));
                    int year = Integer.parseInt(dateStr.substring(0, dateStr.indexOf("Q")));
                    int MONTH = Integer.parseInt(date.substring(date.indexOf("Q") + 1, date.length()));
                    int YEAR = Integer.parseInt(date.substring(0, date.indexOf("Q")));

                    if (YEAR > year) {
                        if (month + 1 > 4) {
                            String sss = dateStr.replace(year + "Q" + month, (year + 1) + "Q1");
                            tvDate.setText(sss);
                        } else {
                            tvDate.setText(dateStr.replace("Q" + month, "Q" + (month + 1)));
                        }
                    } else if (YEAR == year) {
                        if (MONTH > month) {
                            tvDate.setText(dateStr.replace("Q" + month, "Q" + (month + 1)));
                        } else {
                            tvDate.setText(date);
                        }

                    }
                }
                getNumData(SPUtils.get(this, USER_ACCOUNT, "").toString(), tvDate.getText().toString(), 1);
                String flag2 = (String) SPUtils.get(this, SPUtils.ROLE_FLAG, "0");
                if (flag2.equals("205")) {
                    getChartDataForInvestment("trackProj");
                    getChartDataForInvestment("newProj");
                    getChartDataForInvestment("readProj");
                    getChartDataForInvestment("toLookProj");
                } else {

                    getChartDataForPartner("trackProj");
                    getChartDataForPartner("newProj");
                    getChartDataForPartner("readProj");
                    getChartDataForPartner("toLookProj");
                }
                break;
        }
    }

    public void getNumData(String username, String dateStr, final int flag) {
        ListHttpHelper.getMyWorkReportNumData(this, username, dateStr, dateFlag, new SDRequestCallBack(WorkDetailNumBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkDetailNumBean bean = (WorkDetailNumBean) responseInfo.getResult();
                if (flag == 1) {
                    tvFollowNum.setText(bean.getData().getData().getTrackNum() + "");
                    tvNewNum.setText(bean.getData().getData().getNewNum() + "");
                    tvSawNum.setText(bean.getData().getData().getReadNum() + "");
                    tvNotSeeNum.setText(bean.getData().getData().getToLookNum() + "");
                } else {
                    tvFollowNum2.setText(bean.getData().getData().getTrackNum() + "");
                    tvNewNum2.setText(bean.getData().getData().getNewNum() + "");
                    tvSawNum2.setText(bean.getData().getData().getReadNum() + "");
                    tvNotSeeNum2.setText(bean.getData().getData().getToLookNum() + "");
                }
            }
        });

    }

    public void getNameListData() {
        ListHttpHelper.getWorkSummaryDetailNameListData(this, com.chaoxiang.base.utils.SPUtils.get(this, USER_ACCOUNT, "").toString(), new SDRequestCallBack(NameListsBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                NameListsBean bean = (NameListsBean) responseInfo.getResult();
                horizonDatas.clear();
                horizonDatas.addAll(bean.getData().getData());
                horizonDatas.get(index).setChoose(true);
                horizonAdapter.notifyDataSetChanged();
                getNumData(horizonDatas.get(index).getUserAccount(), tvDate.getText().toString(), 2);
            }
        });
    }

    public void getChartDataForPartner(final String type) {
        ListHttpHelper.getMyWorkReportChartData(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), tvDate.getText().toString(), dateFlag, type, new SDRequestCallBack(WorkPChartBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkPChartBean bean = (WorkPChartBean) responseInfo.getResult();
                xData = new String[bean.getData().getData().size()];
                for (int i = 0; i < bean.getData().getData().size(); i++) {
                    xData[i] = bean.getData().getData().get(i).getUserName();
                }
                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                for (int i = 0; i < bean.getData().getData().size(); i++) {
                    yVals1.add(new BarEntry(i + 1, bean.getData().getData().get(i).getNum()));
                }

                if (type.equals("trackProj")) {
                    initChart(chart1, yVals1,bean.getData().getData().size());
                }else if (type.equals("newProj")) {
                    initChart(chart2, yVals1,bean.getData().getData().size());
                }else if (type.equals("readProj")) {
                    initChart(chart3, yVals1,bean.getData().getData().size());
                }else if (type.equals("toLookProj")) {
                    initChart(chart4, yVals1,bean.getData().getData().size());
                }
            }
        });

    }

    public void getChartDataForInvestment(final String type) {
        ListHttpHelper.getMyWorkReportChartDataForInvestment(this, SPUtils.get(this, USER_ACCOUNT, "").toString(), tvDate.getText().toString(), dateFlag, type, new SDRequestCallBack(WorkChartBean.class) {
            @Override
            public void onRequestFailure(HttpException error, String msg) {
                SDToast.showShort(msg);
            }

            @Override
            public void onRequestSuccess(SDResponseInfo responseInfo) {
                WorkChartBean bean = (WorkChartBean) responseInfo.getResult();
                xData = new String[bean.getData().getData().size()];
                for (int i = 0; i < bean.getData().getData().size(); i++) {
                    xData[i] = bean.getData().getData().get(i).getDateStr();
                }
                ArrayList<BarEntry> yVals1 = new ArrayList<>();
                for (int i = 0; i < bean.getData().getData().size(); i++) {
                    yVals1.add(new BarEntry(i + 1, bean.getData().getData().get(i).getNum()));
                }

                if (type.equals("trackProj")) {
                    initChart(chart1, yVals1,bean.getData().getData().size());
                }else if (type.equals("newProj")) {
                    initChart(chart2, yVals1,bean.getData().getData().size());
                }else if (type.equals("readProj")) {
                    initChart(chart3, yVals1,bean.getData().getData().size());
                }else if (type.equals("toLookProj")) {
                    initChart(chart4, yVals1,bean.getData().getData().size());
                }
            }
        });

    }


    /**
     * 初始化Chart
     */
    private void initChart(BarChart mBarChart, ArrayList<BarEntry> yVals1,int size) {



        //设置柱状图点击的时候，的回调函数
        // mBarChart.setOnChartValueSelectedListener(this);
        //柱状图的阴影
        mBarChart.setDrawBarShadow(false);
        //设置柱状图Value值显示在柱状图上方 true 为显示上方，默认false value值显示在柱状图里面
        mBarChart.setDrawValueAboveBar(true);
        //Description Label 是否可见
        mBarChart.getDescription().setEnabled(false);
        // 设置最大可见Value值的数量 针对于ValueFormartter有效果
        mBarChart.setMaxVisibleValueCount(60);
        // 二指控制X轴Y轴同时放大
        mBarChart.setPinchZoom(false);
        //是否显示表格背景颜色
        mBarChart.setDrawGridBackground(false);
        //设置X轴显示文字旋转角度-60意为逆时针旋转60度
        //mBarChart.getXAxis().setLabelRotationAngle(-60);

        XAxis xAxis = mBarChart.getXAxis();
        //设置X轴显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //X轴纵向分割线，一般不设置显示
        xAxis.setDrawGridLines(false);
        // X轴显示Value值的精度，与自定义X轴返回的Value值精度一致
        xAxis.setGranularity(1f);

        //X轴最大坐标
        xAxis.setAxisMaximum(size + 1);
        //X轴最小坐标
        xAxis.setAxisMinimum(0.5f);
        //X轴横坐标显示的数量
        xAxis.setLabelCount(size + 2, false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                int index = (int) value;
                if (index == 0 || index > xData.length) {
                    return "";
                } else {
                    return xData[index - 1];
                }
            }
        });
        //Y左边轴
        YAxis leftAxis = mBarChart.getAxisLeft();
        //设置Y左边轴显示的值 label 数量
        leftAxis.setLabelCount(5, false);
        //设置值显示的位置，我们这里设置为显示在Y轴外面
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //设置Y轴 与值的空间空隙 这里设置30f意为30%空隙，默认是10%
        leftAxis.setSpaceTop(10f);
        //设置Y轴最小坐标和最大坐标
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(50f);

        //Y轴右边轴的设置，跟左边轴类似
        YAxis rightAxis = mBarChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(10f);
        rightAxis.setAxisMinimum(0f);
        rightAxis.setAxisMaximum(50f);


        // 设置表格标示的位置
        Legend l = mBarChart.getLegend();
        //标示坐落再表格左下方
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        //标示水平朝向
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //标示在表格外
        l.setDrawInside(false);
        //样式
        l.setForm(Legend.LegendForm.SQUARE);
        //字体
        l.setFormSize(9f);
        //大小
        l.setTextSize(11f);


        BarDataSet set1;

        set1 = new BarDataSet(yVals1, "");
        //设置有四种颜色
        //set1.setColors(ColorTemplate.MATERIAL_COLORS);
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setBarWidth(0.6f);
        //data.setValueFormatter(new CallCountValueFormatter());
        //设置数据
        mBarChart.setData(data);
        /*mBarChart.getData().notifyDataChanged();
        mBarChart.notifyDataSetChanged();*/
        mBarChart.animateXY(1000,1000);
        mBarChart.invalidate();

    }


}
