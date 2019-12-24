package com.cxgz.activity.cxim.ui.business.bean;

import java.io.Serializable;

/**
 * Created by selson on 2017/8/15.
 */

public class LeaveBean implements Serializable
{
    private String name;
    private String remark;
    private String receiveId;
    private String startDate;
    private String endDate;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getReceiveId()
    {
        return receiveId;
    }

    public void setReceiveId(String receiveId)
    {
        this.receiveId = receiveId;
    }

    public String getStartDate()
    {
        return startDate;
    }

    public void setStartDate(String startDate)
    {
        this.startDate = startDate;
    }

    public String getEndDate()
    {
        return endDate;
    }

    public void setEndDate(String endDate)
    {
        this.endDate = endDate;
    }
}
