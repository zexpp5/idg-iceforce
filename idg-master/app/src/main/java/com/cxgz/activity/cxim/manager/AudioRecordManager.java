package com.cxgz.activity.cxim.manager;

import android.os.Handler;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.support.handle.AudioRecordHandler;
import com.chaoxiang.base.config.Constants;

import java.io.File;
import java.util.UUID;

/**
 * @version v1.0.0
 * @authon zjh
 * @date 2016-01-25
 * @desc 语音播放管理
 */
public class AudioRecordManager
{
    private static AudioRecordManager audioRecordManager;
    private VoiceRecordListener recordListener;

    private AudioRecordManager()
    {
        initHandle();
    }

    public static Handler micImageHandler = null;
    private AudioRecordHandler audioRecorderInstance = null;
    private File audioSaveFile;
    private Thread audioRecorderThread = null;

    public static AudioRecordManager getInstance()
    {
        if (audioRecordManager == null)
        {
            audioRecordManager = new AudioRecordManager();
        }
        return audioRecordManager;
    }


    /**
     * @param voiceValue
     * @Description 根据分贝值设置录音时的音量动画
     */
    private void onReceiveMaxVolume(int voiceValue)
    {
        if (recordListener != null)
        {
            recordListener.onReceiveMaxVolume(voiceValue);
        }
    }

    private void initHandle()
    {
        micImageHandler = new Handler()
        {
            @Override
            public void handleMessage(android.os.Message msg)
            {
                switch (msg.what)
                {
                    case Constants.RECEIVE_MAX_VOLUME:
                        onReceiveMaxVolume((Integer) msg.obj);
                        break;
                    case Constants.RECORD_AUDIO_TOO_LONG:
                        doFinishRecordAudio();
                        break;
                }
            }
        };
    }

    /**
     * @Description 录音超时，发消息调用该方法
     */
    public void doFinishRecordAudio()
    {
        try
        {
            if (audioRecorderInstance.isRecording())
            {
                audioRecorderInstance.setRecording(false);
            }
            audioRecorderInstance.setRecordTime(Config.MAX_SOUND_RECORD_TIME);

            onRecordVoiceEnd();
        } catch (Exception e)
        {
        }
    }

    /**
     * @Description 录音超时
     */
    private void onRecordVoiceEnd()
    {
        if (recordListener != null)
        {
            recordListener.onOverTimerRecord(audioSaveFile.getAbsolutePath(), (int) Config.MAX_SOUND_RECORD_TIME);
        }
    }

    /**
     * 录音结束
     */
    public void stopRecord()
    {
        if (audioRecorderInstance.isRecording())
        {
            audioRecorderInstance.setRecording(false);
            recordListener.onFinishRecord(audioSaveFile.getAbsolutePath(), (int) audioRecorderInstance.getRecordTime());
        }
    }


    /**
     * 开始录音
     */
    public void startRecoder()
    {
        if (recordListener != null)
        {
            recordListener.onStartRecord();
        }
        audioSaveFile = new File(getSaveDir(), UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis() + ".ogg");
        audioRecorderInstance = new AudioRecordHandler(micImageHandler, audioSaveFile.getAbsolutePath());
        audioRecorderInstance.setAudioCallback(new AudioRecordHandler.AudioCallback()
        {
            @Override
            public void RecorderTooLong()
            {
                SDLogUtil.debug("im_录音时间过长：超出60秒");
            }

            @Override
            public void getMaxAmplitude(int volume)
            {

            }
        });
        audioRecorderThread = new Thread(audioRecorderInstance);
        audioRecorderInstance.setRecording(true);
        audioRecorderThread.start();
    }

    /**
     * 获取缓存目录
     *
     * @return
     */
    public File getSaveDir()
    {
        File saveDir = new File(Config.CACHE_VOICE_DIR);
        if (!saveDir.exists())
        {
            saveDir.mkdirs();
        }
        return saveDir;
    }

    /**
     * 录音回调
     */
    public interface VoiceRecordListener
    {
        void onStartRecord();

        void onReceiveMaxVolume(int voiceValue);

        void onOverTimerRecord(String path, int length);

        void onFinishRecord(String path, int length);
    }

    public VoiceRecordListener getRecordListener()
    {
        return recordListener;
    }

    public void setRecordListener(VoiceRecordListener recordListener)
    {
        this.recordListener = recordListener;
    }
}
