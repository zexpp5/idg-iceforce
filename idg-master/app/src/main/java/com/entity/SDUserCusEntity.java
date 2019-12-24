package com.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

@Table(name = "USER_CUS")
public class SDUserCusEntity extends BaseEntity
{
    /**
     * 内部帐号ID
     */
    @Column(column="USER_ID")
    @NoAutoIncrement
    private long userId;
    /**
     * 圈的类型
     */
    @Column(column = "SPACE_TYPE")
    private int spaceType;
    /**
     * 外部员工帐号ID
     */
    @Column(column = "CUS_USER_ID")
    private long cusUserId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(int spaceType) {
        this.spaceType = spaceType;
    }

    public long getCusUserId() {
        return cusUserId;
    }

    public void setCusUserId(long cusUserId) {
        this.cusUserId = cusUserId;
    }
}
