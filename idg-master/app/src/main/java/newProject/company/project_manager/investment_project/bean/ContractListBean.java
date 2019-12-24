package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/12/22.
 */

public class ContractListBean {

    /**
     * data : {"contracts":[{"fund":"和谐成长二期","ownership":"12.5%","cost":"100000","currency":"CNY","multiply":"1"}],"rmbCost":"1000000","rmbGrowth":"10%","usdCost":"600000","usdGrowth":"-"}
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
         * contracts : [{"fund":"和谐成长二期","ownership":"12.5%","cost":"100000","currency":"CNY","multiply":"1"}]
         * rmbCost : 1000000
         * rmbGrowth : 10%
         * usdCost : 600000
         * usdGrowth : -
         */

        private String rmbCost;
        private String rmbGrowth;
        private String usdCost;
        private String usdGrowth;
        private List<ContractsBean> contracts;

        public String getRmbCost() {
            return rmbCost;
        }

        public void setRmbCost(String rmbCost) {
            this.rmbCost = rmbCost;
        }

        public String getRmbGrowth() {
            return rmbGrowth;
        }

        public void setRmbGrowth(String rmbGrowth) {
            this.rmbGrowth = rmbGrowth;
        }

        public String getUsdCost() {
            return usdCost;
        }

        public void setUsdCost(String usdCost) {
            this.usdCost = usdCost;
        }

        public String getUsdGrowth() {
            return usdGrowth;
        }

        public void setUsdGrowth(String usdGrowth) {
            this.usdGrowth = usdGrowth;
        }

        public List<ContractsBean> getContracts() {
            return contracts;
        }

        public void setContracts(List<ContractsBean> contracts) {
            this.contracts = contracts;
        }

        public static class ContractsBean {
            /**
             * fund : 和谐成长二期
             * ownership : 12.5%
             * cost : 100000
             * currency : CNY
             * multiply : 1
             */

            private String fund;
            private String ownership;
            private String cost;
            private String currency;
            private String multiply;

            public String getFund() {
                return fund;
            }

            public void setFund(String fund) {
                this.fund = fund;
            }

            public String getOwnership() {
                return ownership;
            }

            public void setOwnership(String ownership) {
                this.ownership = ownership;
            }

            public String getCost() {
                return cost;
            }

            public void setCost(String cost) {
                this.cost = cost;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getMultiply() {
                return multiply;
            }

            public void setMultiply(String multiply) {
                this.multiply = multiply;
            }
        }
    }
}
