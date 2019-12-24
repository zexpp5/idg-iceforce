package com.chaoxiang.imsdk.chat;

import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMVoiceGroup;
import com.chaoxiang.entity.dao.IMVoiceGroupDao;

import java.util.List;

/**
 * User: Selson
 * Date: 2016-05-20
 * FIXME 语音会议
 */
public class CXVoiceGroupManager
{
    private static CXVoiceGroupManager instance;

    private CXVoiceGroupManager()
    {

    }

    public static synchronized CXVoiceGroupManager getInstance()
    {
        if (instance == null )
        {
            instance = new CXVoiceGroupManager();
        }
        return instance;
    }

    public static void clear()
    {
    }

    /**
     * 插入数据、或更新数据
     */
    public void inSertOrReplace(IMVoiceGroup imVoiceGroup)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().insertOrReplace(imVoiceGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 插入数据
     */
    public void inSert(IMVoiceGroup imVoiceGroup)
    {
        try
        {
            IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().insert(imVoiceGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否有未读的会议记录
     *
     * @return boolean
     */
    public boolean findVoiceGroupList()
    {
        boolean hasDate = false;
        List<IMVoiceGroup> iMVoiceGroup = null;
        try
        {
            iMVoiceGroup = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().queryBuilder().where(IMVoiceGroupDao.Properties.Attachment.eq("1")).build().list();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        if (iMVoiceGroup.size() > 0)
        {
            hasDate = true;
        }

        return hasDate;
    }

    /**
     * 获取所有未读的会议记录
     *
     * @return boolean
     */
    public List<IMVoiceGroup> findVoiceGroupUnReadList()
    {
        List<IMVoiceGroup> iMVoiceGroup = null;
        try
        {
            iMVoiceGroup = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().queryBuilder().where(IMVoiceGroupDao.Properties.Attachment.eq("1")).build().list();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return iMVoiceGroup;
    }

    /**
     * 更新消息是否已读
     */
    public void updateVoiceGroup(String groupId, boolean isChange)
    {
        IMVoiceGroup iMVoiceGroup = null;
        try
        {
            iMVoiceGroup = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().queryBuilder().where(IMVoiceGroupDao.Properties.GroupId.eq(groupId)).unique();
            if (isChange)
                iMVoiceGroup.setAttachment("1");
            else
                iMVoiceGroup.setAttachment("0");
            IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().update(iMVoiceGroup);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 返回groupID查询到的语音群组信息
     */
    public IMVoiceGroup find(String groupId)
    {
        IMVoiceGroup iMVoiceGroup = null;
        try
        {
            iMVoiceGroup = IMDaoManager.getInstance().getDaoSession().getIMVoiceGroupDao().queryBuilder().where(IMVoiceGroupDao.Properties.GroupId.eq(groupId)).unique();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return iMVoiceGroup;
    }


}