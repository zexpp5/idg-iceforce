package com.superdata.im.handle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.chaoxiang.base.utils.FileUtils;
import com.im.client.struct.IMMessage;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;
import com.superdata.im.manager.CxDispatcherManager;
import com.superdata.im.processor.CxIProcessor;

import java.util.Map;

import static android.R.id.message;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2016/1/4
 * @desc 消息处理, 分发消息到各个处理器
 */
public class CxDoMsgHandle extends Handler
{
    public static final int DO_IM_MSG = 1001;

    public static final String MSG_DATA = "msg_data";
    public static final String MSG_STATUS = "msg_status";
    public static final String MSG_TYPE = "msg_type";
    public static int STATUS_SUCCESS = 0;
    public static int STATUS_ERROR = 1;

    @Override
    public void handleMessage(Message msg)
    {
        if (msg.what == DO_IM_MSG)
        {   
            Bundle data = msg.getData();
            int status = data.getInt(MSG_STATUS);
            int plushType = data.getInt(MSG_TYPE, 0);
            IMMessage imMessage = (IMMessage) data.get(MSG_DATA);
            CxMessage cxMessage = new CxMessage(imMessage);

            String tmpString = "im-广播CxDoMsgHandle=发送队列= " + cxMessage.getImMessage().getBody();
            FileUtils.getInstance().writeFileToSDCard(tmpString.getBytes(), null, "app_im_IDGServer.txt", true,
                    true);

            if (imMessage.getHeader() != null)
            {
                cxMessage.setMediaType(imMessage.getHeader().getMediaType());
            }
            cxMessage.setType(plushType);
            for (Map.Entry<CxIMMessageType, CxIProcessor> entry : CxDispatcherManager.getInstance().getMap().entrySet())
            {
                int type = entry.getKey().getValue();
                if (status == STATUS_SUCCESS)
                {
                    cxMessage.setStatus(CxIMMessageStatus.SUCCESS);
                    if (plushType == type)
                    {
                        entry.getValue().doMsg(cxMessage);
                        break;
                    }
                } else if (status == STATUS_ERROR)
                {
                    cxMessage.setStatus(CxIMMessageStatus.FAIL);
                    if (entry.getValue().doErrorMsg(cxMessage))
                    {
                        break;
                    }
                }

            }

        }
    }
}
