package newProject.company.announce;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */
public class AnnounceListBean implements Serializable
{
    /**
     * data : [{"cover":"http://chun.blob.core.chinacloudapi.cn/public/e96850ef227e479eb177a671356fb8f8.jpg","createId":1,
     * "createTime":"2018-06-26 17:14:35","eid":9,
     * "remark":"啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊","title":"测试_2018"},{"cover":"http://chun.blob.core.chinacloudapi.cn/public/fd431fa8b4d0498ba6e2691f6c7b0b3b.jpg","createId":1,"createTime":"2018-06-25 11:31:17","eid":5,"remark":"这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试这是一条通知公告的测试","title":"通知公告测试"},{"cover":"","createId":1,"createTime":"2018-06-25 12:41:43","eid":7,"remark":"这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这是一条没图片的这","title":"测试的没图"},{"cover":"http://chun.blob.core.chinacloudapi.cn/public/b9ff9dd35ed143ee84adb99729b8d071.jpg","createId":1,"createTime":"2018-06-11 14:41:56","eid":1,"remark":"test","title":"测试2"},{"cover":"http://chun.blob.core.chinacloudapi.cn/public/c352798d0c104c2f811bc9532193fe6a.jpg","createId":1,"createTime":"2018-06-22 21:00:21","eid":3,"remark":"test","title":"测试_1"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 5
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

    public static class DataBean implements MultiItemEntity
    {
        /**
         *
         * cover : http://chun.blob.core.chinacloudapi.cn/public/e96850ef227e479eb177a671356fb8f8.jpg
         * createId : 1
         * createTime : 2018-06-26 17:14:35
         * eid : 9
         * remark :
         * 啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊阿啊啊啊啊啊啊啊啊啊啊
         * title : 测试_2018
         */
        private int itemType;
        private String cover;
        private int createId;
        private String createTime;
        private int eid;
        private String remark;
        private String title;

        public int getItemType()
        {
            return itemType;
        }

        public void setItemType(int itemType)
        {
            this.itemType = itemType;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public int getCreateId()
        {
            return createId;
        }

        public void setCreateId(int createId)
        {
            this.createId = createId;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public int getEid()
        {
            return eid;
        }

        public void setEid(int eid)
        {
            this.eid = eid;
        }

        public String getRemark()
        {
            return remark;
        }

        public void setRemark(String remark)
        {
            this.remark = remark;
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
