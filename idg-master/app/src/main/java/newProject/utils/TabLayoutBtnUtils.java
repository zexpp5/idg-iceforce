package newProject.utils;

import com.chaoxiang.base.utils.StringUtils;

import newProject.bean.InvestmentBean;
import newProject.bean.TabLayoutBtnMessage;

/**
 * Created by selson on 2019/5/10.
 */

public class TabLayoutBtnUtils
{
    private TabLayoutBtnUtils()
    {

    }

    public static class TabLayoutBtnUtilsHelper
    {
        public static TabLayoutBtnUtils tabLayoutBtnUtils = new TabLayoutBtnUtils();
    }

    public static synchronized TabLayoutBtnUtils getInstance()
    {
        return TabLayoutBtnUtilsHelper.tabLayoutBtnUtils;
    }

    public TabLayoutBtnMessage getTabBean(String fgCode, boolean isKey, String value)
    {
        if (StringUtils.notEmpty(fgCode))
        {
            InvestmentBean investmentBean = new InvestmentBean();
            investmentBean.setKey(isKey);
            investmentBean.setValue(value);
            TabLayoutBtnMessage tabLayoutBtnMessage = new TabLayoutBtnMessage();
            tabLayoutBtnMessage.fgCode = fgCode;
            tabLayoutBtnMessage.setInvestmentBean(investmentBean);
            return tabLayoutBtnMessage;
        } else
        {
            return null;
        }
    }
}
