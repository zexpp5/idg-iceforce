package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/5/10.
 */

public class PIMDInfoBean {

    /**
     * data : {"code":"success","data":{"fundName":"基金****34621","investCondition":"null投资CNY 400,000,000.00,占比2.22%","investDate":"2017-10-17","projTeam":"过以宏,邵辉,刘雨坤","projType":null,"significantDeal":"","vcpeFlag":null},"total":null}
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
         * data : {"fundName":"基金****34621","investCondition":"null投资CNY 400,000,000.00,占比2.22%","investDate":"2017-10-17","projTeam":"过以宏,邵辉,刘雨坤","projType":null,"significantDeal":"","vcpeFlag":null}
         * total : null
         */

        private String code;
        private DataBean data;
        private Integer total;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public static class DataBean {
            /**
             * fundName : 基金****34621
             * investCondition : null投资CNY 400,000,000.00,占比2.22%
             * investDate : 2017-10-17
             * projTeam : 过以宏,邵辉,刘雨坤
             * projType : null
             * significantDeal :
             * vcpeFlag : null
             */

            private String fundName;
            private String investCondition;
            private String investDate;
            private String projTeam;
            private String projType;
            private String significantDeal;
            private String significantDealStr;
            private String vcpeFlag;

            public String getFundName() {
                return fundName;
            }

            public void setFundName(String fundName) {
                this.fundName = fundName;
            }

            public String getInvestCondition() {
                return investCondition;
            }

            public void setInvestCondition(String investCondition) {
                this.investCondition = investCondition;
            }

            public String getInvestDate() {
                return investDate;
            }

            public void setInvestDate(String investDate) {
                this.investDate = investDate;
            }

            public String getProjTeam() {
                return projTeam;
            }

            public void setProjTeam(String projTeam) {
                this.projTeam = projTeam;
            }

            public String getProjType() {
                return projType;
            }

            public void setProjType(String projType) {
                this.projType = projType;
            }

            public String getSignificantDeal() {
                return significantDeal;
            }

            public void setSignificantDeal(String significantDeal) {
                this.significantDeal = significantDeal;
            }

            public String getSignificantDealStr() {
                return significantDealStr;
            }

            public void setSignificantDealStr(String significantDealStr) {
                this.significantDealStr = significantDealStr;
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
