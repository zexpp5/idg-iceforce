package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2017-04-28
 * FIXME
 */
public class StrangeBean implements Serializable
{
    private String icon;

    private String imAccount;

    private String name;

    private int userId;

    private String dpName;

    private int isFriend;

    private String userType;

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return this.icon;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getImAccount()
    {
        return this.imAccount;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public int getUserId()
    {
        return this.userId;
    }

    public void setDpName(String dpName)
    {
        this.dpName = dpName;
    }

    public String getDpName()
    {
        return this.dpName;
    }

    public void setIsFriend(int isFriend)
    {
        this.isFriend = isFriend;
    }

    public int getIsFriend()
    {
        return this.isFriend;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getUserType()
    {
        return this.userType;
    }
} 