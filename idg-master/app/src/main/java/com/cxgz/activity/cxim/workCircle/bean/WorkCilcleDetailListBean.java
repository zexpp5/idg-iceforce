package com.cxgz.activity.cxim.workCircle.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-03
 * FIXME
 */
public class WorkCilcleDetailListBean implements Serializable
{
    private WorkCilcleDetailBean data;

    private int status;

    public void setData(WorkCilcleDetailBean data)
    {
        this.data = data;
    }

    public WorkCilcleDetailBean getData()
    {
        return this.data;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }

} 