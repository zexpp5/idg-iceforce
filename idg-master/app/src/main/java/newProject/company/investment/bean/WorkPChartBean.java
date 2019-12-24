package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/9/2.
 */

public class WorkPChartBean {

    /**
     * data : {"code":"success","data":[{"num":0,"userName":"贾爽"},{"num":0,"userName":"范中昱"},{"num":0,"userName":"张赞峰"},{"num":0,"userName":"朱晨璇"},{"num":0,"userName":"Alex Tang"},{"num":0,"userName":"Kelvin Yu"},{"num":0,"userName":"吴佳璐"},{"num":0,"userName":"张迟"},{"num":0,"userName":"边潇男"},{"num":0,"userName":"周珣"},{"num":0,"userName":"郑又鑫"},{"num":0,"userName":"何昊"},{"num":0,"userName":"金姜赟"},{"num":0,"userName":"撒拉拂"},{"num":0,"userName":"王显之"},{"num":0,"userName":"江左"},{"num":0,"userName":"张赛"},{"num":0,"userName":"张希"},{"num":0,"userName":"谢昱骋"}],"returnMessage":"SUCCESS","total":null}
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
         * data : [{"num":0,"userName":"贾爽"},{"num":0,"userName":"范中昱"},{"num":0,"userName":"张赞峰"},{"num":0,"userName":"朱晨璇"},{"num":0,"userName":"Alex Tang"},{"num":0,"userName":"Kelvin Yu"},{"num":0,"userName":"吴佳璐"},{"num":0,"userName":"张迟"},{"num":0,"userName":"边潇男"},{"num":0,"userName":"周珣"},{"num":0,"userName":"郑又鑫"},{"num":0,"userName":"何昊"},{"num":0,"userName":"金姜赟"},{"num":0,"userName":"撒拉拂"},{"num":0,"userName":"王显之"},{"num":0,"userName":"江左"},{"num":0,"userName":"张赛"},{"num":0,"userName":"张希"},{"num":0,"userName":"谢昱骋"}]
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
             * num : 0
             * userName : 贾爽
             */

            private int num;
            private String userName;

            public int getNum() {
                return num;
            }

            public void setNum(int num) {
                this.num = num;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }
        }
    }
}
