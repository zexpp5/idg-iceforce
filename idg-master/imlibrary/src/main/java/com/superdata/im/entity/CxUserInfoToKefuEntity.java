package com.superdata.im.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;

/**
 * User: Selson
 * Date: 2016-08-02
 * FIXME 发送给客服的附件-实体类
 */
public class CxUserInfoToKefuEntity implements Serializable
{
    private String dpName;
    private String account;
    private String userId;
    private String realName;
    private String hxAccount;
    private String userType;
    private String email;
    private String telephone;
    private String job;
    private String sex;
    private String icon;
    private String attachment;
    private String phone;

    /**
     * 返回一个实体类，解析json
     */
    public static CxUserInfoToKefuEntity parse(String jsonString)
    {
        Gson gson = new Gson();
        CxUserInfoToKefuEntity info = gson.fromJson(jsonString, new TypeToken<CxUserInfoToKefuEntity>()
        {
        }.getType());
        return info == null ? null : info;
    }

    public static String returnJsonString(CxUserInfoToKefuEntity cxUserInfoToKefuEntity)
    {
        String jsonObjectString = new Gson().toJson(cxUserInfoToKefuEntity).toString();
        return jsonObjectString;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getDpName()
    {
        return dpName;
    }

    public void setDpName(String dpName)
    {
        this.dpName = dpName;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public String getRealName()
    {
        return realName;
    }

    public void setRealName(String realName)
    {
        this.realName = realName;
    }

    public String getHxAccount()
    {
        return hxAccount;
    }

    public void setHxAccount(String hxAccount)
    {
        this.hxAccount = hxAccount;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public String getSex()
    {
        return sex;
    }

    public void setSex(String sex)
    {
        this.sex = sex;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public String getAttachment()
    {
        return attachment;
    }

    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }
}