package com.cxgz.activity.cxim.bean;


import java.io.Serializable;

/**
 * a-z排序的基类
 */
public class SDSortEntity implements Serializable
{
    /**
     * 真实姓名首字母
     */
    protected String header;

    public String getHeader()
    {
        return header;
    }

    public void setHeader(String header)
    {
        this.header = header;
    }
}
