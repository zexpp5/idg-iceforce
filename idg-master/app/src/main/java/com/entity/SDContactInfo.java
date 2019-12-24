package com.entity;


import com.cxgz.activity.db.entity.Menus;
import com.cxgz.activity.db.entity.SDUserDeptRelEntity;
import com.cxgz.activity.db.dao.SDUserEntity;

import java.util.List;

/**
 * 用于接收通讯录的3张表
 *
 * @desc
 */
public class SDContactInfo
{
    //联系人列表
    private List<SDUserEntity> users;
    //部门列表
    private List<SDDepartmentEntity> dps;
    //用户和部门的对应关系
    private List<SDUserDeptRelEntity> user_dp_rels;//user_dp_rels

    private List<Menus> userMenus;


    public List<Menus> getUserMenus() {
        return userMenus;
    }

    public void setUserMenus(List<Menus> userMenus) {
        this.userMenus = userMenus;
    }

    public List<SDUserEntity> getUsers()
    {
        return users;
    }

    public void setUsers(List<SDUserEntity> users)
    {
        this.users = users;
    }

    public List<SDDepartmentEntity> getDps()
    {
        return dps;
    }

    public void setDps(List<SDDepartmentEntity> dps)
    {
        this.dps = dps;
    }

    public List<SDUserDeptRelEntity> getUser_dp_rels()
    {
        return user_dp_rels;
    }

    public void setUser_dp_rels(List<SDUserDeptRelEntity> user_dp_rels)
    {
        this.user_dp_rels = user_dp_rels;
    }
}
