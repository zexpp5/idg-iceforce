package com.cxgz.activity.db.help;

import android.app.Notification;
import android.content.Context;

import com.cxgz.activity.db.dao.PushUnreadDao;
import com.cxgz.activity.db.dao.SDUnreadDao;
import com.cxgz.activity.db.entity.SDUnreadEntity;
import com.cxgz.activity.db.entity.UnreadEntity;
import com.cxgz.activity.superqq.SuperMainActivity;
import com.injoy.idg.R;
import com.superdata.im.entity.CxNotifyEntity;
import com.superdata.im.utils.CxNotificationUtils;
import com.utils.SPUtils;

import java.util.Map;

import de.greenrobot.event.EventBus;
import yunjing.processor.eventbus.UnReadMessage;

/**
 * Created by selson on 2017/9/4.
        */
    public class PushDaoHelper
    {
        private static SDUnreadEntity sdUnreadEntity;

        /**
         * total，总条数
         */
    public static void unReaderChange(Context context, int total, Map<String, String> map)
    {
        sdUnreadEntity = SDUnreadDao.getInstance().findByMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
        if (sdUnreadEntity == null)
        {
            sdUnreadEntity = new SDUnreadEntity();
            sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
//            sdUnreadEntity.setMenuCode(map.get("menuCode"));
//            sdUnreadEntity.setMenuName(map.get("menuName"));
            sdUnreadEntity.setBtype(Integer.parseInt(map.get("btype")));
            sdUnreadEntity.setRead(false);
            sdUnreadEntity.setUnreadCount(1);
            sdUnreadEntity.setType(Integer.parseInt(map.get("type")));
            sdUnreadEntity.setFromAccount(map.get("from"));
            sdUnreadEntity.setMsg(map.get("msg"));
            //二级目录

            SDUnreadDao.getInstance().save(sdUnreadEntity);
        } else
        {
            sdUnreadEntity.setMenuId(Integer.parseInt(map.get(SDUnreadDao.BTYPE)));
            sdUnreadEntity.setUnreadCount(total);
            sdUnreadEntity.setRead(false);
            sdUnreadEntity.setMsg(map.get("msg"));
            SDUnreadDao.getInstance().save(sdUnreadEntity);
        }

        //告诉所有，接收业务推送，把APP内红点标上
        EventBus.getDefault().post(new UnReadMessage(true,
                Integer.parseInt(map.get(SDUnreadDao.BTYPE)), total));
    }
}
