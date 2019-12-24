package com.superdata.im.utils;

import com.chaoxiang.base.utils.StringUtils;
import com.im.client.MediaType;
import com.im.client.struct.Header;
import com.im.client.struct.IMMessage;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.entity.CxAttachment;
import com.superdata.im.entity.CxMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-07
 * @desc
 */
public class CXMessageUtils
{
    /**
     * 创建消息发送实体
     * @param msg
     * @param to
     * @param mediaType
     * @return
     */
    public static CxMessage createSendMsg(String msg, String to, int mediaType, Map<String, String> cxAttachment)
    {
        return createSendMsg("", msg, to, mediaType, cxAttachment);
    }


    /**
     * 创建消息发送实体-阅后即焚
     *
     * @param msg
     * @param to
     * @param mediaType
     * @return
     */
    public static CxMessage createSendMsg(String msg, String to, boolean isHotChat, int mediaType, Map<String, String> cxAttachment)
    {
        return createSendMsg("", msg, to, isHotChat, mediaType, cxAttachment);
    }

    /**
     * 创建消息发送实体
     *
     * @param groupId
     * @param msg
     * @param to
     * @param mediaType
     * @return
     */
    public static CxMessage createSendMsg(String groupId, String msg, String to, int mediaType, Map<String, String> cxAttachment)
    {
        IMMessage imMessage = new IMMessage();
        Header header = new Header();
        header.setGroupId(groupId);
        header.setTo(to);
        //添加一个百度定位的经纬度和省市
        header.setAttachment(cxAttachment);
        imMessage.setHeader(header);
        imMessage.setBody(msg);
        CxMessage cxMessage = new CxMessage(imMessage);
        cxMessage.setDirect(CxIMMessageDirect.SEND);
        cxMessage.setMediaType(mediaType);
        if (mediaType != MediaType.TEXT.value() && mediaType != MediaType.POSITION.value())
        {
            cxMessage.setStatus(CxIMMessageStatus.FILE_SENDING);
        } else
        {
            cxMessage.setStatus(CxIMMessageStatus.SENDING);
        }
        return cxMessage;

    }

    /**
     * 创建消息发送实体，阅后即焚
     *
     * @param groupId
     * @param msg
     * @param to
     * @param mediaType
     * @return
     */
    public static CxMessage createSendMsg(String groupId, String msg, String to, boolean isHotChat, int mediaType, Map<String, String> cxAttachment)
    {
        IMMessage imMessage = new IMMessage();
        Header header = new Header();
        header.setGroupId(groupId);
        header.setTo(to);
        //添加一个百度定位的经纬度和省市
        header.setAttachment(cxAttachment);
        imMessage.setHeader(header);
        imMessage.setBody(msg);
        CxMessage cxMessage = new CxMessage(imMessage);
        cxMessage.setDirect(CxIMMessageDirect.SEND);
        cxMessage.setMediaType(mediaType);
        cxMessage.setHotChat(isHotChat);//加入是否是阅后即焚
        if (mediaType != MediaType.TEXT.value() && mediaType != MediaType.POSITION.value())
        {
            cxMessage.setStatus(CxIMMessageStatus.FILE_SENDING);
        } else
        {
            cxMessage.setStatus(CxIMMessageStatus.SENDING);
        }
        return cxMessage;

    }


    /**
     * 检测ObservableMsgEntity是否存在
     */
    public static boolean checkExist(String msgId, List<CxMessage> cxMessages)
    {
        for (int i = cxMessages.size() - 1; i >= 0; i--)
        {
            CxMessage cxMessage = cxMessages.get(i);
            if (cxMessage.getImMessage().getHeader().getMessageId() == msgId)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 更新msg状态
     *
     * @param msgId
     * @param cxMessages
     * @param msgStatus
     */
    public static void updateMsgStatus(String msgId, List<CxMessage> cxMessages, int msgStatus)
    {
        for (int i = cxMessages.size() - 1; i >= 0; i--)
        {
            CxMessage cxMessage = cxMessages.get(i);
            if (cxMessage.getImMessage().getHeader().getMessageId() == msgId)
            {
                cxMessage.setStatus(msgStatus);
                break;
            }
        }
    }

    /**
     * 将IMMessage转成数据库的message
     *
     * @param cxMessage
     * @return
     */
    public static com.chaoxiang.entity.chat.IMMessage convertCXMessage(CxMessage cxMessage)
    {
        if (cxMessage == null)
        {
            return null;
        }
        IMMessage msg = cxMessage.getImMessage();
        com.chaoxiang.entity.chat.IMMessage daoMsg = new com.chaoxiang.entity.chat.IMMessage();
        daoMsg.set_from(msg.getHeader().getFrom());
        daoMsg.set_to(msg.getHeader().getTo());
        daoMsg.setMessage(msg.getBody());
        if (msg.getHeader().getGroupId() != null && !"".equals(msg.getHeader().getGroupId()))
        {
            daoMsg.setGroupId(msg.getHeader().getGroupId());
        }

        daoMsg.setMessageId(msg.getHeader().getMessageId());
        daoMsg.setMsgStatus(cxMessage.getStatus());
        daoMsg.setMsgChatType(cxMessage.getMediaType());
        daoMsg.setDirect(cxMessage.getDirect());
        daoMsg.setType(cxMessage.getType());
        daoMsg.setCreateTime(cxMessage.getCreateTime());
        daoMsg.setCreateTimeMillisecond(cxMessage.getCreateTimeMillisecond());
        daoMsg.setPriority(msg.getHeader().getPriority());
        daoMsg.setIsReaded(cxMessage.isReader());
        daoMsg.setIsReadStatus(cxMessage.isReaderStatus());

        daoMsg.setIsHotChat(cxMessage.isHotChat());
        daoMsg.setIsHotChatVisible(cxMessage.isHotChatVisible());

        //转换
        daoMsg.setAttachment(CxAttachment.returnJsonStringFromMap(msg.getHeader().getAttachment()));

        return daoMsg;
    }

    /**
     * 转换IMMessage为CXMessage
     *
     * @param chatMessage
     * @return
     */
    public static CxMessage covertIMMessage(com.chaoxiang.entity.chat.IMMessage chatMessage)
    {
        if (chatMessage == null)
        {
            return null;
        }

        IMMessage imMessage = new IMMessage();
        imMessage.setBody(chatMessage.getMessage());
        Header header = new Header();
        header.setTo(chatMessage.get_to());
        header.setFrom(chatMessage.get_from());
        header.setMessageId(chatMessage.getMessageId());
        //加入groupid
        if (chatMessage.getGroupId() != null && !"".equals(chatMessage.getGroupId()))
        {
            header.setGroupId(chatMessage.getGroupId());
        }
        header.setMediaType(chatMessage.getMsgChatType());
        //转换
        header.setAttachment(CxAttachment.parseMap(chatMessage.getAttachment()));

        imMessage.setHeader(header);

        CxMessage cxMessage = new CxMessage(imMessage);

        if (chatMessage.getId() != null && (chatMessage.getId().equals("")))
        {
            cxMessage.setId(chatMessage.getId());
        }
        cxMessage.setType(chatMessage.getType());
        cxMessage.setStatus(chatMessage.getMsgStatus());
        cxMessage.setDirect(chatMessage.getDirect());
        cxMessage.setCreateTimeMillisecond(chatMessage.getCreateTimeMillisecond());
        cxMessage.setCreateTime(chatMessage.getCreateTime());
        cxMessage.setId(chatMessage.getId());
        Boolean isReader = chatMessage.getIsReaded();
        if (isReader == null)
        {
            cxMessage.setReader(false);
        } else
        {
            cxMessage.setReader(chatMessage.getIsReaded());
        }

        Boolean isReaderStatus = chatMessage.getIsReadStatus();
        if (isReaderStatus == null)
        {
            cxMessage.setReaderStatus(false);
        } else
        {
            cxMessage.setReaderStatus(chatMessage.getIsReadStatus());
        }


        Boolean isTmpHotChat = chatMessage.getIsHotChat();
        if (isTmpHotChat == null)
        {
            cxMessage.setHotChat(false);
        } else
        {
            cxMessage.setHotChat(chatMessage.getIsHotChat());
        }

        Boolean isHotChatVisible = chatMessage.getIsHotChatVisible();
        if (isHotChatVisible == null)
        {
            cxMessage.setHotChatVisible(false);
        } else
        {
            cxMessage.setHotChatVisible(chatMessage.getIsHotChatVisible());
        }

        return cxMessage;
    }

    /**
     * 转换调转顺序
     * @return
     */
    public static List<CxMessage> changeCxMessage(List<CxMessage> cxMessageList)
    {
        List<CxMessage> cxMessages = new ArrayList<>();
        if (StringUtils.notEmpty(cxMessageList) && cxMessageList.size() > 0)
        {
            for (int i = 0; i < cxMessageList.size(); i++)
            {
                cxMessages.add(0, cxMessageList.get(i));
            }
            return cxMessages;
        }
        return cxMessages;
    }

    /**
     * 转换CXMessage为IMMessage
     *
     * @param
     * @return
     */
    public static List<com.chaoxiang.entity.chat.IMMessage> covertCxMessage(List<CxMessage> cxMessages)
    {
        List<com.chaoxiang.entity.chat.IMMessage> iMMessage = new ArrayList<>();
        if (cxMessages == null || cxMessages.isEmpty())
        {
            return iMMessage;
        }
        for (int i = 0; i < cxMessages.size(); i++)
        {
            iMMessage.add(0, convertCXMessage(cxMessages.get(i)));
        }
        return iMMessage;
    }

    /**
     * 转换IMMessage为CXMessage
     * @param chatMessages
     * @return
     */
    public static List<CxMessage> covertIMMessage(List<com.chaoxiang.entity.chat.IMMessage> chatMessages)
    {
        List<CxMessage> cxMessages = new ArrayList<>();
        if (chatMessages == null || chatMessages.isEmpty())
        {
            return cxMessages;
        }
        for (int i = 0; i < chatMessages.size(); i++)
        {
            cxMessages.add(0, covertIMMessage(chatMessages.get(i)));
        }
        return cxMessages;
    }

}
