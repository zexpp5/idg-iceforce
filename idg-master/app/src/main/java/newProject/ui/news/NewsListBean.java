package newProject.ui.news;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/12/28.
 */
public class NewsListBean implements Serializable
{
    private List<Data> data;

    private int page;

    private int pageCount;

    private int status;

    private int total;

    public void setData(List<Data> data)
    {
        this.data = data;
    }

    public List<Data> getData()
    {
        return this.data;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPage()
    {
        return this.page;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getPageCount()
    {
        return this.pageCount;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getStatus()
    {
        return this.status;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public int getTotal()
    {
        return this.total;
    }

    public class Data
    {
        private int eid;

        private int btype;

        private String createTime;

        private String content;

        public void setEid(int eid)
        {
            this.eid = eid;
        }

        public int getEid()
        {
            return this.eid;
        }

        public void setBtype(int btype)
        {
            this.btype = btype;
        }

        public int getBtype()
        {
            return this.btype;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getCreateTime()
        {
            return this.createTime;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }
    }
}
