package com.bean;

import java.io.Serializable;

/**
 * Created by selson on 2018/9/20.
 */

public class UnReadNumBean implements Serializable
{
    /**
     * data : 100
     * status : 200
     */
    private int data;
    private int status;

    public int getData()
    {
        return data;
    }

    public void setData(int data)
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
