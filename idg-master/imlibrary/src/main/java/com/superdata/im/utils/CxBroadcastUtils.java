package com.superdata.im.utils;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.Serializable;
import java.util.Map;

/**
 * @author zjh
 * @date 2016/1/4
 * @desc 广播工具类
 */
public class CxBroadcastUtils
{
    public static final String PLUSH_SUCCESS = "plush_success_idg";
    public static final String PLUSH_ERROR = "plush_error_idg";

    /**
     * 推送数据状态(是否成功) 0=成功,1=失败
     */

    public static String EXTR_PLUSH_STATUS = "extr_plush_status_idg";
    public static String EXTR_PLUSH_DATA = "extr_plush_data_idg";
    public static String EXTR_PLUSH_TYPE = "extr_plush_type_idg";

    public static void sendBroadcast(Context context, String action, Map<String, Object> datas)
    {
        Intent intent = new Intent();
        intent.setAction(action);
        if (datas != null)
        {
            for (Map.Entry<String, Object> entry : datas.entrySet())
            {
                intent.putExtra(entry.getKey(), (Serializable) entry.getValue());
            }
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

}
