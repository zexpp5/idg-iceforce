package com.cxgz.activity.db.dao;

import android.app.Application;

import com.base.BaseApplication;
import com.cxgz.activity.db.entity.SDPowerMenusEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by selson on 2017/8/21.
 * 权限菜单
 */
public class SDMenusDao extends BaseDao
{
    private SDMenusDao()
    {

    }

    public static synchronized SDMenusDao getInstance()
    {
        return SDMenusDaoHelper.sdMenusDao;
    }

    private static class SDMenusDaoHelper
    {
        private static final SDMenusDao sdMenusDao = new SDMenusDao();
    }

    /**
     * 保存静态数据字典
     * @param application
     * @param sdPowerMenusEntities
     */
    public static void saveMenus(Application application,
                                 List<SDPowerMenusEntity> sdPowerMenusEntities)
    {

        if (sdPowerMenusEntities == null || sdPowerMenusEntities.size() < 0)
        {
            return;
        }
        try
        {
            // 删除表
            BaseApplication.getmDbUtils().dropTable(SDPowerMenusEntity.class);
            // 创建表
            BaseApplication.getmDbUtils().createTableIfNotExist(SDPowerMenusEntity.class);
        } catch (DbException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for (SDPowerMenusEntity sdPowerMenusEntity : sdPowerMenusEntities)
        {
            try
            {
                BaseApplication.getmDbUtils().save(sdPowerMenusEntity);
            } catch (DbException e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据Code查询静态数据
     */
    public List<SDPowerMenusEntity> findMenuByCode(String menuCode)
    {
        List<SDPowerMenusEntity> dicList = new ArrayList<SDPowerMenusEntity>();
        try
        {
            dicList = BaseApplication.getmDbUtils()
                    .findAll(Selector.from(SDPowerMenusEntity.class).where("code", "=", menuCode));
        } catch (DbException e)
        {
            dicList = null;
            e.printStackTrace();
        }
        return dicList;
    }

    /**
     * 通用传入查询
     * condition 传入要查的字段。
     * 比如："name": "公司公告",
     * condition传入name， content传入公司公告
     */
    public List<SDPowerMenusEntity> findMenuByYourSelf(String condition, String content)
    {
        List<SDPowerMenusEntity> dicList = new ArrayList<SDPowerMenusEntity>();
        try
        {
            dicList = BaseApplication.getmDbUtils()
                    .findAll(Selector.from(SDPowerMenusEntity.class).where(condition, "=", content));
        } catch (DbException e)
        {
            dicList = null;
            e.printStackTrace();
        }
        return dicList;
    }

    /**
     * 根据type 返回3个类型的对应的类型的数据
     * menu=菜单，function=功能，s_menu = 系统设置
     * type 1=菜单，2=功能，3= 系统设置
     */
    public List<SDPowerMenusEntity> findMenuyForType(int type)
    {
        List<SDPowerMenusEntity> sdPowerMenusEntities = new ArrayList<SDPowerMenusEntity>();
        String menuyType = "";
        switch (type)
        {
            case 1:
                menuyType = "menu";
                break;

            case 2:
                menuyType = "function";
                break;

            case 3:
                menuyType = "s_menu ";
                break;
        }
        try
        {
            sdPowerMenusEntities = BaseApplication.getmDbUtils()
                    .findAll(Selector.from(SDPowerMenusEntity.class)
                            .where("menuyType", "=", menuyType));
        } catch (DbException e)
        {
            sdPowerMenusEntities = null;
            e.printStackTrace();
        }
        return sdPowerMenusEntities;
    }


}
