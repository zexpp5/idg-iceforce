package com.cxgz.activity.cxim.support.handle;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;

import com.chaoxiang.base.utils.SDLogUtil;
import com.superdata.marketing.support.audio.SpeexDecoder;

import java.io.File;

/**
 * 用于播放录音的帮助类
 */
public class AudioPlayerHandler
{
    private String currentPlayPath = null;
    private SpeexDecoder speexdec = null;
    private Thread th = null;

    private static AudioPlayerHandler instance = null;

    private OnVoiceListener onVoiceListener;

    public static AudioPlayerHandler getInstance()
    {
        if (null == instance)
        {
            synchronized (AudioPlayerHandler.class)
            {
                instance = new AudioPlayerHandler();
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

    private AudioPlayerHandler()
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

    //停止播放后回调
    private void stopAnimation()
    {
        if (audioListener != null)
        {
            audioListener.onStop();
        }
    }

    //停止播放。
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
            stopAnimation();
        }
    }

    public boolean isPlaying()
    {
        return null != th;
    }

    public void startPlay(String filePath) throws Exception
    {
        this.currentPlayPath = filePath;
        speexdec = new SpeexDecoder(new File(this.currentPlayPath));
        RecordPlayThread rpt = new RecordPlayThread();
        if (null == th)
        {
            th = new Thread(rpt);
        }
        th.start();
    }

    class RecordPlayThread extends Thread
    {
        public void run()
        {
            try
            {
                if (null != speexdec)
                    speexdec.decode();
            } catch (Exception e)
            {
                SDLogUtil.debug(e.getMessage());
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

    public String getCurrentPlayPath()
    {
        return currentPlayPath;
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

    public interface OnVoiceListener
    {
        void playPositionChange(int currentPosition);

        void playCompletion();

        void playDuration(int duration);

        void playException(Exception e);

        void playStopVioce();
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
