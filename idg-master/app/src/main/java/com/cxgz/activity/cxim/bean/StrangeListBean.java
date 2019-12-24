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
public class StrangeListBean implements Serializable
{
    private List<StrangeBean> data;

    private int status;

    public static StrangeListBean parse(String jsonString)
    {
        StrangeListBean bean = new StrangeListBean();
        Gson gson = new Gson();
        bean.data = new ArrayList<StrangeBean>();
        bean = gson.fromJson(jsonString, new TypeToken<StrangeListBean>()
        {
        }.getType());
        return bean;
    }

    public void setData(List<StrangeBean> data)
    {
        this.data = data;
    }

    public List<StrangeBean> getData()
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