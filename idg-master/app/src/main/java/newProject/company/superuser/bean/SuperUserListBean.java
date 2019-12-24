package newProject.company.superuser.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/27.
 */

public class SuperUserListBean {

    /**
     * data : [{"deptId":62,"deptName":"未归类","eid":46,"icon":"","imAccount":"wim_13500135001","isSuper":1,"job":"系统管理员","name":"学姐放假发酒疯","status":1,"superStatus":1,"userType":1}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * deptId : 62
         * deptName : 未归类
         * eid : 46
         * icon :
         * imAccount : wim_13500135001
         * isSuper : 1
         * job : 系统管理员
         * name : 学姐放假发酒疯
         * status : 1
         * superStatus : 1
         * userType : 1
         */

        private int deptId;
        private String deptName;
        private int eid;
        private String icon;
        private String imAccount;
        private int isSuper;
        private String job;
        private String name;
        private int status;
        private int superStatus;
        private int userType;

        public int getDeptId() {
            return deptId;
        }

        public void setDeptId(int deptId) {
            this.deptId = deptId;
        }

        public String getDeptName() {
            return deptName;
        }

        public void setDeptName(String deptName) {
            this.deptName = deptName;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
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

        public int getIsSuper() {
            return isSuper;
        }

        public void setIsSuper(int isSuper) {
            this.isSuper = isSuper;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getSuperStatus() {
            return superStatus;
        }

        public void setSuperStatus(int superStatus) {
            this.superStatus = superStatus;
        }

        public int getUserType() {
            return userType;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }
    }
}
