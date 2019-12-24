package com.cxgz.activity.db.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/5/31.
 */
public class SDuserInfo implements Serializable {
    private int status;
    List<SDUserEntity> users;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<SDUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<SDUserEntity> users) {
        this.users = users;
    }
}
