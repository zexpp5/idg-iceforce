package com.superdata.im.manager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.PowerManager;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.entity.CxNetworkStatus;
import com.superdata.im.utils.CxCommUtils;
import com.superdata.im.utils.Observable.CxNetWorkObservable;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-02
 * @desc 心跳管理
 */
public class CxHeartBeatManager
{
    private static CxHeartBeatManager cxHeartBeatManager;

    private PendingIntent pendingIntent;
    private Context context;

    public AtomicInteger count = new AtomicInteger(0);
    /**
     * 触发重连的次数
     */
    public AtomicInteger triggerReconnCount = new AtomicInteger(0);

    private final String ACTION_SENDING_HEARTBEAT = "com.superdata.im.manager.CxHeartBeatManager_idg";

    private CxHeartBeatManager(Context context)
    {
        this.context = context;
    }

    public static CxHeartBeatManager getInstance(Context context)
    {
        if (cxHeartBeatManager == null)
        {
            cxHeartBeatManager = new CxHeartBeatManager(context);
        }
        return cxHeartBeatManager;
    }

    /**
     * 启动心跳定时器
     */
    public void startHeartbeatSchedule()
    {
        SDLogUtil.info("send heatbeat start");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_SENDING_HEARTBEAT);
        context.registerReceiver(imReceiver, intentFilter);
        scheduleHeartbeat(Config.HEARTBEAT_INTERVAL);
    }

    private void scheduleHeartbeat(int seconds)
    {
        if (pendingIntent == null)
        {
            Intent intent = new Intent(ACTION_SENDING_HEARTBEAT);
            pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
            if (pendingIntent == null)
            {
                return;
            }
        }

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= 19)
        {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, seconds, pendingIntent);
        } else
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + seconds, seconds, pendingIntent);
        }
    }

    /**
     * --------------------boradcast-广播相关-----------------------------
     */
    private BroadcastReceiver imReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(ACTION_SENDING_HEARTBEAT))
            {
                int tempCount = count.getAndIncrement();
                SDLogUtil.info("send heartbeat count==>" + tempCount);
                if (tempCount >= Config.TRIGGER_HEARTBEAT_COUNT)
                {
                    SDLogUtil.info("send heartbeat package");
                    if (CxCommUtils.isNetWorkConnected(context))
                    {
                        SDLogUtil.info("heartbeat network connect");
                    } else
                    {
                        SDLogUtil.info("heartbeat network disconnect");
                    }
                    sendHeartBeatPacket();
                }
            }
        }
    };

    /**
     * 发送心跳包
     */
    private void sendHeartBeatPacket()
    {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        final PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "cx_heartBeat_wakelock");
        CxSocketManager.getInstance().sendHeatbeatMsg();
        Timer timer = new Timer();
        timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                wl.acquire();
                int tempTriggerReconnCount;
                if (count.get() == 0)
                {
                    return;
                } else
                {
                    tempTriggerReconnCount = triggerReconnCount.getAndIncrement();
                }
                if (tempTriggerReconnCount >= Config.TRIGGER_RECONN_COUNT)
                {  //此处应设计有问题,为达到心跳超时即时重连,心跳失败一次就发起重连(应尝试多次失败再发起重连)
                    SDLogUtil.info("send reconn request");
                    if (CxCommUtils.isNetWorkConnected(context))
                    {
                        SDLogUtil.info("network_true");
                        CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION_SERVER);
                    } else
                    {
                        SDLogUtil.info("network_false");
                        CxNetWorkObservable.getInstance().notifyUpdate(CxNetworkStatus.DISCONNECTION);
                    }

                }
                wl.release();
            }
        }, Config.SEND_TIME_OUT * 1000);
    }


    /**
     * 取消心跳定时器
     */
    public void cancelHeartSchedule()
    {
        SDLogUtil.info("cancelHeartSchedule");
        if (pendingIntent == null)
        {
            return;
        }
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }

}
