package com.superdata.im.utils.Observable;

import com.chaoxiang.entity.chat.IMVoiceGroup;

import java.util.Observable;

/**
 * 会话发布者
 */
public class CxVoiceMeettingObservale extends Observable
{

    public CxVoiceMeettingObservale() {
    }

    private static CxVoiceMeettingObservale cxVoiceMeettingObservale;

    public static CxVoiceMeettingObservale getInstance()
    {
        if (cxVoiceMeettingObservale == null)
        {
            cxVoiceMeettingObservale = new CxVoiceMeettingObservale();
        }
        return cxVoiceMeettingObservale;
    }

    public void sendVoiceMeettingInfo(IMVoiceGroup iMVoiceGroup)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(iMVoiceGroup);
    }

}
