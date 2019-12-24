package com.entity;

/**
 * Created by leo on 15-10-13.
 */
public class SDTripDetilInfo {
    private int status;
    private SDTripEntity data;
    private String msg;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SDTripEntity getData() {
        return data;
    }

    public void setData(SDTripEntity data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "SDTripDetilInfo{" +
                "status=" + status +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }
}
