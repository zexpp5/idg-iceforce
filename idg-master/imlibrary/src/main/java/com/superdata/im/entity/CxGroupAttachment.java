package com.superdata.im.entity;

import java.util.HashMap;

/**
 * User: Selson
 * Date: 2016-05-24
 * FIXME
 */
public class CxGroupAttachment
{
    private static CxGroupAttachment instance;
    private HashMap<String, String> map;

    public CxGroupAttachment()
    {

    }

    public synchronized static CxGroupAttachment getInstance()
    {
        if (instance == null)
        {
            instance = new CxGroupAttachment();
        }
        return instance;
    }

    //封装成一个json字符串。
    public void getJsonFromMap(HashMap<String, String> map)
    {

    }
}