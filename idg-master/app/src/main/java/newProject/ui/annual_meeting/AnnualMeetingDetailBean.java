package newProject.ui.annual_meeting;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/1/17.
 */
public class AnnualMeetingDetailBean implements Serializable
{

    /**
     * data : {"detail":{"address":"广州酒家","eid":10,"meetTime":"2018-01-10 16:25:00","remark":"9点入场，11点30开始","title":"2017年会",
     * "year":"2017"},"signList":[{"hignIcon":"http://chun.blob.core.chinacloudapi
     * .cn/public/7bfeb9f7e358485e989ae9f51d7406c4.jpg","icon":"http://chun.blob.core.chinacloudapi
     * .cn/public/6847e43acff44ed281e83145396302fe.png","imAccount":"idg_admin","name":"admin","userId":1}]}
     * msg : 当前没有年会正在进行
     * status : 200
     */

    private DataBean data;
    private String msg;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBean
    {
        /**
         * detail : {"address":"广州酒家","eid":10,"meetTime":"2018-01-10 16:25:00","remark":"9点入场，11点30开始","title":"2017年会",
         * "year":"2017"}
         * signList : [{"hignIcon":"http://chun.blob.core.chinacloudapi.cn/public/7bfeb9f7e358485e989ae9f51d7406c4.jpg",
         * "icon":"http://chun.blob.core.chinacloudapi.cn/public/6847e43acff44ed281e83145396302fe.png","imAccount":"idg_admin",
         * "name":"admin","userId":1}]
         */

        private DetailBean detail;
        private List<SignListBean> signList;

        public DetailBean getDetail()
        {
            return detail;
        }

        public void setDetail(DetailBean detail)
        {
            this.detail = detail;
        }

        public List<SignListBean> getSignList()
        {
            return signList;
        }

        public void setSignList(List<SignListBean> signList)
        {
            this.signList = signList;
        }

        public static class DetailBean
        {
            /**
             * address : 广州酒家
             * eid : 10
             * meetTime : 2018-01-10 16:25:00
             * remark : 9点入场，11点30开始
             * title : 2017年会
             * year : 2017
             */
            private String address;
            private int eid;
            private String meetTime;
            private String remark;
            private String title;
            private String year;
            private String picUrl;

            public String getPicUrl()
            {
                return picUrl;
            }

            public void setPicUrl(String picUrl)
            {
                this.picUrl = picUrl;
            }

            public String getAddress()
            {
                return address;
            }

            public void setAddress(String address)
            {
                this.address = address;
            }

            public int getEid()
            {
                return eid;
            }

            public void setEid(int eid)
            {
                this.eid = eid;
            }

            public String getMeetTime()
            {
                return meetTime;
            }

            public void setMeetTime(String meetTime)
            {
                this.meetTime = meetTime;
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

            public String getYear()
            {
                return year;
            }

            public void setYear(String year)
            {
                this.year = year;
            }
        }

        public static class SignListBean
        {
            /**
             * hignIcon : http://chun.blob.core.chinacloudapi.cn/public/7bfeb9f7e358485e989ae9f51d7406c4.jpg
             * icon : http://chun.blob.core.chinacloudapi.cn/public/6847e43acff44ed281e83145396302fe.png
             * imAccount : idg_admin
             * name : admin
             * userId : 1
             */

            private String hignIcon;
            private String icon;
            private String imAccount;
            private String name;
            private int userId;

            public String getHignIcon()
            {
                return hignIcon;
            }

            public void setHignIcon(String hignIcon)
            {
                this.hignIcon = hignIcon;
            }

            public String getIcon()
            {
                return icon;
            }

            public void setIcon(String icon)
            {
                this.icon = icon;
            }

            public String getImAccount()
            {
                return imAccount;
            }

            public void setImAccount(String imAccount)
            {
                this.imAccount = imAccount;
            }

            public String getName()
            {
                return name;
            }

            public void setName(String name)
            {
                this.name = name;
            }

            public int getUserId()
            {
                return userId;
            }

            public void setUserId(int userId)
            {
                this.userId = userId;
            }
        }
    }
}
