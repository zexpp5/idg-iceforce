package com.cxgz.activity.cxim.ui.voice.detail;

import java.io.Serializable;

/**
 * Created by selson on 2017/10/26.
 */

public class MeetingVoiceDetail implements Serializable
{
    /**
     * content : 内容1
     * deptName : 未归类
     * eid : 1
     * icon :
     * job : 系统管理员
     * name : yesido
     * type : 1
     * ygId : 8
     */
    private String content;
    private String deptName;
    private long eid;
    private String icon;
    private String job;
    private String name;
    private int type;
    private String ygId;
    private double length;
    private int unRead;
    private boolean playStatus;

    public boolean isPlayStatus()
    {
        return playStatus;
    }

    public void setPlayStatus(boolean playStatus)
    {
        this.playStatus = playStatus;
    }

    public int getUnRead()
    {
        return unRead;
    }

    public void setUnRead(int unRead)
    {
        this.unRead = unRead;
    }

    public double getLength()
    {
        return length;
    }

    public void setLength(double length)
    {
        this.length = length;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public long getEid()
    {
        return eid;
    }

    public void setEid(long eid)
    {
        this.eid = eid;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getYgId()
    {
        return ygId;
    }

    public void setYgId(String ygId)
    {
        this.ygId = ygId;
    }
}
