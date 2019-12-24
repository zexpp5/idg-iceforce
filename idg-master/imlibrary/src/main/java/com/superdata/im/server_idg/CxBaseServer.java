package com.superdata.im.server_idg;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.constants.CxBroadcastAction;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2016/1/4
 * @desc
 */
public abstract class CxBaseServer extends Service
{
    public static Map<Boolean, Object> serverMap = new HashMap<>();
    //5分钟检查一次
    private final int INTERVAL_TIME = 3 * 60 * 1000;
    /**
     * 是否已经开始检测服务
     */
    private static boolean isCheck;

    @Override
    public void onCreate()
    {
        super.onCreate();
        startCheckServer();
        serverMap.put(isBindServer(), this.getClass());
    }

    /**
     * 检测服务是否被杀,是则重启
     */
    protected void startCheckServer()
    {
        if (isCheck)
        {
            return;
        }
        SDLogUtil.debug("server==>start Check Server");
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(CxBroadcastAction.ACTION_CHECK_SERVER);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
        if (pi != null)
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + INTERVAL_TIME, INTERVAL_TIME, pi);
            isCheck = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    public abstract boolean isBindServer();
}
