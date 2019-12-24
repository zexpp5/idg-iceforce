package com.cxgz.activity.cxim.utils;

import com.cxgz.activity.db.dao.SDUnreadDao;
import com.chaoxiang.base.config.Constants;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import yunjing.processor.eventbus.UnReadMessage;

/**
 * Created by selson on 2017/11/6.
 */

public class UnReadUtils
{

    private UnReadUtils()
    {

    }

    public static synchronized UnReadUtils getInstance()
    {
        return UnReadUtilsHelper.unReadUtils;
    }

    private static class UnReadUtilsHelper
    {
        private static final UnReadUtils unReadUtils = new UnReadUtils();
    }

    public void findUnReadCompanyCount()
    {
        //公司通知
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_GT) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_GT);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_GT, unReadTotal));
        }
    }

    //请假审批的
    public void findUnReadLeaveCKCount()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CK);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_CK, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_CK, 0));
        }
    }

    //请假
    public void findUnReadLeave()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_QJ);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_QJ, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_QJ, 0));
        }
    }

    public void findUnReadXJ()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_XIAO, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_XIAO, 0));
        }
    }

    public void findUnReadXJPS()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO2) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_XIAO2);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_XIAO2, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_XIAO2, 0));
        }
    }

    //报销审批
    public void findUnReadBSPS()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_BSPS) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_BSPS);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_BSPS, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_BSPS, 0));
        }
    }

    //我的出差
    public void findUnReadCC()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_CLSP, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_CLSP, 0));
        }
    }

    //我的出差
    public void findUnReadCCPS()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP2) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_CLSP2);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_CLSP2, unReadTotal));
        }
        else
        {
            EventBus.getDefault().post(new UnReadMessage(false, Constants.IM_PUSH_CLSP2, 0));
        }
    }

    //消息
    public void findUnReadNews()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PUSH_HOLIDAY);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_PUSH_HOLIDAY, unReadTotal));
        }
    }

    public void findUnReadProgress()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_PROGRESS);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_PROGRESS, unReadTotal));
        }
    }

    //日常会议
    public void findUnReadDayMeeting()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_DM) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_DM);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_DM, unReadTotal));
        }
    }

    //语音会议的
    public void findUnReadVoiceMeeting()
    {
        if (SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_VM) > 0)
        {
            int unReadTotal = SDUnreadDao.getInstance().findUnreadCount(Constants.IM_PUSH_VM);
            EventBus.getDefault().post(new UnReadMessage(true, Constants.IM_PUSH_VM, unReadTotal));
        }
    }

    public int findUnReadApproveCount()
    {
        List<Integer> oneList = new ArrayList<Integer>();
        oneList.add(Constants.IM_PUSH_CK);
        oneList.add(Constants.IM_PUSH_CLSP2);
//        oneList.add(Constants.IM_PUSH_XIAO2);
        oneList.add(Constants.IM_PUSH_BSPS);

        int total1 = SDUnreadDao.getInstance().findUnReadByMenuId(oneList);

        if (total1 > 0)
        {
            return total1;
        } else
        {
            return 0;
        }
    }

    public int findUnReadBusinessCount()
    {
        List<Integer> oneList = new ArrayList<Integer>();
        oneList.add(Constants.IM_PUSH_QJ);
        oneList.add(Constants.IM_PUSH_CK);

        oneList.add(Constants.IM_PUSH_CLSP);
        oneList.add(Constants.IM_PUSH_CLSP2);

        oneList.add(Constants.IM_PUSH_XIAO);
        oneList.add(Constants.IM_PUSH_XIAO2);

        oneList.add(Constants.IM_PUSH_BSPS);

        oneList.add(Constants.IM_PUSH_DM);
        oneList.add(Constants.IM_PUSH_VM);

        oneList.add(Constants.IM_PUSH_PUSH_HOLIDAY);
        oneList.add(Constants.IM_PUSH_PROGRESS);

        int total1 = SDUnreadDao.getInstance().findUnReadByMenuId(oneList);

        if (total1 > 0)
        {
            return total1;
        } else
        {
            return 0;
        }
    }

    public int findUnReadSystemCount()
    {
        List<Integer> oneList = new ArrayList<Integer>();
        oneList.add(Constants.IM_PUSH_QJ);
        oneList.add(Constants.IM_PUSH_CK);
        oneList.add(Constants.IM_PUSH_XIAO);
        oneList.add(Constants.IM_PUSH_XIAO2);
        oneList.add(Constants.IM_PUSH_CLSP);
        oneList.add(Constants.IM_PUSH_CLSP2);

        int total1 = SDUnreadDao.getInstance().findUnReadByMenuId(oneList);

        if (total1 > 0)
        {
            return total1;
        } else
        {
            return 0;
        }
    }

    /**
     * 设置已读-清零
     */
    public void setIsRead(int menuId)
    {
        SDUnreadDao.getInstance().setUnreadCount(menuId);
    }

}
