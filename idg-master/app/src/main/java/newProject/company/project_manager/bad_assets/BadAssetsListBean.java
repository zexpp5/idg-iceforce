package newProject.company.project_manager.bad_assets;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/7/23
 */

public class BadAssetsListBean {

    /**
     * data : [{"dealLeadName":null,"dealLegalName":null,"ename":"Aircom","grade":null,"indusName":null,"projId":655,"projInDate":1531198800000},{"dealLeadName":"过以宏","dealLegalName":"熊晓鸽","ename":"","grade":"B","indusName":"娱乐/内容","projId":4,"projInDate":1531198800000},{"dealLeadName":"熊晓鸽","dealLegalName":null,"ename":"abc","grade":null,"indusName":"电商/消费","projId":3,"projInDate":1531198800000},{"dealLeadName":"孙宇含","dealLegalName":null,"ename":"果麦","grade":null,"indusName":"文化娱乐","projId":7830,"projInDate":1531112400000},{"dealLeadName":null,"dealLegalName":null,"ename":null,"grade":null,"indusName":null,"projId":5,"projInDate":1531198800000}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 5
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
         * dealLeadName : null
         * dealLegalName : null
         * ename : Aircom
         * grade : null
         * indusName : null
         * projId : 655
         * projInDate : 1531198800000
         */

        private String dealLeadName;
        private String dealLegalName;
        private String ename;
        private String grade;
        private String indusName;
        private int projId;
        private long projInDate;

        public String getDealLeadName() {
            return dealLeadName;
        }

        public void setDealLeadName(String dealLeadName) {
            this.dealLeadName = dealLeadName;
        }

        public String getDealLegalName() {
            return dealLegalName;
        }

        public void setDealLegalName(String dealLegalName) {
            this.dealLegalName = dealLegalName;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getIndusName() {
            return indusName;
        }

        public void setIndusName(String indusName) {
            this.indusName = indusName;
        }

        public int getProjId() {
            return projId;
        }

        public void setProjId(int projId) {
            this.projId = projId;
        }

        public long getProjInDate() {
            return projInDate;
        }

        public void setProjInDate(long projInDate) {
            this.projInDate = projInDate;
        }
    }
}
