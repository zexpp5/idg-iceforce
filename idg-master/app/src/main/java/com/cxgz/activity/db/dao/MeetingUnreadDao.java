package com.cxgz.activity.db.dao;

import android.app.Application;

import com.base.BaseApplication;
import com.cxgz.activity.db.entity.MeetingUnreadEntity;
import com.lidroid.xutils.exception.DbException;

import java.util.List;

import static com.base.BaseApplication.getmDbUtils;

/**
 * Created by selson on 2018/5/4.
 */

public class MeetingUnreadDao
{
    public static String EID = "eid";

    public static synchronized MeetingUnreadDao getInstance()
    {
        return MeetingUnreadHelper.meetingUnreadDao;
    }

    private static class MeetingUnreadHelper
    {
        private static final MeetingUnreadDao meetingUnreadDao = new MeetingUnreadDao();
    }

    /**
     * 保存语音会议字典
     *
     * @param application
     */
    public void saveMeetingUnRead(Application application)
    {
        try
        {
            // 创建表
            getmDbUtils().createTableIfNotExist(MeetingUnreadEntity.class);
        } catch (DbException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //保存会议未读信息
    public void saveMeeting(MeetingUnreadEntity meetingUnreadEntity)
    {
        try
        {
            getmDbUtils().saveOrUpdate(meetingUnreadEntity);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    //保存会议未读信息
    public void saveMeetingAll(List<MeetingUnreadEntity> meetingUnreadEntity)
    {
        try
        {
            getmDbUtils().saveOrUpdateAll(meetingUnreadEntity);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    //更新会议未读信息
    public void updateMeeting(MeetingUnreadEntity sdUnreadEntity)
    {
        try
        {
            getmDbUtils().update(sdUnreadEntity, "unreadCount", "isRead");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 查询单条记录-是否存在
     */
    public MeetingUnreadEntity findByEid(int eid)
    {
        MeetingUnreadEntity sdUnreadEntity = null;
        try
        {
            sdUnreadEntity = BaseApplication.getmDbUtils().findById(MeetingUnreadEntity.class, eid);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return sdUnreadEntity;
    }
}
