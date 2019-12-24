package newProject.ui.work;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.CalendarReminderUtils;
import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.ScreenUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;
import com.utils.DialogImUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import newProject.ui.work.backup.GeneralMeetingListActivity;
import newProject.ui.work.list.GeneralAllMeetingListActivity;
import newProject.view.DialogTextFilter;
import yunjing.utils.DividerItemDecoration2;
import yunjing.view.CustomNavigatorBar;
import yunjing.view.StatusTipsView;

/**
 * Created by selson on 2017/11/27.
 * 月会安排
 */
public class GeneralMeetingActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnMonthChangeListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnViewChangeListener
{
    @Bind(R.id.title_bar)
    CustomNavigatorBar titleBar;
    @Bind(R.id.tv_month_day)
    TextView tvMonthDay;
    @Bind(R.id.tv_year)
    TextView tvYear;
    @Bind(R.id.tv_lunar)
    TextView tvLunar;
    @Bind(R.id.tv_current_day)
    TextView tvCurrentDay;
    @Bind(R.id.calendarView)
    CalendarView mCalendarView;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.fl_current)
    FrameLayout flCurrent;
    @Bind(R.id.rl_year)
    RelativeLayout rlAllMeeting;
    @Bind(R.id.rl_month)
    RelativeLayout rlMineMeeting;

    @Bind(R.id.rl_year_chang_btn)
    RelativeLayout rl_year_chang_btn;
    @Bind(R.id.tv_week)
    TextView tv_week;

    @Bind(R.id.loading_view)
    StatusTipsView mLoadingView;

    private int mYear;

    private int tmpYear = 0;
    private int tmpMonth = 0;
    private int tmpDay = 0;

    private int selectYear = 0;
    private int selectMonth = 0;
    private int selectDay = 0;
    private String selectLunar = "";

    private GMeetingAdapter gMeetingAdapter;
    private List<GMeetingDataBean> mDatas = new ArrayList<>();

    private int monthInt = 12;  //计时次数
    private int monthTmpInt = 0; //存放比那里的月份
    private int yearInt = 0; //存放比那里的月份

    //填充详情
    private void setAdapter()
    {
        gMeetingAdapter = new GMeetingAdapter(GeneralMeetingActivity.this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(GeneralMeetingActivity.this));
        recyclerView.addItemDecoration(new DividerItemDecoration2(GeneralMeetingActivity.this, LinearLayoutManager
                .VERTICAL, R.drawable.recyclerview_divider2,
                ScreenUtils.dp2px(this, 1)));
        recyclerView.setAdapter(gMeetingAdapter);
        gMeetingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
                Intent intent = new Intent(GeneralMeetingActivity.this, SuperGMeetingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("EID", mDatas.get(position).getEid());
                bundle.putBoolean("ADD", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
//        addHeadView();
    }

    private void addHeadView()
    {
        View header = LayoutInflater.from(GeneralMeetingActivity.this).inflate(R.layout.line_layout_10dp, null);
        gMeetingAdapter.addHeaderView(header);
        gMeetingAdapter.notifyDataSetChanged();
    }

    private void reFresh()
    {
        gMeetingAdapter.notifyDataSetChanged();
    }

    private boolean isHave = false;

    @Override
    protected void init()
    {
        ButterKnife.bind(this);
        titleBar.setMidText(getResources().getString(R.string.super_find_first_02));
        titleBar.setLeftImageVisible(true);

        titleBar.setLeftImageOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });

//        titleBar.setRightImageVisible(true);
//        titleBar.setRightImageOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                CalendarReminderUtils.findEventEid(GeneralMeetingActivity.this, mDatas.get(0).getEid() + "");
//            }
//        });

        initView();
        initData();
        setAdapter();

        tmpYear = mCalendarView.getCurYear();
        tmpMonth = mCalendarView.getCurMonth();
        tmpDay = mCalendarView.getCurDay();

        monthTmpInt = tmpMonth;
        yearInt = tmpYear;

        getData(0, mYear, tmpMonth, tmpDay);
        getData(1, mYear, tmpMonth, tmpDay);

        getCalendar("year", DateUtils.getNowDate(java.util.Calendar.YEAR) + "");
    }

    private void initView()
    {
        mLoadingView.hide();
    }

    private void initData()
    {
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);

        tvYear.setText(String.valueOf(mCalendarView.getCurYear()) + "年");
        mYear = mCalendarView.getCurYear();
        tvMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        tvLunar.setText("今日");
        tv_week.setText(DateUtils.getWeek(this, mCalendarView.getCurYear() + "-" + mCalendarView.getCurMonth() + "-" +
                mCalendarView.getCurDay()));

        tvCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_general_meeting_main;
    }

    @OnClick({R.id.rl_year_chang_btn, R.id.fl_current, R.id.rl_year, R.id.rl_month})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.rl_year_chang_btn:

                mCalendarView.showYearSelectLayout(mYear);
                tvLunar.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                tv_week.setVisibility(View.GONE);
                tvMonthDay.setText(String.valueOf(mYear) + "年");

                break;
            case R.id.fl_current:
                mCalendarView.scrollToCurrent();
                break;
            //所有的
            case R.id.rl_year:
                Intent intentAll = new Intent(this, GeneralAllMeetingListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(GeneralAllMeetingListActivity.TYPE, 0);
                intentAll.putExtras(bundle);
                startActivity(intentAll);
                break;
            //我的
            case R.id.rl_month:

                Intent intentMine = new Intent(this, GeneralAllMeetingListActivity.class);
                Bundle bundle2 = new Bundle();
                bundle2.putInt(GeneralAllMeetingListActivity.TYPE, 1);
                intentMine.putExtras(bundle2);
                startActivity(intentMine);
                break;
        }
    }

    private void getData(final int type, final int yearFilter, final int monthFilter, final int dayFilter)
    {
        String monthString = "";
        String dayString = "";
        if (monthFilter < 10)
        {
            monthString = "0" + monthFilter;
        } else
        {
            monthString = monthFilter + "";
        }
        if (dayFilter < 10)
        {
            dayString = "0" + dayFilter;
        } else
        {
            dayString = dayFilter + "";
        }

        String dateString = "";
        String typeString = "";
        if (type == 0)
        {
            typeString = "month";
            dateString = yearFilter + "-" + monthString;
        } else if (type == 1)
        {
            typeString = "day";
            dateString = yearFilter + "-" + monthString + "-" + dayString;
        }
        BasicDataHttpHelper.postGMeetingMonthOrdayList(GeneralMeetingActivity.this, typeString, dateString, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(GeneralMeetingActivity.this, msg);
                        setList(null);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        List<GMeetingDataBean> dataList = new ArrayList<GMeetingDataBean>();
                        if (type == 0)
                        {
                            GMeetingDataBean gMeetingDataBean = null;
                            try
                            {
                                JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                                JSONObject mapJSON = jsonObject.getJSONObject("data");
                                // 动态获取key值
                                Iterator<String> iterator = mapJSON.keys();
                                if (iterator.hasNext())
                                {
                                    if (!isHave)
                                    {
                                        isHave = true;
                                        mCalendarView.scrollToCalendar(yearFilter, monthFilter, dayFilter);
                                    }
                                    while (iterator.hasNext())
                                    {
                                        String key = iterator.next();
                                        JSONArray keyArray = mapJSON.getJSONArray(key);

                                        for (int i = 0; i < keyArray.length(); i++)
                                        {
                                            JSONObject jsonObjectBean = keyArray.getJSONObject(i);
                                            int eid = jsonObjectBean.getInt("eid");
                                            String endTime = jsonObjectBean.getString("endTime");
                                            String meetPlace = jsonObjectBean.getString("meetPlace");
                                            String startTime = jsonObjectBean.getString("startTime");
                                            String startTimeKey = jsonObjectBean.getString("startTimeKey");
                                            String title = jsonObjectBean.getString("title");

                                            gMeetingDataBean = new GMeetingDataBean();
                                            gMeetingDataBean.setEid(eid);
                                            gMeetingDataBean.setEndTime(endTime);
                                            gMeetingDataBean.setMeetPlace(meetPlace);
                                            gMeetingDataBean.setStartTime(startTime);
                                            gMeetingDataBean.setStartTimeKey(startTimeKey);
                                            gMeetingDataBean.setTitle(title);

                                            dataList.add(gMeetingDataBean);
                                        }
                                    }
                                    if (dataList.size() > 0)
                                    {
                                        setList(dataList);
                                    } else
                                    {
                                        setList(null);
                                    }
                                } else
                                {
                                    setList(null);
                                    //继续请求
                                    if (!isHave)
                                    {
                                        if (monthInt > 1)
                                        {
                                            --monthInt;
                                            if (monthTmpInt < 1)
                                            {
                                                if (yearInt == mYear)
                                                {
                                                    --yearInt;
                                                    monthTmpInt = 12;
                                                    getData(0, yearInt, monthTmpInt, tmpDay);
                                                } else
                                                {
//                                                    getData(0, yearInt, monthTmpInt, tmpDay);
                                                }
                                            } else
                                            {
                                                --monthTmpInt;
                                                getData(0, yearInt, monthTmpInt, tmpDay);
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        } else if (type == 1)
                        {
                            GMDataList gmDataList = GMDataList.parse(responseInfo.getResult().toString());
                            if (gmDataList != null)
                            {
                                mDatas.clear();
                                if (gmDataList.getData() != null && gmDataList.getData().size() > 0)
                                {
                                    mLoadingView.hide();
                                    mDatas.addAll(gmDataList.getData());
                                } else
                                {
                                    mLoadingView.showText("暂无会议安排");
                                }
                                reFresh();
                            }
                        }
                        SDLogUtil.debug("1");
                    }
                });
    }

    private void setList(List<GMeetingDataBean> gMeetingDataBeanList)
    {
        List<Calendar> schemes = new ArrayList<>();
        if (gMeetingDataBeanList != null && gMeetingDataBeanList.size() > 0)
        {
            for (int i = 0; i < gMeetingDataBeanList.size(); i++)
            {
                String str0 = gMeetingDataBeanList.get(i).getStartTimeKey();
                Date d1 = null;//定义起始日期
                try
                {
                    d1 = new SimpleDateFormat("yyyy-MM-dd").parse(str0);
                    SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
                    SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
                    SimpleDateFormat sdf2 = new SimpleDateFormat("dd");
                    schemes.add(getSchemeCalendar(tmpYear, tmpMonth, tmpDay, Color.parseColor("#d9d9d9"), "议"));
                    if (sdf0.format(d1).equals(tmpYear + "") && sdf1.format(d1).equals(tmpMonth + "") && sdf2.format(d1).equals
                            (tmpDay + ""))
                    {
//                        schemes.add(getSchemeCalendar(selectYear, selectMonth, selectDay, Color.parseColor("#F2DBDE"), "议"));
                    } else//今天的
                    {
                        schemes.add(getSchemeCalendar(Integer.parseInt(sdf0.format(d1)),
                                Integer.parseInt(sdf1.format(d1)), Integer.parseInt(sdf2.format(d1)),
                                0xFFF2DBDE, "议"));
                    }
//                    schemes.add(getSchemeCalendar(selectYear, selectMonth, selectDay, Color.parseColor("#AD1128"), "议"));
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        } else
        {
            schemes.add(getSchemeCalendar(tmpYear, tmpMonth, tmpDay, Color.parseColor("#d9d9d9"), "议"));
        }
        mCalendarView.setSelected(false);
        mCalendarView.setSchemeDate(schemes);
    }

    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text)
    {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onYearChange(int year)
    {
        tvMonthDay.setText(String.valueOf(year));
    }

    @Override
    public void onMonthChange(int year, int month)
    {
        mDatas.clear();
        reFresh();

        tvLunar.setVisibility(View.VISIBLE);
        tvYear.setVisibility(View.VISIBLE);
        tv_week.setVisibility(View.VISIBLE);
        tvYear.setText(String.valueOf(year) + "年");

        mYear = year;
        getData(0, year, month, 1);

        //上一个选中的是否当前年月
        if (StringUtils.notEmpty(selectYear) && StringUtils.notEmpty(selectMonth) && StringUtils.notEmpty(selectDay))
            if (selectYear == year && selectMonth == month)
            {
                getData(1, selectYear, selectMonth, selectDay);
                tvMonthDay.setText(selectMonth + "月" + selectDay + "日");
                tvYear.setText(selectYear + "年");
                tvLunar.setText("(" + selectLunar + ")");
                tv_week.setText(DateUtils.getWeek(this, selectYear + "-" + selectMonth + "-" + selectDay));
            }
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick)
    {
        mDatas.clear();
        tvLunar.setVisibility(View.VISIBLE);
        tvYear.setVisibility(View.VISIBLE);
        tvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        tvYear.setText(String.valueOf(calendar.getYear()) + "年");
        tvLunar.setText("(" + calendar.getLunar() + ")");

        tv_week.setText(DateUtils.getWeek(this, calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay()));

//        MyToast.showToast(this, "星期：" + calendar.getWeek() + "");
        mYear = calendar.getYear();

        //选中的
        selectYear = calendar.getYear();
        selectMonth = calendar.getMonth();
        selectDay = calendar.getDay();
        selectLunar = calendar.getLunar();

        getData(1, calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }

    @Override
    public void onViewChange(boolean isMonthView)
    {

    }

    private List<GMeetingDataBean> tmpCalendarData = new ArrayList<>();

    //同步日历
    private void getCalendar(String type, String date)
    {
        BasicDataHttpHelper.postMeetingtoCalendar(GeneralMeetingActivity.this, type, date, "", new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(GeneralMeetingActivity.this, msg);
                    }

                    @Override
                    public void onRequestSuccess(SDResponseInfo responseInfo)
                    {
                        GMeetingDataBean gMeetingDataBean = null;
                        try
                        {
                            tmpCalendarData.clear();
                            JSONObject jsonObject = new JSONObject(responseInfo.getResult().toString());
                            JSONObject mapJSON = jsonObject.getJSONObject("data");
                            // 动态获取key值
                            Iterator<String> iterator = mapJSON.keys();
                            while (iterator.hasNext())
                            {
                                String key = iterator.next();
                                JSONArray keyArray = mapJSON.getJSONArray(key);
                                for (int i = 0; i < keyArray.length(); i++)
                                {
                                    JSONObject jsonObjectBean = keyArray.getJSONObject(i);
                                    int eid = jsonObjectBean.getInt("eid");
                                    String endTime = jsonObjectBean.getString("endTime");
                                    String meetPlace = jsonObjectBean.getString("meetPlace");
                                    String startTime = jsonObjectBean.getString("startTime");
                                    String startTimeKey = jsonObjectBean.getString("startTimeKey");
                                    String title = jsonObjectBean.getString("title");

                                    gMeetingDataBean = new GMeetingDataBean();
                                    gMeetingDataBean.setEid(eid);
                                    gMeetingDataBean.setEndTime(endTime);
                                    gMeetingDataBean.setMeetPlace(meetPlace);
                                    gMeetingDataBean.setStartTime(startTime);
                                    gMeetingDataBean.setStartTimeKey(startTimeKey);
                                    gMeetingDataBean.setTitle(title);

                                    tmpCalendarData.add(gMeetingDataBean);
                                }

                            }
                            if (tmpCalendarData.size() > 0)
                            {
                                titleBar.setRightText("同 步");
                                titleBar.setRightTextVisible(false);
                                titleBar.setRightTextOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View v)
                                    {
                                        showMeetingFinish();
                                    }
                                });
                            }
                        } catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void synchronizationCalendar()
    {
        if (tmpCalendarData != null && tmpCalendarData.size() > 0)
            for (int i = 0; i < tmpCalendarData.size(); i++)
            {
                CalendarReminderUtils.addCalendarEvent(GeneralMeetingActivity.this,
                        tmpCalendarData.get(i)
                                .getEid() + "", tmpCalendarData.get(i)
                                .getTitle(), tmpCalendarData.get(i)
                                .getRemark(), DateUtils.dateToMillis("yyyy-MM-dd HH:mm:ss",
                                tmpCalendarData.get(i).getStartTime()), DateUtils.dateToMillis
                                ("yyyy-MM-dd HH:mm:ss",
                                        tmpCalendarData.get(i).getEndTime()), 1);
            }
    }

    private void showMeetingFinish()
    {
        DialogTextFilter dialogTextFilter = new DialogTextFilter();
        dialogTextFilter.setContentString("是否将会议信息同步到日历?");
        dialogTextFilter.setYesString("是");
        dialogTextFilter.setNoString("否");
        DialogImUtils.getInstance().showCommonDialog(GeneralMeetingActivity.this, dialogTextFilter, new
                DialogImUtils.OnYesOrNoListener()
                {
                    @Override
                    public void onYes()
                    {
                        synchronizationCalendar();
                    }

                    @Override
                    public void onNo()
                    {

                    }
                });
    }
}
