package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-02
 * FIXME
 */
public class CommentBean implements Serializable
{
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

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}