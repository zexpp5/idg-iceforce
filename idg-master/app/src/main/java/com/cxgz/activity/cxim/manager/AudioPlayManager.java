package com.cxgz.activity.cxim.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import com.chaoxiang.base.config.Config;
import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.cxim.support.handle.AudioPlayerHandler;

import java.io.File;

/**
 * @author zjh
 */
@SuppressLint("HandlerLeak")
public class AudioPlayManager
{
    private AudioPlayManager()
    {

    }

    public static AudioPlayManager playManager;
    private AnimationDrawable animationDrawable;
    private final int VOICE_WHATE = 1001;
    private OnVoiceListener onVoiceListener;
    private AudioPlayerHandler audioPlayerHandler;
    /**
     * 动画
     */
    private ImageView animationView;
    private ScreenLightManager screenLightManager;
    private AudioManager am;
    private int lastDuration = -1;

    /**
     * 播放路径
     */
    private String playPath;

    /**
     * 播放时间
     */
    private Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == 1)
            {
                try
                {
                    doPlayFinish();
                } catch (Exception e)
                {

                }
            }
        }
    };

    /**
     * 播放时间
     */
    private Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            try
            {
                int currentPosition = audioPlayerHandler.getCurrentPostion();
                if (msg.what == VOICE_WHATE)
                {
                    if (onVoiceListener != null)
                    {
                        lastDuration = currentPosition;
                        onVoiceListener.playPositionChange(currentPosition);
                        int totalTime = audioPlayerHandler.getTotalTime();
                        if (totalTime <= 0)
                        {
                            onVoiceListener.playException(new Exception("voice damage"));
                            throw new Exception("voice damage");
                        }
                        onVoiceListener.playDuration(audioPlayerHandler.getTotalTime());
                        if (animationDrawable != null && !animationDrawable.isRunning())
                        {
                            animationDrawable.start();
                        }
                        if (currentPosition == audioPlayerHandler.getTotalTime())
                        {
                            doPlayFinish();
                        }
                    }
                    postCurrentPosition();
                }
            } catch (Exception e)
            {
                doPlayFinish();
            }
        }
    };


    private void doPlayFinish()
    {
        SDLogUtil.debug("im_测试_playVoice停止的语音播放55555！");
        close();
        recoveryScreenLight();
        if (onVoiceListener != null)
        {
            onVoiceListener.playCompletion();
        }
    }

    /**
     * 恢复屏幕亮度
     */
    private void recoveryScreenLight()
    {
        if (screenLightManager != null)
        {
            if (screenLightManager.getInitBrighLight() != screenLightManager.getScreenBrightness())
            {
                screenLightManager.turnOnScreen();
            }
        }
    }

    //停止动画
    private void stopAnimation()
    {
        if (animationView != null && animationDrawable != null && animationDrawable.isRunning())
        {
            SDLogUtil.debug("im_voice_AudioPlayerManager—animationDrawable—停止的语音播放！");
            animationDrawable.selectDrawable(0);
            animationDrawable.stop();
        }
    }

    public static AudioPlayManager getInstance()
    {
        if (playManager == null)
        {
            playManager = new AudioPlayManager();
        }
        return playManager;
    }

    AudioManager.OnAudioFocusChangeListener afChangeListener = new AudioManager.OnAudioFocusChangeListener()
    {
        @Override
        public void onAudioFocusChange(int focusChange)
        {
            //该回调吾需实现,因为不存在其他应用与我们应用墙焦点的情况
//            switch (focusChange){
//                case AudioManager.AUDIOFOCUS_GAIN:
//                    //获取到焦点
//                    SDLogUtil.debug("获取到焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
//                    //暂时获取到焦点
//                    SDLogUtil.debug("暂时获取到焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS:
//                    //失去焦点
//                    SDLogUtil.debug("失去焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
//                    //暂时失去焦点
//                    SDLogUtil.debug("暂时失去焦点");
//                    break;
//                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
//                    //暂时失去焦点但可继续播放
//                    SDLogUtil.debug("暂时失去焦点但可继续播放");
//                    break;
//            }
        }
    };

    /**
     * 播放录音
     *
     * @param context
     * @param path
     * @param imageView
     */
    public synchronized void play(final Context context, final String path, final ImageView imageView)
    {
        this.playPath = path;
        if (audioPlayerHandler == null)
        {
            audioPlayerHandler = AudioPlayerHandler.getInstance();
            audioPlayerHandler.setAudioMode(AudioManager.MODE_NORMAL, context);
            am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            am.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            animationView = imageView;

            audioPlayerHandler.setAudioListener(new AudioPlayerHandler.AudioListener()
            {
                @Override
                public void onStop()
                {
                    SDLogUtil.debug("im_voice_AudioPlayManager——停止的语音播放！");
                    SDLogUtil.debug("im_测试_stopPlayer停止的语音播放6666666！");
                    onVoiceListener.playStopVioce();
                    //此处应该停止语音动画
                    stopAnimation();
                    //imageView.setImageResource(R.mipmap.voice_img_bg);
                }
            });
        } else
        {
            if (audioPlayerHandler.isPlaying())
            {
                SDLogUtil.debug("im_测试_stopPlayer停止的语音播放222222！");
                audioPlayerHandler.stopPlayer();
            }
        }
        if (imageView != null)
        {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof AnimationDrawable)
            {
                animationDrawable = (AnimationDrawable) drawable;
            }
            if (animationDrawable != null)
            {
                if (animationDrawable.isRunning())
                {
                    animationDrawable.stop();
                }
                animationDrawable.setOneShot(false);
                animationDrawable.start();
            }
        }

        if (path.startsWith("http"))
        {
            String locaPath = Config.CACHE_VOICE_DIR + File.separator + path.substring(path.lastIndexOf("/") + 1);
            File file = new File(locaPath);
            if (file.exists())
            {
                SDLogUtil.debug("im_测试_playVoice的语音播放44444！");
                playVoice(context, path);
            }
        } else
        {
            SDLogUtil.debug("im_测试_playVoice的语音播放44444！");
            playVoice(context, path);
        }
    }

    private void playVoice(final Context context, final String path)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Thread.sleep(500);
                    audioPlayerHandler.startPlay(path);
                    postCurrentPosition();
                } catch (Exception e)
                {
                    e.printStackTrace();
                    Looper.prepare();
                    Looper.loop();
                    close();
                }
            }
        }).start();
    }

    //外部调用停止播放
    public void stopOut()
    {
        if (audioPlayerHandler != null)
        {
            audioPlayerHandler.stopPlayer();
            if (onVoiceListener != null)
            {
                onVoiceListener.playCompletion();
            }
        }
    }

    //外部调用停止播放
    public void stop()
    {
        if (audioPlayerHandler != null)
        {
            audioPlayerHandler.stopPlayer();
        }
    }

    public void close()
    {
        if (audioPlayerHandler != null)
        {
            audioPlayerHandler.clear();
        }
        if (screenLightManager != null)
        {
            screenLightManager.removeBlackView();
        }
        if (am != null)
        {
            am.abandonAudioFocus(afChangeListener);
            am = null;
        }
        stopAnimation();
    }

    /**
     * 发送当前播放位置到消息队列
     */
    private void postCurrentPosition()
    {
        if (audioPlayerHandler.isPlaying())
        {
            handler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if (audioPlayerHandler != null)
                    {
                        Message message = Message.obtain();
                        message.what = VOICE_WHATE;
                        handler.sendMessage(message);
                    }
                }
            }, 100);
        }
    }

    /**
     * 屏幕贴近耳朵时转非扩音播放
     */
    public void sensorChanged(Context context, Sensor sensor, SensorEvent sensorEvent, ScreenLightManager screenLightManager)
    {
        try
        {
            if (audioPlayerHandler == null || !audioPlayerHandler.isPlaying())
            {
                return;
            }
            this.screenLightManager = screenLightManager;
            float range = sensorEvent.values[0];
            if (null != sensor && range >= 5.0)
            {
                // 屏幕恢复亮度
                screenLightManager.turnOnScreen();
                audioPlayerHandler.setAudioMode(AudioManager.MODE_NORMAL, context);
            } else
            {
                // 屏幕变黑
                screenLightManager.turnOffScreen();
                audioPlayerHandler.setAudioMode(AudioManager.MODE_IN_CALL, context);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
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

    public boolean isPlaying()
    {
        return audioPlayerHandler.isPlaying();
    }

    public String getPlayPath()
    {
        return playPath;
    }

    public AudioPlayerHandler getAudioPlayerHandler()
    {
        return audioPlayerHandler;
    }
}
