package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/10.
 */

public class PIMDChartListBean {

    /**
     * data : {"code":"success","data":[{"actualIncome":"584678.8900","actualNetProfit":"-11469971.2700","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q4"},{"actualIncome":"519144.1100","actualNetProfit":"-4793412.7900","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q3"},{"actualIncome":"511434.8400","actualNetProfit":"-3220448.2600","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q2"},{"actualIncome":"407890.0700","actualNetProfit":"-3533941.0100","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q1"}],"total":null}
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
         * data : [{"actualIncome":"584678.8900","actualNetProfit":"-11469971.2700","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q4"},{"actualIncome":"519144.1100","actualNetProfit":"-4793412.7900","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q3"},{"actualIncome":"511434.8400","actualNetProfit":"-3220448.2600","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q2"},{"actualIncome":"407890.0700","actualNetProfit":"-3533941.0100","incomeDiffRatio":"-","netProfitDiffRatio":"-","predictIncome":"0","predictNetProfit":"0","reportFrequency":"2018Q1"}]
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
             * actualIncome : 584678.8900
             * actualNetProfit : -11469971.2700
             * incomeDiffRatio : -
             * netProfitDiffRatio : -
             * predictIncome : 0
             * predictNetProfit : 0
             * reportFrequency : 2018Q4
             */

            private String actualIncome;
            private String actualNetProfit;
            private String incomeDiffRatio;
            private String netProfitDiffRatio;
            private String predictIncome;
            private String predictNetProfit;
            private String reportFrequency;

            public String getActualIncome() {
                return actualIncome;
            }

            public void setActualIncome(String actualIncome) {
                this.actualIncome = actualIncome;
            }

            public String getActualNetProfit() {
                return actualNetProfit;
            }

            public void setActualNetProfit(String actualNetProfit) {
                this.actualNetProfit = actualNetProfit;
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

            public String getPredictIncome() {
                return predictIncome;
            }

            public void setPredictIncome(String predictIncome) {
                this.predictIncome = predictIncome;
            }

            public String getPredictNetProfit() {
                return predictNetProfit;
            }

            public void setPredictNetProfit(String predictNetProfit) {
                this.predictNetProfit = predictNetProfit;
            }

            public String getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(String reportFrequency) {
                this.reportFrequency = reportFrequency;
            }
        }
    }
}
