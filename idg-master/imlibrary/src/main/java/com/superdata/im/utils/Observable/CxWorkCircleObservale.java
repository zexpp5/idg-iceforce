package com.superdata.im.utils.Observable;

import java.util.Observable;

/**
 * 工作圈
 * 会话发布者
 */
public class CxWorkCircleObservale extends Observable
{
    private CxWorkCircleObservale()
    {

    }

    private static CxWorkCircleObservale cxWorkCircleObservale;
    public static CxWorkCircleObservale getInstance()
    {
        if (cxWorkCircleObservale == null)
        {
            cxWorkCircleObservale = new CxWorkCircleObservale();
        }
        return cxWorkCircleObservale;
    }

    public void sendWorkUnRead(int amount)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(amount);
    }
}
