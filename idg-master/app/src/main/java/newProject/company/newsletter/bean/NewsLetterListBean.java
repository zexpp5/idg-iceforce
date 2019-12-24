package newProject.company.newsletter.bean;

import java.util.List;

/**
 * @author: Created by Freeman on 2018/8/10
 */

public class NewsLetterListBean {


    /**
     * data : [{"docId":1003,"docName":"测试报告","indusGroupName":"工业技术组","newsDate":1533657600000,"summary":"阿斯顿发大水发阿斯顿发大水发是的发生发大水发的发的说法大是大非","uploadUser":"张屹"},{"docId":1002,"docName":"abc","indusGroupName":"工业技术组","newsDate":1533571200000,"summary":"abc","uploadUser":"张屹"},{"docId":1001,"docName":"abc","indusGroupName":"地产组","newsDate":1533571200000,"summary":"abc","uploadUser":"张屹"}]
     * page : 1
     * pageCount : 1
     * status : 200
     * total : 3
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
         * docId : 1003
         * docName : 测试报告
         * indusGroupName : 工业技术组
         * newsDate : 1533657600000
         * summary : 阿斯顿发大水发阿斯顿发大水发是的发生发大水发的发的说法大是大非
         * uploadUser : 张屹
         */

        private int docId;
        private String docName;
        private String indusGroupName;
        private long newsDate;
        private String summary;
        private String uploadUser;

        public int getDocId() {
            return docId;
        }

        public void setDocId(int docId) {
            this.docId = docId;
        }

        public String getDocName() {
            return docName;
        }

        public void setDocName(String docName) {
            this.docName = docName;
        }

        public String getIndusGroupName() {
            return indusGroupName;
        }

        public void setIndusGroupName(String indusGroupName) {
            this.indusGroupName = indusGroupName;
        }

        public long getNewsDate() {
            return newsDate;
        }

        public void setNewsDate(long newsDate) {
            this.newsDate = newsDate;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getUploadUser() {
            return uploadUser;
        }

        public void setUploadUser(String uploadUser) {
            this.uploadUser = uploadUser;
        }
    }
}
