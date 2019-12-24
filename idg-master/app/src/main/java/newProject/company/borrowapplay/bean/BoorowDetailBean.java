package newProject.company.borrowapplay.bean;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

/**
 * Created by tujingwu on 2017/10/23.
 */

public class BoorowDetailBean {

    /**
     * data : {"annexList":[],"loan":{"approvalFinal":3,"approvalNo":1,"approvalPerson":[{"job":"系统管理员","name":"13657721381","no":"1","userId":48}],"approvalSta":-1,"approvalUserId":48,"createId":48,"createTime":"2014","currencyValue":"币","eid":11,"icon":"","money":0,"remark":"asdf","serNo":"JK1710230003","title":"sd","ygDeptId":64,"ygDeptName":"未归类","ygId":48,"ygJob":"系统管理员","ygName":"13657721381"}}
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
         * annexList : []
         * loan : {"approvalFinal":3,"approvalNo":1,"approvalPerson":[{"job":"系统管理员","name":"13657721381","no":"1","userId":48}],"approvalSta":-1,"approvalUserId":48,"createId":48,"createTime":"2014","currencyValue":"币","eid":11,"icon":"","money":0,"remark":"asdf","serNo":"JK1710230003","title":"sd","ygDeptId":64,"ygDeptName":"未归类","ygId":48,"ygJob":"系统管理员","ygName":"13657721381"}
         */

        private LoanBean loan;

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }


        private List<Annexdata> annexList;

        public LoanBean getLoan() {
            return loan;
        }

        public void setLoan(LoanBean loan) {
            this.loan = loan;
        }




        public static class LoanBean {
            /**
             * approvalFinal : 3
             * approvalNo : 1
             * approvalPerson : [{"job":"系统管理员","name":"13657721381","no":"1","userId":48}]
             * approvalSta : -1
             * approvalUserId : 48
             * createId : 48
             * createTime : 2014
             * currencyValue : 币
             * eid : 11
             * icon :
             * money : 0.0
             * remark : asdf
             * serNo : JK1710230003
             * title : sd
             * ygDeptId : 64
             * ygDeptName : 未归类
             * ygId : 48
             * ygJob : 系统管理员
             * ygName : 13657721381
             */

            private int approvalFinal;
            private int approvalNo;
            private int approvalSta;
            private int approvalUserId;
            private int createId;
            private String createTime;
            private String currencyValue;
            private int eid;
            private String icon;
            private double money;
            private String bigMoney;
            private String remark;
            private String serNo;
            private String title;
            private int ygDeptId;
            private String ygDeptName;
            private int ygId;
            private String ygJob;
            private String ygName;
            private List<ApprovalPersonBean> approvalPerson;

            public String getBigMoney() {
                return bigMoney;
            }

            public void setBigMoney(String bigMoney) {
                this.bigMoney = bigMoney;
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

            public String getCurrencyValue() {
                return currencyValue;
            }

            public void setCurrencyValue(String currencyValue) {
                this.currencyValue = currencyValue;
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

            public double getMoney() {
                return money;
            }

            public void setMoney(double money) {
                this.money = money;
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
    }
}
