package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/5/13.
 */

public class MyDoneNumberBean {
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
         * data : {"date":"2019-04","done":1,"unRead":1}
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
             * date : 2019-04
             */

            private String date;
            private int recommendedPartner;
            private int potential;
            private int group;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getRecommendedPartner() {
                return recommendedPartner;
            }

            public void setRecommendedPartner(int recommendedPartner) {
                this.recommendedPartner = recommendedPartner;
            }

            public int getPotential() {
                return potential;
            }

            public void setPotential(int potential) {
                this.potential = potential;
            }

            public int getGroup() {
                return group;
            }

            public void setGroup(int group) {
                this.group = group;
            }
        }
    }
}
