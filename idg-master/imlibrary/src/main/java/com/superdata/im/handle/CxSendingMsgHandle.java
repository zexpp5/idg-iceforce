package com.superdata.im.handle;

import android.os.Handler;
import android.os.Message;

import com.superdata.im.entity.CxMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-03-05
 * @desc 用于解决消息在发送中被异常退出无法更新状态问题
 */
public class CxSendingMsgHandle extends Handler
{
    private static CxSendingMsgHandle instance;

    private CxSendingMsgHandle()
    {
    }

    private List<CxMessage> msgList = new ArrayList<>();
    public final static int ADD = 1;
    public final static int REMOVE = 2;


    public static CxSendingMsgHandle getInstance()
    {
        if (instance == null)
        {
            instance = new CxSendingMsgHandle();
        }
        return instance;
    }

    @Override
    public void handleMessage(Message msg)
    {
        CxMessage cxMessage = (CxMessage) msg.obj;
        if (cxMessage != null)
        {
            if (msg.what == ADD)
            {
                msgList.add(cxMessage);
            } else if (msg.what == REMOVE)
            {
                msgList.remove(cxMessage);
            }
        }
    }


    public List<CxMessage> getMsgList()
    {
        return msgList;
    }


    /**
     * 创建并发送一条消息
     * @param cxMessage
     * @param what
     */
    public void createAndSendMsg(CxMessage cxMessage, int what)
    {
        Message message = obtainMessage();
        message.obj = cxMessage;
        message.what = what;
        sendMessage(message);
    }
}
