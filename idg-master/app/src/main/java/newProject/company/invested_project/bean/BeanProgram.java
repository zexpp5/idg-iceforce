package newProject.company.invested_project.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by selson on 2019/4/29.
 */

public class BeanProgram implements Serializable
{

    /**
     * data : {"code":"success","data":[{"createBy":"9999","createDate":"2019-04-29 15:06:00","currId":"CNY","currIdStr":"人民币",
     * "editBy":"","editDate":"","fund":"和谐一","isIcApproved":"1","isIcApprovedStr":"否","pipelineAmt":1000,"pipelineAmtStr":"1,
     * 000万元","planDate":"2019-04-28","planDesc":"计划投资1000w","planId":"01a2e0c404924d6fbd9952c6cfe4befc","planType":"2",
     * "planTypeStr":"IC","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","remarks":"备注","segmentPermission":"W","username":""}],
     * "total":1}
     * status : 200
     */

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
        /**
         * code : success
         * data : [{"createBy":"9999","createDate":"2019-04-29 15:06:00","currId":"CNY","currIdStr":"人民币","editBy":"",
         * "editDate":"","fund":"和谐一","isIcApproved":"1","isIcApprovedStr":"否","pipelineAmt":1000,"pipelineAmtStr":"1,000万元",
         * "planDate":"2019-04-28","planDesc":"计划投资1000w","planId":"01a2e0c404924d6fbd9952c6cfe4befc","planType":"2",
         * "planTypeStr":"IC","projId":"8ec3af3dc4c94e3497dc018e78df0fe2","remarks":"备注","segmentPermission":"W","username":""}]
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
             * createBy : 9999
             * createDate : 2019-04-29 15:06:00
             * currId : CNY
             * currIdStr : 人民币
             * editBy :
             * editDate :
             * fund : 和谐一
             * isIcApproved : 1
             * isIcApprovedStr : 否
             * pipelineAmt : 1000.0
             * pipelineAmtStr : 1,000万元
             * planDate : 2019-04-28
             * planDesc : 计划投资1000w
             * planId : 01a2e0c404924d6fbd9952c6cfe4befc
             * planType : 2
             * planTypeStr : IC
             * projId : 8ec3af3dc4c94e3497dc018e78df0fe2
             * remarks : 备注
             * segmentPermission : W
             * username :
             */

            private String createBy;
            private String createDate;
            private String currId;
            private String currIdStr;
            private String editBy;
            private String editDate;
            private String fund;
            private String isIcApproved;
            private String isIcApprovedStr;
            private Number pipelineAmt;
            private String pipelineAmtStr;
            private String planDate;
            private String planDesc;
            private String planId;
            private String planType;
            private String planTypeStr;
            private String projId;
            private String remarks;
            private String segmentPermission;
            private String username;

            private Integer permission;

            public Integer getPermission()
            {
                return permission;
            }

            public void setPermission(Integer permission)
            {
                this.permission = permission;
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

            public String getCurrId()
            {
                return currId;
            }

            public void setCurrId(String currId)
            {
                this.currId = currId;
            }

            public String getCurrIdStr()
            {
                return currIdStr;
            }

            public void setCurrIdStr(String currIdStr)
            {
                this.currIdStr = currIdStr;
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

            public String getFund()
            {
                return fund;
            }

            public void setFund(String fund)
            {
                this.fund = fund;
            }

            public String getIsIcApproved()
            {
                return isIcApproved;
            }

            public void setIsIcApproved(String isIcApproved)
            {
                this.isIcApproved = isIcApproved;
            }

            public String getIsIcApprovedStr()
            {
                return isIcApprovedStr;
            }

            public void setIsIcApprovedStr(String isIcApprovedStr)
            {
                this.isIcApprovedStr = isIcApprovedStr;
            }

            public Number getPipelineAmt()
            {
                return pipelineAmt;
            }

            public void setPipelineAmt(Number pipelineAmt)
            {
                this.pipelineAmt = pipelineAmt;
            }

            public String getPipelineAmtStr()
            {
                return pipelineAmtStr;
            }

            public void setPipelineAmtStr(String pipelineAmtStr)
            {
                this.pipelineAmtStr = pipelineAmtStr;
            }

            public String getPlanDate()
            {
                return planDate;
            }

            public void setPlanDate(String planDate)
            {
                this.planDate = planDate;
            }

            public String getPlanDesc()
            {
                return planDesc;
            }

            public void setPlanDesc(String planDesc)
            {
                this.planDesc = planDesc;
            }

            public String getPlanId()
            {
                return planId;
            }

            public void setPlanId(String planId)
            {
                this.planId = planId;
            }

            public String getPlanType()
            {
                return planType;
            }

            public void setPlanType(String planType)
            {
                this.planType = planType;
            }

            public String getPlanTypeStr()
            {
                return planTypeStr;
            }

            public void setPlanTypeStr(String planTypeStr)
            {
                this.planTypeStr = planTypeStr;
            }

            public String getProjId()
            {
                return projId;
            }

            public void setProjId(String projId)
            {
                this.projId = projId;
            }

            public String getRemarks()
            {
                return remarks;
            }

            public void setRemarks(String remarks)
            {
                this.remarks = remarks;
            }

            public String getSegmentPermission()
            {
                return segmentPermission;
            }

            public void setSegmentPermission(String segmentPermission)
            {
                this.segmentPermission = segmentPermission;
            }

            public String getUsername()
            {
                return username;
            }

            public void setUsername(String username)
            {
                this.username = username;
            }
        }
    }
}
