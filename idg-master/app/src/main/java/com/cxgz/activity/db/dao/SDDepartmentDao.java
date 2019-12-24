package com.cxgz.activity.db.dao;

import android.content.Context;
import android.database.Cursor;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDDepartmentEntity;
import com.lidroid.xutils.db.sqlite.SqlInfo;
import com.lidroid.xutils.db.table.DbModel;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 部门
 */
public class SDDepartmentDao extends BaseDao
{
    private String userId;

    public SDDepartmentDao(Context context)
    {
        super(context);
        userId = (String) SPUtils.get(context, SPUtils.USER_ID, "-1");
    }

    /**
     * 通过departmentId获取用户
     * @param dpId
     * @return
     */
    public List<SDUserEntity> findUserByDepartmentId(String dpId)
    {
        List<SDUserEntity> sdUserEntityList = SDUserDao.getInstance().findAllUsersBydpId(dpId);
        if (sdUserEntityList != null)
            return sdUserEntityList;
        else
            return null;
    }

    /**
     * 通过部门id查找该部门的员工不包含自己
     * @param dpId
     * @return
     */
    public int findDpCount(String dpId)
    {
        Cursor cursor = null;
        int count = 0;
        SqlInfo sqlInfo = new SqlInfo("SELECT count(*) FROM SYS_USER WHERE USER_ID IN " +
                "(SELECT USER_ID FROM USER_DEPT_REL WHERE dpId=?) AND STATUS = ? AND USER_TYPE = ? AND USER_ID != ?", dpId, "1", "1", userId);
        try
        {
            cursor = db.execQuery(sqlInfo);
            if (cursor.moveToFirst())
            {
                count = cursor.getInt(0);
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return count;
    }

    //查询全部部门
    public List<SDDepartmentEntity> findAllDeparment()
    {
        List<SDDepartmentEntity> departmentEntities = new ArrayList<SDDepartmentEntity>();
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo("SELECT * FROM SYS_DEPARTMENT"));
            while (cursor.moveToNext())
            {
                SDDepartmentEntity DepartmentEntity = new SDDepartmentEntity();
                int dpId = cursor.getColumnIndex("dpId");
                int dpName = cursor.getColumnIndex("dpName");
                int dpCode = cursor.getColumnIndex("dpCode");
                DepartmentEntity.setDpId(Integer.parseInt(cursor.getString(dpId)));
                DepartmentEntity.setDpName(cursor.getString(dpName));
                DepartmentEntity.setDpCode(cursor.getString(dpCode));
                departmentEntities.add(DepartmentEntity);
            }
        } catch (Exception e)
        {

        } finally
        {
            closeCusor(cursor);
        }
        return departmentEntities;
    }

    public SDDepartmentEntity findByDpCode(String str)
    {
        SDDepartmentEntity entity = new SDDepartmentEntity();
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo("SELECT * FROM SYS_DEPARTMENT WHERE dpCode = ?", str));
            while (cursor.moveToNext())
            {
                int dpId = cursor.getColumnIndex("dpId");
                int dpName = cursor.getColumnIndex("dpName");
                int dpCode = cursor.getColumnIndex("dpCode");
                entity.setDpId(Integer.parseInt(cursor.getString(dpId)));
                entity.setDpName(cursor.getString(dpName));
                entity.setDpCode(cursor.getString(dpCode));
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            closeCusor(cursor);
        }

        return entity;
    }


    /**
     * 通过userid获取部门
     *
     * @param userId
     * @return
     */
    public List<SDDepartmentEntity> findDeptNameByUserID(String userId)
    {
        List<SDDepartmentEntity> DepartmentEntitys = new ArrayList<>();
        Cursor cursor = null;
        try
        {
            cursor = db.execQuery(new SqlInfo("SELECT * FROM SYS_DEPARTMENT WHERE dpId IN " +
                    "(SELECT dpId FROM USER_DEPT_REL WHERE USER_ID=?)", userId));
            while (cursor.moveToNext())
            {
                SDDepartmentEntity DepartmentEntity = new SDDepartmentEntity();
                int dpId = cursor.getColumnIndex("dpId");
                int dpName = cursor.getColumnIndex("dpName");
                DepartmentEntity.setDpId(Integer.parseInt(cursor.getString(dpId)));
                DepartmentEntity.setDpName(cursor.getString(dpName));
                DepartmentEntitys.add(DepartmentEntity);
            }
            SDLogUtil.syso("find===========" + Arrays.asList(DepartmentEntitys).toString());
        } catch (DbException e)
        {
            e.printStackTrace();
//            return DepartmentEntitys;
        } finally
        {
            closeCusor(cursor);
        }
        return DepartmentEntitys == null ? null : DepartmentEntitys;
    }

    public SDDepartmentEntity findDepartmentById(String dpId)
    {
        try
        {
            return db.findById(SDDepartmentEntity.class, dpId);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过UserId查询部门名字
     *
     * @param userId
     * @return
     */
    public SDDepartmentEntity findDepartmentByUseId(String userId)
    {
        DbModel dbModels = null;
        SDDepartmentEntity sdDepartmentEntity = null;
//            SDUserDeptRelEntity sdUserDeptRelEntity = db.findFirst(Selector.from(SDUserDeptRelEntity.class).where("USER_ID", "=", userId));
//            if (sdUserDeptRelEntity != null)
//                sdDepartmentEntity = db.findById(SDDepartmentEntity.class, sdUserDeptRelEntity.getDpId());

        SqlInfo sqlInfo = new SqlInfo("SELECT * FROM SYS_DEPARTMENT WHERE dpId IN " +
                "(SELECT dpId FROM USER_DEPT_REL WHERE USER_ID=?)", userId);
        try
        {
            dbModels = db.findDbModelFirst(sqlInfo); // 自定义sql查询
            if (dbModels != null)
            {
                sdDepartmentEntity = new SDDepartmentEntity();
                int dpID = Integer.valueOf(dbModels.getString("dpId")).intValue();
                sdDepartmentEntity.setDpId(dpID);
                sdDepartmentEntity.setDpName(dbModels.getString("dpName"));
                sdDepartmentEntity.setHeader(dbModels.getString("header"));
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }

        return sdDepartmentEntity;
    }


}
