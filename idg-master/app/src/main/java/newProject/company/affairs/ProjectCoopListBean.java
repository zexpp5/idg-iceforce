package newProject.company.affairs;

import java.util.List;

/**
 * Created by Administrator on 2017/10/25.
 */

public class ProjectCoopListBean {

    /**
     * data : []
     * pageCount : 1
     * status : 200
     * total : 3
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<ProjectDataBean> data;

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

    public List<ProjectDataBean> getData() {
        return data;
    }

    public void setData(List<ProjectDataBean> data) {
        this.data = data;
    }

    public static class ProjectDataBean {
        /**
         * createTime : 2017-10-21
         * eid : 5
         * remark : 需要你们协作处理
         * title : wim项目
         * ygDeptName : 未归类
         * ygName : yesido
         */

        private String createTime;
        private int eid;
        private String remark;
        private String title;
        private String ygDeptName;
        private String ygName;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
