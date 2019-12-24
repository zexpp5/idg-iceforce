package com.superdata.im.utils.Observable;

import com.chaoxiang.entity.chat.IMVoiceGroup;

import java.util.Observable;
import java.util.Observer;

/**
 * @des
 */
public class CxVoiceMeettingSubscribe implements Observer
{
    private VoiceMeettingListener voiceMeettingListener;

    public CxVoiceMeettingSubscribe(VoiceMeettingListener listener)
    {
        this.voiceMeettingListener = listener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof IMVoiceGroup)
            {
                if (voiceMeettingListener != null)
                {
                    voiceMeettingListener.acceptVoiceMeetting((IMVoiceGroup) data);
                }
            }
        }
    }
    
    public interface VoiceMeettingListener
    {
        void acceptVoiceMeetting(IMVoiceGroup iMVoiceGroup);
    }
}
