package com.cxgz.activity.db.dao;

import com.cxgz.activity.superqq.bean.SDSortEntity;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.NoAutoIncrement;
import com.lidroid.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * 用户列表
 */
@Table(name = "SYS_ALL_CONSTACTS")
public class SDAllConstactsEntity extends SDSortEntity implements Serializable
{
    //用户ID
    @Id(column = "mainid")
    private long mainid;

    //用户ID
    @Column(column = "eid")
    private long eid;

    //名字
    @Column(column = "name")
    private String name;

    //部门名字
    @Column(column = "deptName")
    private String deptName;

    //部门分类名字
    @Column(column = "deptNameABC")
    private String deptNameABC;

    //工作-职位
    @Column(column = "job")
    private String job;

    //IM账号
    @Column(column = "imAccount")
    private String imAccount;

    //账号授权 1-启用 2-停用
    @Column(column = "status")
    private int status;

    //    超级用户状态
    //1=启用，0=停用
    @Column(column = "superStatus")
    private int superStatus;

    //是否是超级用户
    //1=是，0=否
    @Column(column = "isSuper")
    private int isSuper;

    //用户类型
    //1=公司管理员，2=普通用户
    @Column(column = "userType")
    private int userType;

    @Column(column = "code")
    private String code;

    //用户账号-一般指登录的手机账号。
    @Column(column = "account")
    private String account;

    @Column(column = "ename")
    private String ename;

    //头像
    @Column(column = "hignIcon")
    private String hignIcon;

    //头像
    @Column(column = "icon")
    private String icon;

    //性别
    @Column(column = "sex")
    private int sex;

    //岁数
    @Column(column = "age")
    private int age;

    //家庭住址地址
    @Column(column = "address")
    private String address;

    //生日
    @Column(column = "birthDate")
    private String birthDate;

    //公司ID
    @Column(column = "companyId")
    private long companyId;

    //合同信息 1=正式 2-实习 3-雇佣 4-兼职 5-试用期
    @Column(column = "contract")
    private int contract;

    //部门id
    @Column(column = "deptId")
    private long deptId;

    //学历 1=初中，2=高中，3=大专，4=本科，5=硕士，6=博士
    @Column(column = "education")
    private int education;

    //邮箱
    @Column(column = "email")
    private String email;

    //入职时间
    @Column(column = "employTime")
    private String employTime;

    //IM状态 0-停用 1启用
    @Column(column = "imStatus")
    private int imStatus;


    //任职状态 1-正式 2-试用期 3-在职 4-离职
    @Column(column = "jobStatus")
    private int jobStatus;

    //层级归属 1-管理岑 2=部门干部 3员工
    @Column(column = "level")
    private int level;

    //联系地址
    @Column(column = "linkAddress")
    private String linkAddress;

    //上级的ID
    @Column(column = "managerId")
    private long managerId;

    //婚姻状态 1=未婚未育，2=未婚已育，3=已婚未育，4=已婚已育
    @Column(column = "marry")
    private int marry;

    //户籍 贯籍
    @Column(column = "originAddr")
    private String originAddr;

    //腾讯旗下的一个即时聊天
    @Column(column = "qq")
    private String qq;

    //薪酬
    @Column(column = "salary")
    private String salary;

    @Column(column = "phone")
    private String phone;

    //联系电话
    @Column(column = "telephone")
    private String telephone;

    //微信
    @Column(column = "weChat")
    private String weChat;

    /**
     * 开始
     * 用于兼容前面的版本
     */
    private String jobRole;

    public String getDeptNameABC()
    {
        return deptNameABC;
    }

    public void setDeptNameABC(String deptNameABC)
    {
        this.deptNameABC = deptNameABC;
    }

    public String getHignIcon()
    {
        return hignIcon;
    }

    public void setHignIcon(String hignIcon)
    {
        this.hignIcon = hignIcon;
    }

    public String getEname()
    {
        return ename;
    }

    public void setEname(String ename)
    {
        this.ename = ename;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public long getUserId()
    {
        return eid;
    }

    public String getJobRole()
    {
        return jobRole;
    }

    public void setJobRole(String jobRole)
    {
        this.jobRole = jobRole;
    }

    public int getSuperStatus()
    {
        return superStatus;
    }

    public void setSuperStatus(int superStatus)
    {
        this.superStatus = superStatus;
    }

    public int getIsSuper()
    {
        return isSuper;
    }

    public void setIsSuper(int isSuper)
    {
        this.isSuper = isSuper;
    }

    /**
     * 结束
     * 用于兼容前面的版本
     */


    public long getEid()
    {
        return eid;
    }

    public void setEid(long eid)
    {
        this.eid = eid;
    }

    public String getImAccount()
    {
        return imAccount;
    }

    public void setImAccount(String imAccount)
    {
        this.imAccount = imAccount;
    }

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    public int getSex()
    {
        return sex;
    }

    public void setSex(int sex)
    {
        this.sex = sex;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getBirthDate()
    {
        return birthDate;
    }

    public void setBirthDate(String birthDate)
    {
        this.birthDate = birthDate;
    }

    public long getCompanyId()
    {
        return companyId;
    }

    public void setCompanyId(long companyId)
    {
        this.companyId = companyId;
    }

    public int getContract()
    {
        return contract;
    }

    public void setContract(int contract)
    {
        this.contract = contract;
    }

    public long getDeptId()
    {
        return deptId;
    }

    public void setDeptId(long deptId)
    {
        this.deptId = deptId;
    }

    public String getDeptName()
    {
        return deptName;
    }

    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public int getEducation()
    {
        return education;
    }

    public void setEducation(int education)
    {
        this.education = education;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public String getEmployTime()
    {
        return employTime;
    }

    public void setEmployTime(String employTime)
    {
        this.employTime = employTime;
    }

    public int getImStatus()
    {
        return imStatus;
    }

    public void setImStatus(int imStatus)
    {
        this.imStatus = imStatus;
    }

    public String getJob()
    {
        return job;
    }

    public void setJob(String job)
    {
        this.job = job;
    }

    public int getJobStatus()
    {
        return jobStatus;
    }

    public void setJobStatus(int jobStatus)
    {
        this.jobStatus = jobStatus;
    }

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public String getLinkAddress()
    {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress)
    {
        this.linkAddress = linkAddress;
    }

    public long getManagerId()
    {
        return managerId;
    }

    public void setManagerId(long managerId)
    {
        this.managerId = managerId;
    }

    public int getMarry()
    {
        return marry;
    }

    public void setMarry(int marry)
    {
        this.marry = marry;
    }

    public String getOriginAddr()
    {
        return originAddr;
    }

    public void setOriginAddr(String originAddr)
    {
        this.originAddr = originAddr;
    }

    public String getQq()
    {
        return qq;
    }

    public void setQq(String qq)
    {
        this.qq = qq;
    }

    public String getSalary()
    {
        return salary;
    }

    public void setSalary(String salary)
    {
        this.salary = salary;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getTelephone()
    {
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone = telephone;
    }

    public int getUserType()
    {
        return userType;
    }

    public void setUserType(int userType)
    {
        this.userType = userType;
    }

    public String getWeChat()
    {
        return weChat;
    }

    public void setWeChat(String weChat)
    {
        this.weChat = weChat;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SDAllConstactsEntity)
        {
            SDAllConstactsEntity sdUserEntity = (SDAllConstactsEntity) obj;
            return sdUserEntity.getImAccount() != null && imAccount.equalsIgnoreCase(sdUserEntity.getImAccount().trim());
        }
        return false;
    }

}
