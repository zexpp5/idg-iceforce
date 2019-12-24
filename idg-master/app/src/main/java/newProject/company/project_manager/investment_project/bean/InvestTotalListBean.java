package newProject.company.project_manager.investment_project.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsz on 2019/4/28.
 */

public class InvestTotalListBean {

    /**
     * data : {"code":"success","data":[{"currency":"USD","invTotal":1.01505219E8,"investStage":"已投资","monthToMonth":null,"ownership":0.1809,"valuationOfFund":2.15036805E8,"valuationOfProj":2.15036805E8,"valueDate":"2018-12-31"}],"total":null}
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
         * data : [{"currency":"USD","invTotal":1.01505219E8,"investStage":"已投资","monthToMonth":null,"ownership":0.1809,"valuationOfFund":2.15036805E8,"valuationOfProj":2.15036805E8,"valueDate":"2018-12-31"}]
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
             * currency : USD
             * invTotal : 1.01505219E8
             * investStage : 已投资
             * monthToMonth : null
             * ownership : 0.1809
             * valuationOfFund : 2.15036805E8
             * valuationOfProj : 2.15036805E8
             * valueDate : 2018-12-31
             */

            private String currency;
            private String  invTotal;
            private String investStage;
            private String monthToMonth;
            private String ownership;
            private String valuationOfFund;
            private String valuationOfProj;
            private String valueDate;

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getInvTotal() {
                return invTotal;
            }

            public void setInvTotal(String invTotal) {
                this.invTotal = invTotal;
            }

            public String getInvestStage() {
                return investStage;
            }

            public void setInvestStage(String investStage) {
                this.investStage = investStage;
            }

            public String getMonthToMonth() {
                return monthToMonth;
            }

            public void setMonthToMonth(String monthToMonth) {
                this.monthToMonth = monthToMonth;
            }

            public String getOwnership() {
                return ownership;
            }

            public void setOwnership(String ownership) {
                this.ownership = ownership;
            }

            public String getValuationOfFund() {
                return valuationOfFund;
            }

            public void setValuationOfFund(String valuationOfFund) {
                this.valuationOfFund = valuationOfFund;
            }

            public String getValuationOfProj() {
                return valuationOfProj;
            }

            public void setValuationOfProj(String valuationOfProj) {
                this.valuationOfProj = valuationOfProj;
            }

            public String getValueDate() {
                return valueDate;
            }

            public void setValueDate(String valueDate) {
                this.valueDate = valueDate;
            }
        }
    }
}
