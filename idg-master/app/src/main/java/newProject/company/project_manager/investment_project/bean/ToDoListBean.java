package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/16.
 */

public class ToDoListBean {

    /**
     * data : {"code":"success","data":[{"busId":"827e38fbb1c44c8994b14d091804420e","busType":"项目评分","projName":null,"projType":null,"remark":"试一试","showDesc":"黄建爽@你,需要对项目进行评分 ","sourceUser":"黄建爽"},{"busId":"09d85741934d49e28c1e031c372dd37a","busType":"项目评分","projName":null,"projType":null,"remark":"看看这个项目","showDesc":"张屹@你,需要对项目进行评分 ","sourceUser":"张屹"},{"busId":"09d85741934d49e28c1e031c372dd37a","busType":"跟进项目","projName":"ABCDEFG","projType":"POTENTIAL","remark":"看看这个项目","showDesc":"张屹@你,需要跟进项目的跟踪进展 ","sourceUser":"张屹"}],"total":3}
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
         * data : [{"busId":"827e38fbb1c44c8994b14d091804420e","busType":"项目评分","projName":null,"projType":null,"remark":"试一试","showDesc":"黄建爽@你,需要对项目进行评分 ","sourceUser":"黄建爽"},{"busId":"09d85741934d49e28c1e031c372dd37a","busType":"项目评分","projName":null,"projType":null,"remark":"看看这个项目","showDesc":"张屹@你,需要对项目进行评分 ","sourceUser":"张屹"},{"busId":"09d85741934d49e28c1e031c372dd37a","busType":"跟进项目","projName":"ABCDEFG","projType":"POTENTIAL","remark":"看看这个项目","showDesc":"张屹@你,需要跟进项目的跟踪进展 ","sourceUser":"张屹"}]
         * total : 3
         */

        private String code;
        private Integer total;
        private String returnMessage;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public String getReturnMessage() {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage) {
            this.returnMessage = returnMessage;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * busId : 827e38fbb1c44c8994b14d091804420e
             * busType : 项目评分
             * projName : null
             * projType : null
             * remark : 试一试
             * showDesc : 黄建爽@你,需要对项目进行评分 
             * sourceUser : 黄建爽
             */

            private String busId;
            private String busType;
            private String busTypeStr;
            private String projName;
            private String projType;
            private String remark;
            private String showDesc;
            private String sourceUser;
            private String validDate;
            //
            private String applyUser;
            private String origBusId;

            //
            private String msgId;

            //
            private String year;
            private String reportFrequency;
            private String dateStr;

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getReportFrequency() {
                return reportFrequency;
            }

            public void setReportFrequency(String reportFrequency) {
                this.reportFrequency = reportFrequency;
            }

            public String getDateStr() {
                return dateStr;
            }

            public void setDateStr(String dateStr) {
                this.dateStr = dateStr;
            }

            public String getMsgId() {
                return msgId;
            }

            public void setMsgId(String msgId) {
                this.msgId = msgId;
            }

            public String getBusId() {
                return busId;
            }

            public void setBusId(String busId) {
                this.busId = busId;
            }

            public String getBusType() {
                return busType;
            }

            public void setBusType(String busType) {
                this.busType = busType;
            }

            public String getBusTypeStr() {
                return busTypeStr;
            }

            public void setBusTypeStr(String busTypeStr) {
                this.busTypeStr = busTypeStr;
            }

            public String getProjName() {
                return projName;
            }

            public void setProjName(String projName) {
                this.projName = projName;
            }

            public String getProjType() {
                return projType;
            }

            public void setProjType(String projType) {
                this.projType = projType;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getShowDesc() {
                return showDesc;
            }

            public void setShowDesc(String showDesc) {
                this.showDesc = showDesc;
            }

            public String getSourceUser() {
                return sourceUser;
            }

            public void setSourceUser(String sourceUser) {
                this.sourceUser = sourceUser;
            }

            public String getValidDate() {
                return validDate;
            }

            public void setValidDate(String validDate) {
                this.validDate = validDate;
            }

            public String getApplyUser() {
                return applyUser;
            }

            public void setApplyUser(String applyUser) {
                this.applyUser = applyUser;
            }

            public String getOrigBusId() {
                return origBusId;
            }

            public void setOrigBusId(String origBusId) {
                this.origBusId = origBusId;
            }
        }
    }
}
