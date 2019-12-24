package com.chaoxiang.entity.dao;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;

import com.chaoxiang.entity.chat.IMMessage;

import com.chaoxiang.entity.conversation.IMConversation;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DDX_IM_CONVERSATION".
*/
public class IMConversationDao extends AbstractDao<IMConversation, Long> {

    public static final String TABLENAME = "DDX_IM_CONVERSATION";

    /**
     * Properties of entity IMConversation.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Username = new Property(1, String.class, "username", false, "USERNAME");
        public final static Property MessageId = new Property(2, String.class, "messageId", false, "MESSAGE_ID");
        public final static Property Type = new Property(3, Integer.class, "type", false, "TYPE");
        public final static Property UnReadMsg = new Property(4, int.class, "unReadMsg", false, "UN_READ_MSG");
        public final static Property CreateTime = new Property(5, java.util.Date.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(6, java.util.Date.class, "updateTime", false, "UPDATE_TIME");
        public final static Property Title = new Property(7, String.class, "title", false, "TITLE");
        public final static Property Attachment = new Property(8, String.class, "attachment", false, "ATTACHMENT");
    };

    private DaoSession daoSession;


    public IMConversationDao(DaoConfig config) {
        super(config);
    }
    
    public IMConversationDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DDX_IM_CONVERSATION\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"USERNAME\" TEXT UNIQUE ," + // 1: username
                "\"MESSAGE_ID\" TEXT," + // 2: messageId
                "\"TYPE\" INTEGER," + // 3: type
                "\"UN_READ_MSG\" INTEGER NOT NULL ," + // 4: unReadMsg
                "\"CREATE_TIME\" INTEGER," + // 5: createTime
                "\"UPDATE_TIME\" INTEGER," + // 6: updateTime
                "\"TITLE\" TEXT," + // 7: title
                "\"ATTACHMENT\" TEXT);"); // 8: attachment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DDX_IM_CONVERSATION\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IMConversation entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String username = entity.getUsername();
        if (username != null) {
            stmt.bindString(2, username);
        }
 
        String messageId = entity.getMessageId();
        if (messageId != null) {
            stmt.bindString(3, messageId);
        }
 
        Integer type = entity.getType();
        if (type != null) {
            stmt.bindLong(4, type);
        }
        stmt.bindLong(5, entity.getUnReadMsg());
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(6, createTime.getTime());
        }
 
        java.util.Date updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(7, updateTime.getTime());
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(8, title);
        }
 
        String attachment = entity.getAttachment();
        if (attachment != null) {
            stmt.bindString(9, attachment);
        }
    }

    @Override
    protected void attachEntity(IMConversation entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public IMConversation readEntity(Cursor cursor, int offset) {
        IMConversation entity = new IMConversation( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // username
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // messageId
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3), // type
            cursor.getInt(offset + 4), // unReadMsg
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // createTime
            cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)), // updateTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // title
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // attachment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IMConversation entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUsername(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMessageId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
        entity.setUnReadMsg(cursor.getInt(offset + 4));
        entity.setCreateTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setUpdateTime(cursor.isNull(offset + 6) ? null : new java.util.Date(cursor.getLong(offset + 6)));
        entity.setTitle(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setAttachment(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(IMConversation entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(IMConversation entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getIMMessageDao().getAllColumns());
            builder.append(" FROM DDX_IM_CONVERSATION T");
            builder.append(" LEFT JOIN DDX_IM_MESSAGE T0 ON T.\"MESSAGE_ID\"=T0.\"ID\"");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected IMConversation loadCurrentDeep(Cursor cursor, boolean lock) {
        IMConversation entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        IMMessage iMMessage = loadCurrentOther(daoSession.getIMMessageDao(), cursor, offset);
        entity.setIMMessage(iMMessage);

        return entity;    
    }

    public IMConversation loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<IMConversation> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<IMConversation> list = new ArrayList<IMConversation>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<IMConversation> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<IMConversation> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }

    // KEEP METHODS - put your custom methods here
    /**
     * 搜索全部的会话记录
     * @return
     */
    public List<IMConversation> loadAllConversationList()
    {
        try
        {
            return queryBuilder().orderDesc(Properties.UpdateTime).build().list();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 通过username查询对应的会话
     *
     * @param userName
     * @return
     */
    public IMConversation loadByUserName(String userName)
    {
        IMConversation conversation = null;
        try
        {
            conversation = queryBuilder().where(Properties.Username.eq(userName)).unique();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return conversation;
    }

    /**
     * 通过username查询对应的会话
     *
     * @param userName
     * @return
     */
    public IMConversation loadByUserName(String userName,int type)
    {
        IMConversation conversation = null;
        try
        {
            conversation = queryBuilder().where(Properties.Username.eq(userName),Properties.Type.eq(userName)).unique();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return conversation;
    }

    public List<IMConversation> loadAll(int limit)
    {
        try
        {
            return queryBuilder().offset(0).limit(limit).orderDesc(Properties.UpdateTime).build().list();
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 更新
     */
    public void updateConversation(IMConversation imConversation)
    {
        update(imConversation);
    }
    // KEEP METHODS END
}