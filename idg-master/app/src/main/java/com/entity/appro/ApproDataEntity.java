package com.entity.appro;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/5/12.
 */
public class ApproDataEntity implements Serializable {
    private List<AnnexListEntity> annexList;
    private  long approvalId;
    private String approvalStatus;
    private String approvalTime;
    private  long bId;
    private String isAgree;
    private String isOver;
    private String money;
    private long recordId;
    private String remark;
    private String userName;

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    private String totalMoney;

    /**
     * ID : 135
     * approvalName : ceshi016
     * dpId : 2
     * dpName : 销售部
     * rDpCode : dp_xs
     * taskId : 589
     */

    private int ID;
    private String approvalName;
    private int dpId;
    private String dpName;
    private String rDpCode;
    private int taskId;

    @Override
    public String toString() {
        return "ApproDataEntity{" +
                "annexList=" + annexList +
                ", approvalId=" + approvalId +
                ", approvalStatus='" + approvalStatus + '\'' +
                ", approvalTime='" + approvalTime + '\'' +
                ", bId=" + bId +
                ", isAgree='" + isAgree + '\'' +
                ", isOver='" + isOver + '\'' +
                ", money='" + money + '\'' +
                ", recordId=" + recordId +
                ", remark='" + remark + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<AnnexListEntity> getAnnexList() {
        return annexList;
    }

    public void setAnnexList(List<AnnexListEntity> annexList) {
        this.annexList = annexList;
    }

    public long getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(long approvalId) {
        this.approvalId = approvalId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(String approvalTime) {
        this.approvalTime = approvalTime;
    }

    public long getbId() {
        return bId;
    }

    public void setbId(long bId) {
        this.bId = bId;
    }

    public String getIsAgree() {
        return isAgree;
    }

    public void setIsAgree(String isAgree) {
        this.isAgree = isAgree;
    }

    public String getIsOver() {
        return isOver;
    }

    public void setIsOver(String isOver) {
        this.isOver = isOver;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public long getRecordId() {
        return recordId;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getApprovalName() {
        return approvalName;
    }

    public void setApprovalName(String approvalName) {
        this.approvalName = approvalName;
    }

    public int getDpId() {
        return dpId;
    }

    public void setDpId(int dpId) {
        this.dpId = dpId;
    }

    public String getDpName() {
        return dpName;
    }

    public void setDpName(String dpName) {
        this.dpName = dpName;
    }

    public String getRDpCode() {
        return rDpCode;
    }

    public void setRDpCode(String rDpCode) {
        this.rDpCode = rDpCode;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}
