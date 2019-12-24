package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/7.
 */

public class ReSearchReportTabsBean {

    /**
     * data : {"code":"success","data":[{"deptName":"全部","indusGroup":""},{"deptName":"VC组","indusGroup":"1"},{"deptName":"并购组","indusGroup":"9"},{"deptName":"PE组","indusGroup":"5354"},{"deptName":"Fintech","indusGroup":"144144"},{"deptName":"知行并进组","indusGroup":"6"},{"deptName":"地产组","indusGroup":"4"},{"deptName":"体育组","indusGroup":"7"},{"deptName":"医疗组","indusGroup":"10"},{"deptName":"General","indusGroup":"147957"},{"deptName":"智慧出行","indusGroup":"22"},{"deptName":"保险组","indusGroup":"5"},{"deptName":"Others","indusGroup":"8"},{"deptName":"新能源组","indusGroup":"87619"},{"deptName":"金融组","indusGroup":"149602"},{"deptName":"品牌组","indusGroup":"21"},{"deptName":"工业技术组","indusGroup":"2"}],"total":null}
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
         * data : [{"deptName":"全部","indusGroup":""},{"deptName":"VC组","indusGroup":"1"},{"deptName":"并购组","indusGroup":"9"},{"deptName":"PE组","indusGroup":"5354"},{"deptName":"Fintech","indusGroup":"144144"},{"deptName":"知行并进组","indusGroup":"6"},{"deptName":"地产组","indusGroup":"4"},{"deptName":"体育组","indusGroup":"7"},{"deptName":"医疗组","indusGroup":"10"},{"deptName":"General","indusGroup":"147957"},{"deptName":"智慧出行","indusGroup":"22"},{"deptName":"保险组","indusGroup":"5"},{"deptName":"Others","indusGroup":"8"},{"deptName":"新能源组","indusGroup":"87619"},{"deptName":"金融组","indusGroup":"149602"},{"deptName":"品牌组","indusGroup":"21"},{"deptName":"工业技术组","indusGroup":"2"}]
         * total : null
         */

        private String code;
        private Integer total;
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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * deptName : 全部
             * indusGroup :
             */

            private String deptName;
            private String indusGroup;
            private boolean flag;

            public String getDeptName() {
                return deptName;
            }

            public void setDeptName(String deptName) {
                this.deptName = deptName;
            }

            public String getIndusGroup() {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup) {
                this.indusGroup = indusGroup;
            }

            public boolean isFlag() {
                return flag;
            }

            public void setFlag(boolean flag) {
                this.flag = flag;
            }
        }
    }
}
