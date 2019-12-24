package com.chaoxiang.base.utils;

import android.content.Context;

import com.chaoxiang.base.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * @date 2016-01-18
 * @desc
 */
public class DateUtils
{
    public static String getTimestampString(Date var0)
    {
        String var1 = "";
        long var2 = var0.getTime();
        if (isToday(new Date(var2)))
        {
            Calendar var4 = GregorianCalendar.getInstance();
            var4.setTime(var0);
            var1 = "HH:mm";
        } else
        {
            var1 = "M月d日 HH:mm";
        }

        return (new SimpleDateFormat(var1, Locale.CHINA)).format(var0);
    }

    public static String getTimestampString2(Date var0)
    {
        String var1 = "";
        var1 = "M月d日";
        return (new SimpleDateFormat(var1, Locale.CHINA)).format(var0);
    }

    public static String getTimestampString(long timeMillis)
    {
        return getTimestampString(new Date(timeMillis));
    }

    /**
     * 是否隐藏聊天时间
     *
     * @param currentTimeMillis
     * @param prevTimeMillis
     * @return
     */
    public static boolean isCloseEnough(long currentTimeMillis, long prevTimeMillis)
    {
        long spaceTimeMillis = currentTimeMillis - prevTimeMillis;
        if (spaceTimeMillis < 0L)
        {
            spaceTimeMillis = -spaceTimeMillis;
        }
        return spaceTimeMillis < 300000L;
    }


    /**
     * @param time
     * @return
     */
    public static String formateTimer(long time)
    {
        String str = "00:00:00";
        int hour = 0;
        if (time >= 1000 * 3600)
        {
            hour = (int) (time / (1000 * 3600));
            time -= hour * 1000 * 3600;
        }
        int minute = 0;
        if (time >= 1000 * 60)
        {
            minute = (int) (time / (1000 * 60));
            time -= minute * 1000 * 60;
        }
        int sec = (int) (time / 1000);
        str = formateNumber(hour) + ":" + formateNumber(minute) + ":" + formateNumber(sec);
        return str;
    }

    /**
     * @param time
     * @return
     */
    private static String formateNumber(int time)
    {
        return String.format("%02d", time);
    }

    public static String setBlank(String data)
    {
        try
        {
            return data.replaceAll(" ", "  ");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String showFriendlyTime(String sdate)
    {
        Date time = null;
        try
        {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdate);
        } catch (ParseException e)
        {

        }
        if (time == null)
        {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        // 判断是否是同一天
        String curDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()); //当前时间。
        String paramDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
        if (curDate.equals(paramDate))
        {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0)
        {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0)
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            else
                ftime = hour + "小时前";
        } else if (days == 1)
        {
            ftime = "昨天";
        } else if (days == 2)
        {
            ftime = "前天";
        } else if (days > 2 && days <= 10)
        {
            ftime = days + "天前";
        } else if (days >= 1)
        {
            ftime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
        }
        return ftime;
    }


    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate)
    {
        boolean b = false;
        Date time = null;
        try
        {
            time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdate);
        } catch (ParseException e)
        {

        }
        Date today = new Date();
        if (time != null)
        {
            String nowDate = new SimpleDateFormat("yyyy-MM-dd").format(today);
            String timeDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
            if (nowDate.equals(timeDate))
            {
                b = true;
            }
        }
        return b;
    }

    public static boolean isToday(Date date)
    {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(new Date());
        int year2 = c2.get(Calendar.YEAR);
        int month2 = c2.get(Calendar.MONTH) + 1;
        int day2 = c2.get(Calendar.DAY_OF_MONTH);
        if (year1 == year2 && month1 == month2 && day1 == day2)
        {
            return true;
        }
        return false;
    }


    /**
     * 比较输入时间是否小于当前时间的,是则返回true,否则返回false
     *
     * @param date
     * @return
     */
    public static boolean compareDate(String date)
    {
        long inputMillis = dateToMillis("yyyy-MM-dd HH:mm:ss", date);
        if (inputMillis > System.currentTimeMillis())
        {
            return false;
        } else
        {
            return true;
        }
    }

    public static boolean compareDate2(String date)
    {
        long inputMillis = dateToMillis("yyyy-MM-dd HH:mm:ss", date);
        if (inputMillis < System.currentTimeMillis())
        {
            return false;
        } else
        {
            return true;
        }
    }


    /**
     * 比较两个时间大小，后者比前者大
     *
     * @return
     */
    public static boolean compareTwoDate(String date1, String date2)
    {
        long inputMillis = dateToMillis("yyyy-MM-dd HH:mm", date1);
        long inputMillis2 = dateToMillis("yyyy-MM-dd HH:mm", date2);
        if (inputMillis > inputMillis2)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 比较两个时间大小，后者比前者大
     *
     * @return
     */
    public static boolean compareTwoDateM(String date1, String date2)
    {
        long inputMillis = dateToMillis("yyyy-MM-dd", date1);
        long inputMillis2 = dateToMillis("yyyy-MM-dd", date2);
        if (inputMillis > inputMillis2)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 比较两个时间大小，后者比前者大
     *
     * @return
     */
    public static boolean compareTwoDate(String date1, String date2, String inFormat)
    {
        long inputMillis = dateToMillis(inFormat, date1);
        long inputMillis2 = dateToMillis(inFormat, date2);
        if (inputMillis > inputMillis2)
        {
            return false;
        } else
        {
            return true;
        }
    }

    /**
     * 指定时间转时间戳
     *
     * @param inFormat
     * @param date
     * @return
     */
    public static long dateToMillis(String inFormat, String date)
    {
        SimpleDateFormat format = new SimpleDateFormat(inFormat, Locale.CHINESE);
        try
        {
            Date resDate = format.parse(date);
            return resDate.getTime();
        } catch (ParseException e)
        {
            e.printStackTrace();
            return 0;
        }
    }

    //通过指定时间格式返回date
    public static Date reTurnDate(String inFormat, String timeString)
    {
        SimpleDateFormat format = new SimpleDateFormat(inFormat, Locale.CHINESE);
        try
        {
            Date resDate = format.parse(timeString);
            return resDate;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取年月日
     *
     * @param strTime
     * @return
     */
    public static String getData(String strTime)
    {
        if (strTime != null && strTime.trim().contains(" "))
        {
            long time = dateToMillis("yyyy-MM-dd HH:mm:ss", strTime);
            return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
        } else
        {
            return strTime;
        }

    }

    /**
     * @param inFormat
     * @param timeMillis
     * @return
     */
    public static String getFormatDate(String inFormat, long timeMillis)
    {
        DateFormat format = new SimpleDateFormat(inFormat, Locale.CHINA);
        return format.format(timeMillis);
    }

    public static CharSequence getFormatDate(String inFormat, Date inDate)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inFormat, Locale.CHINESE);
        return simpleDateFormat.format(inDate);
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的时间
     *
     * @param milliseconds
     * @return
     */
    public static String getDateAndWeekDay(long milliseconds)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return simpleDateFormat.format(milliseconds);
    }

    /**
     * 获取格式为yyyy-MM-dd HH:mm:ss的时间
     *
     * @param date
     * @return
     */
    public static String getDateAndWeekDay(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        return simpleDateFormat.format(date);
    }

    public static String getSimpleDate(long milliseconds)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        return simpleDateFormat.format(milliseconds);
    }

    public static String getSimpleDate(Date date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
        return simpleDateFormat.format(date);
    }

    /**
     * 获取指定格式的时间-转换为指定时间格式
     */
    public static String getDate(String inFormat, String date)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inFormat, Locale.CHINESE);
        return simpleDateFormat.format(dateToMillis(inFormat, date)).toString();
    }

    /**
     * 得到几天前的时间
     *
     * @param date
     * @param day
     * @return
     */
    public static Date getDateBefore(Date date, int day)
    {
        long time = (date.getTime() / 1000) - day * 60 * 60 * 24;//秒
        date.setTime(time * 1000);//毫秒
        return date;
    }

    public static String getFromWhereDate(String data)
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
        try
        {
            Date resDate = simpleDateFormat.parse(data);
            Calendar cal = Calendar.getInstance();//使用日历类
            cal.setTime(resDate);
            int year = cal.get(Calendar.YEAR);//得到年
            int month = cal.get(Calendar.MONTH) + 1;//得到月，因为从0开始的，所以要加1
            int day = cal.get(Calendar.DAY_OF_MONTH);//得到天
            return year + "-" + month + "-" + day;
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return data;
    }

    /**
     * 是否大于当前时间
     *
     * @param currentDate
     * @return
     */
    public static boolean isAfterTime(String currentDate)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date nowDate = new Date(System.currentTimeMillis());
        Date data = null;
        try
        {
            data = dateFormat.parse(currentDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return data.after(nowDate);
    }

    /**
     * @param compare1
     * @param compare2
     * @return (compare2 > compare1, true), (compare2 < compare1, false)
     */
    public static boolean isAfterTime(String compare1, String compare2)
    {
        String format = "yyyy-MM-dd HH:mm:ss";
//		String format = "yyyy-MM-dd";
        if (!compare1.trim().contains(" "))
        {
            compare1 = compare1 + " 00:00:00";
            compare2 = compare2 + " 00:00:00";
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date nowDate = new Date(dateToMillis(format, compare1));
        Date data = null;
        try
        {
            data = dateFormat.parse(compare2);
            return data.after(nowDate);
        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据制定日期返回星期几
     *
     * @param context
     * @param pTime   日期
     * @return 星期几
     */
    public static String getWeek(Context context, String pTime)
    {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try
        {
            c.setTime(format.parse(pTime));

        } catch (ParseException e)
        {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1)
        {
            Week += context.getString(R.string.Sunday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2)
        {
            Week += context.getString(R.string.Monday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3)
        {
            Week += context.getString(R.string.Tuesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4)
        {
            Week += context.getString(R.string.Wednesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5)
        {
            Week += context.getString(R.string.Thursday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6)
        {
            Week += context.getString(R.string.Friday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7)
        {
            Week += context.getString(R.string.Saturday);
        }
        return Week;
    }

    public static Date getNextDay(Date date)
    {
        long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒
        date.setTime(time * 1000);//毫秒
        return date;
    }

    public static Date getDayForTime(Date date, long howtime)
    {
        long time = (date.getTime() / 1000) + 60 * howtime;//秒
        date.setTime(time * 1000);//毫秒
        return date;
    }

    //Calendar 转为时间
    public static String clanderTodatetime(Calendar calendar, String style)
    {
        SimpleDateFormat formatter = new SimpleDateFormat(style);
        return formatter.format(calendar.getTime());

    }

    //指定时间格式，返回当前时间
    public static String formatNowDate(String inFormat)
    {
        return getFormatDate(inFormat, System.currentTimeMillis());
    }

    public static int getNowDate(int type)
    {
        int time = 0;

        Calendar c = Calendar.getInstance();//
        if (type == Calendar.YEAR)
            time = c.get(Calendar.YEAR); // 获取当前年份
        if (type == Calendar.MONTH)
            time = c.get(Calendar.MONTH) + 1;// 获取当前月份
        if (type == Calendar.DAY_OF_MONTH)
            time = c.get(Calendar.DAY_OF_MONTH);// 获取当日期
        if (type == Calendar.DAY_OF_WEEK)
            time = c.get(Calendar.DAY_OF_WEEK);// 获取当前日期的星期
        if (type == Calendar.HOUR_OF_DAY)
            time = c.get(Calendar.HOUR_OF_DAY);//时
        if (type == Calendar.MINUTE)
            time = c.get(Calendar.MINUTE);//分
        return time;
    }


    /**
     * 根据给定的时间字符串，返回月 日 时 分 秒
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTomTime(String allDate)
    {
        return allDate.substring(5, 19);
    }

    /**
     * 根据给定的时间字符串，返回月 日 时 分 月到分钟
     *
     * @param allDate like "yyyy-MM-dd hh:mm:ss SSS"
     * @return
     */
    public static String getMonthTime(String allDate)
    {
        return allDate.substring(5, 16);
    }

    //月份，日期，加前缀
    private static String getTwoLength(final int data)
    {
        if (data < 10)
        {
            return "0" + data;
        } else
        {
            return "" + data;
        }
    }
}
