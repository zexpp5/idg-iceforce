package com.ui.http.bean;

import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.imsdk.CXSDKHelper;
import com.cxgz.activity.cxim.bean.OwnerBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * Created by selson on 2017/7/31.
 */

public class SuDaLoginBean implements Serializable
{
    private String errMsg;

    private int errNo;

    private String url;

    private String strValidDate;

    private String strValidDays;

    private String AgentName;

    private String CompanyName;

    private String EMail;

    private String AgentTel;

    public static SuDaLoginBean parse(String jsonString)
    {
        SuDaLoginBean ownerBean = new SuDaLoginBean();
        Gson gson = new Gson();
        try
        {
            ownerBean = gson.fromJson(jsonString, new TypeToken<SuDaLoginBean>()
            {
            }.getType());
        } catch (Exception e)
        {

        }
        return ownerBean;
    }

    public String getErrMsg()
    {
        return errMsg;
    }

    public void setErrMsg(String errMsg)
    {
        this.errMsg = errMsg;
    }

    public long getErrNo()
    {
        return errNo;
    }

    public void setErrNo(int errNo)
    {
        this.errNo = errNo;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getStrValidDate()
    {
        return strValidDate;
    }

    public void setStrValidDate(String strValidDate)
    {
        this.strValidDate = strValidDate;
    }

    public String getStrValidDays()
    {
        return strValidDays;
    }

    public void setStrValidDays(String strValidDays)
    {
        this.strValidDays = strValidDays;
    }

    public String getAgentName()
    {
        return AgentName;
    }

    public void setAgentName(String agentName)
    {
        AgentName = agentName;
    }

    public String getCompanyName()
    {
        return CompanyName;
    }

    public void setCompanyName(String companyName)
    {
        CompanyName = companyName;
    }

    public String getEMail()
    {
        return EMail;
    }

    public void setEMail(String EMail)
    {
        this.EMail = EMail;
    }

    public String getAgentTel()
    {
        return AgentTel;
    }

    public void setAgentTel(String agentTel)
    {
        AgentTel = agentTel;
    }
}
