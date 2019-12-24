package com.superdata.im.entity;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-09-19
 * FIXME
 */
public class MessageCallBactStatus implements Serializable
{
    private String messageId;

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }
}