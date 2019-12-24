package newProject.company.resumption;

import java.util.List;

/**
 * Created by Administrator on 2018/4/14.
 */

public class ReleaveListBean {

    /**
     * data : [{"kid":1,"kleaveId":"521","leaveType":"11","operateDate":1523513044297,"remark":null,"resumpitonDays":0.5,"resumptionBegin":1504800000000,"resumptionEnd":1504800000000,"signed":1,"userName":null},{"kid":2,"kleaveId":"521","leaveType":"11","operateDate":1523527451723,"remark":null,"resumpitonDays":0.5,"resumptionBegin":1504800000000,"resumptionEnd":1504800000000,"signed":1,"userName":null}]
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
         * kid : 1
         * kleaveId : 521
         * leaveType : 11
         * operateDate : 1523513044297
         * remark : null
         * resumpitonDays : 0.5
         * resumptionBegin : 1504800000000
         * resumptionEnd : 1504800000000
         * signed : 1
         * userName : null
         */
        private int kid;
        private String kleaveId;
        private String leaveType;
        private String operateDate;
        private String remark;
        private double resumptionDays;
        private String resumptionBegin;
        private String resumptionEnd;
        private int signed;
        private String userName;

        private String applyDate;
        private String currentApprove;
        private String reason;

        public String getReason()
        {
            return reason;
        }

        public void setReason(String reason)
        {
            this.reason = reason;
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

        public double getResumptionDays() {
            return resumptionDays;
        }

        public void setResumptionDays(double resumptionDays) {
            this.resumptionDays = resumptionDays;
        }

        public int getKid() {
            return kid;
        }

        public void setKid(int kid) {
            this.kid = kid;
        }

        public String getKleaveId() {
            return kleaveId;
        }

        public void setKleaveId(String kleaveId) {
            this.kleaveId = kleaveId;
        }

        public String getLeaveType() {
            return leaveType;
        }

        public void setLeaveType(String leaveType) {
            this.leaveType = leaveType;
        }

        public String getOperateDate() {
            return operateDate;
        }

        public void setOperateDate(String operateDate) {
            this.operateDate = operateDate;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getResumptionBegin() {
            return resumptionBegin;
        }

        public void setResumptionBegin(String resumptionBegin) {
            this.resumptionBegin = resumptionBegin;
        }

        public String getResumptionEnd() {
            return resumptionEnd;
        }

        public void setResumptionEnd(String resumptionEnd) {
            this.resumptionEnd = resumptionEnd;
        }

        public int getSigned() {
            return signed;
        }

        public void setSigned(int signed) {
            this.signed = signed;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
