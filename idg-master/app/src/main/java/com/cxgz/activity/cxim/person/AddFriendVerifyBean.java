package com.cxgz.activity.cxim.person;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-06-22
 * FIXME 发送验证信息的实体类
 */
public class AddFriendVerifyBean implements Serializable
{
    private int companyId; //": 1,
    private int selfId;//": 85, //被邀请人ID
    private String selfName;//": "ceshi074", //被邀请人姓名
    private int userId;//": "84", //邀请人ID
    private String userName;//": "ceshi073" //邀请人姓名,
    private String attached;//":""  //附件信息
    private String hxAccount;
    private String creatTime;
    private String icon;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(int companyId)
    {
        this.companyId = companyId;
    }

    public int getSelfId()
    {
        return selfId;
    }

    public void setSelfId(int selfId)
    {
        this.selfId = selfId;
    }

    public String getSelfName()
    {
        return selfName;
    }

    public void setSelfName(String selfName)
    {
        this.selfName = selfName;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getAttached()
    {
        return attached;
    }

    public void setAttached(String attached)
    {
        this.attached = attached;
    }

    public String getHxAccount()
    {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount)
    {
        this.hxAccount = hxAccount;
    }

    public String getCreatTime()
    {
        return creatTime;
    }

    public void setCreatTime(String creatTime)
    {
        this.creatTime = creatTime;
    }
}