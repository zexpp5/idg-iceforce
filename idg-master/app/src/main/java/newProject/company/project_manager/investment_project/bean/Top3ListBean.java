package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/5.
 */

public class Top3ListBean {

    /**
     * data : {"code":"success","data":[{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"551,699,568.00","yearToYear":0},{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"551,699,568.00","yearToYear":0},{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"540,852,072.00","yearToYear":-13.371}],"total":null}
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
         * data : [{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"551,699,568.00","yearToYear":0},{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"551,699,568.00","yearToYear":0},{"monthToMonth":0,"projId":"3420","projName":"小米科技","valuation":"540,852,072.00","yearToYear":-13.371}]
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
             * monthToMonth : 0.0
             * projId : 3420
             * projName : 小米科技
             * valuation : 551,699,568.00
             * yearToYear : 0.0
             */

            private String monthToMonth;
            private String monthToMonthFlag;
            private String projId;
            private String projName;
            private String valuation;
            private String yearToYear;
            private String yearToYearFlag;
            private String valueDate;

            public String getValueDate() {
                return valueDate;
            }

            public void setValueDate(String valueDate) {
                this.valueDate = valueDate;
            }

            public String getMonthToMonth() {
                return monthToMonth;
            }

            public void setMonthToMonth(String monthToMonth) {
                this.monthToMonth = monthToMonth;
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

            public String getValuation() {
                return valuation;
            }

            public void setValuation(String valuation) {
                this.valuation = valuation;
            }

            public String getYearToYear() {
                return yearToYear;
            }

            public void setYearToYear(String yearToYear) {
                this.yearToYear = yearToYear;
            }

            public String getMonthToMonthFlag() {
                return monthToMonthFlag;
            }

            public void setMonthToMonthFlag(String monthToMonthFlag) {
                this.monthToMonthFlag = monthToMonthFlag;
            }

            public String getYearToYearFlag() {
                return yearToYearFlag;
            }

            public void setYearToYearFlag(String yearToYearFlag) {
                this.yearToYearFlag = yearToYearFlag;
            }
        }
    }
}
