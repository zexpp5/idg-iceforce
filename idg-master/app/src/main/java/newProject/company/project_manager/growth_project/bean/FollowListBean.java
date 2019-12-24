package newProject.company.project_manager.growth_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/3/1.
 */

public class FollowListBean {

    /**
     * data : [{"devDate":1514613600000,"devId":531,"invFlowUp":"flowUp","keyNote":"更新1","projId":10686},{"devDate":1514613600000,"devId":532,"invFlowUp":"flowUp","keyNote":"更新2","projId":10686},{"devDate":1514613600000,"devId":533,"invFlowUp":"flowUp","keyNote":"更新3","projId":10686},{"devDate":1514613600000,"devId":534,"invFlowUp":"flowUp","keyNote":"更新4","projId":10686},{"devDate":1514613600000,"devId":535,"invFlowUp":"flowUp","keyNote":"更新5","projId":10686}]
     * page : 1
     * pageCount : 2
     * status : 200
     * total : 7
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * devDate : 1514613600000
         * devId : 531
         * invFlowUp : flowUp
         * keyNote : 更新1
         * projId : 10686
         */

        private String devDate;
        private int devId;
        private String invFlowUp;
        private String keyNote;
        private int projId;
        private String followPerson;

        public String getFollowPerson() {
            return followPerson;
        }

        public void setFollowPerson(String followPerson) {
            this.followPerson = followPerson;
        }

        public String getDevDate() {
            return devDate;
        }

        public void setDevDate(String devDate) {
            this.devDate = devDate;
        }

        public int getDevId() {
            return devId;
        }

        public void setDevId(int devId) {
            this.devId = devId;
        }

        public String getInvFlowUp() {
            return invFlowUp;
        }

        public void setInvFlowUp(String invFlowUp) {
            this.invFlowUp = invFlowUp;
        }

        public String getKeyNote() {
            return keyNote;
        }

        public void setKeyNote(String keyNote) {
            this.keyNote = keyNote;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }
    }
}
