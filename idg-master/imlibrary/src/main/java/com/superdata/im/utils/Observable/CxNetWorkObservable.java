package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxNetworkStatus;

import java.util.Observable;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-02-18
 * @desc
 */
public class CxNetWorkObservable extends Observable
{
    private CxNetWorkObservable()
    {
    }

    private static CxNetWorkObservable netWorkObservable;

    public static CxNetWorkObservable getInstance()
    {
        if (netWorkObservable == null)
        {
            netWorkObservable = new CxNetWorkObservable();
        }
        return netWorkObservable;
    }

    /**
     * 通知观察者,有新消息
     *
     * @param status
     */
    public void notifyUpdate(CxNetworkStatus status)
    {
        setChanged();
        notifyObservers(status);
    }


}
