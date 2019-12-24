package com.cxgz.activity.superqq.bean;

import java.io.Serializable;

/**
 * Created by selson on 2018/1/17.
 */
public class HighIconBean implements Serializable
{
    /**
     * data : http://chun.blob.core.chinacloudapi.cn/public/7bfeb9f7e358485e989ae9f51d7406c4.jpg
     * status : 200
     */

    private String data;
    private int status;

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
