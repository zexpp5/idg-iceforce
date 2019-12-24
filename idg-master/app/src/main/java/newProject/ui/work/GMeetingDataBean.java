package newProject.ui.work;

import java.io.Serializable;

/**
 * Created by selson on 2017/11/28.
 */

public class GMeetingDataBean implements Serializable
{
    private int eid;

    private String endTime;

    private String meetPlace;

    private String startTime;

    private String title;

    private String startTimeKey;

    private int sortColumn;

    private String type;

    private int typeNum;

    private String remark;

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public int getSortColumn()
    {
        return sortColumn;
    }

    public void setSortColumn(int sortColumn)
    {
        this.sortColumn = sortColumn;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public int getTypeNum()
    {
        return typeNum;
    }

    public void setTypeNum(int typeNum)
    {
        this.typeNum = typeNum;
    }

    public String getStartTimeKey()
    {
        return startTimeKey;
    }

    public void setStartTimeKey(String startTimeKey)
    {
        this.startTimeKey = startTimeKey;
    }

    public void setEid(int eid)
    {
        this.eid = eid;
    }

    public int getEid()
    {
        return this.eid;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return this.endTime;
    }

    public void setMeetPlace(String meetPlace)
    {
        this.meetPlace = meetPlace;
    }

    public String getMeetPlace()
    {
        return this.meetPlace;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getStartTime()
    {
        return this.startTime;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }
}
