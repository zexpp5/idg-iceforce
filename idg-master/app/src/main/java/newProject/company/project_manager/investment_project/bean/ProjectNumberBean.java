package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/4/30.
 */

public class ProjectNumberBean {

    /**
     * data : {"code":"success","data":{"followUp":1,"invested":1,"potencial":9},"total":null}
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
         * data : {"followUp":1,"invested":1,"potencial":9}
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
             * followUp : 1
             * invested : 1
             * potencial : 9
             */

            private int followUp;
            private int invested;
            private int potencial;

            public int getFollowUp() {
                return followUp;
            }

            public void setFollowUp(int followUp) {
                this.followUp = followUp;
            }

            public int getInvested() {
                return invested;
            }

            public void setInvested(int invested) {
                this.invested = invested;
            }

            public int getPotencial() {
                return potencial;
            }

            public void setPotencial(int potencial) {
                this.potencial = potencial;
            }
        }
    }
}
