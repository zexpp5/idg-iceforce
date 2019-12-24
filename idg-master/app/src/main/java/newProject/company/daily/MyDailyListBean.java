package newProject.company.daily;

import java.util.List;

/**
 * Created by Administrator on 2017/10/23.
 */

public class MyDailyListBean {

    /**
     * data : [{"createTime":"2017-10-21","eid":7,"title":"日志标题22","ygDeptId":28,"ygDeptName":"销售部","ygId":16,"ygName":"小明"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 1
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<MyDailyDataBean> data;

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

    public List<MyDailyDataBean> getData() {
        return data;
    }

    public void setData(List<MyDailyDataBean> data) {
        this.data = data;
    }

    public static class MyDailyDataBean {
        /**
         * createTime : 2017-10-21
         * eid : 7
         * title : 日志标题22
         * ygDeptId : 28
         * ygDeptName : 销售部
         * ygId : 16
         * ygName : 小明
         */

        private String createTime;
        private int eid;
        private String title;
        private int ygDeptId;
        private String ygDeptName;
        private int ygId;
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
