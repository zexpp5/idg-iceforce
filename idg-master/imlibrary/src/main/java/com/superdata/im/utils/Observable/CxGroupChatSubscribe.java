package com.superdata.im.utils.Observable;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.im.callback.CxUpdateUICallback;
import com.superdata.im.constants.CxIMMessageDirect;
import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.entity.CxMessage;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 群聊订阅者
 */
public class CxGroupChatSubscribe implements Observer
{
    private CxUpdateUICallback callback;

    public CxGroupChatSubscribe(CxUpdateUICallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data instanceof CxMessage)
        {
            CxMessage cxMessage = (CxMessage) data;
            if (cxMessage.getType() == CxIMMessageType.GROUP_CHAT.getValue() && cxMessage.getDirect() == CxIMMessageDirect.RECEIVER)
            {
                dealData();
                if (callback != null)
                {
                    callback.updateUI(cxMessage);
                }
            }
        }
    }

    /**
     * 处理数据
     */
    private void dealData()
    {
        //TODO 处理数据
    }
}
