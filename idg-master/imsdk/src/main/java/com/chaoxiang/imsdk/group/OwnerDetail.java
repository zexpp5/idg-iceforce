package com.chaoxiang.imsdk.group;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-12-12
 * FIXME
 */
public class OwnerDetail implements Serializable
{
    private String icon;

    private int joinTimeMillisecond;

    private String userId;

    private String name;

    public void setIcon(String icon){
        this.icon = icon;
    }
    public String getIcon(){
        return this.icon;
    }
    public void setJoinTimeMillisecond(int joinTimeMillisecond){
        this.joinTimeMillisecond = joinTimeMillisecond;
    }
    public int getJoinTimeMillisecond(){
        return this.joinTimeMillisecond;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }
    public String getUserId(){
        return this.userId;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
} 