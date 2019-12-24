package com.superdata.im.entity;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2017-02-11
 * FIXME
 */
public class AttachmentEntity implements Serializable
{
    private String burnAfterReadTime;

    private String imageDimensions;

    private String isBurnAfterRead;

    private String location;

    private String shareDic;

    private String userInfo;

    public String getUserInfo()
    {
        return userInfo;
    }

    public void setUserInfo(String userInfo)
    {
        this.userInfo = userInfo;
    }

    public void setBurnAfterReadTime(String burnAfterReadTime)
    {
        this.burnAfterReadTime = burnAfterReadTime;
    }

    public String getBurnAfterReadTime()
    {
        return this.burnAfterReadTime;
    }

    public void setImageDimensions(String imageDimensions)
    {
        this.imageDimensions = imageDimensions;
    }

    public String getImageDimensions()
    {
        return this.imageDimensions;
    }

    public void setIsBurnAfterRead(String isBurnAfterRead)
    {
        this.isBurnAfterRead = isBurnAfterRead;
    }

    public String getIsBurnAfterRead()
    {
        return this.isBurnAfterRead;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return this.location;
    }

    public void setShareDic(String shareDic)
    {
        this.shareDic = shareDic;
    }

    public String getShareDic()
    {
        return this.shareDic;
    }
} 