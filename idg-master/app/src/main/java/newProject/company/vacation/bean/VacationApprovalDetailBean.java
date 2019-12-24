package newProject.company.vacation.bean;

/**
 * Created by tujingwu on 2017/11/21.
 */

public class VacationApprovalDetailBean {

    /**
     * data : {"code":"success","data":{"applyId":"538574.7318408345","approveId":19,"isApprove":"2","leaveInfo":{"applyDate":"2017/11/21","leaveDay":555,"leaveEnd":"2017/11/21","leaveId":51,"leaveMemo":"55","leaveReason":"555","leaveStart":"2017/11/21","leaveType":22,"name":"刘小雨","userName":"amy_liu"}}}
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
         * data : {"applyId":"538574.7318408345","approveId":19,"isApprove":"2","leaveInfo":{"applyDate":"2017/11/21","leaveDay":555,"leaveEnd":"2017/11/21","leaveId":51,"leaveMemo":"55","leaveReason":"555","leaveStart":"2017/11/21","leaveType":22,"name":"刘小雨","userName":"amy_liu"}}
         */

        private String code;
        private DataBean data;

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

        public static class DataBean {
            /**
             * applyId : 538574.7318408345
             * approveId : 19
             * isApprove : 2
             * leaveInfo : {"applyDate":"2017/11/21","leaveDay":555,"leaveEnd":"2017/11/21","leaveId":51,"leaveMemo":"55","leaveReason":"555","leaveStart":"2017/11/21","leaveType":22,"name":"刘小雨","userName":"amy_liu"}
             */

            private String applyId;
            private int approveId;
            private String isApprove;
            private LeaveInfoBean leaveInfo;

            public String getApplyId() {
                return applyId;
            }

            public void setApplyId(String applyId) {
                this.applyId = applyId;
            }

            public int getApproveId() {
                return approveId;
            }

            public void setApproveId(int approveId) {
                this.approveId = approveId;
            }

            public String getIsApprove() {
                return isApprove;
            }

            public void setIsApprove(String isApprove) {
                this.isApprove = isApprove;
            }

            public LeaveInfoBean getLeaveInfo() {
                return leaveInfo;
            }

            public void setLeaveInfo(LeaveInfoBean leaveInfo) {
                this.leaveInfo = leaveInfo;
            }

            public static class LeaveInfoBean {
                /**
                 * applyDate : 2017/11/21
                 * leaveDay : 555
                 * leaveEnd : 2017/11/21
                 * leaveId : 51
                 * leaveMemo : 55
                 * leaveReason : 555
                 * leaveStart : 2017/11/21
                 * leaveType : 22
                 * name : 刘小雨
                 * userName : amy_liu
                 */

                private String applyDate;
                private double leaveDay;
                private String leaveEnd;
                private int leaveId;
                private String leaveMemo;
                private String leaveReason;
                private String leaveStart;
                private String leaveType;
                private String name;
                private String userName;

                public String getApplyDate() {
                    return applyDate;
                }

                public void setApplyDate(String applyDate) {
                    this.applyDate = applyDate;
                }

                public double getLeaveDay() {
                    return leaveDay;
                }

                public void setLeaveDay(double leaveDay) {
                    this.leaveDay = leaveDay;
                }

                public String getLeaveEnd() {
                    return leaveEnd;
                }

                public void setLeaveEnd(String leaveEnd) {
                    this.leaveEnd = leaveEnd;
                }

                public int getLeaveId() {
                    return leaveId;
                }

                public void setLeaveId(int leaveId) {
                    this.leaveId = leaveId;
                }

                public String getLeaveMemo() {
                    return leaveMemo;
                }

                public void setLeaveMemo(String leaveMemo) {
                    this.leaveMemo = leaveMemo;
                }

                public String getLeaveReason() {
                    return leaveReason;
                }

                public void setLeaveReason(String leaveReason) {
                    this.leaveReason = leaveReason;
                }

                public String getLeaveStart() {
                    return leaveStart;
                }

                public void setLeaveStart(String leaveStart) {
                    this.leaveStart = leaveStart;
                }

                public String getLeaveType() {
                    return leaveType;
                }

                public void setLeaveType(String leaveType) {
                    this.leaveType = leaveType;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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
}
