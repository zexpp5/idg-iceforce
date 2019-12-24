package com.superdata.im.entity;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * @des
 */
public class PushModelEntity
{
    private int count;
    private Map<String, String> data;

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public Map<String, String> getData()
    {
        return data;
    }

    public void setData(Map<String, String> data)
    {
        this.data = data;
    }
}
