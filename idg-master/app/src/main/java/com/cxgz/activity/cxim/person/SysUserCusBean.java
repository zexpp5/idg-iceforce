package com.cxgz.activity.cxim.person;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-06-22
 * FIXME
 */
public class SysUserCusBean implements Serializable
{
    //msg=1,是好友，2，不是好友

    private boolean data;

    private int status;

    public void setData(boolean data){
        this.data = data;
    }
    public boolean getData(){
        return this.data;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public int getStatus(){
        return this.status;
    }

} 