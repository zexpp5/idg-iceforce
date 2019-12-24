package com.cxgz.activity.cxim.workCircle.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-12
 * FIXME
 */
public class UpdateImgBgBean implements Serializable
{
    private String data;

    private int status;

    public static UpdateImgBgBean parse(String jsonString)
    {
        UpdateImgBgBean data = new UpdateImgBgBean();
        Gson gson = new Gson();
        data = gson.fromJson(jsonString, new TypeToken<UpdateImgBgBean>(){}.getType());
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

    public String getData()
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