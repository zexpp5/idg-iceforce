package newProject.company.goout.bean;

import newProject.bean.CCBean;

/**
 * Created by tujingwu on 2017/10/25.
 */

public class GoOutSubmitBean {
    private String eid;
    private String ygId;
    private String ygName;
    private String ygDeptId;
    private String ygDeptName;
    private String ygJob;
    private String approvalPerson;
    private String targetAddress;
    private String vehicles;
    private String startTime;
    private String endTime;
    private String reason;
    private String remark;

    private CCBean cc;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getYgId() {
        return ygId;
    }

    public void setYgId(String ygId) {
        this.ygId = ygId;
    }

    public String getYgName() {
        return ygName;
    }

    public void setYgName(String ygName) {
        this.ygName = ygName;
    }

    public String getYgDeptId() {
        return ygDeptId;
    }

    public void setYgDeptId(String ygDeptId) {
        this.ygDeptId = ygDeptId;
    }

    public String getYgDeptName() {
        return ygDeptName;
    }

    public void setYgDeptName(String ygDeptName) {
        this.ygDeptName = ygDeptName;
    }

    public String getYgJob() {
        return ygJob;
    }

    public void setYgJob(String ygJob) {
        this.ygJob = ygJob;
    }

    public String getApprovalPerson() {
        return approvalPerson;
    }

    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    }

    public String getTargetAddress() {
        return targetAddress;
    }

    public void setTargetAddress(String targetAddress) {
        this.targetAddress = targetAddress;
    }

    public String getVehicles() {
        return vehicles;
    }

    public void setVehicles(String vehicles) {
        this.vehicles = vehicles;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public CCBean getCc() {
        return cc;
    }

    public void setCc(CCBean cc) {
        this.cc = cc;
    }
}
