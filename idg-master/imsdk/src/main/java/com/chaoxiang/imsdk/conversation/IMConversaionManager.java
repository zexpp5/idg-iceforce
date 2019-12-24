package com.chaoxiang.imsdk.conversation;

import android.database.Cursor;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.conversation.IMConversation;
import com.chaoxiang.entity.dao.IMConversationDao;
import com.chaoxiang.imsdk.chat.CXVoiceGroupManager;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.processor.CxChatCxBaseProcessor;
import com.superdata.im.utils.Observable.CxConversationObserable;
import com.superdata.im.utils.Observable.CxConversationSubscribe;

import java.util.Date;
import java.util.List;

/**
 * @auth lwj
 * @date 2016-01-13
 * @desc
 */
public class IMConversaionManager
{

    private static IMConversaionManager instance;

    private IMConversaionManager()
    {

    }

    public static synchronized IMConversaionManager getInstance()
    {
        if (instance == null)
        {
            instance = new IMConversaionManager();
        }
        return instance;
    }

    public static void clear()
    {

    }

    /**
     * 删除会话
     *
     * @param type     删除的类型
     * @param username 删除的对象
     */
    public void deleteConversation(int type, String username)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao.Properties
                            .Type.eq(type),
                    IMConversationDao.Properties.Username.eq(username)).buildDelete().executeDeleteWithoutDetachingEntities();
            CxChatCxBaseProcessor.fixLinkHashMap.remove(username);
        } catch (Exception e)
        {
            SDLogUtil.error("-----删除会话-----");
        }
    }

    public void deleteConversation2(int type, String username)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao.Properties
                            .Type.eq(type),
                    IMConversationDao.Properties.Username.eq(username)).buildDelete().executeDeleteWithoutDetachingEntities();
            CxChatCxBaseProcessor.fixLinkHashMap.remove(username);
        } catch (Exception e)
        {
            SDLogUtil.error("-----删除会话-----");
        }
    }

    public void delteMsgByMsgId(String username)
    {
        IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao.Properties
                .Username.eq(username)).buildDelete().executeDeleteWithoutDetachingEntities();
    }

    /**
     * 获取会话列表(全部)
     *
     * @param limit 获取多少条
     * @return
     */
    public List<IMConversation> loadConversation(int limit)
    {
        List<IMConversation> conversations = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().
                queryBuilder()
                .limit(limit)
                .orderDesc(IMConversationDao.Properties.UpdateTime).build().list();
        return conversations;
    }

    /**
     * 获取会话列表
     *
     * @return
     */
    public List<IMConversation> loadConversation()
    {
        List<IMConversation> conversations = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().
                queryBuilder()
                .orderDesc(IMConversationDao.Properties.UpdateTime).build().list();
        return conversations;
    }

    /**
     * 查询在群聊列表中存在的。但在语音会议列表中不存在的
     */
    public int loadConversationRemoveVoice()
    {
        String conversationTableName = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().getTablename();
        String voiceGroupTableName = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().getTablename();
        String sqlString = "SELECT " + conversationTableName + ".USERNAME FROM " + conversationTableName + ","
                + voiceGroupTableName + " where(" + conversationTableName + ".USERNAME != " + voiceGroupTableName + ".GROUP_ID) ";

        Cursor c = IMDaoManager.getInstance().getDaoSession().getDatabase().rawQuery(sqlString, null);
        int count = 0;
        if (c.moveToFirst())
        {
            count = c.getInt(0);
        }
        if (c != null && !c.isClosed())
        {
            c.close();
        }
        return count;
    }

    public int loadAllUnreadCount()
    {
        int count = 0;
        try
        {
            String sql = "SELECT SUM(UN_READ_MSG) AS num from " + IMDaoManager.getInstance().getDaoSession()
                    .getIMConversationDao().getTablename();
            Cursor c = IMDaoManager.getInstance().getDaoSession().getDatabase().rawQuery(sql, null);

            if (c.moveToFirst())
            {
                count = c.getInt(0);
            }
            if (c != null && !c.isClosed())
            {
                c.close();
            }
        } catch (Exception e)
        {

        }
        return count;
    }

    /**
     * 保存群聊会话
     *
     * @param groupName
     */
    public IMConversation saveGroupConversation(String groupid, IMMessage message, String groupName, String type)
    {
        IMConversation conversation = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().loadByUserName(String
                .valueOf(groupid));
        if (conversation == null)
        {
            conversation = new IMConversation();
            conversation.setCreateTime(new Date(message.getCreateTimeMillisecond()));
        }
        conversation.setUnReadMsg(1);
        conversation.setTitle(groupName);
        conversation.setUsername(groupid);
        conversation.setType(CxIMMessageType.GROUP_CHAT.getValue());
        conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
        conversation.setMessageId(message.getMessageId());

//        conversation.setAttachment(message.getMsgChatType() + "");

        IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation);
        if (CxChatCxBaseProcessor.fixLinkHashMap != null)
        {
            CxChatCxBaseProcessor.fixLinkHashMap.put(conversation.getUsername(), conversation);
        }
        return conversation;
    }

    /**
     * 保存群聊会话
     *
     * @param groupName
     */
    public IMConversation saveConversation(String groupid, IMMessage message, String groupName)
    {
        IMConversation conversation = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().loadByUserName(String
                .valueOf(groupid));
        if (conversation == null)
        {
            conversation = new IMConversation();
            conversation.setCreateTime(new Date(message.getCreateTimeMillisecond()));
            conversation.setUnReadMsg(1);
            conversation.setTitle(groupName);
            conversation.setUsername(groupid);
            conversation.setType(CxIMMessageType.GROUP_CHAT.getValue());
            conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
            conversation.setMessageId(message.getMessageId());
            IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insert(conversation);
        } else
        {
            conversation.setUnReadMsg(1);
            conversation.setTitle(groupName);
            conversation.setUsername(groupid);
            conversation.setType(CxIMMessageType.GROUP_CHAT.getValue());
            conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
            conversation.setMessageId(message.getMessageId());
//        conversation.setAttachment(message.getMsgChatType() + "");
            IMDaoManager.getInstance().getDaoSession().getIMConversationDao().update(conversation);
        }

        if (CxChatCxBaseProcessor.fixLinkHashMap != null)
        {
            CxChatCxBaseProcessor.fixLinkHashMap.put(conversation.getUsername(), conversation);
        }
        return conversation;
    }

    /**
     * 单聊保存
     *
     * @param name
     * @param message
     * @return
     */

    public IMConversation saveConversation(String name, IMMessage message)
    {
        IMConversation conversation = IMDaoManager.getInstance().getDaoSession().getIMConversationDao().loadByUserName(name);
        if (conversation == null)
        {
            conversation = new IMConversation();
            conversation.setCreateTime(new Date(message.getCreateTimeMillisecond()));
        }

        conversation.setTitle(name);
        conversation.setUsername(name);
        conversation.setType(CxIMMessageType.SINGLE_CHAT.getValue());
        conversation.setUpdateTime(new Date(message.getCreateTimeMillisecond()));
        conversation.setMessageId(message.getMessageId());

        IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation);
        if (CxChatCxBaseProcessor.fixLinkHashMap != null)
        {
            CxChatCxBaseProcessor.fixLinkHashMap.put(conversation.getUsername(), conversation);
        }
        return conversation;
    }

    /**
     * 保存会话
     *
     * @param identity
     */
    public IMConversation saveConversation(IMConversation conversation, String identity, String title, String lastMsgId, int
            unReadMsg, int type, Date updateTime)
    {
        if (conversation == null)
        {
            conversation = new IMConversation();
        }
        if (updateTime == null)
        {
            updateTime = new Date();
        }

        conversation.setUnReadMsg(unReadMsg);
        conversation.setMessageId(lastMsgId);
        conversation.setCreateTime(new Date());
        conversation.setTitle(title);
        conversation.setType(type);
        conversation.setUsername(identity);
        conversation.setUpdateTime(updateTime);
        conversation.setId(IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation));
        if (CxChatCxBaseProcessor.fixLinkHashMap != null)
        {
            CxChatCxBaseProcessor.fixLinkHashMap.put(conversation.getUsername(), conversation);
        }
        return conversation;
    }

    /**
     * 通过username查询对应的会话
     *
     * @param
     * @return
     */
    public IMConversation loadByMsgId(String msgId)
    {
//        IMConversation imConversation =null;
//        String conversationTableName = conversationDao.getTablename();
//        String sqlString = "SELECT *  FROM " + conversationTableName + " WHERE MESSAGE_ID = " + msgId;
//        Cursor c = IMDaoManager.getInstance().getDaoSession().getDatabase().rawQuery(sqlString, null);
//        if (c.moveToNext()) {
//            imConversation = new IMConversation();
//            int idIndex = c.getColumnIndex("_id");
//            int msgIdIndex = c.getColumnIndex("MESSAGE_ID");
//            int usernameIndex = c.getColumnIndex("USERNAME");
//            int updateTimeIndex = c.getColumnIndex("UPDATE_TIME");
//            int createTimeIndex = c.getColumnIndex("CREATE_TIME");
//            int unreadIndex = c.getColumnIndex("UN_READ_MSG");
//            int titleIndex = c.getColumnIndex("TITLE");
//            int attachmentIndex = c.getColumnIndex("ATTACHMENT");
//            imConversation.setMessageId(c.getString(msgIdIndex));
//            imConversation.setUsername(c.getString(usernameIndex));
//            imConversation.setId(Long.parseLong(c.getString(idIndex)));
//            imConversation.setUpdateTime(new Date(c.getString(updateTimeIndex)));
//            imConversation.setCreateTime(new Date(c.getString(createTimeIndex)));
//            imConversation.setUnReadMsg(Integer.parseInt(c.getString(unreadIndex)));
//            imConversation.setTitle(c.getString(titleIndex));
//            imConversation.setAttachment(c.getString(attachmentIndex));
//        }
        return IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao
                .Properties.MessageId.eq(msgId)).unique();
        //return  imConversation;
    }

    /**
     * 通过username查询对应的会话
     * @param userName
     * @return
     */
    public IMConversation loadByUserName(String userName)
    {
        return IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao
                .Properties.Username.eq(userName)).unique();
    }

    public void updateConversation(IMConversation conversation)
    {
        IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation);
    }

    /**
     * 根据群组ID获取对应的所有信息，用在语音会议上!
     */
    public IMConversation loadByGroupId(String groupID)
    {
        // Query 类代表了一个可以被重复执行的查询
//        QueryBuilder query = conversationDao.queryBuilder();
//        query.where(IMConversationDao.Properties.Username.eq(groupID))
//                .list();
//        //,query.and(IMConversationDao.Properties.Type.eq()),
//        List<IMConversation> notes = query.list();
//        IMConversation info=notes.get(0);
//        return info;

        return IMDaoManager.getInstance().getDaoSession().getIMConversationDao().queryBuilder().where(IMConversationDao
                .Properties.Username.eq(groupID)).unique();
    }


    /**
     * 通知会话更新
     *
     * @param conversation
     * @param type
     */
    public void notifyConversationUpdate(IMConversation conversation, CxWrapperConverstaion.OperationType type)
    {
        CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(type, conversation));
    }

    /**
     * 注册会话监听
     */
    public CxConversationSubscribe registerConversationListener(CxConversationSubscribe.ConversationChangeCallback callback)
    {
        CxConversationSubscribe conversationSubscribe = new CxConversationSubscribe(callback);
        CxConversationObserable.getInstance().addObserver(conversationSubscribe);
        return conversationSubscribe;
    }

    /**
     * 注销会话监听
     *
     * @param conversationSubscribe
     */
    public void unRegisterConversationListener(CxConversationSubscribe conversationSubscribe)
    {
        CxConversationObserable.getInstance().deleteObserver(conversationSubscribe);
    }
}
