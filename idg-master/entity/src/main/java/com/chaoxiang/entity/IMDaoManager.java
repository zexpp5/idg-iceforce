package com.chaoxiang.entity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.entity.dao.DaoMaster;
import com.chaoxiang.entity.dao.DaoSession;


/**
 * @auth lwj
 * @date 2016-01-06
 * @desc dao管理者
 */
public class IMDaoManager
{
    private static IMDaoManager instance;
    private DaoMaster cx_jia_daoMaster;
    private DaoSession cx_jia_daoSession;
    private Cursor cx_jia_cursor;
    private SQLiteDatabase sqlDb;
    private Context context;
    private DaoMaster.DevOpenHelper helper;

    public static synchronized IMDaoManager getInstance()
    {
        if (instance == null)
        {
            instance = new IMDaoManager();
        }
        return instance;
    }

    public Context getContext()
    {
        return context;
    }

    public void initDB(Context context, String dbName)
    {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context, "cx_jia_" + dbName, null);
        instance.sqlDb = helper.getWritableDatabase();
        instance.cx_jia_daoMaster = new DaoMaster(instance.sqlDb);
        instance.cx_jia_daoSession = instance.cx_jia_daoMaster.newSession();
    }

    public DaoMaster getDaoMaster()
    {
        return cx_jia_daoMaster;
    }

    public DaoSession getDaoSession()
    {
        return cx_jia_daoSession;
    }

    public Cursor getCursor()
    {
        return cx_jia_cursor;
    }

    public SQLiteDatabase getDb()
    {
        return sqlDb;
    }


    /**
     * 只关闭helper就好,看源码就知道helper关闭的时候会关闭数据库
     */
    public void close()
    {
        if (helper != null)
        {
            helper.close();
            helper = null;
        }
        if (cx_jia_daoSession != null)
        {
            cx_jia_daoSession.clear();
            cx_jia_daoSession = null;
        }
    }

    public void clearDaoSession()
    {
        if (cx_jia_daoSession != null)
        {
            cx_jia_daoSession.clear();
            cx_jia_daoSession = null;
        }
    }

//    public void close()
//    {
//        if (sqlDb != null)
//        {
//            sqlDb.close();
//        }
//        if (cx_jia_daoSession != null)
//        {
//            cx_jia_daoSession.clear();
//        }
//
//    }
}
