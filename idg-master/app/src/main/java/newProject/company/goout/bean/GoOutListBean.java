package newProject.company.goout.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/10/21.
 */

public class GoOutListBean {

    /**
     * data : [{"UPDATE_TIME":1508546289000,"createTime":"2017-10-21","eid":8,"reason":"拜访客户","remark":"老客户生日，需要回馈客户","statusName":"批审中","ygDeptName":"未归类","ygName":"yesido"},{"UPDATE_TIME":1508502889000,"createTime":"2017-10-20","eid":5,"reason":"拜访客户","remark":"老客户生日，需要回馈客户","statusName":"批审中","ygDeptName":"未归类","ygName":"yesido"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 2
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<GoOutDataBean> data;

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

    public List<GoOutDataBean> getData() {
        return data;
    }

    public void setData(List<GoOutDataBean> data) {
        this.data = data;
    }

    public static class GoOutDataBean {
        /**
         * UPDATE_TIME : 1508546289000
         * createTime : 2017-10-21
         * eid : 8
         * reason : 拜访客户
         * remark : 老客户生日，需要回馈客户
         * statusName : 批审中
         * ygDeptName : 未归类
         * ygName : yesido
         */

        private long UPDATE_TIME;
        private String createTime;
        private int eid;
        private String reason;
        private String remark;
        private String statusName;
        private String ygDeptName;
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

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public String getYgDeptName() {
            return ygDeptName;
        }

        public void setYgDeptName(String ygDeptName) {
            this.ygDeptName = ygDeptName;
        }

        public String getYgName() {
            return ygName;
        }

        public void setYgName(String ygName) {
            this.ygName = ygName;
        }
    }
}
