package newProject.company.superpower.bean;

/**
 * Created by tujingwu on 2017/10/31.
 */

public class UserDetailBean {

    /**
     * data : {"account":"12366666666","deptId":142,"deptName":"特工总部","eid":144,"job":"特工","name":"灰太狼","password":"F59BD65F7EDAFB087A81D4DCA06C4910"}
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
         * account : 12366666666
         * deptId : 142
         * deptName : 特工总部
         * eid : 144
         * job : 特工
         * name : 灰太狼
         * password : F59BD65F7EDAFB087A81D4DCA06C4910
         */

        private String account;
        private int deptId;
        private String deptName;
        private int eid;
        private String job;
        private String name;
        private int pid;
        private String pName;
        private String password;

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getpName() {
            return pName;
        }

        public void setpName(String pName) {
            this.pName = pName;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

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

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
