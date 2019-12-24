package newProject.company.project_manager.investment_project.bean;

/**
 * Created by zsz on 2019/4/30.
 */

public class MyFollowOnNumberBean {

    /**
     * data : {"code":"success","data":{"date":"2019-04","done":1,"unRead":1},"total":null}
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
             * done : 1
             * unRead : 1
             */

            private String date;
            private int done;
            private int unRead;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getDone() {
                return done;
            }

            public void setDone(int done) {
                this.done = done;
            }

            public int getUnRead() {
                return unRead;
            }

            public void setUnRead(int unRead) {
                this.unRead = unRead;
            }
        }
    }
}
