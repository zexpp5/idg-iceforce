package com.chaoxiang.imsdk.chat;

import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.dao.IMAddFriendDao;
import com.chaoxiang.entity.dao.IMVoiceGroupDao;

/**
 * User: Selson
 * Date: 2016-06-23
 * FIXME 好友添加管理
 */
public class CxAddFriendManager
{
    private static CxAddFriendManager instance;

    private IMAddFriendDao iMAddFriendDao;

    private CxAddFriendManager()
    {

    }

    public static synchronized CxAddFriendManager getInstance()
    {
        if (instance == null || instance.iMAddFriendDao == null)
        {
            instance = new CxAddFriendManager();
            instance.iMAddFriendDao = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao();
        }
        return instance;
    }

    public IMAddFriendDao getIMAddFriendDao()
    {
        return iMAddFriendDao;
    }

    public static void clear()
    {
        if (instance != null)
            if (instance.iMAddFriendDao != null)
                instance.iMAddFriendDao = null;
    }
} 