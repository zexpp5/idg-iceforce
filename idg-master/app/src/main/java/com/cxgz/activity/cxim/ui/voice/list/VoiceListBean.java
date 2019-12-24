package com.cxgz.activity.cxim.ui.voice.list;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/8/21.
 */

public class VoiceListBean implements Serializable
{

    /**
     * data : [{"count":2,"createTime":"04:00 PM","eid":6,"title":"关于接口进度"},{"count":2,"createTime":"03:49 PM","eid":4,
     * "title":"会议议题"},{"count":0,"createTime":"03:07 PM","eid":3,"title":"会议议题"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 3
     */
    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * count : 2
         * createTime : 04:00 PM
         * eid : 6
         * title : 关于接口进度
         */
        private int count;
        private String createTime;
        private long eid;
        private String title;
        private String icon;
        private String useId;

        public String getUseId()
        {
            return useId;
        }

        public void setUseId(String useId)
        {
            this.useId = useId;
        }

        public String getIcon()
        {
            return icon;
        }

        public void setIcon(String icon)
        {
            this.icon = icon;
        }

        public int getCount()
        {
            return count;
        }

        public void setCount(int count)
        {
            this.count = count;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public long getEid()
        {
            return eid;
        }

        public void setEid(long eid)
        {
            this.eid = eid;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }
    }
}
