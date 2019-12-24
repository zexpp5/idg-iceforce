package com.cxgz.activity.db.dao;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;

import com.base.BaseApplication;
import com.bean_erp.LoginListBean;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cx.utils.HanziToPinyin;
import com.entity.LoginDataBean;
import com.entity.SDDepartmentEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 */
public class SDAllConstactsDao extends BaseDao
{
    private SDAllConstactsDao()
    {
    }

    public static synchronized SDAllConstactsDao getInstance()
    {
        return SDAllConstactsHelper.sdAllConstactsDao;
    }

    public static class SDAllConstactsHelper
    {
        private static final SDAllConstactsDao sdAllConstactsDao = new SDAllConstactsDao();
    }


    /**
     * 根据account查询通讯录
     */
    public synchronized SDAllConstactsEntity findAllConstactsByAccount(String account)
    {
        List<String> list = new ArrayList<String>();
        list.add(account);
        List<SDAllConstactsEntity> userEntities = findAllUserByAccount(list);
        if (userEntities == null || userEntities.isEmpty())
        {
            return null;
        }
        return userEntities.get(0);
    }

    /**
     * 多个查询
     * 根据Account查询通讯录
     */
    public synchronized List<SDAllConstactsEntity> findAllUserByAccount(List<String> accountList)
    {
        List<SDAllConstactsEntity> userEntities = new ArrayList<>();
        if (accountList == null)
        {
            return userEntities;
        }
        String[] conditionArray = accountList.toArray(new String[accountList.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(Selector.from(SDAllConstactsEntity.class).where("account", "in",
                    conditionArray));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }


    /**
     * 根据account查询通讯录
     */
    public synchronized SDAllConstactsEntity findAllConstactsByImAccount(String imAccount)
    {
        List<String> list = new ArrayList<String>();
        list.add(imAccount);
        List<SDAllConstactsEntity> userEntities = findAllUserByImAccount(list);
        if (userEntities == null || userEntities.isEmpty())
        {
            return null;
        }
        return userEntities.get(0);
    }
    /**
     * 多个查询
     * 根据Account查询通讯录
     */
    public synchronized List<SDAllConstactsEntity> findAllUserByImAccount(List<String> accountList)
    {
        List<SDAllConstactsEntity> userEntities = new ArrayList<>();
        if (accountList == null)
        {
            return userEntities;
        }
        String[] conditionArray = accountList.toArray(new String[accountList.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(Selector.from(SDAllConstactsEntity.class).where("imAccount", "in",
                    conditionArray));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return userEntities;
    }
}