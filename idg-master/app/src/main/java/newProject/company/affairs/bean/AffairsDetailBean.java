package newProject.company.affairs.bean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/25.
 */

public class AffairsDetailBean {

    /**
     * data : {"affair":{"approvalFinal":2,"approvalNo":1,"approvalPerson":[{"job":"系统管理员","name":"13657721381","no":"1","userId":48}],"approvalSta":-1,"approvalUserId":48,"createId":53,"createTime":"2017-10-25","eid":25,"icon":"","remark":"sdfadfa","serNo":"SW1710250002","title":"sd","ygDeptId":71,"ygDeptName":"未归类","ygId":53,"ygJob":"系统管理员","ygName":"djxn"},"ccList":[{"userId":48,"userName":"13657721381"}]}
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
         * affair : {"approvalFinal":2,"approvalNo":1,"approvalPerson":[{"job":"系统管理员","name":"13657721381","no":"1","userId":48}],"approvalSta":-1,"approvalUserId":48,"createId":53,"createTime":"2017-10-25","eid":25,"icon":"","remark":"sdfadfa","serNo":"SW1710250002","title":"sd","ygDeptId":71,"ygDeptName":"未归类","ygId":53,"ygJob":"系统管理员","ygName":"djxn"}
         * ccList : [{"userId":48,"userName":"13657721381"}]
         */

        private AffairBean affair;
        private List<Annexdata> annexList;
        private List<CcListBean> ccList;

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public AffairBean getAffair() {
            return affair;
        }

        public void setAffair(AffairBean affair) {
            this.affair = affair;
        }

        public List<CcListBean> getCcList() {
            return ccList;
        }

        public void setCcList(List<CcListBean> ccList) {
            this.ccList = ccList;
        }

        public static class AffairBean {
            /**
             * approvalFinal : 2
             * approvalNo : 1
             * approvalPerson : [{"job":"系统管理员","name":"13657721381","no":"1","userId":48}]
             * approvalSta : -1
             * approvalUserId : 48
             * createId : 53
             * createTime : 2017-10-25
             * eid : 25
             * icon :
             * remark : sdfadfa
             * serNo : SW1710250002
             * title : sd
             * ygDeptId : 71
             * ygDeptName : 未归类
             * ygId : 53
             * ygJob : 系统管理员
             * ygName : djxn
             */

            private int approvalFinal;
            private int approvalNo;
            private int approvalSta;
            private int approvalUserId;
            private int createId;
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
            private List<ApprovalPersonBean> approvalPerson;

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

            public int getApprovalSta() {
                return approvalSta;
            }

            public void setApprovalSta(int approvalSta) {
                this.approvalSta = approvalSta;
            }

            public int getApprovalUserId() {
                return approvalUserId;
            }

            public void setApprovalUserId(int approvalUserId) {
                this.approvalUserId = approvalUserId;
            }

            public int getCreateId() {
                return createId;
            }

            public void setCreateId(int createId) {
                this.createId = createId;
            }

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

            public List<ApprovalPersonBean> getApprovalPerson() {
                return approvalPerson;
            }

            public void setApprovalPerson(List<ApprovalPersonBean> approvalPerson) {
                this.approvalPerson = approvalPerson;
            }

            public static class ApprovalPersonBean {
                /**
                 * job : 系统管理员
                 * name : 13657721381
                 * no : 1
                 * userId : 48
                 */

                private String job;
                private String name;
                private String no;
                private int userId;

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

                public String getNo() {
                    return no;
                }

                public void setNo(String no) {
                    this.no = no;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }
            }
        }

        public static class CcListBean {
            /**
             * userId : 48
             * userName : 13657721381
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
