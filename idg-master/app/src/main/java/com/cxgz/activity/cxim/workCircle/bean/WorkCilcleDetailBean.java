package com.cxgz.activity.cxim.workCircle.bean;

import com.entity.administrative.employee.Annexdata;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-12-03
 * FIXME
 */
public class WorkCilcleDetailBean implements Serializable
{

    private List<Annexdata> annexList ;

    private int btype;

    private String createTime;

    private int eid;

    private String endDate;

    private String name;

    private int receiveId;

    private List<RecordListBean> recordList ;

    private String remark;

    private String startDate;

    private int userId;

    private double money;

    public double getMoney()
    {
        return money;
    }

    public void setMoney(double money)
    {
        this.money = money;
    }

    public void setAnnexList(List<Annexdata> annexList){
        this.annexList = annexList;
    }
    public List<Annexdata> getAnnexList(){
        return this.annexList;
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
    public void setEndDate(String endDate){
        this.endDate = endDate;
    }
    public String getEndDate(){
        return this.endDate;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setReceiveId(int receiveId){
        this.receiveId = receiveId;
    }
    public int getReceiveId(){
        return this.receiveId;
    }
    public void setRecordList(List<RecordListBean> recordList){
        this.recordList = recordList;
    }
    public List<RecordListBean> getRecordList(){
        return this.recordList;
    }
    public void setRemark(String remark){
        this.remark = remark;
    }
    public String getRemark(){
        return this.remark;
    }
    public void setStartDate(String startDate){
        this.startDate = startDate;
    }
    public String getStartDate(){
        return this.startDate;
    }
    public void setUserId(int userId){
        this.userId = userId;
    }
    public int getUserId(){
        return this.userId;
    }

}