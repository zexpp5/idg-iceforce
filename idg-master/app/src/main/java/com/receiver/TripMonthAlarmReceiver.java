package com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.entity.SDTripEntity;
import com.lidroid.xutils.util.LogUtils;
import com.utils.AlarmUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import tablayout.view.dialog.AlarmDialog;

/**
 * 行程管理-重复周期为每月提醒接收器
 * @author xiaoli
 */
public class TripMonthAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar c = Calendar.getInstance();
        if (intent != null) {
            String title = intent.getStringExtra("title");
            String className = intent.getStringExtra("className");
            SDTripEntity entity = (SDTripEntity) intent.getSerializableExtra("entity");
            LogUtils.e(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            long alarmTime = 0;
            int i = 0;
            do{
                i++;
                c.setTime(new Date());
                int oldDay = c.get(Calendar.DATE);
                c.add(Calendar.MONTH, i);
                int newDay = c.get(Calendar.DATE);
                if (oldDay == newDay) {
                    alarmTime = c.getTime().getTime();
                    break;
                }
            }while (true);
            AlarmUtils.createAlerm(context, alarmTime, entity,title, className);
            AlarmDialog dialog = new AlarmDialog(context, title, entity.getTitle(),entity.getCid(), className);
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();

        }
    }
}