package com.superdata.im.utils.Observable;

import java.util.Observable;
import java.util.Observer;

/**
 * @author 小黎
 * @date 2016/1/11 10:03
 * @des
 */
public class VoiceObserver implements Observer
{
    private VoiceListener voiceListener;

    public VoiceObserver(VoiceListener listener)
    {
        this.voiceListener = listener;
    }

    @Override
    public void update(Observable observable, Object o)
    {
        if (o != null)
        {
            if (o instanceof String)
            {
                if (voiceListener != null)
                {
                    voiceListener.finishVoice((String) o);
                }
            }
        }
    }

    public interface VoiceListener
    {
        void finishVoice(String groupId);
    }
}
