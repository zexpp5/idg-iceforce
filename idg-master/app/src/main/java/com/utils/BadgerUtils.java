package com.utils;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.chaoxiang.base.utils.SDGson;
import com.cxgz.activity.cxim.bean.SDGroup;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.injoy.idg.R;
import com.service.BadgeIntentService;
import com.superdata.im.entity.CxNotifyEntity;
import com.superdata.im.utils.CxNotificationUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import me.leolin.shortcutbadger.ShortcutBadger;

import static com.base.BaseApplication.applicationContext;

/**
 * Created by selson
 */
public class BadgerUtils
{
    /**
     * 内部类实现单例模式
     * 延迟加载，减少内存开销
     */
    private static class BadgerHelper
    {
        private static BadgerUtils instance = new BadgerUtils();
    }

    /**
     * 私有的构造函数
     */
    private BadgerUtils()
    {

    }

    public static BadgerUtils getInstance()
    {
        return BadgerHelper.instance;
    }

    public void setNotificationBadger(Context context, String title, String content, int unReadCount)
    {
        if (unReadCount > 0)
        {
            CxNotificationUtils.getInstance(context).showNotification(new CxNotifyEntity(0, title, content, null,
                    SuperMainActivity.class,
                    content, R.mipmap.ic_launcher, R.mipmap.ic_launcher, true), new CxNotificationUtils
                    .onCallBackNotification()
            {
                @Override
                public void reTurnNotifi(Notification notification)
                {
//                    int count = UnReadUtils.getInstance().findUnReadApproveCount();
//                    if (count > 0)
//                        setBadger(applicationContext, notification, count);
                }
            });
        }
    }

    //1，正常notification(适配8.0)
    //2,需要设置红点
    //3,适配一些不能单纯用applyCount的机型
    public void setBadger(Context context, Notification notification, int count)
    {
        if (count > 0)
        {
            if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi"))
            {
                //小米的适配有问题。
                ShortcutBadger.applyNotification(context, notification, count);
//                context.startService(new Intent(context, BadgeIntentService.class)
//                        .putExtra("badgeCount", count).putExtra("Notification", SDGson.toJson(notification)));

            } else
            {
//                if (Build.MANUFACTURER.equalsIgnoreCase("samsung") && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//                {
//                    context.startService(new Intent(context, BadgeIntentService.class).putExtra("badgeCount", count));
//                }
                //目前其他机型是这个,具体自己找写高级鸡测测吧
                ShortcutBadger.applyCount(context, count);
            }
        }
    }

    public void setBadgerApplyCount(Context context, int count)
    {
        ShortcutBadger.applyCount(context, count);
    }

    public void setRemoveCount(Context context)
    {
        ShortcutBadger.removeCount(context);
    }
}
