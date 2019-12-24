package com.cxgz.activity.cxim.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2017-04-28
 * FIXME
 */
public class TuiGuangListBean implements Serializable
{
    private TuiGuangBean data;
    private int status;

    public static TuiGuangListBean parse(String jsonString)
    {
        TuiGuangListBean bean = new TuiGuangListBean();
        Gson gson = new Gson();
        bean = gson.fromJson(jsonString, new TypeToken<TuiGuangBean>()
        {
        }.getType());
        return bean;
    }

    public TuiGuangBean getData()
    {
        return data;
    }

    public void setData(TuiGuangBean data)
    {
        this.data = data;
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