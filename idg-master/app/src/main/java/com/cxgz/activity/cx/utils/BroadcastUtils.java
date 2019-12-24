package com.cxgz.activity.cx.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.cxgz.activity.cx.server.NotifyService_YJ;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zjh
 */
public class BroadcastUtils
{
    public static String ACTION_PLUSH_MSG = "action_com.superdata_marketing_plush_msg";
    public static String ACTION_NOTIFY_MSG = "action_com.superdata_marketing_notify_msg";
    public static final String PLUSH_SUCCESS = "plush_success";
    public static final String PLUSH_ERROR = "plush_error";
    /**
     * 推送数据状态(是否成功) 0=成功,1=失败
     */
    public static String EXTR_PLUSH_STATUS = "extr_plush_status";
    public static String EXTR_PLUSH_DATA = "extr_plush_data";
    public static String EXTR_PLUSH_TYPE = "extr_plush_type";

    public static void sendBroadcast(Context context, String action, Map<String, Object> datas)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        for (Map.Entry<String, Object> entry : datas.entrySet())
        {
            intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
        }
        context.sendBroadcast(intent);
    }

    public static void registerBroadcast(Context context, String action, BroadcastReceiver receiver)
    {
        IntentFilter intentFilter = new IntentFilter(action);
        context.registerReceiver(receiver, intentFilter);
    }

    public static void unregisterBroadcast(Context context, BroadcastReceiver receiver)
    {
        if (receiver != null)
        {
            context.unregisterReceiver(receiver);
        }
    }

    public static void sendNotifyBroadcast(Context context, NotifyEntity notifyEntity)
    {
        Intent intent = new Intent();
        intent.putExtra(NotifyService_YJ.EXTR_NOTIFY_DATA, notifyEntity);
        intent.setAction(ACTION_NOTIFY_MSG);
        context.sendBroadcast(intent);
    }
}
