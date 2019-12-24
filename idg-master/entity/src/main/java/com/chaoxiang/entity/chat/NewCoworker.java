package com.chaoxiang.entity.chat;

import java.io.Serializable;

/**
 * Created by selson on 2017/9/7.
 */
public class NewCoworker implements Serializable
{
    private int deptId;

    private String deptName;

    private String from;

    private String icon;

    private String imAccount;

    private String job;

    private String joinTime;

    private String name;

    private String type;

    public void setDeptId(int deptId)
    {
        this.deptId = deptId;
    }

    public int getDeptId()
    {
        return this.deptId;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return this.deptName;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getFrom()
    {
        return this.from;
    }

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

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getJob()
    {
        return this.job;
    }

    public void setJoinTime(String joinTime)
    {
        this.joinTime = joinTime;
    }

    public String getJoinTime()
    {
        return this.joinTime;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
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
