package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-11-15
 * FIXME
 */
public class BusinessFileBean implements Serializable
{
    private int count;

    private String fileType;

    public void setCount(int count)
    {
        this.count = count;
    }

    public int getCount()
    {
        return this.count;
    }

    public void setFileType(String fileType)
    {
        this.fileType = fileType;
    }

    public String getFileType()
    {
        return this.fileType;
    }

} 