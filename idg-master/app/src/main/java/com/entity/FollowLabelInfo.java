package com.entity;

import java.util.List;

/**
 * Created by injoy-pc on 2016/4/28.
 */
public class FollowLabelInfo {

    /**
     * datas : [{"companyId":1,"concernCount":3,"id":1,"name":"asdhasdzxcbasdg","userId":34},{"companyId":1,"concernCount":3,"id":2,"name":"和客户发回国vk","userId":35},{"companyId":1,"concernCount":2,"id":3,"name":"一会GIF咖啡","userId":35},{"companyId":1,"concernCount":5,"id":4,"name":"的可开发量大家风范家","userId":37}]
     * pageNumber : 1
     * status : 200
     * total : 4
     */

    private int pageNumber;
    private int status;
    private int total;
    /**
     * companyId : 1
     * concernCount : 3
     * id : 1
     * name : asdhasdzxcbasdg
     * userId : 34
     */

    private List<FollowLableListEntity> datas;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FollowLableListEntity> getDatas() {
        return datas;
    }

    public void setDatas(List<FollowLableListEntity> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "FollowLabelInfo{" +
                "pageNumber=" + pageNumber +
                ", status=" + status +
                ", total=" + total +
                ", datas=" + datas +
                '}';
    }
}
