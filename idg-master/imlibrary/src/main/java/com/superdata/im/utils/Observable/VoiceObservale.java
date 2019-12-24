package com.superdata.im.utils.Observable;

import java.util.Observable;

/**
 * @des
 */
public class VoiceObservale extends Observable
{
    private VoiceObservale()
    {

    }

    private static VoiceObservale voiceObservale;

    public static VoiceObservale getInstance()
    {
        if (voiceObservale == null)
        {
            voiceObservale = new VoiceObservale();
        }
        return voiceObservale;
    }

    public void sendGroupMsg(String groupId)
    {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(groupId);
    }
}
