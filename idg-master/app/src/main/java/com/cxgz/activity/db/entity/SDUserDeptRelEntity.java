package com.cxgz.activity.db.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 部门和用户的对应关系
 */
@Table(name = "USER_DEPT_REL")
public class SDUserDeptRelEntity
{
    @Id
    private int id;

    @Column(column = "dpId")
    private String dpId;

    @Column(column = "USER_ID")
    private String userId;

    public String getDpId()
    {
        return dpId;
    }

    public void setDpId(String dpId)
    {
        this.dpId = dpId;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return "SDUserAndDeptRel [id=" + id + ", dpId=" + dpId
                + ", userId=" + userId + "]";
    }
}
