package com.cxgz.activity.cxim.bean;

import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;

/**
 * Created by
 */
public class SDGroup
{
    @Id
    @NoAutoIncrement
    private long groupId;

    //20160520才加上去的
    private String groupID;

    private long time;
    /**
     * 是否结束了此会议
     */
    private boolean isFinish;

    public String getGroupID()
    {
        return groupID;
    }

    public void setGroupID(String groupID)
    {
        this.groupID = groupID;
    }

    public boolean isFinish()
    {
        return isFinish;
    }

    public void setFinish(boolean finish)
    {
        isFinish = finish;
    }

    public long getTime()
    {
        return time;
    }

    public void setTime(long time)
    {
        this.time = time;
    }

    public long getGroupId()
    {
        return groupId;
    }

    public void setGroupId(long groupId)
    {
        this.groupId = groupId;
    }
}
