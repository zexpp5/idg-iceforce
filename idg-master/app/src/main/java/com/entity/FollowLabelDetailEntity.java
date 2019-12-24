package com.entity;

/**
 * Created by injoy-pc on 2016/4/28.
 */
public class FollowLabelDetailEntity {


    /**
     * userConcern : {"companyId":1,"concernCount":26,"id":12,"name":"四大皆空","userId":35}
     * userIds : [1,29,30,31,32,33,34,35,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,56,57]
     */

    private FollowLabelDetailInfoEntity datas;
    /**
     * datas : {"userConcern":{"companyId":1,"concernCount":26,"id":12,"name":"四大皆空","userId":35},"userIds":[1,29,30,31,32,33,34,35,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,56,57]}
     * status : 200
     */

    private int status;

    public FollowLabelDetailInfoEntity getDatas() {
        return datas;
    }

    public void setDatas(FollowLabelDetailInfoEntity datas) {
        this.datas = datas;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FollowLabelDetailEntity{" +
                "datas=" + datas +
                ", status=" + status +
                '}';
    }
}
