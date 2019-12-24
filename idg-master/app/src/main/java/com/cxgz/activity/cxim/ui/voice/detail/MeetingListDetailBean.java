package com.cxgz.activity.cxim.ui.voice.detail;

import java.util.List;

/**
 * Created by selson on 2017/10/26.
 */

public class MeetingListDetailBean
{
    private int status;
    private String msg;
    private List<MeetingVoiceDetail> data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<MeetingVoiceDetail> getData()
    {
        return data;
    }

    public void setData(List<MeetingVoiceDetail> data)
    {
        this.data = data;
    }
}
