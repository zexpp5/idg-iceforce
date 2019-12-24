package newProject.company.daily;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

import newProject.bean.CCBean;


/**
 * Created by Administrator on 2017/10/23.
 */

public class MyDailyDetailBean {


    /**
     * data : {"annexList":[],"ccList":[],"workLog":{"createId":16,"createTime":"2017-10-21","eid":7,"remark":"日志内容22","serNo":"SW1710210007","title":"日志标题22","ygDeptId":28,"ygDeptName":"销售部","ygId":16,"ygJob":"","ygName":"莉莉1"}}
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
         * ccList : []
         * workLog : {"createId":16,"createTime":"2017-10-21","eid":7,"remark":"日志内容22","serNo":"SW1710210007","title":"日志标题22","ygDeptId":28,"ygDeptName":"销售部","ygId":16,"ygJob":"","ygName":"莉莉1"}
         */

        private WorkLogBean workLog;
        private List<Annexdata> annexList;
        private List<CCBean> ccList;

        public WorkLogBean getWorkLog() {
            return workLog;
        }

        public void setWorkLog(WorkLogBean workLog) {
            this.workLog = workLog;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public List<CCBean> getCcList() {
            return ccList;
        }

        public void setCcList(List<CCBean> ccList) {
            this.ccList = ccList;
        }

        public static class WorkLogBean {
            /**
             * createId : 16
             * createTime : 2017-10-21
             * eid : 7
             * remark : 日志内容22
             * serNo : SW1710210007
             * title : 日志标题22
             * ygDeptId : 28
             * ygDeptName : 销售部
             * ygId : 16
             * ygJob :
             * ygName : 莉莉1
             */

            private int createId;
            private String createTime;
            private int eid;
            private String remark;
            private String serNo;
            private String title;
            private int ygDeptId;
            private String ygDeptName;
            private int ygId;
            private String ygJob;
            private String ygName;
            private String icon;

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
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

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
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
        }
    }
}
