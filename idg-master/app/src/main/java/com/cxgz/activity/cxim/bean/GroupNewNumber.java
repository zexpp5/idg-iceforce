package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-12
 * FIXME
 */
public class GroupNewNumber implements Serializable
{
    private String userId;

    private String name;

    private String icon;

    private String joinTime;

    private String joinTimeMillisecond;

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getJoinTime()
    {
        return joinTime;
    }

    public void setJoinTime(String joinTime)
    {
        this.joinTime = joinTime;
    }

    public String getJoinTimeMillisecond()
    {
        return joinTimeMillisecond;
    }

    public void setJoinTimeMillisecond(String joinTimeMillisecond)
    {
        this.joinTimeMillisecond = joinTimeMillisecond;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
} 