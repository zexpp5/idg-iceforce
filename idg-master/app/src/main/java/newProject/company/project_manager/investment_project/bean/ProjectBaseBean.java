package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/4/18.
 */

public class ProjectBaseBean {

    /**
     * data : {"code":"success","data":[{"projId":"99f245c6a61e4b6aaa2369708b39828a","projName":"Y"},{"projId":"c31699e5cbc043e09a5273ed8ad27b1b","projName":"Y"},{"projId":"f1d478b596e040bfa4abc79a87eec824","projName":"Yeah "}],"total":null}
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
         * data : [{"projId":"99f245c6a61e4b6aaa2369708b39828a","projName":"Y"},{"projId":"c31699e5cbc043e09a5273ed8ad27b1b","projName":"Y"},{"projId":"f1d478b596e040bfa4abc79a87eec824","projName":"Yeah "}]
         * total : null
         */

        private String code;
        private String total;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
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
             * projId : 99f245c6a61e4b6aaa2369708b39828a
             * projName : Y
             */

            private String projId;
            private String projName;

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
        }
    }
}
