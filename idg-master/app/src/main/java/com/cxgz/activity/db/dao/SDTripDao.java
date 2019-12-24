package com.cxgz.activity.db.dao;

import android.content.Context;

import com.cxgz.activity.db.help.SDDbHelp;
import com.entity.SDTripEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.List;


/**
 * @author xiaoli
 */
public class SDTripDao extends BaseDao
{
    private DbUtils mDbUtils;
    private Context context;

    public SDTripDao(Context context)
    {
        super(context);
        mDbUtils = SDDbHelp.createDbUtils(context);
        this.context = context;
    }

    public void saveAll(List<SDTripEntity> tripEntityList) throws DbException
    {
        mDbUtils.saveOrUpdateAll(tripEntityList);
    }

    public List<SDTripEntity> findAll(String userId) throws DbException
    {
        Selector s = Selector.from(SDTripEntity.class);
        s.where("USER_ID", "=", userId);
        return mDbUtils.findAll(s);
    }

    public void save(SDTripEntity tripEntity) throws DbException
    {
        mDbUtils.saveOrUpdate(tripEntity);
    }

    public List<SDTripEntity> findByDate(String startDate, String endDate) throws DbException
    {
        Selector s = Selector.from(SDTripEntity.class);
        s.where("START", ">=", startDate);
        s.and("START", "<=", endDate);
        return mDbUtils.findAll(s);
    }

    public void deleteAll(List<SDTripEntity> tripEntityList) throws DbException
    {
        mDbUtils.deleteAll(tripEntityList);
    }

    public void deleteById(long cid) throws DbException
    {
        mDbUtils.deleteById(SDTripEntity.class, cid);
    }
}
