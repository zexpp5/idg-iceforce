package newProject.company.business_trip.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/6/13.
 */

public class BeanPersonalHoliday implements Serializable
{


    /**
     * data : [{"applyDate":"2019/02/19","approveReason":null,"currentApprove":"","isConflict":0,"leaveDay":1,
     * "leaveEnd":"2019/02/19","leaveId":1175,"leaveReason":"fdfd","leaveStart":"2019/02/19","leaveType":"年假(Annual leave)",
     * "leaveTypeStr":null,"resumptionStatus":1,"signed":2,"userName":"钟秋月"}]
     * status : 200
     */

    private int status;
    private List<DataBean> data;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * applyDate : 2019/02/19
         * approveReason : null
         * currentApprove :
         * isConflict : 0
         * leaveDay : 1.0
         * leaveEnd : 2019/02/19
         * leaveId : 1175
         * leaveReason : fdfd
         * leaveStart : 2019/02/19
         * leaveType : 年假(Annual leave)
         * leaveTypeStr : null
         * resumptionStatus : 1
         * signed : 2
         * userName : 钟秋月
         */

        private String applyDate;
        private String approveReason;
        private String currentApprove;
        private int isConflict;
        private double leaveDay;
        private String leaveEnd;
        private int leaveId;
        private String leaveReason;
        private String leaveStart;
        private String leaveType;
        private String leaveTypeStr;
        private int resumptionStatus;
        private int signed;
        private String userName;

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

        public int getIsConflict()
        {
            return isConflict;
        }

        public void setIsConflict(int isConflict)
        {
            this.isConflict = isConflict;
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

        public String getApproveReason()
        {
            return approveReason;
        }

        public void setApproveReason(String approveReason)
        {
            this.approveReason = approveReason;
        }

        public String getLeaveTypeStr()
        {
            return leaveTypeStr;
        }

        public void setLeaveTypeStr(String leaveTypeStr)
        {
            this.leaveTypeStr = leaveTypeStr;
        }

        public int getResumptionStatus()
        {
            return resumptionStatus;
        }

        public void setResumptionStatus(int resumptionStatus)
        {
            this.resumptionStatus = resumptionStatus;
        }

        public int getSigned()
        {
            return signed;
        }

        public void setSigned(int signed)
        {
            this.signed = signed;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }
    }
}
