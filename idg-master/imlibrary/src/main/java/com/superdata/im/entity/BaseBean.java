package com.superdata.im.entity;

import com.chaoxiang.imrestful.callback.EntityCallback;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;

import java.io.Serializable;

/**
 */
public class BaseBean implements Serializable
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
