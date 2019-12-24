package com.chaoxiang.imsdk.group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-12-13
 * FIXME
 */
public class RemoveMemberBean implements Serializable
{
    private int status;
    
    private long joinTime;

    private String groupId;

    private List<String> members;

    public static RemoveMemberBean parse(String jsonString)
    {
        RemoveMemberBean removeMemberBean = new RemoveMemberBean();
        removeMemberBean.members = new ArrayList<String>();
        Gson gson = new Gson();
        removeMemberBean = gson.fromJson(jsonString, new TypeToken<RemoveMemberBean>()
        {
        }.getType());
        return removeMemberBean;
    }


    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }

    public void setJoinTime(long joinTime)
    {
        this.joinTime = joinTime;
    }

    public long getJoinTime()
    {
        return this.joinTime;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupId()
    {
        return this.groupId;
    }

    public void setString(List<String> members)
    {
        this.members = members;
    }

    public List<String> getString()
    {
        return this.members;
    }
} 