package newProject.company.borrowapplay.bean;

import newProject.bean.CCBean;

/**
 * Created by tujingwu on 2017/10/23.
 */

public class BoorowSubmitBean {
    private String eid;
    private String ygId;
    private String ygName;
    private String ygDeptId;
    private String ygDeptName;
    private String ygJob;
    private String title;
    private String remark;
    private String money;
    private String bigMoney;
    private String currencyValue;
    private String approvalPerson;
    private CCBean cc;

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getBigMoney() {
        return bigMoney;
    }

    public void setBigMoney(String bigMoney) {
        this.bigMoney = bigMoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCurrencyValue() {
        return currencyValue;
    }

    public void setCurrencyValue(String currencyValue) {
        this.currencyValue = currencyValue;
    }

    public String getApprovalPerson() {
        return approvalPerson;
    }

    public void setApprovalPerson(String approvalPerson) {
        this.approvalPerson = approvalPerson;
    }

    public CCBean getCc() {
        return cc;
    }

    public void setCc(CCBean cc) {
        this.cc = cc;
    }
}
