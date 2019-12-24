package com.superdata.im.utils;

import com.chaoxiang.base.utils.SDLogUtil;
import com.chaoxiang.entity.IMDaoManager;
import com.chaoxiang.entity.chat.IMMessage;
import com.chaoxiang.entity.dao.IMMessageDao;
import com.superdata.im.utils.Observable.CxUpdateMessageObservale;

import java.util.List;

/**
 * User: Selson
 * Date: 2016-09-13
 * FIXME  操作已读未读的工具类
 */
public class CxGreenDaoOperateUtils
{
    private static CxGreenDaoOperateUtils instance;

    private CxGreenDaoOperateUtils()
    {

    }

    public static synchronized CxGreenDaoOperateUtils getInstance()
    {
        if (instance == null)
        {
            instance = new CxGreenDaoOperateUtils();
        }
        return instance;
    }

    public void updateMessageBatch(List<IMMessage> listMessage) throws Exception
    {
//        imMessageDao.updateInTx(listMessage);
        IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insertOrReplaceInTx(listMessage);
    }

    public List<IMMessage> selectMessageBatch(List<String> messageIds)
    {
        List<IMMessage> list = null;
        list = IMDaoManager.getInstance().getDaoSession().getIMMessageDao().findUnReadMessageList(messageIds);
        return list;

    }

    public void updateMessageInfo(List<String> messageId)
    {
        try
        {
            List<IMMessage> list = selectMessageBatch(messageId);
            for (int i = 0; i < list.size(); i++)
            {
//                list.get(i).setIsReaded(true);
                list.get(i).setIsReadStatus(true);
            }
            //是否已读未读
            SDLogUtil.debug("IM_已读未读——更新消息状态的第一条数据：" + list.get(0).getIsReadStatus());
            updateMessageBatch(list);
            //
            CxUpdateMessageObservale.getInstance().sendUpdateMessageRead(0);
        } catch (Exception e)
        {
            SDLogUtil.error("Im_更新消息已读未读出错！");
        }

    }

} 