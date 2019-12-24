package com.superdata.im.utils;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.conversation.IMConversation;
import com.im.client.struct.Header;
import com.im.client.util.UUID;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.CxWrapperConverstaion;
import com.superdata.im.utils.Observable.CxConversationObserable;

import java.util.Date;

import static com.superdata.im.processor.CxChatCxBaseProcessor.fixLinkHashMap;

/**
 * User: Selson
 * Date: 2018-06-26
 * FIXME
 */
public class ImUtils
{
    public static final String idg_notice = "idg_tongzhigonggao";
    public static final String idg_common = "idg_gongzhonghao";
    public static final String idg_system_message = "idg_system_message";
    public static final String idg_newsletter = "idg_newsletter";
    public static final String idg_newsList = "idg_newsList";

    private ImUtils()
    {
    }

    public static synchronized ImUtils getInstance()
    {
        return ImUtilsHelper.imUtils;
    }

    private static final class ImUtilsHelper
    {
        private static final ImUtils imUtils = new ImUtils();
    }

    //创建
    private CxMessage createTmpMessage(int type, String toAccount, String fromAccount, String content)
    {
        CxMessage cxMessage = new CxMessage();
        cxMessage.setCallback(null);
        cxMessage.setCreateTime(DateUtils.getTimestampString(System.currentTimeMillis()));
        cxMessage.setType(type);
        cxMessage.setId(0);
        cxMessage.setHotChatVisible(false);
        cxMessage.setReader(false);
        cxMessage.setReaderStatus(false);
        cxMessage.setHotTime(0);
        cxMessage.setDirect(1);
        cxMessage.setStatus(0);
        cxMessage.setHotChat(false);

        com.im.client.struct.IMMessage imMessage = new com.im.client.struct.IMMessage();
        imMessage.setBody(content);

        Header header = new Header();
        header.setAttachment(null);
        header.setTo(toAccount);
        header.setPassword("");
        header.setFrom(fromAccount);
        header.setGroupId("");
        header.setMessageId(fromAccount + UUID.next());

        header.setCreateTime(System.currentTimeMillis());
        header.setMediaType(1);
        header.setPriority(new Byte("0"));
        header.setSocketType(0);
        header.setType(new Byte("1"));

        imMessage.setHeader(header);
        cxMessage.setImMessage(imMessage);
        return cxMessage;
    }

    //判断信息,是否已经加了会话记录
    public boolean isHaveConversation(int type)
    {
        boolean isHave = false;
        IMConversation imConversation = null;
        if (type == CxIMMessageType.CHAT_NOTICE.getValue())
        {
            imConversation = IMDaoManager.getInstance().getDaoSession()
                    .getIMConversationDao().loadByUserName(idg_notice);
        } else if (type == CxIMMessageType.CHAT_COMMON.getValue())
        {
            imConversation = IMDaoManager.getInstance().getDaoSession()
                    .getIMConversationDao().loadByUserName(idg_common);
        }
        if (imConversation != null)
        {
            isHave = true;
        }
        return isHave;
    }

//    public void updataConversation(IMConversation conversation, String fromName, String title, String content,
//                                   String msgId, int unReadMsg, int type, Date updateTime)
//    {
//        IMConversation conversation = null;
//        if (cxMessage.getType() == CxIMMessageType.SINGLE_CHAT.getValue())
//        {
//            conversation = fixLinkHashMap.get(cxMessage.getImMessage().getHeader().getFrom());
//            //这里搜索会话的列表找出对应的对象
//            IMConversation imConversation = IMDaoManager.getInstance().getDaoSession()
//                    .getIMConversationDao().loadByUserName(cxMessage.getImMessage().getHeader().getFrom());
//            if (StringUtils.notEmpty(imConversation))
//            {
//                if (StringUtils.notEmpty(conversation))
//                    conversation.setUnReadMsg(imConversation.getUnReadMsg() + 1);
//            }
//        }
//    }

    //存入会话
    public void saveConversation(IMConversation conversation, String fromName, String title, String content,
                                 int unReadMsg, int type, Date updateTime)
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
        conversation.setMessageId(type + fromName);
        conversation.setCreateTime(new Date());
        conversation.setTitle(title);
        conversation.setType(type);
        conversation.setUsername(fromName);
        conversation.setUpdateTime(updateTime);
        conversation.setAttachment(content);

        conversation.setId(IMDaoManager.getInstance().getDaoSession().getIMConversationDao().insertOrReplace(conversation));
        if (fixLinkHashMap != null)
        {
            if (fixLinkHashMap.get(conversation.getUsername()) != null)
            {
                if (conversation != null)
                    CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(CxWrapperConverstaion
                            .OperationType
                            .UPDATE_CONVERSTAION, conversation));
            } else
            {
                fixLinkHashMap.put(conversation.getUsername(), conversation);
                if (conversation != null)
                    CxConversationObserable.getInstance().notifyUpdate(new CxWrapperConverstaion(CxWrapperConverstaion
                            .OperationType.ADD_CONVERSTAION, conversation));
            }
        }
//        return conversation;
    }

    //更新信息
}