/**
 * @title:SDDbHelp.java TODO包含类名列表
 * Copyright (C) oa.cn All right reserved.
 * @version：v3.0,2015年4月8日
 */
package com.cxgz.activity.db.help;

import android.content.Context;
import android.database.Cursor;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDFileListEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;
import com.tencent.connect.UserInfo;
import com.utils.SPUtils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @name SDDbHelp
 * @description 数据库帮助类
 */
public class SDDbHelp
{
    private static DbUtils dbUtils;
    /**
     * 数据库名
     */
    public static String dbName = "suda_cx";
    /**
     * 数据库版本号
     */
    public static final int dbVersion = 1;

    private SDDbHelp()
    {
    }

    public static DbUtils createDbUtils(Context context)
    {
        if (dbUtils == null)
        {
            dbName = SPUtils.get(context, SPUtils.IM_NAME, "") + "_" + dbName;
            dbUtils = DbUtils.create(context, dbName, dbVersion, new DbUtils.DbUpgradeListener()
            {
                @Override
                public void onUpgrade(DbUtils dbUtils, int oldVersion, int newVersion)
                {
                    if (newVersion != oldVersion)
                    {   
                        //按需求进行更新
                    }
                }
            });
            dbUtils.configDebug(SDLogUtil.FLAG);
            dbUtils.configAllowTransaction(true);
        }
        try
        {
            dbUtils.createTableIfNotExist(SDFileListEntity.class);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return dbUtils;
    }

    public static void updateTable(DbUtils dbUtils, Class<?> tClass)
    {
        try
        {
            if (dbUtils.tableIsExist(tClass))
            {
                String tableName = tClass.getName();
                tableName = tableName.replace(".", "_");
                String sql = "select * from " + tableName;
                //声名一个map用来保存原有表中的字段
                Map<String, String> filedMap = new HashMap<>();
                //执行自定义的sql语句
                Cursor cursor = dbUtils.execQuery(sql);
                int count = cursor.getColumnCount();
                for (int i = 0; i < count; i++)
                {
                    filedMap.put(cursor.getColumnName(i), cursor.getColumnName(i));
                }
                //cursor在用完之后一定要close
                cursor.close();
                //下面用到了一些反射知识，下面是获取实体类的所有私有属性（即我们更改表结构后的所有字段名）
                Field[] fields = UserInfo.class.getDeclaredFields();

                for (int i = 0; i < fields.length; i++)
                {
                    if (filedMap.containsKey(fields[i].getName()))
                    {
                        //假如字段名已存在就进行下次循环
                        continue;
                    } else
                    {
                        //不存在，就放到map中，并且给表添加字段
                        filedMap.put(fields[i].getName(), fields[i].getName());
                        //根据属性的类型给表增加字段
                        if (fields[i].getType().toString().equals("class java.lang.String"))
                        {

                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " TEXT ");
                        } else if (fields[i].getType().equals("int") || fields[i].getType().equals("long") || fields[i].getType
                                ().equals("boolean"))
                        {
                            dbUtils.execNonQuery("alter table " + tableName + " add " + fields[i].getName() + " INTEGER ");
                        }
                    }
                }


            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }


    }

}

