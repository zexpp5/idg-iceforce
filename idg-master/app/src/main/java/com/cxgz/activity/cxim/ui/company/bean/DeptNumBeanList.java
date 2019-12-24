package com.cxgz.activity.cxim.ui.company.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/8/23.
 */

public class DeptNumBeanList implements Serializable
{

    private List<Data> data;

    public class Data
    {

        private int count;

        private String deptName;

        private long deptId;

        public int getCount()
        {
            return count;
        }

        public void setCount(int count)
        {
            this.count = count;
        }

        public String getDeptName()
        {
            return deptName;
        }

        public void setDeptName(String deptName)
        {
            this.deptName = deptName;
        }

        public long getDeptId()
        {
            return deptId;
        }

        public void setDeptId(long deptId)
        {
            this.deptId = deptId;
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
