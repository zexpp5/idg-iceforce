package com.chaoxiang.imsdk.group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.superdata.im.entity.Members;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-12-13
 * FIXME 正式使用的情况下。
 */
public class RemoveMemberFormalBean implements Serializable
{
    private int status;

    private long joinTime;

    private String groupId;

    private List<Members> members;

    public static RemoveMemberFormalBean parse(String jsonString)
    {
        RemoveMemberFormalBean removeMemberBean = new RemoveMemberFormalBean();
        removeMemberBean.members = new ArrayList<Members>();
        Gson gson = new Gson();
        removeMemberBean = gson.fromJson(jsonString, new TypeToken<RemoveMemberFormalBean>()
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

    public void setString(List<Members> members)
    {
        this.members = members;
    }

    public List<Members> getString()
    {
        return this.members;
    }
} 