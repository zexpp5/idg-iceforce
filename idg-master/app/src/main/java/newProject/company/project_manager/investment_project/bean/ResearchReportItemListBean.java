package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by zsz on 2019/5/7.
 */

public class ResearchReportItemListBean {

    /**
     * data : {"code":"success","data":[{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:12:53","docDate":"2017-08-28 00:00:00","docId":"110","docName":"BAT海外","editBy":"1110","editDate":"2018-02-06 15:12:53","fileId":"5cb6a153f37d454b68caad9e","induId":"垂直领域","indusGroup":"VC组","keyWord":"海外","summary":"海外"},{"authorName":"黄峰，江左","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:14:23","docDate":"2017-08-28 00:00:00","docId":"111","docName":"ICO报告","editBy":"1110","editDate":"2018-02-06 15:14:23","fileId":"5cb6a153f37d454b68caada1","induId":"互联网金融","indusGroup":"VC组","keyWord":"ICO","summary":"ICO"},{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:15:33","docDate":"2017-08-28 00:00:00","docId":"112","docName":"海外Update_Aug.vf","editBy":"1110","editDate":"2018-02-06 15:15:33","fileId":"5cb6a154f37d454b68caada5","induId":"垂直领域","indusGroup":"VC组","keyWord":"海外","summary":"海外"},{"authorName":"王辛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:16:27","docDate":"2017-08-28 00:00:00","docId":"113","docName":"教育行业思路","editBy":"1110","editDate":"2018-02-06 15:16:27","fileId":"5cb6a154f37d454b68caada9","induId":"垂直领域","indusGroup":"VC组","keyWord":"教育","summary":"教育"},{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:19:09","docDate":"2017-10-14 00:00:00","docId":"114","docName":"2017 Oct. Top VC Update.vf","editBy":"1110","editDate":"2018-02-06 15:19:09","fileId":"5cb6a154f37d454b68caadad","induId":"垂直领域","indusGroup":"VC组","keyWord":"top vc","summary":"top vc"},{"authorName":"张公涛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:20:34","docDate":"2017-10-14 00:00:00","docId":"115","docName":"软银投资策略研究","editBy":"1110","editDate":"2018-02-06 15:20:34","fileId":"5cb6a154f37d454b68caadb2","induId":"垂直领域","indusGroup":"VC组","keyWord":"软银","summary":"软银"},{"authorName":"陈润曦","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:22:12","docDate":"2017-10-14 00:00:00","docId":"116","docName":"美国新零售品牌趋势","editBy":"1110","editDate":"2018-02-06 15:22:12","fileId":"5cb6a155f37d454b68caadbb","induId":"电商/消费","indusGroup":"VC组","keyWord":"美国 新零售","summary":"美国新零售"},{"authorName":"张公涛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:28:19","docDate":"2017-12-14 00:00:00","docId":"118","docName":"雾计算时代的创新介绍","editBy":"1110","editDate":"2018-02-06 15:28:19","fileId":"5cb6a155f37d454b68caadbe","induId":"垂直领域","indusGroup":"VC组","keyWord":"软银","summary":"软银"},{"authorName":"王辛，程沧海","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:29:11","docDate":"2017-12-14 00:00:00","docId":"119","docName":"汽车分时租赁","editBy":"1110","editDate":"2018-02-06 15:29:11","fileId":"5cb6a155f37d454b68caadc4","induId":"垂直领域","indusGroup":"VC组","keyWord":"分时","summary":"分时租赁"},{"authorName":"刘露","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:31:07","docDate":"2017-12-14 00:00:00","docId":"121","docName":"知识付费研究报告","editBy":"1110","editDate":"2018-02-06 15:31:07","fileId":"5cb6a155f37d454b68caadcb","induId":"垂直领域","indusGroup":"VC组","keyWord":"知识付费","summary":"知识付费"}],"total":377}
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
         * data : [{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:12:53","docDate":"2017-08-28 00:00:00","docId":"110","docName":"BAT海外","editBy":"1110","editDate":"2018-02-06 15:12:53","fileId":"5cb6a153f37d454b68caad9e","induId":"垂直领域","indusGroup":"VC组","keyWord":"海外","summary":"海外"},{"authorName":"黄峰，江左","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:14:23","docDate":"2017-08-28 00:00:00","docId":"111","docName":"ICO报告","editBy":"1110","editDate":"2018-02-06 15:14:23","fileId":"5cb6a153f37d454b68caada1","induId":"互联网金融","indusGroup":"VC组","keyWord":"ICO","summary":"ICO"},{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:15:33","docDate":"2017-08-28 00:00:00","docId":"112","docName":"海外Update_Aug.vf","editBy":"1110","editDate":"2018-02-06 15:15:33","fileId":"5cb6a154f37d454b68caada5","induId":"垂直领域","indusGroup":"VC组","keyWord":"海外","summary":"海外"},{"authorName":"王辛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:16:27","docDate":"2017-08-28 00:00:00","docId":"113","docName":"教育行业思路","editBy":"1110","editDate":"2018-02-06 15:16:27","fileId":"5cb6a154f37d454b68caada9","induId":"垂直领域","indusGroup":"VC组","keyWord":"教育","summary":"教育"},{"authorName":"成辉","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:19:09","docDate":"2017-10-14 00:00:00","docId":"114","docName":"2017 Oct. Top VC Update.vf","editBy":"1110","editDate":"2018-02-06 15:19:09","fileId":"5cb6a154f37d454b68caadad","induId":"垂直领域","indusGroup":"VC组","keyWord":"top vc","summary":"top vc"},{"authorName":"张公涛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:20:34","docDate":"2017-10-14 00:00:00","docId":"115","docName":"软银投资策略研究","editBy":"1110","editDate":"2018-02-06 15:20:34","fileId":"5cb6a154f37d454b68caadb2","induId":"垂直领域","indusGroup":"VC组","keyWord":"软银","summary":"软银"},{"authorName":"陈润曦","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:22:12","docDate":"2017-10-14 00:00:00","docId":"116","docName":"美国新零售品牌趋势","editBy":"1110","editDate":"2018-02-06 15:22:12","fileId":"5cb6a155f37d454b68caadbb","induId":"电商/消费","indusGroup":"VC组","keyWord":"美国 新零售","summary":"美国新零售"},{"authorName":"张公涛","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:28:19","docDate":"2017-12-14 00:00:00","docId":"118","docName":"雾计算时代的创新介绍","editBy":"1110","editDate":"2018-02-06 15:28:19","fileId":"5cb6a155f37d454b68caadbe","induId":"垂直领域","indusGroup":"VC组","keyWord":"软银","summary":"软银"},{"authorName":"王辛，程沧海","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:29:11","docDate":"2017-12-14 00:00:00","docId":"119","docName":"汽车分时租赁","editBy":"1110","editDate":"2018-02-06 15:29:11","fileId":"5cb6a155f37d454b68caadc4","induId":"垂直领域","indusGroup":"VC组","keyWord":"分时","summary":"分时租赁"},{"authorName":"刘露","canRead":true,"canWrite":false,"createBy":"1110","createDate":"2018-02-06 15:31:07","docDate":"2017-12-14 00:00:00","docId":"121","docName":"知识付费研究报告","editBy":"1110","editDate":"2018-02-06 15:31:07","fileId":"5cb6a155f37d454b68caadcb","induId":"垂直领域","indusGroup":"VC组","keyWord":"知识付费","summary":"知识付费"}]
         * total : 377
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
             * authorName : 成辉
             * canRead : true
             * canWrite : false
             * createBy : 1110
             * createDate : 2018-02-06 15:12:53
             * docDate : 2017-08-28 00:00:00
             * docId : 110
             * docName : BAT海外
             * editBy : 1110
             * editDate : 2018-02-06 15:12:53
             * fileId : 5cb6a153f37d454b68caad9e
             * induId : 垂直领域
             * indusGroup : VC组
             * keyWord : 海外
             * summary : 海外
             */

            private String authorName;
            private boolean canRead;
            private boolean canWrite;
            private String createBy;
            private String createDate;
            private String docDate;
            private String docId;
            private String docName;
            private String editBy;
            private String editDate;
            private String fileId;
            private String induId;
            private String indusGroup;
            private String keyWord;
            private String summary;

            public String getAuthorName() {
                return authorName;
            }

            public void setAuthorName(String authorName) {
                this.authorName = authorName;
            }

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

            public String getDocDate() {
                return docDate;
            }

            public void setDocDate(String docDate) {
                this.docDate = docDate;
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

            public String getInduId() {
                return induId;
            }

            public void setInduId(String induId) {
                this.induId = induId;
            }

            public String getIndusGroup() {
                return indusGroup;
            }

            public void setIndusGroup(String indusGroup) {
                this.indusGroup = indusGroup;
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
}
