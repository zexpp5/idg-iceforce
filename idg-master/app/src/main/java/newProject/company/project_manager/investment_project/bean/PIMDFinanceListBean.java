package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/10.
 */

public class PIMDFinanceListBean {

    /**
     * data : {"code":"success","data":[{"actualIncome":"697,341,160.89","actualIncomeRemark":null,"actualNetProfit":"-824,698,275.86","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"32.55%","chainGrowthOfActualNetProfit":"111.41%","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add43a6-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q3","signalFlag":null,"username":null,"vcpeFlag":null},{"actualIncome":"526,094,954.3","actualIncomeRemark":null,"actualNetProfit":"-390,090,696.43","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"255.46%","chainGrowthOfActualNetProfit":"46.38%","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add439c-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q2","signalFlag":null,"username":null,"vcpeFlag":null},{"actualIncome":"148,002,143.65","actualIncomeRemark":null,"actualNetProfit":"-266,489,854.51","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"","chainGrowthOfActualNetProfit":"","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add4390-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q1","signalFlag":null,"username":null,"vcpeFlag":null}],"total":null}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"actualIncome":"697,341,160.89","actualIncomeRemark":null,"actualNetProfit":"-824,698,275.86","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"32.55%","chainGrowthOfActualNetProfit":"111.41%","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add43a6-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q3","signalFlag":null,"username":null,"vcpeFlag":null},{"actualIncome":"526,094,954.3","actualIncomeRemark":null,"actualNetProfit":"-390,090,696.43","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"255.46%","chainGrowthOfActualNetProfit":"46.38%","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add439c-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q2","signalFlag":null,"username":null,"vcpeFlag":null},{"actualIncome":"148,002,143.65","actualIncomeRemark":null,"actualNetProfit":"-266,489,854.51","actualNetProfitRemark":null,"chainGrowthOfActualIncome":"","chainGrowthOfActualNetProfit":"","currency":"CNY","endDate":null,"fundId":null,"incomeDiffRatio":"","netProfitDiffRatio":"","pageNo":0,"pageSize":10,"predictIncome":"0.00","predictIncomeRemark":null,"predictNetProfit":"0.00","predictNetProfitRemark":null,"projFinanceId":"5add4390-67d4-11e9-a66d-fefcfe837585","projId":"1457","projName":"奇安信（360企业安全）","reportFrequency":"2018Q1","signalFlag":null,"username":null,"vcpeFlag":null}]
         * total : null
         */

        private String code;
        private Integer total;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * actualIncome : 697,341,160.89
             * actualIncomeRemark : null
             * actualNetProfit : -824,698,275.86
             * actualNetProfitRemark : null
             * chainGrowthOfActualIncome : 32.55%
             * chainGrowthOfActualNetProfit : 111.41%
             * currency : CNY
             * endDate : null
             * fundId : null
             * incomeDiffRatio :
             * netProfitDiffRatio :
             * pageNo : 0
             * pageSize : 10
             * predictIncome : 0.00
             * predictIncomeRemark : null
             * predictNetProfit : 0.00
             * predictNetProfitRemark : null
             * projFinanceId : 5add43a6-67d4-11e9-a66d-fefcfe837585
             * projId : 1457
             * projName : 奇安信（360企业安全）
             * reportFrequency : 2018Q3
             * signalFlag : null
             * username : null
             * vcpeFlag : null
             */

            private String actualIncome;
            private String actualIncomeRemark;
            private String actualNetProfit;
            private String actualNetProfitRemark;
            private String chainGrowthOfActualIncome;
            private String chainGrowthOfActualNetProfit;
            private String currency;
            private String endDate;
            private String fundId;
            private String incomeDiffRatio;
            private String netProfitDiffRatio;
            private int pageNo;
            private int pageSize;
            private String predictIncome;
            private String predictIncomeRemark;
            private String predictNetProfit;
            private String predictNetProfitRemark;
            private String projFinanceId;
            private String projId;
            private String projName;
            private String reportFrequency;
            private String signalFlag;
            private String username;
            private String vcpeFlag;

            //
            private String startDate;
            private String frequency;
            private String year;
            private String dateStr;

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public String getFrequency()
            {
                return frequency;
            }

            public void setFrequency(String frequency)
            {
                this.frequency = frequency;
            }


            public String getStartDate() {
                return startDate;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getActualIncome() {
                return actualIncome;
            }

            public void setActualIncome(String actualIncome) {
                this.actualIncome = actualIncome;
            }

            public String getActualIncomeRemark() {
                return actualIncomeRemark;
            }

            public void setActualIncomeRemark(String actualIncomeRemark) {
                this.actualIncomeRemark = actualIncomeRemark;
            }

            public String getActualNetProfit() {
                return actualNetProfit;
            }

            public void setActualNetProfit(String actualNetProfit) {
                this.actualNetProfit = actualNetProfit;
            }

            public String getActualNetProfitRemark() {
                return actualNetProfitRemark;
            }

            public void setActualNetProfitRemark(String actualNetProfitRemark) {
                this.actualNetProfitRemark = actualNetProfitRemark;
            }

            public String getChainGrowthOfActualIncome() {
                return chainGrowthOfActualIncome;
            }

            public void setChainGrowthOfActualIncome(String chainGrowthOfActualIncome) {
                this.chainGrowthOfActualIncome = chainGrowthOfActualIncome;
            }

            public String getChainGrowthOfActualNetProfit() {
                return chainGrowthOfActualNetProfit;
            }

            public void setChainGrowthOfActualNetProfit(String chainGrowthOfActualNetProfit) {
                this.chainGrowthOfActualNetProfit = chainGrowthOfActualNetProfit;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getFundId() {
                return fundId;
            }

            public void setFundId(String fundId) {
                this.fundId = fundId;
            }

            public String getIncomeDiffRatio() {
                return incomeDiffRatio;
            }

            public void setIncomeDiffRatio(String incomeDiffRatio) {
                this.incomeDiffRatio = incomeDiffRatio;
            }

            public String getNetProfitDiffRatio() {
                return netProfitDiffRatio;
            }

            public void setNetProfitDiffRatio(String netProfitDiffRatio) {
                this.netProfitDiffRatio = netProfitDiffRatio;
            }

            public int getPageNo() {
                return pageNo;
            }

            public void setPageNo(int pageNo) {
                this.pageNo = pageNo;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public String getPredictIncome() {
                return predictIncome;
            }

            public void setPredictIncome(String predictIncome) {
                this.predictIncome = predictIncome;
            }

            public String getPredictIncomeRemark() {
                return predictIncomeRemark;
            }

            public void setPredictIncomeRemark(String predictIncomeRemark) {
                this.predictIncomeRemark = predictIncomeRemark;
            }

            public String getPredictNetProfit() {
                return predictNetProfit;
            }

            public void setPredictNetProfit(String predictNetProfit) {
                this.predictNetProfit = predictNetProfit;
            }

            public String getPredictNetProfitRemark() {
                return predictNetProfitRemark;
            }

            public void setPredictNetProfitRemark(String predictNetProfitRemark) {
                this.predictNetProfitRemark = predictNetProfitRemark;
            }

            public String getProjFinanceId() {
                return projFinanceId;
            }

            public void setProjFinanceId(String projFinanceId) {
                this.projFinanceId = projFinanceId;
            }

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getProjName() {
                return projName;
            }

            public void setProjName(String projName) {
                this.projName = projName;
            }

            public String getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(String reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public String getSignalFlag() {
                return signalFlag;
            }

            public void setSignalFlag(String signalFlag) {
                this.signalFlag = signalFlag;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getVcpeFlag() {
                return vcpeFlag;
            }

            public void setVcpeFlag(String vcpeFlag) {
                this.vcpeFlag = vcpeFlag;
            }
        }
    }
}
