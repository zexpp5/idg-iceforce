package com.chaoxiang.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMAddFriend;
import com.chaoxiang.entity.chat.IMWorkCircle;

import java.util.List;

import static android.R.attr.type;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "DDX_IM_ADD_FRIEND".
 */
public class IMAddFriendDao extends AbstractDao<IMAddFriend, Long>
{

    public static final String TABLENAME = "DDX_IM_ADD_FRIEND";

    /**
     * Properties of entity IMAddFriend.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties
    {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property ApplicantMsgId = new Property(1, String.class, "applicantMsgId", false, "APPLICANT_MSG_ID");
        public final static Property ApplicantTime = new Property(2, String.class, "applicantTime", false, "APPLICANT_TIME");
        public final static Property ApplicantId = new Property(3, String.class, "applicantId", false, "APPLICANT_ID");
        public final static Property ApplicantName = new Property(4, String.class, "applicantName", false, "APPLICANT_NAME");
        public final static Property Attached = new Property(5, String.class, "attached", false, "ATTACHED");
        public final static Property WorkStatus = new Property(6, String.class, "workStatus", false, "WORK_STATUS");
        public final static Property OperateTime = new Property(7, String.class, "operateTime", false, "OPERATE_TIME");
        public final static Property Icon = new Property(8, String.class, "icon", false, "ICON");
        public final static Property ImAccount = new Property(9, String.class, "imAccount", false, "IM_ACCOUNT");
        public final static Property Attachment = new Property(10, String.class, "attachment", false, "ATTACHMENT");
    }

    ;


    public IMAddFriendDao(DaoConfig config)
    {
        super(config);
    }

    public IMAddFriendDao(DaoConfig config, DaoSession daoSession)
    {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists)
    {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"DDX_IM_ADD_FRIEND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"APPLICANT_MSG_ID\" TEXT," + // 1: applicantMsgId
                "\"APPLICANT_TIME\" TEXT," + // 2: applicantTime
                "\"APPLICANT_ID\" TEXT," + // 3: applicantId
                "\"APPLICANT_NAME\" TEXT," + // 4: applicantName
                "\"ATTACHED\" TEXT," + // 5: attached
                "\"WORK_STATUS\" TEXT," + // 6: workStatus
                "\"OPERATE_TIME\" TEXT," + // 7: operateTime
                "\"ICON\" TEXT," + // 8: icon
                "\"IM_ACCOUNT\" TEXT," + // 9: imAccount
                "\"ATTACHMENT\" TEXT);"); // 10: attachment
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists)
    {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DDX_IM_ADD_FRIEND\"";
        db.execSQL(sql);
    }

    /**
     * @inheritdoc
     */
    @Override
    protected void bindValues(SQLiteStatement stmt, IMAddFriend entity)
    {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null)
        {
            stmt.bindLong(1, id);
        }

        String applicantMsgId = entity.getApplicantMsgId();
        if (applicantMsgId != null)
        {
            stmt.bindString(2, applicantMsgId);
        }

        String applicantTime = entity.getApplicantTime();
        if (applicantTime != null)
        {
            stmt.bindString(3, applicantTime);
        }

        String applicantId = entity.getApplicantId();
        if (applicantId != null)
        {
            stmt.bindString(4, applicantId);
        }

        String applicantName = entity.getApplicantName();
        if (applicantName != null)
        {
            stmt.bindString(5, applicantName);
        }

        String attached = entity.getAttached();
        if (attached != null)
        {
            stmt.bindString(6, attached);
        }

        String workStatus = entity.getWorkStatus();
        if (workStatus != null)
        {
            stmt.bindString(7, workStatus);
        }

        String operateTime = entity.getOperateTime();
        if (operateTime != null)
        {
            stmt.bindString(8, operateTime);
        }

        String icon = entity.getIcon();
        if (icon != null)
        {
            stmt.bindString(9, icon);
        }

        String imAccount = entity.getImAccount();
        if (imAccount != null)
        {
            stmt.bindString(10, imAccount);
        }

        String attachment = entity.getAttachment();
        if (attachment != null)
        {
            stmt.bindString(11, attachment);
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long readKey(Cursor cursor, int offset)
    {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    /**
     * @inheritdoc
     */
    @Override
    public IMAddFriend readEntity(Cursor cursor, int offset)
    {
        IMAddFriend entity = new IMAddFriend( //
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // applicantMsgId
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // applicantTime
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // applicantId
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // applicantName
                cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // attached
                cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // workStatus
                cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // operateTime
                cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // icon
                cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // imAccount
                cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10) // attachment
        );
        return entity;
    }

    /**
     * @inheritdoc
     */
    @Override
    public void readEntity(Cursor cursor, IMAddFriend entity, int offset)
    {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setApplicantMsgId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setApplicantTime(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setApplicantId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setApplicantName(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setAttached(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setWorkStatus(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setOperateTime(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setIcon(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImAccount(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setAttachment(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
    }

    /**
     * @inheritdoc
     */
    @Override
    protected Long updateKeyAfterInsert(IMAddFriend entity, long rowId)
    {
        entity.setId(rowId);
        return rowId;
    }

    /**
     * @inheritdoc
     */
    @Override
    public Long getKey(IMAddFriend entity)
    {
        if (entity != null)
        {
            return entity.getId();
        } else
        {
            return null;
        }
    }

    /**
     * @inheritdoc
     */
    @Override
    protected boolean isEntityUpdateable()
    {
        return true;
    }

    // KEEP METHODS - put your custom methods here
    public void search()
    {
        // Query 类代表了一个可以被重复执行的查询
        Query query = IMDaoManager.getInstance().getDaoSession().getIMAddFriendDao().queryBuilder().build();
    }

    /**
     * 返回一个list数组 ，所有的添加好友的申请！
     *
     * @return
     */
    public List<IMAddFriend> loadAll()
    {
        return queryBuilder().orderAsc(Properties.Id).build().list();
    }

    /**
     * 返回一个list数组 ，所有的添加好友的申请！
     *
     * @return
     */
    public List<IMAddFriend> loadFriendList(String searchString)
    {
//        return queryBuilder().orderAsc(Properties.Id).build().list();
        QueryBuilder qb = queryBuilder();
        qb.where(Properties.ApplicantName.like("%" + searchString + "%"));
        return qb.orderAsc(Properties.Id).build().list();
    }

    /**
     * 查询对应申请人的ID信息
     *
     * @param applicantId
     * @return
     */
    public IMAddFriend loadAddFriendFromId(String applicantId)
    {
        return queryBuilder().where(Properties.ApplicantId.eq(applicantId)).unique();
    }

    public void updateMsgByMsgId(IMAddFriend iMAddFriend)
    {
        try
        {
            update(iMAddFriend);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 搜索是否有未接收的好友-目前是未更改为已读的信息
     *
     * @return
     */
    public boolean findFriendStatusList()
    {
        boolean isHas = false;
        List<IMAddFriend> list = queryBuilder().where(Properties.WorkStatus.eq("0")).build().list();
        if (list != null && list.size() > 0)
            isHas = true;
        else
            isHas = false;
        return isHas;
    }

    /**
     * 所有设置为已读
     */
    public void upDateFriendStatusRead()
    {
        List<IMAddFriend> list = queryBuilder()
                .where(Properties.WorkStatus.eq("0"))
                .orderAsc(Properties.Id)
                .build()
                .list();

        if (list.size() > 0)
        {
            for (int i = 0; i < list.size(); i++)
            {
                list.get(i).setWorkStatus("1");
            }
            upDateFriendStatusList(list);
        }
    }

    /**
     * 修改列表
     *
     * @param list
     */
    public void upDateFriendStatusList(List<IMAddFriend> list)
    {
        insertOrReplaceInTx(list);
    }


    public void deleteInfoById(long id)
    {
        try
        {
            deleteByKey(id);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // KEEP METHODS END

}
