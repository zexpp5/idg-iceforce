package com.chaoxiang.entity.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.QueryBuilder;

import com.chaoxiang.entity.group.IMGroup;

import java.util.List;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table "DDX_IM_GROUP".
*/
public class IMGroupDao extends AbstractDao<IMGroup, String> {

    public static final String TABLENAME = "DDX_IM_GROUP";

    /**
     * Properties of entity IMGroup.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property GroupId = new Property(0, String.class, "groupId", true, "GROUP_ID");
        public final static Property GroupName = new Property(1, String.class, "groupName", false, "GROUP_NAME");
        public final static Property CompanyId = new Property(2, String.class, "companyId", false, "COMPANY_ID");
        public final static Property Owner = new Property(3, String.class, "owner", false, "OWNER");
        public final static Property JoinTime = new Property(4, Long.class, "joinTime", false, "JOIN_TIME");
        public final static Property CreateTime = new Property(5, String.class, "createTime", false, "CREATE_TIME");
        public final static Property UpdateTime = new Property(6, String.class, "updateTime", false, "UPDATE_TIME");
        public final static Property MemberStringList = new Property(7, String.class, "memberStringList", false, "MEMBER_STRING_LIST");
        public final static Property GroupType = new Property(8, Integer.class, "groupType", false, "GROUP_TYPE");
        public final static Property CreateTimeMillisecond = new Property(9, Long.class, "createTimeMillisecond", false, "CREATE_TIME_MILLISECOND");
        public final static Property UpdateTimeMillisecond = new Property(10, Long.class, "updateTimeMillisecond", false, "UPDATE_TIME_MILLISECOND");
        public final static Property Attachment = new Property(11, String.class, "attachment", false, "ATTACHMENT");
    };


    public IMGroupDao(DaoConfig config) {
        super(config);
    }

    public IMGroupDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DDX_IM_GROUP\" (" + //
                "\"GROUP_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: groupId
                "\"GROUP_NAME\" TEXT," + // 1: groupName
                "\"COMPANY_ID\" TEXT," + // 2: companyId
                "\"OWNER\" TEXT," + // 3: owner
                "\"JOIN_TIME\" INTEGER," + // 4: joinTime
                "\"CREATE_TIME\" TEXT," + // 5: createTime
                "\"UPDATE_TIME\" TEXT," + // 6: updateTime
                "\"MEMBER_STRING_LIST\" TEXT," + // 7: memberStringList
                "\"GROUP_TYPE\" INTEGER," + // 8: groupType
                "\"CREATE_TIME_MILLISECOND\" INTEGER," + // 9: createTimeMillisecond
                "\"UPDATE_TIME_MILLISECOND\" INTEGER," + // 10: updateTimeMillisecond
                "\"ATTACHMENT\" TEXT);"); // 11: attachment
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DDX_IM_GROUP\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, IMGroup entity) {
        stmt.clearBindings();

        String groupId = entity.getGroupId();
        if (groupId != null) {
            stmt.bindString(1, groupId);
        }

        String groupName = entity.getGroupName();
        if (groupName != null) {
            stmt.bindString(2, groupName);
        }

        String companyId = entity.getCompanyId();
        if (companyId != null) {
            stmt.bindString(3, companyId);
        }

        String owner = entity.getOwner();
        if (owner != null) {
            stmt.bindString(4, owner);
        }

        Long joinTime = entity.getJoinTime();
        if (joinTime != null) {
            stmt.bindLong(5, joinTime);
        }

        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(6, createTime);
        }

        String updateTime = entity.getUpdateTime();
        if (updateTime != null) {
            stmt.bindString(7, updateTime);
        }

        String memberStringList = entity.getMemberStringList();
        if (memberStringList != null) {
            stmt.bindString(8, memberStringList);
        }

        Integer groupType = entity.getGroupType();
        if (groupType != null) {
            stmt.bindLong(9, groupType);
        }

        Long createTimeMillisecond = entity.getCreateTimeMillisecond();
        if (createTimeMillisecond != null) {
            stmt.bindLong(10, createTimeMillisecond);
        }

        Long updateTimeMillisecond = entity.getUpdateTimeMillisecond();
        if (updateTimeMillisecond != null) {
            stmt.bindLong(11, updateTimeMillisecond);
        }

        String attachment = entity.getAttachment();
        if (attachment != null) {
            stmt.bindString(12, attachment);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }

    /** @inheritdoc */
    @Override
    public IMGroup readEntity(Cursor cursor, int offset) {
        IMGroup entity = new IMGroup( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // groupId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // groupName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // companyId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // owner
            cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4), // joinTime
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // createTime
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // updateTime
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // memberStringList
            cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8), // groupType
            cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9), // createTimeMillisecond
            cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10), // updateTimeMillisecond
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // attachment
        );
        return entity;
    }

    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, IMGroup entity, int offset) {
        entity.setGroupId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setGroupName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCompanyId(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOwner(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setJoinTime(cursor.isNull(offset + 4) ? null : cursor.getLong(offset + 4));
        entity.setCreateTime(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUpdateTime(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setMemberStringList(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setGroupType(cursor.isNull(offset + 8) ? null : cursor.getInt(offset + 8));
        entity.setCreateTimeMillisecond(cursor.isNull(offset + 9) ? null : cursor.getLong(offset + 9));
        entity.setUpdateTimeMillisecond(cursor.isNull(offset + 10) ? null : cursor.getLong(offset + 10));
        entity.setAttachment(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }

    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(IMGroup entity, long rowId) {
        return entity.getGroupId();
    }

    /** @inheritdoc */
    @Override
    public String getKey(IMGroup entity) {
        if(entity != null) {
            return entity.getGroupId();
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
     * 模糊搜素对应信息的
     * -语音会议
     *
     * @param nameString
     * @return
     */
    public List<IMGroup> loadSearchVoiceGroupNameList(String nameString)
    {
        QueryBuilder qb = queryBuilder();
        qb.where(qb.and(Properties.GroupName.like("%" + nameString + "%"), Properties.GroupType.eq(2)));
//        qb.where(Properties.Type.eq(3));
//        return qb.limit(limit).orderDesc(Properties.Id).build().list();
        return qb.build().list();
    }

    /**
     * -根据群组ID查询
     * @param
     * @return
     */
    public IMGroup loadGroupFromGroupId(String groupId)
    {
//        QueryBuilder qb = queryBuilder();
//        qb.where(qb.and(Properties.GroupId.eq(idString), Properties.GroupType.eq(1)));
//        IMGroup imGroup = queryBuilder().where(Properties.GroupId.eq(groupId), Properties.GroupType.eq(1)).unique();
        IMGroup imGroup = queryBuilder().where(Properties.GroupId.eq(groupId)).unique();
        return imGroup != null ? imGroup : null;

    }
    // KEEP METHODS END
}