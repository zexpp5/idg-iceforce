package newProject.company.invested_project.bean;

import java.io.Serializable;

/**
 * Created by selson on 2019/5/13.
 * 投资方案-post-实体类
 */
public class PostProgramBean implements Serializable
{
    private String projId;
    private String planDate;
    private String planDesc;
    private String pipelineAmt;
    private String isIcApproved;
    private String currId;
    private String remarks;
    private String fund;
    private String username;
    private String planType;

    public String getProjId()
    {
        return projId;
    }

    public void setProjId(String projId)
    {
        this.projId = projId;
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

    public String getPipelineAmt()
    {
        return pipelineAmt;
    }

    public void setPipelineAmt(String pipelineAmt)
    {
        this.pipelineAmt = pipelineAmt;
    }

    public String getIsIcApproved()
    {
        return isIcApproved;
    }

    public void setIsIcApproved(String isIcApproved)
    {
        this.isIcApproved = isIcApproved;
    }

    public String getCurrId()
    {
        return currId;
    }

    public void setCurrId(String currId)
    {
        this.currId = currId;
    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public String getFund()
    {
        return fund;
    }

    public void setFund(String fund)
    {
        this.fund = fund;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPlanType()
    {
        return planType;
    }

    public void setPlanType(String planType)
    {
        this.planType = planType;
    }
}
