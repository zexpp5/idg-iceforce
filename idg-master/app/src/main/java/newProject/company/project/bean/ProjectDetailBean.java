package newProject.company.project.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/26.
 */

public class ProjectDetailBean {

    /**
     * data : {"ccList":[{"userId":102,"userName":"哈哈哈"}],"project":{"createTime":"2017-10-26","eid":21,"icon":"","remark":"6666","serNo":"XM1710260001","title":"23","ygDeptId":62,"ygDeptName":"未归类","ygId":46,"ygJob":"系统管理员","ygName":"13500135001"}}
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
         * ccList : [{"userId":102,"userName":"哈哈哈"}]
         * project : {"createTime":"2017-10-26","eid":21,"icon":"","remark":"6666","serNo":"XM1710260001","title":"23","ygDeptId":62,"ygDeptName":"未归类","ygId":46,"ygJob":"系统管理员","ygName":"13500135001"}
         */

        private ProjectBean project;
        private List<CcListBean> ccList;

        public ProjectBean getProject() {
            return project;
        }

        public void setProject(ProjectBean project) {
            this.project = project;
        }

        public List<CcListBean> getCcList() {
            return ccList;
        }

        public void setCcList(List<CcListBean> ccList) {
            this.ccList = ccList;
        }

        public static class ProjectBean {
            /**
             * createTime : 2017-10-26
             * eid : 21
             * icon :
             * remark : 6666
             * serNo : XM1710260001
             * title : 23
             * ygDeptId : 62
             * ygDeptName : 未归类
             * ygId : 46
             * ygJob : 系统管理员
             * ygName : 13500135001
             */

            private String createTime;
            private int eid;
            private String icon;
            private String remark;
            private String serNo;
            private String title;
            private int ygDeptId;
            private String ygDeptName;
            private int ygId;
            private String ygJob;
            private String ygName;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
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

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getSerNo() {
                return serNo;
            }

            public void setSerNo(String serNo) {
                this.serNo = serNo;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getYgDeptId() {
                return ygDeptId;
            }

            public void setYgDeptId(int ygDeptId) {
                this.ygDeptId = ygDeptId;
            }

            public String getYgDeptName() {
                return ygDeptName;
            }

            public void setYgDeptName(String ygDeptName) {
                this.ygDeptName = ygDeptName;
            }

            public int getYgId() {
                return ygId;
            }

            public void setYgId(int ygId) {
                this.ygId = ygId;
            }

            public String getYgJob() {
                return ygJob;
            }

            public void setYgJob(String ygJob) {
                this.ygJob = ygJob;
            }

            public String getYgName() {
                return ygName;
            }

            public void setYgName(String ygName) {
                this.ygName = ygName;
            }
        }

        public static class CcListBean {
            /**
             * userId : 102
             * userName : 哈哈哈
             */

            private int userId;
            private String userName;

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
}
