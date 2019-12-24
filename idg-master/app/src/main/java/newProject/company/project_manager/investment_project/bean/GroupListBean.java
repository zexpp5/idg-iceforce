package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/17.
 */

public class GroupListBean {

    /**
     * data : {"code":"success","data":[{"createBy":null,"createDate":null,"deptId":"1","deptName":"VC组","deptOrder":10,"editBy":"640","editDate":"2017-11-14 13:41:58","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"1","createDate":"2017-06-16 15:18:06","deptId":"5354","deptName":"PE组","deptOrder":15,"editBy":"640","editDate":"2017-11-14 13:43:06","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"2","deptName":"工业技术组","deptOrder":20,"editBy":"640","editDate":"2017-11-14 13:42:05","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"4","deptName":"地产组","deptOrder":40,"editBy":"640","editDate":"2017-11-14 13:42:37","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"5","deptName":"保险组","deptOrder":50,"editBy":"640","editDate":"2017-11-14 13:42:44","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"6","deptName":"知行并进组","deptOrder":60,"editBy":"640","editDate":"2017-11-14 13:42:51","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"7","deptName":"体育组","deptOrder":70,"editBy":"640","editDate":"2017-11-14 13:42:57","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"9","deptName":"并购组","deptOrder":90,"editBy":"640","editDate":"2017-11-14 13:43:02","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"640","createDate":"2017-09-14 13:33:08","deptId":"87619","deptName":"新能源组","deptOrder":91,"editBy":"640","editDate":"2017-11-14 13:41:52","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"new energy","validFlag":1},{"createBy":null,"createDate":null,"deptId":"8","deptName":"Others","deptOrder":102,"editBy":"310","editDate":"2017-09-20 10:06:23","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"1","createDate":"2018-08-20 22:03:19","deptId":"149602","deptName":"金融组","deptOrder":220,"editBy":"1","editDate":"2018-08-20 22:03:19","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"116","createDate":"2018-03-23 11:18:15","deptId":"147957","deptName":"General","deptOrder":230,"editBy":"116","editDate":"2018-03-23 11:18:15","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1}]}
     */

    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"createBy":null,"createDate":null,"deptId":"1","deptName":"VC组","deptOrder":10,"editBy":"640","editDate":"2017-11-14 13:41:58","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"1","createDate":"2017-06-16 15:18:06","deptId":"5354","deptName":"PE组","deptOrder":15,"editBy":"640","editDate":"2017-11-14 13:43:06","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"2","deptName":"工业技术组","deptOrder":20,"editBy":"640","editDate":"2017-11-14 13:42:05","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"4","deptName":"地产组","deptOrder":40,"editBy":"640","editDate":"2017-11-14 13:42:37","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"5","deptName":"保险组","deptOrder":50,"editBy":"640","editDate":"2017-11-14 13:42:44","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"6","deptName":"知行并进组","deptOrder":60,"editBy":"640","editDate":"2017-11-14 13:42:51","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"7","deptName":"体育组","deptOrder":70,"editBy":"640","editDate":"2017-11-14 13:42:57","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":null,"createDate":null,"deptId":"9","deptName":"并购组","deptOrder":90,"editBy":"640","editDate":"2017-11-14 13:43:02","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"640","createDate":"2017-09-14 13:33:08","deptId":"87619","deptName":"新能源组","deptOrder":91,"editBy":"640","editDate":"2017-11-14 13:41:52","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"new energy","validFlag":1},{"createBy":null,"createDate":null,"deptId":"8","deptName":"Others","deptOrder":102,"editBy":"310","editDate":"2017-09-20 10:06:23","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"1","createDate":"2018-08-20 22:03:19","deptId":"149602","deptName":"金融组","deptOrder":220,"editBy":"1","editDate":"2018-08-20 22:03:19","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1},{"createBy":"116","createDate":"2018-03-23 11:18:15","deptId":"147957","deptName":"General","deptOrder":230,"editBy":"116","editDate":"2018-03-23 11:18:15","induFlag":"Y","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","validFlag":1}]
         */

        private String code;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * createBy : null
             * createDate : null
             * deptId : 1
             * deptName : VC组
             * deptOrder : 10
             * editBy : 640
             * editDate : 2017-11-14 13:41:58
             * induFlag : Y
             * pageNo : null
             * pageSize : null
             * queryString : null
             * remarks : 
             * validFlag : 1
             */

            private String createBy;
            private String createDate;
            private String deptId;
            private String deptName;
            private int deptOrder;
            private String editBy;
            private String editDate;
            private String induFlag;
            private String pageNo;
            private String pageSize;
            private String queryString;
            private String remarks;
            private int validFlag;

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

            public int getDeptOrder() {
                return deptOrder;
            }

            public void setDeptOrder(int deptOrder) {
                this.deptOrder = deptOrder;
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

            public String getInduFlag() {
                return induFlag;
            }

            public void setInduFlag(String induFlag) {
                this.induFlag = induFlag;
            }

            public String getPageNo() {
                return pageNo;
            }

            public void setPageNo(String pageNo) {
                this.pageNo = pageNo;
            }

            public String getPageSize() {
                return pageSize;
            }

            public void setPageSize(String pageSize) {
                this.pageSize = pageSize;
            }

            public String getQueryString() {
                return queryString;
            }

            public void setQueryString(String queryString) {
                this.queryString = queryString;
            }

            public String getRemarks() {
                return remarks;
            }

            public void setRemarks(String remarks) {
                this.remarks = remarks;
            }

            public int getValidFlag() {
                return validFlag;
            }

            public void setValidFlag(int validFlag) {
                this.validFlag = validFlag;
            }
        }
    }
}
