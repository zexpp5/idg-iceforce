package newProject.bean;

/**
 * Created by Administrator on 2017/11/10.
 */

public class SuperConfigDetailBean {


    /**
     * data : {"androidLogo":"","companyId":103,"companyName":"羊爱上狼","createTime":"2017-11-10 11:24:06.0","eid":26,"fingerprintLogin":1,"iosLogo1":"","iosLogo2":"","iosLogo3":"","iosLogo4":"","isRead":2,"location":2,"logo":"","maxMessage":1002,"platformName":"喜洋洋与灰太狼","updateTime":"2017-11-10 11:24:06.0"}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * androidLogo :
         * companyId : 103
         * companyName : 羊爱上狼
         * createTime : 2017-11-10 11:24:06.0
         * eid : 26
         * fingerprintLogin : 1
         * iosLogo1 :
         * iosLogo2 :
         * iosLogo3 :
         * iosLogo4 :
         * isRead : 2
         * location : 2
         * logo :
         * maxMessage : 1002
         * platformName : 喜洋洋与灰太狼
         * updateTime : 2017-11-10 11:24:06.0
         */

        private String androidLogo;
        private int companyId;
        private String companyName;
        private String createTime;
        private int eid;
        private int fingerprintLogin;
        private String iosLogo1;
        private String iosLogo2;
        private String iosLogo3;
        private String iosLogo4;
        private int isRead;
        private int location;
        private String logo;
        private int maxMessage;
        private String platformName;
        private String updateTime;

        public String getAndroidLogo() {
            return androidLogo;
        }

        public void setAndroidLogo(String androidLogo) {
            this.androidLogo = androidLogo;
        }

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public int getFingerprintLogin() {
            return fingerprintLogin;
        }

        public void setFingerprintLogin(int fingerprintLogin) {
            this.fingerprintLogin = fingerprintLogin;
        }

        public String getIosLogo1() {
            return iosLogo1;
        }

        public void setIosLogo1(String iosLogo1) {
            this.iosLogo1 = iosLogo1;
        }

        public String getIosLogo2() {
            return iosLogo2;
        }

        public void setIosLogo2(String iosLogo2) {
            this.iosLogo2 = iosLogo2;
        }

        public String getIosLogo3() {
            return iosLogo3;
        }

        public void setIosLogo3(String iosLogo3) {
            this.iosLogo3 = iosLogo3;
        }

        public String getIosLogo4() {
            return iosLogo4;
        }

        public void setIosLogo4(String iosLogo4) {
            this.iosLogo4 = iosLogo4;
        }

        public int getIsRead() {
            return isRead;
        }

        public void setIsRead(int isRead) {
            this.isRead = isRead;
        }

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public int getMaxMessage() {
            return maxMessage;
        }

        public void setMaxMessage(int maxMessage) {
            this.maxMessage = maxMessage;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
