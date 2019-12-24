package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/12/15.
 */

public class InvestListBean {

    /**
     * data : [{"induName":"垂直领域","projId":2539,"projManagerName":"连盟","projName":"酒花儿/上海精酿","projStage":"接触项目"},{"induName":"垂直领域","projId":2059,"projManagerName":"楼军","projName":"轻氧智能洗衣","projStage":"行业小组讨论"}]
     * page : 1
     * pageCount : 1405
     * status : 200
     * total : 7022
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
         * induName : 垂直领域
         * projId : 2539
         * projManagerName : 连盟
         * projName : 酒花儿/上海精酿
         * projStage : 接触项目
         */

        private String induName;
        private int projId;
        private String projManagerName;
        private String projName;
        private String projStage;
        private String business;
        private int projGroup;

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public int getProjGroup() {
            return projGroup;
        }

        public void setProjGroup(int projGroup) {
            this.projGroup = projGroup;
        }

        public String getInduName() {
            return induName;
        }

        public void setInduName(String induName) {
            this.induName = induName;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public String getProjManagerName() {
            return projManagerName;
        }

        public void setProjManagerName(String projManagerName) {
            this.projManagerName = projManagerName;
        }

        public String getProjName() {
            return projName;
        }

        public void setProjName(String projName) {
            this.projName = projName;
        }

        public String getProjStage() {
            return projStage;
        }

        public void setProjStage(String projStage) {
            this.projStage = projStage;
        }
    }
}
