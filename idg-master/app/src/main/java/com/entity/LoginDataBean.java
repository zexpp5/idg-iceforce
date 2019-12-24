package com.entity;

import com.cxgz.activity.db.entity.SDDictionaryEntity;
import com.cxgz.activity.db.entity.SDPowerMenusEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/5/10.
 */
public class LoginDataBean implements Serializable
{

    public LoginEntity data;
    private int status;

    public LoginEntity getData()
    {
        return data;
    }

    public void setData(LoginEntity data)
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

    public static class LoginEntity
    {

        private String account;

        private String createTime;

        private int status;

        private int eid;

        private String email;

        private String icon;

        private String imAccount;

        private String userName;

        private String sex;

        private String telephone;

        private String token;

        private String updateTime;

        private int userType;

        private long currencyId;

        private String currencyName;

        private String applyGroup;

        /**
         * 登录用户的角色名称(admin为管理员)
         */
        private String roleName;

        private String expireTime;

        //云版本
        private String dpName;

        //云版本
        private String dpId;

        private String job;

        private int companyId;

        private String companyName;
        //菜单权限控制
        private String companyLevel;
        //是否开启IM
        private String imStatus;
        //是否超级用户及状态
        private int isSuper;
        private int superStatus;

        //静态数据
        private List<SDDictionaryEntity> staticList;

        //菜单控制
        private List<SDPowerMenusEntity> menuList;

        //例子账状态 1=需要提示下载例子账，2=下载过例子账，3=清空过例子账或者跳过下载例子账
        private int caseStatus;
        //0=未付费，1=付费
        private int isVip;

        private String versionNum;  //1=统合行业版本，2=服装行业版本，3=电器五金行业版本，4=食品行业版本

        private boolean hasLocal;

        private boolean isShare;//是否需要分享按钮

        private String yaoUrl;

        //专属定制

        private int level;
        private int s_fingerprintLogin; //指纹登录
        private int s_location; //开启定位
        private int s_read;        //已读-未读
        private String androidLogo;     //引导页
        private String s_companyName;  //企业名称
        private String s_platformName;  //平台名称

        //年会
        private String showAnnualMeeting;

        public String getShowAnnualMeeting()
        {
            return showAnnualMeeting;
        }

        public void setShowAnnualMeeting(String showAnnualMeeting)
        {
            this.showAnnualMeeting = showAnnualMeeting;
        }

        private int isAnnualTem;

        public int getIsAnnualTem()
        {
            return isAnnualTem;
        }

        public void setIsAnnualTem(int isAnnualTem)
        {
            this.isAnnualTem = isAnnualTem;
        }

        private int isUpdatePwd;

        public int getIsUpdatePwd()
        {
            return isUpdatePwd;
        }

        public void setIsUpdatePwd(int isUpdatePwd)
        {
            this.isUpdatePwd = isUpdatePwd;
        }

        public int getS_fingerprintLogin()
        {
            return s_fingerprintLogin;
        }

        public void setS_fingerprintLogin(int s_fingerprintLogin)
        {
            this.s_fingerprintLogin = s_fingerprintLogin;
        }

        public int getS_location()
        {
            return s_location;
        }

        public void setS_location(int s_location)
        {
            this.s_location = s_location;
        }

        public int getS_read()
        {
            return s_read;
        }

        public void setS_read(int s_read)
        {
            this.s_read = s_read;
        }

        public String getAndroidLogo()
        {
            return androidLogo;
        }

        public void setAndroidLogo(String androidLogo)
        {
            this.androidLogo = androidLogo;
        }

        public String getS_companyName()
        {
            return s_companyName;
        }

        public void setS_companyName(String s_companyName)
        {
            this.s_companyName = s_companyName;
        }

        public String getS_platformName()
        {
            return s_platformName;
        }

        public void setS_platformName(String s_platformName)
        {
            this.s_platformName = s_platformName;
        }

        public int getIsSuper()
        {
            return isSuper;
        }

        public void setIsSuper(int isSuper)
        {
            this.isSuper = isSuper;
        }

        public int getSuperStatus()
        {
            return superStatus;
        }

        public void setSuperStatus(int superStatus)
        {
            this.superStatus = superStatus;
        }

        public String getYaoUrl()
        {
            return yaoUrl;
        }

        public void setYaoUrl(String yaoUrl)
        {
            this.yaoUrl = yaoUrl;
        }

        public String getDpId()
        {
            return dpId;
        }

        public void setDpId(String dpId)
        {
            this.dpId = dpId;
        }

        public int getCaseStatus()
        {
            return caseStatus;
        }

        public void setCaseStatus(int caseStatus)
        {
            this.caseStatus = caseStatus;
        }

        public int getIsVip()
        {
            return isVip;
        }

        public void setIsVip(int isVip)
        {
            this.isVip = isVip;
        }

        public int getCompanyId()
        {
            return companyId;
        }

        public void setCompanyId(int companyId)
        {
            this.companyId = companyId;
        }

        public String getCompanyLevel()
        {
            return companyLevel;
        }

        public void setCompanyLevel(String companyLevel)
        {
            this.companyLevel = companyLevel;
        }

        public String getImStatus()
        {
            return imStatus;
        }

        public void setImStatus(String imStatus)
        {
            this.imStatus = imStatus;
        }

        public List<SDDictionaryEntity> getStaticList()
        {
            return staticList;
        }

        public void setStaticList(List<SDDictionaryEntity> staticList)
        {
            this.staticList = staticList;
        }

        public List<SDPowerMenusEntity> getMenuList()
        {
            return menuList;
        }

        public void setMenuList(List<SDPowerMenusEntity> menuList)
        {
            this.menuList = menuList;
        }

        public String getDpName()
        {
            return dpName;
        }

        public void setDpName(String dpName)
        {
            this.dpName = dpName;
        }

        public String getJob()
        {
            return job;
        }

        public void setJob(String job)
        {
            this.job = job;
        }

        public int getLevel()
        {
            return level;
        }

        public void setLevel(int level)
        {
            this.level = level;
        }

        public String getCompanyName()
        {
            return companyName;
        }

        public void setCompanyName(String companyName)
        {
            this.companyName = companyName;
        }

        public String getExpireTime()
        {
            return expireTime;
        }

        public void setExpireTime(String expireTime)
        {
            this.expireTime = expireTime;
        }

        public String getApplyGroup()
        {
            return applyGroup;
        }

        public void setApplyGroup(String applyGroup)
        {
            this.applyGroup = applyGroup;
        }

        public String getRoleName()
        {
            return roleName;
        }

        public void setRoleName(String roleName)
        {
            this.roleName = roleName;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public long getCurrencyId()
        {
            return currencyId;
        }

        public void setCurrencyId(long currencyId)
        {
            this.currencyId = currencyId;
        }

        public String getCurrencyName()
        {
            return currencyName;
        }

        public void setCurrencyName(String currencyName)
        {
            this.currencyName = currencyName;
        }

        public boolean isHasLocal()
        {
            return hasLocal;
        }

        public void setHasLocal(boolean hasLocal)
        {
            this.hasLocal = hasLocal;
        }

        public String getVersionNum()
        {
            return versionNum;
        }

        public void setVersionNum(String versionNum)
        {
            this.versionNum = versionNum;
        }

        public boolean isShare()
        {
            return isShare;
        }

        public void setShare(boolean share)
        {
            isShare = share;
        }

        public int getUserType()
        {
            return userType;
        }

        public void setUserType(int userType)
        {
            this.userType = userType;
        }

        public void setAccount(String account)
        {
            this.account = account;
        }

        public String getAccount()
        {
            return this.account;
        }

        public void setCreateTime(String createTime)
        {
            this.createTime = createTime;
        }

        public String getCreateTime()
        {
            return this.createTime;
        }

        public void setEid(int eid)
        {
            this.eid = eid;
        }

        public int getEid()
        {
            return this.eid;
        }

        public void setEmail(String email)
        {
            this.email = email;
        }

        public String getEmail()
        {
            return this.email;
        }

        public void setIcon(String icon)
        {
            this.icon = icon;
        }

        public String getIcon()
        {
            return this.icon;
        }

        public void setImAccount(String imAccount)
        {
            this.imAccount = imAccount;
        }

        public String getImAccount()
        {
            return this.imAccount;
        }

        public String getUserName()
        {
            return userName;
        }

        public void setUserName(String userName)
        {
            this.userName = userName;
        }

        public void setSex(String sex)
        {
            this.sex = sex;
        }

        public String getSex()
        {
            return this.sex;
        }

        public void setTelephone(String telephone)
        {
            this.telephone = telephone;
        }

        public String getTelephone()
        {
            return this.telephone;
        }

        public void setToken(String token)
        {
            this.token = token;
        }

        public String getToken()
        {
            return this.token;
        }

        public void setUpdateTime(String updateTime)
        {
            this.updateTime = updateTime;
        }

        public String getUpdateTime()
        {
            return this.updateTime;
        }
    }
}