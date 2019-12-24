package com.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.entity.SDTripEntity;
import com.receiver.AlarmReceiver;
import com.receiver.TripMonthAlarmReceiver;


/**
 * @author xiaoli
 */
public class AlarmUtils {
    /**
     * 不重复提醒
     * @param triggerAtMillis 开始提醒时间
     * @param title dialog显示的标题
     * @param content dialog显示的内容
     * @param id 区分闹钟
     * @param className 字节全路径 也作为action区别intent
     */
    public static void createAlerm(Context context, long triggerAtMillis, String title, String content, long id, String className){
        PendingIntent sender=getPendingIntent(context, title, content,id, className);
        AlarmManager am=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
    }

    /**
     * 重复提醒
     * @param intervalMillis 间隔时间
     * @param triggerAtMillis 开始提醒时间
     * @param title dialog显示的标题
     * @param content dialog显示的内容
     * @param id 区分闹钟
     * @param className 字节全路径 也作为action区别intent
     */
    public static void createAlerm(Context context, long triggerAtMillis, long intervalMillis,
                                   String title, String content, long id, String className){
        PendingIntent sender=getPendingIntent(context,title,content,id,className);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtMillis, intervalMillis, sender);
    }

    /**
     * 每月提醒,需要特殊处理
     */
    public static void createAlerm(Context context, long triggerAtMillis, SDTripEntity entity, String title, String className){
        Intent intent =new Intent(context, TripMonthAlarmReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("entity", entity);
        intent.putExtra("className", className);
        intent.setAction(className);
        PendingIntent sender= PendingIntent.getBroadcast(context, (int) entity.getCid(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, sender);
    }
    /**
     * 取消每月提醒
     */
    public static void cancelMonthAlerm(Context context, long id, String action){
        Intent intent =new Intent(context, TripMonthAlarmReceiver.class);
        intent.setAction(action);
        PendingIntent sender= PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
    /**
     * 取消提醒
     */
    public static void cancelAlerm(Context context, long id, String action){
        Intent intent =new Intent(context, AlarmReceiver.class);
        intent.setAction(action);
        PendingIntent sender= PendingIntent.getBroadcast(context, (int) id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager)context.getSystemService(context.ALARM_SERVICE);
        alarm.cancel(sender);
    }
    private static PendingIntent getPendingIntent(Context context, String title, String content, long id, String className){
        Intent intent =new Intent(context, AlarmReceiver.class);
        intent.setAction(className);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        intent.putExtra("id", id);
        intent.putExtra("className", className);
        PendingIntent sender= PendingIntent.getBroadcast(context, (int)id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return sender;
    }
}
