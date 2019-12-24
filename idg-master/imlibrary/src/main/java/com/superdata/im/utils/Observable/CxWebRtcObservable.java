package com.superdata.im.utils.Observable;

import com.superdata.im.entity.CxMessage;

import java.util.Observable;

/**
 * @auth lwj
 * @date 2016-01-30
 * @desc
 */
public class CxWebRtcObservable extends Observable {
    private CxWebRtcObservable(){}
    private static CxWebRtcObservable imObservable;

    public static CxWebRtcObservable getInstance(){
        if(imObservable == null){
            imObservable = new CxWebRtcObservable();
        }
        return imObservable;
    }

    /**
     * 通知观察者,有新消息
     * @param cxMessage
     */
    public void notifyUpdate(CxMessage cxMessage){
        setChanged();
        notifyObservers(cxMessage);
    }
}
