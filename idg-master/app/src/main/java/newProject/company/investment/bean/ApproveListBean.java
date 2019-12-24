package newProject.company.investment.bean;

import java.util.List;

/**
 * Created by zsz on 2019/9/3.
 */

public class ApproveListBean {

    /**
     * data : {"code":"success","data":[{"desc":"休假申请:李佳巍 2019-09-17至2019-09-26","id":906,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:李佳巍 2019-09-02至2019-09-04","id":905,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":902,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":901,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":900,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-29至2019-04-29","id":899,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-29至2019-04-29","id":898,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-06-06至2019-06-10","id":897,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-06-06至2019-06-10","id":896,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":895,"oaItemType":"holiday","type":"请假"}],"returnMessage":null,"total":170}
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
         * data : [{"desc":"休假申请:李佳巍 2019-09-17至2019-09-26","id":906,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:李佳巍 2019-09-02至2019-09-04","id":905,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":902,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":901,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":900,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-29至2019-04-29","id":899,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-29至2019-04-29","id":898,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-06-06至2019-06-10","id":897,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-06-06至2019-06-10","id":896,"oaItemType":"holiday","type":"请假"},{"desc":"休假申请:罗智慧 2019-04-30至2019-05-05","id":895,"oaItemType":"holiday","type":"请假"}]
         * returnMessage : null
         * total : 170
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
             * desc : 休假申请:李佳巍 2019-09-17至2019-09-26
             * id : 906
             * oaItemType : holiday
             * type : 请假
             */

            private String desc;
            private String id;
            private String oaItemType;
            private String type;

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getOaItemType() {
                return oaItemType;
            }

            public void setOaItemType(String oaItemType) {
                this.oaItemType = oaItemType;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
