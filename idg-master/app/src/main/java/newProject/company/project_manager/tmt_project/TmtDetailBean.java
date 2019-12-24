package newProject.company.project_manager.tmt_project;

/**
 * Created by Administrator on 2018/2/27.
 */

public class TmtDetailBean {

    /**
     * data : {"domain":"教育","feedback":null,"followUpPersonName":null,"indu":"O2O","investGroup":"华平资本，元生资本","money":"1.2亿美元","pastFinancing":"2014年12月获得 青松基金数百万元的天使轮投资，2015年7月获得 顺为资本、青松基金2000万人民币的A轮投资，2016年3月获得 达晨创投、顺为资本数千万人民币的B轮投资，2016年9月获得 华晟资本-华兴资本、顺为资本、达晨创投亿元及以上人民币的C轮投资，2017年7月获得StarVC亿元及以上人民币的C+轮投资，2017年12月获得 华平投资、元生资本1.2亿美元的D轮投资。","projId":15288,"projName":"掌门1对1(翼师网络)","teamDesc":"张翼，掌门1对1创始人、CEO。曾在麦肯锡实习和工作。","zhDesc":"掌门1对1（前身是掌门新锐）是一家为初高中学生提供在线1对1教育的平台，尤其主打高端专业的1对1订制化教学服务。上海翼师网络科技有限公司旗下网站。"}
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
         * domain : 教育
         * feedback : null
         * followUpPersonName : null
         * indu : O2O
         * investGroup : 华平资本，元生资本
         * money : 1.2亿美元
         * pastFinancing : 2014年12月获得 青松基金数百万元的天使轮投资，2015年7月获得 顺为资本、青松基金2000万人民币的A轮投资，2016年3月获得 达晨创投、顺为资本数千万人民币的B轮投资，2016年9月获得 华晟资本-华兴资本、顺为资本、达晨创投亿元及以上人民币的C轮投资，2017年7月获得StarVC亿元及以上人民币的C+轮投资，2017年12月获得 华平投资、元生资本1.2亿美元的D轮投资。
         * projId : 15288
         * projName : 掌门1对1(翼师网络)
         * teamDesc : 张翼，掌门1对1创始人、CEO。曾在麦肯锡实习和工作。
         * zhDesc : 掌门1对1（前身是掌门新锐）是一家为初高中学生提供在线1对1教育的平台，尤其主打高端专业的1对1订制化教学服务。上海翼师网络科技有限公司旗下网站。
         */

        private String domain;
        private String feedback;
        private String followUpPersonName;
        private String indu;
        private String investGroup;
        private String money;
        private String pastFinancing;
        private int projId;
        private String projName;
        private String teamDesc;
        private String zhDesc;

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }

        public String getFollowUpPersonName() {
            return followUpPersonName;
        }

        public void setFollowUpPersonName(String followUpPersonName) {
            this.followUpPersonName = followUpPersonName;
        }

        public String getIndu() {
            return indu;
        }

        public void setIndu(String indu) {
            this.indu = indu;
        }

        public String getInvestGroup() {
            return investGroup;
        }

        public void setInvestGroup(String investGroup) {
            this.investGroup = investGroup;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getPastFinancing() {
            return pastFinancing;
        }

        public void setPastFinancing(String pastFinancing) {
            this.pastFinancing = pastFinancing;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getTeamDesc() {
            return teamDesc;
        }

        public void setTeamDesc(String teamDesc) {
            this.teamDesc = teamDesc;
        }

        public String getZhDesc() {
            return zhDesc;
        }

        public void setZhDesc(String zhDesc) {
            this.zhDesc = zhDesc;
        }
    }
}
