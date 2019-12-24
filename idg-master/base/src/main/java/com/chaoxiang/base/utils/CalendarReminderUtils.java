package com.chaoxiang.base.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by selson on 2018/9/12.
 */
public class CalendarReminderUtils
{
    private static String CALENDER_URL = "content://com.android.calendar/calendars";
    private static String CALENDER_EVENT_URL = "content://com.android.calendar/events";
    private static String CALENDER_REMINDER_URL = "content://com.android.calendar/reminders";

    private static String CALENDARS_NAME = "idg";
    private static String CALENDARS_ACCOUNT_NAME = "idg@idg.com";
    private static String CALENDARS_ACCOUNT_TYPE = "com.injoy.idg";
    private static String CALENDARS_DISPLAY_NAME = "IDG账户";

    /**
     * 检查是否已经添加了日历账户，如果没有添加先添加一个日历账户再查询
     * 获取账户成功返回账户id，否则返回-1
     */
    private static int checkAndAddCalendarAccount(Context context)
    {
        int oldId = checkCalendarAccount(context);
        if (oldId >= 0)
        {
            return oldId;
        } else
        {
            long addId = addCalendarAccount(context);
            if (addId >= 0)
            {
                return checkCalendarAccount(context);
            } else
            {
                return -1;
            }
        }
    }

    /**
     * 检查是否存在现有账户，存在则返回账户id，否则返回-1
     */
    private static int checkCalendarAccount(Context context)
    {
        Cursor userCursor = context.getContentResolver().query(Uri.parse(CALENDER_URL), null, null, null, null);
        try
        {
            if (userCursor == null)
            { //查询返回空值
                return -1;
            }
            int count = userCursor.getCount();
            if (count > 0)
            { //存在现有账户，取第一个账户的id返回
                userCursor.moveToFirst();
                return userCursor.getInt(userCursor.getColumnIndex(CalendarContract.Calendars._ID));
            } else
            {
                return -1;
            }
        } finally
        {
            if (userCursor != null)
            {
                userCursor.close();
            }
        }
    }

    /**
     * 添加日历账户，账户创建成功则返回账户id，否则返回-1
     */
    private static long addCalendarAccount(Context context)
    {
        TimeZone timeZone = TimeZone.getDefault();
        ContentValues value = new ContentValues();
        value.put(CalendarContract.Calendars.NAME, CALENDARS_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE);
        value.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDARS_DISPLAY_NAME);
        value.put(CalendarContract.Calendars.VISIBLE, 1);
        value.put(CalendarContract.Calendars.CALENDAR_COLOR, Color.BLUE);
        value.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        value.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        value.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZone.getID());
        value.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDARS_ACCOUNT_NAME);
        value.put(CalendarContract.Calendars.CAN_ORGANIZER_RESPOND, 0);

        Uri calendarUri = Uri.parse(CALENDER_URL);
        calendarUri = calendarUri.buildUpon()
                .appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true")
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDARS_ACCOUNT_NAME)
                .appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CALENDARS_ACCOUNT_TYPE)
                .build();

        Uri result = context.getContentResolver().insert(calendarUri, value);
        long id = result == null ? -1 : ContentUris.parseId(result);
        return id;
    }

    /**
     * 添加日历事件
     */
    public static void addCalendarEvent(Context context, String eid, String title, String description, long startTime, long
            endTime, int previousDate)
    {
        if (context == null)
        {
            return;
        }
        int calId = checkAndAddCalendarAccount(context); //获取日历账户的id
        if (calId < 0)
        { //获取账户id失败直接返回，添加日历事件失败
            return;
        }

        //更新操作
        if (updateCalendar(context, eid, title, description, startTime, endTime))
        {
            return;
        }

        //添加日历事件
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(startTime);//设置开始时间
        long start = mCalendar.getTime().getTime();
//        mCalendar.setTimeInMillis(start + 10 * 60 * 1000);
        mCalendar.setTimeInMillis(endTime);
        long end = mCalendar.getTime().getTime();
        ContentValues event = new ContentValues();
        event.put(CalendarContract.Events.CALENDAR_ID, calId);
        event.put(CalendarContract.Events.TITLE, title);
        event.put(CalendarContract.Events.DESCRIPTION, description);
        event.put(CalendarContract.Events._ID, eid);
        event.put(CalendarContract.Events.DTSTART, start);
        event.put(CalendarContract.Events.DTEND, end);
        event.put(CalendarContract.Events.HAS_ALARM, 1);//设置有闹钟提醒
        event.put(CalendarContract.Events.EVENT_TIMEZONE, "Asia/Shanghai");//这个是时区，必须有
        Uri newEvent = context.getContentResolver().insert(Uri.parse(CALENDER_EVENT_URL), event); //添加事件
        if (newEvent == null)
        { //添加日历事件失败直接返回
            MyToast.showToast(context, "添加失败");
            return;
        }
        //事件提醒的设定
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Reminders.EVENT_ID, ContentUris.parseId(newEvent));
        values.put(CalendarContract.Reminders.MINUTES, previousDate * 1 * 30);// 提前previousDate天有提醒
        values.put(CalendarContract.Reminders.METHOD, CalendarContract.Reminders.METHOD_ALERT);
        Uri uri = context.getContentResolver().insert(Uri.parse(CALENDER_REMINDER_URL), values);
        if (uri == null)
        { //添加事件提醒失败直接返回
//            MyToast.showToast(context, "添加提醒失败");
            return;
        }
        MyToast.showToast(context, "同步日历成功");
    }

    /**
     * 删除日历事件
     */
    public static void deleteCalendarEvent(Context context, String title)
    {
        if (context == null)
        {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try
        {
            if (eventCursor == null)
            { //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0)
            {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext())
                {
                    String eventTitle = eventCursor.getString(eventCursor.getColumnIndex("title"));
                    if (!TextUtils.isEmpty(title) && title.equals(eventTitle))
                    {
                        int id = eventCursor.getInt(eventCursor.getColumnIndex(CalendarContract.Calendars._ID));//取得id
                        Uri deleteUri = ContentUris.withAppendedId(Uri.parse(CALENDER_EVENT_URL), id);
                        int rows = context.getContentResolver().delete(deleteUri, null, null);
                        if (rows == -1)
                        { //事件删除失败
                            return;
                        }
                    }
                }
            }
        } finally
        {
            if (eventCursor != null)
            {
                eventCursor.close();
            }
        }
    }

    /**
     * 删除日历事件
     */
    public static void findEventEid(Context context, String eid)
    {
        if (context == null)
        {
            return;
        }
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        try
        {
            if (eventCursor == null)
            { //查询返回空值
                return;
            }
            if (eventCursor.getCount() > 0)
            {
                //遍历所有事件，找到title跟需要查询的title一样的项
                for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext())
                {
                    String tmpEid = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events._ID));
                    if (!TextUtils.isEmpty(eid) && eid.equals(tmpEid))
                    {
                        MyToast.showToast(context, "查询到相同数据！");
                        //事件的ID
                        String id = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events._ID)); //不同于Events
                        //事件的标题
                        String title = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.TITLE));
                        //事件的描述
                        String description = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events
                                .DESCRIPTION));
                        //事件的起始时间
                        String dtstart = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DTSTART));
                        //事件的结束时间 ，如果事件是每天/周,那么就没有结束时间
                        String dtend = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events.DTEND));
                    }
                }
            }
        } finally
        {
            if (eventCursor != null)
            {
                eventCursor.close();
            }
        }
    }

    public static boolean updateCalendar(Context context, String eid, String title, String description, long startTime, long
            endTime)
    {
        boolean isHava = false;
        Cursor eventCursor = context.getContentResolver().query(Uri.parse(CALENDER_EVENT_URL), null, null, null, null);
        if (eventCursor.getCount() > 0)
        {
            for (eventCursor.moveToFirst(); !eventCursor.isAfterLast(); eventCursor.moveToNext())
            {
                String tmpEid = eventCursor.getString(eventCursor.getColumnIndex(CalendarContract.Events._ID));
                if (!TextUtils.isEmpty(eid) && eid.equals(tmpEid))
                {
                    long eventID = Long.parseLong(eid); //这个id一定是要表中_id的值，不能是calendar_id的值
                    ContentResolver cr = context.getContentResolver();
                    ContentValues values = new ContentValues();
                    Uri updateUri = null;
                    values.put(CalendarContract.Events.TITLE, title);
                    values.put(CalendarContract.Events.DESCRIPTION, description);
                    values.put(CalendarContract.Events.DTSTART, startTime);
                    values.put(CalendarContract.Events.DTEND, endTime);
                    updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                    int rows = cr.update(updateUri, values, null, null);
                    if (rows > 0)
                    {
//                        MyToast.showToast(context, "修改成功");
                        isHava = true;
                    } else
                    {
//                        MyToast.showToast(context, "修改失败");
                    }
                }
            }
        }

        return isHava;
    }
}
