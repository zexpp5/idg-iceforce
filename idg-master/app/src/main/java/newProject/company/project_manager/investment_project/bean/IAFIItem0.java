package newProject.company.project_manager.investment_project.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import newProject.company.project_manager.investment_project.adapter.InvestmentAndFinancingInformationAdapter;

/**
 * Created by zsz on 2019/5/20.
 */

public class IAFIItem0 extends AbstractExpandableItem implements MultiItemEntity
{
    /**
     * agency : 度小满
     * agencyStr : 度小满
     * beginDate : null
     * city : 哈尔滨
     * desc : 消费金融服务提供商
     * endDate : null
     * financingAmt : 未披露
     * financingDate : 2019-05-16
     * group : null
     * industry : 互联网金融
     * isAll : null
     * key : null
     * keyAgency : null
     * pageNo : 0
     * pageSize : 10
     * projFinancingId : 5079
     * projName : 哈银消费金融
     * redOrNot : 0
     * round : 战略投资
     * status : 0
     * userId : null
     * username : null
     */

    private String agency;
    private String agencyStr;
    private Object beginDate;
    private String city;
    private String desc;
    private Object endDate;
    private String financingAmt;
    private String financingDate;
    private Object group;
    private String industry;
    private Object isAll;
    private Object key;
    private Object keyAgency;
    private int pageNo;
    private int pageSize;
    private int projFinancingId;
    private String projName;
    private String redOrNot;
    private String round;
    private int status;
    private Object userId;
    private Object username;
    private String legalName;

    public String getLegalName()
    {
        return legalName;
    }

    public void setLegalName(String legalName)
    {
        this.legalName = legalName;
    }

    public String getAgency()
    {
        return agency;
    }

    public void setAgency(String agency)
    {
        this.agency = agency;
    }

    public String getAgencyStr()
    {
        return agencyStr;
    }

    public void setAgencyStr(String agencyStr)
    {
        this.agencyStr = agencyStr;
    }

    public Object getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(Object beginDate)
    {
        this.beginDate = beginDate;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }

    public Object getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Object endDate)
    {
        this.endDate = endDate;
    }

    public String getFinancingAmt()
    {
        return financingAmt;
    }

    public void setFinancingAmt(String financingAmt)
    {
        this.financingAmt = financingAmt;
    }

    public String getFinancingDate()
    {
        return financingDate;
    }

    public void setFinancingDate(String financingDate)
    {
        this.financingDate = financingDate;
    }

    public Object getGroup()
    {
        return group;
    }

    public void setGroup(Object group)
    {
        this.group = group;
    }

    public String getIndustry()
    {
        return industry;
    }

    public void setIndustry(String industry)
    {
        this.industry = industry;
    }

    public Object getIsAll()
    {
        return isAll;
    }

    public void setIsAll(Object isAll)
    {
        this.isAll = isAll;
    }

    public Object getKey()
    {
        return key;
    }

    public void setKey(Object key)
    {
        this.key = key;
    }

    public Object getKeyAgency()
    {
        return keyAgency;
    }

    public void setKeyAgency(Object keyAgency)
    {
        this.keyAgency = keyAgency;
    }

    public int getPageNo()
    {
        return pageNo;
    }

    public void setPageNo(int pageNo)
    {
        this.pageNo = pageNo;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public int getProjFinancingId()
    {
        return projFinancingId;
    }

    public void setProjFinancingId(int projFinancingId)
    {
        this.projFinancingId = projFinancingId;
    }

    public String getProjName()
    {
        return projName;
    }

    public void setProjName(String projName)
    {
        this.projName = projName;
    }

    public String getRedOrNot()
    {
        return redOrNot;
    }

    public void setRedOrNot(String redOrNot)
    {
        this.redOrNot = redOrNot;
    }

    public String getRound()
    {
        return round;
    }

    public void setRound(String round)
    {
        this.round = round;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public Object getUserId()
    {
        return userId;
    }

    public void setUserId(Object userId)
    {
        this.userId = userId;
    }

    public Object getUsername()
    {
        return username;
    }

    public void setUsername(Object username)
    {
        this.username = username;
    }

    @Override
    public int getLevel()
    {
        return 0;
    }

    @Override
    public int getItemType()
    {
        return InvestmentAndFinancingInformationAdapter.TYPE_LEVEL_0;
    }
}
