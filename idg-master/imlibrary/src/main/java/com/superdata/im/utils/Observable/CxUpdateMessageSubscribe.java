package com.superdata.im.utils.Observable;

import java.util.Observable;
import java.util.Observer;

/**
 * User: Selson
 * Date: 2016-09-13
 * FIXME  未读观察者
 */
public class CxUpdateMessageSubscribe implements Observer
{
    private UpdateMessageListener updateMessageListener;

    public CxUpdateMessageSubscribe(UpdateMessageListener listener)
    {
        this.updateMessageListener = listener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof Integer)
            {
                if (updateMessageListener != null)
                {
                    updateMessageListener.updateMessageRead((int) data);
                }
            }
        }
    }

    public interface UpdateMessageListener
    {
        void updateMessageRead(int updateMessage);
    }
}