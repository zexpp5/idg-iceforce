package com.entity.update;

/**
 * {
 * "createTime": "2016-06-08",
 * "description": "说明",
 * "status": 200,
 * "type": "1",
 * "urlLink": "www.injony.down",
 * "versionCode": "1.1.2",
 * "versionName": "公测版本",
 * "versionStatus": "1"
 * }
 * Created by cx on 2016/6/24.
 */
public class UpdateEntity
{
    /**
     * data : {"createTime":"2016-12-13","description":"ERP2.5版本已发布，请及时更新！","forgetStatus":"2","type":"2","urlLink":"http://a.app.qq.com/o/simple.jsp?pkgname=com.activity.cxgz.enjoysoho","versionName":"2.5","versionStatus":"2"}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData()
    {
        return data;
    }

    public void setData(DataBean data)
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

    public static class DataBean
    {
        /**
         * createTime : 2016-12-13
         * description : ERP2.5版本已发布，请及时更新！
         * forgetStatus : 2
         * type : 2
         * urlLink : http://a.app.qq.com/o/simple.jsp?pkgname=com.activity.cxgz.enjoysoho
         * versionName : 2.5
         * versionStatus : 2
         */

        private String createTime;

        private String description;

        private String forgetStatus;

        private String isOpen;

        private String type;

        private String urlLink;

        private String versionCode;

        private String versionName;

        private String status;

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getCreateTime()
        {
            return this.createTime;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getDescription()
        {
            return this.description;
        }

        public void setForgetStatus(String forgetStatus)
        {
            this.forgetStatus = forgetStatus;
        }

        public String getForgetStatus()
        {
            return this.forgetStatus;
        }

        public void setIsOpen(String isOpen)
        {
            this.isOpen = isOpen;
        }

        public String getIsOpen()
        {
            return this.isOpen;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getType()
        {
            return this.type;
        }

        public void setUrlLink(String urlLink)
        {
            this.urlLink = urlLink;
        }

        public String getUrlLink()
        {
            return this.urlLink;
        }

        public void setVersionCode(String versionCode)
        {
            this.versionCode = versionCode;
        }

        public String getVersionCode()
        {
            return this.versionCode;
        }

        public void setVersionName(String versionName)
        {
            this.versionName = versionName;
        }

        public String getVersionName()
        {
            return this.versionName;
        }

        public String getStatus()
        {
            return status;
        }

        public void setStatus(String status)
        {
            this.status = status;
        }
    }


//    private String createTime;
//    private String description;
//    private String status;
//    private String  type;
//    private String  urlLink;
//    private int  versionCode;
//    private String versionName;
//    private String versionStatus;
//
//
//    public String getCreateTime() {
//        return createTime;
//    }
//
//    public void setCreateTime(String createTime) {
//        this.createTime = createTime;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//
//    public String getUrlLink() {
//        return urlLink;
//    }
//
//    public void setUrlLink(String urlLink) {
//        this.urlLink = urlLink;
//    }
//
//    public int getVersionCode() {
//        return versionCode;
//    }
//
//    public void setVersionCode(int versionCode) {
//        this.versionCode = versionCode;
//    }
//
//    public String getVersionName() {
//        return versionName;
//    }
//
//    public void setVersionName(String versionName) {
//        this.versionName = versionName;
//    }
//
//    public String getVersionStatus() {
//        return versionStatus;
//    }
//
//    public void setVersionStatus(String versionStatus) {
//        this.versionStatus = versionStatus;
//    }
}
