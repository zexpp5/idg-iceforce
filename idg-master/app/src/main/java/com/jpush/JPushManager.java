package com.jpush;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import com.chaoxiang.base.utils.SDLogUtil;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.ContentValues.TAG;
import static com.jpush.TagAliasOperatorHelper.ACTION_DELETE;
import static com.jpush.TagAliasOperatorHelper.ACTION_SET;
import static com.jpush.TagAliasOperatorHelper.sequence;
import static com.umeng.socialize.utils.DeviceConfig.context;

/**
 * Created by selson on 2018/11/2.
 */
public class JPushManager
{
    private static Context mContext;

    private JPushManager()
    {
    }

    public static synchronized JPushManager getInstance(Context context)
    {
        JPushManager.mContext = context;
        return JPushManagerHelper.jPushManager;
    }

    public static class JPushManagerHelper
    {
        private static final JPushManager jPushManager = new JPushManager();
    }

    /**
     * 初始化极光，一般可以放到程序的启动Activity或是Application的onCreate方法中调用
     */
    public void initJPush()
    {
        JPushInterface.setDebugMode(false); // 设置开启日志,发布时请关闭日志
        JPushInterface.init(mContext); // 初始化 JPush
    }

    /**
     * 退出极光，一般是程序退出登录时候，具体还是需要看项目的实际需求
     */
    public void stopJPush()
    {
        JPushInterface.stopPush(mContext);
//        setAliasAndTags("xxxxxxxxxxxxxxxx", null);//通过清空别名来停止极光
    }

    /**
     * 极光推送恢复正常工作
     */
    public void resumeJPush()
    {
        JPushInterface.resumePush(mContext);
    }

    public void setAlias(final String alias)
    {
        if (TextUtils.isEmpty(alias))
        {
            return;
        }
        // 调用 Handler 来异步设置别名
        Set<String> tags = null;
        int action = -1;
        boolean isAliasAction = false;
        isAliasAction = true;
        action = ACTION_SET;
        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        if (isAliasAction)
        {
            tagAliasBean.alias = alias;
        } else
        {
            tagAliasBean.tags = tags;
        }
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(mContext, sequence, tagAliasBean);
    }

    public void DelteAlias(final String alias)
    {
        if (TextUtils.isEmpty(alias))
        {
            return;
        }
        // 调用 Handler 来异步设置别名
        Set<String> tags = null;
        int action = -1;
        boolean isAliasAction = false;

        isAliasAction = true;
        action = ACTION_DELETE;

        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = action;
        sequence++;
        if (isAliasAction)
        {
            tagAliasBean.alias = alias;
        } else
        {
            tagAliasBean.tags = tags;
        }
        tagAliasBean.isAliasAction = isAliasAction;
        TagAliasOperatorHelper.getInstance().handleAction(mContext, sequence, tagAliasBean);
    }


}
