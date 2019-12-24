package com.superdata.im.entity;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-20
 * FIXME
 */
public class MessageEntity implements Serializable
{
    private int chatType;
    private String imAccount;
    private String msg;
    private int mediaType;
    private long audioOrVideoLength;

    //分享的内容。
    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private String shareIconUrl;
    private String shareAppName;

    public String getShareIconUrl()
    {
        return shareIconUrl;
    }

    public void setShareIconUrl(String shareIconUrl)
    {
        this.shareIconUrl = shareIconUrl;
    }
	
    public String getShareAppName()
    {
        return shareAppName;
    }

    public void setShareAppName(String shareAppName)
    {
        shareAppName = shareAppName;
    }

    public String getShareTitle()
    {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle)
    {
        this.shareTitle = shareTitle;
    }

    public String getShareContent()
    {
        return shareContent;
    }

    public void setShareContent(String shareContent)
    {
        this.shareContent = shareContent;
    }

    public String getShareUrl()
    {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl)
    {
        this.shareUrl = shareUrl;
    }

    public int getChatType()
    {
        return chatType;
    }

    public void setChatType(int chatType)
    {
        this.chatType = chatType;
    }

    public String getImAccount()
    {
        return imAccount;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getMediaType()
    {
        return mediaType;
    }

    public void setMediaType(int mediaType)
    {
        this.mediaType = mediaType;
    }

    public long getAudioOrVideoLength()
    {
        return audioOrVideoLength;
    }

    public void setAudioOrVideoLength(long audioOrVideoLength)
    {
        this.audioOrVideoLength = audioOrVideoLength;
    }
}