package com.cxgz.activity.cxim.http;

/**
 * User: Selson
 * Date: 2016-11-22
 * FIXME
 */
public class AddFriendFilter
{
    //对方的信息
    private String friendImaccount;
    private String friendId;
    private String friendName;
    private String friendIcon;
    private String remark;
    private String createTime;

    public String getFriendImaccount()
    {
        return friendImaccount;
    }

    public void setFriendImaccount(String friendImaccount)
    {
        this.friendImaccount = friendImaccount;
    }

    public String getFriendId()
    {
        return friendId;
    }

    public void setFriendId(String friendId)
    {
        this.friendId = friendId;
    }

    public String getFriendName()
    {
        return friendName;
    }

    public void setFriendName(String friendName)
    {
        this.friendName = friendName;
    }

    public String getFriendIcon()
    {
        return friendIcon;
    }

    public void setFriendIcon(String friendIcon)
    {
        this.friendIcon = friendIcon;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }
}