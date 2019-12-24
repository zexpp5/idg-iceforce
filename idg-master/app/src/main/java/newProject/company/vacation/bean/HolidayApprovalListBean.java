package newProject.company.vacation.bean;

import java.util.List;

/**
 * Created by tujingwu on 2017/11/21.
 */

public class HolidayApprovalListBean {


    /**
     * data : [{"applyId":"662587.1319308584","approveId":27,"currentApprove":"qiuyue_zhong","holidayType":"Others","isApprove":"0","leaveDay":0,"leaveEnd":"","leaveId":59,"leaveStart":"","name":""}]
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
         * applyId : 662587.1319308584
         * approveId : 27
         * currentApprove : qiuyue_zhong
         * holidayType : Others
         * isApprove : 0
         * leaveDay : 0
         * leaveEnd :
         * leaveId : 59
         * leaveStart :
         * name :
         */

        private String applyId;
        private int approveId;
        private String currentApprove;
        private String holidayType;
        private String isApprove;
        private double leaveDay;
        private String leaveEnd;
        private int leaveId;
        private String leaveStart;
        private String name;

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

        public String getCurrentApprove() {
            return currentApprove;
        }

        public void setCurrentApprove(String currentApprove) {
            this.currentApprove = currentApprove;
        }

        public String getHolidayType() {
            return holidayType;
        }

        public void setHolidayType(String holidayType) {
            this.holidayType = holidayType;
        }

        public String getIsApprove() {
            return isApprove;
        }

        public void setIsApprove(String isApprove) {
            this.isApprove = isApprove;
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

        public String getLeaveStart() {
            return leaveStart;
        }

        public void setLeaveStart(String leaveStart) {
            this.leaveStart = leaveStart;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
