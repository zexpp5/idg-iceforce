package com.receiver;

import com.entity.SDFileListEntity;

import java.util.List;

/**
 * Created by Administrator on 2015/7/2.
 */
public class AllmyInfo {
    private int status;
    private int pageNumber;
    private int total;

    private List<SDFileListEntity> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public List<SDFileListEntity> getData() {
        return data;
    }

    public void setData(List<SDFileListEntity> data) {
        this.data = data;
    }
}
