package newProject.company.project_manager.growth_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/28.
 */

public class PeDetailBean {


    /**
     * data : {"bizDesc":null,"comIndus":"文化娱乐","invContactStatus":"date","invDate":1507941942000,"invFlowUp":"flowUp","invGroup":null,"invRound":null,"projId":10686,"projName":"东方梦工厂","ptProjFollows":[{"devDate":1507870800000,"devId":526,"invFlowUp":"flowUp","keyNote":null,"projId":10686},{"devDate":1507870800000,"devId":17,"invFlowUp":"flowUp","keyNote":null,"projId":10686}],"region":null,"userName":"赵立"}
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
         * bizDesc : null
         * comIndus : 文化娱乐
         * invContactStatus : date
         * invDate : 1507941942000
         * invFlowUp : flowUp
         * invGroup : null
         * invRound : null
         * projId : 10686
         * projName : 东方梦工厂
         * ptProjFollows : [{"devDate":1507870800000,"devId":526,"invFlowUp":"flowUp","keyNote":null,"projId":10686},{"devDate":1507870800000,"devId":17,"invFlowUp":"flowUp","keyNote":null,"projId":10686}]
         * region : null
         * userName : 赵立
         */

        private String bizDesc;
        private String comIndus;
        private String invContactStatus;
        private String invDate;
        private String invFlowUp;
        private String invGroup;
        private String invRound;
        private int projId;
        private String projName;
        private String region;
        private String userName;
        private String invStatus;
        private int importantStatus;

        private List<PtProjFollowsBean> ptProjFollows;

        public String getInvStatus() {
            return invStatus;
        }

        public int getImportantStatus()
        {
            return importantStatus;
        }

        public void setImportantStatus(int importantStatus)
        {
            this.importantStatus = importantStatus;
        }

        public void setInvStatus(String invStatus) {
            this.invStatus = invStatus;
        }

        public String getBizDesc() {
            return bizDesc;
        }

        public void setBizDesc(String bizDesc) {
            this.bizDesc = bizDesc;
        }

        public String getComIndus() {
            return comIndus;
        }

        public void setComIndus(String comIndus) {
            this.comIndus = comIndus;
        }

        public String getInvContactStatus() {
            return invContactStatus;
        }

        public void setInvContactStatus(String invContactStatus) {
            this.invContactStatus = invContactStatus;
        }

        public String getInvDate() {
            return invDate;
        }

        public void setInvDate(String invDate) {
            this.invDate = invDate;
        }

        public String getInvFlowUp() {
            return invFlowUp;
        }

        public void setInvFlowUp(String invFlowUp) {
            this.invFlowUp = invFlowUp;
        }

        public String getInvGroup() {
            return invGroup;
        }

        public void setInvGroup(String invGroup) {
            this.invGroup = invGroup;
        }

        public String getInvRound() {
            return invRound;
        }

        public void setInvRound(String invRound) {
            this.invRound = invRound;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public List<PtProjFollowsBean> getPtProjFollows() {
            return ptProjFollows;
        }

        public void setPtProjFollows(List<PtProjFollowsBean> ptProjFollows) {
            this.ptProjFollows = ptProjFollows;
        }

        public static class PtProjFollowsBean {
            /**
             * devDate : 1507870800000
             * devId : 526
             * invFlowUp : flowUp
             * keyNote : null
             * projId : 10686
             */

            private String devDate;
            private int devId;
            private String invFlowUp;
            private String keyNote;
            private int projId;

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
}
