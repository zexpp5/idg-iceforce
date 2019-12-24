package yunjing.processor.eventbus;

/**
 * 弹幕
 */
public class UnReadDanmu
{
    public boolean isShow;

    public String icon;
    public String msg;
    public String from;
    public String create;
    public String type;
    public String btype;
    public String meetId;

    public UnReadDanmu()
    {
    }

    public UnReadDanmu(boolean isShow, String icon, String msg, String from, String create, String type, String btype, String
            meetId)
    {
        this.isShow = isShow;
        this.icon = icon;
        this.msg = msg;
        this.from = from;
        this.create = create;
        this.type = type;
        this.btype = btype;
        this.meetId = meetId;
    }

    public String getMeetId()
    {
        return meetId;
    }

    public void setMeetId(String meetId)
    {
        this.meetId = meetId;
    }

    public boolean isShow()
    {
        return isShow;
    }

    public void setShow(boolean show)
    {
        isShow = show;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getCreate()
    {
        return create;
    }

    public void setCreate(String create)
    {
        this.create = create;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getBtype()
    {
        return btype;
    }

    public void setBtype(String btype)
    {
        this.btype = btype;
    }
}

