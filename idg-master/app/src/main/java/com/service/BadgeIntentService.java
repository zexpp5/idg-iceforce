package com.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.chaoxiang.base.utils.SDGson;


import me.leolin.shortcutbadger.ShortcutBadger;

import static com.chaoxiang.base.config.Config.channelId;
import static com.chaoxiang.base.config.Config.channelName;
import static com.chaoxiang.base.config.Config.isShowBadger;
import static com.chaoxiang.base.config.Config.isShowLight;
import static com.chaoxiang.base.config.Config.lightColor;
import static com.umeng.socialize.utils.DeviceConfig.context;

public class BadgeIntentService extends IntentService
{
    private int notificationId = 9999999;

    public BadgeIntentService()
    {
        super("BadgeIntentService");
    }

    private NotificationManager mNotificationManager;

    @Override
    public void onCreate()
    {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            int badgeCount = intent.getIntExtra("badgeCount", 0);

            String notificationString = intent.getStringExtra("Notification");

//            Notification.Builder notifcationBuilder =null;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            {   //8.0+
//                notifcationBuilder = new Notification.Builder(context, getChannelId(mNotificationManager));
//            } else
//            {
//                notifcationBuilder = new Notification.Builder(context);
//            }
//
//            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 1, resultIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
//
//            notifcationBuilder
//                    .setFullScreenIntent(resultPendingIntent, true)
//                    .setContentTitle(cxNotifyEntity.getTitle())
//                    .setContentText(cxNotifyEntity.getContent())
//                    .setSmallIcon(cxNotifyEntity.getSmallIconResId())
//                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), cxNotifyEntity.getLargeIconResId()))
//                    .setTicker(cxNotifyEntity.getContent())
//                    .setContentIntent(resultPendingIntent)
//                    .setAutoCancel(true);
//
//            mNotificationManager.cancel(notificationId);
//            notificationId--;
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//            {
//                getChannelId(mNotificationManager);
////                notification.setChannelId(NOTIFICATION_CHANNEL);
//            }

//            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi"))
//            {
//                ShortcutBadger.applyNotification(getApplicationContext(), notifcationBuilder.build(), badgeCount);
//            } else
//            {
//                ShortcutBadger.applyCount(getApplicationContext(), badgeCount);
//            }
//            mNotificationManager.notify(notificationId, notifcationBuilder.build());
        }
    }

    //桌面红点，灯光，颜色等
    @TargetApi(Build.VERSION_CODES.O)
    public String getChannelId(NotificationManager mNotificationManager)
    {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(isShowLight);
        channel.setLightColor(lightColor);
        channel.setShowBadge(isShowBadger);
        mNotificationManager.createNotificationChannel(channel);
        return channel.getId();
    }

}
