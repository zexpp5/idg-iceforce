package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/24.
 */

public class PIMFundListBean {

    /**
     * data : {"code":"success","data":[{"createBy":"1","createDate":"2019-05-24 10:18:20","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"247","fundName":"和谐成长二期","id":"4","orderBy":3},{"createBy":"1","createDate":"2019-05-23 20:04:29","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"F0012","fundName":"CC2Main","id":"5","orderBy":4},{"createBy":"1","createDate":"2019-05-23 20:04:52","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"F0025","fundName":"CC3Main","id":"6","orderBy":5}],"total":null}
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
         * data : [{"createBy":"1","createDate":"2019-05-24 10:18:20","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"247","fundName":"和谐成长二期","id":"4","orderBy":3},{"createBy":"1","createDate":"2019-05-23 20:04:29","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"F0012","fundName":"CC2Main","id":"5","orderBy":4},{"createBy":"1","createDate":"2019-05-23 20:04:52","deptId":"5354","deptName":"PE组","editBy":null,"editDate":null,"fundId":"F0025","fundName":"CC3Main","id":"6","orderBy":5}]
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
             * createBy : 1
             * createDate : 2019-05-24 10:18:20
             * deptId : 5354
             * deptName : PE组
             * editBy : null
             * editDate : null
             * fundId : 247
             * fundName : 和谐成长二期
             * id : 4
             * orderBy : 3
             */

            private String createBy;
            private String createDate;
            private String deptId;
            private String deptName;
            private String editBy;
            private String editDate;
            private String fundId;
            private String fundName;
            private String id;
            private int orderBy;

            private boolean isChoose;

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDeptId() {
                return deptId;
            }

            public void setDeptId(String deptId) {
                this.deptId = deptId;
            }

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getEditBy() {
                return editBy;
            }

            public void setEditBy(String editBy) {
                this.editBy = editBy;
            }

            public String getEditDate() {
                return editDate;
            }

            public void setEditDate(String editDate) {
                this.editDate = editDate;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public int getOrderBy() {
                return orderBy;
            }

            public void setOrderBy(int orderBy) {
                this.orderBy = orderBy;
            }
        }
    }
}
