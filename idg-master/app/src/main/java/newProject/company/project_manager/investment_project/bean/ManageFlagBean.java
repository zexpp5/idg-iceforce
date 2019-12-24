package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/5/20.
 */

public class ManageFlagBean {


    /**
     * data : {"code":"success","data":{"deptId":"5354","deptName":"PE组","email":"justin_niu@idgcapital.com","flag":1,"myProjFlag":1,"roleFlag":"208","teamProjFlag":1,"userId":"209","userName":"牛奎光"},"returnMessage":"SUCCESS","total":null}
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
         * data : {"deptId":"5354","deptName":"PE组","email":"justin_niu@idgcapital.com","flag":1,"myProjFlag":1,"roleFlag":"208","teamProjFlag":1,"userId":"209","userName":"牛奎光"}
         * returnMessage : SUCCESS
         * total : null
         */

        private String code;
        private DataBean data;
        private String returnMessage;
        private Integer total;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public static class DataBean {
            /**
             * deptId : 5354
             * deptName : PE组
             * email : justin_niu@idgcapital.com
             * flag : 1
             * myProjFlag : 1
             * roleFlag : 208
             * teamProjFlag : 1
             * userId : 209
             * userName : 牛奎光
             */

            private String deptId;
            private String deptName;
            private String email;
            private int flag;
            private int myProjFlag;
            private String roleFlag;
            private int teamProjFlag;
            private String userId;
            private String userName;

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

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public int getFlag() {
                return flag;
            }

            public void setFlag(int flag) {
                this.flag = flag;
            }

            public int getMyProjFlag() {
                return myProjFlag;
            }

            public void setMyProjFlag(int myProjFlag) {
                this.myProjFlag = myProjFlag;
            }

            public String getRoleFlag() {
                return roleFlag;
            }

            public void setRoleFlag(String roleFlag) {
                this.roleFlag = roleFlag;
            }

            public int getTeamProjFlag() {
                return teamProjFlag;
            }

            public void setTeamProjFlag(int teamProjFlag) {
                this.teamProjFlag = teamProjFlag;
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
        }
    }
}
