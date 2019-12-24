package com.cxgz.activity.cxim.bean;


import com.chaoxiang.entity.group.IMGroup;

/**
 * @date 2016-03-03
 * @desc
 */
public class Group extends SDSortEntity
{
    private IMGroup group;
    private String icon;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public IMGroup getGroup()
    {
        return group;
    }

    public void setGroup(IMGroup group)
    {
        this.group = group;
    }
}
