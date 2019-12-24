package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 */
@Table(name = "PUSH_UNREAD")
public class SDUnreadEntity
{
    @Id(column = "menuId")
    @NoAutoIncrement
    private int menuId;

    @Column(column = "btype")
    private int btype;

    @Column(column = "pid")
    private int pid;
    //
    @Column(column = "type")
    private int type;

    //审批code
    @Column(column = "menuCode")
    private String menuCode;

    //菜单名字
    @Column(column = "name")
    private String name;

    //二级名字
    @Column(column = "menuName")
    private String menuName;

    //推送type
    @Column(column = "pushType")
    private String pushType;

    //推送人
    @Column(column = "fromAccount")
    private String fromAccount;

    //推送的内容
    @Column(column = "msg")
    private String msg;

    //消息ID
    @Column(column = "messageId")
    private String messageId;

    //未读条数
    @Column(column = "unreadCount")
    private int unreadCount;

    //是否已读
    @Column(column = "isRead")
    private boolean isRead;

    //备注
    @Column(column = "remark")
    private String remark;

    @Override
    public String toString()
    {
        return "SDUnreadEntity{" +
                "menuId=" + menuId +
                ", btype=" + btype +
                ", pid=" + pid +
                ", type=" + type +
                ", menuCode='" + menuCode + '\'' +
                ", name='" + name + '\'' +
                ", menuName='" + menuName + '\'' +
                ", pushType='" + pushType + '\'' +
                ", fromAccount='" + fromAccount + '\'' +
                ", msg='" + msg + '\'' +
                ", messageId='" + messageId + '\'' +
                ", unreadCount=" + unreadCount +
                ", isRead=" + isRead +
                ", remark='" + remark + '\'' +
                '}';
    }

    public int getBtype()
    {
        return btype;
    }

    public void setBtype(int btype)
    {
        this.btype = btype;
    }

    public int getMenuId()
    {
        return menuId;
    }

    public void setMenuId(int menuId)
    {
        this.menuId = menuId;
    }

    public int getPid()
    {
        return pid;
    }

    public void setPid(int pid)
    {
        this.pid = pid;
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMenuName()
    {
        return menuName;
    }

    public void setMenuName(String menuName)
    {
        this.menuName = menuName;
    }

    public String getPushType()
    {
        return pushType;
    }

    public void setPushType(String pushType)
    {
        this.pushType = pushType;
    }

    public String getFromAccount()
    {
        return fromAccount;
    }

    public void setFromAccount(String fromAccount)
    {
        this.fromAccount = fromAccount;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMessageId()
    {
        return messageId;
    }

    public void setMessageId(String messageId)
    {
        this.messageId = messageId;
    }

    public int getUnreadCount()
    {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount)
    {
        this.unreadCount = unreadCount;
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

}
