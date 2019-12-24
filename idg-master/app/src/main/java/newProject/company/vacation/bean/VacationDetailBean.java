package newProject.company.vacation.bean;

/**
 * Created by Administrator on 2017/11/22.
 */

public class VacationDetailBean
{


    /**
     * data : {"code":"SUCCESS","data":{"applyDate":"2017/11/21","currentApprove":"qiuyue_zhong","leaveDay":1,
     * "leaveEnd":"2017/11/22","leaveId":49,"leaveMemo":"其他","leaveReason":"","leaveStart":"2017/11/21","leaveType":23,
     * "signed":3}}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        /**
         * code : SUCCESS
         * data : {"applyDate":"2017/11/21","currentApprove":"qiuyue_zhong","leaveDay":1,"leaveEnd":"2017/11/22","leaveId":49,
         * "leaveMemo":"其他","leaveReason":"","leaveStart":"2017/11/21","leaveType":23,"signed":3}
         */

        private String code;
        private DataBean data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public DataBean getData()
        {
            return data;
        }

        public void setData(DataBean data)
        {
            this.data = data;
        }

        public static class DataBean
        {
            /**
             * applyDate : 2017/11/21
             * currentApprove : qiuyue_zhong
             * leaveDay : 1
             * leaveEnd : 2017/11/22
             * leaveId : 49
             * leaveMemo : 其他
             * leaveReason :
             * leaveStart : 2017/11/21
             * leaveType : 23
             * signed : 3
             */

            private String applyDate;
            private String currentApprove;
            private double leaveDay;
            private String leaveEnd;
            private int leaveId;
            private String leaveMemo;
            private String leaveReason;
            private String leaveStart;
            private String leaveType;
            private int signed;
            private int leaveTypeCode;
            private String approveReason;

            public String getApproveReason()
            {
                return approveReason;
            }

            public void setApproveReason(String approveReason)
            {
                this.approveReason = approveReason;
            }

            public int getLeaveTypeCode()
            {
                return leaveTypeCode;
            }

            public void setLeaveTypeCode(int leaveTypeCode)
            {
                this.leaveTypeCode = leaveTypeCode;
            }

            public String getApplyDate()
            {
                return applyDate;
            }

            public void setApplyDate(String applyDate)
            {
                this.applyDate = applyDate;
            }

            public String getCurrentApprove()
            {
                return currentApprove;
            }

            public void setCurrentApprove(String currentApprove)
            {
                this.currentApprove = currentApprove;
            }

            public double getLeaveDay()
            {
                return leaveDay;
            }

            public void setLeaveDay(double leaveDay)
            {
                this.leaveDay = leaveDay;
            }

            public String getLeaveEnd()
            {
                return leaveEnd;
            }

            public void setLeaveEnd(String leaveEnd)
            {
                this.leaveEnd = leaveEnd;
            }

            public int getLeaveId()
            {
                return leaveId;
            }

            public void setLeaveId(int leaveId)
            {
                this.leaveId = leaveId;
            }

            public String getLeaveMemo()
            {
                return leaveMemo;
            }

            public void setLeaveMemo(String leaveMemo)
            {
                this.leaveMemo = leaveMemo;
            }

            public String getLeaveReason()
            {
                return leaveReason;
            }

            public void setLeaveReason(String leaveReason)
            {
                this.leaveReason = leaveReason;
            }

            public String getLeaveStart()
            {
                return leaveStart;
            }

            public void setLeaveStart(String leaveStart)
            {
                this.leaveStart = leaveStart;
            }

            public String getLeaveType()
            {
                return leaveType;
            }

            public void setLeaveType(String leaveType)
            {
                this.leaveType = leaveType;
            }

            public int getSigned()
            {
                return signed;
            }

            public void setSigned(int signed)
            {
                this.signed = signed;
            }
        }
    }
}
