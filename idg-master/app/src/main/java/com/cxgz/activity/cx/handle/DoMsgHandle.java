package com.cxgz.activity.cx.handle;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cxgz.activity.cx.manager.DispatcherManager;
import com.cxgz.activity.cx.processor.IProcessor;
import com.im.client.struct.IMMessage;

import java.io.Serializable;
import java.util.Map;

/**
 * @desc 消息处理队里, 分发消息到各个处理器
 */
public class DoMsgHandle extends Handler
{
    public static final int DO_IM_MSG = 1001;

    public static final String MSG_DATA = "msg_data";
    public static final String MSG_STATUS = "msg_status";
    public static final String MSG_TYPE = "msg_type";
    public static int STATUS_SUCCESS = 0;
    public static int STATUS_ERROR = 1;

    public enum Type implements Serializable
    {
        PUSH(1),
        OFFLINE_PUSH(3),
        LOGIN(2),
        SINGLE_CHAT(6);

        private int value;

        Type(int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }
    }

    @Override
    public void handleMessage(Message msg)
    {
        if (msg.what == DO_IM_MSG)
        {
            Bundle data = msg.getData();
            int status = data.getInt(MSG_STATUS);
            int plushType = data.getInt(MSG_TYPE, 0);
            IMMessage imMessage = (IMMessage) data.get(MSG_DATA);
            for (Map.Entry<Type, IProcessor> entry : DispatcherManager.newInstance().getMap().entrySet())
            {
                int type = entry.getKey().getValue();
                if (status == STATUS_SUCCESS)
                {
                    if (plushType == type)
                    {
                        entry.getValue().doMsg(imMessage);
                        break;
                    }
                } else if (status == STATUS_ERROR)
                {
                    if (!entry.getValue().doErrorMsg(imMessage))
                    {
                        break;
                    }
                }

            }
        }
    }
}
