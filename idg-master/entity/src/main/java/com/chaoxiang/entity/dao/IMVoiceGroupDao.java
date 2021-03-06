package com.chaoxiang.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.chaoxiang.entity.chat.IMVoiceGroup;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DDX_IM_VOICE_GROUP".
*/
public class IMVoiceGroupDao extends AbstractDao<IMVoiceGroup, Long> {

    public static final String TABLENAME = "DDX_IM_VOICE_GROUP";

    /**
     * Properties of entity IMVoiceGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property GroupId = new Property(1, String.class, "groupId", false, "GROUP_ID");
        public final static Property CreateTime = new Property(2, Long.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(3, Long.class, "updateTime", false, "UPDATE_TIME");
        public final static Property JoinTime = new Property(4, Long.class, "joinTime", false, "JOIN_TIME");
        public final static Property Time = new Property(5, Long.class, "time", false, "TIME");
        public final static Property IsFinish = new Property(6, Boolean.class, "isFinish", false, "IS_FINISH");
        public final static Property Attachment = new Property(7, String.class, "attachment", false, "ATTACHMENT");
    };


    public IMVoiceGroupDao(DaoConfig config) {
        super(config);
    }
    
    public IMVoiceGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DDX_IM_VOICE_GROUP\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"GROUP_ID\" TEXT," + // 1: groupId
                "\"CREATE_TIME\" INTEGER," + // 2: createTime
                "\"UPDATE_TIME\" INTEGER," + // 3: updateTime
                "\"JOIN_TIME\" INTEGER," + // 4: joinTime
                "\"TIME\" INTEGER," + // 5: time
                "\"IS_FINISH\" INTEGER," + // 6: isFinish
                "\"ATTACHMENT\" TEXT);"); // 7: attachment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DDX_IM_VOICE_GROUP\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IMVoiceGroup entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(2, groupId);
        }
 
        Long createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(3, createTime);
        }
 
        Long updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindLong(4, updateTime);
        }
 
        Long joinTime = entity.getJoinTime();
        if (joinTime != null) {
            stmt.bindLong(5, joinTime);
        }
 
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(6, time);
        }
 
        Boolean isFinish = entity.getIsFinish();
        if (isFinish != null) {
            stmt.bindLong(7, isFinish ? 1L: 0L);
        }
 
        String attachment = entity.getAttachment();
        if (attachment != null) {
            stmt.bindString(8, attachment);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public IMVoiceGroup readEntity(Cursor cursor, int offset) {
        IMVoiceGroup entity = new IMVoiceGroup( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // groupId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // createTime
            cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3), // updateTime
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // joinTime
            cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5), // time
            cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0, // isFinish
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7) // attachment
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IMVoiceGroup entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setGroupId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCreateTime(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setUpdateTime(cursor.isNull(offset + 3) ? null : cursor.getLong(offset + 3));
        entity.setJoinTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setTime(cursor.isNull(offset + 5) ? null : cursor.getLong(offset + 5));
        entity.setIsFinish(cursor.isNull(offset + 6) ? null : cursor.getShort(offset + 6) != 0);
        entity.setAttachment(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(IMVoiceGroup entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(IMVoiceGroup entity) {
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

    // KEEP METHODS - put your custom methods here
    /**
     * -根据群组ID查询
     * @param
     * @return
     */
    public IMVoiceGroup loadVoiceGroupFromGroupId(String groupId)
    {
        IMVoiceGroup imVoiceGroup = queryBuilder().where(Properties.GroupId.eq(groupId)).unique();
        return imVoiceGroup != null ? imVoiceGroup : null;
    }
    // KEEP METHODS END
}
