package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class BaseInforBean {

    /**
     * data : {"business":"创始人","city":null,"comPhaseName":"成长期","contributors":null,"induName":"Media / entertainment","invRoundName":"Pre-IPO","projDDManagerName":"潘盼","projGroupName":"文化创意组","projLawyerName":"谢一岚","projManagerName":"董丁","projSourName":"同事介绍","projSourPer":"熊晓鸽","projStageName":"合伙人周会讨论","inDate":"2017-10-11","teamName":["储雯漪","曲芳韵"]}
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
         * business : 创始人
         * city : null
         * comPhaseName : 成长期
         * contributors : null
         * induName : Media / entertainment
         * invRoundName : Pre-IPO
         * projDDManagerName : 潘盼
         * projGroupName : 文化创意组
         * projLawyerName : 谢一岚
         * projManagerName : 董丁
         * projSourName : 同事介绍
         * projSourPer : 熊晓鸽
         * projStageName : 合伙人周会讨论
         * inDate : 2017-10-11
         * teamName : ["储雯漪","曲芳韵"]
         */

        private String business;
        private String city;
        private String comPhaseName;
        private List<String> contributors;
        private String induName;
        private String invRoundName;
        private String projDDManagerName;
        private String projGroupName;
        private String projLawyerName;
        private String projManagerName;
        private String projSourName;
        private String projSourPer;
        private String projStageName;
        private String inDate;
        private List<String> teamName;

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getComPhaseName() {
            return comPhaseName;
        }

        public void setComPhaseName(String comPhaseName) {
            this.comPhaseName = comPhaseName;
        }

        public List<String> getContributors() {
            return contributors;
        }

        public void setContributors(List<String> contributors) {
            this.contributors = contributors;
        }

        public String getInduName() {
            return induName;
        }

        public void setInduName(String induName) {
            this.induName = induName;
        }

        public String getInvRoundName() {
            return invRoundName;
        }

        public void setInvRoundName(String invRoundName) {
            this.invRoundName = invRoundName;
        }

        public String getProjDDManagerName() {
            return projDDManagerName;
        }

        public void setProjDDManagerName(String projDDManagerName) {
            this.projDDManagerName = projDDManagerName;
        }

        public String getProjGroupName() {
            return projGroupName;
        }

        public void setProjGroupName(String projGroupName) {
            this.projGroupName = projGroupName;
        }

        public String getProjLawyerName() {
            return projLawyerName;
        }

        public void setProjLawyerName(String projLawyerName) {
            this.projLawyerName = projLawyerName;
        }

        public String getProjManagerName() {
            return projManagerName;
        }

        public void setProjManagerName(String projManagerName) {
            this.projManagerName = projManagerName;
        }

        public String getProjSourName() {
            return projSourName;
        }

        public void setProjSourName(String projSourName) {
            this.projSourName = projSourName;
        }

        public String getProjSourPer() {
            return projSourPer;
        }

        public void setProjSourPer(String projSourPer) {
            this.projSourPer = projSourPer;
        }

        public String getProjStageName() {
            return projStageName;
        }

        public void setProjStageName(String projStageName) {
            this.projStageName = projStageName;
        }

        public String getInDate() {
            return inDate;
        }

        public void setInDate(String inDate) {
            this.inDate = inDate;
        }

        public List<String> getTeamName() {
            return teamName;
        }

        public void setTeamName(List<String> teamName) {
            this.teamName = teamName;
        }
    }
}
