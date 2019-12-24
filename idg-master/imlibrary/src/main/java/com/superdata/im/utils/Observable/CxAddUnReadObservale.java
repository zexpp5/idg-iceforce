package com.superdata.im.utils.Observable;

import java.util.Observable;

/**
 *会话发布者
 */
public class CxAddUnReadObservale extends Observable
{
    private CxAddUnReadObservale()
    {

    }

    private static CxAddUnReadObservale cxAddUnReadObservale;

    public static CxAddUnReadObservale getInstance()
    {
        if (cxAddUnReadObservale == null)
        {
            cxAddUnReadObservale = new CxAddUnReadObservale();
        }
        return cxAddUnReadObservale;
    }

    public void sendAddUnRead(int addUnReadStatus)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(addUnReadStatus);
    }
}
