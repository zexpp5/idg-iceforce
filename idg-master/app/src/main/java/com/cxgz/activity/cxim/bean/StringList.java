package com.cxgz.activity.cxim.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/8/21.
 */

public class StringList implements Serializable
{

    private List<Data> data;

    public static class Data
    {
        private int id;

        private String name;

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }
    }

    public List<Data> getData()
    {
        return data;
    }

    public void setData(List<Data> data)
    {
        this.data = data;
    }
}
