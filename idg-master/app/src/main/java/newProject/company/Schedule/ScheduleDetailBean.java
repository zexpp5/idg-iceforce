package newProject.company.Schedule;

import com.entity.administrative.employee.Annexdata;

import java.util.List;

import newProject.bean.CCBean;

/**
 * Created by Administrator on 2017/11/20.
 */

public class ScheduleDetailBean {

    /**
     * data : {"meet":{"createId":2,"eid":148,"meetPlace":"体育西","remark":"测试看看成不","startTime":"2017-11-17","title":"日常会议","type":2}}
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
         * meet : {"createId":2,"eid":148,"meetPlace":"体育西","remark":"测试看看成不","startTime":"2017-11-17","title":"日常会议","type":2}
         */
        private MeetBean meet;
        private List<Annexdata> annexList;
        private List<CCBean> ccList;
        private List<String> groupNames;

        public List<String> getGroupNames()
        {
            return groupNames;
        }

        public void setGroupNames(List<String> groupNames)
        {
            this.groupNames = groupNames;
        }

        public List<CCBean> getCcList() {
            return ccList;
        }

        public void setCcList(List<CCBean> ccList) {
            this.ccList = ccList;
        }

        public List<Annexdata> getAnnexList() {
            return annexList;
        }

        public void setAnnexList(List<Annexdata> annexList) {
            this.annexList = annexList;
        }

        public MeetBean getMeet() {
            return meet;
        }

        public void setMeet(MeetBean meet) {
            this.meet = meet;
        }

        public static class MeetBean {
            /**
             * createId : 2
             * eid : 148
             * meetPlace : 体育西
             * remark : 测试看看成不
             * startTime : 2017-11-17
             * title : 日常会议
             * type : 2
             */

            private int createId;
            private int eid;
            private String meetPlace;
            private String remark;
            private String startTime;
            private String endTime;
            private String title;
            private int type;

            public String getEndTime() {
                return endTime;
            }

            public void setEndTime(String endTime) {
                this.endTime = endTime;
            }

            public int getCreateId() {
                return createId;
            }

            public void setCreateId(int createId) {
                this.createId = createId;
            }

            public int getEid() {
                return eid;
            }

            public void setEid(int eid) {
                this.eid = eid;
            }

            public String getMeetPlace() {
                return meetPlace;
            }

            public void setMeetPlace(String meetPlace) {
                this.meetPlace = meetPlace;
            }

            public String getRemark() {
                return remark;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }
        }
    }
}
