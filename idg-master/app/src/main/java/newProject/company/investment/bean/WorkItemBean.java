package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/8/29.
 */

public class WorkItemBean {

    /**
     * data : {"code":"success","data":[{"date":"2019-07-31","dateStr":"2019年7月","itemName":"工作周报"},{"date":"2019-06-30","dateStr":"2019Q2","itemName":"工作周报"}],"returnMessage":"SUCCESS","total":null}
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
         * data : [{"date":"2019-07-31","dateStr":"2019年7月","itemName":"工作周报"},{"date":"2019-06-30","dateStr":"2019Q2","itemName":"工作周报"}]
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
             * date : 2019-07-31
             * dateStr : 2019年7月
             * itemName : 工作周报
             */

            private String date;
            private String dateStr;
            private String itemName;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public String getItemName() {
                return itemName;
            }

            public void setItemName(String itemName) {
                this.itemName = itemName;
            }
        }
    }
}
