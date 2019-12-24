package com.cxgz.activity.db.dao;

import android.app.Application;

import com.base.BaseApplication;
import com.chaoxiang.base.config.Constants;
import com.chaoxiang.base.utils.StringUtils;
import com.cxgz.activity.cxim.utils.UnReadUtils;
import com.cxgz.activity.db.entity.SDUnreadEntity;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.util.ArrayList;
import java.util.List;

import static com.base.BaseApplication.getmDbUtils;

/**
 * Created by selson on 2017/9/4.
 */

public class SDUnreadDao
{
    public static String CODE = "menuId";

    public static String SHOW_MSG = "msg";

    public static String BTYPE = "btype";

    public static String TYPE = "type";


    private SDUnreadDao()
    {

    }

    public static synchronized SDUnreadDao getInstance()
    {
        return SDUnreadDaoHelper.sdUnreadDao;
    }

    private static class SDUnreadDaoHelper
    {
        private static final SDUnreadDao sdUnreadDao = new SDUnreadDao();
    }

    /**
     * 保存静态数据字典
     *
     * @param application
     */
    public static void saveUnRead(Application application)
    {
        try
        {
            // 创建表
            getmDbUtils().createTableIfNotExist(SDUnreadEntity.class);
        } catch (DbException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //保存未读信息
    public void save(SDUnreadEntity sdUnreadEntity)
    {
        try
        {
            getmDbUtils().saveOrUpdate(sdUnreadEntity);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    //保存未读信息
    public void update(SDUnreadEntity sdUnreadEntity)
    {
        try
        {
            getmDbUtils().update(sdUnreadEntity, BTYPE, "unreadCount", "isRead", "msg");
        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 查询单条记录-是否存在
     */
    public SDUnreadEntity findByMenuId(int btype)
    {
        SDUnreadEntity sdUnreadEntity = null;
        try
        {
            sdUnreadEntity = BaseApplication.getmDbUtils().findById(SDUnreadEntity.class, btype);
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return sdUnreadEntity;
    }

    /**
     * 根据menuCode码获取菜单的
     * 未读条数
     */
    public int findUnreadCount(int btype)
    {
        SDUnreadEntity sdUnreadEntity = null;
        int unRead = 0;
        try
        {
            sdUnreadEntity = BaseApplication.getmDbUtils().findById(SDUnreadEntity.class, btype);
            if (StringUtils.notEmpty(sdUnreadEntity))
            {
                unRead = sdUnreadEntity.getUnreadCount();
            }
        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return unRead;
    }

    /**
     * 多个查询
     * 根据IMAccount查询通讯录
     */
    public synchronized int findUnReadByMenuId(List<Integer> list)
    {
        int totle = 0;
        List<SDUnreadEntity> userEntities = new ArrayList<>();
        if (list == null)
        {
            return userEntities.size();
        }
        Integer[] conditionArray = list.toArray(new Integer[list.size()]);
        try
        {
            userEntities = BaseApplication.getmDbUtils().findAll(
                    Selector.from(SDUnreadEntity.class).where(BTYPE, "in",
                            conditionArray));
            if (userEntities != null)
                if (userEntities.size() > 0)
                {
                    for (int i = 0; i < userEntities.size(); i++)
                    {
                        totle += userEntities.get(i).getUnreadCount();
                    }
                }

        } catch (DbException e)
        {
            e.printStackTrace();
        }
        return totle;
    }

    /**
     * 根据btype,修改未读条数
     */
    public void setUnreadCount(int btype)
    {
        SDUnreadEntity sdUnreadEntity = null;
        try
        {
            sdUnreadEntity = BaseApplication.getmDbUtils().findById(SDUnreadEntity.class, btype);
            if (sdUnreadEntity != null)
            {
                if (StringUtils.notEmpty(sdUnreadEntity.getUnreadCount()))
                    if (sdUnreadEntity.getUnreadCount() > 0)
                    {
                        sdUnreadEntity.setUnreadCount(0);
                        sdUnreadEntity.setRead(true);
                        BaseApplication.getmDbUtils().update(sdUnreadEntity, "unreadCount", "isRead");
                    }
            }

        } catch (DbException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 设置所有未读的都为0
     */
    public void setAllPSUnread0()
    {
        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CLSP2);
        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_CK);
        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_BSPS);
        UnReadUtils.getInstance().setIsRead(Constants.IM_PUSH_XIAO2);
    }

}
