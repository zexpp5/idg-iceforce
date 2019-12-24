package newProject.company.project_manager.investment_project.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10.
 */

public class AnnexListBean {


    /**
     * data : [{"attaName":"2018年年假记录.xls","attaPath":"/1/2/5/45463/47275/","boxId":47277,"content":null,"fileType":"xls","isDir":"N","parentId":47275,"uploadTime":1525816939000,"uploadUser":1,"uploadUserName":"张屹"},{"attaName":"IDG China Venture Capital Fund IV LP December 31 2017(1).pdf","attaPath":"/1/2/5/45463/47275/","boxId":47276,"content":null,"fileType":"pdf","isDir":"N","parentId":47275,"uploadTime":1525816927000,"uploadUser":1,"uploadUserName":"张屹"}]
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
         * attaName : 2018年年假记录.xls
         * attaPath : /1/2/5/45463/47275/
         * boxId : 47277
         * content : null
         * fileType : xls
         * isDir : N
         * parentId : 47275
         * uploadTime : 1525816939000
         * uploadUser : 1
         * uploadUserName : 张屹
         */

        private String attaName;
        private String attaPath;
        private int boxId;
        private Object content;
        private String fileType;
        private String isDir;
        private int parentId;
        private long uploadTime;
        private int uploadUser;
        private String uploadUserName;

        public String getAttaName() {
            return attaName;
        }

        public void setAttaName(String attaName) {
            this.attaName = attaName;
        }

        public String getAttaPath() {
            return attaPath;
        }

        public void setAttaPath(String attaPath) {
            this.attaPath = attaPath;
        }

        public int getBoxId() {
            return boxId;
        }

        public void setBoxId(int boxId) {
            this.boxId = boxId;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public String getFileType() {
            return fileType;
        }

        public void setFileType(String fileType) {
            this.fileType = fileType;
        }

        public String getIsDir() {
            return isDir;
        }

        public void setIsDir(String isDir) {
            this.isDir = isDir;
        }

        public int getParentId() {
            return parentId;
        }

        public void setParentId(int parentId) {
            this.parentId = parentId;
        }

        public long getUploadTime() {
            return uploadTime;
        }

        public void setUploadTime(long uploadTime) {
            this.uploadTime = uploadTime;
        }

        public int getUploadUser() {
            return uploadUser;
        }

        public void setUploadUser(int uploadUser) {
            this.uploadUser = uploadUser;
        }

        public String getUploadUserName() {
            return uploadUserName;
        }

        public void setUploadUserName(String uploadUserName) {
            this.uploadUserName = uploadUserName;
        }
    }
}
