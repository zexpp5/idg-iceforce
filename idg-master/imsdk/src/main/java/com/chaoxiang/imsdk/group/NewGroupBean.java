package com.chaoxiang.imsdk.group;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.superdata.im.entity.Members;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-12-12
 * FIXME
 */
public class NewGroupBean implements Serializable
{
    private String createTime;

    private long createTimeMillisecond;

    private String updateTime;

    private long updateTimeMillisecond;

    private int groupType;

    private String groupId;

    private String groupName;

    private OwnerDetail ownerDetail;

    private int status;

    private long joinTime;

    private String owner;

    private String companyId;

    private List<Members> members;

    private String version;

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public long getCreateTimeMillisecond()
    {
        return createTimeMillisecond;
    }

    public void setCreateTimeMillisecond(long createTimeMillisecond)
    {
        this.createTimeMillisecond = createTimeMillisecond;
    }

    public String getUpdateTime()
    {
        return updateTime;
    }

    public void setUpdateTime(String updateTime)
    {
        this.updateTime = updateTime;
    }

    public long getUpdateTimeMillisecond()
    {
        return updateTimeMillisecond;
    }

    public void setUpdateTimeMillisecond(long updateTimeMillisecond)
    {
        this.updateTimeMillisecond = updateTimeMillisecond;
    }

    public void setGroupType(int groupType)
    {
        this.groupType = groupType;
    }

    public int getGroupType()
    {
        return this.groupType;
    }

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

    public void setOwnerDetail(OwnerDetail ownerDetail)
    {
        this.ownerDetail = ownerDetail;
    }

    public OwnerDetail getOwnerDetail()
    {
        return this.ownerDetail;
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

    public void setOwner(String owner)
    {
        this.owner = owner;
    }

    public String getOwner()
    {
        return this.owner;
    }

    public void setCompanyId(String companyId)
    {
        this.companyId = companyId;
    }

    public String getCompanyId()
    {
        return this.companyId;
    }

    public void setMembers(List<Members> members)
    {
        this.members = members;
    }

    public List<Members> getMembers()
    {
        return this.members;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getVersion()
    {
        return this.version;
    }

    public static NewGroupBean parseNewGroupBean(String jsonString)
    {
        NewGroupBean newGroupBean = new NewGroupBean();
        newGroupBean.members = new ArrayList<Members>();
        newGroupBean.ownerDetail = new OwnerDetail();
        Gson gson = new Gson();
        newGroupBean = gson.fromJson(jsonString, new TypeToken<NewGroupBean>()
        {
        }.getType());
        return newGroupBean;
    }
}