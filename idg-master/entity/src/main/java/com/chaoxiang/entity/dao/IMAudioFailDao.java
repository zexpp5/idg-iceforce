package com.chaoxiang.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.chaoxiang.entity.chat.IMAudioFail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DDX_IMAudioFail".
*/
public class IMAudioFailDao extends AbstractDao<IMAudioFail, Long> {

    public static final String TABLENAME = "DDX_IMAudioFail";

    /**
     * Properties of entity IMAudioFail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "ID");
        public final static Property Attachment = new Property(1, String.class, "attachment", false, "ATTACHMENT");
        public final static Property MessageId = new Property(2, String.class, "messageId", false, "MESSAGE_ID");
    };


    public IMAudioFailDao(DaoConfig config) {
        super(config);
    }
    
    public IMAudioFailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DDX_IMAudioFail\" (" + //
                "\"ID\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"ATTACHMENT\" TEXT," + // 1: attachment
                "\"MESSAGE_ID\" TEXT UNIQUE );"); // 2: messageId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DDX_IMAudioFail\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IMAudioFail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String attachment = entity.getAttachment();
        if (attachment != null) {
            stmt.bindString(2, attachment);
        }
 
        String messageId = entity.getMessageId();
        if (messageId != null) {
            stmt.bindString(3, messageId);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public IMAudioFail readEntity(Cursor cursor, int offset) {
        IMAudioFail entity = new IMAudioFail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // attachment
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // messageId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IMAudioFail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setAttachment(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMessageId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(IMAudioFail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(IMAudioFail entity) {
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
    
}
