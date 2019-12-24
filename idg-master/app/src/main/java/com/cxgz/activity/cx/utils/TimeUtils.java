package com.cxgz.activity.cx.utils;


import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具
 */
public class TimeUtils
{
    private SimpleDateFormat sf = null;

    /*获取系统时间 格式为："yyyy/MM/dd "*/
    public static String getCurrentDate()
    {
        Date d = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /*时间戳转换成字符窜*/
    public static String getDateToString(long time)
    {
        Date d = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        return sf.format(d);
    }

    /*将字符串转为时间戳*/
    public static long getStringToDate(String time)
    {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date();
        try
        {
            date = sf.parse(time);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    /**
     * @param
     * @param
     * @return把时间戳转成日期格式
     */
    public static String formatPhotoDate(long time)
    {
        return timeFormat(time, "yyyy-MM-dd");
    }

    /**
     * @param timeMillis
     * @param pattern
     * @return把时间戳转成日期格式
     */
    public static String timeFormat(long timeMillis, String pattern)
    {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    /**
     * 返回文件的最后修改时间
     *
     * @param path
     * @return
     */
    public static String formatPhotoDate(String path)
    {
        String fileTimeString;
        File file = new File(path);
        if (file.exists())
        {
            long time = file.lastModified();
            fileTimeString = formatPhotoDate(time);
        } else
        {
            fileTimeString = "1970-01-01";
        }
        return fileTimeString;
    }

    /**
     * 年月日
     *
     * @return
     */
    public static String getTodayTimeString()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(new Date().getTime());
        return dateString;
    }

    /**
     * 获取今天0点的时间戳
     *
     * @return
     */
    public static long getTodayTimestamp()
    {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String date = sdf.format(new Date());
        return dateFormatTimestamp(date, pattern);
    }

    /**
     * 日期转时间戳
     *
     * @param time
     * @param pattern
     * @return
     */
    public static long dateFormatTimestamp(String time, String pattern)
    {
        pattern = "yyyy-MM-dd";
        SimpleDateFormat sf = new SimpleDateFormat(pattern);
        Date date = new Date();
        try
        {
            date = sf.parse(time);
        } catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }
}
