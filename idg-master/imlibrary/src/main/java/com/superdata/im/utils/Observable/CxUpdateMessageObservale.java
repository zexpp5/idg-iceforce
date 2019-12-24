package com.superdata.im.utils.Observable;

import java.util.Observable;

/**
 * User: Selson
 * Date: 2016-09-13
 * FIXME  更新聊天界面的。未读发布者
 */
public class CxUpdateMessageObservale extends Observable
{
    private CxUpdateMessageObservale()
    {

    }

    private static CxUpdateMessageObservale cxUpdateMessageObservale;

    public static CxUpdateMessageObservale getInstance()
    {
        if (cxUpdateMessageObservale == null)
        {
            cxUpdateMessageObservale = new CxUpdateMessageObservale();
        }
        return cxUpdateMessageObservale;
    }

    public void sendUpdateMessageRead(int updateMessageRead)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(updateMessageRead);
    }

} 