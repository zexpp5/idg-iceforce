package newProject.company.project_manager.investment_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zsz on 2019/4/17.
 */

public class PublicUserListBean {

    /**
     * data : {"code":"success","data":[{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"yi_zhang","userEmail":"yi_zhang@idgcapital.com","userFunction":"","userId":"1","userName":"张屹","userOfficephone":"","userPassword":"eaflyWzK+quSZuHpkZ4WIhFqJVs=","userTelphone":"13811869851","validFlag":1},{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"nan_zhuo","userEmail":"nan_zhuo@idgcapital.com","userFunction":"","userId":"10","userName":"卓楠","userOfficephone":"85901897","userPassword":"ShK4IGDlrKn6sG5TAUCCT2TwqP4=","userTelphone":"13488865430","validFlag":1},{"createBy":"1","createDate":"2017-07-17 20:25:15","editBy":"1","editDate":"2017-07-17 20:25:15","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"jie_cheng","userEmail":"jie_cheng@idgcapital.com","userFunction":"","userId":"1050","userName":"程洁","userOfficephone":"","userPassword":"O95r37V8tZLQSulwdPT0qfxu8cU=","userTelphone":"13810545602","validFlag":1},{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"cecilia_wang","userEmail":"cecilia_wang@idgcapital.com","userFunction":"","userId":"11","userName":"王新叶","userOfficephone":"010-85901837","userPassword":"H4LJQr79optu1IelHaGZ94/OfwU=","userTelphone":"15011476147","validFlag":1}]}
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
         * data : [{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"yi_zhang","userEmail":"yi_zhang@idgcapital.com","userFunction":"","userId":"1","userName":"张屹","userOfficephone":"","userPassword":"eaflyWzK+quSZuHpkZ4WIhFqJVs=","userTelphone":"13811869851","validFlag":1},{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"nan_zhuo","userEmail":"nan_zhuo@idgcapital.com","userFunction":"","userId":"10","userName":"卓楠","userOfficephone":"85901897","userPassword":"ShK4IGDlrKn6sG5TAUCCT2TwqP4=","userTelphone":"13488865430","validFlag":1},{"createBy":"1","createDate":"2017-07-17 20:25:15","editBy":"1","editDate":"2017-07-17 20:25:15","pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"jie_cheng","userEmail":"jie_cheng@idgcapital.com","userFunction":"","userId":"1050","userName":"程洁","userOfficephone":"","userPassword":"O95r37V8tZLQSulwdPT0qfxu8cU=","userTelphone":"13810545602","validFlag":1},{"createBy":null,"createDate":null,"editBy":null,"editDate":null,"pageNo":null,"pageSize":null,"queryString":null,"remarks":"","segments":null,"showOrder":300,"userAccount":"cecilia_wang","userEmail":"cecilia_wang@idgcapital.com","userFunction":"","userId":"11","userName":"王新叶","userOfficephone":"010-85901837","userPassword":"H4LJQr79optu1IelHaGZ94/OfwU=","userTelphone":"15011476147","validFlag":1}]
         */

        private String code;
        private List<DataBean> data;
        private String returnMessage;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public List<DataBean> getData() {
            return data;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable{
            /**
             * createBy : null
             * createDate : null
             * editBy : null
             * editDate : null
             * pageNo : null
             * pageSize : null
             * queryString : null
             * remarks :
             * segments : null
             * showOrder : 300
             * userAccount : yi_zhang
             * userEmail : yi_zhang@idgcapital.com
             * userFunction :
             * userId : 1
             * userName : 张屹
             * userOfficephone :
             * userPassword : eaflyWzK+quSZuHpkZ4WIhFqJVs=
             * userTelphone : 13811869851
             * validFlag : 1
             */

            private String createBy;
            private String createDate;
            private String editBy;
            private String editDate;
            private String pageNo;
            private String pageSize;
            private String queryString;
            private String remarks;
            private String segments;
            private int showOrder;
            private String userAccount;
            private String userEmail;
            private String userFunction;
            private String userId;
            private String userName;
            private String userOfficephone;
            private String userPassword;
            private String userTelphone;
            private int validFlag;
            private boolean isFlag;

            //
            //
            private String createByPhoto;

            public String getCreateByPhoto() {
                return createByPhoto;
            }

            public void setCreateByPhoto(String createByPhoto) {
                this.createByPhoto = createByPhoto;
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

            public String getSegments() {
                return segments;
            }

            public void setSegments(String segments) {
                this.segments = segments;
            }

            public int getShowOrder() {
                return showOrder;
            }

            public void setShowOrder(int showOrder) {
                this.showOrder = showOrder;
            }

            public String getUserAccount() {
                return userAccount;
            }

            public void setUserAccount(String userAccount) {
                this.userAccount = userAccount;
            }

            public String getUserEmail() {
                return userEmail;
            }

            public void setUserEmail(String userEmail) {
                this.userEmail = userEmail;
            }

            public String getUserFunction() {
                return userFunction;
            }

            public void setUserFunction(String userFunction) {
                this.userFunction = userFunction;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserOfficephone() {
                return userOfficephone;
            }

            public void setUserOfficephone(String userOfficephone) {
                this.userOfficephone = userOfficephone;
            }

            public String getUserPassword() {
                return userPassword;
            }

            public void setUserPassword(String userPassword) {
                this.userPassword = userPassword;
            }

            public String getUserTelphone() {
                return userTelphone;
            }

            public void setUserTelphone(String userTelphone) {
                this.userTelphone = userTelphone;
            }

            public int getValidFlag() {
                return validFlag;
            }

            public void setValidFlag(int validFlag) {
                this.validFlag = validFlag;
            }

            public boolean isFlag() {
                return isFlag;
            }

            public void setFlag(boolean flag) {
                isFlag = flag;
            }
        }
    }
}
