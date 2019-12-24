package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/8.
 */

public class NewsLetterItemListBean {

    /**
     * data : {"code":"success","data":[{"canRead":true,"canWrite":true,"createBy":"张屹","createDate":"2019-04-25 16:24:04","docId":"109d69f4c4044a1ca73a8dd1e00f2f1d","docName":"fdsga","editBy":null,"editDate":null,"fileId":"5cc16ea4972c8737381cf2c2","indusGroup":"21","indusGroupName":"品牌组","newsDate":"2019-04-03 00:00:00","summary":"cvv"}],"total":1}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static class DataBeanX {
        /**
         * code : success
         * data : [{"canRead":true,"canWrite":true,"createBy":"张屹","createDate":"2019-04-25 16:24:04","docId":"109d69f4c4044a1ca73a8dd1e00f2f1d","docName":"fdsga","editBy":null,"editDate":null,"fileId":"5cc16ea4972c8737381cf2c2","indusGroup":"21","indusGroupName":"品牌组","newsDate":"2019-04-03 00:00:00","summary":"cvv"}]
         * total : 1
         */

        private String code;
        private Integer total;
        private List<DataBean> data;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
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
             * canRead : true
             * canWrite : true
             * createBy : 张屹
             * createDate : 2019-04-25 16:24:04
             * docId : 109d69f4c4044a1ca73a8dd1e00f2f1d
             * docName : fdsga
             * editBy : null
             * editDate : null
             * fileId : 5cc16ea4972c8737381cf2c2
             * indusGroup : 21
             * indusGroupName : 品牌组
             * newsDate : 2019-04-03 00:00:00
             * summary : cvv
             */

            private boolean canRead;
            private boolean canWrite;
            private String createBy;
            private String createDate;
            private String docId;
            private String docName;
            private String editBy;
            private String editDate;
            private String fileId;
            private String indusGroup;
            private String indusGroupName;
            private String newsDate;
            private String summary;

            public boolean isCanRead() {
                return canRead;
            }

            public void setCanRead(boolean canRead) {
                this.canRead = canRead;
            }

            public boolean isCanWrite() {
                return canWrite;
            }

            public void setCanWrite(boolean canWrite) {
                this.canWrite = canWrite;
            }

            public String getCreateBy() {
                return createBy;
            }

            public void setCreateBy(String createBy) {
                this.createBy = createBy;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getDocId() {
                return docId;
            }

            public void setDocId(String docId) {
                this.docId = docId;
            }

            public String getDocName() {
                return docName;
            }

            public void setDocName(String docName) {
                this.docName = docName;
            }

            public String getEditBy() {
                return editBy;
            }

            public void setEditBy(String editBy) {
                this.editBy = editBy;
            }

            public String getEditDate() {
                return editDate;
            }

            public void setEditDate(String editDate) {
                this.editDate = editDate;
            }

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public String getIndusGroup() {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup) {
                this.indusGroup = indusGroup;
            }

            public String getIndusGroupName() {
                return indusGroupName;
            }

            public void setIndusGroupName(String indusGroupName) {
                this.indusGroupName = indusGroupName;
            }

            public String getNewsDate() {
                return newsDate;
            }

            public void setNewsDate(String newsDate) {
                this.newsDate = newsDate;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }
        }
    }
}
