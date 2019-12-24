package com.bean_erp;

import com.cxgz.activity.db.dao.SDAllConstactsEntity;
import com.cxgz.activity.db.dao.SDUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-11-18
 * FIXME
 */
public class LoginListBean implements Serializable
{
    private List<SDUserEntity> data;

    private List<SDAllConstactsEntity> allContacts;

    private int status;

    public List<SDAllConstactsEntity> getAllContacts()
    {
        return allContacts;
    }

    public void setAllContacts(List<SDAllConstactsEntity> allContacts)
    {
        this.allContacts = allContacts;
    }

    public void setData(List<SDUserEntity> data)
    {
        this.data = data;
    }

    public List<SDUserEntity> getData()
    {
        return this.data;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }
} 