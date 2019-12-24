package newProject.company.project_manager.investment_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2018/5/11.
 */

public class FileAnnexBean implements Serializable
{
    /**
     * data : [{"attaName":"4.4 合格投资者承诺书-张玲.pdf","attaPath":"/1/3/18506/18641/18691/18699/18701/","boxId":19024,"content":null,
     * "fileType":"pdf","isDir":"N","parentId":18701,"uploadTime":"2017-09-07","uploadUser":3,"uploadUserName":"胡哲"},
     * {"attaName":"2.4 风险揭示书-张玲.pdf","attaPath":"/1/3/18506/18641/18691/18699/18700/","boxId":19021,"content":null,
     * "fileType":"pdf","isDir":"N","parentId":18700,"uploadTime":"2017-09-07","uploadUser":3,"uploadUserName":"胡哲"},
     * {"attaName":"3.4 风险调查问卷-张玲.pdf","attaPath":"/1/3/18506/18641/18691/18694/18698/","boxId":19018,"content":null,
     * "fileType":"pdf","isDir":"N","parentId":18698,"uploadTime":"2017-09-07","uploadUser":3,"uploadUserName":"胡哲"},
     * {"attaName":"5.4 身份证明及资产证明-张玲.pdf","attaPath":"/1/3/18506/18641/18691/18694/18697/","boxId":19015,"content":null,
     * "fileType":"pdf","isDir":"N","parentId":18697,"uploadTime":"2017-09-07","uploadUser":3,"uploadUserName":"胡哲"},
     * {"attaName":"5.4 身份证明及资产证明-张玲.pdf","attaPath":"/1/3/18506/18641/18691/18694/18695/","boxId":19013,"content":null,
     * "fileType":"pdf","isDir":"N","parentId":18695,"uploadTime":"2017-09-07","uploadUser":3,"uploadUserName":"胡哲"},
     * {"attaName":"分贝金服2017年2季度合并报表_170731_Final.xlsx","attaPath":"/1/2/6/1869/3205/18265/18266/18269/","boxId":18272,
     * "content":null,"fileType":"xlsx","isDir":"N","parentId":18269,"uploadTime":"2017-09-07","uploadUser":12,
     * "uploadUserName":"王雪卿"},{"attaName":"分贝金服2016年度合并报表_170731_Final.xlsx","attaPath":"/1/2/6/1869/3205/18265/18266/18267/",
     * "boxId":18268,"content":null,"fileType":"xlsx","isDir":"N","parentId":18267,"uploadTime":"2017-09-06","uploadUser":12,
     * "uploadUserName":"王雪卿"},{"attaName":"分贝金服2016年度合并报表_170731_Final.xlsx","attaPath":"/1/2/6/1869/3205/3206/3207/3211/",
     * "boxId":18264,"content":null,"fileType":"xlsx","isDir":"N","parentId":3211,"uploadTime":"2017-09-06","uploadUser":12,
     * "uploadUserName":"王雪卿"},{"attaName":"2016年9月合并报表-toIDG.xlsx","attaPath":"/1/2/6/1869/3205/3206/3207/3210/","boxId":3214,
     * "content":null,"fileType":"xlsx","isDir":"N","parentId":3210,"uploadTime":"2017-04-20","uploadUser":310,
     * "uploadUserName":"余天天"},{"attaName":"2016-6合并报表-TO IDG.xlsx","attaPath":"/1/2/6/1869/3205/3206/3207/3209/","boxId":3213,
     * "content":null,"fileType":"xlsx","isDir":"N","parentId":3209,"uploadTime":"2017-04-20","uploadUser":310,
     * "uploadUserName":"余天天"}]
     * page : 1
     * pageCount : 4
     * status : 200
     * total : 34
     */

    private int page;
    private int pageCount;
    private int status;
    private int total;
    private List<DataBean> data;

    public int getPage()
    {
        return page;
    }

    public void setPage(int page)
    {
        this.page = page;
    }

    public int getPageCount()
    {
        return pageCount;
    }

    public void setPageCount(int pageCount)
    {
        this.pageCount = pageCount;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getTotal()
    {
        return total;
    }

    public void setTotal(int total)
    {
        this.total = total;
    }

    public List<DataBean> getData()
    {
        return data;
    }

    public void setData(List<DataBean> data)
    {
        this.data = data;
    }

    public static class DataBean
    {
        /**
         * attaName : 4.4 合格投资者承诺书-张玲.pdf
         * attaPath : /1/3/18506/18641/18691/18699/18701/
         * boxId : 19024
         * content : null
         * fileType : pdf
         * isDir : N
         * parentId : 18701
         * uploadTime : 2017-09-07
         * uploadUser : 3
         * uploadUserName : 胡哲
         */

        private String attaName;
        private String attaPath;
        private int boxId;
        private String content;
        private String fileType;
        private String isDir;
        private int parentId;
        private String uploadTime;
        private int uploadUser;
        private String uploadUserName;

        public String getAttaName()
        {
            return attaName;
        }

        public void setAttaName(String attaName)
        {
            this.attaName = attaName;
        }

        public String getAttaPath()
        {
            return attaPath;
        }

        public void setAttaPath(String attaPath)
        {
            this.attaPath = attaPath;
        }

        public int getBoxId()
        {
            return boxId;
        }

        public void setBoxId(int boxId)
        {
            this.boxId = boxId;
        }

        public String getContent()
        {
            return content;
        }

        public void setContent(String content)
        {
            this.content = content;
        }

        public String getFileType()
        {
            return fileType;
        }

        public void setFileType(String fileType)
        {
            this.fileType = fileType;
        }

        public String getIsDir()
        {
            return isDir;
        }

        public void setIsDir(String isDir)
        {
            this.isDir = isDir;
        }

        public int getParentId()
        {
            return parentId;
        }

        public void setParentId(int parentId)
        {
            this.parentId = parentId;
        }

        public String getUploadTime()
        {
            return uploadTime;
        }

        public void setUploadTime(String uploadTime)
        {
            this.uploadTime = uploadTime;
        }

        public int getUploadUser()
        {
            return uploadUser;
        }

        public void setUploadUser(int uploadUser)
        {
            this.uploadUser = uploadUser;
        }

        public String getUploadUserName()
        {
            return uploadUserName;
        }

        public void setUploadUserName(String uploadUserName)
        {
            this.uploadUserName = uploadUserName;
        }
    }
}
