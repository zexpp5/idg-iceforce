package newProject.company.goout.bean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/25.
 */

public class GoOutDetailBean {

    /**
     * data : {"outWork":{"approvalFinal":2,"approvalPerson":[{"name":"13657721381","userId":0}],"approvalSta":-1,"approvalUserId":0,"createTime":"2017-10-24","eid":12,"endTime":"2018-10-24","icon":"","reason":"asd","remark":"dsaf","serNo":"OW1710240001","startTime":"2017-10-24","targetAddress":"adfa","vehicles":"qece","ygDeptId":71,"ygDeptName":"未归类","ygId":53,"ygJob":"系统管理员","ygName":"djxn"}}
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
         * outWork : {"approvalFinal":2,"approvalPerson":[{"name":"13657721381","userId":0}],"approvalSta":-1,"approvalUserId":0,"createTime":"2017-10-24","eid":12,"endTime":"2018-10-24","icon":"","reason":"asd","remark":"dsaf","serNo":"OW1710240001","startTime":"2017-10-24","targetAddress":"adfa","vehicles":"qece","ygDeptId":71,"ygDeptName":"未归类","ygId":53,"ygJob":"系统管理员","ygName":"djxn"}
         */

        private OutWorkBean outWork;
        private List<Annexdata> annexList;

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public OutWorkBean getOutWork() {
            return outWork;
        }

        public void setOutWork(OutWorkBean outWork) {
            this.outWork = outWork;
        }

        public static class OutWorkBean {
            /**
             * approvalFinal : 2
             * approvalPerson : [{"name":"13657721381","userId":0}]
             * approvalSta : -1
             * approvalUserId : 0
             * createTime : 2017-10-24
             * eid : 12
             * endTime : 2018-10-24
             * icon :
             * reason : asd
             * remark : dsaf
             * serNo : OW1710240001
             * startTime : 2017-10-24
             * targetAddress : adfa
             * vehicles : qece
             * ygDeptId : 71
             * ygDeptName : 未归类
             * ygId : 53
             * ygJob : 系统管理员
             * ygName : djxn
             */

            private int approvalFinal;
            private int approvalSta;
            private int approvalUserId;
            private String createTime;
            private int eid;
            private String endTime;
            private String icon;
            private String reason;
            private String remark;
            private String serNo;
            private String startTime;
            private String targetAddress;
            private String vehicles;
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

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            public String getReason() {
                return reason;
            }

            public void setReason(String reason) {
                this.reason = reason;
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

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getTargetAddress() {
                return targetAddress;
            }

            public void setTargetAddress(String targetAddress) {
                this.targetAddress = targetAddress;
            }

            public String getVehicles() {
                return vehicles;
            }

            public void setVehicles(String vehicles) {
                this.vehicles = vehicles;
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
                 * name : 13657721381
                 * userId : 0
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

                public String getNo() {
                    return no;
                }

                public void setNo(String no) {
                    this.no = no;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }
            }
        }
    }
}
