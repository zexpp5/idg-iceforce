package com.superdata.im.manager;

import android.content.Context;

import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.processor.CxAnswerProcessor;
import com.superdata.im.processor.CxGroupChatProcessor;
import com.superdata.im.processor.CxIProcessor;
import com.superdata.im.processor.CxLoginProcessor;
import com.superdata.im.processor.CxOfflinePushProcessor;
import com.superdata.im.processor.CxPushProcessor;
import com.superdata.im.processor.CxReadProcessor;
import com.superdata.im.processor.CxSingleChatProcessor;
import com.superdata.im.processor.CxWebrtcProcessor;

/**
 * @version v1.0.0
 * @date 2016-01-13
 * @desc
 */
public class CxProcessorManager
{
    private CxProcessorManager()
    {

    }

    private static CxProcessorManager manager;

    /**
     * 推送处理器
     */
    public CxPushProcessor pushProccessor;
    /**
     * 登陆处理器
     */
    public CxLoginProcessor loginProcessor;
    /**
     * 应答处理器
     */
    public CxAnswerProcessor cxAnswerProcessor;
    /**
     * 单聊处理器
     */
    public CxSingleChatProcessor singleChatProcessor;
    /**
     * 群聊处理器
     */
    public CxGroupChatProcessor groupChatProcessor;

    public CxOfflinePushProcessor offlinePushProcessor;

    public CxWebrtcProcessor webrtcProcessor;

    public CxReadProcessor cxReadProcessor;

    public static CxProcessorManager getInstance()
    {
        if (manager == null)
        {
            manager = new CxProcessorManager();
        }
        return manager;
    }

    /**
     * 初始化处理器
     */
    public void initProcessor(Context context)
    {
        loginProcessor = new CxLoginProcessor(context);
        pushProccessor = new CxPushProcessor(context);
        cxAnswerProcessor = new CxAnswerProcessor(context);
        singleChatProcessor = new CxSingleChatProcessor(context);
        groupChatProcessor = new CxGroupChatProcessor(context);
        offlinePushProcessor = new CxOfflinePushProcessor(context);
        webrtcProcessor = new CxWebrtcProcessor(context);
        cxReadProcessor = new CxReadProcessor(context);

        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.LOGIN, loginProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.PUSH, pushProccessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.ANSWER, cxAnswerProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.SINGLE_CHAT, singleChatProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.GROUP_CHAT, groupChatProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.OFFLINE_PUSH, offlinePushProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.SINGLE_VIDEO_OR_AUDIO_CHAT, webrtcProcessor);
        CxDispatcherManager.getInstance().registerManager(CxIMMessageType.READ_STATUS, cxReadProcessor);

    }

    /**
     * 注册处理器
     *
     * @param messageType
     * @param processor
     */
    public void registerProcessor(CxIMMessageType messageType, CxIProcessor processor)
    {
        CxDispatcherManager.getInstance().registerManager(messageType, processor);
    }

}
