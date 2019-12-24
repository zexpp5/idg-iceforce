package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/9/3.
 */

public class HistoryListBean {

    /**
     * data : {"code":"success","data":[{"date":"2019-03-07","desc":"张屹申请年假(Annual leave)7.0天，2019-02-20至2019-02-28","item":"请假审批处理","result":"驳回"},{"date":"2019-03-07","desc":"张屹申请年假(Annual leave)2.0天，2019-03-07至2019-03-08","item":"请假审批处理","result":"同意"}],"returnMessage":null,"total":2}
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
         * data : [{"date":"2019-03-07","desc":"张屹申请年假(Annual leave)7.0天，2019-02-20至2019-02-28","item":"请假审批处理","result":"驳回"},{"date":"2019-03-07","desc":"张屹申请年假(Annual leave)2.0天，2019-03-07至2019-03-08","item":"请假审批处理","result":"同意"}]
         * returnMessage : null
         * total : 2
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
             * date : 2019-03-07
             * desc : 张屹申请年假(Annual leave)7.0天，2019-02-20至2019-02-28
             * item : 请假审批处理
             * result : 驳回
             */

            private String date;
            private String desc;
            private String item;
            private String result;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getItem() {
                return item;
            }

            public void setItem(String item) {
                this.item = item;
            }

            public String getResult() {
                return result;
            }

            public void setResult(String result) {
                this.result = result;
            }
        }
    }
}
