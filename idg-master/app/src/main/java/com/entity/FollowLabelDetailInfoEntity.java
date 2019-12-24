package com.entity;

import java.util.List;

/**
 * Created by injoy-pc on 2016/4/28.
 */
public class FollowLabelDetailInfoEntity {
    private FollowLableListEntity userConcern;
    private List<Integer> userIds;

    public FollowLableListEntity getUserConcern() {
        return userConcern;
    }

    public void setUserConcern(FollowLableListEntity userConcern) {
        this.userConcern = userConcern;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    @Override
    public String toString() {
        return "FollowLabelDetailInfoEntity{" +
                "userConcern=" + userConcern +
                ", userIds=" + userIds +
                '}';
    }
}
