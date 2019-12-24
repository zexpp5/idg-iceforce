package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/28.
 */

public class FollowonListBean {

    /**
     * data : {"code":"success","data":[{"isShow":1,"orderBy":1,"statusCode":"1","statusCodeName":"接触项目"},{"isShow":1,"orderBy":3,"statusCode":"2","statusCodeName":"行业小组讨论"},{"isShow":0,"orderBy":5,"statusCode":"3","statusCodeName":"合伙人周会讨论"},{"isShow":0,"orderBy":7,"statusCode":"4","statusCodeName":"Termsheet"},{"isShow":0,"orderBy":9,"statusCode":"5","statusCodeName":"IC"}],"total":null}
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
         * data : [{"isShow":1,"orderBy":1,"statusCode":"1","statusCodeName":"接触项目"},{"isShow":1,"orderBy":3,"statusCode":"2","statusCodeName":"行业小组讨论"},{"isShow":0,"orderBy":5,"statusCode":"3","statusCodeName":"合伙人周会讨论"},{"isShow":0,"orderBy":7,"statusCode":"4","statusCodeName":"Termsheet"},{"isShow":0,"orderBy":9,"statusCode":"5","statusCodeName":"IC"}]
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
             * isShow : 1
             * orderBy : 1
             * statusCode : 1
             * statusCodeName : 接触项目
             */

            private int isShow;
            private int orderBy;
            private String statusCode;
            private String statusCodeName;

            public int getIsShow() {
                return isShow;
            }

            public void setIsShow(int isShow) {
                this.isShow = isShow;
            }

            public int getOrderBy() {
                return orderBy;
            }

            public void setOrderBy(int orderBy) {
                this.orderBy = orderBy;
            }

            public String getStatusCode() {
                return statusCode;
            }

            public void setStatusCode(String statusCode) {
                this.statusCode = statusCode;
            }

            public String getStatusCodeName() {
                return statusCodeName;
            }

            public void setStatusCodeName(String statusCodeName) {
                this.statusCodeName = statusCodeName;
            }
        }
    }
}
