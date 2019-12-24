package com.entity;

import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;


@Table(name="TRIP")
public class SDTripEntity implements Serializable {
    @Column(column="TITLE")
    private String title;
    @Column(column="ALLDAY")
    private int allday;
    @Column(column="START")
    private String start;
    @Column(column="END")
    private String end;
    @Column(column="PERIOD")
    private int period;
    @Column(column="REMIND")
    private int remind;
    @Column(column="REMARK")
    private String remark;
    @Column(column="USER_ID")
    private long userId;
    @Column(column="COMPANYID")
    private long companyId;
    @Column(column="CREATETIME")
    private String createTime;
    @Id(column="TRIP_ID")
    @NoAutoIncrement
    private long cid;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAllday() {
        return allday;
    }

    public void setAllday(int allday) {
        this.allday = allday;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public int getRemind() {
        return remind;
    }

    public void setRemind(int remind) {
        this.remind = remind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "SDTripEntity{" +
                ", title='" + title + '\'' +
                ", allday=" + allday +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", period=" + period +
                ", remind=" + remind +
                ", remark='" + remark + '\'' +
                ", userId=" + userId +
                ", companyId=" + companyId +
                ", createTime='" + createTime + '\'' +
                ", cid=" + cid +
                '}';
    }
}
