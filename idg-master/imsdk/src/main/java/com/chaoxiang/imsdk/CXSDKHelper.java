package com.chaoxiang.imsdk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.imsdk.chat.CXCallProcessor;
import com.chaoxiang.imsdk.chat.CXChatManager;
import com.chaoxiang.imsdk.chat.CXKefuManager;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.chaoxiang.imsdk.chat.CxAddFriendManager;
import com.chaoxiang.imsdk.conversation.IMConversaionManager;
import com.chaoxiang.imsdk.group.IMGroupManager;
import com.superdata.im.constants.CxBroadcastAction;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.manager.CxNetworkStatusManager;
import com.superdata.im.manager.CxProcessorManager;
import com.superdata.im.manager.CxServerManager;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.processor.CxBaseProcessor;
import com.superdata.im.receiver.CxCheckServerReceiver;
import com.superdata.im.receiver.TimeTickReceiver;
import com.superdata.im.utils.CxBroadcastUtils;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-13
 * @desc SDK帮助类
 */
public class CXSDKHelper
{
    private static CXSDKHelper instance;
    public boolean isVoiceCalling;
    public boolean isVideoCalling;
//    private MyCallListener callListener;

    private CXSDKHelper()
    {
    }

    public static synchronized CXSDKHelper getInstance()
    {
        if (instance == null)
        {
            instance = new CXSDKHelper();
        }
        return instance;
    }

    public static void init(Application application)
    {
        QueryBuilder.LOG_SQL = Config.DEBUG;
        QueryBuilder.LOG_VALUES = Config.DEBUG;

        String account = (String) SPUtils.get(application, CxSPIMKey.STRING_ACCOUNT, "");
//        初始化数据库
        if (account != null && !account.equals(""))
        {
            if (IMDaoManager.getInstance().getDaoSession() != null)
            {
                IMDaoManager.getInstance().getDaoSession().clear();
                IMConversaionManager.clear();
                CxAddFriendManager.clear();
                CXVoiceGroupManager.clear();
                IMGroupManager.clear();
                CXChatManager.clear();
                CXKefuManager.clear();
            }
            IMDaoManager.getInstance().initDB(application.getApplicationContext(), account);
        }

        initServer(application);
        startCheckReceiver(application);
        CxProcessorManager.getInstance().initProcessor(application);
        //网络观察者
        CxNetworkStatusManager.getInstance().init(application);
        //监听
        registerCallback();
    }

    //注册了个广播
    private static void startCheckReceiver(Context context)
    {
        CxCheckServerReceiver checkServerReceiver = new CxCheckServerReceiver();
        CxBroadcastUtils.registerBroadcast(context, CxBroadcastAction.ACTION_CHECK_SERVER, checkServerReceiver);

        //注册个系统的广播，
        TimeTickReceiver timeTickReceiver = new TimeTickReceiver();
        CxBroadcastUtils.registerBroadcast(context, Intent.ACTION_TIME_TICK, timeTickReceiver);
    }

    private static void initServer(Context context)
    {
        CxServerManager.initContext(context);
        CxServerManager.getInstance().startServer();

        CxSocketManager.getInstance().init(context);
    }

    private static void registerCallback()
    {
        CxProcessorManager.getInstance().pushProccessor.setDealMsgCallback(new CxBaseProcessor.DealMsgCallback()
        {
            @Override
            public void successBefore(CxMessage cxMessage)
            {
                IMGroupManager.dealProcessor(cxMessage);
                CXCallProcessor.getInstance().dealProcessor(cxMessage);
            }

            @Override
            public void successAfter(CxMessage cxMessage)
            {
                SDLogUtil.debug("==successAfter===");
            }

            @Override
            public void errorBefore(CxMessage cxMessage)
            {
                SDLogUtil.debug("==successAfter===");
            }

            @Override
            public void errorAfter(CxMessage cxMessage)
            {
                SDLogUtil.debug("==successAfter===");
            }
        });

        CxProcessorManager.getInstance().offlinePushProcessor.setDealMsgCallback(new CxBaseProcessor.DealMsgCallback()
        {
            @Override
            public void successBefore(CxMessage cxMessage)
            {
                IMGroupManager.dealProcessor(cxMessage);
            }

            @Override
            public void successAfter(CxMessage cxMessage)
            {

            }

            @Override
            public void errorBefore(CxMessage cxMessage)
            {

            }

            @Override
            public void errorAfter(CxMessage cxMessage)
            {

            }
        });
    }

}
