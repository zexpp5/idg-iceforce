package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxWrapperConverstaion;

import java.util.Observable;
import java.util.Observer;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-12
 * @desc 会话订阅者
 */
public class CxConversationSubscribe implements Observer
{
    private ConversationChangeCallback callback;

    public CxConversationSubscribe(ConversationChangeCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data instanceof CxWrapperConverstaion)
        {
            if (callback != null)
            {
                callback.onChange((CxWrapperConverstaion) data);
            }
        }
    }

    public interface ConversationChangeCallback
    {
        void onChange(CxWrapperConverstaion conversation);
    }
}
