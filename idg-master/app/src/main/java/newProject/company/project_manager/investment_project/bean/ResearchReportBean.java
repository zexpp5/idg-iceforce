package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/27.
 */

public class ResearchReportBean {

    /**
     * data : [{"authorName":"ssss","docId":55,"docName":"aaaa","induName":null,"keyWord":"dddd","summary":"sdsssss"},{"authorName":"zz","docId":56,"docName":"zz","induName":null,"keyWord":"zz","summary":"zz"},{"authorName":"tt","docId":59,"docName":"tt","induName":null,"keyWord":"tt","summary":"tt"}]
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
         * authorName : ssss
         * docId : 55
         * docName : aaaa
         * induName : null
         * keyWord : dddd
         * summary : sdsssss
         */

        private String authorName;
        private int docId;
        private String docName;
        private String induName;
        private String keyWord;
        private String summary;
        private int indusGroup;

        public int getIndusGroup() {
            return indusGroup;
        }

        public void setIndusGroup(int indusGroup) {
            this.indusGroup = indusGroup;
        }

        public String getAuthorName() {
            return authorName;
        }

        public void setAuthorName(String authorName) {
            this.authorName = authorName;
        }

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

        public String getInduName() {
            return induName;
        }

        public void setInduName(String induName) {
            this.induName = induName;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }
}
