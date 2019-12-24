package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/9/2.
 */

public class WorkChartBean {

    /**
     * data : {"code":"success","data":[{"dateStr":"2019年5月","num":7},{"dateStr":"2019年6月","num":25},{"dateStr":"2019年7月","num":4},{"dateStr":"2019年8月","num":1}],"returnMessage":"SUCCESS","total":null}
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
         * data : [{"dateStr":"2019年5月","num":7},{"dateStr":"2019年6月","num":25},{"dateStr":"2019年7月","num":4},{"dateStr":"2019年8月","num":1}]
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private String returnMessage;
        private String total;
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

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
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
             * dateStr : 2019年5月
             * num : 7
             */

            private String dateStr;
            private int num;

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }
        }
    }
}
