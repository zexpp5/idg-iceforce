package newProject.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/24.
 */

public class ApprovalBean {

    /**
     * data : [{"approvalFinal":1,"approvalNo":1,"approvalRemark":"同意！不错","bid":12,"btype":"3","createTime":"10-23 09:47","deptName":"未归类","eid":17,"job":"系统管理员","remark":"不错","userId":14,"userName":"子敬"}]
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
         * approvalFinal : 1
         * approvalNo : 1
         * approvalRemark : 同意！不错
         * bid : 12
         * btype : 3
         * createTime : 10-23 09:47
         * deptName : 未归类
         * eid : 17
         * job : 系统管理员
         * remark : 不错
         * userId : 14
         * userName : 子敬
         */

        private int approvalFinal;
        private int approvalNo;
        private String approvalRemark;
        private int bid;
        private String btype;
        private String createTime;
        private String deptName;
        private int eid;
        private String job;
        private String remark;
        private int userId;
        private String userName;
        private String icon;

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public int getApprovalFinal() {
            return approvalFinal;
        }

        public void setApprovalFinal(int approvalFinal) {
            this.approvalFinal = approvalFinal;
        }

        public int getApprovalNo() {
            return approvalNo;
        }

        public void setApprovalNo(int approvalNo) {
            this.approvalNo = approvalNo;
        }

        public String getApprovalRemark() {
            return approvalRemark;
        }

        public void setApprovalRemark(String approvalRemark) {
            this.approvalRemark = approvalRemark;
        }

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public String getBtype() {
            return btype;
        }

        public void setBtype(String btype) {
            this.btype = btype;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
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
