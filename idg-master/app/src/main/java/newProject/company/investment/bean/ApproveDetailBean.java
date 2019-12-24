package newProject.company.investment.bean;

/**
 * Created by zsz on 2019/9/3.
 */

public class ApproveDetailBean {

    /**
     * data : {"code":"success","data":{"applyId":"2097608","approveId":906,"isApprove":"0","isConflict":0,"leaveInfo":{"applyDate":"2019/09/02","leaveDay":10,"leaveEnd":"2019/09/26","leaveId":2294,"leaveMemo":null,"leaveReason":"婚假，谢谢","leaveStart":"2019/09/17","leaveType":"婚假","leaveTypeCode":"13","leaveTypeStr":"婚假","name":"李佳巍","resumptionStatus":null,"userName":"jiawei_li"}}}
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
         * data : {"applyId":"2097608","approveId":906,"isApprove":"0","isConflict":0,"leaveInfo":{"applyDate":"2019/09/02","leaveDay":10,"leaveEnd":"2019/09/26","leaveId":2294,"leaveMemo":null,"leaveReason":"婚假，谢谢","leaveStart":"2019/09/17","leaveType":"婚假","leaveTypeCode":"13","leaveTypeStr":"婚假","name":"李佳巍","resumptionStatus":null,"userName":"jiawei_li"}}
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
             * applyId : 2097608
             * approveId : 906
             * isApprove : 0
             * isConflict : 0
             * leaveInfo : {"applyDate":"2019/09/02","leaveDay":10,"leaveEnd":"2019/09/26","leaveId":2294,"leaveMemo":null,"leaveReason":"婚假，谢谢","leaveStart":"2019/09/17","leaveType":"婚假","leaveTypeCode":"13","leaveTypeStr":"婚假","name":"李佳巍","resumptionStatus":null,"userName":"jiawei_li"}
             */

            private String applyId;
            private int approveId;
            private String isApprove;
            private int isConflict;
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

            public int getIsConflict() {
                return isConflict;
            }

            public void setIsConflict(int isConflict) {
                this.isConflict = isConflict;
            }

            public LeaveInfoBean getLeaveInfo() {
                return leaveInfo;
            }

            public void setLeaveInfo(LeaveInfoBean leaveInfo) {
                this.leaveInfo = leaveInfo;
            }

            public static class LeaveInfoBean {
                /**
                 * applyDate : 2019/09/02
                 * leaveDay : 10.0
                 * leaveEnd : 2019/09/26
                 * leaveId : 2294
                 * leaveMemo : null
                 * leaveReason : 婚假，谢谢
                 * leaveStart : 2019/09/17
                 * leaveType : 婚假
                 * leaveTypeCode : 13
                 * leaveTypeStr : 婚假
                 * name : 李佳巍
                 * resumptionStatus : null
                 * userName : jiawei_li
                 */

                private String applyDate;
                private double leaveDay;
                private String leaveEnd;
                private int leaveId;
                private String leaveMemo;
                private String leaveReason;
                private String leaveStart;
                private String leaveType;
                private String leaveTypeCode;
                private String leaveTypeStr;
                private String name;
                private String resumptionStatus;
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

                public String getLeaveTypeCode() {
                    return leaveTypeCode;
                }

                public void setLeaveTypeCode(String leaveTypeCode) {
                    this.leaveTypeCode = leaveTypeCode;
                }

                public String getLeaveTypeStr() {
                    return leaveTypeStr;
                }

                public void setLeaveTypeStr(String leaveTypeStr) {
                    this.leaveTypeStr = leaveTypeStr;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getResumptionStatus() {
                    return resumptionStatus;
                }

                public void setResumptionStatus(String resumptionStatus) {
                    this.resumptionStatus = resumptionStatus;
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
