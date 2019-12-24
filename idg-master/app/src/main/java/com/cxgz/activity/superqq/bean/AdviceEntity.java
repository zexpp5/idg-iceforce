package com.cxgz.activity.superqq.bean;

import java.io.Serializable;

/**
 * @des
 */
public class AdviceEntity implements Serializable
{
    private long id;
    private long userId;
    private long companyId;
    private String title;//标题
    private String content;//反馈内容
    private String createTime;//提交时间
    private String replyContent;//处理内容
    private String replyTime;//处理时间
    private String name;//建议标题
    private String remark;//建议内容
    private String userName;//提交用户名
    private String replyUserName;//回复人用户名
    private String replyUserId;//回复人ID
    private int status;//处理状态

    @Override
    public String toString()
    {
        return "AdviceEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", companyId=" + companyId +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createTime='" + createTime + '\'' +
                ", replyContent='" + replyContent + '\'' +
                ", replyTime='" + replyTime + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", userName='" + userName + '\'' +
                ", replyUserName='" + replyUserName + '\'' +
                ", replyUserId='" + replyUserId + '\'' +
                ", status=" + status +
                '}';
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public long getUserId()
    {
        return userId;
    }

    public void setUserId(long userId)
    {
        this.userId = userId;
    }

    public long getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(long companyId)
    {
        this.companyId = companyId;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getReplyContent()
    {
        return replyContent;
    }

    public void setReplyContent(String replyContent)
    {
        this.replyContent = replyContent;
    }

    public String getReplyTime()
    {
        return replyTime;
    }

    public void setReplyTime(String replyTime)
    {
        this.replyTime = replyTime;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getReplyUserName()
    {
        return replyUserName;
    }

    public void setReplyUserName(String replyUserName)
    {
        this.replyUserName = replyUserName;
    }

    public String getReplyUserId()
    {
        return replyUserId;
    }

    public void setReplyUserId(String replyUserId)
    {
        this.replyUserId = replyUserId;
    }
}
