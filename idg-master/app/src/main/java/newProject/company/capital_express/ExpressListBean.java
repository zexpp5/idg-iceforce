package newProject.company.capital_express;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/22.
 */

public class ExpressListBean implements Serializable
{
    /**
     * data : [{"createTime":"2018-06-25 19:00:14","digest":"打赌你猜不到他的pick。","mid":"2b2ed29a67dba26f13018e8fe7ed4221",
     * "thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcvqgvwYbASc1Cicr7Tw7kuak1VuAJAe5zvolyIwnd4jTDicYtGchwGd5DDKLLeDXXs5OraQbT3gEMVg/0?wx_fmt=jpeg",
     * "thumb_url_height":720,"thumb_url_width":1280,"title":"IDG资本连盟对话喜茶聂云宸：请回答，你的偶像pick谁？","updateTime":"2018-06-26
     * 18:02:46","url":"http://mp.weixin.qq
     * .com/s?__biz=MzA5OTE3ODUyOA==&mid=503479476&idx=1&sn=2b2ed29a67dba26f13018e8fe7ed4221&chksm
     * =0b709dfb3c0714ed8bcea9fc497c95f0247cedc211bf5721b9db9ac53ee068001ae2db1599bb#rd"},{"createTime":"2018-06-25 19:00:14",
     * "digest":"给你一口好烟。","mid":"4d580f5103a4b2675b25671a78794741","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcvhNGmxlXFf5qb88fVfFZE8BcVibhtpps36xz89Rxjmfp8xOOIEtUTX1OqQZQdLFPpjukKBTUxuBRg/0?wx_fmt=jpeg",
     * "thumb_url_height":421,"thumb_url_width":747,"title":"IDG Family | RELX悦刻电子烟完成天使轮3800万人民币融资","updateTime":"2018-06-26
     * 18:02:46","url":"http://mp.weixin.qq
     * .com/s?__biz=MzA5OTE3ODUyOA==&mid=503479476&idx=2&sn=4d580f5103a4b2675b25671a78794741&chksm
     * =0b709dfb3c0714ed10aca24ceef78c64620479abc8ba517b7c17488cbc78e978b7647dbe5b77#rd"},{"createTime":"2018-06-24 16:57:03",
     * "digest":"卢卡库：他们再也不会查我的身份证了。","mid":"37dda780764521a7f031aca4d18bed1d","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcvpuPEicEDocZtic0V6AKHWkicCiatGNBsndDmq0o4sJFibTfEVavmFDic5zfhaBX6G332Ohk0SCfT7IpQQ/0?wx_fmt=jpeg
     * ","thumb_url_height":605,"thumb_url_width":1073,"title":"昨晚除了德国队和101，还有一名最有意志力的球员","updateTime":"2018-06-24 21:03:40",
     * "url":"http://mp.weixin.qq.com/s?__biz=MzA5OTE3ODUyOA==&mid=503479473&idx=1&sn=37dda780764521a7f031aca4d18bed1d&chksm
     * =0b709dfe3c0714e84acf516171c8ca19cc1aea6942df2a230cb9fab8b25433c75b3764a3f394#rd"},{"createTime":"2018-06-22 16:44:55",
     * "digest":"佛系蹦迪了解一下？","mid":"3eea6c3b7ccd9b074400ea2fac47e43a","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAct1GMt2tcMRRSrLoaX8PRdnZs2Y5vGwTSQ2EXwpEBEBL8lBOlzywCW2vWtZRVGUQEbxfWicdzicvlEA/0?wx_fmt=jpeg",
     * "thumb_url_height":606,"thumb_url_width":1080,"title":"电音和尚：50岁，他在寺庙里活成了一名DJ","updateTime":"2018-06-22 18:00:23",
     * "url":"http://mp.weixin.qq.com/s?__biz=MzA5OTE3ODUyOA==&mid=503479469&idx=1&sn=3eea6c3b7ccd9b074400ea2fac47e43a&chksm
     * =0b709de23c0714f45b9aa8d62a95dc3f3d32f56fcb4bd5d8b2d2d38f2d625fc33a1da6f8fb68#rd"},{"createTime":"2018-06-20 10:58:37",
     * "digest":"重新定义\u201c社群电商\u201d。","mid":"8d1b06270c3e576c417e72b0fb3ebb28","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcvwUzLG9OVc7tOaPwNhqZ61poOPff33CG2vGKYPnT2nlznOfFgmkXFy8psEHVuRUkHUI0tt57uQww/0?wx_fmt=jpeg",
     * "thumb_url_height":500,"thumb_url_width":888,"title":"IDG Family | IDG资本领投社群电商\u201c好衣库\u201dA轮融资",
     * "updateTime":"2018-06-20 12:31:51","url":"http://mp.weixin.qq
     * .com/s?__biz=MzA5OTE3ODUyOA==&mid=503479462&idx=1&sn=8d1b06270c3e576c417e72b0fb3ebb28&chksm
     * =0b709de93c0714ff7851688e3e5b69b96e2bff274bd47ec6aa8f047b11a5d50a4653c4d8d132#rd"},{"createTime":"2018-06-20 10:58:37",
     * "digest":"儿童思维训练了解一下？","mid":"a90ac2c4cf3e0fab68f4a2e3be5fedbc","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcvwUzLG9OVc7tOaPwNhqZ61Gk5Mibq0ENhxrRRBu2aLjd5iaJR6Pd1DauFYSYUlWartHSx63c9uUqJg/0?wx_fmt=jpeg",
     * "thumb_url_height":583,"thumb_url_width":582,"title":"IDG Family | 儿童思维训练平台\u201c火花思维\u201d宣布获得B+轮融资",
     * "updateTime":"2018-06-20 12:31:51","url":"http://mp.weixin.qq
     * .com/s?__biz=MzA5OTE3ODUyOA==&mid=503479462&idx=2&sn=a90ac2c4cf3e0fab68f4a2e3be5fedbc&chksm
     * =0b709de93c0714ff222805f47e6d25a5a617b2a802457e8294f1ebb1c73999aeeee9ca99ffe8#rd"},{"createTime":"2018-06-19 17:45:20",
     * "digest":"你将如何选择？","mid":"f850020b74440e20167dbb798c354699","thumb_url":"http://mmbiz.qpic
     * .cn/mmbiz_jpg/tmkZVANzAcsXBCDY4t4RficBnNvbDgE7DEDIHaNPqnXYMS0DB7JhoibKDiaxCr4WsZZtfeS2sRadN530gmsqWlvWg/0?wx_fmt=jpeg",
     * "thumb_url_height":319,"thumb_url_width":567,"title":"如果一生只能投资 5 个项目\u2026\u2026","updateTime":"2018-06-19 19:09:21",
     * "url":"http://mp.weixin.qq.com/s?__biz=MzA5OTE3ODUyOA==&mid=503479450&idx=1&sn=f850020b74440e20167dbb798c354699&chksm
     * =0b709dd53c0714c37b678e1f7852a5e400630b4bae10a2c341e52a12d3f2e17502e28df4ae99#rd"}]
     * page : 1
     * pageCount : 267
     * status : 200
     * total : 534
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
         * createTime : 2018-06-25 19:00:14
         * digest : 打赌你猜不到他的pick。
         * mid : 2b2ed29a67dba26f13018e8fe7ed4221
         * thumb_url : http://mmbiz.qpic
         * .cn/mmbiz_jpg/tmkZVANzAcvqgvwYbASc1Cicr7Tw7kuak1VuAJAe5zvolyIwnd4jTDicYtGchwGd5DDKLLeDXXs5OraQbT3gEMVg/0?wx_fmt=jpeg
         * thumb_url_height : 720
         * thumb_url_width : 1280
         * title : IDG资本连盟对话喜茶聂云宸：请回答，你的偶像pick谁？
         * updateTime : 2018-06-26 18:02:46
         * url : http://mp.weixin.qq.com/s?__biz=MzA5OTE3ODUyOA==&mid=503479476&idx=1&sn=2b2ed29a67dba26f13018e8fe7ed4221&chksm
         * =0b709dfb3c0714ed8bcea9fc497c95f0247cedc211bf5721b9db9ac53ee068001ae2db1599bb#rd
         */
        private int itemType;
        private String createTime;
        private String digest;
        private String mid;
        private String thumb_url;
        private int thumb_url_height;
        private int thumb_url_width;
        private String title;
        private String updateTime;
        private String url;

        public int getItemType()
        {
            return itemType;
        }

        public void setItemType(int itemType)
        {
            this.itemType = itemType;
        }

        public String getCreateTime()
        {
            return createTime;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getDigest()
        {
            return digest;
        }

        public void setDigest(String digest)
        {
            this.digest = digest;
        }

        public String getMid()
        {
            return mid;
        }

        public void setMid(String mid)
        {
            this.mid = mid;
        }

        public String getThumb_url()
        {
            return thumb_url;
        }

        public void setThumb_url(String thumb_url)
        {
            this.thumb_url = thumb_url;
        }

        public int getThumb_url_height()
        {
            return thumb_url_height;
        }

        public void setThumb_url_height(int thumb_url_height)
        {
            this.thumb_url_height = thumb_url_height;
        }

        public int getThumb_url_width()
        {
            return thumb_url_width;
        }

        public void setThumb_url_width(int thumb_url_width)
        {
            this.thumb_url_width = thumb_url_width;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getUpdateTime()
        {
            return updateTime;
        }

        public void setUpdateTime(String updateTime)
        {
            this.updateTime = updateTime;
        }

        public String getUrl()
        {
            return url;
        }

        public void setUrl(String url)
        {
            this.url = url;
        }
    }
}
