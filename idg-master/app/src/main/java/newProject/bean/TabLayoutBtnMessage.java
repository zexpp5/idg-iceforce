package newProject.bean;

/**
 * Created by cx on 2016/9/22.
 */
public class TabLayoutBtnMessage
{
    public String fgCode;  //第几个tab的type
    public InvestmentBean investmentBean;

    public TabLayoutBtnMessage()
    {

    }

    public TabLayoutBtnMessage(InvestmentBean investmentBean, String fgCode)
    {
        this.investmentBean = investmentBean;
        this.fgCode = fgCode;
    }

    public String getFgCode()
    {
        return fgCode;
    }

    public void setFgCode(String fgCode)
    {
        this.fgCode = fgCode;
    }

    public InvestmentBean getInvestmentBean()
    {
        return investmentBean;
    }

    public void setInvestmentBean(InvestmentBean investmentBean)
    {
        this.investmentBean = investmentBean;
    }
}
