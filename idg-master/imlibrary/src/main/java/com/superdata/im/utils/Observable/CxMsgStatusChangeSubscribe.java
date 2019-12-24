package com.superdata.im.utils.Observable;

import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageStatus;
import com.superdata.im.entity.CxMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-08
 * @desc 状态发生改变订阅者者
 */
public class CxMsgStatusChangeSubscribe implements Observer
{
    private MsgStatusChangeCallback callback;

    public CxMsgStatusChangeSubscribe(MsgStatusChangeCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data instanceof CxMessage)
        {
            CxMessage cxMessage = (CxMessage) data;
            if (cxMessage.getDirect() == CxIMMessageDirect.SEND)
            {
                if (callback != null)
                {
                    if (cxMessage.getStatus() != CxIMMessageStatus.SENDING)
                    {
                        callback.onMsgStatusChange(cxMessage);
                    }
                }
            }
        }
    }


    public interface MsgStatusChangeCallback
    {
        void onMsgStatusChange(CxMessage cxMessage);
    }
}
