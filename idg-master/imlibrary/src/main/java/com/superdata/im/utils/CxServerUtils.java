package com.superdata.im.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

import com.chaoxiang.base.utils.SDLogUtil;

import java.util.List;

/**
 * @author zjh
 * @date 2016/1/4
 * @desc 服务工具类, 用于启动服务, 绑定服务等
 */
public class CxServerUtils
{
    public static void startBindServer(Context context, ServiceConnection connection, Class clazz)
    {
        if (!isServiceRunning(context, clazz.getName()))
        {
            context.startService(new Intent(context, clazz));
            context.bindService(new Intent(context, clazz), connection, Context.BIND_AUTO_CREATE);
        } else
        {
            context.bindService(new Intent(context, clazz), connection, Context.BIND_AUTO_CREATE);
        }
    }

//    public static void bindServer(Context context,ServiceConnection connection,Class clazz){
//        if(!isServiceRunning(context,clazz.getName())){
//            context.bindService(new Intent(context, clazz), connection, Context.BIND_AUTO_CREATE);
//        }
//    }

    public static void startServer(Context context, Class clazz)
    {
        if (!isServiceRunning(context, clazz.getName()))
        {
            context.startService(new Intent(context, clazz));
        }
    }

    public static void unBindServer(Context context, ServiceConnection connection)
    {
        if (connection != null)
        {
            context.unbindService(connection);
        }
    }


    /**
     * 用来判断服务是否运行.
     *
     * @param mContext
     * @param className 判断的服务名字
     * @return true 在运行 false 不在运行
     */
    public static boolean isServiceRunning(Context mContext, String className)
    {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)
                mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList
                = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (!(serviceList.size() > 0))
        {
            return false;
        }
        for (int i = 0; i < serviceList.size(); i++)
        {
            if (serviceList.get(i).service.getClassName().equals(className))
            {
                isRunning = true;
                break;
            }
        }
        SDLogUtil.debug("isRunning===" + isRunning + ",className==" + className);
        return isRunning;
    }

}
