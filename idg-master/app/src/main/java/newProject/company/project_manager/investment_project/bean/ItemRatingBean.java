package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/4/16.
 */

public class ItemRatingBean {

    /**
     * data : {"code":"success","data":{"businessScore":null,"comment":null,"createTime":"2019-04-15 10:21:37","projBusiness":null,"projId":"827e38fbb1c44c8994b14d091804420e","projName":"齐天大圣要上天续","rateId":"e794a7bdc79743e5a268b0beac3b3f9f","scoreDate":"2019-04-15 00:00:00","scoreName":"张屹","staus":"0","teamScore":null,"userId":"1"},"total":null}
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
         * data : {"businessScore":null,"comment":null,"createTime":"2019-04-15 10:21:37","projBusiness":null,"projId":"827e38fbb1c44c8994b14d091804420e","projName":"齐天大圣要上天续","rateId":"e794a7bdc79743e5a268b0beac3b3f9f","scoreDate":"2019-04-15 00:00:00","scoreName":"张屹","staus":"0","teamScore":null,"userId":"1"}
         * total : null
         */

        private String code;
        private DataBean data;
        private Integer total;

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

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public static class DataBean {
            /**
             * businessScore : null
             * comment : null
             * createTime : 2019-04-15 10:21:37
             * projBusiness : null
             * projId : 827e38fbb1c44c8994b14d091804420e
             * projName : 齐天大圣要上天续
             * rateId : e794a7bdc79743e5a268b0beac3b3f9f
             * scoreDate : 2019-04-15 00:00:00
             * scoreName : 张屹
             * staus : 0
             * teamScore : null
             * userId : 1
             */

            private String businessScore;
            private String comment;
            private String createTime;
            private String projBusiness;
            private String projId;
            private String projName;
            private String rateId;
            private String scoreDate;
            private String scoreName;
            private String staus;
            private String teamScore;
            private String userId;

            public String getBusinessScore() {
                return businessScore;
            }

            public void setBusinessScore(String businessScore) {
                this.businessScore = businessScore;
            }

            public String getComment() {
                return comment;
            }

            public void setComment(String comment) {
                this.comment = comment;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public String getProjBusiness() {
                return projBusiness;
            }

            public void setProjBusiness(String projBusiness) {
                this.projBusiness = projBusiness;
            }

            public String getProjId() {
                return projId;
            }

            public void setProjId(String projId) {
                this.projId = projId;
            }

            public String getProjName() {
                return projName;
            }

            public void setProjName(String projName) {
                this.projName = projName;
            }

            public String getRateId() {
                return rateId;
            }

            public void setRateId(String rateId) {
                this.rateId = rateId;
            }

            public String getScoreDate() {
                return scoreDate;
            }

            public void setScoreDate(String scoreDate) {
                this.scoreDate = scoreDate;
            }

            public String getScoreName() {
                return scoreName;
            }

            public void setScoreName(String scoreName) {
                this.scoreName = scoreName;
            }

            public String getStaus() {
                return staus;
            }

            public void setStaus(String staus) {
                this.staus = staus;
            }

            public String getTeamScore() {
                return teamScore;
            }

            public void setTeamScore(String teamScore) {
                this.teamScore = teamScore;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }
        }
    }
}
