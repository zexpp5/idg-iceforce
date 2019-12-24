package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-05-12
 * FIXME
 */
public class CxBaiDuEntity implements Serializable
{

    private String longitude;
    private String latitude;
    private String address;

    /**
     * 返回一个实体类，解析json
     */
    public static CxBaiDuEntity parse(String jsonString)
    {
        Gson gson = new Gson();
        CxBaiDuEntity info = gson.fromJson(jsonString, new TypeToken<CxBaiDuEntity>()
        {
        }.getType());
        return info == null ? null : info;
    }

    /**
     * 根据实体类返回一串json字符串
     */
    public static String returnJsonString(CxBaiDuEntity baiduEntity)
    {
        String jsonObjectString = new Gson().toJson(baiduEntity).toString();
        return jsonObjectString;
    }



    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }
}