package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-19
 * FIXME 分享实体类
 */
public class ShareDicEntity implements Serializable
{
    private String shareTitle;
    private String shareContent;
    private String shareUrl;
    private String shareIconUrl;
    private String ShareAppName;

    public static ShareDicEntity parse(String jsonString)
    {
        Gson gson = new Gson();
        ShareDicEntity info = gson.fromJson(jsonString, new TypeToken<ShareDicEntity>()
        {
        }.getType());
        return info;
    }

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
        return ShareAppName;
    }

    public void setShareAppName(String shareAppName)
    {
        ShareAppName = shareAppName;
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
}