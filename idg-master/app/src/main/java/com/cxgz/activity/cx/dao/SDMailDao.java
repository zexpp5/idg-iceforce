package com.cxgz.activity.cx.dao;

import android.content.Context;

import com.cxgz.activity.cx.bean.dao.MailConfig;
import com.cxgz.activity.db.help.SDDbHelp;
import com.cxgz.activity.db.dao.BaseDao;
import com.cxgz.activity.db.dao.BaseUserName;
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

    public void saveMail(BaseUserName.MailMessageEntity entity) throws DbException
    {
        mDbUtils.saveOrUpdate(entity);
    }

    public BaseUserName.MailMessageEntity findByUid(String uid) throws DbException
    {
        return mDbUtils.findById(BaseUserName.MailMessageEntity.class, uid);
    }

    public List<BaseUserName.MailMessageEntity> findAll() throws DbException
    {
        return mDbUtils.findAll(BaseUserName.MailMessageEntity.class);
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
        mDbUtils.deleteAll(BaseUserName.MailMessageEntity.class);
    }
}
