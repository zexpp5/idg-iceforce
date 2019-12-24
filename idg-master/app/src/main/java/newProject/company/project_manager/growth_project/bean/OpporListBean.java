package newProject.company.project_manager.growth_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class OpporListBean {

    /**
     * data : [{"bizDesc":null,"comIndus":"文化娱乐","invDate":1507941942000,"projId":10686,"projName":"东方梦工厂","userName":"赵立"},{"bizDesc":null,"comIndus":"汽车交通","invDate":1507923469000,"projId":10685,"projName":"首汽租车","userName":"赵立"},{"bizDesc":"K12教育培训机构","comIndus":"教育培训","invDate":1506661200000,"projId":10049,"projName":"高思教育 870155","userName":"谢昱骋"},{"bizDesc":"POS机与信贷服务提供商","comIndus":"金融","invDate":1506661200000,"projId":10050,"projName":"钱包生活","userName":"谢昱骋"},{"bizDesc":"高发癌症检测服务商","comIndus":"医疗健康","invDate":1506661200000,"projId":10063,"projName":"诺辉健康","userName":"谢昱骋"}]
     * page : 1
     * pageCount : 28
     * status : 200
     * total : 140
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
         * bizDesc : null
         * comIndus : 文化娱乐
         * invDate : 1507941942000
         * projId : 10686
         * projName : 东方梦工厂
         * userName : 赵立
         */
        private String bizDesc;
        private String comIndus;
        private long invDate;
        private int projId;
        private String projName;
        private String userName;
        private String importantStatus;

        public String getImportantStatus()
        {
            return importantStatus;
        }

        public void setImportantStatus(String importantStatus)
        {
            this.importantStatus = importantStatus;
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

        public long getInvDate() {
            return invDate;
        }

        public void setInvDate(long invDate) {
            this.invDate = invDate;
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

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }
}
