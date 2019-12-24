package com.cxgz.activity.cx.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;

import com.injoy.idg.R;


/**
 * @author zjh
 */
public class NotificationUtils
{
    public static void showNotification(Context context, NotifyEntity notifyEntity)
    {
       /* if(!CommUtils.isApplicationBroughtToBackground(context)){
            if(!notifyEntity.isShowForeground()){
                return;
            }
        }*/

        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        NotificationCompat.Builder compat = new NotificationCompat.Builder(context);
        compat.setSmallIcon(R.mipmap.notice_status_logo)
                .setContentText(notifyEntity.getContent())
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(notifyEntity.getTicker())
                .setAutoCancel(true);
        if (notifyEntity.getTitle() != null && !"".equals(notifyEntity.getTitle()))
        {
            compat.setContentTitle(notifyEntity.getTitle());
        } else
        {
            compat.setContentTitle(context.getString(R.string.app_name));
        }
        if (am.getRingerMode() == AudioManager.RINGER_MODE_SILENT || am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE)
        {
            compat.setDefaults(Notification.DEFAULT_VIBRATE);
        } else
        {
            if (notifyEntity.getSound() != null)
            {
                compat.setSound(notifyEntity.getSound());
            } else
            {
                compat.setDefaults(Notification.DEFAULT_SOUND);
            }
        }
        Intent resultIntent = new Intent(context, notifyEntity.getIntentClass());
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, notifyEntity.getIdentity(), resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        compat.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(notifyEntity.getIdentity(), compat.build());
    }

    /**
     * 清除单条notification
     *
     * @param context
     * @param notifyId
     */
    public static void clearnNotification(Context context, int notifyId)
    {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notifyId);
    }

    /**
     * 清除所有notification
     *
     * @param context
     */
    public static void clearnAllNotification(Context context)
    {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();
    }
}
