package com.entity;

import java.util.List;

/**
 * Created by leo on 15-10-9.
 */
public class SDTtripInfo {
    private int status;
    private List<SDTripEntity> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SDTripEntity> getData() {
        return data;
    }

    public void setData(List<SDTripEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "SDTtripInfo{" +
                "status=" + status +
                ", data=" + data +
                '}';
    }
}
