package com.entity;


import com.cxgz.activity.superqq.bean.AdviceEntity;

import java.util.List;

/**
 * @author 小黎
 * @date 2016/1/26 15:21
 * @des
 */
public class AdviceInfo {
    private int status;
    private String msg;
    private int pageNumber;
    private int total;
    private List<AdviceEntity> datas;

    @Override
    public String toString() {
        return "AdviceInfo{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", pageNumber=" + pageNumber +
                ", total=" + total +
                ", datas=" + datas +
                '}';
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<AdviceEntity> getData() {
        return datas;
    }

    public void setData(List<AdviceEntity> data) {
        this.datas = datas;
    }
}
