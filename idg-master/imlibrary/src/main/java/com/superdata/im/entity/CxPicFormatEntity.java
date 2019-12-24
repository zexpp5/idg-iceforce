package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-05-12
 * FIXME
 */
public class CxPicFormatEntity implements Serializable
{
    private String width;
    private String height;

    @Override
    public String toString()
    {
        return "CxPicFormatEntity{" +
                "width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }

    /**
     * 返回一个实体类，解析json
     */
    public static CxPicFormatEntity parse(String jsonString)
    {
        Gson gson = new Gson();
        CxPicFormatEntity info = gson.fromJson(jsonString, new TypeToken<CxPicFormatEntity>()
        {
        }.getType());
        return info;
    }

    /**
     * 根据实体类返回一串json字符串
     */
    public static String returnJsonString(CxPicFormatEntity picFormat)
    {
        String jsonObjectString = new Gson().toJson(picFormat).toString();
        return jsonObjectString;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }


}