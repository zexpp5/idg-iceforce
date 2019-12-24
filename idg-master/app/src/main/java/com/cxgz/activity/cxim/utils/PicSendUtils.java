package com.cxgz.activity.cxim.utils;

import android.app.Activity;

import com.chaoxiang.base.utils.DateUtils;
import com.chaoxiang.base.utils.MyToast;
import com.chaoxiang.base.utils.SDGson;
import com.chaoxiang.base.utils.SPUtils;
import com.chaoxiang.base.utils.StringUtils;
import com.chaoxiang.entity.IMDaoManager;
import com.cxgz.activity.cxim.ChatActivity;
import com.cxgz.activity.cxim.bean.PicBean;
import com.im.client.core.ChatManager;
import com.im.client.struct.IMMessageProtos;
import com.im.client.util.UUID;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.constants.CxSPIMKey;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.utils.CXMessageUtils;

/**
 * Created by selson on 2017/10/12.
 */

public class PicSendUtils
{

    private PicSendUtils()
    {
    }

    public static synchronized PicSendUtils getInstance()
    {
        return PicSendHelper.picSendUtils;
    }

    private static class PicSendHelper
    {
        private static final PicSendUtils picSendUtils = new PicSendUtils();
    }

    public void sendPic(Activity activity, CxMessage cxMessage)
    {
        String msgId = UUID.next();
        cxMessage.setType(CxIMMessageType.SINGLE_CHAT.getValue());
        PicBean picBean = SDGson.toObject(cxMessage.getBody(), PicBean.class);
        if (StringUtils.notEmpty(picBean.getRemoteUrl()))
            cxMessage.setStatus(CxIMMessageStatus.SUCCESS);

        long currenTimeMillis = System.currentTimeMillis();
        cxMessage.setCreateTime(DateUtils.getTimestampString(currenTimeMillis));
        cxMessage.setCreateTimeMillisecond(currenTimeMillis);

        saveToDb(activity, cxMessage, msgId);

        IMMessageProtos.IMMessage fileMsg = null;
        try
        {
            fileMsg = ChatManager.getInstance().buildSingleChatReq(cxMessage.getImMessage().getHeader()
                            .getTo(),
                    cxMessage.getBody(), cxMessage.getMediaType(), msgId, cxMessage.getImMessage().getHeader().getAttachment());
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        //发送的信息body.
        ChatManager.getInstance().sendMessage(fileMsg);

        if (cxMessage != null)
        {
            MyToast.showToast(activity, "转发成功");
        } else
        {
            MyToast.showToast(activity, "转发失败");
        }

        cxMessage.startWatcher();


    }

    public void saveToDb(Activity activity, CxMessage cxMessage, String msgId)
    {
        //保存到数据库
        String account = (String) SPUtils.get(activity, CxSPIMKey.STRING_ACCOUNT, "");
        if (StringUtils.notEmpty(account))
        {
            cxMessage.getImMessage().getHeader().setFrom(account);
            cxMessage.getImMessage().getHeader().setMessageId(msgId);
            try
            {
                IMDaoManager.getInstance().getDaoSession().getIMMessageDao().insert(CXMessageUtils.convertCXMessage(cxMessage));
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        } else
        {

        }


    }

}
