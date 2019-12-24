package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.Map;

/**
 * User: Selson
 * Date: 2016-05-12
 * FIXME
 */
public class CxAttachment implements Serializable
{
    //这里还是两个jsonString字符串，再去调用对应的实体类解析
    private String burnAfterReadTime;

    private String location;

    private String isBurnAfterRead;

    private String shareDic;

    private String imageDimensions;

    public void setBurnAfterReadTime(String burnAfterReadTime)
    {
        this.burnAfterReadTime = burnAfterReadTime;
    }

    public String getBurnAfterReadTime()
    {
        return this.burnAfterReadTime;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getLocation()
    {
        return this.location;
    }

    public void setIsBurnAfterRead(String isBurnAfterRead)
    {
        this.isBurnAfterRead = isBurnAfterRead;
    }

    public String getIsBurnAfterRead()
    {
        return this.isBurnAfterRead;
    }

    public void setShareDic(String shareDic)
    {
        this.shareDic = shareDic;
    }

    public String getShareDic()
    {
        return this.shareDic;
    }

    public void setImageDimensions(String imageDimensions)
    {
        this.imageDimensions = imageDimensions;
    }

    public String getImageDimensions()
    {
        return this.imageDimensions;
    }

    /**
     * 返回一个实体类，解析json
     */
    public static CxAttachment parse(String jsonString)
    {
        Gson gson = new Gson();
        CxAttachment info = gson.fromJson(jsonString, new TypeToken<CxAttachment>()
        {
        }.getType());
        return info;
    }

    /**
     * 根据实体类返回一串json字符串
     */
    public static String returnJsonString(CxAttachment cxAttachment)
    {
        String jsonObjectString = new Gson().toJson(cxAttachment).toString();
        return jsonObjectString;
    }

    /**
     * @param jsonString
     * @return 转换为Map形式
     */
    public static Map<String, String> parseMap(String jsonString)
    {
        Gson gson = new Gson();
        Map<String, String> info = gson.fromJson(jsonString, new TypeToken<Map<String, String>>()
        {
        }.getType());
        return info;
    }

    public static String returnJsonStringFromMap(Map<String, String> attachmentMap)
    {
        String jsonObjectString = new Gson().toJson(attachmentMap).toString();
        return jsonObjectString;
    }

}