package com.cxgz.activity.cxim.ui.voice;

import java.io.Serializable;

/**
 * Created by selson on 2017/10/24.
 */

public class MeetingAddFilter implements Serializable
{
    private String ygId;
    private String ygName;
    private String ygDeptId;
    private String ygDeptName;
    private String ygJob;
    private String title;
    private String cc;

    public String getYgId()
    {
        return ygId;
    }

    public void setYgId(String ygId)
    {
        this.ygId = ygId;
    }

    public String getYgName()
    {
        return ygName;
    }

    public void setYgName(String ygName)
    {
        this.ygName = ygName;
    }

    public String getYgDeptId()
    {
        return ygDeptId;
    }

    public void setYgDeptId(String ygDeptId)
    {
        this.ygDeptId = ygDeptId;
    }

    public String getYgDeptName()
    {
        return ygDeptName;
    }

    public void setYgDeptName(String ygDeptName)
    {
        this.ygDeptName = ygDeptName;
    }

    public String getYgJob()
    {
        return ygJob;
    }

    public void setYgJob(String ygJob)
    {
        this.ygJob = ygJob;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getCc()
    {
        return cc;
    }

    public void setCc(String cc)
    {
        this.cc = cc;
    }

    @Override
    public String toString()
    {
        return "MeetingAddFilter{" +
                "ygId='" + ygId + '\'' +
                ", ygName='" + ygName + '\'' +
                ", ygDeptId='" + ygDeptId + '\'' +
                ", ygDeptName='" + ygDeptName + '\'' +
                ", ygJob='" + ygJob + '\'' +
                ", title='" + title + '\'' +
                ", cc='" + cc + '\'' +
                '}';
    }
}
