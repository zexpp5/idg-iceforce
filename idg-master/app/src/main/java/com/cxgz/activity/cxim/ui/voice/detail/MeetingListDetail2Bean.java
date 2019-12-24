package com.cxgz.activity.cxim.ui.voice.detail;

import java.util.List;

/**
 * Created by selson on 2017/10/26.
 */

public class MeetingListDetail2Bean
{

    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        private int forward_limitId;
        private List<MeetingVoiceDetail> data;

        public int getForward_limitId()
        {
            return forward_limitId;
        }

        public void setForward_limitId(int forward_limitId)
        {
            this.forward_limitId = forward_limitId;
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
}
