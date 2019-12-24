package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/7/3.
 */
public class BeanDirector implements Serializable
{
    /**
     * data : {"code":"success","data":[{"content":"233","decisionType":"type1","decisionTypeStr":"对外重大投资决策",
     * "fileInfoList":[{"busType":"MEEING_FILE","busTypeStr":"","createBy":"1","createByUser":"张屹","createDate":"2019-06-17
     * 00:00:00","editBy":"","editDate":"","endDate":"","file":"","fileId":"149a9b4a9c9247d983a2bfd8e5396fd3",
     * "fileName":"2019-05-06-5-10熊晓翠工作周报 .pdf","fileSize":77243,"fileType":"pdf",
     * "objectId":"6438d197dcc24fe09c498eeb2715af98","pageNo":"","pageSize":"","path":"","pdfUrlId":"","permission":1,
     * "projName":"","projType":"","queryString":"","remarks":"","startDate":"","urlId":"5d074100972c877c54fde468",
     * "username":"","validFlag":1}],"importantFlag":1,"importantFlagStr":"是","meetingDateStr":"2019-06-12",
     * "meetingDesc":"2333","meetingId":"6438d197dcc24fe09c498eeb2715af98"}],"returnMessage":"SUCCESS","total":1}
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

    public static class DataBeanX implements Serializable
    {
        /**
         * code : success
         * data : [{"content":"233","decisionType":"type1","decisionTypeStr":"对外重大投资决策",
         * "fileInfoList":[{"busType":"MEEING_FILE","busTypeStr":"","createBy":"1","createByUser":"张屹","createDate":"2019-06-17
         * 00:00:00","editBy":"","editDate":"","endDate":"","file":"","fileId":"149a9b4a9c9247d983a2bfd8e5396fd3",
         * "fileName":"2019-05-06-5-10熊晓翠工作周报 .pdf","fileSize":77243,"fileType":"pdf",
         * "objectId":"6438d197dcc24fe09c498eeb2715af98","pageNo":"","pageSize":"","path":"","pdfUrlId":"","permission":1,
         * "projName":"","projType":"","queryString":"","remarks":"","startDate":"","urlId":"5d074100972c877c54fde468",
         * "username":"","validFlag":1}],"importantFlag":1,"importantFlagStr":"是","meetingDateStr":"2019-06-12",
         * "meetingDesc":"2333","meetingId":"6438d197dcc24fe09c498eeb2715af98"}]
         * returnMessage : SUCCESS
         * total : 1
         */

        private String code;
        private String returnMessage;
        private int total;
        private List<DataBean> data;

        public String getCode()
        {
            return code;
        }

        public void setCode(String code)
        {
            this.code = code;
        }

        public String getReturnMessage()
        {
            return returnMessage;
        }

        public void setReturnMessage(String returnMessage)
        {
            this.returnMessage = returnMessage;
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

        public static class DataBean implements Serializable
        {
            /**
             * content : 233
             * decisionType : type1
             * decisionTypeStr : 对外重大投资决策
             * fileInfoList : [{"busType":"MEEING_FILE","busTypeStr":"","createBy":"1","createByUser":"张屹",
             * "createDate":"2019-06-17 00:00:00","editBy":"","editDate":"","endDate":"","file":"",
             * "fileId":"149a9b4a9c9247d983a2bfd8e5396fd3","fileName":"2019-05-06-5-10熊晓翠工作周报 .pdf","fileSize":77243,
             * "fileType":"pdf","objectId":"6438d197dcc24fe09c498eeb2715af98","pageNo":"","pageSize":"","path":"",
             * "pdfUrlId":"","permission":1,"projName":"","projType":"","queryString":"","remarks":"","startDate":"",
             * "urlId":"5d074100972c877c54fde468","username":"","validFlag":1}]
             * importantFlag : 1
             * importantFlagStr : 是
             * meetingDateStr : 2019-06-12
             * meetingDesc : 2333
             * meetingId : 6438d197dcc24fe09c498eeb2715af98
             */

            private String content;
            private String decisionType;
            private String decisionTypeStr;
            private int importantFlag;
            private String importantFlagStr;
            private String meetingDateStr;
            private String meetingDesc;
            private String meetingId;
            private String projId;
            private String projName;
            private List<FileInfoListBean> fileInfoList;

            public String getProjId()
            {
                return projId;
            }

            public void setProjId(String projId)
            {
                this.projId = projId;
            }

            public String getProjName()
            {
                return projName;
            }

            public void setProjName(String projName)
            {
                this.projName = projName;
            }

            public String getContent()
            {
                return content;
            }

            public void setContent(String content)
            {
                this.content = content;
            }

            public String getDecisionType()
            {
                return decisionType;
            }

            public void setDecisionType(String decisionType)
            {
                this.decisionType = decisionType;
            }

            public String getDecisionTypeStr()
            {
                return decisionTypeStr;
            }

            public void setDecisionTypeStr(String decisionTypeStr)
            {
                this.decisionTypeStr = decisionTypeStr;
            }

            public int getImportantFlag()
            {
                return importantFlag;
            }

            public void setImportantFlag(int importantFlag)
            {
                this.importantFlag = importantFlag;
            }

            public String getImportantFlagStr()
            {
                return importantFlagStr;
            }

            public void setImportantFlagStr(String importantFlagStr)
            {
                this.importantFlagStr = importantFlagStr;
            }

            public String getMeetingDateStr()
            {
                return meetingDateStr;
            }

            public void setMeetingDateStr(String meetingDateStr)
            {
                this.meetingDateStr = meetingDateStr;
            }

            public String getMeetingDesc()
            {
                return meetingDesc;
            }

            public void setMeetingDesc(String meetingDesc)
            {
                this.meetingDesc = meetingDesc;
            }

            public String getMeetingId()
            {
                return meetingId;
            }

            public void setMeetingId(String meetingId)
            {
                this.meetingId = meetingId;
            }

            public List<FileInfoListBean> getFileInfoList()
            {
                return fileInfoList;
            }

            public void setFileInfoList(List<FileInfoListBean> fileInfoList)
            {
                this.fileInfoList = fileInfoList;
            }

            public static class FileInfoListBean
            {
                /**
                 * busType : MEEING_FILE
                 * busTypeStr :
                 * createBy : 1
                 * createByUser : 张屹
                 * createDate : 2019-06-17 00:00:00
                 * editBy :
                 * editDate :
                 * endDate :
                 * file :
                 * fileId : 149a9b4a9c9247d983a2bfd8e5396fd3
                 * fileName : 2019-05-06-5-10熊晓翠工作周报 .pdf
                 * fileSize : 77243
                 * fileType : pdf
                 * objectId : 6438d197dcc24fe09c498eeb2715af98
                 * pageNo :
                 * pageSize :
                 * path :
                 * pdfUrlId :
                 * permission : 1
                 * projName :
                 * projType :
                 * queryString :
                 * remarks :
                 * startDate :
                 * urlId : 5d074100972c877c54fde468
                 * username :
                 * validFlag : 1
                 */

                private String busType;
                private String busTypeStr;
                private String createBy;
                private String createByUser;
                private String createDate;
                private String editBy;
                private String editDate;
                private String endDate;
                private String file;
                private String fileId;
                private String fileName;
                private int fileSize;
                private String fileType;
                private String objectId;
                private String pageNo;
                private String pageSize;
                private String path;
                private String pdfUrlId;
                private int permission;
                private String projName;
                private String projId;
                private String projType;
                private String queryString;
                private String remarks;
                private String startDate;
                private String urlId;
                private String username;
                private int validFlag;

                public String getProjId()
                {
                    return projId;
                }

                public void setProjId(String projId)
                {
                    this.projId = projId;
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

                public String getEditBy()
                {
                    return editBy;
                }

                public void setEditBy(String editBy)
                {
                    this.editBy = editBy;
                }

                public String getEditDate()
                {
                    return editDate;
                }

                public void setEditDate(String editDate)
                {
                    this.editDate = editDate;
                }

                public String getEndDate()
                {
                    return endDate;
                }

                public void setEndDate(String endDate)
                {
                    this.endDate = endDate;
                }

                public String getFile()
                {
                    return file;
                }

                public void setFile(String file)
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

                public int getFileSize()
                {
                    return fileSize;
                }

                public void setFileSize(int fileSize)
                {
                    this.fileSize = fileSize;
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

                public String getPageNo()
                {
                    return pageNo;
                }

                public void setPageNo(String pageNo)
                {
                    this.pageNo = pageNo;
                }

                public String getPageSize()
                {
                    return pageSize;
                }

                public void setPageSize(String pageSize)
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

                public int getPermission()
                {
                    return permission;
                }

                public void setPermission(int permission)
                {
                    this.permission = permission;
                }

                public String getProjName()
                {
                    return projName;
                }

                public void setProjName(String projName)
                {
                    this.projName = projName;
                }

                public String getProjType()
                {
                    return projType;
                }

                public void setProjType(String projType)
                {
                    this.projType = projType;
                }

                public String getQueryString()
                {
                    return queryString;
                }

                public void setQueryString(String queryString)
                {
                    this.queryString = queryString;
                }

                public String getRemarks()
                {
                    return remarks;
                }

                public void setRemarks(String remarks)
                {
                    this.remarks = remarks;
                }

                public String getStartDate()
                {
                    return startDate;
                }

                public void setStartDate(String startDate)
                {
                    this.startDate = startDate;
                }

                public String getUrlId()
                {
                    return urlId;
                }

                public void setUrlId(String urlId)
                {
                    this.urlId = urlId;
                }

                public String getUsername()
                {
                    return username;
                }

                public void setUsername(String username)
                {
                    this.username = username;
                }

                public int getValidFlag()
                {
                    return validFlag;
                }

                public void setValidFlag(int validFlag)
                {
                    this.validFlag = validFlag;
                }
            }
        }
    }
}
