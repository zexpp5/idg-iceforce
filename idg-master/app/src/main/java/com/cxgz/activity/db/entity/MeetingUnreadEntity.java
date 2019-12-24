package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 */
@Table(name = "MEETING_UNREAD")
public class MeetingUnreadEntity
{

    @Id(column = "eid")
    @NoAutoIncrement
    private int eid;

    @Column(column = "unreadId")
    private String unreadId;

    @Column(column = "menuId")
    private int menuId;

    @Column(column = "btype")
    private int btype;

    //
    @Column(column = "type")
    private int type;

    //审批code
    @Column(column = "menuCode")
    private String menuCode;

    //未读
    @Column(column = "unreadCount")
    private int unreadCount;

    @Column(column = "userId")
    private String userId;

    //是否已读
    @Column(column = "isRead")
    private boolean isRead;

    //备注
    @Column(column = "remark")
    private String remark;

    //备注2
    @Column(column = "remark2")
    private String remark2;


    public int getEid()
    {
        return eid;
    }

    public void setEid(int eid)
    {
        this.eid = eid;
    }

    public String getUnreadId()
    {
        return unreadId;
    }

    public void setUnreadId(String unreadId)
    {
        this.unreadId = unreadId;
    }

    public int getMenuId()
    {
        return menuId;
    }

    public void setMenuId(int menuId)
    {
        this.menuId = menuId;
    }

    public int getBtype()
    {
        return btype;
    }

    public void setBtype(int btype)
    {
        this.btype = btype;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getMenuCode()
    {
        return menuCode;
    }

    public void setMenuCode(String menuCode)
    {
        this.menuCode = menuCode;
    }

    public int getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount)
    {
        this.unreadCount = unreadCount;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public boolean isRead()
    {
        return isRead;
    }

    public void setRead(boolean read)
    {
        isRead = read;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    @Override
    public String toString()
    {
        return "MeetingUnreadEntity{" +
                "unreadId='" + unreadId + '\'' +
                ", menuId=" + menuId +
                ", btype=" + btype +
                ", eid=" + eid +
                ", type=" + type +
                ", menuCode='" + menuCode + '\'' +
                ", unreadCount=" + unreadCount +
                ", userId='" + userId + '\'' +
                ", isRead=" + isRead +
                ", remark='" + remark + '\'' +
                '}';
    }
}
