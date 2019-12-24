package com.cxgz.activity.cxim.workCircle.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-03
 * FIXME
 */
public class RecordListBean implements Serializable
{
    private int bid;

    private int btype;

    private String createTime;

    private int eid;

    private String remark;

    private int userId;

    public void setBid(int bid){
        this.bid = bid;
    }
    public int getBid(){
        return this.bid;
    }
    public void setBtype(int btype){
        this.btype = btype;
    }
    public int getBtype(){
        return this.btype;
    }
    public void setCreateTime(String createTime){
        this.createTime = createTime;
    }
    public String getCreateTime(){
        return this.createTime;
    }
    public void setEid(int eid){
        this.eid = eid;
    }
    public int getEid(){
        return this.eid;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }

}