package com.chaoxiang.imsdk.chat;

import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMKeFu;
import com.chaoxiang.entity.dao.IMKeFuDao;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Selson
 * Date: 2016-09-21
 * FIXME
 */
public class CXKefuManager
{
    private static CXKefuManager instance;

    private CXKefuManager()
    {

    }

    public static synchronized CXKefuManager getInstance()
    {
        if (instance == null )
        {
            instance = new CXKefuManager();
        }
        return instance;
    }

    public static void clear()
    {

    }

    /**
     * 获取客服通讯录列表
     *
     * @return
     */
    public List<IMKeFu> loadKefuContactList()
    {
        List<IMKeFu> kefuList = new ArrayList<>();
        kefuList = IMDaoManager.getInstance().getDaoSession().getIMKeFuDao().loadAll();
        return kefuList;
    }

    public IMKeFu findUserByImAccount(String imAccount)
    {
        IMKeFu userEntities = IMDaoManager.getInstance().getDaoSession().getIMKeFuDao().queryBuilder().where(IMKeFuDao.Properties.ImAccount.eq(imAccount)).unique();
        return userEntities;
    }
} 