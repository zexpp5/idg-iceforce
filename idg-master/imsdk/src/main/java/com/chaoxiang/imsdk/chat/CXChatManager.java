package com.chaoxiang.imsdk.chat;

import android.content.Context;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.dao.IMMessageDao;
import com.im.client.util.UUID;
import com.superdata.im.callback.CxReadMessageCallBack;
import com.superdata.im.callback.CxReceiverMsgCallback;
import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.manager.CxProcessorManager;
import com.superdata.im.manager.CxSocketManager;
import com.superdata.im.utils.CXMessageUtils;
import com.superdata.im.utils.Observable.CxGroupChatSubscribe;
import com.superdata.im.utils.Observable.CxIMObservable;
import com.superdata.im.utils.Observable.CxMsgStatusChangeSubscribe;
import com.superdata.im.utils.Observable.CxSingleChatSubscribe;

import java.util.List;
import java.util.Map;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-15
 * @desc
 */
public class CXChatManager
{

    private static CXChatManager instance;

    private CXChatManager()
    {

    }

    public static synchronized CXChatManager getInstance()
    {
        if (instance == null )
        {
            instance = new CXChatManager();
        }
        return instance;
    }

    public static void clear()
    {

    }

    /**
     * 通过messageId查询一条消息
     * @param messageId
     * @return
     */
    public static CxMessage loadChatMsgByMsgId(String messageId)
    {
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId(messageId));
    }

    public IMMessage findChatMsgById(String messageId)
    {
        return IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId(messageId);
    }


    /**
     * 获取群会话消息
     *
     * @param groupId
     * @param limit
     * @return
     */
    public static List<CxMessage> loadGroupConversationMsg(String groupId, int limit)
    {
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadGroupConversationMsg(groupId, limit));
    }

    /**
     * 获取群会话消息
     *
     * @param groupId
     * @return
     */
    public static List<CxMessage> loadGroupConversationMsg(String groupId)
    {
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadIMMessageListFromGroupId(groupId));
    }


    /**
     * 加载更多群会话消息
     *
     * @param groupId
     * @param limit
     * @return
     */
    public static List<CxMessage> loadMoreGroupConversationMsg(String groupId, long id, int limit)
    {
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMoreConversationMsg(groupId, id, limit));
    }

    /**
     * 获取单聊会话消息
     *
     * @param context
     * @param to
     * @param limit
     * @return
     */
    public static List<CxMessage> loadSingleChatConversationMsg(Context context, String to, int limit)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadConversationMsg(from, to, limit));
    }

    /**
     * 获取单聊会话消息(所有未读的。)
     *
     * @param context
     * @param to
     * @param
     * @return
     */
    public static List<CxMessage> loadSingleChatMsgUnread(Context context, String to)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMessageUnReadMsg(from, to));
    }

    /**
     * 获取单聊会话消息
     *
     * @param context
     * @param to
     * @param limit
     * @return
     */
    public static List<CxMessage> loadMoreSingleChatConversationMsg(Context context, String to, long id, int limit)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMoreConversationMsg(from, to, id, limit));
    }

    /**
     * 获取单聊会话消息的条数
     *
     * @param context
     * @param to
     * @return
     */
    public static long loadSingleChatMsgCount(Context context, String to, long id)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        long count = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadSingleChatMsgCount(from, to, id);
        return count;
    }

    public static long loadGroupChatMsgCount(Context context, String groupId)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        long count = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadGroupChatMsgCount(groupId);
        return count;
    }

    /**
     * 分页获取单聊会话消息
     * @param context
     * @param to
     * @param limit
     * @return
     */
    public List<CxMessage> loadSingleChatPageMsg(Context context, String to, long id, int limit, int page)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadSingleChatPageMsg(from, to, id, limit, page));
    }

    public List<CxMessage> loadSingleChatPageMsg2(Context context, String to, int limit, int page)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadSingleChatPageMsg2(from, to,limit, page));
    }

    /**
     * 分页获取群聊会话消息
     * @param context
     * @param limit
     * @return
     */
    public static List<CxMessage> loadGroupChatPageMsg(Context context, String groupId, long id, int limit, int page)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadGroupChatPageMsg(groupId, limit, page));
    }

    public List<CxMessage> loadGroupChatPageMsg2(Context context, String groupId,  int limit, int page)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        return CXMessageUtils.covertIMMessage(IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadGroupChatPageMsg2(groupId, limit, page));
    }

    /**
     * 删除单聊会话所有消息
     *
     * @param context
     * @param to
     */
    public static void removeSingleChatConversationMsg(Context context, String to)
    {
        String from = (String) SPUtils.get(context, CxSPIMKey.STRING_ACCOUNT, "");
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().removeSingleChatConversationMsg(from, to);

    }

    public static void delteMsgByMsgId(CxMessage cxMessage)
    {
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().deleteMsgByMsgId(cxMessage.getImMessage().getHeader().getMessageId());
    }

    public static void delteMsgByMsgIdNoSql(CxMessage cxMessage)
    {
        if (cxMessage != null)
        {
            IMMessage imMsg = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().loadMsgByMessageId(cxMessage.getImMessage().getHeader().getMessageId());
            if (imMsg != null)
            {//先判断是否存在，如果存在这条记录就删除
                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().deleteMsgByMsgIdNoSql(cxMessage.getImMessage().getHeader().getMessageId());
            }
        }
    }

    public static void delteMsgByMsgIdNoSql(String msgId)
    {
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().deleteMsgByMsgIdNoSql(msgId);
    }

    /**
     * 删除群聊会话所有消息
     *
     * @param groupId
     */
    public static void removeGroupChatConversationMsg(String groupId)
    {
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().removeGroupConversationMsg(groupId);
    }

    /**
     * 更新消息
     *
     * @param cxMessage
     */
    public static void updateMsg(CxMessage cxMessage)
    {
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().updateMsgByMsgId(CXMessageUtils.convertCXMessage(cxMessage));
    }

    /**
     * 群主订阅者
     */
    private static CxGroupChatSubscribe groupChatSubscribe;

    /**
     * 状态改变订阅者
     */
    private static CxMsgStatusChangeSubscribe msgStatusChangeSubscribe;

    /**
     * 单聊观察者
     */
    private static CxSingleChatSubscribe singleChatSubscribe;

    /**
     * 注册群聊监听
     *
     * @param receiverMsgCallback     接收到群聊消息监听
     * @param msgStatusChangeCallback 发送消息状态发生改变监听
     */
    public static void registerGroupListener(CxUpdateUICallback receiverMsgCallback, CxMsgStatusChangeSubscribe.MsgStatusChangeCallback msgStatusChangeCallback)
    {
        unregisterGroupListener();

        groupChatSubscribe = new CxGroupChatSubscribe(receiverMsgCallback);
        msgStatusChangeSubscribe = new CxMsgStatusChangeSubscribe(msgStatusChangeCallback);
        //
        CxIMObservable.getInstance().addObserver(groupChatSubscribe);
        CxIMObservable.getInstance().addObserver(msgStatusChangeSubscribe);
    }

    /**
     * 反注销群聊监听
     */
    public static void unregisterGroupListener()
    {
        if (groupChatSubscribe != null)
        {
            CxIMObservable.getInstance().deleteObserver(groupChatSubscribe);
            groupChatSubscribe = null;
        }
        if (msgStatusChangeSubscribe != null)
        {
            CxIMObservable.getInstance().deleteObserver(msgStatusChangeSubscribe);
            groupChatSubscribe = null;
        }
    }

    /**
     * 发送聊天消息
     *
     * @param groupId
     * @param msg
     * @param chatMsgType 消息类型
     * @return
     */
    public static CxMessage sendGroupMsg(String groupId, String msg, int chatMsgType, long audioOrVideoLength, Map<String, String> cxAttachment)
    {
        return CxSocketManager.getInstance().sendGroupChat(groupId, msg, chatMsgType, audioOrVideoLength, cxAttachment);
    }

    /**
     * 注册单聊监听
     *
     * @param receiverMsgCallback     接收到单聊消息监听
     * @param msgStatusChangeCallback 发送消息状态发生改变监听
     */
    public static void registerSingleChatListener(CxUpdateUICallback receiverMsgCallback, CxMsgStatusChangeSubscribe.MsgStatusChangeCallback msgStatusChangeCallback)
    {
        unregisterSingleChatListener();

        singleChatSubscribe = new CxSingleChatSubscribe(receiverMsgCallback);
        msgStatusChangeSubscribe = new CxMsgStatusChangeSubscribe(msgStatusChangeCallback);

        CxIMObservable.getInstance().addObserver(singleChatSubscribe);
        CxIMObservable.getInstance().addObserver(msgStatusChangeSubscribe);
    }

    /**
     * 反注销监听
     */
    public static void unregisterSingleChatListener()
    {
        if (singleChatSubscribe != null)
        {
            CxIMObservable.getInstance().deleteObserver(singleChatSubscribe);
            singleChatSubscribe = null;
        }
        if (msgStatusChangeSubscribe != null)
        {
            CxIMObservable.getInstance().deleteObserver(msgStatusChangeSubscribe);
            msgStatusChangeSubscribe = null;
        }
    }

    /**
     * 发送单聊聊天消息
     *
     * @param to
     * @param msg
     * @param chatMsgType 消息类型
     * @return
     */
    public static CxMessage sendSingleChatMsg(String to, String msg, int chatMsgType, long audioOrVideoLength, Map<String, String> cxAttachment)
    {
        return CxSocketManager.getInstance().sendSingleChat(to, msg, chatMsgType, audioOrVideoLength, cxAttachment);
    }

    /**
     * 发送单聊聊天消息
     *
     * @param to
     * @param msg
     * @param chatMsgType 消息类型
     * @return
     */
    public static CxMessage sendSingleChatMsg(String to, String msg, int chatMsgType, boolean isHotChat, long audioOrVideoLength, Map<String, String> cxAttachment)
    {
        return CxSocketManager.getInstance().sendSingleChat(to, msg, chatMsgType, isHotChat, audioOrVideoLength, cxAttachment);
    }

    /**
     * 重发消息
     *
     * @param cxMessage
     */
    public static void reSendChatMsg(CxMessage cxMessage)
    {
        CxSocketManager.getInstance().reSendChatMsg(cxMessage);
    }

    /**
     * 保存通知类型的消息
     *
     * @param groupId
     * @param msg
     * @param timeMillisecond
     */
    public static IMMessage saveNoticeMsg(String groupId, String msg, long timeMillisecond)
    {
        IMMessage message = new IMMessage();
        String msgId = UUID.next();
        message.setMessageId(msgId);
        message.setGroupId(groupId);
        message.setMessage(msg);
        message.setMsgChatType(NoticeType.NORMAL_TYPE);
        message.setType(CxIMMessageType.GROUP_CHAT.getValue());
        message.setCreateTime(DateUtils.getTimestampString(timeMillisecond));
        message.setCreateTimeMillisecond(timeMillisecond);
        message.setMsgStatus(CxIMMessageStatus.SUCCESS);
        message.setDirect(CxIMMessageDirect.RECEIVER);
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insertOrReplace(message);
        return message;
    }


    /**
     * 保存音视频通话消息
     *
     * @param to
     * @param msg
     * @param msgType
     * @param timeMillisecond
     * @return
     */
    public static IMMessage saveAudioOrVideoMsg(String to, String from, String msg, int msgType, int direct, long timeMillisecond)
    {
        IMMessage message = new IMMessage();
        String msgId = UUID.next();
        message.set_to(to);
        message.set_from(from);
        message.setMessageId(msgId);
        message.setMessage(msg);
        message.setMsgChatType(msgType);
        message.setType(CxIMMessageType.SINGLE_CHAT.getValue());
        message.setCreateTime(DateUtils.getTimestampString(timeMillisecond));
        message.setCreateTimeMillisecond(timeMillisecond);
        message.setMsgStatus(CxIMMessageStatus.SUCCESS);
        message.setDirect(direct);
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insertOrReplace(message);
        return message;
    }

    /**
     * 注册单聊全局消息接收
     *
     * @param msgCallback
     */
    public static void registerGlobalSingleChatRec(CxReceiverMsgCallback msgCallback)
    {
        CxProcessorManager.getInstance().singleChatProcessor.setCxReceiverMsgCallback(msgCallback);
    }

    /**
     * 注册群聊全局消息接收
     *
     * @param msgCallback
     */
    public static void registerGlobalGroupChatRec(CxReceiverMsgCallback msgCallback)
    {
        CxProcessorManager.getInstance().groupChatProcessor.setCxReceiverMsgCallback(msgCallback);
    }

    /**
     * @param msgCallback
     */
    public static void registerChatMessageStatusRec(CxReadMessageCallBack msgCallback)
    {
        CxProcessorManager.getInstance().cxReadProcessor.setCxReadMessageCallBack(msgCallback);
    }
//    /**
//     *
//     * @param msgCallback
//     */
//    public static void registerChatMessageOffLineStatusRec(CxReadMessageCallBack msgCallback)
//    {
//        CxProcessorManager.getInstance().offlinePushProcessor.setCxReadMessageCallBack(msgCallback);
//    }

}
