package com.cxgz.activity.cxim.bean;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-06-25
 * FIXME 搜索用户实体类
 */
public class SearchUserListBean implements Serializable
{
    private List<SearchUserBean> data;

    private int status;

    public void setData(List<SearchUserBean> data)
    {
        this.data = data;
    }

    public List<SearchUserBean> getData()
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