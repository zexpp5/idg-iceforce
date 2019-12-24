package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

/**
 *
 */
@Table(name = "PUSH_UNREAD")
public class UnreadEntity {
    @Id(column = "UNREAD_ID")
    @NoAutoIncrement
    private String unreadId;
    @Column(column = "TYPE")
    private int type;
    @Column(column = "UNREADCOUNT")
    private int unreadCount;
    @Column(column="USER_ID")
    private String userId;
    //是否已读
    @Column(column="IS_READ")
    private boolean isRead;
    //备注
    @Column(column="REMARK")
    private String remark;

    //属于哪个模块，比如我的办公，公告，对应(PlushModeEntity)
    @Column(column="PALCE_INDEX")
    private int placeIndex;

    //属于哪个工作模块，比如研发工作，电商工作
    @Column(column="WORK_MENU")
    private int workMenu;



    public UnreadEntity(int type, int unreadCount, String userId) {
        this.unreadId = userId+type;
        this.type = type;
        this.unreadCount = unreadCount;
        this.userId = userId;
    }

    public UnreadEntity( int type, int unreadCount, String userId, boolean isRead,int placeIndex,int workMenu) {
        this.unreadId = userId+type;
        this.type = type;
        this.unreadCount = unreadCount;
        this.userId = userId;
        this.isRead = isRead;
        this.placeIndex=placeIndex;
        this.workMenu=workMenu;
    }

    public int getWorkMenu() {
        return workMenu;
    }

    public void setWorkMenu(int workMenu) {
        this.workMenu = workMenu;
    }

    public int getPlaceIndex() {
        return placeIndex;
    }

    public void setPlaceIndex(int placeIndex) {
        this.placeIndex = placeIndex;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


    public UnreadEntity() {

    }

    @Override
    public String toString() {
        return "UnreadEntity{" +
                "unreadId='" + unreadId + '\'' +
                ", type=" + type +
                ", unreadCount=" + unreadCount +
                ", userId='" + userId + '\'' +
                ", isRead=" + isRead +
                '}';
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getUnreadId() {
        return unreadId;
    }

    public void setUnreadId(String unreadId) {
        this.unreadId = unreadId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
