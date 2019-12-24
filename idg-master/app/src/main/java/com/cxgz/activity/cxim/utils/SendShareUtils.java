package com.cxgz.activity.cxim.utils;

import android.content.Context;

import com.cxgz.activity.cxim.bean.SendShareBean;
import com.im.client.MediaType;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.MessageEntity;

/**
 * User: Selson
 * Date: 2016-12-28
 * FIXME
 */
public class SendShareUtils
{
    public static void sendShareMsg(Context context, SendShareBean sendShareBean)
    {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMsg("");
        messageEntity.setChatType(CxIMMessageType.SINGLE_CHAT.getValue());
        messageEntity.setImAccount(sendShareBean.getImAccount());
        messageEntity.setMediaType(MediaType.TEXT.value());
        messageEntity.setAudioOrVideoLength(-1);
        messageEntity.setShareContent(sendShareBean.getShareContent());
        messageEntity.setShareTitle(sendShareBean.getShareTitle());
        messageEntity.setShareUrl(sendShareBean.getShareUrl());
        SendMsgUtils.sendMsg(context, messageEntity);
    }

} 