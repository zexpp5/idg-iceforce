package com.superdata.im.utils;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SPUtils;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxNotifyEntity;

import static com.chaoxiang.base.config.Config.channelId;
import static com.chaoxiang.base.config.Config.channelName;
import static com.chaoxiang.base.config.Config.isShowBadger;
import static com.chaoxiang.base.config.Config.isShowLight;
import static com.chaoxiang.base.config.Config.lightColor;

/**
 *
 */
public class CxNotificationUtils
{
    /**
     * 是否开启通知
     */
    private boolean isOpenNotification;
    /**
     * 是否开启通知声音
     */
    private boolean isOpenNotifySound;
    /**
     * 石头开启通知震动
     */
    private boolean isOpenNotifyVibrate;
    private Context context;


    private CxNotificationUtils(Context context)
    {
        this.context = context;
        isOpenNotification = (boolean) SPUtils.get(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION, true);
        isOpenNotifySound = (boolean) SPUtils.get(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION_SOUND, true);
        isOpenNotifyVibrate = (boolean) SPUtils.get(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION_VIBRATE, true);
    }

    public static CxNotificationUtils getInstance(Context context)
    {
        return new CxNotificationUtils(context);
    }

    private int notificationId = 0;

    public interface onCallBackNotification
    {
        void reTurnNotifi(Notification notification);
    }

    public void showNotification(CxNotifyEntity cxNotifyEntity, onCallBackNotification onCallBackNotification)
    {
        if (!CxCommUtils.isApplicationBroughtToBackground(context))
        {
            if (!cxNotifyEntity.isShowForeground())
            {
                return;
            }
        }
        if (!isOpenNotification)
        {
            return;
        }

        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notifcationBuilder = null;
        mNotificationManager.cancel(notificationId);
        notificationId++;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {   //8.0+
            notifcationBuilder = new Notification.Builder(context, getChannelId(mNotificationManager));
        } else
        {
            notifcationBuilder = new Notification.Builder(context);
        }

        Class intentClazz = cxNotifyEntity.getIntentClass();
        Intent resultIntent = null;
        if (intentClazz != null)
        {
            resultIntent = new Intent(context, intentClazz);
        } else
        {
            resultIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        }

        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        notifcationBuilder
                .setFullScreenIntent(resultPendingIntent, true)
                .setContentTitle(cxNotifyEntity.getTitle())
                .setContentText(cxNotifyEntity.getContent())
                .setSmallIcon(cxNotifyEntity.getSmallIconResId())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), cxNotifyEntity.getLargeIconResId()))
                .setTicker(cxNotifyEntity.getContent())
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true);

        if (cxNotifyEntity.getTitle() != null && !"".equals(cxNotifyEntity.getTitle()))
        {
            notifcationBuilder.setContentTitle(cxNotifyEntity.getTitle());
        }
        if (isOpenNotifyVibrate)
        {
            notifcationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        }
        if (isOpenNotifySound)
        {
            if (cxNotifyEntity.getSound() != null)
            {
                notifcationBuilder.setSound(cxNotifyEntity.getSound());
            } else
            {
                notifcationBuilder.setDefaults(Notification.DEFAULT_SOUND);
            }
        }

//        mNotificationManager.notify(cxNotifyEntity.getIdentity(), notifcationBuilder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
//            notifcationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
//            notifcationBuilder.setPublicVersion(notifcationBuilder.build());
        }
        mNotificationManager.notify(notificationId, notifcationBuilder.build());

        if (onCallBackNotification != null)
        {
            onCallBackNotification.reTurnNotifi(notifcationBuilder.build());
        }
    }

    //桌面红点，灯光，颜色等
    @TargetApi(Build.VERSION_CODES.O)
    public String getChannelId(NotificationManager mNotificationManager)
    {
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = mNotificationManager.getNotificationChannel(channelId);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE)
            {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                context.startActivity(intent);
                MyToast.showToast(context, "IDG:请手动将通知打开");
            }
        }
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.enableLights(isShowLight);
        channel.setLightColor(lightColor);
        channel.setShowBadge(isShowBadger);
        mNotificationManager.createNotificationChannel(channel);
        return channel.getId();
    }

    /**
     * 清除所有Notification
     *
     * @param context
     */

    public static void cleanAllNotification(Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    /**
     * 清除指定id的notification
     *
     * @param context
     * @param id
     */
    public static void cleanNotificationById(Context context, int id)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

    public boolean isOpenNotification()
    {
        return isOpenNotification;
    }

    public boolean isOpenNotifySound()
    {
        return isOpenNotifySound;
    }

    public boolean isOpenNotifyVibrate()
    {
        return isOpenNotifyVibrate;
    }

    public void setIsOpenNotification(boolean isOpenNotification)
    {
        this.isOpenNotification = isOpenNotification;
        SPUtils.put(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION, isOpenNotification);
    }

    public void setIsOpenNotifySound(boolean isOpenNotifySound)
    {
        this.isOpenNotifySound = isOpenNotifySound;
        SPUtils.put(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION_SOUND, isOpenNotifySound);
    }

    public void setIsOpenNotifyVibrate(boolean isOpenNotifyVibrate)
    {
        this.isOpenNotifyVibrate = isOpenNotifyVibrate;
        SPUtils.put(context, CxSPIMKey.BOOLEAN_OPEN_NOTIFICATION_VIBRATE, isOpenNotifyVibrate);
    }
}
