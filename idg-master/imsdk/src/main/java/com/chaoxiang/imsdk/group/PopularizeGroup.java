package com.chaoxiang.imsdk.group;

import com.superdata.im.entity.Members;

import java.io.Serializable;
import java.util.List;

/**
 * User: Selson
 * Date: 2017-05-02
 * FIXME
 */
public class PopularizeGroup implements Serializable
{
    private String groupId;

    private String groupName;

    private String groupType;

    private List<Members> members;

    private String msg;

    private String owner;

    private boolean result;

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getGroupId()
    {
        return this.groupId;
    }

    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return this.groupName;
    }

    public void setGroupType(String groupType)
    {
        this.groupType = groupType;
    }

    public String getGroupType()
    {
        return this.groupType;
    }

    public void setMembers(List<Members> members)
    {
        this.members = members;
    }

    public List<Members> getMembers()
    {
        return this.members;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getMsg()
    {
        return this.msg;
    }

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public void setResult(boolean result)
    {
        this.result = result;
    }

    public boolean getResult()
    {
        return this.result;
    }
} 