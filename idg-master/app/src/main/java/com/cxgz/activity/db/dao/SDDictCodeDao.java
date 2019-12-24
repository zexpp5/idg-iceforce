package com.cxgz.activity.db.dao;

import android.app.Application;

import com.base.BaseApplication;
import com.cxgz.activity.db.entity.SDDictionaryEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * 登录获取的静态数据
 */
public class SDDictCodeDao extends BaseDao
{

//    public SDDictCodeDao(Context context)
//    {
//        super(context);
//        mDbUtils = SDDbHelp.createDbUtils(context);
//        this.context = context;
//    }

    private SDDictCodeDao()
    {

    }

    public static synchronized SDDictCodeDao getInstance()
    {
        return SDDictCodeDaoHelper.sdDictCodeDao;
    }

    private static class SDDictCodeDaoHelper
    {
        private static final SDDictCodeDao sdDictCodeDao = new SDDictCodeDao();
    }


    /**
     * 保存静态数据字典
     *
     * @param application
     * @param Dictionarys
     */
    public static void saveDictionary(Application application,
                                      List<SDDictionaryEntity> Dictionarys)
    {
        if (Dictionarys == null || Dictionarys.size() < 0)
        {
            return;
        }
        for (int i = 0; i < Dictionarys.size(); i++)
        {
            Dictionarys.get(i).setDictId(i + 1);
        }
        try
        {
            // 删除表
            BaseApplication.getmDbUtils().dropTable(SDDictionaryEntity.class);
            // 创建表
            BaseApplication.getmDbUtils().createTableIfNotExist(SDDictionaryEntity.class);
        } catch (DbException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (SDDictionaryEntity Dictionary : Dictionarys)
        {
            try
            {
                BaseApplication.getmDbUtils().save(Dictionary);
            } catch (DbException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据Code查询静态数据
     */
    public List<SDDictionaryEntity> findDictByCode(String dictCode)
    {
        List<SDDictionaryEntity> dicList = new ArrayList<SDDictionaryEntity>();
        try
        {
            dicList = BaseApplication.getmDbUtils()
                    .findAll(Selector.from(SDDictionaryEntity.class).where("dict_code", "=", dictCode));
        } catch (DbException e)
        {
            dicList = null;
            e.printStackTrace();
        }
        return dicList;
    }

    /**
     * 根据Remark查询静态数据
     */
    public List<SDDictionaryEntity> findDictByRemark(String dictRemark)
    {
        List<SDDictionaryEntity> dicList = new ArrayList<SDDictionaryEntity>();
        try
        {
            dicList = BaseApplication.getmDbUtils()
                    .findFirst(Selector.from(SDDictionaryEntity.class).where("dict_remark", "=", dictRemark));
        } catch (DbException e)
        {
            dicList = null;
            e.printStackTrace();
        }

        return dicList;
    }

}
