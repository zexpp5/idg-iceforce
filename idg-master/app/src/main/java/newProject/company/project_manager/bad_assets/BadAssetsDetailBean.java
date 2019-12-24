package newProject.company.project_manager.bad_assets;

import android.text.TextUtils;

/**
 * @author: Created by Freeman on 2018/7/23
 */

public class BadAssetsDetailBean {

    /**
     * data : {"analysis":"","classType":null,"confirmInfo":"","dealLeadName":"孙宇含","dealLegalName":null,"ename":"果麦","entity":"爱爱","entityOversea":"嗯嗯","grade":null,"indusName":"文化娱乐","investAmt":"和谐成长二期 共借款约 人民币100000000","percent":"","projId":7830,"projInDate":"2018-07-09","riskControl":"","situation":""}
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
         * analysis :
         * classType : null
         * confirmInfo :
         * dealLeadName : 孙宇含
         * dealLegalName : null
         * ename : 果麦
         * entity : 爱爱
         * entityOversea : 嗯嗯
         * grade : null
         * indusName : 文化娱乐
         * investAmt : 和谐成长二期 共借款约 人民币100000000
         * percent :
         * projId : 7830
         * projInDate : 2018-07-09
         * riskControl :
         * situation :
         */

        private String analysis;
        private String classType;
        private String confirmInfo;
        private String dealLeadName;
        private String dealLegalName;
        private String ename;
        private String entity;
        private String entityOversea;
        private String grade;
        private String indusName;
        private String investAmt;
        private String percent;
        private int projId;
        private String projInDate;
        private String riskControl;
        private String situation;

        public String getAnalysis() {
            return analysis;
        }

        public void setAnalysis(String analysis) {
            this.analysis = analysis;
        }

        public String getClassType() {
            return classType;
        }

        public void setClassType(String classType) {
            this.classType = classType;
        }

        public String getConfirmInfo() {
            return confirmInfo;
        }

        public void setConfirmInfo(String confirmInfo) {
            this.confirmInfo = confirmInfo;
        }

        public String getDealLeadName() {
            return dealLeadName;
        }

        public void setDealLeadName(String dealLeadName) {
            this.dealLeadName = dealLeadName;
        }

        public String getDealLegalName() {
            return dealLegalName;
        }

        public void setDealLegalName(String dealLegalName) {
            this.dealLegalName = dealLegalName;
        }

        public String getEname() {
            return TextUtils.isEmpty(ename) ? "" : ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getEntity() {
            return entity;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public String getEntityOversea() {
            return entityOversea;
        }

        public void setEntityOversea(String entityOversea) {
            this.entityOversea = entityOversea;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getIndusName() {
            return indusName;
        }

        public void setIndusName(String indusName) {
            this.indusName = indusName;
        }

        public String getInvestAmt() {
            return investAmt;
        }

        public void setInvestAmt(String investAmt) {
            this.investAmt = investAmt;
        }

        public String getPercent() {
            return percent;
        }

        public void setPercent(String percent) {
            this.percent = percent;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjInDate() {
            return projInDate;
        }

        public void setProjInDate(String projInDate) {
            this.projInDate = projInDate;
        }

        public String getRiskControl() {
            return riskControl;
        }

        public void setRiskControl(String riskControl) {
            this.riskControl = riskControl;
        }

        public String getSituation() {
            return situation;
        }

        public void setSituation(String situation) {
            this.situation = situation;
        }
    }
}
