package com.cxgz.activity.cxim.utils;

import android.content.Context;

import com.chaoxiang.imsdk.chat.CXChatManager;
import com.google.gson.Gson;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.entity.MessageEntity;
import com.superdata.im.entity.ShareDicEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Selson
 * Date: 2016-12-20
 * FIXME
 */
public class SendMsgUtils
{
    public static CxMessage sendMsg(Context context, MessageEntity messageEntity)
    {
        Map<String, String> cxAttachment = new HashMap<String, String>();
        cxAttachment.put("location", "");
        cxAttachment.put("imageDimensions", "");
        cxAttachment.put("isBurnAfterRead", "0");
        cxAttachment.put("burnAfterReadTime", "10");
        ShareDicEntity shareDicEntity=new ShareDicEntity();
        shareDicEntity.setShareUrl(messageEntity.getShareUrl());
        shareDicEntity.setShareTitle(messageEntity.getShareTitle());
        shareDicEntity.setShareContent(messageEntity.getShareContent());

        cxAttachment.put("shareDic",new Gson().toJson(shareDicEntity));

        CxMessage cxMessage = null;
        if (messageEntity.getChatType() == (CxIMMessageType.SINGLE_CHAT.getValue()))
        {
            cxMessage = CXChatManager.sendSingleChatMsg(messageEntity.getImAccount(), messageEntity.getMsg(), messageEntity.getMediaType(), messageEntity.getAudioOrVideoLength(), cxAttachment);
        } else if (messageEntity.getChatType() == CxIMMessageType.GROUP_CHAT.getValue())
        {
            cxMessage = CXChatManager.sendGroupMsg(messageEntity.getImAccount(), messageEntity.getMsg(), messageEntity.getMediaType(), messageEntity.getAudioOrVideoLength(), cxAttachment);
        }
        return cxMessage;
    }
} 