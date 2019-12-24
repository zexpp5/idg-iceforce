package newProject.company.tools;

import java.util.List;

/**
 * Created by Administrator on 2018/3/3.
 */

public class ToolsListBean {


    /**
     * data : [{"eid":10,"publishTime":"2018-01-29","title":"算法"},{"eid":2,"publishTime":"2018-01-29","title":"需求"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 2
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
         * eid : 10
         * publishTime : 2018-01-29
         * title : 算法
         */

        private int eid;
        private String publishTime;
        private String title;

        public int getEid() {
            return eid;
        }

        public void setEid(int eid) {
            this.eid = eid;
        }

        public String getPublishTime() {
            return publishTime;
        }

        public void setPublishTime(String publishTime) {
            this.publishTime = publishTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
