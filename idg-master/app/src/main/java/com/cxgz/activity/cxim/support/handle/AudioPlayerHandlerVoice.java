package com.cxgz.activity.cxim.support.handle;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.marketing.support.audio.SpeexDecoder;

import java.io.File;


import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.marketing.support.audio.SpeexDecoder;

import java.io.File;

/**
 * User: Selson
 * Date: 2016-05-23
 * FIXME
 */
public class AudioPlayerHandlerVoice
{
    private String currentPlayPath = null;
    private SpeexDecoder speexdec = null;
    private Thread th = null;

    private static AudioPlayerHandlerVoice instance = null;

    private OnVoiceListener onVoiceListener;

    public static AudioPlayerHandlerVoice getInstance()
    {
        if (null == instance)
        {
            synchronized (AudioPlayerHandler.class)
            {
                instance = new AudioPlayerHandlerVoice();
            }
        }
        return instance;
    }

    //语音播放的模式
    public void setAudioMode(int mode, Context ctx)
    {
        if (mode != AudioManager.MODE_NORMAL && mode != AudioManager.MODE_IN_CALL)
        {
            return;
        }
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(mode);
    }

    /**
     * messagePop调用
     */
    public int getAudioMode(Context ctx)
    {
        AudioManager audioManager = (AudioManager) ctx.getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getMode();
    }

    public void clear()
    {
        if (isPlaying())
        {
            stopPlayer();
        }
        instance = null;
    }

    private AudioPlayerHandlerVoice()
    {

    }

    /**
     * yingmu modify
     * speexdec 由于线程模型
     */
    public interface AudioListener
    {
        void onStop();
    }

    private AudioListener audioListener;

    public void setAudioListener(AudioListener audioListener)
    {
        this.audioListener = audioListener;
    }

    private void stopAnimation()
    {
        if (audioListener != null)
        {
            audioListener.onStop();
        }
    }

    public void stopPlayer()
    {
        try
        {
            if (null != th)
            {
                th.interrupt();
                th = null;
                Thread.currentThread().interrupt();
            } else
            {

            }
        } catch (Exception e)
        {
            SDLogUtil.debug(e.getMessage());
        } finally
        {
//            stopAnimation();
        }
    }

    public Handler myHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what)
            {
                case 1:
                    SDLogUtil.debug("speex--AudioPlayerHandler的myHandler方法内线程ID" + Thread.currentThread().getId());
                    onVoiceListener.playCompletion();
                    break;
            }
        }
    };

    //线程是否存在
    public boolean isPlaying()
    {
        return null != th;
    }

    public void startPlay(String filePath) throws Exception
    {
        this.currentPlayPath = filePath;
        speexdec = new SpeexDecoder(new File(this.currentPlayPath), myHandler);
        SDLogUtil.debug("speex--AudioPlayerHandler的startPlay方法内线程ID" + Thread.currentThread().getId());
        RecordPlayThread rpt = new RecordPlayThread();
        if (null == th)
            th = new Thread(rpt);
        th.start();
    }

    public SpeexDecoder getSpeexdec()
    {
        return this.speexdec;
    }


    class RecordPlayThread extends Thread
    {
        public void run()
        {
            try
            {
                if (null != speexdec)
                {
                    SDLogUtil.debug("speex--AudioPlayerHandler的RecordPlayThread内线程ID" + Thread.currentThread().getId());
                    speexdec.decode();
                }
            } catch (Exception e)
            {
                stopAnimation();
            }
        }
    }

    public int getCurrentPostion()
    {
        return speexdec.getCurrentPostion();
    }

    public int getTotalTime()
    {
        return speexdec.getTotalTime();
    }

    public void setPaused(boolean isPaused)
    {
        speexdec.setPaused(isPaused);
    }

    //获取暂停状态
    public boolean getPaused()
    {
        return speexdec.isPaused();
    }

    public String getCurrentPlayPath()
    {
        return currentPlayPath;
    }

    public interface OnVoiceListener
    {
        //        void playPositionChange(int currentPosition);
        void playCompletion();
//        void playDuration(int duration);
//        void playException(Exception e);
    }

    public OnVoiceListener getOnVoiceListener()
    {
        return onVoiceListener;
    }

    public void setOnVoiceListener(OnVoiceListener onVoiceListener)
    {
        this.onVoiceListener = onVoiceListener;
    }
}
