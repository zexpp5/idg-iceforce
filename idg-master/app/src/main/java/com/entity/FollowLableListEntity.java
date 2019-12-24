package com.entity;

/**
 * Created by injoy-pc on 2016/4/28.
 */
public class FollowLableListEntity {

    /**
     * companyId : 1
     * concernCount : 3
     * id : 1
     * name : asdhasdzxcbasdg
     * userId : 34
     */

    private long companyId;
    private int concernCount;
    private int id;
    private String name;
    private String userId;

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public int getConcernCount() {
        return concernCount;
    }

    public void setConcernCount(int concernCount) {
        this.concernCount = concernCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "FollowLableListEntity{" +
                "companyId=" + companyId +
                ", concernCount=" + concernCount +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }
}
