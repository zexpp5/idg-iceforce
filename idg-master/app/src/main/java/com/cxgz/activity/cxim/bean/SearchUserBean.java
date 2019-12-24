package com.cxgz.activity.cxim.bean;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-06-25
 * FIXME 搜索用户实体类
 */
public class SearchUserBean implements Serializable
{
    private int eid;

    private String email;

    private String icon;

    private String imAccount;

    private String name;

    private String sex;

    private String telephone;

    public void setEid(int eid){
        this.eid = eid;
    }
    public int getEid(){
        return this.eid;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setImAccount(String imAccount){
        this.imAccount = imAccount;
    }
    public String getImAccount(){
        return this.imAccount;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setSex(String sex){
        this.sex = sex;
    }
    public String getSex(){
        return this.sex;
    }
    public void setTelephone(String telephone){
        this.telephone = telephone;
    }
    public String getTelephone(){
        return this.telephone;
    }
}