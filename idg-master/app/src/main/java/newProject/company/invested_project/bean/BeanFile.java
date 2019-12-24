package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/30.
 */

public class BeanFile implements Serializable
{


    /**
     * data : {"code":"success","data":[{"busType":"pf01","busTypeStr":"FDD文件","createBy":"1","createByUser":"张屹",
     * "createDate":"2019-05-14 00:00:00","editBy":null,"editDate":null,"file":null,
     * "fileId":"b7bad3fc00b649b1b1d250354e8c0ccb","fileName":"Investment Management System.pdf","fileSize":218343,
     * "fileType":"pdf","objectId":"a211276783804fd395212faf106b66f6","pageNo":null,"pageSize":null,
     * "path":"\\项目文件\\熊希哦啊催大的点点滴滴\\相关附件\\","pdfUrlId":"5cda7b1b972c87615f5ab6c2","projName":null,"projType":null,
     * "segmentPermission":"W","urlId":"5cda7b1b972c87615f5ab6c2","username":null,"validFlag":1}],"total":1}
     * status : 200
     */

    private DataBeanX data;
    private int status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {
        /**
         * code : success
         * data : [{"busType":"pf01","busTypeStr":"FDD文件","createBy":"1","createByUser":"张屹","createDate":"2019-05-14
         * 00:00:00","editBy":null,"editDate":null,"file":null,"fileId":"b7bad3fc00b649b1b1d250354e8c0ccb",
         * "fileName":"Investment Management System.pdf","fileSize":218343,"fileType":"pdf",
         * "objectId":"a211276783804fd395212faf106b66f6","pageNo":null,"pageSize":null,"path":"\\项目文件\\熊希哦啊催大的点点滴滴\\相关附件\\",
         * "pdfUrlId":"5cda7b1b972c87615f5ab6c2","projName":null,"projType":null,"segmentPermission":"W",
         * "urlId":"5cda7b1b972c87615f5ab6c2","username":null,"validFlag":1}]
         * total : 1
         */

        private String code;
        private Integer total;
        private List<DataBean> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public Integer getTotal()
        {
            return total;
        }

        public void setTotal(Integer total)
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
             * busType : pf01
             * busTypeStr : FDD文件
             * createBy : 1
             * createByUser : 张屹
             * createDate : 2019-05-14 00:00:00
             * editBy : null
             * editDate : null
             * file : null
             * fileId : b7bad3fc00b649b1b1d250354e8c0ccb
             * fileName : Investment Management System.pdf
             * fileSize : 218343
             * fileType : pdf
             * objectId : a211276783804fd395212faf106b66f6
             * pageNo : null
             * pageSize : null
             * path : \项目文件\熊希哦啊催大的点点滴滴\相关附件\
             * pdfUrlId : 5cda7b1b972c87615f5ab6c2
             * projName : null
             * projType : null
             * segmentPermission : W
             * urlId : 5cda7b1b972c87615f5ab6c2
             * username : null
             * validFlag : 1
             */

            private String busType;
            private String busTypeStr;
            private String createBy;
            private String createByUser;
            private String createDate;
            private Object editBy;
            private Object editDate;
            private Object file;
            private String fileId;
            private String fileName;
            private Integer fileSize;
            private String fileType;
            private String objectId;
            private Object pageNo;
            private Object pageSize;
            private String path;
            private String pdfUrlId;
            private Object projName;
            private Object projType;
            private String segmentPermission;
            private String urlId;
            private Object username;
            private Integer validFlag;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }


            public String getBusType()
            {
                return busType;
            }

            public void setBusType(String busType)
            {
                this.busType = busType;
            }

            public String getBusTypeStr()
            {
                return busTypeStr;
            }

            public void setBusTypeStr(String busTypeStr)
            {
                this.busTypeStr = busTypeStr;
            }

            public String getCreateBy()
            {
                return createBy;
            }

            public void setCreateBy(String createBy)
            {
                this.createBy = createBy;
            }

            public String getCreateByUser()
            {
                return createByUser;
            }

            public void setCreateByUser(String createByUser)
            {
                this.createByUser = createByUser;
            }

            public String getCreateDate()
            {
                return createDate;
            }

            public void setCreateDate(String createDate)
            {
                this.createDate = createDate;
            }

            public Object getEditBy()
            {
                return editBy;
            }

            public void setEditBy(Object editBy)
            {
                this.editBy = editBy;
            }

            public Object getEditDate()
            {
                return editDate;
            }

            public void setEditDate(Object editDate)
            {
                this.editDate = editDate;
            }

            public Object getFile()
            {
                return file;
            }

            public void setFile(Object file)
            {
                this.file = file;
            }

            public String getFileId()
            {
                return fileId;
            }

            public void setFileId(String fileId)
            {
                this.fileId = fileId;
            }

            public String getFileName()
            {
                return fileName;
            }

            public void setFileName(String fileName)
            {
                this.fileName = fileName;
            }


            public String getFileType()
            {
                return fileType;
            }

            public void setFileType(String fileType)
            {
                this.fileType = fileType;
            }

            public String getObjectId()
            {
                return objectId;
            }

            public void setObjectId(String objectId)
            {
                this.objectId = objectId;
            }

            public Object getPageNo()
            {
                return pageNo;
            }

            public void setPageNo(Object pageNo)
            {
                this.pageNo = pageNo;
            }

            public Object getPageSize()
            {
                return pageSize;
            }

            public void setPageSize(Object pageSize)
            {
                this.pageSize = pageSize;
            }

            public String getPath()
            {
                return path;
            }

            public void setPath(String path)
            {
                this.path = path;
            }

            public String getPdfUrlId()
            {
                return pdfUrlId;
            }

            public void setPdfUrlId(String pdfUrlId)
            {
                this.pdfUrlId = pdfUrlId;
            }

            public Object getProjName()
            {
                return projName;
            }

            public void setProjName(Object projName)
            {
                this.projName = projName;
            }

            public Object getProjType()
            {
                return projType;
            }

            public void setProjType(Object projType)
            {
                this.projType = projType;
            }

            public String getSegmentPermission()
            {
                return segmentPermission;
            }

            public void setSegmentPermission(String segmentPermission)
            {
                this.segmentPermission = segmentPermission;
            }

            public String getUrlId()
            {
                return urlId;
            }

            public void setUrlId(String urlId)
            {
                this.urlId = urlId;
            }

            public Object getUsername()
            {
                return username;
            }

            public void setUsername(Object username)
            {
                this.username = username;
            }

            public Integer getFileSize()
            {
                return fileSize;
            }

            public void setFileSize(Integer fileSize)
            {
                this.fileSize = fileSize;
            }

            public Integer getValidFlag()
            {
                return validFlag;
            }

            public void setValidFlag(Integer validFlag)
            {
                this.validFlag = validFlag;
            }
        }
    }
}
