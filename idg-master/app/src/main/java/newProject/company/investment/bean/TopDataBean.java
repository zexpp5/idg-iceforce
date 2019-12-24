package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/12/4.
 */

public class TopDataBean {

    /**
     * data : {"code":"success","data":[{"invCost":"$2.2千万","projId":"5127","projName":"PP租车(CVC4Main)","totalReturnMultiple":"0.18X","valuation":"$0.4千万","valueDate":"2019-06-30"},{"invCost":"$1.4千万","projId":"12840","projName":"银河宽带(CVC5Main)","totalReturnMultiple":"1.00X","valuation":"$1.4千万","valueDate":"2019-06-30"},{"invCost":"$1.1千万","projId":"5445","projName":"轻轻家教(CVC4Main)","totalReturnMultiple":"2.83X","valuation":"$3.3千万","valueDate":"2019-06-30"},{"invCost":"$0.9千万","projId":"1480","projName":"玩多多（花火思维）(CVC4Main)","totalReturnMultiple":"1.46X","valuation":"$1.4千万","valueDate":"2019-06-30"},{"invCost":"$0.7千万","projId":"4913","projName":"好巧旅游(Seed V)","totalReturnMultiple":"0.67X","valuation":"$0.1千万","valueDate":"2019-06-30"},{"invCost":"$0.7千万","projId":"4913","projName":"好巧旅游(Seed V)","totalReturnMultiple":"0.50X","valuation":"$0.00千万","valueDate":"2019-06-30"},{"invCost":"$0.5千万","projId":"5038","projName":"信美二手车（优车诚品）(GF3Main)","totalReturnMultiple":"1.00X","valuation":"$0.5千万","valueDate":"2019-06-30"},{"invCost":"$0.5千万","projId":"20402","projName":"Yi Lan Technology(CVC5Main)","totalReturnMultiple":"1.26X","valuation":"$0.6千万","valueDate":"2019-06-30"},{"invCost":"$0.4千万","projId":"4913","projName":"好巧旅游(Seed IV)","totalReturnMultiple":"1.34X","valuation":"$0.3千万","valueDate":"2019-06-30"},{"invCost":"$0.4千万","projId":"4913","projName":"好巧旅游(Seed IV)","totalReturnMultiple":"0.67X","valuation":"$0.00千万","valueDate":"2019-06-30"}],"returnMessage":"SUCCESS","total":45}
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
         * data : [{"invCost":"$2.2千万","projId":"5127","projName":"PP租车(CVC4Main)","totalReturnMultiple":"0.18X","valuation":"$0.4千万","valueDate":"2019-06-30"},{"invCost":"$1.4千万","projId":"12840","projName":"银河宽带(CVC5Main)","totalReturnMultiple":"1.00X","valuation":"$1.4千万","valueDate":"2019-06-30"},{"invCost":"$1.1千万","projId":"5445","projName":"轻轻家教(CVC4Main)","totalReturnMultiple":"2.83X","valuation":"$3.3千万","valueDate":"2019-06-30"},{"invCost":"$0.9千万","projId":"1480","projName":"玩多多（花火思维）(CVC4Main)","totalReturnMultiple":"1.46X","valuation":"$1.4千万","valueDate":"2019-06-30"},{"invCost":"$0.7千万","projId":"4913","projName":"好巧旅游(Seed V)","totalReturnMultiple":"0.67X","valuation":"$0.1千万","valueDate":"2019-06-30"},{"invCost":"$0.7千万","projId":"4913","projName":"好巧旅游(Seed V)","totalReturnMultiple":"0.50X","valuation":"$0.00千万","valueDate":"2019-06-30"},{"invCost":"$0.5千万","projId":"5038","projName":"信美二手车（优车诚品）(GF3Main)","totalReturnMultiple":"1.00X","valuation":"$0.5千万","valueDate":"2019-06-30"},{"invCost":"$0.5千万","projId":"20402","projName":"Yi Lan Technology(CVC5Main)","totalReturnMultiple":"1.26X","valuation":"$0.6千万","valueDate":"2019-06-30"},{"invCost":"$0.4千万","projId":"4913","projName":"好巧旅游(Seed IV)","totalReturnMultiple":"1.34X","valuation":"$0.3千万","valueDate":"2019-06-30"},{"invCost":"$0.4千万","projId":"4913","projName":"好巧旅游(Seed IV)","totalReturnMultiple":"0.67X","valuation":"$0.00千万","valueDate":"2019-06-30"}]
         * returnMessage : SUCCESS
         * total : 45
         */

        private String code;
        private String returnMessage;
        private int total;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
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
             * invCost : $2.2千万
             * projId : 5127
             * projName : PP租车(CVC4Main)
             * totalReturnMultiple : 0.18X
             * valuation : $0.4千万
             * valueDate : 2019-06-30
             */

            private String invCost;
            private String projId;
            private String projName;
            private String totalReturnMultiple;
            private String valuation;
            private String valueDate;

            public String getInvCost() {
                return invCost;
            }

            public void setInvCost(String invCost) {
                this.invCost = invCost;
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

            public String getTotalReturnMultiple() {
                return totalReturnMultiple;
            }

            public void setTotalReturnMultiple(String totalReturnMultiple) {
                this.totalReturnMultiple = totalReturnMultiple;
            }

            public String getValuation() {
                return valuation;
            }

            public void setValuation(String valuation) {
                this.valuation = valuation;
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
