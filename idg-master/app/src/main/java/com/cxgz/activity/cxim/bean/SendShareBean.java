package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-28
 * FIXME
 */
public class SendShareBean implements Serializable
{
    private String imAccount;
    private String shareContent;
    private String shareTitle;
    private String shareUrl;

    public String getImAccount()
    {
        return imAccount;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getShareContent()
    {
        return shareContent;
    }

    public void setShareContent(String shareContent)
    {
        this.shareContent = shareContent;
    }

    public String getShareTitle()
    {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle)
    {
        this.shareTitle = shareTitle;
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