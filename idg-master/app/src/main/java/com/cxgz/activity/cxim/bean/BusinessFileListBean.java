package com.cxgz.activity.cxim.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-11-15
 * FIXME
 */
public class BusinessFileListBean implements Serializable
{
    private List<BusinessFileBean> data;

    private int status;

    public void setData(List<BusinessFileBean> data)
    {
        this.data = data;
    }

    public List<BusinessFileBean> getData()
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