package com.cxgz.activity.cxim.search;

/**
 * User: Selson
 * Date: 2016-08-08
 * FIXME
 */
public class SearchInfo
{
    private String name;
    private String message;
    private String date;   //预留 时间
    private String id; //预留  各种信息的ID
    private String icon;
    private String localIcon;

    private String from;
    private String to;

    private String singleId;
    private String groupId;


    private String type;//标识是单聊还是群聊 1是单聊 2是群聊 3是语音会议

    public SearchInfo()
    {
    }

    public SearchInfo(String name, String message, String date, String id)
    {
        this.name = name;
        this.message = message;
        this.date = date;
        this.id = id;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getTo()
    {
        return to;
    }

    public void setTo(String to)
    {
        this.to = to;
    }

    public String getSingleId()
    {
        return singleId;
    }

    public void setSingleId(String singleId)
    {
        this.singleId = singleId;
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getLocalIcon()
    {
        return localIcon;
    }

    public void setLocalIcon(String localIcon)
    {
        this.localIcon = localIcon;
    }
}