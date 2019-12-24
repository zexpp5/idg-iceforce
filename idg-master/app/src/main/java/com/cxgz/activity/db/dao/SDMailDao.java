package com.cxgz.activity.db.dao;

import android.content.Context;

import com.cxgz.activity.db.help.SDDbHelp;
import com.entity.MailConfig;
import com.entity.MailMessageEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.util.List;

/**
 * @author xiaoli
 */
public class SDMailDao extends BaseDao
{
    private DbUtils mDbUtils;
    private String userId;
    private Context context;

    public SDMailDao(Context context)
    {
        super(context);
        mDbUtils = SDDbHelp.createDbUtils(context);
        this.context = context;
        userId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
    }

    public void saveMail(MailMessageEntity entity) throws DbException
    {
        mDbUtils.saveOrUpdate(entity);
    }

    public MailMessageEntity findByUid(String uid) throws DbException
    {
        return mDbUtils.findById(MailMessageEntity.class, uid);
    }

    public List<MailMessageEntity> findAll() throws DbException
    {
        return mDbUtils.findAll(MailMessageEntity.class);
    }

    public void saveConfig(MailConfig config) throws DbException
    {
        mDbUtils.saveOrUpdate(config);
    }

    public MailConfig findCurrMailConfig() throws DbException
    {
        MailConfig config = db.findFirst(Selector.from(MailConfig.class).where(WhereBuilder.b("userId", "=", userId).and("selected", "=", true)));
        return config;
    }

    public void deleteAll() throws DbException
    {
        mDbUtils.deleteAll(MailMessageEntity.class);
    }
}
