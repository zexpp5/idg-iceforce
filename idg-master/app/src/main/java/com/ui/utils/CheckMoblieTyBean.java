package com.ui.utils;

import java.io.Serializable;

/**
 * Created by cx007 on 2016/6/6.
 */
public class CheckMoblieTyBean implements Serializable {


    /**
     * data : {"loginUser":{"account":"13640635965","createTime":"","eid":null,"email":"","icon":"","imAccount":"13640635965_erp_experience","name":"","sex":"","telephone":"","token":"ZmUxMWY1NmQ4YmYxNDhiNWI4MzMwMjI4M2U0YmZmOTA=","updateTime":"","workImg":""},"status":200}
     * status : 200
     */

    private DataBean data;
    private int status;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBean {
        /**
         * loginUser : {"account":"13640635965","createTime":"","eid":null,"email":"","icon":"","imAccount":"13640635965_erp_experience","name":"","sex":"","telephone":"","token":"ZmUxMWY1NmQ4YmYxNDhiNWI4MzMwMjI4M2U0YmZmOTA=","updateTime":"","workImg":""}
         * status : 200
         */

        private LoginUserBean loginUser;
        private int status;

        public LoginUserBean getLoginUser() {
            return loginUser;
        }

        public void setLoginUser(LoginUserBean loginUser) {
            this.loginUser = loginUser;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public static class LoginUserBean {
            /**
             * account : 13640635965
             * createTime :
             * eid : null
             * email :
             * icon :
             * imAccount : 13640635965_erp_experience
             * name :
             * sex :
             * telephone :
             * token : ZmUxMWY1NmQ4YmYxNDhiNWI4MzMwMjI4M2U0YmZmOTA=
             * updateTime :
             * workImg :
             */

            private String account;
            private String createTime;
            private Object eid;
            private String email;
            private String icon;
            private String imAccount;
            private String name;
            private String sex;
            private String telephone;
            private String token;
            private String updateTime;
            private String workImg;

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Object getEid() {
                return eid;
            }

            public void setEid(Object eid) {
                this.eid = eid;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getImAccount() {
                return imAccount;
            }

            public void setImAccount(String imAccount) {
                this.imAccount = imAccount;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }

            public String getTelephone() {
                return telephone;
            }

            public void setTelephone(String telephone) {
                this.telephone = telephone;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }

            public String getWorkImg() {
                return workImg;
            }

            public void setWorkImg(String workImg) {
                this.workImg = workImg;
            }
        }
    }
}
