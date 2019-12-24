package com.chaoxiang.imsdk.group;

import java.io.Serializable;

/**
 * Created by selson on 2017/5/2.
 */

public class PopularizeGroupBean implements Serializable
{
    private PopularizeGroup data;

    private String msg;

    private int status;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(PopularizeGroup data)
    {
        this.data = data;
    }

    public PopularizeGroup getData()
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
