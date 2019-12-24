package com.bean_erp;

import com.cxgz.activity.db.dao.SDUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-11-18
 * FIXME
 */
public class LoginListBean2 implements Serializable
{
    private List<SDUserEntity> contacts;    //部门人员列表 通讯录用这个列表的

    private List<SDUserEntity> allContacts; //部门人员列表（全部人员

    private int status;

    public List<SDUserEntity> getContacts()
    {
        return contacts;
    }

    public void setContacts(List<SDUserEntity> contacts)
    {
        this.contacts = contacts;
    }

    public List<SDUserEntity> getAllContacts()
    {
        return allContacts;
    }

    public void setAllContacts(List<SDUserEntity> allContacts)
    {
        this.allContacts = allContacts;
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