package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxMessage;

import java.util.Observable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-04
 * @desc 用于注册im接收消息的观察者
 */
public class CxIMObservable extends Observable
{
    private CxIMObservable()
    {

    }

    private static CxIMObservable imObservable;

    public static CxIMObservable getInstance()
    {
        if (imObservable == null)
        {
            imObservable = new CxIMObservable();
        }
        return imObservable;
    }

    /**
     * 通知观察者,有新消息
     *
     * @param cxMessage
     */
    public void notifyUpdate(CxMessage cxMessage)
    {
        setChanged();
        notifyObservers(cxMessage);
    }

}
