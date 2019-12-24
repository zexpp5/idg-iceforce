package com.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.format.DateFormat;


import com.injoy.idg.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;


@SuppressLint("SimpleDateFormat")
public class DateUtils {
	/***
	 * 通过当前时间戳获取指定时间格式
	 * @param inFormat
	 * @return
	 */
	public static CharSequence formatDate(CharSequence inFormat) {
		return DateFormat.format(inFormat, System.currentTimeMillis());
	}

	public static String getTimestampString(Date var0) {
		String var1;
		long var2 = var0.getTime();
		if(isToday(new Date(var2))) {
			Calendar var4 = GregorianCalendar.getInstance();
			var4.setTime(var0);
			var1 = "HH:mm";
		} else {
			var1 = "M月d日 HH:mm";
		}

		return (new SimpleDateFormat(var1, Locale.CHINA)).format(var0);
	}

	public static String setBlank(String data) {
		try {
			return data.replaceAll(" ", "  ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static boolean isToday(Date date) {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		int year1 = c1.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH)+1;
		int day1 = c1.get(Calendar.DAY_OF_MONTH);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date());
		int year2 = c2.get(Calendar.YEAR);
		int month2 = c2.get(Calendar.MONTH)+1;
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		if(year1 == year2 && month1 == month2 && day1 == day2){
			return true;
		}
		return false;
	}



	/**
	 * 比较输入时间是否小于当前时间的,是则返回true,否则返回false
	 * @param date
	 * @return
	 */
	public static boolean compareDate(String date){
		long inputMillis = dateToMillis("yyyy-MM-dd HH:mm:ss", date);
		if(inputMillis > System.currentTimeMillis()){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 获取年月日
	 * @param strTime
	 * @return
	 */
	public static String getData(String strTime) {
		if(strTime!=null && strTime.trim().contains(" ")) {
			long time = dateToMillis("yyyy-MM-dd HH:mm:ss", strTime);
			return new SimpleDateFormat("yyyy-MM-dd").format(new Date(time));
		}  else {
			return strTime;
		}

	}
	
	public static CharSequence formatDate(String inFormat, long inTimeInMillis) {
		SimpleDateFormat format = new SimpleDateFormat(inFormat, Locale.CHINESE);
		return format.format(inTimeInMillis);
	}
	
	public static CharSequence formatDate(String inFormat, Date inDate) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inFormat, Locale.CHINESE);
		return simpleDateFormat.format(inDate);
	}
	
	/**
	 * 获取格式为yyyy-MM-dd HH:mm:ss的时间
	 * @param milliseconds
	 * @return
	 */
	public static String getDateAndWeekDay(long milliseconds){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		return simpleDateFormat.format(milliseconds);
	}
	/**
	 * 获取格式为yyyy-MM-dd HH:mm:ss的时间
	 * @param date
	 * @return
	 */
	public static String getDateAndWeekDay(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		return simpleDateFormat.format(date);
	}

	public static String getSimpleDate(long milliseconds){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINESE);
		return simpleDateFormat.format(milliseconds);
	}

	/**
	 * 获取指定格式的时间-转换为时间戳
	 */
	public static String getDate(String inFormat, String date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(inFormat, Locale.CHINESE);
		return simpleDateFormat.format(dateToMillis(inFormat, date)).toString();
	}

	/**
	 * 指定时间转时间戳
	 * @param inFormat
	 * @param date
	 * @return
	 */
	public static long dateToMillis(String inFormat, String date){
		SimpleDateFormat format = new SimpleDateFormat(inFormat, Locale.CHINESE);
		try {
			Date resDate = format.parse(date);
			return resDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 得到几天前的时间
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date getDateBefore(Date date, int day){
//		Calendar now =Calendar.getInstance();
//		now.setTime(d);
//		now.set(Calendar.DATE,now.get(Calendar.DATE)-day);
		long time = (date.getTime() / 1000) - day * 60 * 60 * 24;//秒
		date.setTime(time * 1000);//毫秒
		return date;
	}

	public static String getFromWhereDate(String data) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINESE);
		try {
			Date resDate = simpleDateFormat.parse(data);
			Calendar cal= Calendar.getInstance();//使用日历类
			cal.setTime(resDate);
			int year=cal.get(Calendar.YEAR);//得到年
			int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
			int day=cal.get(Calendar.DAY_OF_MONTH);//得到天
			return year+"-"+month+"-"+day;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 是否大于当前时间
	 * @param currentDate
	 * @return
	 */
	public static boolean isAfterTime(String currentDate){
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date nowDate = new Date(System.currentTimeMillis());
		Date data = null;
		try {
			data = dateFormat.parse(currentDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return data.after(nowDate);
	}


	/**
	 *
	 * @param compare1
	 * @param compare2
	 * @return (compare2 > compare1,true),(compare2 < compare1,false)
	 */
	public static boolean isAfterTime(String compare1, String compare2){
		String format = "yyyy-MM-dd HH:mm:ss";
//		String format = "yyyy-MM-dd";
		if(!compare1.trim().contains(" ")) {
			compare1 = compare1 + " 00:00:00";
			compare2 = compare2 + " 00:00:00";
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date nowDate = new Date(dateToMillis(format, compare1));
		Date data = null;
		try {
			data = dateFormat.parse(compare2);
			return data.after(nowDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

    /**
     * 根据制定日期返回星期几
     * @param context
     * @param pTime 日期
     * @return 星期几
     */
    public static String getWeek(Context context, String pTime) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            Week += context.getString(R.string.Sunday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 2) {
            Week += context.getString(R.string.Monday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 3) {
            Week += context.getString(R.string.Tuesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 4) {
            Week += context.getString(R.string.Wednesday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 5) {
            Week += context.getString(R.string.Thursday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 6) {
            Week += context.getString(R.string.Friday);
        }
        if (c.get(Calendar.DAY_OF_WEEK) == 7) {
            Week += context.getString(R.string.Saturday);
        }
        return Week;
    }
	public static Date getNextDay(Date date) {
		long time = (date.getTime() / 1000) + 60 * 60 * 24;//秒
		date.setTime(time * 1000);//毫秒
		return date;
	}
	public static Date getDayForTime(Date date, long howtime) {
		long time = (date.getTime() / 1000) + 60* howtime;//秒
		date.setTime(time * 1000);//毫秒
		return date;
	}

}
