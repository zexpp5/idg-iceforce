package com.cxgz.activity.cxim.ui.voice;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2017/10/25.
 */

public class MeetingDetailBean implements Serializable
{
    private DataBean data;
    private int status;
    private String msg;

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public void setData(DataBean data)
    {
        this.data = data;
    }

    public DataBean getData()
    {
        return data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public class DataBean implements Serializable
    {
        private VedioMeetBean vedioMeet;
        private List<CcListBean> ccList;

        public VedioMeetBean getVedioMeet()
        {
            return vedioMeet;
        }

        public void setVedioMeet(VedioMeetBean vedioMeet)
        {
            this.vedioMeet = vedioMeet;
        }

        public List<CcListBean> getCcList()
        {
            return ccList;
        }

        public void setCcList(List<CcListBean> ccList)
        {
            this.ccList = ccList;
        }

        public class VedioMeetBean implements Serializable
        {
            /**
             * createTime : 2017-10-25
             * eid : 29
             * icon :
             * isEnd : 1
             * serNo : VM1710250001
             * title : 语音会议2
             * ygDeptId : 54
             * ygDeptName : 未归类
             * ygId : 38
             * ygJob : 系统管理员
             * ygName : 18012345601
             */

            private String createTime;
            private long eid;
            private String icon;
            private int isEnd;
            private String serNo;
            private String title;
            private long ygDeptId;
            private String ygDeptName;
            private long ygId;
            private String ygJob;
            private String ygName;

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

            public String getIcon()
            {
                return icon;
            }

            public void setIcon(String icon)
            {
                this.icon = icon;
            }

            public int getIsEnd()
            {
                return isEnd;
            }

            public void setIsEnd(int isEnd)
            {
                this.isEnd = isEnd;
            }

            public String getSerNo()
            {
                return serNo;
            }

            public void setSerNo(String serNo)
            {
                this.serNo = serNo;
            }

            public String getTitle()
            {
                return title;
            }

            public void setTitle(String title)
            {
                this.title = title;
            }

            public long getYgDeptId()
            {
                return ygDeptId;
            }

            public void setYgDeptId(long ygDeptId)
            {
                this.ygDeptId = ygDeptId;
            }

            public String getYgDeptName()
            {
                return ygDeptName;
            }

            public void setYgDeptName(String ygDeptName)
            {
                this.ygDeptName = ygDeptName;
            }

            public long getYgId()
            {
                return ygId;
            }

            public void setYgId(long ygId)
            {
                this.ygId = ygId;
            }

            public String getYgJob()
            {
                return ygJob;
            }

            public void setYgJob(String ygJob)
            {
                this.ygJob = ygJob;
            }

            public String getYgName()
            {
                return ygName;
            }

            public void setYgName(String ygName)
            {
                this.ygName = ygName;
            }
        }

        public class CcListBean implements Serializable
        {
            /**
             * userId : 55
             * userName : 永夜君王
             */
            private long userId;
            private String userName;
            private String imAccount;

            public String getImAccount()
            {
                return imAccount;
            }

            public void setImAccount(String imAccount)
            {
                this.imAccount = imAccount;
            }

            public long getUserId()
            {
                return userId;
            }

            public void setUserId(long userId)
            {
                this.userId = userId;
            }

            public String getUserName()
            {
                return userName;
            }

            public void setUserName(String userName)
            {
                this.userName = userName;
            }
        }
    }
}
