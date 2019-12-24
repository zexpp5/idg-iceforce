package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * Created by selson on 2017/10/12.
 */
public class PicBean implements Serializable
{
    private int length;

    private String localUrl;

    private String name;

    private String remoteUrl;

    private int size;

    public void setLength(int length)
    {
        this.length = length;
    }

    public int getLength()
    {
        return this.length;
    }

    public void setLocalUrl(String localUrl)
    {
        this.localUrl = localUrl;
    }

    public String getLocalUrl()
    {
        return this.localUrl;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public void setRemoteUrl(String remoteUrl)
    {
        this.remoteUrl = remoteUrl;
    }

    public String getRemoteUrl()
    {
        return this.remoteUrl;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getSize()
    {
        return this.size;
    }

}
