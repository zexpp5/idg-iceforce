package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-11-22
 * FIXME
 */
public class PersonalListBean implements Serializable
{
    private PersonInfoBean data;

    private int status;

    public void setData(PersonInfoBean data)
    {
        this.data = data;
    }

    public PersonInfoBean getData()
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