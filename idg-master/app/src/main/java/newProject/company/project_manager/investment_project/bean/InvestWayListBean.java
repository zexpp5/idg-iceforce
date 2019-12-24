package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/18.
 */

public class InvestWayListBean {


    /**
     * data : [{"amt":"","approvedFlag":"是","currency":"","planDate":"2017-12-14","planDesc":"post-moneyUS$85亿，仅跟投；\npost-moneyUS$40亿，我们可领投；\npost-moneyUS$30亿，可全部额度投资；\n","planId":144425,"status":"新项目通报"},{"amt":"","approvedFlag":"是","currency":"","planDate":"2017-12-13","planDesc":"拟按post-money US$85亿估值融$US8.5亿，占10%。12月20日签署协议预付10%定金，1月31日交割。","planId":144423,"status":"新项目通报"},{"amt":"","approvedFlag":"否","currency":"","planDate":"2017-11-10","planDesc":"按post-money US$ 85亿估值，报US$3-5亿额度，最终额度根据实际情况调整。但要求用人民币投资。","planId":144427,"status":"新项目通报"},{"amt":"200","approvedFlag":"否","currency":"USD","planDate":"2017-11-06","planDesc":"拟按post-money (asking US$10B,但offering US$ 8.5B)估值融US$1B，占股10%-11.76%，如领投需 US$200M。","planId":144097,"status":"新项目通报"}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * amt :
         * approvedFlag : 是
         * currency :
         * planDate : 2017-12-14
         * planDesc : post-moneyUS$85亿，仅跟投；
         post-moneyUS$40亿，我们可领投；
         post-moneyUS$30亿，可全部额度投资；

         * planId : 144425
         * status : 新项目通报
         */

        private String amt;
        private String approvedFlag;
        private String currency;
        private String planDate;
        private String planDesc;
        private int planId;
        private String status;
        private String approvedBy;
        private String approvedByName;
        private List<String> teamName;
        private String editByName;

        public String getEditByName() {
            return editByName;
        }

        public void setEditByName(String editByName) {
            this.editByName = editByName;
        }

        public String getApprovedBy() {
            return approvedBy;
        }

        public void setApprovedBy(String approvedBy) {
            this.approvedBy = approvedBy;
        }

        public String getApprovedByName() {
            return approvedByName;
        }

        public void setApprovedByName(String approvedByName) {
            this.approvedByName = approvedByName;
        }

        public List<String> getTeamName() {
            return teamName;
        }

        public void setTeamName(List<String> teamName) {
            this.teamName = teamName;
        }

        public String getAmt() {
            return amt;
        }

        public void setAmt(String amt) {
            this.amt = amt;
        }

        public String getApprovedFlag() {
            return approvedFlag;
        }

        public void setApprovedFlag(String approvedFlag) {
            this.approvedFlag = approvedFlag;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getPlanDate() {
            return planDate;
        }

        public void setPlanDate(String planDate) {
            this.planDate = planDate;
        }

        public String getPlanDesc() {
            return planDesc;
        }

        public void setPlanDesc(String planDesc) {
            this.planDesc = planDesc;
        }

        public int getPlanId() {
            return planId;
        }

        public void setPlanId(int planId) {
            this.planId = planId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
