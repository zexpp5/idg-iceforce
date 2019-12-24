package newProject.company.project_manager.investment_project.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zsz on 2019/4/28.
 */

public class FundInvestListBean {

    /**
     * data : {"code":"success","data":[{"abbr":"CCM","currency":"USD","fundId":"F0010","fundName":"CCMain","invTotal":9.7029124E7,"ownership":0.1729,"valuationOfFund":2.0555371E8,"valuationOfProj":2.0555371E8,"valueDate":"2018-12-31"},{"abbr":"CCI","currency":"USD","fundId":"F0011","fundName":"CCInv","invTotal":4476095,"ownership":0.008,"valuationOfFund":9483095,"valuationOfProj":9483095,"valueDate":"2018-12-31"}],"total":null}
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
         * data : [{"abbr":"CCM","currency":"USD","fundId":"F0010","fundName":"CCMain","invTotal":9.7029124E7,"ownership":0.1729,"valuationOfFund":2.0555371E8,"valuationOfProj":2.0555371E8,"valueDate":"2018-12-31"},{"abbr":"CCI","currency":"USD","fundId":"F0011","fundName":"CCInv","invTotal":4476095,"ownership":0.008,"valuationOfFund":9483095,"valuationOfProj":9483095,"valueDate":"2018-12-31"}]
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
             * abbr : CCM
             * currency : USD
             * fundId : F0010
             * fundName : CCMain
             * invTotal : 9.7029124E7
             * ownership : 0.1729
             * valuationOfFund : 2.0555371E8
             * valuationOfProj : 2.0555371E8
             * valueDate : 2018-12-31
             */

            private String abbr;
            private String currency;
            private String fundId;
            private String fundName;
            private BigDecimal invTotal;
            private String invTotalStr;
            private String ownership;
            private BigDecimal valuationOfFund;
            private String valuationOfFundStr;
            private String valuationOfProj;
            private String valueDate;

            public String getAbbr() {
                return abbr;
            }

            public void setAbbr(String abbr) {
                this.abbr = abbr;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getFundId() {
                return fundId;
            }

            public void setFundId(String fundId) {
                this.fundId = fundId;
            }

            public String getFundName() {
                return fundName;
            }

            public void setFundName(String fundName) {
                this.fundName = fundName;
            }

            public BigDecimal getInvTotal() {
                return invTotal;
            }

            public void setInvTotal(BigDecimal invTotal) {
                this.invTotal = invTotal;
            }

            public String getOwnership() {
                return ownership;
            }

            public void setOwnership(String ownership) {
                this.ownership = ownership;
            }

            public BigDecimal getValuationOfFund() {
                return valuationOfFund;
            }

            public void setValuationOfFund(BigDecimal valuationOfFund) {
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

            public String getInvTotalStr() {
                return invTotalStr;
            }

            public void setInvTotalStr(String invTotalStr) {
                this.invTotalStr = invTotalStr;
            }

            public String getValuationOfFundStr() {
                return valuationOfFundStr;
            }

            public void setValuationOfFundStr(String valuationOfFundStr) {
                this.valuationOfFundStr = valuationOfFundStr;
            }
        }
    }
}
