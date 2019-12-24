package com.cxgz.activity.cxim.bean;


import com.chaoxiang.base.utils.StringUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;


/**
 * User: Selson
 * Date: 2016-12-12
 * FIXME
 */
public class OwnerBean implements Serializable
{
    private String userId;

    private String name;

    private String icon;

    private String joinTimeMillisecond;

    private String account;

    public static OwnerBean parse(String jsonString)
    {
        OwnerBean ownerBean = new OwnerBean();
        Gson gson = new Gson();
        try
        {
            ownerBean = gson.fromJson(jsonString, new TypeToken<OwnerBean>()
            {
            }.getType());
        } catch (Exception e)
        {
            String a = gson.fromJson(jsonString, new TypeToken<String>()
            {
            }.getType());
            ownerBean.setUserId(a);
            ownerBean.setName(StringUtils.getPhoneString(a));
        }
        return ownerBean;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getJoinTimeMillisecond()
    {
        return joinTimeMillisecond;
    }

    public void setJoinTimeMillisecond(String joinTimeMillisecond)
    {
        this.joinTimeMillisecond = joinTimeMillisecond;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getIcon()
    {
        return this.icon;
    }
} 