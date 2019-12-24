package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/29.
 */
public class BeanMeeting implements Serializable
{
    private DataBeanX data;
    private Integer status;

    public DataBeanX getData()
    {
        return data;
    }

    public void setData(DataBeanX data)
    {
        this.data = data;
    }

    public Integer getStatus()
    {
        return status;
    }

    public void setStatus(Integer status)
    {
        this.status = status;
    }

    public static class DataBeanX
    {

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
             * id : aad7b4fd640d4ee8875b76ad00d609da
             * projId : 827e38fbb1c44c8994b14d091804420e
             * opinionDate : 2019-04-01
             * opinionType : 视频会议
             * content : 鸡毛不飞天了
             * conclusion : Pass
             * approvedBy :
             * groupId : 1
             * validFlag : 1
             * createBy : 1388
             * createDate : 2019-04-12 14:28:26
             * editBy : 1
             * editDate : 2019-04-12 14:32:45
             * opinionTypeStr :
             * approvedByStr :
             * approveOpinion
             * groupIdStr : VC组
             * createByStr : 罗智慧
             */

            private String id;
            private String projId;
            private String opinionDate;
            private String opinionType;
            private String content;
            private String conclusion;
            private String approvedBy;
            private String groupId;
            private Integer validFlag;
            private String createBy;
            private String createDate;
            private String editBy;
            private String editDate;
            private String opinionTypeStr;
            private String approvedByStr;
            private String groupIdStr;
            private String createByStr;
            private String approveOpinion;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
            }

            public String getApproveOpinion()
            {
                return approveOpinion;
            }

            public void setApproveOpinion(String approveOpinion)
            {
                this.approveOpinion = approveOpinion;
            }

            public String getId()
            {
                return id;
            }

            public void setId(String id)
            {
                this.id = id;
            }

            public String getProjId()
            {
                return projId;
            }

            public void setProjId(String projId)
            {
                this.projId = projId;
            }

            public String getOpinionDate()
            {
                return opinionDate;
            }

            public void setOpinionDate(String opinionDate)
            {
                this.opinionDate = opinionDate;
            }

            public String getOpinionType()
            {
                return opinionType;
            }

            public void setOpinionType(String opinionType)
            {
                this.opinionType = opinionType;
            }

            public String getContent()
            {
                return content;
            }

            public void setContent(String content)
            {
                this.content = content;
            }

            public String getConclusion()
            {
                return conclusion;
            }

            public void setConclusion(String conclusion)
            {
                this.conclusion = conclusion;
            }

            public String getApprovedBy()
            {
                return approvedBy;
            }

            public void setApprovedBy(String approvedBy)
            {
                this.approvedBy = approvedBy;
            }

            public String getGroupId()
            {
                return groupId;
            }

            public void setGroupId(String groupId)
            {
                this.groupId = groupId;
            }

            public Integer getValidFlag()
            {
                return validFlag;
            }

            public void setValidFlag(Integer validFlag)
            {
                this.validFlag = validFlag;
            }

            public String getCreateBy()
            {
                return createBy;
            }

            public void setCreateBy(String createBy)
            {
                this.createBy = createBy;
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

            public String getOpinionTypeStr()
            {
                return opinionTypeStr;
            }

            public void setOpinionTypeStr(String opinionTypeStr)
            {
                this.opinionTypeStr = opinionTypeStr;
            }

            public String getApprovedByStr()
            {
                return approvedByStr;
            }

            public void setApprovedByStr(String approvedByStr)
            {
                this.approvedByStr = approvedByStr;
            }

            public String getGroupIdStr()
            {
                return groupIdStr;
            }

            public void setGroupIdStr(String groupIdStr)
            {
                this.groupIdStr = groupIdStr;
            }

            public String getCreateByStr()
            {
                return createByStr;
            }

            public void setCreateByStr(String createByStr)
            {
                this.createByStr = createByStr;
            }
        }
    }
}
