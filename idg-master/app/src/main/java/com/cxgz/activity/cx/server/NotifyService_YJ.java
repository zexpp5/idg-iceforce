package com.cxgz.activity.cx.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cx.utils.NotifyEntity;
import com.cxgz.activity.cx.handle.DoMsgHandle;
import com.cxgz.activity.cx.utils.BroadcastUtils;
import com.cxgz.activity.cx.utils.NotificationUtils;
import com.cxgz.activity.cx.utils.ServerUtils;

/**
 * @author zjh
 * @desc 消息通知服务
 */
public class NotifyService_YJ extends BaseServer
{
    public final static String EXTR_NOTIFY_DATA = "extr_notify_data_yj";
    public static Handler doMsgHandle = new DoMsgHandle();

    BroadcastReceiver doNotifyReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            NotifyEntity notifyEntity = (NotifyEntity) intent.getSerializableExtra(EXTR_NOTIFY_DATA);
            NotificationUtils.showNotification(context, notifyEntity);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        BroadcastUtils.registerBroadcast(this, BroadcastUtils.ACTION_NOTIFY_MSG, doNotifyReceiver);
    }

    @Override
    public boolean isBindServer()
    {
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        BroadcastUtils.unregisterBroadcast(this, doNotifyReceiver);
        super.onDestroy();
        ServerUtils.startServer(this, getClass());
        SDLogUtil.debug("NotifyService_YJ==onDestroy");
    }
}
