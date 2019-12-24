package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-01
 * FIXME
 */
public class CircleModeBean implements Serializable
{
    private int btype;

    private String createTime;

    private int eid;

    private int isReder;

    private String name;

    private String send_icon;

    private String send_imAccount;

    private String send_name;

    private int userId;

    private String remark;

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
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
    public void setIsReder(int isReder){
        this.isReder = isReder;
    }
    public int getIsReder(){
        return this.isReder;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSend_icon(String send_icon){
        this.send_icon = send_icon;
    }
    public String getSend_icon(){
        return this.send_icon;
    }
    public void setSend_imAccount(String send_imAccount){
        this.send_imAccount = send_imAccount;
    }
    public String getSend_imAccount(){
        return this.send_imAccount;
    }
    public void setSend_name(String send_name){
        this.send_name = send_name;
    }
    public String getSend_name(){
        return this.send_name;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }

} 