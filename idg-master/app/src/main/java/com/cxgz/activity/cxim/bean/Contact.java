package com.cxgz.activity.cxim.bean;


import com.chaoxiang.entity.pushuser.IMUser;

/**
 * @desc
 */
public class Contact extends SDSortEntity
{
    private IMUser user;
    private String icon;

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public IMUser getUser()
    {
        return user;
    }

    public void setUser(IMUser user)
    {
        this.user = user;
    }
}
