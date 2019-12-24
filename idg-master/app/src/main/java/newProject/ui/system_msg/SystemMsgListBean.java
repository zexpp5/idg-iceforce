package newProject.ui.system_msg;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/7/24
 */

public class SystemMsgListBean {

    /**
     * data : [{"bid":0,"btype":2,"content":"张屹于2018-07-27 09:10:43提交的请假申请等待您的审核。","createTime":"2018-07-27 09:10:43","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 17:40:07提交的请假申请等待您的审核。","createTime":"2018-07-26 17:40:07","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 17:36:52提交的请假申请等待您的审核。","createTime":"2018-07-26 17:36:52","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 17:28:34提交的请假申请等待您的审核。","createTime":"2018-07-26 17:28:34","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 14:57:55提交的请假申请等待您的审核。","createTime":"2018-07-26 14:57:55","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 14:35:55提交的请假申请等待您的审核。","createTime":"2018-07-26 14:35:55","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 14:26:41提交的请假申请等待您的审核。","createTime":"2018-07-26 14:26:41","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 14:10:06提交的请假申请等待您的审核。","createTime":"2018-07-26 14:10:06","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 14:09:25提交的请假申请等待您的审核。","createTime":"2018-07-26 14:09:24","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 13:53:44提交的请假申请等待您的审核。","createTime":"2018-07-26 13:53:44","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 13:47:11提交的请假申请等待您的审核。","createTime":"2018-07-26 13:47:11","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 13:42:30提交的请假申请等待您的审核。","createTime":"2018-07-26 13:42:29","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 10:31:08提交的请假申请等待您的审核。","createTime":"2018-07-26 10:31:07","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 10:29:04提交的请假申请等待您的审核。","createTime":"2018-07-26 10:29:03","title":"【待审核】您有一条待审核内容"},{"bid":0,"btype":2,"content":"张屹于2018-07-26 10:27:53提交的请假申请等待您的审核。","createTime":"2018-07-26 10:27:52","title":"【待审核】您有一条待审核内容"}]
     * page : 1
     * pageCount : 2
     * status : 200
     * total : 28
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
         * bid : 0
         * btype : 2
         * content : 张屹于2018-07-27 09:10:43提交的请假申请等待您的审核。
         * createTime : 2018-07-27 09:10:43
         * title : 【待审核】您有一条待审核内容
         */

        private int bid;
        private int btype;
        private String content;
        private String createTime;
        private String title;

        public int getBid() {
            return bid;
        }

        public void setBid(int bid) {
            this.bid = bid;
        }

        public int getBtype() {
            return btype;
        }

        public void setBtype(int btype) {
            this.btype = btype;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
