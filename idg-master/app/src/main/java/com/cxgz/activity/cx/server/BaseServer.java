package com.cxgz.activity.cx.server;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import com.chaoxiang.base.utils.SDLogUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjh
 */
public abstract class BaseServer extends Service {
    public static Map<Boolean,Object> serverMap = new HashMap<>();
    private final int INTERVAL_TIME = 50 * 1000 * 60;
    private static final String CHECK_SERVER_ACTION = "check_server_action";
    private static boolean isCheck;
    @Override
    public void onCreate() {
        super.onCreate();
        SDLogUtil.debug("server==>oncreate,serverName==>"+this.getClass().getName());
        startCheckServer();
        serverMap.put(isBindServer(),this.getClass());
    }

    /**
     * 检测服务是否被杀,是则从启
     */
    protected void startCheckServer(){
        if(isCheck){
            return;
        }
        SDLogUtil.debug("server==>start Check Server");
        AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent();
        intent.setAction(CHECK_SERVER_ACTION);
        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);
        if(pi != null){
            am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis() + INTERVAL_TIME,INTERVAL_TIME,pi);
            isCheck = true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public abstract boolean isBindServer();
}
