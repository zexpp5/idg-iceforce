package com.receiver;

import com.cxgz.activity.superqq.bean.SDVersionEntity;

public class SDVersionInfo {

    private int status;
    private SDVersionEntity data;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(SDVersionEntity data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public SDVersionEntity getData() {
        return data;
    }
}
