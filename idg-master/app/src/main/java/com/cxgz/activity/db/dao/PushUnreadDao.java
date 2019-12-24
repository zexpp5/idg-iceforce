package com.cxgz.activity.db.dao;

import android.content.Context;
import android.database.Cursor;

import com.cxgz.activity.db.entity.UnreadEntity;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * @des
 */
public class PushUnreadDao extends BaseDao
{
    private String mUserId;
    private Context context;

    public PushUnreadDao(Context context)
    {
        super(context);
        this.mUserId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
        this.context = context;
    }

    /**
     * 保存未读条数
     */
    public void save(UnreadEntity entity)
    {
        try
        {
            db.saveOrUpdate(entity);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * placeIndex
     * 获取未读条数
     */
    public int getUnreadCount(int type)
    {
        String user_id = SPUtils.get(context, SPUtils.USER_ID, "") + "";
        String unreadId = mUserId + type;
        String sql = "select UNREADCOUNT from push_unread where UNREAD_ID = " + unreadId;
        //String sql = "select UNREADCOUNT from push_unread where TYPE = " + type +" and USER_ID = "+mUserId;
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo(sql));
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        if (null != cursor && cursor.moveToFirst())
        {
            int unreadcountIndex = cursor.getColumnIndex("UNREADCOUNT");
            return cursor.getInt(unreadcountIndex);
        }
        return 0;
    }

    /**
     * 获取整个模块未读数
     */
    public List<UnreadEntity> getUnreadEntityByPlaceIndex(int type)
    {
        String user_id = SPUtils.get(context, SPUtils.USER_ID, "") + "";
        List<UnreadEntity> entitys = new ArrayList<>();
        String sql = "select * from push_unread where PALCE_INDEX = " + type + " and UNREAD_ID like '" + mUserId + "%'";
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo(sql));
            while (cursor.moveToNext())
            {
                UnreadEntity entity = new UnreadEntity();
                int unreadcountIndex = cursor.getColumnIndex("UNREADCOUNT");
                int unreadIdIndex = cursor.getColumnIndex("UNREAD_ID");
                int typeIndex = cursor.getColumnIndex("TYPE");
                int userIdIndex = cursor.getColumnIndex("USER_ID");
                int isReadIndex = cursor.getColumnIndex("IS_READ");
                int remarkIndex = cursor.getColumnIndex("REMARK");
                int palecIndex = cursor.getColumnIndex("PALCE_INDEX");
                entity.setType(Integer.parseInt(cursor.getString(typeIndex)));
                entity.setRemark(cursor.getString(remarkIndex));
                entity.setUnreadCount(Integer.parseInt(cursor.getString(unreadcountIndex)));
                entity.setPlaceIndex(Integer.parseInt(cursor.getString(palecIndex)));
                entitys.add(entity);
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        } finally
        {
            closeCusor(cursor);
        }

        return entitys;
    }


    /**
     * 获取整个模块未读数
     */
    public List<UnreadEntity> getUnreadEntityByWorkType(int type)
    {
        String user_id = SPUtils.get(context, SPUtils.USER_ID, "") + "";
        List<UnreadEntity> entitys = new ArrayList<>();
        String sql = "select * from push_unread where WORK_MENU = " + type + " and UNREAD_ID like '" + mUserId + "%'";
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo(sql));
            while (cursor.moveToNext())
            {
                UnreadEntity entity = new UnreadEntity();
                int unreadcountIndex = cursor.getColumnIndex("UNREADCOUNT");
                int unreadIdIndex = cursor.getColumnIndex("UNREAD_ID");
                int typeIndex = cursor.getColumnIndex("TYPE");
                int userIdIndex = cursor.getColumnIndex("USER_ID");
                int isReadIndex = cursor.getColumnIndex("IS_READ");
                int remarkIndex = cursor.getColumnIndex("REMARK");
                int palecIndex = cursor.getColumnIndex("PALCE_INDEX");
                entity.setType(Integer.parseInt(cursor.getString(typeIndex)));
                entity.setRemark(cursor.getString(remarkIndex));
                entity.setUnreadCount(Integer.parseInt(cursor.getString(unreadcountIndex)));
                entity.setPlaceIndex(Integer.parseInt(cursor.getString(palecIndex)));
                entitys.add(entity);
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        } finally
        {
            closeCusor(cursor);
        }

        return entitys;
    }

    public void setUnreadCount(int type, int count)
    {
        try
        {
            db.update(new UnreadEntity(type, count, SPUtils.get(context, SPUtils.USER_ID, "") + ""), "UNREADCOUNT");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    public void setUnreadCount(int type, int count, boolean isRead, int placeIndex, int workMenu)
    {
        try
        {
            db.update(new UnreadEntity(type, count, SPUtils.get(context, SPUtils.USER_ID, "") + "", isRead, placeIndex, workMenu), "UNREADCOUNT");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    public UnreadEntity findByUserId(int type)
    {
        String unreadId = mUserId + type;
        try
        {
            return db.findById(UnreadEntity.class, unreadId);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public UnreadEntity findByType(int type, String userId)
    {
        UnreadEntity entity = null;
        String sql = "select * from PUSH_UNREAD WHERE TYPE = " + type + " AND USER_ID = " + userId;
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo(sql));
            while (cursor.moveToNext())
            {
                entity = new UnreadEntity();
                int unreadcountIndex = cursor.getColumnIndex("UNREADCOUNT");
                int unreadIdIndex = cursor.getColumnIndex("UNREAD_ID");
                int typeIndex = cursor.getColumnIndex("TYPE");
                int userIdIndex = cursor.getColumnIndex("USER_ID");
                int isReadIndex = cursor.getColumnIndex("IS_READ");
                int remarkIndex = cursor.getColumnIndex("REMARK");
                int palecIndex = cursor.getColumnIndex("PALCE_INDEX");
                int workMenuIndex = cursor.getColumnIndex("WORK_MENU");
                entity.setType(Integer.parseInt(cursor.getString(typeIndex)));
                entity.setRemark(cursor.getString(remarkIndex));
                entity.setUnreadCount(Integer.parseInt(cursor.getString(unreadcountIndex)));
                entity.setPlaceIndex(Integer.parseInt(cursor.getString(palecIndex)));
                entity.setWorkMenu(Integer.parseInt(cursor.getString(workMenuIndex)));
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        } finally
        {
            closeCusor(cursor);
        }

        return entity;
    }
}
