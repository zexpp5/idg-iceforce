package com.cxgz.activity.cxim.ui.company.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/8/21.
 */

public class DeptBeanList implements Serializable
{

    private List<Data> data;
    private int status;

    public class Data
    {
        private int deptId;
        private String deptName;
        private int num;

        public int getDeptId()
        {
            return deptId;
        }

        public void setDeptId(int deptId)
        {
            this.deptId = deptId;
        }

        public String getDeptName()
        {
            return deptName;
        }

        public void setDeptName(String deptName)
        {
            this.deptName = deptName;
        }

        public int getNum()
        {
            return num;
        }

        public void setNum(int num)
        {
            this.num = num;
        }
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
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
