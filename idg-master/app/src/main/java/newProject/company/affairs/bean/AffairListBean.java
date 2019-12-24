package newProject.company.affairs.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */

public class AffairListBean {


    /**
     * data : [{"UPDATE_TIME":1508494197000,"createTime":"2017-10-20","eid":17,"statusName":"批审中","title":"报告标题","ygDeptId":21,"ygDeptName":"未归类","ygId":13,"ygName":null}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 1
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<AffairListDataBean> data;

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

    public List<AffairListDataBean> getData() {
        return data;
    }

    public void setData(List<AffairListDataBean> data) {
        this.data = data;
    }

    public static class AffairListDataBean {
        /**
         * UPDATE_TIME : 1508494197000
         * createTime : 2017-10-20
         * eid : 17
         * statusName : 批审中
         * title : 报告标题
         * ygDeptId : 21
         * ygDeptName : 未归类
         * ygId : 13
         * ygName : null
         */

        private long UPDATE_TIME;
        private String createTime;
        private int eid;
        private String statusName;
        private String title;
        private int ygDeptId;
        private String ygDeptName;
        private int ygId;
        private String ygName;

        public long getUPDATE_TIME() {
            return UPDATE_TIME;
        }

        public void setUPDATE_TIME(long UPDATE_TIME) {
            this.UPDATE_TIME = UPDATE_TIME;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getYgDeptId() {
            return ygDeptId;
        }

        public void setYgDeptId(int ygDeptId) {
            this.ygDeptId = ygDeptId;
        }

        public String getYgDeptName() {
            return ygDeptName;
        }

        public void setYgDeptName(String ygDeptName) {
            this.ygDeptName = ygDeptName;
        }

        public int getYgId() {
            return ygId;
        }

        public void setYgId(int ygId) {
            this.ygId = ygId;
        }

        public String getYgName() {
            return ygName;
        }

        public void setYgName(String ygName) {
            this.ygName = ygName;
        }
    }
}
