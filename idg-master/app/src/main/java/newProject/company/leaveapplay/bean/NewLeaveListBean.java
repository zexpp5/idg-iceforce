package newProject.company.leaveapplay.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class NewLeaveListBean {

    /**
     * data : [{"UPDATE_TIME":1508569685000,"createTime":"2017-10-21","eid":4,"statusName":"批审中","title":"请假事由","ygDeptId":666,"ygDeptName":"欧美特工总部","ygId":7,"ygName":"詹姆斯邦"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 1
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<NewLeaveDataBean> data;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<NewLeaveDataBean> getData() {
        return data;
    }

    public void setData(List<NewLeaveDataBean> data) {
        this.data = data;
    }

    public static class NewLeaveDataBean {
            /**
             * userName : yi_zhang
             * leaveStart : 2017/12/01
             * leaveEnd : 2017/12/05
             * leaveTime : 5
             * leaveType :  11
             * signed :  1
             * currentApprove :
             */
        private int eid;
        private String userName;
        private String leaveStart;
        private String leaveEnd;
        private double leaveDay;
        private String leaveType;
        private int signed;
        private String currentApprove;
        private int leaveId;
        private String applyDate;

        private String approveReason;

        public String getApproveReason()
        {
            return approveReason;
        }

        public void setApproveReason(String approveReason)
        {
            this.approveReason = approveReason;
        }

        public double getLeaveDay() {
            return leaveDay;
        }

        public void setLeaveDay(double leaveDay) {
            this.leaveDay = leaveDay;
        }

        public String getApplyDate() {
            return applyDate;
        }

        public void setApplyDate(String applyDate) {
            this.applyDate = applyDate;
        }

        public int getLeaveId() {
            return leaveId;
        }

        public void setLeaveId(int leaveId) {
            this.leaveId = leaveId;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getLeaveStart() {
                return leaveStart;
            }

            public void setLeaveStart(String leaveStart) {
                this.leaveStart = leaveStart;
            }

            public String getLeaveEnd() {
                return leaveEnd;
            }

            public void setLeaveEnd(String leaveEnd) {
                this.leaveEnd = leaveEnd;
            }

            public String getLeaveType() {
                return leaveType;
            }

            public void setLeaveType(String leaveType) {
                this.leaveType = leaveType;
            }

            public int getSigned() {
                return signed;
            }

            public void setSigned(int signed) {
                this.signed = signed;
            }

            public String getCurrentApprove() {
                return currentApprove;
            }

            public void setCurrentApprove(String currentApprove) {
                this.currentApprove = currentApprove;
            }
        }

}
