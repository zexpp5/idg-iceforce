package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2017-02-11
 * FIXME
 */
public class BoyEntity implements Serializable
{
    private AttachmentEntity attachment;

    private String body;

    private long createTime;

    private String from;

    private int mediaType;

    private String to;

    private String type;

    public static BoyEntity parse(String jsonString)
    {
        Gson gson = new Gson();
        BoyEntity info = gson.fromJson(jsonString, new TypeToken<BoyEntity>()
        {
        }.getType());
        return info;
    }


    public void setAttachment(AttachmentEntity attachment)
    {
        this.attachment = attachment;
    }

    public AttachmentEntity getAttachment()
    {
        return this.attachment;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public String getBody()
    {
        return this.body;
    }

    public void setCreateTime(long createTime)
    {
        this.createTime = createTime;
    }

    public long getCreateTime()
    {
        return this.createTime;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from;
    }

    public void setMediaType(int mediaType)
    {
        this.mediaType = mediaType;
    }

    public int getMediaType()
    {
        return this.mediaType;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getTo()
    {
        return this.to;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

} 