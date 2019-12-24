package newProject.ui.work.backup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.base.BaseActivity;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;
import com.http.SDResponseInfo;
import com.http.callback.SDRequestCallBack;
import com.injoy.idg.R;
import com.lidroid.xutils.exception.HttpException;
import com.ui.http.BasicDataHttpHelper;

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
import newProject.ui.work.GMDataList;
import newProject.ui.work.GMeetingAdapter;
import newProject.ui.work.GMeetingDataBean;
import newProject.ui.work.SuperGMeetingActivity;
import yunjing.view.CustomNavigatorBar;

/**
 * Created by selson on 2017/11/27.
 * 日常会议 //备用的
 */
public class GeneralMeetingListActivity extends BaseActivity implements
        CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener,
        CalendarView.OnMonthChangeListener
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

    private int mYear;

    private int tmpYear = 0;
    private int tmpMonth = 0;
    private int tmpDay = 0;

    private int selectYear = 0;
    private int selectMonth = 0;
    private int selectDay = 0;
    private String selectLunar = "";

    //
    private GMeetingAdapter gMeetingAdapter;
    private List<GMeetingDataBean> mDatas = new ArrayList<>();

    //填充详情
    private void setAdapter()
    {
        gMeetingAdapter = new GMeetingAdapter(GeneralMeetingListActivity.this, mDatas);
        recyclerView.setLayoutManager(new LinearLayoutManager(GeneralMeetingListActivity.this));
        recyclerView.setAdapter(gMeetingAdapter);
        gMeetingAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position)
            {
//                SuperGMeetingActivity
                Intent intent = new Intent(GeneralMeetingListActivity.this, SuperGMeetingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("EID", mDatas.get(position).getEid());
                bundle.putBoolean("ADD", false);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void reFresh()
    {
        gMeetingAdapter.notifyDataSetChanged();
    }

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

        initView();
        initData();

        setAdapter();

        tmpYear = mCalendarView.getCurYear();
        tmpMonth = mCalendarView.getCurMonth();
        tmpDay = mCalendarView.getCurDay();

        getData(0, mYear, mCalendarView.getCurMonth(), mCalendarView.getCurDay());
        getData(1, mYear, mCalendarView.getCurMonth(), mCalendarView.getCurDay());
    }

    private void initView()
    {

    }

    private void initData()
    {
        mCalendarView.setOnYearChangeListener(this);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnMonthChangeListener(this);
        tvYear.setText(String.valueOf(mCalendarView.getCurYear()));
        mYear = mCalendarView.getCurYear();
        tvMonthDay.setText(mCalendarView.getCurMonth() + "月" + mCalendarView.getCurDay() + "日");
        tvLunar.setText("今日");
        tvCurrentDay.setText(String.valueOf(mCalendarView.getCurDay()));
    }

    @Override
    protected int getContentLayout()
    {
        return R.layout.activity_general_meeting_list;
    }

    @OnClick({R.id.tv_month_day, R.id.fl_current})
    public void onViewClicked(View view)
    {
        switch (view.getId())
        {
            case R.id.tv_month_day:

                mCalendarView.showSelectLayout(mYear);
                tvLunar.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                tvMonthDay.setText(String.valueOf(mYear));

                break;
            case R.id.fl_current:

                mCalendarView.scrollToCurrent();

                break;
        }
    }

    private void getData(final int type, int year, int month, int day)
    {
        String monthString = "";
        String dayString = "";
        if (month < 10)
        {
            monthString = "0" + month;
        } else
        {
            monthString = month + "";
        }
        if (day < 10)
        {
            dayString = "0" + day;
        } else
        {
            dayString = day + "";
        }

        String dateString = "";
        String typeString = "";
        if (type == 0)
        {
            typeString = "month";
            dateString = year + "-" + monthString;
        } else if (type == 1)
        {
            typeString = "day";
            dateString = year + "-" + monthString + "-" + dayString;
        }
        BasicDataHttpHelper.postGMeetingMonthOrdayList(GeneralMeetingListActivity.this, typeString, dateString, new
                SDRequestCallBack()
                {
                    @Override
                    public void onRequestFailure(HttpException error, String msg)
                    {
                        MyToast.showToast(GeneralMeetingListActivity.this, msg);
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
                                while (iterator.hasNext())
                                {
                                    String key = iterator.next();
                                    JSONArray keyArray = mapJSON.getJSONArray(key);
                                    gMeetingDataBean = new GMeetingDataBean();
                                    for (int i = 0; i < keyArray.length(); i++)
                                    {
                                        JSONObject jsonObjectBean = keyArray.getJSONObject(i);
                                        int eid = jsonObjectBean.getInt("eid");
                                        String endTime = jsonObjectBean.getString("endTime");
                                        String meetPlace = jsonObjectBean.getString("meetPlace");
                                        String startTime = jsonObjectBean.getString("startTime");
                                        String startTimeKey = jsonObjectBean.getString("startTimeKey");
                                        String title = jsonObjectBean.getString("title");

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
                                    mDatas.addAll(gmDataList.getData());
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
        int year = mCalendarView.getCurYear();
        int month = mCalendarView.getCurMonth();

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
                        //今天的
                    } else
                    {
                        schemes.add(getSchemeCalendar(Integer.parseInt(sdf0.format(d1)),
                                Integer.parseInt(sdf1.format(d1)), Integer.parseInt(sdf2.format(d1)),
                                0xFFF2DBDE, "议"));
                    }

                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
            }
        } else
        {
            schemes.add(getSchemeCalendar(tmpYear, tmpMonth, tmpDay, Color.parseColor("#d9d9d9"), "议"));
        }

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
    public void onDateSelected(Calendar calendar, boolean isClick)
    {
        mDatas.clear();

        tvLunar.setVisibility(View.VISIBLE);
        tvYear.setVisibility(View.VISIBLE);
        tvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        tvYear.setText(String.valueOf(calendar.getYear()));
        tvLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

        getData(1, calendar.getYear(), calendar.getMonth(), calendar.getDay());

        //选中的
        selectYear = calendar.getYear();
        selectMonth = calendar.getMonth();
        selectDay = calendar.getDay();
        selectLunar = calendar.getLunar();
    }

    @Override
    public void onMonthChange(int year, int month)
    {
        mDatas.clear();
        reFresh();

        mDatas.clear();
        reFresh();

        tvLunar.setVisibility(View.VISIBLE);
        tvYear.setVisibility(View.VISIBLE);
        tvYear.setText(String.valueOf(year));
        //        tvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        //        tvLunar.setText(calendar.getLunar());
        mYear = year;

        getData(0, year, month, 1);

        //上一个选中的是否当前年月
        if (StringUtils.notEmpty(selectYear) && StringUtils.notEmpty(selectMonth) && StringUtils.notEmpty(selectDay))
            if (selectYear == year && selectMonth == month)
            {
                getData(1, selectYear, selectMonth, selectDay);
                tvMonthDay.setText(selectMonth + "月" + selectDay + "日");
                tvYear.setText(String.valueOf(year));
                tvLunar.setText(selectLunar);
            }

    }
}
